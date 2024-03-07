package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.extensions.getDrawableChecker
import com.tokopedia.product.detail.databinding.ThumbnailVariantViewBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by yovi.putra on 13/01/23"
 * Project name: android-tokopedia-core
 **/

class ThumbnailVariantView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    val binding: ThumbnailVariantViewBinding

    init {
        inflate(context, R.layout.thumbnail_variant_view, this).apply {
            binding = ThumbnailVariantViewBinding.bind(this)
        }
    }

    fun setData(
        title: String,
        thumbnailUrl: String,
        showPromoIcon: Boolean
    ) {
        setTitle(title)
        setThumbnail(thumbnailUrl)
        showPromoIcon(showPromoIcon)
    }

    fun onClickListener(onClicked: () -> Unit) {
        binding.variantCard.setOnClickListener {
            onClicked.invoke()
        }
    }

    private fun setTitle(title: String) {
        binding.variantTitle.text = title
    }

    private fun setThumbnail(url: String) {
        binding.variantThumbnail.loadImage(url, properties = {
            centerCrop()
        })
    }

    private fun showPromoIcon(show: Boolean) {
        binding.variantPromoIcon.isVisible = show
    }

    fun setSelectedState() {
        setVariantTitleColor(unifyprinciplesR.color.Unify_GN500)
        setVariantTitleWeight(Typography.BOLD)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_selected)
        resetThumbGrayscale()
    }

    fun setSelectedStockEmptyState() {
        setDisableState()
        setVariantTitleColor(unifyprinciplesR.color.Unify_GN500)
        setVariantTitleWeight(Typography.BOLD)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_selected)
    }

    fun setUnselectedState() {
        setVariantTitleColor(unifyprinciplesR.color.Unify_NN600)
        setVariantTitleWeight(Typography.REGULAR)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_default)
        resetThumbGrayscale()
    }

    fun setDisableState() {
        setVariantTitleColor(unifyprinciplesR.color.Unify_NN400)
        setVariantTitleWeight(Typography.REGULAR)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_disable)
        setThumbGrayscale()
    }

    private fun setThumbGrayscale() {
        binding.variantOverlayEmpty.show()
        if (binding.variantThumbnail.colorFilter == null) {
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)
            binding.variantThumbnail.colorFilter = filter
        }
    }

    private fun resetThumbGrayscale() {
        binding.variantOverlayEmpty.hide()
        if (binding.variantThumbnail.colorFilter != null) {
            binding.variantThumbnail.colorFilter = null
        }
    }

    private fun setVariantTitleColor(resId: Int) {
        val color = getColor(resId)
        binding.variantTitle.setTextColor(color)
    }

    private fun setVariantTitleWeight(weight: Int){
        binding.variantTitle.setWeight(weight)
    }

    private fun setCardBorder(resId: Int) {
        val drawable = context.getDrawableChecker(resId)
        binding.variantCard.foreground = drawable
    }

    private fun getColor(resId: Int): Int {
        return context.getColorChecker(resId)
    }
}
