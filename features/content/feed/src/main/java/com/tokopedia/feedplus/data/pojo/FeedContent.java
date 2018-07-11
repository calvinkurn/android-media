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
  private Integer totalProduct;

  @SerializedName("display")
  @Expose
  private String display;

  @SerializedName("status_activity")
  @Expose
  private String statusActivity;

  @SerializedName("new_status_activity")
  @Expose
  private StatusActivity newStatusActivity;

  @SerializedName("total_store")
  @Expose
  private Integer totalStore;

  @SerializedName("redirect_url_app")
  @Expose
  private String redirectUrlApp;

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
  private FeedsFavoriteCta favoriteCta;

  @SerializedName("kol_cta")
  @Expose
  private FeedsKolCta kolCta;

  @SerializedName("topads")
  @Expose
  private List<TopAd> topads;

  public void setProducts(List<ProductFeedType> products) {
    this.products = products;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setTotalProduct(Integer totalProduct) {
    this.totalProduct = totalProduct;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public void setStatusActivity(String statusActivity) {
    this.statusActivity = statusActivity;
  }

  public void setNewStatusActivity(StatusActivity newStatusActivity) {
    this.newStatusActivity = newStatusActivity;
  }

  public void setTotalStore(Integer totalStore) {
    this.totalStore = totalStore;
  }

  public void setRedirectUrlApp(String redirectUrlApp) {
    this.redirectUrlApp = redirectUrlApp;
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

  public void setFavoriteCta(FeedsFavoriteCta favoriteCta) {
    this.favoriteCta = favoriteCta;
  }

  public void setKolCta(FeedsKolCta kolCta) {
    this.kolCta = kolCta;
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

  public Integer getTotalProduct() {
    return this.totalProduct;
  }

  public String getDisplay() {
    return this.display;
  }

  public String getStatusActivity() {
    return this.statusActivity;
  }

  public StatusActivity getNewStatusActivity() {
    return this.newStatusActivity;
  }

  public Integer getTotalStore() {
    return this.totalStore;
  }

  public String getRedirectUrlApp() {
    return this.redirectUrlApp;
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

  public FeedsFavoriteCta getFavoriteCta() {
    return this.favoriteCta;
  }

  public FeedsKolCta getKolCta() {
    return this.kolCta;
  }

  public List<TopAd> getTopads() {
    return this.topads;
  }
}
