package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 22/05/18.
 */
public class AdditionalFeature {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("text")
    @Expose
    private int text;
    @SerializedName("button_text")
    @Expose
    private int buttonText;

    public int getId() {
        return id;
    }

    public int getText() {
        return text;
    }

    public int getButtonText() {
        return buttonText;
    }
}
