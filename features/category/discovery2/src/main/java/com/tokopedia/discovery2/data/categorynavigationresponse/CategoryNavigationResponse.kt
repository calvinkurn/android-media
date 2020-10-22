package com.tokopedia.discovery2.data.categorynavigationresponse

import com.google.gson.annotations.SerializedName

data class CategoryNavigationResponse(

        @SerializedName("child")
        val child: List<ChildItem?>? = null
)