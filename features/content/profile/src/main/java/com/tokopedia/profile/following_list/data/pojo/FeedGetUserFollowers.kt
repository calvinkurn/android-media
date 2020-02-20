package com.tokopedia.profile.following_list.data.pojo

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class FeedGetUserFollowers(

	@SerializedName("data")
	@Expose
	val data: List<DataItem> = ArrayList(),

	@SerializedName("meta")
	@Expose
	val meta: Meta = Meta()
)