package com.tokopedia.scp_rewards_widgets.coupon_section

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.MaterialShapeDrawable
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
        private const val CORNER_RADIUS = 12
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

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
            .apply {
                setTint(ContextCompat.getColor(context, com.tokopedia.scp_rewards_widgets.R.color.coupon_card_background))
            }

        val innerShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
            .apply {
                setTint(ContextCompat.getColor(context, com.tokopedia.scp_rewards_widgets.R.color.Unify_NN0))
            }

        background = shapeDrawable
        binding.layoutDetails.background = innerShapeDrawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        applyEdgeTreatment()
        elevation = CARD_ELEVATION
        clipToOutline = false
        clipChildren = false
        clipToPadding = false
    }

    fun setData() {
        with(binding) {
            tvTitle.text = "asda"
            tvDescription.text = "asda\nasd\nasdf\nasdasd"
            tvAdditional.text = "asda"
            tvInfo.text = "asda"

            ribbonStatus.setData()
        }
    }
}
