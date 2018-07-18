package com.tokopedia.digital_deals.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubConfig implements Parcelable {

	@SerializedName("name")
	private String name;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"SubConfig{" + 
			"name = '" + name + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
	}

	public SubConfig() {
	}

	protected SubConfig(Parcel in) {
		this.name = in.readString();
	}

	public static final Creator<SubConfig> CREATOR = new Creator<SubConfig>() {
		@Override
		public SubConfig createFromParcel(Parcel source) {
			return new SubConfig(source);
		}

		@Override
		public SubConfig[] newArray(int size) {
			return new SubConfig[size];
		}
	};
}