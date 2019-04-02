
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoRatings {

    @SerializedName("accuracy")
    @Expose
    private ShopInfoAccuracy accuracy;
    @SerializedName("accuracy_width")
    @Expose
    private ShopInfoAccuracyWidth accuracyWidth;
    @SerializedName("quality")
    @Expose
    private ShopInfoQuality quality;
    @SerializedName("quality_width")
    @Expose
    private ShopInfoQualityWidth qualityWidth;

    public ShopInfoAccuracy getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(ShopInfoAccuracy accuracy) {
        this.accuracy = accuracy;
    }

    public ShopInfoAccuracyWidth getAccuracyWidth() {
        return accuracyWidth;
    }

    public void setAccuracyWidth(ShopInfoAccuracyWidth accuracyWidth) {
        this.accuracyWidth = accuracyWidth;
    }

    public ShopInfoQuality getQuality() {
        return quality;
    }

    public void setQuality(ShopInfoQuality quality) {
        this.quality = quality;
    }

    public ShopInfoQualityWidth getQualityWidth() {
        return qualityWidth;
    }

    public void setQualityWidth(ShopInfoQualityWidth qualityWidth) {
        this.qualityWidth = qualityWidth;
    }

}
