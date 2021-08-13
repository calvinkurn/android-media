package com.tokopedia.user_identification_common.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class KycUserProjectInfoPojo {

    @Expose
    @SerializedName("kycProjectInfo")
    private KycProjectInfo kycProjectInfo;

    public KycProjectInfo getKycProjectInfo() {
        return kycProjectInfo;
    }

    public void setKycProjectInfo(KycProjectInfo kycProjectInfo) {
        this.kycProjectInfo = kycProjectInfo;
    }

    public class KycProjectInfo {

        @Expose
        @SerializedName("Status")
        private Integer status;

        @Expose
        @SerializedName("StatusName")
        private String statusName;

        @Expose
        @SerializedName("Message")
        private String message;

        @Expose
        @SerializedName("IsAllowToRegister")
        private boolean isAllowToRegister;

        @Expose
        @SerializedName("Reason")
        private ArrayList<String> reasonList;

        @Expose
        @SerializedName("TypeList")
        private ArrayList<TypeList> typeLists;

        @Expose
        @SerializedName("IsSelfie")
        private Boolean isSelfie;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isAllowToRegister() {
            return isAllowToRegister;
        }

        public void setAllowToRegister(boolean allowToRegister) {
            isAllowToRegister = allowToRegister;
        }

        public List<String> getReasonList() {
            return reasonList;
        }

        public void setReasonList(ArrayList<String> reasonList) {
            this.reasonList = reasonList;
        }

        public List<TypeList> getTypeLists() {
            return typeLists;
        }

        public void setTypeLists(ArrayList<TypeList> typeLists) {
            this.typeLists = typeLists;
        }

        public boolean isSelfie() { return isSelfie; }
    }

    public class TypeList {

        @Expose
        @SerializedName("TypeID")
        private Integer ID;

        @Expose
        @SerializedName("Status")
        private Integer status;

        @Expose
        @SerializedName("StatusName")
        private String statusName;

        @Expose
        @SerializedName("IsAllowToUpload")
        private boolean isAllowToUpload;

        public Integer getID() {
            return ID;
        }

        public void setID(Integer ID) {
            this.ID = ID;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public boolean isAllowToUpload() {
            return isAllowToUpload;
        }

        public void setAllowToUpload(boolean allowToUpload) {
            isAllowToUpload = allowToUpload;
        }
    }
}
