package com.tokopedia.kol.feature.following_list.data.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kol.feature.following_list.data.pojo.DataItem
import com.tokopedia.kol.feature.following_list.data.pojo.FollowerListData
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel
import rx.functions.Func1
import java.util.ArrayList
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-22
 */
class KolFollowerMapper @Inject constructor() : Func1<GraphqlResponse, KolFollowingResultViewModel> {

    override fun call(t: GraphqlResponse?): KolFollowingResultViewModel {
        val data = t?.getData<FollowerListData?>(FollowerListData::class.java)
        data?.let {
            return mapFollowersData(data)
        }
        return KolFollowingResultViewModel()
    }
    private fun mapFollowersData(data: FollowerListData): KolFollowingResultViewModel {
        return KolFollowingResultViewModel(
                !data.feedGetUserFollowers.meta.nextCursor.isEmpty(),
                mapFollowData(data.feedGetUserFollowers.data),
                data.feedGetUserFollowers.meta.nextCursor,
                "",
                ""
        )
    }

    private fun mapFollowData(dataList: List<DataItem>): List<KolFollowingViewModel> {
        val resultList = ArrayList<KolFollowingViewModel>()
        for ((_, _, applink, name, photo, id) in dataList) {
            resultList.add(KolFollowingViewModel(
                    id,
                    photo,
                    applink,
                    applink,
                    false,
                    name
            ))
        }
        return resultList
    }
}