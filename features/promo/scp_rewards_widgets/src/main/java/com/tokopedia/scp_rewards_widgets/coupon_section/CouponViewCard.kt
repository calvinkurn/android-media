package com.tokopedia.scp_rewards_widgets.coupon_section

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.scp_rewards_common.dpToPx
import com.tokopedia.scp_rewards_widgets.databinding.ItemCouponLayoutBinding

@SuppressLint("RestrictedApi")
class CouponViewCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private var binding = ItemCouponLayoutBinding.inflate(LayoutInflater.from(context), this)

    companion object {
        private const val SCALLOP_RADIUS = 30
        private const val CORNER_RADIUS = 15
        private const val CARD_ELEVATION = 10f
    }

    private fun applyEdgeTreatment() {
        val edgeTreatment = CouponCardEdgeTreatment(
            context,
            horizontalOffset = (binding.divider.top - SCALLOP_RADIUS).toFloat())
            .apply {
                scallopDiameter = (2 * SCALLOP_RADIUS).toFloat()
            }

        shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setRightEdge(edgeTreatment)
            .setAllCornerSizes(dpToPx(context, CORNER_RADIUS))
            .build()

        elevation = CARD_ELEVATION

    }

    fun setData() {
        with(binding) {
            tvTitle.text = "asda"
            tvDescription.text = "asda"
            tvAdditional.text = "asda"

            divider.post { applyEdgeTreatment() }
        }
    }
}
