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
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.chatlist.adapter.MiniCartChatListAdapter
import com.tokopedia.minicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.chatlist.viewholder.MiniCartChatProductViewHolder
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.databinding.LayoutBottomsheetMiniCartChatListBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class MiniCartChatListBottomSheet @Inject constructor(
    private var miniCartChatProductDecoration: MiniCartChatListDecoration,
    private var analytics: MiniCartAnalytics
) : MiniCartChatProductViewHolder.ChatProductListener {

    companion object {
        const val MAX_PRODUCT_SIZE = 3
        const val DEFAULT_PRODUCT_SIZE = 0
        const val RECYCLER_VIEW_EXTRA_PADDING_BOTTOM = 16
    }

    private var viewBinding: LayoutBottomsheetMiniCartChatListBinding? = null
    private var bottomSheetUiModelObserver: Observer<MiniCartListUiModel>? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var adapter: MiniCartChatListAdapter? = null
    private var viewModel: MiniCartViewModel? = null
    private var mContext: Context? = null
    private var miniCartListUiModel: MiniCartListUiModel? = null
    private var elements: ArrayList<MiniCartChatProductUiModel> = arrayListOf()
    private var layoutManager: LinearLayoutManager? = null
    private var scrollPosition = 0

    override fun onClickProduct(element: MiniCartChatProductUiModel, isChecked: Boolean) {
        if (isChecked) {
            updateDataWhenAdding(element)
        } else {
            updateDataWhenRemoving(element)
        }
        analytics.eventClickTickBoxChatBottomSheet(isChecked)
    }

    fun show(
        context: Context?,
        fragmentManager: FragmentManager,
        lifecycleOwner: LifecycleOwner,
        viewModel: MiniCartViewModel
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
            setButton(viewBinding)
            mContext?.apply {
                bottomSheet?.setTitle(getString(com.tokopedia.minicart.R.string.mini_cart_chat_bottomsheet_title_label))
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
                this@MiniCartChatListBottomSheet.viewBinding?.cardView?.gone()
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
        layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
        viewBinding.rvMiniCartChatList.adapter = adapter
        viewBinding.rvMiniCartChatList.layoutManager = layoutManager
        viewBinding.rvMiniCartChatList.addItemDecoration(miniCartChatProductDecoration)
        viewBinding.rvMiniCartChatList.itemAnimator = null
    }

    private fun initializeCartData(viewModel: MiniCartViewModel) {
        adapter?.clearAllElements()
        showLoading()
        viewModel.getCartList(isFirstLoad = true)
    }

    private fun updateDataWhenAdding(element: MiniCartChatProductUiModel) {
        mContext?.apply {
            if (elements.size <= MAX_PRODUCT_SIZE) {
                elements.add(element)
                if (elements.size == MAX_PRODUCT_SIZE) {
                    resetModelData(element)
                    scrollPosition = layoutManager?.findLastVisibleItemPosition().orZero()
                    miniCartListUiModel?.chatVisitables?.toMutableList()?.let {
                        adapter?.updateList(it)
                    }
                    viewBinding?.rvMiniCartChatList?.post {
                        viewBinding?.rvMiniCartChatList?.scrollToPosition(scrollPosition)
                    }
                }
            }
            viewBinding?.btnChat?.text = getString(com.tokopedia.minicart.R.string.mini_cart_chat_btn_label_ask_product, elements.size)
        }
    }

    private fun updateDataWhenRemoving(element: MiniCartChatProductUiModel) {
        mContext?.apply {
            elements.remove(element)
            if (elements.size == DEFAULT_PRODUCT_SIZE) {
                viewBinding?.btnChat?.text = getString(com.tokopedia.minicart.R.string.mini_cart_chat_btn_label)
            } else {
                viewBinding?.btnChat?.text = getString(com.tokopedia.minicart.R.string.mini_cart_chat_btn_label_ask_product, elements.size)
                resetModelData(element)
                scrollPosition = layoutManager?.findLastVisibleItemPosition().orZero()
                miniCartListUiModel?.chatVisitables?.toMutableList()?.let {
                    adapter?.updateList(it)
                }
                viewBinding?.rvMiniCartChatList?.post {
                    viewBinding?.rvMiniCartChatList?.scrollToPosition(scrollPosition)
                }
            }
        }
    }

    private fun resetModelData(productSelected: MiniCartChatProductUiModel) {
        miniCartListUiModel?.chatVisitables?.forEach { model ->
            if (model is MiniCartChatProductUiModel) {
                if (model.productId == productSelected.productId) {
                    model.isChecked = productSelected.isChecked
                    model.size = elements.size
                } else {
                    model.size = elements.size
                }
            }
        }
    }

    private fun setButton(viewBinding: LayoutBottomsheetMiniCartChatListBinding) {
        mContext?.apply {
            viewBinding.cardView.show()
            viewBinding.btnChat.setDrawable(getIconUnifyDrawable(this, IconUnify.CHAT, ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)))
            viewBinding.btnChat.text = getString(R.string.mini_cart_chat_btn_label)
            viewBinding.btnChat.setOnClickListener {
                val shopId = viewModel?.currentShopIds?.value?.firstOrNull().orEmpty()
                if (elements.isNullOrEmpty()) {
                    openChatPageWithoutProduct(shopId)
                    analytics.eventClickBtnDirectChatBottomSheet()
                } else {
                    openChatPage(shopId)
                    analytics.eventClickBtnAskProductChatBottomSheet()
                }
            }
            viewBinding.rvMiniCartChatList.setPadding(
                0,
                0,
                0,
                viewBinding.cardView.height +
                    RECYCLER_VIEW_EXTRA_PADDING_BOTTOM.dpToPx(resources.displayMetrics)
            )
        }
    }

    private fun openChatPageWithoutProduct(shopId: String) {
        val intent = RouteManager.getIntent(mContext, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId)
        mContext?.startActivity(intent)
    }

    private fun openChatPage(shopId: String) {
        val productIds = mutableListOf<String>()
        elements.forEach { element ->
            productIds.add(element.productId)
        }
        val intent = RouteManager.getIntent(mContext, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId)
        val stringProductPreviews = CommonUtil.toJson(productIds)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
        mContext?.startActivity(intent)
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
