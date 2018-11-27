package com.tokopedia.payment.setting.add.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("cc_iframe")
    @Expose
    var ccIframe: CcIframe? = null
    @SerializedName("cc_iframe_encode")
    @Expose
    var ccIframeEncode: String? = null
    @SerializedName("api_info")
    @Expose
    var apiInfo: ApiInfo? = null

}
