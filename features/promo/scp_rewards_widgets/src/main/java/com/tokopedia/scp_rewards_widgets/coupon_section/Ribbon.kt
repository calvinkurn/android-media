package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.scp_rewards_common.dpToPx
import com.tokopedia.scp_rewards_common.parseColorOrFallback
import com.tokopedia.scp_rewards_widgets.databinding.LayoutRibbonBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class Ribbon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    companion object {
        private const val CORNER_RADIUS = 6
    }

    private var binding = LayoutRibbonBinding.inflate(LayoutInflater.from(context), this)

    fun setData(statusBadgeText: String?, statusBadgeColor: String?) {
        binding.tvStatus.text = statusBadgeText
        applyBackgroundColor(statusBadgeColor)
    }

    private fun applyBackgroundColor(color: String?) {
        shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setAllCornerSizes(dpToPx(context, CORNER_RADIUS))
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        shapeDrawable.setTint(
            context.parseColorOrFallback(color, unifyprinciplesR.color.Unify_NN1000_32)
        )

        background = shapeDrawable
    }
}
