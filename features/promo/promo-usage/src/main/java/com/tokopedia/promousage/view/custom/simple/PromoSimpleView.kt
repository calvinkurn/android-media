package com.tokopedia.promousage.view.custom.simple

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.promousage.databinding.PromoUsageItemPromoSimpleBinding
import com.tokopedia.promousage.domain.entity.list.PromoSimpleItem
import timber.log.Timber

class PromoSimpleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var _binding: PromoUsageItemPromoSimpleBinding? = null
    private val binding get() = _binding!!

    init {
        _binding = PromoUsageItemPromoSimpleBinding
            .inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(promo: PromoSimpleItem, isFullWidth: Boolean) {
        bindCardBackground(promo)
        bindIcon(promo, isFullWidth)
        bindTitleText(promo)
        bindTypeText(promo)
        bindDescText(promo)
    }

    private fun bindCardBackground(promo: PromoSimpleItem) {
        binding.promoIvBackgroundMiniCard.loadImageWithoutPlaceholder(promo.backgroundUrl)
    }

    private fun bindIcon(promo: PromoSimpleItem, isFullWidth: Boolean) {
        if (isFullWidth) {
            binding.promoIvIconBigMiniCard.loadImage(promo.iconUrl)
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
            binding.promoTvTypeMiniCard.setTextColor(
                Color.parseColor(promo.typeColor)
            )
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun bindDescText(promo: PromoSimpleItem) {
        binding.promoTvDescMiniCard.text = promo.desc
        binding.promoTvDescMiniCard.showWithCondition(promo.desc.isNotBlank())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clean up the binding when the view is detached from the window
        _binding = null
    }
}
