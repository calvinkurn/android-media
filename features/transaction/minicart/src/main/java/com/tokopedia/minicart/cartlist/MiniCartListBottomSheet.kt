package com.tokopedia.minicart.cartlist

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapter
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.minicart.common.widget.MiniCartWidgetViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor(var miniCartListDecoration: MiniCartListDecoration) : MiniCartWidgetListener, MiniCartListActionListener {

    lateinit var viewModel: MiniCartWidgetViewModel
    private var bottomSheet: BottomSheetUnify? = null
    private var totalAmount: TotalAmount? = null
    private var rvMiniCartList: RecyclerView? = null
    private var adapter: MiniCartListAdapter? = null

    fun show(context: Context?,
             fragmentManager: FragmentManager,
             lifecycleOwner: LifecycleOwner,
             viewModel: MiniCartWidgetViewModel,
             onDismiss: () -> Unit) {
        context?.let {
            initializeView(it, fragmentManager, onDismiss)
            initializeViewModel(viewModel, lifecycleOwner)
            initializeCartData()
        }
    }

    fun initializeViewModel(viewModel: MiniCartWidgetViewModel, lifecycleOwner: LifecycleOwner) {
        this.viewModel = viewModel
        viewModel.miniCartListListUiModel.observe(lifecycleOwner, {
            hideLoading()
            bottomSheet?.setTitle(it.title)
            adapter?.updateList(it.visitables)
            updateTotalAmount(it.miniCartWidgetData)
        })
    }

    private fun initializeCartData() {
        showLoading()
        viewModel.getCartList()
    }

    private fun initializeView(context: Context, fragmentManager: FragmentManager, onDismiss: () -> Unit) {
        context.let {
            bottomSheet = BottomSheetUnify().apply {
                showCloseIcon = false
                showHeader = true
                isDragable = true
                showKnob = true
                isHideable = true
                clearContentPadding = true
                customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
                setOnDismissListener {
                    onDismiss.invoke()
                }
            }

            val view = View.inflate(it, R.layout.layout_bottomsheet_mini_cart_list, null)
            bottomSheet?.setChild(view)
            bottomSheet?.show(fragmentManager, this.javaClass.simpleName)

            initializeTotalAmount(view)
            initializeRecyclerView(view)
        }
    }

    private fun initializeRecyclerView(view: View) {
        rvMiniCartList = view.findViewById(R.id.rv_mini_cart_list)
        val adapterTypeFactory = MiniCartListAdapterTypeFactory(this)
        adapter = MiniCartListAdapter(adapterTypeFactory)
        rvMiniCartList?.adapter = adapter
        rvMiniCartList?.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        rvMiniCartList?.addItemDecoration(miniCartListDecoration)
        rvMiniCartList?.itemAnimator = null
//        (rvMiniCartList?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    private fun initializeTotalAmount(view: View) {
        totalAmount = view.findViewById(R.id.total_amount)
        totalAmount?.amountChevronView?.setOnClickListener {
            // Show total amount bottomsheet
        }
        totalAmount?.enableAmountChevron(true)
        totalAmount?.amountChevronView?.setOnClickListener {

        }
        totalAmount?.context?.let {
            val chatIcon = getIconUnifyDrawable(it, IconUnify.CHAT, ContextCompat.getColor(it, R.color.Unify_G500))
            totalAmount?.setAdditionalButton(chatIcon)
        }
        if (totalAmount?.isTotalAmountLoading == false) {
            totalAmount?.isTotalAmountLoading = true
        }
    }

    private fun updateTotalAmount(miniCartWidgetData: MiniCartWidgetData) {
        totalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetData.totalProductPrice, false))
            setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartWidgetData.totalProductCount))
        }
        totalAmount?.isTotalAmountLoading = false
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


    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        // no-op
    }

    override fun onDeleteClicked() {

    }

    override fun onBulkDeleteUnavailableItems() {

    }

    override fun onQuantityChanged(productId: String, newQty: Int) {
        // Todo : debounce before call calculateProduct
        viewModel.updateProductQty(productId, newQty)
        viewModel.calculateProduct()
    }

    override fun onNotesChanged() {

    }

    override fun onShowSimilarProductClicked() {

    }

    override fun onShowUnavailableItemsCLicked() {
        adapter?.data?.forEachIndexed { index, visitable ->
            if (visitable is MiniCartUnavailableHeaderUiModel) {
                rvMiniCartList?.smoothScrollToPosition(index)
                return@forEachIndexed
            }
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel.handleUnavailableItemsAccordion()
        val lastItemPosition = adapter?.list?.size ?: 0 - 1
        if (lastItemPosition != -1) {
            rvMiniCartList?.smoothScrollToPosition(lastItemPosition)
        }
    }

}