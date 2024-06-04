package com.tokopedia.productcard.reimagine.cta

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.R
import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.reimagine.showView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifycomponents.R as unifycomponentsR

internal class ProductCardGenericCtaExtension(
    private val view: View,
    private val type: ProductCardType
) {
    private val cardConstraintLayout by view.lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)

    var ctaClickListener: ((View) -> Unit)? = null
    var ctaSecondaryClickListener: ((View) -> Unit)? = null

    private val productCardCta: LinearLayout?
        get() = view.findViewById(R.id.productCardGenericCta)
    var productCardCtaButton: UnifyButton? = null
        private set
    var productCardCtaSecondaryButton: UnifyImageButton? = null
        private set

    fun render(productCardModel: ProductCardModel) {
        val cardConstraintLayout = cardConstraintLayout ?: return
        view.showView(R.id.productCardGenericCta, productCardModel.shouldShowGenericCta()) {
            GenericCtaLayout(cardConstraintLayout, type.genericCtaConstraints())
        }
        productCardCtaButton = productCardCta?.findViewById(R.id.productCardGenericCtaMain)
        productCardCtaSecondaryButton = productCardCta?.findViewById(R.id.productCardGenericCtaSecondary)

        val item = productCardModel.productCardGenericCta ?: return
        renderCtaButtonMain(item)
        renderCtaButtonSecondary(item)

        productCardCtaButton?.setOnClickListener {
            ctaClickListener?.invoke(it)
        }
        productCardCtaSecondaryButton?.setOnClickListener {
            ctaSecondaryClickListener?.invoke(it)
        }

        handleColorMode(productCardModel.colorMode)
    }

    private fun renderCtaButtonMain(item: ProductCardModel.ProductCardGenericCta) {
        productCardCtaButton?.text = item.copyWriting
        productCardCtaButton?.buttonVariant = item.mainButtonVariant
        productCardCtaButton?.buttonType = item.mainButtonType
    }

    private fun renderCtaButtonSecondary(item: ProductCardModel.ProductCardGenericCta) {
        if (productCardCtaSecondaryButton == null) return
        if (!item.shouldShowSecondaryCta) {
            productCardCtaSecondaryButton?.visibility = View.GONE
            return
        }
        productCardCtaSecondaryButton?.run {
            // Set Drawable image
            loadImage(item.secondaryCtaIconResource ?: R.drawable.product_card_ic_three_dots)
            setColorFilter(item.secondaryCtaIconResourceColorToken ?: ContextCompat.getColor(context, unifycomponentsR.color.Unify_NN600))
            // Set Background & Border
            val backgroundColorToken = item.secondaryCtaBackgroundColorToken ?: ContextCompat.getColor(context, unifycomponentsR.color.Unify_NN0)
            val borderColorToken = item.secondaryCtaBorderColorToken ?: ContextCompat.getColor(context, unifycomponentsR.color.Unify_NN300)
            val enableFillDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(backgroundColorToken)
                setStroke(resources.getDimensionPixelSize(unifycomponentsR.dimen.button_stroke_width), borderColorToken)
                cornerRadius = resources.getDimension(unifycomponentsR.dimen.button_corner_radius)
                clipToOutline = true
            }
            val stateListDefaultDrawable = StateListDrawable().apply {
                addState(intArrayOf(-android.R.attr.state_enabled), enableFillDrawable)
                addState(intArrayOf(android.R.attr.state_enabled), enableFillDrawable)
            }
            background = stateListDefaultDrawable
        }
    }

    private fun handleColorMode(colorMode: ProductCardColor?) {
        colorMode?.buttonColorMode?.let { buttonColorMode ->
            productCardCtaButton?.applyColorMode(buttonColorMode)
        }
    }
}
