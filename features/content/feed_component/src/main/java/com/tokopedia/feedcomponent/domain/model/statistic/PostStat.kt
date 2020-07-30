package com.tokopedia.feedcomponent.domain.model.statistic

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-22
 */
data class PostStat(

        @SerializedName("activityID")
        val activityID: String = "",

        @SerializedName("like")
        val like: LikeStat = LikeStat(),

        @SerializedName("click")
        val click: ClickStat = ClickStat(),

        @SerializedName("comment")
        val comment: CommentStat = CommentStat(),

        @SerializedName("view")
        val view: ViewStat = ViewStat()
)