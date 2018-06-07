package com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaDomain {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("product_id")
@Expose
private Integer productId;
@SerializedName("title")
@Expose
private String title;
@SerializedName("is_thumbnail")
@Expose
private Integer isThumbnail;
@SerializedName("type")
@Expose
private String type;
@SerializedName("description")
@Expose
private String description;
@SerializedName("url")
@Expose
private String url;
@SerializedName("client")
@Expose
private String client;
@SerializedName("status")
@Expose
private Integer status;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getProductId() {
return productId;
}

public void setProductId(Integer productId) {
this.productId = productId;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public Integer getIsThumbnail() {
return isThumbnail;
}

public void setIsThumbnail(Integer isThumbnail) {
this.isThumbnail = isThumbnail;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}

public String getClient() {
return client;
}

public void setClient(String client) {
this.client = client;
}

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

}