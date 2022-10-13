package com.tokopedia.tkpd.flashsale.presentation.detail.mapper

import android.content.Context
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignProductCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignTimelineFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import javax.inject.Inject

class CampaignDetailUIMapper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val SHOWING_TAB_THRESHOLD = 1
    }

    fun mapToFragments(campaignDetailBottomSheetModel: CampaignDetailBottomSheetModel): List<Pair<String, Fragment>> {
        val result: MutableList<Pair<String, Fragment>> = mutableListOf()

        if (campaignDetailBottomSheetModel.showTimeline) {
            val timelineTitle = context.getString(R.string.campaigndetail_timeline_title)
            val timelineSteps = campaignDetailBottomSheetModel.timelineSteps
            val timelineFragment = CampaignTimelineFragment.newInstance(timelineSteps)
            result.add(Pair(timelineTitle, timelineFragment))
        }

        if (campaignDetailBottomSheetModel.showProductCriteria) {
            val criteriaTitle = context.getString(R.string.campaigndetail_product_criteria_title)
            val productCriterias = campaignDetailBottomSheetModel.productCriterias
            val productCriteriaFragment = CampaignProductCriteriaFragment.newInstance(productCriterias)
            result.add(Pair(criteriaTitle, productCriteriaFragment))
        }

        if (campaignDetailBottomSheetModel.showCriteria) {
            val productCriteriaTitle = context.getString(R.string.campaigndetail_criteria_title)
            val criteriaFragment = CampaignCriteriaFragment()
            result.add(Pair(productCriteriaTitle, criteriaFragment))
        }

        return result
    }

    fun getIsShowTab(fragmentList: List<Pair<String, Fragment>>) = fragmentList.size > SHOWING_TAB_THRESHOLD
}
