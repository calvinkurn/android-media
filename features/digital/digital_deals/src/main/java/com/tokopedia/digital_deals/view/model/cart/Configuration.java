package com.tokopedia.digital_deals.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Configuration implements Parcelable {

	@SerializedName("price")
	private Integer price;

	@SerializedName("sub_config")
	private SubConfig subConfig;

	public void setPrice(Integer price){
		this.price = price;
	}

	public Integer getPrice(){
		return price;
	}

	public void setSubConfig(SubConfig subConfig){
		this.subConfig = subConfig;
	}

	public SubConfig getSubConfig(){
		return subConfig;
	}

	@Override
 	public String toString(){
		return 
			"Configuration{" + 
			"price = '" + price + '\'' + 
			",sub_config = '" + subConfig + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.price);
		dest.writeParcelable(this.subConfig, flags);
	}

	public Configuration() {
	}

	protected Configuration(Parcel in) {
		this.price = in.readInt();
		this.subConfig = in.readParcelable(SubConfig.class.getClassLoader());
	}

	public static final Creator<Configuration> CREATOR = new Creator<Configuration>() {
		@Override
		public Configuration createFromParcel(Parcel source) {
			return new Configuration(source);
		}

		@Override
		public Configuration[] newArray(int size) {
			return new Configuration[size];
		}
	};
}