package com.tokopedia.core.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendry on 7/6/2017.
 */

@Deprecated
public class TextErrorObject {
    @SerializedName("text")
    @Expose
    private List<String> textList;
    @SerializedName("type")
    @Expose
    private int type;

    public List<String> getTextList() {
        return textList;
    }

    public void setText(List<String> textList) {
        this.textList = textList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
