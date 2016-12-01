package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.Transient;

/**
 * Created by m.normansyah on 17/12/2015.
 */
@Parcel
public class ReturnPolicyAddModel {

    /**
     * this is for parcelable
     */
    public ReturnPolicyAddModel(){}

    @SerializedName("message_error")
    @Expose
    String[] messageError;

    @SerializedName("message_status")
    @Expose
    String[] messageStatus;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    double serverProcessTime;

    @Parcel
    public static class Data{

        /**
         * this is for parcelable
         */
        public Data(){}

        @SerializedName("is_success")
        @Expose
        int isSuccess;

        @Transient
        public static final int SUCCESS = 1;

        @SerializedName("note_id")
        @Expose
        String noteId;

        public int getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(int isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getNoteId() {
            return noteId;
        }

        public void setNoteId(String noteId) {
            this.noteId = noteId;
        }
    }
}
