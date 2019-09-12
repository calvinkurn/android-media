package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeSmallButton {

@SerializedName("imageURL")
@Expose
private String imageURL;
@SerializedName("appLink")
@Expose
private String appLink;
@SerializedName("url")
@Expose
private String url;

public String getImageURL() {
return imageURL;
}

public void setImageURL(String imageURL) {
this.imageURL = imageURL;
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

}