package com.tokopedia.graphql.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GraphqlError {
    @SerializedName("message")
    private String message;

    @SerializedName("path")
    private List<String> path;

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

    @Override
    public String toString() {
        return "GraphqlError{" +
                "message='" + message + '\'' +
                ", path=" + path +
                '}';
    }
}
