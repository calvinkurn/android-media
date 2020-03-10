package com.tokopedia.play.ui.variantsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.productsheet.ProductSheetView
import com.tokopedia.play.ui.variantsheet.adapter.VariantAdapter
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.variant_common.model.ProductDetailVariantCommonResponse
import com.tokopedia.variant_common.model.VariantOptionWithAttribute
import com.tokopedia.variant_common.util.VariantCommonMapper
import com.tokopedia.variant_common.view.ProductVariantListener

/**
 * Created by jegul on 05/03/20
 */
class VariantSheetView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_variant_sheet, container, true)
            .findViewById(R.id.cl_variant_sheet)

    private val clVariantContent: ConstraintLayout = view.findViewById(R.id.cl_variant_content)
    private val tvSheetTitle: TextView = view.findViewById(R.id.tv_sheet_title)
    private val rvVariantList: RecyclerView = view.findViewById(R.id.rv_variant_list)
    private val btnAction: UnifyButton = view.findViewById(R.id.btn_action)
    private val btnContainer: FrameLayout = view.findViewById(R.id.btn_container)
    private val vBottomOverlay: View = view.findViewById(R.id.v_bottom_overlay)
    private val ivProductImage: ImageView = view.findViewById(R.id.iv_product_image)
    private val tvProductTitle: TextView = view.findViewById(R.id.tv_product_title)
    private val llProductDiscount: LinearLayout = view.findViewById(R.id.ll_product_discount)
    private val tvProductDiscount: TextView = view.findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = view.findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = view.findViewById(R.id.tv_current_price)

    private val imageRadius = view.resources.getDimensionPixelSize(R.dimen.play_product_line_image_radius).toFloat()
    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    private val productVariantListener: ProductVariantListener = object: ProductVariantListener {
        override fun onVariantClicked(variantOptions: VariantOptionWithAttribute) {
            // mapping ulang
        }

        override fun onVariantGuideLineClicked(url: String) {
            // startActivity(ImagePreviewActivity.getCallingIntent(it, arrayListOf(url)))
        }

        override fun getStockWording(): String {
            return "Stok tinggal segini, segimana?hmm"
        }
    }
    private val variantAdapter: VariantAdapter = VariantAdapter(productVariantListener)

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
    }

    override val containerId: Int = view.id

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    internal fun setStateHidden() {
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
        tvSheetTitle.text = model.title
        ivProductImage.loadImageRounded(model.product.imageUrl, imageRadius)
        tvProductTitle.text = model.product.title

        when (model.product.price) {
            is DiscountedPrice -> {
                llProductDiscount.visible()
                tvProductDiscount.text = view.context.getString(R.string.play_discount_percent, model.product.price.discountPercent)
                tvOriginalPrice.text = model.product.price.originalPrice
                tvCurrentPrice.text = model.product.price.discountedPrice
            }
            is OriginalPrice -> {
                llProductDiscount.gone()
                tvCurrentPrice.text = model.product.price.price
            }
        }

        btnAction.text = view.context.getString(
                if (model.action == ProductAction.Buy) R.string.play_buy
                else R.string.play_add_to_card
        )

        btnAction.setOnClickListener {
            if (model.action == ProductAction.Buy) listener.onBuyClicked(this, model.product.id)
            else listener.onAddToCartClicked(this, model.product.id)
        }
    }

    internal fun setDynamicVariant(productDetailVariant: ProductDetailVariantCommonResponse) {
        rvVariantList.adapter = variantAdapter
        VariantCommonMapper.processVariant(productDetailVariant.data)?.let {
            variantAdapter.setItems(it)
            variantAdapter.notifyDataSetChanged()
        }
    }

    interface Listener {
        fun onCloseButtonClicked(view: VariantSheetView)
        fun onAddToCartClicked(view: VariantSheetView, productId: String)
        fun onBuyClicked(view: VariantSheetView, productId: String)
    }
}