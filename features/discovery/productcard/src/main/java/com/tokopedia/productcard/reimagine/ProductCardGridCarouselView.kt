package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.experiments.ReimagineGridCarouselViewStrategy

class ProductCardGridCarouselView: ConstraintLayout {
    private val strategy = ReimagineGridCarouselViewStrategy(this)

    val additionalMarginStart: Int
        get() = strategy.additionalMarginStart

    constructor(context: Context) : super(context) {
        strategy.init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        strategy.init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        strategy.init(context, attrs, defStyleAttr)
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        strategy.setProductModel(productCardModel)
    }

    fun addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
        strategy.setImageProductViewHintListener(holder, object: ViewHintListener {
            override fun onViewHint() { onView() }
        })
    }

    fun setAddToCartOnClickListener(onClickListener: OnClickListener) {
        strategy.setAddToCartOnClickListener(onClickListener)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        strategy.setOnClickListener(l)
    }

    fun setOnClickListener(l: ProductCardClickListener) {
        strategy.setOnClickListener(l)
    }

    fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        strategy.setAddToCartNonVariantClickListener(addToCartNonVariantClickListener)
    }

    fun  setGenericCtaButtonOnClickListener(onClickListener: OnClickListener) {
        strategy.setGenericCtaButtonOnClickListener(onClickListener)
    }

    fun setGenericCtaSecondaryButtonOnClickListener(onClickListener: OnClickListener) {
        strategy.setGenericCtaSecondaryButtonOnClickListener(onClickListener)
    }

    /**
     * Used to re-render the generic cta button.
     * Not recommended for common use. Please copy model and rerender the whole card if possible.
     * Only created from request by Discovery team to prevent flashing UI when updating product card.
     * Please update the adapter's list productCardModel too to prevent wrong recycling render
     * if you use RecyclerView.
     * @param productCardModel ProductCardModel(Old) with updated CTA data
     */
    fun reRenderGenericCtaButton(productCardModel: com.tokopedia.productcard.ProductCardModel) {
        val reimagineModel = ProductCardModel.from(productCardModel)
        reRenderGenericCtaButton(reimagineModel)
    }

    /**
     * Used to re-render the generic cta button.
     * Not recommended for common use. Please copy model and rerender the whole card if possible.
     * Only created from request by Discovery team to prevent flashing UI when updating product card.
     * Please update the adapter's list productCardModel too to prevent wrong recycling render
     * if you use RecyclerView.
     * @param productCardModel ProductCardModel(Reimagine) with updated CTA data
     */
    fun reRenderGenericCtaButton(productCardModel: ProductCardModel) {
        strategy.reRenderGenericCtaButton(productCardModel)
    }

    fun recycle() {
        strategy.recycle()
    }
}
