package com.tokopedia.play.view.viewcomponent

import android.graphics.Paint
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.R
import com.tokopedia.play.ui.variantsheet.adapter.VariantAdapter
import com.tokopedia.play.ui.variantsheet.itemdecoration.VariantItemDecoration
import com.tokopedia.play.view.custom.TopShadowOutlineProvider
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.VariantPlaceholderUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.variant_common.util.VariantCommonMapper

/**
 * Created by jegul on 31/07/20
 */
class VariantSheetViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.cl_variant_sheet), AtcVariantListener {

    private val clProductVariant: ConstraintLayout = findViewById(R.id.cl_product_variant)
    private val phProductVariant: ConstraintLayout = findViewById(R.id.ph_product_variant)
    private val tvSheetTitle: TextView = findViewById(R.id.tv_sheet_title)
    private val rvVariantList: RecyclerView = findViewById(R.id.rv_variant_list)
    private val btnAction: UnifyButton = findViewById(R.id.btn_action)
    private val phBtnAction: View = findViewById(R.id.ph_btn_action)
    private val btnContainer: ConstraintLayout = findViewById(R.id.btn_container)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)
    private val ivProductImage: ImageView = findViewById(R.id.iv_product_image)
    private val tvProductTitle: TextView = findViewById(R.id.tv_product_title)
    private val llProductDiscount: LinearLayout = findViewById(R.id.ll_product_discount)
    private val tvProductDiscount: TextView = findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = findViewById(R.id.tv_current_price)
    private val ivFreeShipping: ImageView = findViewById(R.id.iv_free_shipping)

    private val globalErrorContainer: ScrollView = findViewById(R.id.global_error_variant_container)
    private val globalError: GlobalError = findViewById(R.id.global_error_variant)

    private val imageRadius = resources.getDimensionPixelSize(R.dimen.play_product_image_radius).toFloat()
    private val toasterMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val variantAdapter: VariantAdapter = VariantAdapter(this)
    private var variantSheetUiModel: VariantSheetUiModel? = null

    init {
        findViewById<ImageView>(R.id.iv_sheet_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
                }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            btnContainer.setPadding(btnContainer.paddingLeft, btnContainer.paddingTop, btnContainer.paddingRight, insets.systemWindowInsetBottom)

            insets
        }

        rvVariantList.apply {
            layoutManager = LinearLayoutManager(rvVariantList.context, RecyclerView.VERTICAL, false)
            adapter = variantAdapter
            addItemDecoration(VariantItemDecoration(context))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnContainer.outlineProvider = TopShadowOutlineProvider()
        } else {
            btnContainer.setBackgroundResource(R.drawable.bg_play_product_action_container)
        }

        tvSheetTitle.text = getString(R.string.play_title_variant)
        tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute) {
        variantSheetUiModel?.let {
            it.mapOfSelectedVariants[variantOptions.variantCategoryKey] = variantOptions.variantId
        }

        val isPartialSelected = variantSheetUiModel?.isPartialySelected() ?: false
        val listOfVariants = VariantCommonMapper.processVariant(
                variantSheetUiModel?.parentVariant,
                variantSheetUiModel?.mapOfSelectedVariants,
                variantOptions.level,
                isPartialSelected
        )

        if (!listOfVariants.isNullOrEmpty()) {
            val pairSelectedProduct = VariantCommonMapper.selectedProductData(
                    variantSheetUiModel?.parentVariant?: ProductVariant())
            val selectedProduct = pairSelectedProduct?.second
            if (selectedProduct != null) {
                val stock = selectedProduct.stock

                val product = PlayProductUiModel.Product(
                        id = selectedProduct.productId.toString(),
                        shopId = variantSheetUiModel?.product?.shopId.toEmptyStringIfNull(),
                        imageUrl = selectedProduct.picture?.original ?: "",
                        title = selectedProduct.name,
                        stock = if (stock == null) OutOfStock else StockAvailable(stock.stock ?: 0),
                        isVariantAvailable = true,
                        price = if (selectedProduct.campaign?.isActive == true) {
                            DiscountedPrice(
                                    originalPrice = selectedProduct.campaign?.originalPriceFmt.toEmptyStringIfNull(),
                                    discountedPriceNumber = selectedProduct.campaign?.discountedPrice?.toDouble()?:0.0,
                                    discountPercent = selectedProduct.campaign?.discountedPercentage?.toInt()?:0,
                                    discountedPrice = selectedProduct.campaign?.discountedPriceFmt.toEmptyStringIfNull()
                            )
                        } else {
                            OriginalPrice(selectedProduct.priceFmt.toEmptyStringIfNull(), selectedProduct.price.toDouble())
                        },
                        minQty = variantSheetUiModel?.product?.minQty.orZero(),
                        isFreeShipping = variantSheetUiModel?.product?.isFreeShipping ?: false,
                        applink = null
                )
                variantSheetUiModel?.stockWording = stock?.stockWordingHTML
                variantSheetUiModel?.product = product
                setProduct(product)

                btnAction.isEnabled = variantSheetUiModel?.isPartialySelected() == false
            }
            variantAdapter.setItemsAndAnimateChanges(listOfVariants)
        }
    }

    override fun onVariantGuideLineHide(): Boolean {
        return true
    }

    override fun getStockWording(): String {
        return  if (variantSheetUiModel?.isPartialySelected() == true) {
            ""
        } else {
            variantSheetUiModel?.stockWording ?: getString(R.string.play_stock_available)
        }
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    fun setVariantSheet(model: VariantSheetUiModel) {
        showContent(shouldShow = true, withPlaceholder = false)

        variantSheetUiModel = model

        setProduct(model.product)

        if (model.listOfVariantCategory.isNotEmpty()) {
            variantAdapter.setItemsAndAnimateChanges(model.listOfVariantCategory)
        }

        btnAction.isEnabled = !model.isPartialySelected()

        btnAction.text = getString(
                if (model.action == ProductAction.Buy) R.string.play_product_buy
                else R.string.play_product_add_to_card
        )

        btnAction.setOnClickListener {
            variantSheetUiModel?.product?.let { product ->
                if (model.action == ProductAction.Buy) listener.onBuyClicked(this, product)
                else listener.onAddToCartClicked(this, product)
            }
        }
    }

    fun showPlaceholder() {
        showContent(shouldShow = true, withPlaceholder = true)
        variantAdapter.setItemsAndAnimateChanges(getPlaceholderModel())
    }

    fun showError(isConnectionError: Boolean, onError: () -> Unit) {
        showContent(shouldShow = false, withPlaceholder = false)

        globalError.setActionClickListener {
            onError()
        }

        globalError.setType(
                if (isConnectionError) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
        )
    }

    fun showToaster(toasterType: Int, message: String = "", actionText: String, actionListener: View.OnClickListener) {
        Toaster.toasterCustomBottomHeight = btnAction.height + toasterMargin
        Toaster.build(
                rootView,
                message,
                type = toasterType,
                actionText = actionText,
                clickListener = actionListener
        ).show()
    }

    private fun setProduct(product: PlayProductUiModel.Product) {
        ivProductImage.loadImageRounded(product.imageUrl, imageRadius)
        tvProductTitle.text = product.title

        when (product.price) {
            is DiscountedPrice -> {
                llProductDiscount.show()
                tvProductDiscount.text = getString(R.string.play_discount_percent, product.price.discountPercent)
                tvOriginalPrice.text = product.price.originalPrice
                tvCurrentPrice.text = product.price.discountedPrice
            }
            is OriginalPrice -> {
                llProductDiscount.hide()
                tvCurrentPrice.text = product.price.price
            }
        }

        if (product.isFreeShipping) ivFreeShipping.show()
        else ivFreeShipping.hide()
    }

    private fun showContent(shouldShow: Boolean, withPlaceholder: Boolean) {
        if (shouldShow) {
            btnContainer.show()

            if (withPlaceholder) {
                phProductVariant.show()
                phBtnAction.show()

                clProductVariant.hide()
                btnAction.hide()
            }
            else {
                phProductVariant.hide()
                phBtnAction.hide()

                clProductVariant.show()
                btnAction.show()
            }

            globalErrorContainer.hide()
        } else {
            clProductVariant.hide()
            phProductVariant.hide()
            btnContainer.hide()

            globalErrorContainer.show()
        }
    }

    private fun getPlaceholderModel() = List(PLACEHOLDER_VARIANT_CATEGORY_COUNT) {
        VariantPlaceholderUiModel.Category(
                List(PLACEHOLDER_VARIANT_OPTION_COUNT) { VariantPlaceholderUiModel.Option }
        )
    }

    companion object {
        private const val PLACEHOLDER_VARIANT_CATEGORY_COUNT = 2
        private const val PLACEHOLDER_VARIANT_OPTION_COUNT = 7
    }

    interface Listener {
        fun onCloseButtonClicked(view: VariantSheetViewComponent)
        fun onAddToCartClicked(view: VariantSheetViewComponent, productModel: PlayProductUiModel.Product)
        fun onBuyClicked(view: VariantSheetViewComponent, productModel: PlayProductUiModel.Product)
    }
}