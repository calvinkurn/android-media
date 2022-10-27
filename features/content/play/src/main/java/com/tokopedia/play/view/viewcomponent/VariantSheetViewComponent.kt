package com.tokopedia.play.view.viewcomponent

import android.graphics.Paint
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.extensions.generateButton
import com.tokopedia.play.extensions.toAction
import com.tokopedia.play.ui.variantsheet.adapter.VariantAdapter
import com.tokopedia.play.ui.variantsheet.adapter.VariantLabelAdapter
import com.tokopedia.play.ui.variantsheet.itemdecoration.VariantItemDecoration
import com.tokopedia.play.ui.variantsheet.itemdecoration.VariantLabelItemDecoration
import com.tokopedia.play.view.custom.TopShadowOutlineProvider
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.VariantPlaceholderUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VariantUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 31/07/20
 */
class VariantSheetViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.cl_variant_sheet), AtcVariantListener {

    private val clProductVariant: ConstraintLayout = findViewById(R.id.cl_product_variant)
    private val phProductVariant: ConstraintLayout = findViewById(R.id.ph_product_variant)
    private val tvSheetTitle: TextView = findViewById(com.tokopedia.play_common.R.id.tv_sheet_title)
    private val rvVariantList: RecyclerView = findViewById(R.id.rv_variant_list)
    private val btnAction: UnifyButton = findViewById(R.id.btn_action)
    private val phBtnAction: View = findViewById(R.id.ph_btn_action)
    private val btnContainer: ConstraintLayout = findViewById(R.id.btn_container)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)
    private val ivProductImage: ImageUnify = findViewById(R.id.iv_product_image)
    private val tvProductDiscount: TextView = findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = findViewById(R.id.tv_current_price)
    private val tvProductStock: TextView = findViewById(R.id.tv_product_stock)
    private val rvLabel: RecyclerView = findViewById(R.id.rv_label)

    private val globalErrorContainer: ScrollView = findViewById(R.id.global_error_variant_container)
    private val globalError: GlobalError = findViewById(R.id.global_error_variant)

    private val imageRadius = resources.getDimensionPixelSize(R.dimen.play_product_image_radius).toFloat()
    private val toasterMargin = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val variantAdapter: VariantAdapter = VariantAdapter(this)
    private var mAction: ProductAction = ProductAction.Buy

    private val labelAdapter = VariantLabelAdapter()

    private var mVariantModel: VariantUiModel? = null

    init {
        rvLabel.apply {
            adapter = labelAdapter
            addItemDecoration(VariantLabelItemDecoration(context))
            itemAnimator = null
        }

        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
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

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute, state: Int) {
        listener.onVariantOptionClicked(variantOptions)
    }

    override fun onVariantGuideLineHide(): Boolean {
        return true
    }

    override fun getStockWording(): String {
        return if (
            VariantUiModel.isVariantPartiallySelected(mVariantModel?.selectedVariants.orEmpty())
        ) ""
        else mVariantModel?.stockWording ?: getString(R.string.play_stock_available)
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    fun setAction(button: ProductButtonUiModel) {
        btnAction.text = button.text
        btnAction.generateButton(button.color)

        mAction = button.type.toAction
    }

    fun setVariantSheet(model: VariantUiModel) {
        mVariantModel = model

        showContent(shouldShow = true, withPlaceholder = false)

        ivProductImage.setImageUrl(model.variantDetail.imageUrl)
        when (model.variantDetail.price) {
            is DiscountedPrice -> {
                tvProductDiscount.show()
                tvOriginalPrice.show()
                tvProductDiscount.text = getString(
                    R.string.play_discount_percent,
                    model.variantDetail.price.discountPercent
                )
                tvOriginalPrice.text = model.variantDetail.price.originalPrice
                tvCurrentPrice.text = model.variantDetail.price.discountedPrice
            }
            is OriginalPrice -> {
                tvProductDiscount.hide()
                tvOriginalPrice.hide()
                tvCurrentPrice.text = model.variantDetail.price.price
            }
        }

        variantAdapter.setItemsAndAnimateChanges(model.categories)
        btnAction.isEnabled = !VariantUiModel.isVariantPartiallySelected(
            model.selectedVariants
        )
        btnAction.setOnClickListener {
            listener.onActionClicked(model.variantDetail, model.sectionInfo, mAction)
        }

        val labelList = model.selectedVariants.entries.mapIndexedNotNull { index, entry ->
            model.categories.getOrNull(index)?.variantOptions
                ?.find { it.variantId == entry.value }
                ?.variantName
        }
        labelAdapter.setItemsAndAnimateChanges(labelList)

        if (model.variantDetail.stock is StockAvailable) {
            tvProductStock.visibility = View.VISIBLE
            tvProductStock.text = getString(
                R.string.play_product_item_stock,
                ": ${model.variantDetail.stock.stock}"
            )
        } else tvProductStock.visibility = View.GONE
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

        when (product.price) {
            is DiscountedPrice -> {
                tvProductDiscount.show()
                tvOriginalPrice.show()
                tvProductDiscount.text = getString(R.string.play_discount_percent, product.price.discountPercent)
                tvOriginalPrice.text = product.price.originalPrice
                tvCurrentPrice.text = product.price.discountedPrice
            }
            is OriginalPrice -> {
                tvProductDiscount.hide()
                tvOriginalPrice.hide()
                tvCurrentPrice.text = product.price.price
            }
        }
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
        fun onVariantOptionClicked(option: VariantOptionWithAttribute)
        fun onActionClicked(variant: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, action: ProductAction)
    }
}
