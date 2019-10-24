package com.tokopedia.user_identification_common.view.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 21/11/18.
 */

public class AttachmentImageModel {
    @SerializedName("picture_obj")
    @Expose
    private String pictureObj;

    public String getPictureObj() {
        return pictureObj;
    }
}
