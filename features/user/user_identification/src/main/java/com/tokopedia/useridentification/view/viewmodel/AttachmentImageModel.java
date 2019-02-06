package com.tokopedia.useridentification.view.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 21/11/18.
 */

public class AttachmentImageModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public AttachmentImageModel() {
    }

    public Data getData() {
        return data;
    }


    public class Data{
        @SerializedName("pic_obj")
        @Expose
        private String picObj;
        @SerializedName("pic_src")
        @Expose
        private String picSrc;
        @SerializedName("server_id")
        @Expose
        private String serverId;
        @SerializedName("success")
        @Expose
        private String success;

        public Data() {
        }

        public String getPicObj() {
            return picObj;
        }

        public String getPicSrc() {
            return picSrc;
        }

        public String getServerId() {
            return serverId;
        }

        public String getSuccess() {
            return success;
        }
    }
}
