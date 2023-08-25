package com.tokopedia.scp_rewards_widgets.coupon_section

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.dpToPx
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.databinding.ItemCouponLayoutBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel

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
    }

    private fun applyEdgeTreatment(infoColor: String?) {
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
        shapeDrawable.setTint(
            parseColor(infoColor) ?: ContextCompat.getColor(
                context,
                com.tokopedia.scp_rewards_widgets.R.color.coupon_card_background
            )
        )

        val innerShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
            .apply {
                setTint(ContextCompat.getColor(context, com.tokopedia.scp_rewards_widgets.R.color.Unify_NN0))
            }

        background = shapeDrawable
        binding.layoutDetails.background = innerShapeDrawable
    }

    fun setData(data: MedalBenefitModel, onApplyClick: (String?) -> Unit) {
        with(binding) {
            tvTitle.text = data.title
            if (data.tncList != null) {
                if (data.tncList.size > 1) {
                    tvDescription.text = data.tncList.joinToString(separator = "\n\u2022 ", prefix = "\u2022 ")
                } else {
                    tvDescription.text = data.tncList.first()
                }
            }

            ivBackground.setImageUrl(data.backgroundImageURL.orEmpty())
            ivMedalIcon.setImageUrl(data.medaliImageURL.orEmpty())
            ivBadgeBase.setImageUrl(data.podiumImageURL.orEmpty())
            ivRibbon.setImageUrl(data.typeImageURL.orEmpty())
            tvExpiryLabel.text = data.statusDescription
            tvInfo.text = data.additionalInfoText

            if (data.status.contentEquals("Active", true)) {
                btnApply.visible()
                ribbonStatus.gone()
                btnApply.text = data.cta?.text
                btnApply.setOnClickListener {
                    onApplyClick(data.cta?.appLink)
                }
            } else {
//                ribbonStatus.visible()
                btnApply.gone()
//                ribbonStatus.setData(data.statusBadgeText, data.statusBadgeColor)
            }
            root.post {
                applyEdgeTreatment(data.additionalInfoColor)
            }
        }
    }
}
