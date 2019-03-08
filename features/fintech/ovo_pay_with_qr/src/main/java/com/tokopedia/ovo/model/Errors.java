package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Errors {

@SerializedName("title")
@Expose
private String title;
@SerializedName("message")
@Expose
private String message;

    public Errors(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

}