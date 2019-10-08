package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeActionButton {

@SerializedName("backgroundColor")
@Expose
private String backgroundColor;
@SerializedName("text")
@Expose
private String text;
@SerializedName("appLink")
@Expose
private String appLink;
@SerializedName("url")
@Expose
private String url;
@SerializedName("type")
@Expose
private String type;

public String getBackgroundColor() {
return backgroundColor;
}

public void setBackgroundColor(String backgroundColor) {
this.backgroundColor = backgroundColor;
}

public String getText() {
return text;
}

public void setText(String text) {
this.text = text;
}

public String getAppLink() {
return appLink;
}

public void setAppLink(String appLink) {
this.appLink = appLink;
}

public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

}