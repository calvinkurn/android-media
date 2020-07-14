package com.tokopedia.graphql.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GraphqlError {
    @SerializedName("message")
    private String message;

    @SerializedName("path")
    private List<String> path;

    @SerializedName("extensions")
    private Extensions extensions;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }

    public class Extensions {
        @SerializedName("code")
        private int code;

        @SerializedName("developerMessage")
        private String developerMessage;

        @SerializedName("moreInfo")
        private String moreInfo;

        @SerializedName("timestamp")
        private String timeStamp;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDeveloperMessage() {
            return developerMessage;
        }

        public void setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
        }

        public String getMoreInfo() {
            return moreInfo;
        }

        public void setMoreInfo(String moreInfo) {
            this.moreInfo = moreInfo;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }
    }

    @Override
    public String toString() {
        return "GraphqlError{" +
                "message='" + message + '\'' +
                ", path=" + path +
                '}';
    }
}
