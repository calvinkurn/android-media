
package com.tokopedia.core.network.entity.hotlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class List {

    @SerializedName("hot_product_id")
    @Expose
    private String hotProductId;
    @SerializedName("img")
    @Expose
    private Img img;
    @SerializedName("img_share")
    @Expose
    private String imgShare;
    @SerializedName("img_promo")
    @Expose
    private String imgPromo;
    @SerializedName("img_square")
    @Expose
    private ImgSquare imgSquare;
    @SerializedName("img_portrait")
    @Expose
    private ImgPortrait imgPortrait;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("alk")
    @Expose
    private String alk;

    public String getHotProductId() {
        return hotProductId;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }

    public String getImgShare() {
        return imgShare;
    }

    public ImgSquare getImgSquare() {
        return imgSquare;
    }

    public ImgPortrait getImgPortrait() {
        return imgPortrait;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
