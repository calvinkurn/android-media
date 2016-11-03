
package com.tokopedia.tkpd.note.model.modelnote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Detail {

    @SerializedName("notes_position")
    @Expose
    private String notesPosition;
    @SerializedName("notes_status")
    @Expose
    private String notesStatus;
    @SerializedName("notes_create_time")
    @Expose
    private String notesCreateTime;
    @SerializedName("notes_id")
    @Expose
    private String notesId;
    @SerializedName("notes_title")
    @Expose
    private String notesTitle;
    @SerializedName("notes_active")
    @Expose
    private Integer notesActive;
    @SerializedName("notes_update_time")
    @Expose
    private String notesUpdateTime;
    @SerializedName("notes_content")
    @Expose
    private String notesContent;

    /**
     * 
     * @return
     *     The notesPosition
     */
    public String getNotesPosition() {
        return notesPosition;
    }

    /**
     * 
     * @param notesPosition
     *     The notes_position
     */
    public void setNotesPosition(String notesPosition) {
        this.notesPosition = notesPosition;
    }

    /**
     * 
     * @return
     *     The notesStatus
     */
    public String getNotesStatus() {
        return notesStatus;
    }

    /**
     * 
     * @param notesStatus
     *     The notes_status
     */
    public void setNotesStatus(String notesStatus) {
        this.notesStatus = notesStatus;
    }

    /**
     * 
     * @return
     *     The notesCreateTime
     */
    public String getNotesCreateTime() {
        return notesCreateTime;
    }

    /**
     * 
     * @param notesCreateTime
     *     The notes_create_time
     */
    public void setNotesCreateTime(String notesCreateTime) {
        this.notesCreateTime = notesCreateTime;
    }

    /**
     * 
     * @return
     *     The notesId
     */
    public String getNotesId() {
        return notesId;
    }

    /**
     * 
     * @param notesId
     *     The notes_id
     */
    public void setNotesId(String notesId) {
        this.notesId = notesId;
    }

    /**
     * 
     * @return
     *     The notesTitle
     */
    public String getNotesTitle() {
        return notesTitle;
    }

    /**
     * 
     * @param notesTitle
     *     The notes_title
     */
    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }

    /**
     * 
     * @return
     *     The notesActive
     */
    public Integer getNotesActive() {
        return notesActive;
    }

    /**
     * 
     * @param notesActive
     *     The notes_active
     */
    public void setNotesActive(Integer notesActive) {
        this.notesActive = notesActive;
    }

    /**
     * 
     * @return
     *     The notesUpdateTime
     */
    public String getNotesUpdateTime() {
        return notesUpdateTime;
    }

    /**
     * 
     * @param notesUpdateTime
     *     The notes_update_time
     */
    public void setNotesUpdateTime(String notesUpdateTime) {
        this.notesUpdateTime = notesUpdateTime;
    }

    /**
     * 
     * @return
     *     The notesContent
     */
    public String getNotesContent() {
        return notesContent;
    }

    /**
     * 
     * @param notesContent
     *     The notes_content
     */
    public void setNotesContent(String notesContent) {
        this.notesContent = notesContent;
    }

}
