package com.tokopedia.events.domain.model.request.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CartItem implements Parcelable {

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("configuration")
	private Configuration configuration;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("meta_data")
	private MetaData metaData;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setConfiguration(Configuration configuration){
		this.configuration = configuration;
	}

	public Configuration getConfiguration(){
		return configuration;
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
			"CartItem{" +
			"quantity = '" + quantity + '\'' + 
			",configuration = '" + configuration + '\'' + 
			",product_id = '" + productId + '\'' + 
			",meta_data = '" + metaData + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.quantity);
		dest.writeParcelable(this.configuration, flags);
		dest.writeInt(this.productId);
		dest.writeParcelable(this.metaData, flags);
	}

	public CartItem() {
	}

	protected CartItem(Parcel in) {
		this.quantity = in.readInt();
		this.configuration = in.readParcelable(Configuration.class.getClassLoader());
		this.productId = in.readInt();
		this.metaData = in.readParcelable(MetaData.class.getClassLoader());
	}

	public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
		@Override
		public CartItem createFromParcel(Parcel source) {
			return new CartItem(source);
		}

		@Override
		public CartItem[] newArray(int size) {
			return new CartItem[size];
		}
	};
}