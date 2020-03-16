package com.tokopedia.play.ui.variantsheet

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.variantsheet.adapter.VariantAdapter
import com.tokopedia.play.ui.variantsheet.itemdecoration.VariantPlaceholderItemDecoration
import com.tokopedia.play.view.custom.TopShadowOutlineProvider
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.variant_common.model.ProductVariantCommon
import com.tokopedia.variant_common.model.VariantOptionWithAttribute
import com.tokopedia.variant_common.util.VariantCommonMapper
import com.tokopedia.variant_common.view.ProductVariantListener

/**
 * Created by jegul on 05/03/20
 */
class VariantSheetView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container), ProductVariantListener {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_variant_sheet, container, true)
            .findViewById(R.id.cl_variant_sheet)

    private val clVariantContent: ConstraintLayout = view.findViewById(R.id.cl_variant_content)
    private val clProductVariant: ConstraintLayout = view.findViewById(R.id.cl_product_variant)
    private val phProductVariant: ConstraintLayout = view.findViewById(R.id.ph_product_variant)
    private val tvSheetTitle: TextView = view.findViewById(R.id.tv_sheet_title)
    private val rvVariantList: RecyclerView = view.findViewById(R.id.rv_variant_list)
    private val btnAction: UnifyButton = view.findViewById(R.id.btn_action)
    private val phBtnAction: View = view.findViewById(R.id.ph_btn_action)
    private val btnContainer: ConstraintLayout = view.findViewById(R.id.btn_container)
    private val vBottomOverlay: View = view.findViewById(R.id.v_bottom_overlay)
    private val ivProductImage: ImageView = view.findViewById(R.id.iv_product_image)
    private val tvProductTitle: TextView = view.findViewById(R.id.tv_product_title)
    private val llProductDiscount: LinearLayout = view.findViewById(R.id.ll_product_discount)
    private val tvProductDiscount: TextView = view.findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = view.findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = view.findViewById(R.id.tv_current_price)

    private val imageRadius = view.resources.getDimensionPixelSize(R.dimen.play_product_line_image_radius).toFloat()
    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    private val variantAdapter: VariantAdapter = VariantAdapter(this)
    private var variantSheetUiModel: VariantSheetUiModel? = null

    init {
        view.findViewById<ImageView>(R.id.iv_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
                }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            btnContainer.setPadding(btnContainer.paddingLeft, btnContainer.paddingTop, btnContainer.paddingRight, insets.systemWindowInsetBottom)

            insets
        }

        rvVariantList.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = variantAdapter
            addItemDecoration(VariantPlaceholderItemDecoration(context))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnContainer.outlineProvider = TopShadowOutlineProvider()
        } else {
            btnContainer.setBackgroundResource(R.drawable.bg_play_product_action_container)
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

    internal fun setVariantSheet(model: VariantSheetUiModel) {
        variantSheetUiModel = model
        tvSheetTitle.text = container.context.getString(R.string.play_title_variant)

        setProduct(model.product)

        if (model.listOfVariantCategory.isNotEmpty()) {
            variantAdapter.setItemsAndAnimateChanges(model.listOfVariantCategory)
        }

        btnAction.isEnabled = !model.isPartialySelected()

        btnAction.text = view.context.getString(
                if (model.action == ProductAction.Buy) R.string.play_product_buy
                else R.string.play_product_add_to_card
        )

        btnAction.setOnClickListener {
            if (model.action == ProductAction.Buy) listener.onBuyClicked(this, model.product)
            else listener.onAddToCartClicked(this, model.product)
        }

        showPlaceholder(false)
    }

    internal fun showPlaceholder(isShow: Boolean, placeholderList: List<Any> = emptyList()) {
        if (isShow) {
            phProductVariant.visible()
            phBtnAction.visible()
            variantAdapter.setItemsAndAnimateChanges(placeholderList)

            btnAction.gone()
            clProductVariant.gone()
        } else {
            phProductVariant.gone()
            phBtnAction.gone()

            btnAction.visible()
            clProductVariant.visible()
        }
    }

    private fun setProduct(product: ProductLineUiModel) {
        ivProductImage.loadImageRounded(product.imageUrl, imageRadius)
        tvProductTitle.text = product.title

        when (product.price) {
            is DiscountedPrice -> {
                llProductDiscount.visible()
                tvProductDiscount.text = view.context.getString(R.string.play_discount_percent, product.price.discountPercent)
                tvOriginalPrice.text = product.price.originalPrice
                tvCurrentPrice.text = product.price.discountedPrice
            }
            is OriginalPrice -> {
                llProductDiscount.gone()
                tvCurrentPrice.text = product.price.price
            }
        }

    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute) {
        variantSheetUiModel?.let {
            it.mapOfSelectedVariants[variantOptions.variantOptionIdentifier] = variantOptions.variantId
        }

        val isPartialSelected = variantSheetUiModel?.isPartialySelected() ?: false
        val listOfVariants = VariantCommonMapper.processVariant(
                variantSheetUiModel?.parentVariant,
                variantSheetUiModel?.mapOfSelectedVariants,
                variantOptions.level,
                isPartialSelected
        )

        if (!listOfVariants.isNullOrEmpty()) {
            val selectedProduct = VariantCommonMapper.selectedProductData(
                    variantSheetUiModel?.parentVariant?: ProductVariantCommon())
            if (selectedProduct != null) {
                val stock = selectedProduct.stock

                val product = ProductLineUiModel(
                        id = selectedProduct.productId.toString(),
                        imageUrl = selectedProduct.picture?.original ?: "",
                        title = selectedProduct.name,
                        stock = if (stock == null) OutOfStock else StockAvailable(stock.stock.orZero()),
                        isVariantAvailable = true,
                        price = OriginalPrice(selectedProduct.priceFmt.toEmptyStringIfNull()),
                        minQty = variantSheetUiModel?.product?.minQty.orZero(),
                        applink = null
                )
                variantSheetUiModel?.stockWording = selectedProduct.stock?.stockWordingHTML
                variantSheetUiModel?.product = product
                setProduct(product)

                btnAction.isEnabled = variantSheetUiModel?.isPartialySelected() == false
            }
            variantAdapter.setItemsAndAnimateChanges(listOfVariants)
        }
    }

    override fun onVariantGuideLineClicked(url: String) {
        listener.onVariantGuideLinedClicked(url)
    }

    override fun getStockWording(): String {
        return  if (variantSheetUiModel?.isPartialySelected() == true) {
            ""
        } else {
            variantSheetUiModel?.stockWording?:container.context.getString(R.string.play_stock_available)
        }
    }

    interface Listener {
        fun onCloseButtonClicked(view: VariantSheetView)
        fun onAddToCartClicked(view: VariantSheetView, productModel: ProductLineUiModel)
        fun onBuyClicked(view: VariantSheetView, productModel: ProductLineUiModel)
        fun onVariantGuideLinedClicked(url: String)
    }
}