package com.tokopedia.events.domain.model.request.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OtherChargesItem implements Parcelable {

	@SerializedName("conv_fee")
	private int convFee;

	public void setConvFee(int convFee){
		this.convFee = convFee;
	}

	public int getConvFee(){
		return convFee;
	}

	@Override
 	public String toString(){
		return 
			"OtherChargesItem{" + 
			"conv_fee = '" + convFee + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.convFee);
	}

	public OtherChargesItem() {
	}

	protected OtherChargesItem(Parcel in) {
		this.convFee = in.readInt();
	}

	public static final Creator<OtherChargesItem> CREATOR = new Creator<OtherChargesItem>() {
		@Override
		public OtherChargesItem createFromParcel(Parcel source) {
			return new OtherChargesItem(source);
		}

		@Override
		public OtherChargesItem[] newArray(int size) {
			return new OtherChargesItem[size];
		}
	};
}