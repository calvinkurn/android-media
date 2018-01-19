package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class RelationshipRecentNumber {
    @SerializedName("category")
    @Expose
    private Category category;

    public Category getCategory() {
        return category;
    }
}
