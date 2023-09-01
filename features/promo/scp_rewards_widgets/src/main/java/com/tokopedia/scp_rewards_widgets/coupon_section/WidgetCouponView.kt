package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.loadImageOrFallback
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.constants.CouponState
import com.tokopedia.scp_rewards_widgets.databinding.WidgetCouponViewBinding
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitSectionModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.scp_rewards_widgets.R as scp_rewards_widgetsR

class WidgetCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = WidgetCouponViewBinding.inflate(LayoutInflater.from(context), this)

    private companion object {
        private const val STYLE_GHOST = "ghost"
    }

    fun renderCoupons(
        benefitSectionModel: MedalBenefitSectionModel,
        onApplyClick: (MedalBenefitModel) -> Unit = {},
        onCardTap: (MedalBenefitModel, Boolean) -> Unit = { _, _ -> },
        onCtaClick: (String?, String?) -> Unit = { _, _ -> },
        onErrorAction: () -> Unit = {}
    ) {
        binding.tvTitle.text = benefitSectionModel.title
        setBackgroundColor(parseColor(benefitSectionModel.backgroundColor) ?: Color.WHITE)
        val benefitList = benefitSectionModel.benefitList

        if (benefitList.isNullOrEmpty()) {
            binding.stackCoupon.hide()
            binding.cardEmptyCoupon.hide()
            binding.ivErrorState.visible()
        } else {
            handleCouponState(benefitList, benefitSectionModel, onApplyClick, onCardTap, onCtaClick)
        }
        requestLayout()
    }

    fun updateLoadingStatus(showLoader: Boolean) {
        binding.stackCoupon.updateLoadingStatus(showLoader)
    }

    private fun handleCouponState(
        benefitList: List<MedalBenefitModel>,
        benefitSectionModel: MedalBenefitSectionModel,
        onApplyClick: (MedalBenefitModel) -> Unit = {},
        onCardTap: (MedalBenefitModel, Boolean) -> Unit = { _, _ -> },
        onCtaClick: (String?, String?) -> Unit = { _, _ -> }
    ) {
        val benefit = benefitList.first()
        when (benefit.status) {
            CouponState.EMPTY -> {
                binding.ivErrorState.hide()
                binding.cardEmptyCoupon.visible()
                binding.cardEmptyCoupon.setData(benefit, onCtaClick)
            }

            CouponState.ERROR -> {
                binding.cardEmptyCoupon.hide()
                binding.ivErrorState.visible()
                binding.ivErrorState.loadImageOrFallback(
                    benefit.medaliImageURL,
                    scp_rewards_widgetsR.drawable.ic_coupon_error
                )
            }

            else -> {
                binding.ivErrorState.hide()
                binding.cardEmptyCoupon.hide()
                binding.ivErrorState.hide()
                binding.cardEmptyCoupon.hide()
                binding.stackCoupon.visible()
                binding.stackCoupon.setData(benefitList, benefitSectionModel.benefitInfo, onApplyClick, onCardTap)

                if (benefitList.size > 1 && benefitSectionModel.cta?.text.isNullOrEmpty().not()) {
                    binding.btnViewMore.visible()
                    binding.btnViewMore.text = benefitSectionModel.cta?.text
                    binding.btnViewMore.applyStyle(benefitSectionModel.cta?.style)
                    binding.btnViewMore.setOnClickListener {
                        onCtaClick(benefitSectionModel.cta?.appLink, benefitSectionModel.cta?.deepLink)
                    }
                }
            }
        }
    }

    private fun UnifyButton.applyStyle(style: String?) {
        when (style) {
            STYLE_GHOST -> {
                this.buttonType = UnifyButton.Type.MAIN
                this.buttonSize = UnifyButton.Size.SMALL
                this.buttonVariant = UnifyButton.Variant.GHOST
            }

            else -> {
                this.buttonType = UnifyButton.Type.MAIN
                this.buttonVariant = UnifyButton.Variant.FILLED
                this.buttonVariant = UnifyButton.Variant.GHOST
            }
        }
    }
}
