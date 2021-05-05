package com.tokopedia.play.view.viewcomponent

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
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
import com.tokopedia.play.ui.productsheet.viewholder.MerchantVoucherViewHolder
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.VoucherPlaceholderUiModel
import com.tokopedia.play.view.uimodel.recom.PlayProductTagsBasicInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayProductTagsUiModel
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
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
        override fun onBuyProduct(product: PlayProductUiModel.Product) {
            listener.onBuyButtonClicked(this@ProductSheetViewComponent, product)

        }

        override fun onAtcProduct(product: PlayProductUiModel.Product) {
            listener.onAtcButtonClicked(this@ProductSheetViewComponent, product)
        }

        override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
            listener.onProductCardClicked(this@ProductSheetViewComponent, product, position)
        }
    })
    private val voucherAdapter = MerchantVoucherAdapter(object : MerchantVoucherViewHolder.Listener {
        override fun onCopyVoucherCode(voucher: MerchantVoucherUiModel) {
            listener.onCopyVoucherCodeClicked(this@ProductSheetViewComponent, voucher)
        }
    })

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val voucherScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager = recyclerView.layoutManager
            if (newState == RecyclerView.SCROLL_STATE_SETTLING &&
                    layoutManager is LinearLayoutManager) {
                listener.onVoucherScrolled(this@ProductSheetViewComponent, layoutManager.findLastVisibleItemPosition())
            }
        }
    }

    private val productScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                listener.onProductsImpressed(this@ProductSheetViewComponent, getVisibleProducts())
            }
        }
    }

    private val layoutManagerProductList = object : LinearLayoutManager(rvProductList.context, RecyclerView.VERTICAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            listener.onProductsImpressed(this@ProductSheetViewComponent, getVisibleProducts())
        }
    }

    private val layoutManagerVoucherList = object : LinearLayoutManager(rvVoucherList.context, RecyclerView.HORIZONTAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            listener.onVouchersImpressed(this@ProductSheetViewComponent, getVisibleVouchers())
        }
    }

    private var isProductSheetsInitialized = false

    init {
        findViewById<ImageView>(R.id.iv_sheet_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
                }

        rvProductList.apply {
            layoutManager = layoutManagerProductList
            adapter = productLineAdapter
            addItemDecoration(ProductLineItemDecoration(rvProductList.context))
            addOnScrollListener(StopFlingScrollListener())
        }

        rvVoucherList.apply {
            layoutManager = layoutManagerVoucherList
            adapter = voucherAdapter
            addItemDecoration(MerchantVoucherItemDecoration(rvVoucherList.context))
        }

        rvProductList.addOnScrollListener(productScrollListener)
        rvVoucherList.addOnScrollListener(voucherScrollListener)

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
        sendImpression()
    }

    fun setProductSheet(model: PlayProductTagsUiModel.Complete) {
        showContent(true)

        if (isProductDecreased(model.productList.size)) showToasterProductUpdated()

        tvSheetTitle.text = model.basicInfo.bottomSheetTitle
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

    private fun getPlaceholderModel() = PlayProductTagsUiModel.Complete(
            basicInfo = PlayProductTagsBasicInfoUiModel(
                    bottomSheetTitle = "",
                    partnerId = 0L,
                    maxFeaturedProducts = 0
            ),
            voucherList = List(PLACEHOLDER_COUNT) { VoucherPlaceholderUiModel },
            productList = List(PLACEHOLDER_COUNT) { PlayProductUiModel.Placeholder }
    )

    private fun isProductDecreased(productSize: Int): Boolean {
        return productLineAdapter.getItems().isNotEmpty() &&
                productLineAdapter.getItems().first() is PlayProductUiModel.Product &&
                productLineAdapter.itemCount > productSize
    }

    private fun showToasterProductUpdated() {
        Toaster.build(
                rootView,
                getString(R.string.play_product_updated),
                type = Toaster.TYPE_NORMAL
        ).show()
    }

    private fun sendImpression() {
        if (isProductSheetsInitialized) {
            listener.onProductsImpressed(this@ProductSheetViewComponent, getVisibleProducts())
            listener.onVouchersImpressed(this@ProductSheetViewComponent, getVisibleVouchers())
        }
        else isProductSheetsInitialized = true

    }

    /**
     * Lifecycle Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rootView.requestApplyInsetsWhenAttached()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvVoucherList.removeOnScrollListener(voucherScrollListener)
        rvProductList.removeOnScrollListener(productScrollListener)
    }

    /**
     * Analytic Helper
     */
    fun getVisibleProducts(): List<Pair<PlayProductUiModel.Product, Int>> {
        val products = productLineAdapter.getItems()
        if (products.isNotEmpty()) {
            val startPosition = layoutManagerProductList.findFirstCompletelyVisibleItemPosition()
            val endPosition = layoutManagerProductList.findLastCompletelyVisibleItemPosition()
            if (startPosition > -1 && endPosition < products.size) return products.slice(startPosition..endPosition)
                    .filterIsInstance<PlayProductUiModel.Product>()
                    .mapIndexed { index, item ->
                        Pair(item, startPosition + index)
                    }
        }
        return emptyList()
    }

    fun getVisibleVouchers(): List<MerchantVoucherUiModel> {
        val vouchers = voucherAdapter.getItems()
        if (vouchers.isNotEmpty()) {
            val startPosition = layoutManagerVoucherList.findFirstCompletelyVisibleItemPosition()
            val endPosition = layoutManagerVoucherList.findLastCompletelyVisibleItemPosition()
            if (startPosition > -1 && endPosition < vouchers.size) {
                return vouchers
                        .slice(startPosition..endPosition)
                        .filterIsInstance<MerchantVoucherUiModel>()
            }
        }
        return emptyList()
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 5
    }

    interface Listener {
        fun onCloseButtonClicked(view: ProductSheetViewComponent)
        fun onBuyButtonClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product)
        fun onAtcButtonClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product)
        fun onProductCardClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product, position: Int)
        fun onVoucherScrolled(view: ProductSheetViewComponent, lastPositionViewed: Int)
        fun onEmptyButtonClicked(view: ProductSheetViewComponent, partnerId: Long)
        fun onCopyVoucherCodeClicked(view: ProductSheetViewComponent, voucher: MerchantVoucherUiModel)
        fun onVouchersImpressed(view: ProductSheetViewComponent, vouchers: List<MerchantVoucherUiModel>)
        fun onProductsImpressed(view: ProductSheetViewComponent, products: List<Pair<PlayProductUiModel.Product, Int>>)
    }
}