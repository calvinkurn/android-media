package com.tokopedia.core.manage.shop.notes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.myproduct.model.NoteDetailModel;

/**
 * Created by nisie on 11/2/16.
 */

public class ShopNoteDetailResult {
    /**
     * this is for parcelable
     */
    public ShopNoteDetailResult() {
    }

    @SerializedName("detail")
    @Expose
    ShopNoteDetail detail;

    public ShopNoteDetail getDetail() {
        return detail;
    }

    public void setDetail(ShopNoteDetail detail) {
        this.detail = detail;
    }
}