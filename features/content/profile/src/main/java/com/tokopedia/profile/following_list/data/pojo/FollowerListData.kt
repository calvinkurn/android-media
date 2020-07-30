package com.tokopedia.profile.following_list.data.pojo

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class FollowerListData(

	@SerializedName("feedGetUserFollowers")
	@Expose
	val feedGetUserFollowers: FeedGetUserFollowers = FeedGetUserFollowers()
)