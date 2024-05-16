package com.tokopedia.promousage.view.custom.simple

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.promousage.databinding.PromoUsageItemPromoSimpleBinding
import com.tokopedia.promousage.domain.entity.list.PromoSimpleItem
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import timber.log.Timber

class PromoSimpleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: PromoUsageItemPromoSimpleBinding

    init {
        binding = PromoUsageItemPromoSimpleBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun bind(promo: PromoSimpleItem, isFullWidth: Boolean) {
        bindCircleCut(promo)
        bindCardBackground(promo)
        bindIcon(promo, isFullWidth)
        bindTitleText(promo)
        bindTypeText(promo)
        bindDescText(promo)
    }

    private fun bindCircleCut(promo: PromoSimpleItem) {
        val finalColor = if (this.context.isDarkMode()) {
            promo.curveColorDark
        } else {
            promo.curveColor
        }
        val finalAlpha = if (this.context.isDarkMode()) {
            promo.curveAlphaDark
        } else {
            promo.curveAlpha
        }
        binding.promoMiniCard.changeCircleCutColor(finalColor, finalAlpha)
    }

    private fun bindCardBackground(promo: PromoSimpleItem) {
        val backgroundUrl = if (this.context.isDarkMode()) {
            promo.backgroundUrlDark
        } else {
            promo.backgroundUrl
        }
        binding.promoIvBackgroundMiniCard.loadImageWithoutPlaceholder(backgroundUrl)
    }

    private fun bindIcon(promo: PromoSimpleItem, isFullWidth: Boolean) {
        val iconUrl = if (this.context.isDarkMode()) {
            promo.iconUrlDark
        } else {
            promo.iconUrl
        }
        if (isFullWidth) {
            binding.promoIvIconBigMiniCard.loadImage(iconUrl)
            binding.promoIvIconBigMiniCard.show()
            binding.promoIvIconSmallMiniCard.hide()
        } else {
            binding.promoIvIconSmallMiniCard.loadImage(promo.iconUrl)
            binding.promoIvIconSmallMiniCard.show()
            binding.promoIvIconBigMiniCard.hide()
        }
    }

    private fun bindTitleText(promo: PromoSimpleItem) {
        binding.promoTvTitleMiniCard.text = promo.title
    }

    private fun bindTypeText(promo: PromoSimpleItem) {
        binding.promoTvTypeMiniCard.text = promo.type
        try {
            val color = if (this.context.isDarkMode()) {
                promo.typeColorDark
            } else {
                promo.typeColor
            }
            binding.promoTvTypeMiniCard.setTextColor(
                Color.parseColor(color)
            )
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun bindDescText(promo: PromoSimpleItem) {
        binding.promoTvDescMiniCard.text = promo.desc
        binding.promoTvDescMiniCard.showWithCondition(promo.desc.isNotBlank())
    }

    fun cleanUp() {
        binding.promoIvBackgroundMiniCard.clearImage()
        binding.promoIvIconBigMiniCard.clearImage()
        binding.promoIvIconSmallMiniCard.clearImage()
    }
}
