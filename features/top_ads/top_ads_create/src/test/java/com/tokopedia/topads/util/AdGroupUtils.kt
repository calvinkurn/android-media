package com.tokopedia.topads.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.common.data.response.DashboardGroupDataType
import com.tokopedia.topads.common.data.response.DashboardGroupStatistic
import com.tokopedia.topads.common.data.response.GetTopAdsDashboardGroupsV3
import com.tokopedia.topads.common.data.response.GetTopadsDashboardGroupStatisticsV3
import com.tokopedia.topads.common.data.response.PageType
import com.tokopedia.topads.common.data.response.TopAdsGroupsResponse
import com.tokopedia.topads.common.data.response.TopAdsGroupsStatisticResponseResponse
import com.tokopedia.topads.data.AdGroupStatsData
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ReloadInfiniteUiModel
import org.junit.Assert

object AdGroupTestUtils {

    const val PER_PAGE = 20

    fun getAdGroupDummyData(page:Int = 1,total:Int = PER_PAGE): TopAdsGroupsResponse {
        return TopAdsGroupsResponse(
            GetTopAdsDashboardGroupsV3(
                data = getDummyAdGroupList(page),
                page = PageType(
                    perPage = PER_PAGE,
                    total = total
                )
            )
        )
    }

    fun getAdGroupStatsData(page:Int = 1,total:Int = PER_PAGE): TopAdsGroupsStatisticResponseResponse {
        return TopAdsGroupsStatisticResponseResponse(
            GetTopadsDashboardGroupStatisticsV3(
                page = PageType(
                    perPage = PER_PAGE,
                    total = total
                ),
                data = getDummyAdGroupStatList(page)
            )
        )
    }

    private fun getAdGroupCompleteDummyData(page:Int = 1,statLoading:Boolean = false): List<AdGroupUiModel> {
        return MutableList(PER_PAGE*page) {
            AdGroupUiModel(
                groupId = "$it",
                adGroupStats = AdGroupStatsData(loading = statLoading)
            )
        }
    }

    private fun getDummyAdGroupList(page:Int = 1): List<DashboardGroupDataType> {
        return MutableList(PER_PAGE) {
            DashboardGroupDataType(
                groupId = "${(page-1) * PER_PAGE + it}"
            )
        }
    }

    private fun getDummyAdGroupStatList(page:Int = 1): List<DashboardGroupStatistic> {
        return MutableList(PER_PAGE) {
            DashboardGroupStatistic(
                groupId = "${(page-1) * PER_PAGE + it}"
            )
        }
    }

    fun MutableList<Visitable<*>>.createAdGroupList(page: Int = 1){
        clear()
        add(CreateAdGroupUiModel())
        addAll(getAdGroupCompleteDummyData(page))
    }

    fun MutableList<Visitable<*>>.createAdGroupListWithStatsLoading(page: Int = 1){
        clear()
        add(CreateAdGroupUiModel())
        addAll(getAdGroupCompleteDummyData(page,true))
    }

    fun MutableList<Visitable<*>>.createAdGroupListError(errorType:Int){
        clear()
        add(CreateAdGroupUiModel())
        add(ErrorUiModel(errorType))
    }

    fun MutableList<Visitable<*>>.addInfiniteLoadingError(){
        add(ReloadInfiniteUiModel())
    }

    fun MutableList<Visitable<*>>.chooseAdGroup(index:Int){
        if(index < size){
          if(this[index] is AdGroupUiModel){
              forEachIndexed{ idx,elem ->
                  if(elem is AdGroupUiModel && elem.selected){
                      this[idx] = elem.copy(selected = false)
                  }
              }
              val old = this[index] as AdGroupUiModel
              this[index] = old.copy(selected = true)
          }
        }
    }
}

fun List<Visitable<*>>.assertLists(expected:List<Visitable<*>>){
    var equal = true
    if(size == expected.size){
        forEachIndexed{ idx,_->
            if(this[idx].toString() != expected[idx].toString()){
                equal = false
            }
        }
    }
    else equal = false
    Assert.assertTrue(equal)
}
