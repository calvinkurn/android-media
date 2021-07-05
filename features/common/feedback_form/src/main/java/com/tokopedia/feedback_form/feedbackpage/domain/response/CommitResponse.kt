package com.tokopedia.feedback_form.feedbackpage.domain.response

import com.google.gson.annotations.SerializedName

data class CommitResponse(
        @SerializedName("data")
        var data: DataCommit = DataCommit(),
        @SerializedName("error")
        var error: String = ""
)

data class DataCommit(
        @SerializedName("jiraIssueKey")
        var jiraIssueKey: String = "",
        @SerializedName("jiraIssueURL")
        var jiraIssueURL: String = "",
        @SerializedName("slackChannelURL")
        var slackChannel: String = ""
)