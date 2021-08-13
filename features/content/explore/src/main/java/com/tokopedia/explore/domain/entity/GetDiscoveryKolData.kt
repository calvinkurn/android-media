package com.tokopedia.explore.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetDiscoveryKolData(@SerializedName("error")
                               @Expose
                               var error: String = "",

                               @SerializedName("categories")
                               @Expose
                               var categories: List<Category> = emptyList(),

                               @SerializedName("postKol")
                               @Expose
                               var postKol: List<PostKol> = emptyList(),

                               @SerializedName("lastCursor")
                               @Expose
                               var lastCursor: String = ""
)