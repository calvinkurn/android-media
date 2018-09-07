
package com.tokopedia.shop.note.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ShopNoteList {

    @SerializedName("notes")
    @Expose
    private List<ShopNote> notes = new ArrayList<>();

    public List<ShopNote> getNotes() {
        return notes;
    }

    public void setNotes(List<ShopNote> notes) {
        this.notes = notes;
    }

}
