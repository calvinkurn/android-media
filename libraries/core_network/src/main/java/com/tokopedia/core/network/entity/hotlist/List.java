
package com.tokopedia.core.network.entity.hotlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class List {

    @SerializedName("hot_product_id")
    @Expose
    private String hotProductId;
    @SerializedName("price_start_from")
    @Expose
    private String priceStartFrom;
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

    public void setHotProductId(String hotProductId) {
        this.hotProductId = hotProductId;
    }

    public String getPriceStartFrom() {
        return priceStartFrom;
    }

    public void setPriceStartFrom(String priceStartFrom) {
        this.priceStartFrom = priceStartFrom;
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

    public void setImgShare(String imgShare) {
        this.imgShare = imgShare;
    }

    public String getImgPromo() {
        return imgPromo;
    }

    public void setImgPromo(String imgPromo) {
        this.imgPromo = imgPromo;
    }

    public ImgSquare getImgSquare() {
        return imgSquare;
    }

    public void setImgSquare(ImgSquare imgSquare) {
        this.imgSquare = imgSquare;
    }

    public ImgPortrait getImgPortrait() {
        return imgPortrait;
    }

    public void setImgPortrait(ImgPortrait imgPortrait) {
        this.imgPortrait = imgPortrait;
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

    public String getAlk() {
        return alk;
    }

    public void setAlk(String alk) {
        this.alk = alk;
    }

}
