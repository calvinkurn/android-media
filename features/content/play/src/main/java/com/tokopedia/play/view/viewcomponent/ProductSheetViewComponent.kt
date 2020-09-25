package com.tokopedia.play.view.viewcomponent

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
import com.tokopedia.play.ui.productsheet.adapter.MerchantVoucherAdapter
import com.tokopedia.play.ui.productsheet.adapter.ProductLineAdapter
import com.tokopedia.play.ui.productsheet.itemdecoration.MerchantVoucherItemDecoration
import com.tokopedia.play.ui.productsheet.itemdecoration.ProductLineItemDecoration
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.ProductPlaceholderUiModel
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import com.tokopedia.play.view.uimodel.VoucherPlaceholderUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 31/07/20
 */
class ProductSheetViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.cl_product_sheet) {

    private val clProductContent: ConstraintLayout = findViewById(R.id.cl_product_content)
    private val tvSheetTitle: TextView = findViewById(R.id.tv_sheet_title)
    private val rvProductList: RecyclerView = findViewById(R.id.rv_product_list)
    private val rvVoucherList: RecyclerView = findViewById(R.id.rv_voucher_list)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)

    private val globalError: GlobalError = findViewById(R.id.global_error_product)

    private val clProductEmpty: ConstraintLayout = findViewById(R.id.cl_product_empty)
    private val btnProductEmpty: UnifyButton = findViewById(R.id.btn_action_product_empty)

    private val productLineAdapter = ProductLineAdapter(object : ProductLineViewHolder.Listener {
        override fun onBuyProduct(product: ProductLineUiModel) {
            listener.onBuyButtonClicked(this@ProductSheetViewComponent, product)

        }
        override fun onAtcProduct(product: ProductLineUiModel) {
            listener.onAtcButtonClicked(this@ProductSheetViewComponent, product)
        }

        override fun onClickProductCard(product: ProductLineUiModel, position: Int) {
            listener.onProductCardClicked(this@ProductSheetViewComponent, product, position)
        }
    })
    private val voucherAdapter = MerchantVoucherAdapter()

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    init {
        findViewById<ImageView>(R.id.iv_sheet_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
                }

        rvProductList.apply {
            layoutManager = LinearLayoutManager(rvProductList.context, RecyclerView.VERTICAL, false)
            adapter = productLineAdapter
            addItemDecoration(ProductLineItemDecoration(rvProductList.context))
        }

        rvVoucherList.apply {
            layoutManager = LinearLayoutManager(rvVoucherList.context, RecyclerView.HORIZONTAL, false)
            adapter = voucherAdapter
            addItemDecoration(MerchantVoucherItemDecoration(rvVoucherList.context))
        }

        rvVoucherList.apply {
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_SETTLING &&
                            layoutManager is LinearLayoutManager) {
                        val llManager = layoutManager as LinearLayoutManager
                        listener.onVoucherScrolled(this@ProductSheetViewComponent, llManager.findLastVisibleItemPosition())
                    }
                }
            })
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clProductContent.setPadding(clProductContent.paddingLeft, clProductContent.paddingTop, clProductContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    fun setProductSheet(model: ProductSheetUiModel) {
        showContent(true)

        if (isProductDecreased(model.productList.size)) showToasterProductUpdated()

        tvSheetTitle.text = model.title
        voucherAdapter.setItemsAndAnimateChanges(model.voucherList)
        productLineAdapter.setItemsAndAnimateChanges(model.productList)

        if (model.voucherList.isEmpty()) rvVoucherList.hide()
        else rvVoucherList.show()
    }

    fun showPlaceholder() {
        showContent(true)
        setProductSheet(getPlaceholderModel())
    }

    fun showError(isConnectionError: Boolean, onError: () -> Unit) {
        showContent(false)

        globalError.setActionClickListener {
            onError()
        }

        globalError.setType(
                if (isConnectionError) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
        )
    }

    fun showEmpty(partnerId: Long) {
        showContent(false)
        globalError.hide()
        clProductEmpty.show()

        btnProductEmpty.setOnClickListener {
            listener.onEmptyButtonClicked(this@ProductSheetViewComponent, partnerId)
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
                rootView,
                getString(R.string.play_product_updated),
                type = Toaster.TYPE_NORMAL)
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 5
    }

    interface Listener {
        fun onCloseButtonClicked(view: ProductSheetViewComponent)
        fun onBuyButtonClicked(view: ProductSheetViewComponent, product: ProductLineUiModel)
        fun onAtcButtonClicked(view: ProductSheetViewComponent, product: ProductLineUiModel)
        fun onProductCardClicked(view: ProductSheetViewComponent, product: ProductLineUiModel, position: Int)
        fun onVoucherScrolled(view: ProductSheetViewComponent, lastPositionViewed: Int)
        fun onEmptyButtonClicked(view: ProductSheetViewComponent, partnerId: Long)
    }
}