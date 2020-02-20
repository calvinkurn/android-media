package com.tokopedia.profile.following_list.data.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profile.following_list.data.pojo.DataItem
import com.tokopedia.profile.following_list.data.pojo.FollowerListData
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingResultViewModel
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingViewModel
import rx.functions.Func1
import java.util.ArrayList
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-22
 */
class FollowerMapper @Inject constructor() : Func1<GraphqlResponse, UserFollowingResultViewModel> {

    override fun call(t: GraphqlResponse?): UserFollowingResultViewModel {
        val data = t?.getData<FollowerListData?>(FollowerListData::class.java)
        data?.let {
            return mapFollowersData(data)
        }
        return UserFollowingResultViewModel()
    }
    private fun mapFollowersData(data: FollowerListData): UserFollowingResultViewModel {
        return UserFollowingResultViewModel(
                !data.feedGetUserFollowers.meta.nextCursor.isEmpty(),
                mapFollowData(data.feedGetUserFollowers.data),
                data.feedGetUserFollowers.meta.nextCursor,
                "",
                ""
        )
    }

    private fun mapFollowData(dataList: List<DataItem>): List<UserFollowingViewModel> {
        val resultList = ArrayList<UserFollowingViewModel>()
        for ((_, _, applink, name, photo, id) in dataList) {
            resultList.add(UserFollowingViewModel(
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