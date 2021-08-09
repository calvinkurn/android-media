package com.tokopedia.minicart.chatlist

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.minicart.cartlist.MiniCartListDecoration
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory
import com.tokopedia.minicart.chatlist.adapter.MiniCartChatListAdapter
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.chatlist.viewholder.MiniCartChatProductViewHolder
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.databinding.LayoutBottomsheetMiniCartChatListBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.R
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class MiniCartChatListBottomSheet @Inject constructor(
    private var miniCartListDecoration: MiniCartListDecoration
) : MiniCartChatProductViewHolder.ChatProductListener {
    private var viewBinding: LayoutBottomsheetMiniCartChatListBinding? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var adapter: MiniCartChatListAdapter? = null
    private var viewModel: MiniCartViewModel? = null
    private var bottomSheetUiModelObserver: Observer<MiniCartListUiModel>? = null
    private var mContext: Context? = null
    private var miniCartListUiModel: MiniCartListUiModel? = null
    private var elements: ArrayList<MiniCartChatProductUiModel> = arrayListOf()

    override fun onClickProduct(element: MiniCartChatProductUiModel, isChecked: Boolean) {
        mContext?.apply {
            element.isChecked = isChecked
            if (isChecked) {
                if (elements.size <= 3) {
                    elements.add(element)
                    if (elements.size == 3) {
                        miniCartListUiModel?.chatVisitables?.forEach { model ->
                            if (model is MiniCartChatProductUiModel) {
                                if (model.productId == element.productId) {
                                    model.isChecked = element.isChecked
                                    model.size = elements.size
                                } else {
                                    model.size = elements.size
                                }
                            }
                        }
                        miniCartListUiModel?.chatVisitables?.toMutableList()?.let {
                            adapter?.updateList(it)
                        }
                    }
                }
                viewBinding?.btnChat?.text = getString(com.tokopedia.minicart.R.string.mini_cart_chat_btn_label_ask_product, elements.size)
            } else {
                elements.remove(element)
                if (elements.size == 0) {
                    viewBinding?.btnChat?.text = getString(com.tokopedia.minicart.R.string.mini_cart_chat_btn_label)
                } else {
                    viewBinding?.btnChat?.text = getString(com.tokopedia.minicart.R.string.mini_cart_chat_btn_label_ask_product, elements.size)
                    miniCartListUiModel?.chatVisitables?.forEach { model ->
                        if (model is MiniCartChatProductUiModel) {
                            if (model.productId == element.productId) {
                                model.isChecked = element.isChecked
                                model.size = elements.size
                            } else {
                                model.size = elements.size
                            }
                        }
                    }
                    miniCartListUiModel?.chatVisitables?.toMutableList()?.let {
                        adapter?.updateList(it)
                    }
                }
            }
        }
    }

    fun show(context: Context?,
             fragmentManager: FragmentManager,
             lifecycleOwner: LifecycleOwner,
             viewModel: MiniCartViewModel,
    ) {
        context?.let {
            mContext = context
            viewBinding = LayoutBottomsheetMiniCartChatListBinding.inflate(LayoutInflater.from(context)).apply {
                initializeView(this, fragmentManager)
                initializeViewModel(this, viewModel, lifecycleOwner)
                initializeCartData(viewModel)
            }
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }

    private fun resetObserver() {
        bottomSheetUiModelObserver?.let {
            viewModel?.miniCartChatListBottomSheetUiModel?.removeObserver(it)
            bottomSheetUiModelObserver = null
        }
    }

    private fun initializeBottomSheetUiModelObserver(viewBinding: LayoutBottomsheetMiniCartChatListBinding) {
        bottomSheetUiModelObserver = Observer<MiniCartListUiModel> {
            miniCartListUiModel = it
            hideLoading()
            mContext?.apply {
                bottomSheet?.setTitle(getString(com.tokopedia.minicart.R.string.mini_cart_chat_bottomsheet_title_label))
                viewBinding.btnChat.setDrawable(getIconUnifyDrawable(this, IconUnify.CHAT, ContextCompat.getColor(this, R.color.Unify_NN0)))
                viewBinding.btnChat.text = getString(com.tokopedia.minicart.R.string.mini_cart_chat_btn_label)
                viewBinding.btnChat.setOnClickListener {
                    val shopId = viewModel?.currentShopIds?.value?.firstOrNull().orEmpty()
                    if (elements.isNullOrEmpty()) {
                        val intent = RouteManager.getIntent(mContext, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId)
                        mContext?.startActivity(intent)
                    } else {
                        val productPreviews = mutableListOf<ProductPreview>()
                        elements.forEach {
                            val productPreview = ProductPreview(
                                id = it.productId,
                                imageUrl = it.productImageUrl,
                                name = it.productName,
                                price = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, false),
                                dropPercentage = "${it.productCashbackPercentage}%",
                                remainingStock = it.productQtyLeft.toIntOrZero(),
                                priceBeforeInt = it.productInitialPriceBeforeDrop.toDouble(),
                                priceBefore = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, false),
                            )
                            productPreviews.add(productPreview)
                        }
                        val intent = RouteManager.getIntent(mContext, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId)
                        val stringProductPreviews = CommonUtil.toJson(productPreviews)
                        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
                        startActivity(intent)
                    }
                }
            }
            if (viewBinding.rvMiniCartChatList.isComputingLayout) {
                viewBinding.rvMiniCartChatList.post {
                    adapter?.updateList(it.chatVisitables)
                }
            } else {
                adapter?.updateList(it.chatVisitables)
            }
            if (it.isFirstLoad) {
                it.isFirstLoad = false
            }
        }
    }

    private fun initializeView(viewBinding: LayoutBottomsheetMiniCartChatListBinding, fragmentManager: FragmentManager) {
        initializeBottomSheet(viewBinding, fragmentManager)
        initializeRecyclerView(viewBinding)
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomsheetMiniCartChatListBinding, fragmentManager: FragmentManager) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = false
            showHeader = true
            isDragable = true
            showKnob = true
            isHideable = true
            clearContentPadding = true
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
            setOnDismissListener {
                resetObserver()
                elements.clear()
                this@MiniCartChatListBottomSheet.viewBinding = null
            }
            setChild(viewBinding.root)
            show(fragmentManager, this.javaClass.simpleName)
        }
    }

    private fun initializeViewModel(viewBinding: LayoutBottomsheetMiniCartChatListBinding, viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        this.viewModel = viewModel
        initializeBottomSheetUiModelObserver(viewBinding)
        observeMiniCartListUiModel(viewModel, lifecycleOwner)
    }

    private fun initializeRecyclerView(viewBinding: LayoutBottomsheetMiniCartChatListBinding) {
        val adapterTypeFactory = MiniCartChatListAdapterTypeFactory(this)
        adapter = MiniCartChatListAdapter(adapterTypeFactory)
        viewBinding.rvMiniCartChatList.adapter = adapter
        viewBinding.rvMiniCartChatList.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
        viewBinding.rvMiniCartChatList.addItemDecoration(miniCartListDecoration)
    }

    private fun initializeCartData(viewModel: MiniCartViewModel) {
        adapter?.clearAllElements()
        showLoading()
        viewModel.getCartList(isFirstLoad = true)
    }

    private fun showLoading() {
        adapter?.let {
            it.removeErrorNetwork()
            it.setLoadingModel(LoadingModel())
            it.showLoading()
        }
    }

    private fun hideLoading() {
        adapter?.hideLoading()
    }

    private fun observeMiniCartListUiModel(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        bottomSheetUiModelObserver?.let {
            viewModel.miniCartChatListBottomSheetUiModel.observe(lifecycleOwner, it)
        }
    }
}