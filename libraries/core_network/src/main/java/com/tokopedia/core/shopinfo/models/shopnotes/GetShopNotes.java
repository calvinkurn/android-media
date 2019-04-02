
package com.tokopedia.core.shopinfo.models.shopnotes;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class GetShopNotes {

    @SerializedName("message_error")
    @Expose
    private java.util.List<String> messageError = new ArrayList<String>();
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * 
     * @return
     *     The messageError
     */
    public java.util.List<String> getMessageError() {
        return messageError;
    }

    /**
     * 
     * @param messageError
     *     The message_error
     */
    public void setMessageError(java.util.List<String> messageError) {
        this.messageError = messageError;
    }

    /**
     * 
     * @return
     *     The config
     */
    public Object getConfig() {
        return config;
    }

    /**
     * 
     * @param config
     *     The config
     */
    public void setConfig(Object config) {
        this.config = config;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The serverProcessTime
     */
    public String getServerProcessTime() {
        return serverProcessTime;
    }

    /**
     * 
     * @param serverProcessTime
     *     The server_process_time
     */
    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("allow_add")
        @Expose
        private Integer allowAdd;
        @SerializedName("is_allow")
        @Expose
        private Integer isAllow;
        @SerializedName("has_terms")
        @Expose
        private Integer hasTerms;
        @SerializedName("list")
        @Expose
        private java.util.List<List> list = new ArrayList<List>();

        /**
         *
         * @return
         *     The allowAdd
         */
        public Integer getAllowAdd() {
            return allowAdd;
        }

        /**
         *
         * @param allowAdd
         *     The allow_add
         */
        public void setAllowAdd(Integer allowAdd) {
            this.allowAdd = allowAdd;
        }

        /**
         *
         * @return
         *     The isAllow
         */
        public Integer getIsAllow() {
            return isAllow;
        }

        /**
         *
         * @param isAllow
         *     The is_allow
         */
        public void setIsAllow(Integer isAllow) {
            this.isAllow = isAllow;
        }

        /**
         *
         * @return
         *     The hasTerms
         */
        public Integer getHasTerms() {
            return hasTerms;
        }

        /**
         *
         * @param hasTerms
         *     The has_terms
         */
        public void setHasTerms(Integer hasTerms) {
            this.hasTerms = hasTerms;
        }

        /**
         *
         * @return
         *     The list
         */
        public java.util.List<List> getList() {
            return list;
        }

        /**
         *
         * @param list
         *     The list
         */
        public void setList(java.util.List<List> list) {
            this.list = list;
        }

    }

    public static class List {

        @SerializedName("note_status")
        @Expose
        private Integer noteStatus;
        @SerializedName("note_id")
        @Expose
        private String noteId;
        @SerializedName("note_title")
        @Expose
        private String noteTitle;

        /**
         *
         * @return
         *     The noteStatus
         */
        public Integer getNoteStatus() {
            return noteStatus;
        }

        /**
         *
         * @param noteStatus
         *     The note_status
         */
        public void setNoteStatus(Integer noteStatus) {
            this.noteStatus = noteStatus;
        }

        /**
         *
         * @return
         *     The noteId
         */
        public String getNoteId() {
            return noteId;
        }

        /**
         *
         * @param noteId
         *     The note_id
         */
        public void setNoteId(String noteId) {
            this.noteId = noteId;
        }

        /**
         *
         * @return
         *     The noteTitle
         */
        public String getNoteTitle() {
            return noteTitle;
        }

        /**
         *
         * @param noteTitle
         *     The note_title
         */
        public void setNoteTitle(String noteTitle) {
            this.noteTitle = noteTitle;
        }

    }

}
