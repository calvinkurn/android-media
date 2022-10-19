package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductCardViewBinding
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen

class TokoNowProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private var binding: LayoutTokopedianowProductCardViewBinding

    var type: TokoNowProductCardType = TokoNowProductCardType.NORMAL
        set(value) {
            field = value
            setupUi(value)
        }

    init {
        binding = LayoutTokopedianowProductCardViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
            imageView.loadImage("https://slack-imgs.com/?c=1&o1=ro&url=https%3A%2F%2Fimages.tokopedia.net%2Fimg%2Fandroid%2Fnow%2FPN-RICH.jpg")
            mainPriceTypography.text = "Rp.1.500.000"
            discountLabel.setLabel("100%")
            slashedPriceTypography.text = "9999999"
            productNameTypography.text = "Strawberry impor korea"
            categoryInfoTypography.text = "100gr"
            ratingTypography.text = "4.5"
        }

        type = TokoNowProductCardType.OOS

    }

    private fun setupUi(type: TokoNowProductCardType) {
        when (type) {
            TokoNowProductCardType.NORMAL -> {
                binding.similarProductTypography.hide()
                val constraintLayout = binding.root
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.clear(R.id.rating_typography, ConstraintSet.END)
                constraintSet.clear(R.id.rating_icon, ConstraintSet.START)
                constraintSet.setVerticalBias(binding.ratingTypography.id, 1.0f)
                constraintSet.connect(
                    binding.ratingTypography.id,
                    ConstraintSet.START,
                    binding.ratingIcon.id,
                    ConstraintSet.END,
                    getDpFromDimen(context, R.dimen.tokopedianow_product_card_rating_typography_start_margin_normal_state).toIntSafely()
                )
                constraintSet.connect(
                    binding.ratingIcon.id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    getDpFromDimen(context, R.dimen.tokopedianow_product_card_rating_icon_start_margin_normal_state).toIntSafely()
                )
                constraintSet.connect(
                    binding.ratingTypography.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    getDpFromDimen(context, R.dimen.tokopedianow_product_card_rating_typography_bottom_margin_normal_state).toIntSafely()
                )
                constraintSet.applyTo(constraintLayout)
            }
            TokoNowProductCardType.OOS -> {
                binding.similarProductTypography.show()
            }
            TokoNowProductCardType.FLASH_SALE -> {
                binding.progressBar.show()
                binding.progressTypography.show()
                binding.ratingIcon.hide()
                binding.ratingTypography.hide()
            }
        }
    }

}

enum class TokoNowProductCardType {
    NORMAL, OOS, FLASH_SALE
}
