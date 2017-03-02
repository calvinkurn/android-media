
package com.tokopedia.core.manage.shop.notes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShopNotesResult {

    @SerializedName("list")
    @Expose
    private java.util.List<ShopNote> list = new ArrayList<ShopNote>();

    /**
     * @return The list
     */
    public java.util.List<ShopNote> getList() {
        return list;
    }

    /**
     * @param list The list
     */
    public void setList(java.util.List<ShopNote> list) {
        this.list = list;
    }

    public boolean hasReturnablePolicy() {
        for (ShopNote shopNote : list) {
            if (shopNote.getNoteStatus().equals(ShopNote.IS_RETURNABLE_POLICY)) {
                return true;
            }
        }
        return false;
    }
}
