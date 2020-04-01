package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class CartItemsItem{

	@SerializedName("product")
	private Product product;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("total_price")
	private int totalPrice;

	@SerializedName("configuration")
	private Configuration configuration;

	@SerializedName("item_id")
	private String itemId;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("mrp")
	private int mrp;

	@SerializedName("title")
	private String title;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("fulfillment_service_id")
	private int fulfillmentServiceId;

	@SerializedName("discounted_price")
	private int discountedPrice;

	@SerializedName("display_sequnce")
	private int displaySequnce;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("price")
	private int price;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("meta_data")
	private MetaData metaData;

	public void setProduct(Product product){
		this.product = product;
	}

	public Product getProduct(){
		return product;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setTotalPrice(int totalPrice){
		this.totalPrice = totalPrice;
	}

	public int getTotalPrice(){
		return totalPrice;
	}

	public void setConfiguration(Configuration configuration){
		this.configuration = configuration;
	}

	public Configuration getConfiguration(){
		return configuration;
	}

	public void setItemId(String itemId){
		this.itemId = itemId;
	}

	public String getItemId(){
		return itemId;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setMrp(int mrp){
		this.mrp = mrp;
	}

	public int getMrp(){
		return mrp;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setFulfillmentServiceId(int fulfillmentServiceId){
		this.fulfillmentServiceId = fulfillmentServiceId;
	}

	public int getFulfillmentServiceId(){
		return fulfillmentServiceId;
	}

	public void setDiscountedPrice(int discountedPrice){
		this.discountedPrice = discountedPrice;
	}

	public int getDiscountedPrice(){
		return discountedPrice;
	}

	public void setDisplaySequnce(int displaySequnce){
		this.displaySequnce = displaySequnce;
	}

	public int getDisplaySequnce(){
		return displaySequnce;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setMetaData(MetaData metaData){
		this.metaData = metaData;
	}

	public MetaData getMetaData(){
		return metaData;
	}

	@Override
 	public String toString(){
		return 
			"CartItemsItem{" + 
			"product = '" + product + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",total_price = '" + totalPrice + '\'' + 
			",configuration = '" + configuration + '\'' + 
			",item_id = '" + itemId + '\'' + 
			",image_url = '" + imageUrl + '\'' + 
			",mrp = '" + mrp + '\'' + 
			",title = '" + title + '\'' + 
			",product_name = '" + productName + '\'' + 
			",fulfillment_service_id = '" + fulfillmentServiceId + '\'' + 
			",discounted_price = '" + discountedPrice + '\'' + 
			",display_sequnce = '" + displaySequnce + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",price = '" + price + '\'' + 
			",product_id = '" + productId + '\'' + 
			",meta_data = '" + metaData + '\'' + 
			"}";
		}
}