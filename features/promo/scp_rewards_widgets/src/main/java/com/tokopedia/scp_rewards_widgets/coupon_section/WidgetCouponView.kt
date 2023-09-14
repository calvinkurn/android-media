package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.scp_rewards_common.loadImageOrFallback
import com.tokopedia.scp_rewards_common.parseColor
import com.tokopedia.scp_rewards_widgets.constants.CouponStatus
import com.tokopedia.scp_rewards_widgets.databinding.WidgetCouponViewBinding
import com.tokopedia.scp_rewards_widgets.model.FilterModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitSectionModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        handleCouponFilters(
            benefitSectionModel.filters,
            benefitSectionModel.benefitList
        ) { filteredList ->
            if (filteredList.isNullOrEmpty()) {
                binding.stackCoupon.hide()
                binding.cardEmptyCoupon.hide()
                binding.ivErrorState.visible()
            } else {
                handleCouponState(
                    filteredList,
                    benefitSectionModel,
                    onApplyClick,
                    onCardTap,
                    onCtaClick
                )
            }
        }
        requestLayout()
    }

    private fun handleCouponFilters(
        filters: List<FilterModel>?,
        benefitList: List<MedalBenefitModel>?,
        setOnFilterListener: (List<MedalBenefitModel>?) -> Unit
    ) {
        if (filters != null && filters.isEmpty().not() &&
            benefitList != null && benefitList.isEmpty().not()
        ) {
            val list = filters.map { filter ->
                SortFilterItem(
                    title = filter.text.orEmpty(),
                    iconUrl = filter.iconImageURL.orEmpty(),
                    listener = {
                        filters.forEach { it.isSelected = false }
                        filter.isSelected = true
                        filterData(benefitList, filter, setOnFilterListener)
                    }
                ).apply {
                    type = if (filter.isSelected) {
                        ChipsUnify.TYPE_SELECTED
                    } else {
                        ChipsUnify.TYPE_NORMAL
                    }
                }
            }
            setFiltersLeftPadding()
            binding.filterCoupon.addItem(list as ArrayList<SortFilterItem>)
            filterData(benefitList, filters.find { it.isSelected }, setOnFilterListener)
        } else {
            binding.filterCoupon.gone()
            setOnFilterListener(benefitList)
        }
    }

    private fun filterData(
        benefitList: List<MedalBenefitModel>,
        filter: FilterModel?,
        setOnFilterListener: (List<MedalBenefitModel>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            var filteredList = benefitList.filter { benefit ->
                benefit.categoryIds?.contains(filter?.id) ?: false
            }

            if (filteredList.isEmpty()) {
                filteredList = benefitList
            }

            withContext(Dispatchers.Main) {
                setOnFilterListener(filteredList)
            }
        }

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
            CouponStatus.EMPTY -> {
                binding.btnViewMore.hide()
                binding.ivErrorState.hide()
                binding.stackCoupon.hide()
                binding.cardEmptyCoupon.visible()
                binding.cardEmptyCoupon.setData(benefit, onCtaClick)
            }

            CouponStatus.ERROR -> {
                binding.btnViewMore.hide()
                binding.cardEmptyCoupon.hide()
                binding.stackCoupon.hide()
                binding.ivErrorState.visible()
                binding.ivErrorState.loadImageOrFallback(
                    benefit.medaliImageURL,
                    scp_rewards_widgetsR.drawable.ic_coupon_error
                )
            }

            else -> {
                binding.ivErrorState.hide()
                binding.cardEmptyCoupon.hide()
                binding.stackCoupon.visible()
                binding.stackCoupon.setData(
                    benefitList,
                    benefitSectionModel.benefitInfo,
                    onApplyClick,
                    onCardTap
                )

                if (benefitSectionModel.cta?.text.isNullOrEmpty().not()) {
                    binding.btnViewMore.visible()
                    binding.btnViewMore.text = benefitSectionModel.cta?.text
                    binding.btnViewMore.applyStyle(benefitSectionModel.cta?.style)
                    binding.btnViewMore.setOnClickListener {
                        onCtaClick(
                            benefitSectionModel.cta?.appLink,
                            benefitSectionModel.cta?.deepLink
                        )
                    }
                } else {
                    binding.btnViewMore.hide()
                }
            }
        }
    }

    private fun setFiltersLeftPadding() {
        // Adding padding to the internal view so that the filters can scroll along with padding
        binding.filterCoupon.sortFilterItems.apply {
            post { setPadding(16.toPx(), paddingTop, paddingRight, paddingBottom) }
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
