package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class TokopointPaging {

    @SerializedName("hasNext")
    private boolean hasNext;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}