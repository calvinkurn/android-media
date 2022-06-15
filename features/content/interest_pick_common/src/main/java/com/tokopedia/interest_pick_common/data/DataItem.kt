package com.tokopedia.interest_pick_common.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class DataItem(

	@SerializedName("image")
	@Expose
	val image: String = "",

	@SerializedName("name")
	@Expose
	val name: String = "",

	@SerializedName("isSelected")
	@Expose
	val isSelected: Boolean = false,

	@SuppressLint("Invalid Data Type")
	@SerializedName("id")
	@Expose
	val id: Int = 0
)