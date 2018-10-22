package com.tokopedia.profile.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profile.data.pojo.profileheader.Profile
import com.tokopedia.profile.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import rx.Subscriber

/**
 * @author by milhamj on 22/10/18.
 */
class GetProfileHeaderSubscriber constructor(
        private val view: ProfileEmptyContract.View,
        private val userId: Int) : Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse?) {
        val data: ProfileHeaderData? = response?.getData(ProfileHeaderData::class.java)
        if (data == null) {
            onError(RuntimeException())
            return
        }
        if (TextUtils.isEmpty(data.bymeProfileHeader.profileHeaderError.message).not()) {
            onError(MessageErrorException(data.bymeProfileHeader.profileHeaderError.message))
            return
        }
        val visitables: ArrayList<Visitable<*>> = ArrayList()
        val profile: Profile = data.bymeProfileHeader.profile
        visitables.add(
                ProfileHeaderViewModel(
                        profile.name,
                        profile.avatar,
                        profile.totalFollower.formatted,
                        profile.totalFollowing.formatted,
                        profile.affiliateName,
                        profile.link,
                        userId,
                        profile.isKol,
                        profile.isAffiliate,
                        profile.isFollowed,
                        userId.toString() == view.getUserSession().userId
                )
        )
        view.renderList(visitables)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.showGetListError(e)
    }
}