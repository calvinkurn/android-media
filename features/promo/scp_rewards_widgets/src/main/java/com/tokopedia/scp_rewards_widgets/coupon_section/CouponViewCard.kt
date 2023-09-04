package com.tokopedia.scp_rewards_widgets.coupon_section

//noinspection WrongResourceImportAlias
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.dpToPx
import com.tokopedia.scp_rewards_common.grayScaleFilter
import com.tokopedia.scp_rewards_common.parseColorOrFallback
import com.tokopedia.scp_rewards_widgets.databinding.ItemCouponLayoutBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.scp_rewards_widgets.R as scp_rewards_widgetsR
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR

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

    private fun applyEdgeTreatment(infoColor: Int) {
        val edgeTreatment = CouponCardEdgeTreatment(
                context,
                horizontalOffset = (binding.divider.top - SCALLOP_RADIUS).toFloat()
        )
                .apply {
                    scallopDiameter = (2 * SCALLOP_RADIUS).toFloat()
                }

        shapeAppearanceModel = ShapeAppearanceModel.Builder()
                .setRightEdge(edgeTreatment)
                .setAllCornerSizes(dpToPx(context, CORNER_RADIUS))
                .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        shapeDrawable.setTint(infoColor)

        val innerShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
                .apply {
                    setTint(ContextCompat.getColor(context, unifyPrinciplesR.color.Unify_NN0))
                }

        background = shapeDrawable
        binding.layoutDetails.background = innerShapeDrawable
    }

    fun setData(
            data: MedalBenefitModel,
            onApplyClick: (MedalBenefitModel) -> Unit = {},
            onCardTap: ((MedalBenefitModel) -> Unit)? = null
    ) {
        with(binding) {
            tvTitle.text = data.title
            val tncList = data.tncList
            if (!tncList.isNullOrEmpty()) {
                if (tncList.size > 1) {
                    tvDescription.text = data.tncList.joinToString(separator = "\n\u2022 ", prefix = "\u2022 ")
                } else {
                    tvDescription.text = data.tncList.first()
                }
            }

            ivBackground.setImageUrl(data.backgroundImageURL.orEmpty())
            ivMedalIcon.setImageUrl(data.medaliImageURL.orEmpty())
            ivBadgeBase.setImageUrl(data.podiumImageURL.orEmpty())
            ivStatus.setImageUrl(data.typeImageURL.orEmpty())
            applyColorToDrawable(data.typeBackgroundColor)
            tvExpiryLabel.text = data.statusDescription
            tvInfo.text = data.additionalInfoText

            updateLoadingStatus(data.isLoading)

            if (data.isActive) {
                btnApply.visible()
                ribbonStatus.gone()
                btnApply.text = data.cta?.text
                btnApply.setOnClickListener {
                    onApplyClick(data)
                }
            } else {
                ivBackground.grayScaleFilter()
                ivBadgeBase.grayScaleFilter()
                ivMedalIcon.grayScaleFilter()
                tvTitle.isEnabled = false
                tvDescription.isEnabled = false
                tvExpiryLabel.isEnabled = false
                tvInfo.isEnabled = false
                btnApply.gone()
                if (data.statusBadgeEnabled) {
                    ribbonStatus.visible()
                    ribbonStatus.setData(data.statusBadgeText, data.statusBadgeColor)
                }
            }
            onCardTap?.let {
                root.setOnClickListener { onCardTap(data) }
            }
            root.post {
                val additionalInfoColor = if (data.isActive) {
                    context.parseColorOrFallback(data.additionalInfoColor, scp_rewards_widgetsR.color.coupon_card_background)
                } else {
                    ContextCompat.getColor(context, unifyPrinciplesR.color.Unify_NN50)
                }
                applyEdgeTreatment(additionalInfoColor)
            }
        }
    }

    fun showHideInfo(toShow: Boolean) {
        if (toShow) {
            binding.tvInfo.visible()
        } else {
            binding.tvInfo.hide()
        }
    }

    fun updateLoadingStatus(showLoader: Boolean) {
        binding.btnApply.isLoading = showLoader
    }

    private fun ItemCouponLayoutBinding.applyColorToDrawable(color: String?) {
        val drawable = ContextCompat.getDrawable(context, scp_rewards_widgetsR.drawable.rounded_edge_rectangle)!!
        DrawableCompat.setTint(drawable, context.parseColorOrFallback(color = color))
        ivStatusBackground.background = drawable
    }
}
