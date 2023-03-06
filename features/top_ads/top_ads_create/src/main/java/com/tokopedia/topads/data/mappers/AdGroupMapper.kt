package com.tokopedia.topads.data.mappers

import com.tokopedia.topads.common.data.response.TopAdsGroupsResponse
import com.tokopedia.topads.common.data.response.TopAdsGroupsStatisticResponseResponse
import com.tokopedia.topads.data.AdGroupCompleteData
import com.tokopedia.topads.data.AdGroupSettingData
import com.tokopedia.topads.data.AdGroupStatsData
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel

object AdGroupMapper {

    fun getAdGroupsFromDashboardResponse(data:TopAdsGroupsResponse) : Pair<List<AdGroupCompleteData>,List<AdGroupUiModel>>{
        val uiList = mutableListOf<AdGroupUiModel>()
        val groupList = mutableListOf<AdGroupCompleteData>()
        data.response?.data?.forEach {
            uiList.add(
                AdGroupUiModel(
                    groupId = it.groupId,
                    groupName = it.groupName,
                    adGroupSetting = AdGroupSettingData(
                        productBrowse = it.groupBidSetting.productBrowse,
                        productSearch = it.groupBidSetting.productSearch,
                    ),
                    adGroupStats = AdGroupStatsData(
                        loading = true
                    )
                )
            )
            groupList.add(
                AdGroupCompleteData(
                    groupId = it.groupId,
                    groupName = it.groupName,
                    productBrowse = it.groupBidSetting.productBrowse,
                    productSearch = it.groupBidSetting.productSearch,
                )
            )
        }
        return Pair(groupList,uiList)
    }

    fun getAdGroupStatsFromResponse(data: TopAdsGroupsStatisticResponseResponse) : List<AdGroupStatsData>{
       val statList = mutableListOf<AdGroupStatsData>()
        data.response?.data?.forEach {
            statList.add(
                AdGroupStatsData(
                    totalImpressionStats = it.statTotalImpression,
                    totalClickStats = it.statTotatClick,
                    totalConversionStats = it.statTotalConversion
                )
            )
        }
        return statList
    }

}
