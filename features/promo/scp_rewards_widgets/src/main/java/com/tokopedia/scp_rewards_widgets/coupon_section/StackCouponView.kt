package com.tokopedia.scp_rewards_widgets.coupon_section

//noinspection WrongResourceImportAlias
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.dpToPx
import com.tokopedia.scp_rewards_widgets.databinding.StackCouponLayoutBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@SuppressLint("RestrictedApi")
class StackCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding = StackCouponLayoutBinding.inflate(LayoutInflater.from(context), this)

    companion object {
        private const val CORNER_RADIUS = 12
    }

    fun setData(
        list: List<MedalBenefitModel>,
        benefitInfo: String?,
        onApplyClick: (MedalBenefitModel) -> Unit = {},
        onCardTap: (MedalBenefitModel, Boolean) -> Unit = { _, _ -> }
    ) {
        with(binding) {
            cardFront.setData(list.first(), onApplyClick) {
                onCardTap(it, list.size == 1)
            }
            if (list.size == 1) {
                cardMore.hide()
                cardMiddle.hide()
                cardBack.hide()
            } else {
                cardMiddle.visible()
                cardBack.visible()

                val duplicate = list.first().copy().apply { statusBadgeEnabled = false }
                cardMiddle.setData(duplicate)
                cardBack.setData(duplicate)

                if (benefitInfo.isNullOrEmpty()) {
                    cardMore.hide()
                } else {
                    cardMore.visible()
                    cardMore.apply {
                        shapeAppearanceModel = ShapeAppearanceModel.Builder()
                            .setAllCornerSizes(0f)
                            .setTopLeftCornerSize(dpToPx(context, CORNER_RADIUS))
                            .setTopRightCornerSize(dpToPx(context, CORNER_RADIUS))
                            .build()

                        background = MaterialShapeDrawable(shapeAppearanceModel).apply {
                            setTint(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0))
                        }
                    }

                    tvMore.text = benefitInfo.replace("[number]", "${list.size}")
                }
            }
        }
    }

    fun updateLoadingStatus(showLoader: Boolean) {
        binding.cardFront.updateLoadingStatus(showLoader)
    }
}
