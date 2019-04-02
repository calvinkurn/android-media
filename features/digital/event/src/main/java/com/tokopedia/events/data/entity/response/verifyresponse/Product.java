package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class Product{

	@SerializedName("seo_url")
	private String seoUrl;

	@SerializedName("tnc")
	private String tnc;

	@SerializedName("mrp")
	private int mrp;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("title")
	private String title;

	@SerializedName("url")
	private String url;

	@SerializedName("image_app")
	private String imageApp;

	@SerializedName("thumbnail_web")
	private String thumbnailWeb;

	@SerializedName("offer_text")
	private String offerText;

	@SerializedName("action_text")
	private String actionText;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("autocode")
	private String autocode;

	@SerializedName("censor")
	private String censor;

	@SerializedName("genre")
	private String genre;

	@SerializedName("sales_price")
	private int salesPrice;

	@SerializedName("provider_id")
	private int providerId;

	@SerializedName("id")
	private int id;

	@SerializedName("image_web")
	private String imageWeb;

	@SerializedName("thumbnail_app")
	private String thumbnailApp;

	@SerializedName("promotion_text")
	private String promotionText;

	public void setSeoUrl(String seoUrl){
		this.seoUrl = seoUrl;
	}

	public String getSeoUrl(){
		return seoUrl;
	}

	public void setTnc(String tnc){
		this.tnc = tnc;
	}

	public String getTnc(){
		return tnc;
	}

	public void setMrp(int mrp){
		this.mrp = mrp;
	}

	public int getMrp(){
		return mrp;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setImageApp(String imageApp){
		this.imageApp = imageApp;
	}

	public String getImageApp(){
		return imageApp;
	}

	public void setThumbnailWeb(String thumbnailWeb){
		this.thumbnailWeb = thumbnailWeb;
	}

	public String getThumbnailWeb(){
		return thumbnailWeb;
	}

	public void setOfferText(String offerText){
		this.offerText = offerText;
	}

	public String getOfferText(){
		return offerText;
	}

	public void setActionText(String actionText){
		this.actionText = actionText;
	}

	public String getActionText(){
		return actionText;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setAutocode(String autocode){
		this.autocode = autocode;
	}

	public String getAutocode(){
		return autocode;
	}

	public void setCensor(String censor){
		this.censor = censor;
	}

	public String getCensor(){
		return censor;
	}

	public void setGenre(String genre){
		this.genre = genre;
	}

	public String getGenre(){
		return genre;
	}

	public void setSalesPrice(int salesPrice){
		this.salesPrice = salesPrice;
	}

	public int getSalesPrice(){
		return salesPrice;
	}

	public void setProviderId(int providerId){
		this.providerId = providerId;
	}

	public int getProviderId(){
		return providerId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setImageWeb(String imageWeb){
		this.imageWeb = imageWeb;
	}

	public String getImageWeb(){
		return imageWeb;
	}

	public void setThumbnailApp(String thumbnailApp){
		this.thumbnailApp = thumbnailApp;
	}

	public String getThumbnailApp(){
		return thumbnailApp;
	}

	public void setPromotionText(String promotionText){
		this.promotionText = promotionText;
	}

	public String getPromotionText(){
		return promotionText;
	}

	@Override
 	public String toString(){
		return 
			"Product{" + 
			"seo_url = '" + seoUrl + '\'' + 
			",tnc = '" + tnc + '\'' + 
			",mrp = '" + mrp + '\'' + 
			",display_name = '" + displayName + '\'' + 
			",title = '" + title + '\'' + 
			",url = '" + url + '\'' + 
			",image_app = '" + imageApp + '\'' + 
			",thumbnail_web = '" + thumbnailWeb + '\'' + 
			",offer_text = '" + offerText + '\'' + 
			",action_text = '" + actionText + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",autocode = '" + autocode + '\'' + 
			",censor = '" + censor + '\'' + 
			",genre = '" + genre + '\'' + 
			",sales_price = '" + salesPrice + '\'' + 
			",provider_id = '" + providerId + '\'' + 
			",id = '" + id + '\'' + 
			",image_web = '" + imageWeb + '\'' + 
			",thumbnail_app = '" + thumbnailApp + '\'' + 
			",promotion_text = '" + promotionText + '\'' + 
			"}";
		}
}