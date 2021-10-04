package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.BenefitPackageSectionDecoration
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil.setTextMakeHyperlink
import com.tokopedia.power_merchant.subscribe.databinding.ItemBenefitPackageDataSectionBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageDataListener
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageGradeAdapter
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageDataUiModel
import com.tokopedia.utils.view.binding.viewBinding

class BenefitPackageDataViewHolder(
    view: View,
    private val benefitPackageDataListener: BenefitPackageDataListener
) : AbstractViewHolder<BenefitPackageDataUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_package_data_section
    }

    private val binding: ItemBenefitPackageDataSectionBinding? by viewBinding()

    override fun bind(element: BenefitPackageDataUiModel?) {
        binding?.run {
            tvAdditionalInfoBenefitPackage.text =
                MethodChecker.fromHtml(getString(R.string.pm_additional_info_benefit_package_section))

            tvLearnMoreBenefitPackage.setTextMakeHyperlink(
                getString(R.string.pm_learn_more_shop_performance)
            ) {
                benefitPackageDataListener.onLearnMoreToShopScoreClicked()
            }
        }
        setupBenefitPackageAdapter(element)
    }

    private fun setupBenefitPackageAdapter(element: BenefitPackageDataUiModel?) {
        val benefitPackageGradeAdapter = BenefitPackageGradeAdapter()
        binding?.run {
            rvBenefitPackageList.run {
                if (itemDecorationCount.isZero()) {
                    addItemDecoration(BenefitPackageSectionDecoration())
                }
                layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun canScrollVertically(): Boolean = false
                }
                isNestedScrollingEnabled = false
                adapter = benefitPackageGradeAdapter
            }
        }
        benefitPackageGradeAdapter.setBenefitPackageList(element?.benefitPackageData)
    }
}