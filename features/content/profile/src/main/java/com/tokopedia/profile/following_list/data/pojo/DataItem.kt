package com.tokopedia.profile.following_list.data.pojo

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class DataItem(

	@SerializedName("badges")
	@Expose
	val badges: List<String> = ArrayList(),

	@SerializedName("isFollow")
	@Expose
	val isFollow: Boolean = false,

	@SerializedName("applink")
	@Expose
	val applink: String = "",

	@SerializedName("name")
	@Expose
	val name: String = "",

	@SerializedName("photo")
	@Expose
	val photo: String = "",

	@SerializedName("id")
	@Expose
	val id: Int = 0
)