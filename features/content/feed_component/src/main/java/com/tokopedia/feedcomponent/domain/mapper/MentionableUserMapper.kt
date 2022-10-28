package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.feedcomponent.data.pojo.mention.GetMentionableUserData
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by jegul on 2019-08-05.
 */
class MentionableUserMapper @Inject constructor() : Func1<GraphqlResponse, List<MentionableUserViewModel>> {

    override fun call(t: GraphqlResponse?): List<MentionableUserViewModel> {
        val mentionableUserData = t?.getData<GetMentionableUserData?>(GetMentionableUserData::class.java)
        return mentionableUserData?.let {
            it.aceSearchProfile.profiles.map { profile ->
                MentionableUserViewModel(
                        id = profile.id,
                        userName = if (profile.username.isEmpty()) null else "@${profile.username}",
                        fullName = profile.name,
                        avatarUrl = if (profile.avatar.isEmpty()) null else profile.avatar
                )
            }
        } ?: emptyList()
    }
}