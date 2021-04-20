package com.tokopedia.profile.view.subscriber

import android.content.Intent
import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.data.pojo.profileheader.Profile
import com.tokopedia.feedcomponent.data.pojo.profileheader.ProfileHeaderData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profile.R
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import rx.Subscriber


/**
 * @author by milhamj on 22/10/18.
 */
class GetProfileHeaderSubscriber constructor(
        private val view: ProfileEmptyContract.View,
        private val userId: Int) : Subscriber<GraphqlResponse>() {

    companion object {
        private val TOKOPEDIA_APP_LINK = "https://tokopedia.link/nAiRivf8gR"
    }

    override fun onNext(response: GraphqlResponse?) {
        val data: ProfileHeaderData? = response?.getData(ProfileHeaderData::class.java)
        if (data == null) {
            onError(RuntimeException())
            return
        }
        if (data.bymeProfileHeader.profileHeaderError.message.isNotEmpty()) {
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
                        userId.toString() == view.getUserSession().userId,
                        isCreatePostToggleOn = data.bymeProfileHeader.profileConfig.showPostButton
                )
        )
        visitables.add(getEmptyModel())
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

    private fun getEmptyModel(): EmptyResultViewModel {
        val emptyResult = EmptyResultViewModel()
        emptyResult.iconRes = com.tokopedia.affiliatecommon.R.drawable.ic_af_empty
        emptyResult.title = view.context.getString(R.string.profile_recommend_get_commission)
        emptyResult.content = view.context.getString(R.string.profile_tokopedia_app)
        emptyResult.buttonTitle = view.context.getString(R.string.profile_try_now)
        emptyResult.callback = object : BaseEmptyViewHolder.Callback {
            override fun onEmptyContentItemTextClicked() {
            }

            override fun onEmptyButtonClicked() {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(TOKOPEDIA_APP_LINK))
                view.context.startActivity(browserIntent)
            }
        }
        return emptyResult
    }
}