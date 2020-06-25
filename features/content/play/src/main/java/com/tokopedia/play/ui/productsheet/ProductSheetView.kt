package com.tokopedia.play.ui.productsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.productsheet.adapter.MerchantVoucherAdapter
import com.tokopedia.play.ui.productsheet.adapter.ProductLineAdapter
import com.tokopedia.play.ui.productsheet.itemdecoration.MerchantVoucherItemDecoration
import com.tokopedia.play.ui.productsheet.itemdecoration.ProductLineItemDecoration
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.ProductPlaceholderUiModel
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import com.tokopedia.play.view.uimodel.VoucherPlaceholderUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 02/03/20
 */
class ProductSheetView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_product_sheet, container, true)
            .findViewById(R.id.cl_product_sheet)

    private val clProductContent: ConstraintLayout = view.findViewById(R.id.cl_product_content)
    private val tvSheetTitle: TextView = view.findViewById(R.id.tv_sheet_title)
    private val rvProductList: RecyclerView = view.findViewById(R.id.rv_product_list)
    private val rvVoucherList: RecyclerView = view.findViewById(R.id.rv_voucher_list)
    private val vBottomOverlay: View = view.findViewById(R.id.v_bottom_overlay)

    private val globalError: GlobalError = view.findViewById(R.id.global_error_product)

    private val clProductEmpty: ConstraintLayout = view.findViewById(R.id.cl_product_empty)
    private val btnProductEmpty: UnifyButton = view.findViewById(R.id.btn_action_product_empty)

    private val productLineAdapter = ProductLineAdapter(object : ProductLineViewHolder.Listener {
        override fun onBuyProduct(product: ProductLineUiModel) {
            listener.onBuyButtonClicked(this@ProductSheetView, product)

        }
        override fun onAtcProduct(product: ProductLineUiModel) {
            listener.onAtcButtonClicked(this@ProductSheetView, product)
        }

        override fun onClickProductCard(product: ProductLineUiModel, position: Int) {
            listener.onProductCardClicked(this@ProductSheetView, product, position)
        }
    })
    private val voucherAdapter = MerchantVoucherAdapter()

    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    init {
        view.findViewById<ImageView>(R.id.iv_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
                }

        rvProductList.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = productLineAdapter
            addItemDecoration(ProductLineItemDecoration(view.context))
        }

        rvVoucherList.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            adapter = voucherAdapter
            addItemDecoration(MerchantVoucherItemDecoration(view.context))
        }

        rvVoucherList.apply {
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_SETTLING &&
                            layoutManager is LinearLayoutManager) {
                        val llManager = layoutManager as LinearLayoutManager
                        listener.onVoucherScrolled(llManager.findLastVisibleItemPosition())
                    }
                }
            })
        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clProductContent.setPadding(clProductContent.paddingLeft, clProductContent.paddingTop, clProductContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    internal fun showWithHeight(height: Int) {
        if (view.height != height) {
            val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            view.layoutParams = layoutParams
        }

        show()
    }

    internal fun setProductSheet(model: ProductSheetUiModel) {
        showContent(true)

        if (isProductDecreased(model.productList.size)) showToasterProductUpdated()

        tvSheetTitle.text = model.title
        voucherAdapter.setItemsAndAnimateChanges(model.voucherList)
        productLineAdapter.setItemsAndAnimateChanges(model.productList)

        if (model.voucherList.isEmpty()) rvVoucherList.hide()
        else rvVoucherList.show()
    }

    internal fun showPlaceholder() {
        showContent(true)
        setProductSheet(getPlaceholderModel())
    }

    internal fun showError(isConnectionError: Boolean, onError: () -> Unit) {
        showContent(false)

        globalError.setActionClickListener {
            onError()
        }

        globalError.setType(
                if (isConnectionError) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
        )
    }

    internal fun showEmpty(partnerId: Long) {
        showContent(false)
        globalError.hide()
        clProductEmpty.show()

        btnProductEmpty.setOnClickListener {
            listener.onEmptyButtonClicked(partnerId)
        }
    }

    private fun showContent(shouldShow: Boolean) {
        if (shouldShow) {
            rvProductList.show()
            rvVoucherList.show()

            globalError.hide()
            clProductEmpty.hide()
        } else {
            rvProductList.hide()
            rvVoucherList.hide()

            globalError.show()
        }
    }

    private fun getPlaceholderModel() = ProductSheetUiModel(
            title = "",
            partnerId = 0L,
            voucherList = List(PLACEHOLDER_COUNT) { VoucherPlaceholderUiModel },
            productList = List(PLACEHOLDER_COUNT) { ProductPlaceholderUiModel }
    )

    private fun isProductDecreased(productSize: Int): Boolean {
        return productLineAdapter.getItems().isNotEmpty() &&
                productLineAdapter.getItems().first() is ProductLineUiModel &&
                productLineAdapter.itemCount > productSize
    }

    private fun showToasterProductUpdated() {
        Toaster.make(
                view,
                view.context.getString(R.string.play_product_updated),
                type = Toaster.TYPE_NORMAL)
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 5
    }

    interface Listener {
        fun onCloseButtonClicked(view: ProductSheetView)
        fun onBuyButtonClicked(view: ProductSheetView, product: ProductLineUiModel)
        fun onAtcButtonClicked(view: ProductSheetView, product: ProductLineUiModel)
        fun onProductCardClicked(view: ProductSheetView, product: ProductLineUiModel, position: Int)
        fun onVoucherScrolled(lastPositionViewed: Int)
        fun onEmptyButtonClicked(partnerId: Long)
    }
}