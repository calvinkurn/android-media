package com.tokopedia.talk.feature.reply.data.model

import com.google.gson.annotations.SerializedName

data class TalkFollowUnfollowTalkResponseWrapper(
        @SerializedName("talkFollowUnfollowTalk")
        val talkFollowUnfollowTalkResponse: TalkFollowUnfollowTalk = TalkFollowUnfollowTalk()
)