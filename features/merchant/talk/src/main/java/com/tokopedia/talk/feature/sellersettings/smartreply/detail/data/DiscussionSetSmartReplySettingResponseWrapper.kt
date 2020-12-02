package com.tokopedia.talk.feature.sellersettings.smartreply.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.DiscussionSmartReplyMutationResult

data class DiscussionSetSmartReplySettingResponseWrapper(
        @SerializedName("discussionSetSmartReplySetting")
        @Expose
        val discussionSetSmartReplySetting: DiscussionSmartReplyMutationResult = DiscussionSmartReplyMutationResult()
)