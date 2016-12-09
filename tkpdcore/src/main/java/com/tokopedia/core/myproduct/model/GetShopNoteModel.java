package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.List;

/**
 * Created by m.normansyah on 17/12/2015.
 */
@Parcel
public class GetShopNoteModel {

    /**
     * this is for parcelable
     */
    public GetShopNoteModel(){}

    @SerializedName("message_error")
    @Expose
    String[] messageRrror;

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

    public String[] getMessageRrror() {
        return messageRrror;
    }

    public void setMessageRrror(String[] messageRrror) {
        this.messageRrror = messageRrror;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public double getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(double serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    @Parcel
    public static class Data{

        /**
         * this is for parcelable
         */
        public Data(){}

        @SerializedName("has_terms")
        @Expose
        int hasTerms;

        @Transient
        public static final int HAS_TERM = 1;
        @Transient
        public static final int HAS_NO_TERM = 0;

        @SerializedName("list")
        @Expose
        List<ShopNoteModel> shopNoteModels;

        @SerializedName("allow_add")
        @Expose
        int allowAdd;

        @SerializedName("is_allow")
        @Expose
        int isAllow;

        public int getHasTerms() {
            return hasTerms;
        }

        public void setHasTerms(int hasTerms) {
            this.hasTerms = hasTerms;
        }

        public List<ShopNoteModel> getShopNoteModels() {
            return shopNoteModels;
        }

        public void setShopNoteModels(List<ShopNoteModel> shopNoteModels) {
            this.shopNoteModels = shopNoteModels;
        }

        public int getAllowAdd() {
            return allowAdd;
        }

        public void setAllowAdd(int allowAdd) {
            this.allowAdd = allowAdd;
        }

        public int getIsAllow() {
            return isAllow;
        }

        public void setIsAllow(int isAllow) {
            this.isAllow = isAllow;
        }
    }

    @Parcel
    public static class ShopNoteModel{
        /**
         * this is for parcelable
         */
        public ShopNoteModel(){}

        @SerializedName("note_id")
        @Expose
        String noteId;

        @SerializedName("note_title")
        @Expose
        String noteTitle;

        @SerializedName("note_status")
        @Expose
        String noteStatus;

        public static final String RETURN_POLICY_TYPE = "2";

        public String getNoteId() {
            return noteId;
        }

        public void setNoteId(String noteId) {
            this.noteId = noteId;
        }

        public String getNoteTitle() {
            return noteTitle;
        }

        public void setNoteTitle(String noteTitle) {
            this.noteTitle = noteTitle;
        }

        public String getNoteStatus() {
            return noteStatus;
        }

        public void setNoteStatus(String noteStatus) {
            this.noteStatus = noteStatus;
        }

        @Override
        public String toString() {
            return "ShopNoteModel{" +
                    "noteId='" + noteId + '\'' +
                    ", noteTitle='" + noteTitle + '\'' +
                    ", noteStatus='" + noteStatus + '\'' +
                    '}';
        }
    }
}
