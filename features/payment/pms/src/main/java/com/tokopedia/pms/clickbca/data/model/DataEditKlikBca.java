package com.tokopedia.pms.clickbca.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class DataEditKlikBca {

    @SerializedName("editKlikbca")
    @Expose
    private EditKlikbca editKlikbca;

    public EditKlikbca getEditKlikbca() {
        return editKlikbca;
    }

    public void setEditKlikbca(EditKlikbca editKlikbca) {
        this.editKlikbca = editKlikbca;
    }
}
