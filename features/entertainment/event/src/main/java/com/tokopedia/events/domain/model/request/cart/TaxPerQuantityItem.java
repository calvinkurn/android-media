package com.tokopedia.events.domain.model.request.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TaxPerQuantityItem implements Parcelable {

	@SerializedName("entertaiment")
	private int entertaiment;
	@SerializedName("service")
	private int service;

	public void setEntertaiment(int entertaiment){
		this.entertaiment = entertaiment;
	}

	public int getEntertaiment(){
		return entertaiment;
	}

	@Override
 	public String toString(){
		return 
			"TaxPerQuantityItem{" + 
			"entertaiment = '" + entertaiment + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.entertaiment);
		dest.writeInt(this.service);
	}

	public TaxPerQuantityItem() {
	}

	protected TaxPerQuantityItem(Parcel in) {
		this.entertaiment = in.readInt();
		this.service = in.readInt();
	}

	public static final Creator<TaxPerQuantityItem> CREATOR = new Creator<TaxPerQuantityItem>() {
		@Override
		public TaxPerQuantityItem createFromParcel(Parcel source) {
			return new TaxPerQuantityItem(source);
		}

		@Override
		public TaxPerQuantityItem[] newArray(int size) {
			return new TaxPerQuantityItem[size];
		}
	};
}