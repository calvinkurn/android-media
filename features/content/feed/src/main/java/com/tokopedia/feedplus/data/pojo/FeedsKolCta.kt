package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedsKolCta(
    @SerializedName("img_header")
    @Expose val imgHeader: String = "",

    @SerializedName("title")
    @Expose val title: String = "",

    @SerializedName("subtitle")
    @Expose val subtitle: String = "",

    @SerializedName("button_text")
    @Expose val buttonText: String = "",

    @SerializedName("click_url")
    @Expose val clickUrl: String = "",

    @SerializedName("click_applink")
    @Expose val clickApplink: String = ""
)
