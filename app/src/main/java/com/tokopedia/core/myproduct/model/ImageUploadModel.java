package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by admin on 18/12/2015.
 */
@Parcel
public class ImageUploadModel {

    /**
     * this is for parcelable
     */
    public ImageUploadModel(){}

    @SerializedName("")
    @Expose
    String status;

    @SerializedName("")
    @Expose
    ImageUpload imageUpload;

    @SerializedName("")
    @Expose
    String server_process_time;

    @SerializedName("result")
    @Expose
    String result;

    @SerializedName("message_error")
    @Expose
    ArrayList<String> messageError;


    @Parcel
    public static class ImageUpload{

        /**
         * this is for parcelable
         */
        public ImageUpload(){}

        @SerializedName("file_path")
        @Expose
        String file_path;

        @SerializedName("file_th")
        @Expose
        String file_th;
    }
}
