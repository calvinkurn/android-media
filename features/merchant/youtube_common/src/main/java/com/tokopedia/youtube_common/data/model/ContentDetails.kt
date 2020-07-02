package com.tokopedia.youtube_common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentDetails(@SerializedName("duration")
                          @Expose
                          var duration: String? = null,
                          @SerializedName("dimension")
                        @Expose
                        var dimension: String? = null,
                        @SerializedName("definition")
                        @Expose
                        var definition: String? = null,
                        @SerializedName("caption")
                        @Expose
                        var caption: String? = null,
                        @SerializedName("licensedContent")
                        @Expose
                        var isLicensedContent: Boolean = false,
                        @SerializedName("projection")
                        @Expose
                        var projection: String? = null,
                        @SerializedName("contentRating")
                        @Expose
                        var contentRating: ContentRating? = null)