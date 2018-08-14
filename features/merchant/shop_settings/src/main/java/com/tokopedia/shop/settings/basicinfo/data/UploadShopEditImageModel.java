package com.tokopedia.shop.settings.basicinfo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * https://wiki.tokopedia.net/(Action)_Upload_Shop_Image_WSv4
 */

public class UploadShopEditImageModel {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("status")
    @Expose
    private String status;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("image")
        @Expose
        Image image;

        public Image getImage() {
            return image;
        }

    }

    public class Image {

        @SerializedName("pic_code")
        @Expose
        private String picCode;
        @SerializedName("pic_src")
        @Expose
        private String picSrc;
        @SerializedName("success")
        @Expose
        private String success;

        public String getPicCode() {
            return picCode;
        }

        public String getPicSrc() {
            return picSrc;
        }

        public String getSuccess() {
            return success;
        }
    }
}
