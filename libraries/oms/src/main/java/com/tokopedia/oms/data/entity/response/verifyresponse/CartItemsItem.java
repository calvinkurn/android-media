package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class CartItemsItem{

	@SerializedName("product")
	private Product product;

	@SerializedName("quantity")
	private Integer quantity;

	@SerializedName("total_price")
	private Integer totalPrice;

	@SerializedName("configuration")
	private Configuration configuration;

	@SerializedName("item_id")
	private String itemId;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("mrp")
	private Integer mrp;

	@SerializedName("title")
	private String title;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("fulfillment_service_id")
	private Integer fulfillmentServiceId;

	@SerializedName("discounted_price")
	private Integer discountedPrice;

	@SerializedName("display_sequnce")
	private Integer displaySequnce;

	@SerializedName("category_id")
	private Integer categoryId;

	@SerializedName("price")
	private Integer price;

	@SerializedName("product_id")
	private Integer productId;

	@SerializedName("meta_data")
	private MetaData metaData;

	public void setProduct(Product product){
		this.product = product;
	}

	public Product getProduct(){
		return product;
	}

	public void setQuantity(Integer quantity){
		this.quantity = quantity;
	}

	public Integer getQuantity(){
		return quantity;
	}

	public void setTotalPrice(Integer totalPrice){
		this.totalPrice = totalPrice;
	}

	public Integer getTotalPrice(){
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

	public void setMrp(Integer mrp){
		this.mrp = mrp;
	}

	public Integer getMrp(){
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

	public void setFulfillmentServiceId(Integer fulfillmentServiceId){
		this.fulfillmentServiceId = fulfillmentServiceId;
	}

	public Integer getFulfillmentServiceId(){
		return fulfillmentServiceId;
	}

	public void setDiscountedPrice(Integer discountedPrice){
		this.discountedPrice = discountedPrice;
	}

	public Integer getDiscountedPrice(){
		return discountedPrice;
	}

	public void setDisplaySequnce(Integer displaySequnce){
		this.displaySequnce = displaySequnce;
	}

	public Integer getDisplaySequnce(){
		return displaySequnce;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;
	}

	public Integer getCategoryId(){
		return categoryId;
	}

	public void setPrice(Integer price){
		this.price = price;
	}

	public Integer getPrice(){
		return price;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductId(){
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