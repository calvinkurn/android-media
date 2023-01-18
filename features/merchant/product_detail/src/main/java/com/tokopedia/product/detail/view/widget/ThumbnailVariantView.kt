package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.extensions.getDrawableChecker
import com.tokopedia.product.detail.databinding.ThumbnailVariantViewBinding

/**
 * Created by yovi.putra on 13/01/23"
 * Project name: android-tokopedia-core
 **/


class ThumbnailVariantView(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

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
        setVariantTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        setCardBackground(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_selected)
    }

    fun setSelectedStockEmptyState() {
        setSelectedState()
        setThumbGrayscale()
        setCardBackground(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_selected)
    }

    fun setUnselectedState() {
        setVariantTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
        setCardBackground(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_default)
    }

    fun setDisableState() {
        setVariantTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_NN400)
        setCardBackground(com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        setCardBorder(R.drawable.pdp_thumbnail_variant_border_disable)
        setThumbGrayscale()
    }

    private fun setThumbGrayscale() {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        binding.variantThumbnail.colorFilter = filter
    }

    private fun setVariantTitleColor(resId: Int) {
        binding.variantTitle.setTextColor(getColor(resId))
    }

    private fun setCardBorder(resId: Int) {
        binding.variantCard.foreground = context.getDrawableChecker(resId)
    }

    private fun setCardBackground(colorId: Int) {
        binding.variantCard.setCardBackgroundColor(context.getColorChecker(colorId))
    }

    private fun getColor(resId: Int): Int {
        return context.getColorChecker(resId)
    }
}
