package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by admin on 23/12/2015.
 */
@Parcel
public class NoteDetailModel {

    /**
     * this is for parcelable
     */
    public NoteDetailModel(){}

    @SerializedName("message_error")
    @Expose
    String[] message_error;

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
    String server_process_time;

    @Parcel
    public static class Data{
        /**
         * this is for parcelable
         */
        public Data(){}

        @SerializedName("detail")
        @Expose
        Detail detail;

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }
    }
    @Parcel
    public static class Detail{
        /**
         * this is for parcelable
         */
        public Detail(){}

        @SerializedName("notes_content")
        @Expose
        String notes_content;

        @SerializedName("notes_update_time")
        @Expose
        String notes_update_time;

        @SerializedName("notes_active")
        @Expose
        String notes_active;

        @SerializedName("notes_status")
        @Expose
        String notes_status;

        @SerializedName("notes_position")
        @Expose
        String notes_position;

        @SerializedName("notes_id")
        @Expose
        String notes_id;

        @SerializedName("notes_create_time")
        @Expose
        String notes_create_time;

        @SerializedName("notes_title")
        @Expose
        String notes_title;

        long DbId;

        public long getDbId() {
            return DbId;
        }

        public void setDbId(long dbId) {
            DbId = dbId;
        }

        public String getNotes_content() {
            return notes_content;
        }

        public void setNotes_content(String notes_content) {
            this.notes_content = notes_content;
        }

        public String getNotes_update_time() {
            return notes_update_time;
        }

        public void setNotes_update_time(String notes_update_time) {
            this.notes_update_time = notes_update_time;
        }

        public String getNotes_active() {
            return notes_active;
        }

        public void setNotes_active(String notes_active) {
            this.notes_active = notes_active;
        }

        public String getNotes_status() {
            return notes_status;
        }

        public void setNotes_status(String notes_status) {
            this.notes_status = notes_status;
        }

        public String getNotes_position() {
            return notes_position;
        }

        public void setNotes_position(String notes_position) {
            this.notes_position = notes_position;
        }

        public String getNotes_id() {
            return notes_id;
        }

        public void setNotes_id(String notes_id) {
            this.notes_id = notes_id;
        }

        public String getNotes_create_time() {
            return notes_create_time;
        }

        public void setNotes_create_time(String notes_create_time) {
            this.notes_create_time = notes_create_time;
        }

        public String getNotes_title() {
            return notes_title;
        }

        public void setNotes_title(String notes_title) {
            this.notes_title = notes_title;
        }
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String[] getMessage_error() {
        return message_error;
    }

    public void setMessage_error(String[] message_error) {
        this.message_error = message_error;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
