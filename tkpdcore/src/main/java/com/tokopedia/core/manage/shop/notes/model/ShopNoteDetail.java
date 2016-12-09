package com.tokopedia.core.manage.shop.notes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 11/2/16.
 */

public class ShopNoteDetail {
    /**
     * this is for parcelable
     */
    public ShopNoteDetail() {
    }

    @SerializedName("notes_content")
    @Expose
    private String notes_content;

    @SerializedName("notes_update_time")
    @Expose
    private String notes_update_time;

    @SerializedName("notes_active")
    @Expose
    private String notes_active;

    @SerializedName("notes_status")
    @Expose
    private String notes_status;

    @SerializedName("notes_position")
    @Expose
    private String notes_position;

    @SerializedName("notes_id")
    @Expose
    private String notes_id;

    @SerializedName("notes_create_time")
    @Expose
    private String notes_create_time;

    @SerializedName("notes_title")
    @Expose
    private String notes_title;

    private long DbId;

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
