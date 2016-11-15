
package com.tokopedia.core.manage.shop.notes.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopNote implements Parcelable {

    public static final String IS_RETURNABLE_POLICY = "2";

    @SerializedName("note_status")
    @Expose
    private String noteStatus;
    @SerializedName("note_id")
    @Expose
    private String noteId;
    @SerializedName("note_title")
    @Expose
    private String noteTitle;
    private int position;

    protected ShopNote(Parcel in) {
        noteStatus = in.readString();
        noteId = in.readString();
        noteTitle = in.readString();
        position = in.readInt();
    }

    public ShopNote() {
    }

    public static final Creator<ShopNote> CREATOR = new Creator<ShopNote>() {
        @Override
        public ShopNote createFromParcel(Parcel in) {
            return new ShopNote(in);
        }

        @Override
        public ShopNote[] newArray(int size) {
            return new ShopNote[size];
        }
    };

    /**
     * @return The noteStatus
     */
    public String getNoteStatus() {
        return noteStatus;
    }

    /**
     * @param noteStatus The note_status
     */
    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }

    /**
     * @return The noteId
     */
    public String getNoteId() {
        return noteId;
    }

    /**
     * @param noteId The note_id
     */
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    /**
     * @return The noteTitle
     */
    public String getNoteTitle() {
        return Html.fromHtml(noteTitle).toString();
    }

    /**
     * @param noteTitle The note_title
     */
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteStatus);
        dest.writeString(noteId);
        dest.writeString(noteTitle);
        dest.writeInt(position);
    }
}
