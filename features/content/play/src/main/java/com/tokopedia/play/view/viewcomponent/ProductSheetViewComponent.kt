package com.tokopedia.play.view.viewcomponent

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
import com.tokopedia.play.ui.productsheet.adapter.ProductSectionAdapter
import com.tokopedia.play.ui.productsheet.itemdecoration.ProductLineItemDecoration
import com.tokopedia.play.ui.productsheet.viewholder.ProductSectionViewHolder
import com.tokopedia.play.view.custom.RectangleShadowOutlineProvider
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 31/07/20
 */
class ProductSheetViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.cl_product_sheet) {

    private val clProductContent: ConstraintLayout = findViewById(R.id.cl_product_content)
    private val clProductVoucher: FrameLayout = findViewById(R.id.cl_product_voucher_info)
    private val clVoucherContent: ConstraintLayout = findViewById(R.id.cl_product_voucher_content)
    private val tvSheetTitle: TextView = findViewById(com.tokopedia.play_common.R.id.tv_sheet_title)
    private val rvProductList: RecyclerView = findViewById(R.id.rv_product_list)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)

    private val tvVoucherHeaderTitle: TextView = findViewById(R.id.tv_first_voucher_title)
    private val tvVoucherHeaderDesc: TextView = findViewById(R.id.tv_voucher_count)

    private val globalError: GlobalError = findViewById(R.id.global_error_product)

    private val clProductEmpty: ConstraintLayout = findViewById(R.id.cl_product_empty)
    private val btnProductEmpty: UnifyButton = findViewById(R.id.btn_action_product_empty)

    private val productSectionAdapter = ProductSectionAdapter(object : ProductSectionViewHolder.Listener{
        override fun onBuyProduct(
            product: PlayProductUiModel.Product,
            sectionInfo: ProductSectionUiModel.Section
        ) {
            listener.onBuyButtonClicked(this@ProductSheetViewComponent, product, sectionInfo)

        }
        override fun onATCProduct(
            product: PlayProductUiModel.Product,
            sectionInfo: ProductSectionUiModel.Section
        ) {
            listener.onAtcButtonClicked(this@ProductSheetViewComponent, product, sectionInfo)
        }
        override fun onClickProductCard(
            product: PlayProductUiModel.Product,
            sectionInfo: ProductSectionUiModel.Section,
            position: Int
        ) {
            listener.onProductCardClicked(this@ProductSheetViewComponent, product, sectionInfo, position)
        }
        override fun onProductChanged() {
            listener.onProductCountChanged(this@ProductSheetViewComponent)
        }

        override fun onProductImpressed(
            product: List<Pair<PlayProductUiModel.Product, Int>>,
            sectionInfo: ProductSectionUiModel.Section
        ) {
            listener.onProductsImpressed(this@ProductSheetViewComponent, product, sectionInfo)
        }
    })

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    init {
        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
                }

        rvProductList.apply {
            adapter = productSectionAdapter
            layoutManager = LinearLayoutManager(rvProductList.context)
            addOnScrollListener(StopFlingScrollListener())
            addItemDecoration(ProductLineItemDecoration(context))
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clProductContent.setPadding(clProductContent.paddingLeft, clProductContent.paddingTop, clProductContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }

        clVoucherContent.outlineProvider = RectangleShadowOutlineProvider()
        clVoucherContent.clipToOutline = true
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

    fun setProductSheet(
        sectionList: List<ProductSectionUiModel>,
        voucherList: List<MerchantVoucherUiModel>,
        title: String,
    ) {
        showContent(true)
        tvSheetTitle.text = title
        productSectionAdapter.setItemsAndAnimateChanges(sectionList)

        if (voucherList.isEmpty()) {
            clProductVoucher.hide()
        } else {
            clProductVoucher.setOnClickListener {
                listener.onInfoVoucherClicked(this@ProductSheetViewComponent)
            }

            voucherList.let {
                tvVoucherHeaderTitle.text = it.getOrNull(0)?.title ?: ""
                tvVoucherHeaderDesc.text = getString(R.string.play_product_voucher_header_desc, it.size.toString())
            }
            clProductVoucher.show()
        }
    }

    fun showPlaceholder() {
        showContent(true)
        productSectionAdapter.setItemsAndAnimateChanges(
            List(PLACEHOLDER_COUNT) { ProductSectionUiModel.Placeholder }
        )
    }

    fun showError(isConnectionError: Boolean, onError: () -> Unit) {
        showContent(false)
        clProductEmpty.hide()

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

        btnProductEmpty.setOnClickListener {
            listener.onEmptyButtonClicked(this@ProductSheetViewComponent, partnerId)
        }
    }

    private fun showContent(shouldShow: Boolean) {
        if (shouldShow) {
            rvProductList.show()
            clProductVoucher.show()

            globalError.hide()
            clProductEmpty.hide()
        } else {
            rvProductList.hide()
            clProductVoucher.hide()

            globalError.show()
            clProductEmpty.show()
        }
    }

    /**
     * Lifecycle Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rootView.requestApplyInsetsWhenAttached()
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 5
    }

    interface Listener {
        fun onCloseButtonClicked(view: ProductSheetViewComponent)
        fun onBuyButtonClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section)
        fun onAtcButtonClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section)
        fun onProductCardClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, position: Int)
        fun onEmptyButtonClicked(view: ProductSheetViewComponent, partnerId: Long)
        fun onProductsImpressed(view: ProductSheetViewComponent, products: List<Pair<PlayProductUiModel.Product, Int>>, sectionInfo: ProductSectionUiModel.Section)
        fun onProductCountChanged(view: ProductSheetViewComponent)
        fun onInfoVoucherClicked(view: ProductSheetViewComponent)
    }
}