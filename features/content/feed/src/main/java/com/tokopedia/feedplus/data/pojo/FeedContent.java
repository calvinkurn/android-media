package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedContent {
  @SerializedName("products")
  @Expose
  private List<ProductFeedType> products;

  @SerializedName("type")
  @Expose
  private String type;

  @SerializedName("total_product")
  @Expose
  private Integer total_product;

  @SerializedName("display")
  @Expose
  private String display;

  @SerializedName("status_activity")
  @Expose
  private String status_activity;

  @SerializedName("new_status_activity")
  @Expose
  private StatusActivity new_status_activity;

  @SerializedName("total_store")
  @Expose
  private Integer total_store;

  @SerializedName("redirect_url_app")
  @Expose
  private String redirect_url_app;

  @SerializedName("polling")
  @Expose
  private FeedPolling polling;

  @SerializedName("banner")
  @Expose
  private List<FeedBanner> banner;

  @SerializedName("kolpost")
  @Expose
  private FeedKolType kolpost;

  @SerializedName("followedkolpost")
  @Expose
  private FeedKolType followedkolpost;

  @SerializedName("kolrecommendation")
  @Expose
  private KolRecommendedDataType kolrecommendation;

  @SerializedName("favorite_cta")
  @Expose
  private FeedsFavoriteCta favorite_cta;

  @SerializedName("kol_cta")
  @Expose
  private FeedsKolCta kol_cta;

  @SerializedName("topads")
  @Expose
  private List<TopAd> topads;

  public void setProducts(List<ProductFeedType> products) {
    this.products = products;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setTotal_product(Integer total_product) {
    this.total_product = total_product;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public void setStatus_activity(String status_activity) {
    this.status_activity = status_activity;
  }

  public void setNew_status_activity(StatusActivity new_status_activity) {
    this.new_status_activity = new_status_activity;
  }

  public void setTotal_store(Integer total_store) {
    this.total_store = total_store;
  }

  public void setRedirect_url_app(String redirect_url_app) {
    this.redirect_url_app = redirect_url_app;
  }

  public void setPolling(FeedPolling polling) {
    this.polling = polling;
  }

  public void setBanner(List<FeedBanner> banner) {
    this.banner = banner;
  }

  public void setKolpost(FeedKolType kolpost) {
    this.kolpost = kolpost;
  }

  public void setFollowedkolpost(FeedKolType followedkolpost) {
    this.followedkolpost = followedkolpost;
  }

  public void setKolrecommendation(KolRecommendedDataType kolrecommendation) {
    this.kolrecommendation = kolrecommendation;
  }

  public void setFavorite_cta(FeedsFavoriteCta favorite_cta) {
    this.favorite_cta = favorite_cta;
  }

  public void setKol_cta(FeedsKolCta kol_cta) {
    this.kol_cta = kol_cta;
  }

  public void setTopads(List<TopAd> topads) {
    this.topads = topads;
  }

  public List<ProductFeedType> getProducts() {
    return this.products;
  }

  public String getType() {
    return this.type;
  }

  public Integer getTotal_product() {
    return this.total_product;
  }

  public String getDisplay() {
    return this.display;
  }

  public String getStatus_activity() {
    return this.status_activity;
  }

  public StatusActivity getNew_status_activity() {
    return this.new_status_activity;
  }

  public Integer getTotal_store() {
    return this.total_store;
  }

  public String getRedirect_url_app() {
    return this.redirect_url_app;
  }

  public FeedPolling getPolling() {
    return this.polling;
  }

  public List<FeedBanner> getBanner() {
    return this.banner;
  }

  public FeedKolType getKolpost() {
    return this.kolpost;
  }

  public FeedKolType getFollowedkolpost() {
    return this.followedkolpost;
  }

  public KolRecommendedDataType getKolrecommendation() {
    return this.kolrecommendation;
  }

  public FeedsFavoriteCta getFavorite_cta() {
    return this.favorite_cta;
  }

  public FeedsKolCta getKol_cta() {
    return this.kol_cta;
  }

  public List<TopAd> getTopads() {
    return this.topads;
  }
}
