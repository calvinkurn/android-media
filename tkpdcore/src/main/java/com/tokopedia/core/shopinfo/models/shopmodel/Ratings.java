
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ratings {

    @SerializedName("quality_width")
    @Expose
    public QualityWidth qualityWidth;
    @SerializedName("accuracy_width")
    @Expose
    public AccuracyWidth accuracyWidth;
    @SerializedName("quality")
    @Expose
    public Quality quality;
    @SerializedName("accuracy")
    @Expose
    public Accuracy accuracy;

}
