package com.tokopedia.browse.categoryNavigation.data.model.hotlist

import com.google.gson.annotations.SerializedName

data class CategoryHotlist(

	@field:SerializedName("list")
	val list: ArrayList<ListItem>? = null
)