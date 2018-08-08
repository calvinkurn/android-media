package com.tokopedia.product.manage.item.video.domain.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.item.video.domain.model.youtube.ContentRating

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