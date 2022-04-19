package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoRatings {
    @SerializedName("accuracy")
    @Expose
    var accuracy: ShopInfoAccuracy? = null

    @SerializedName("accuracy_width")
    @Expose
    var accuracyWidth: ShopInfoAccuracyWidth? = null

    @SerializedName("quality")
    @Expose
    var quality: ShopInfoQuality? = null

    @SerializedName("quality_width")
    @Expose
    var qualityWidth: ShopInfoQualityWidth? = null
}