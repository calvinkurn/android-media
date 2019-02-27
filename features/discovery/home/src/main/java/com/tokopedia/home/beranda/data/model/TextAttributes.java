package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TextAttributes implements Parcelable {

	@SerializedName("colour")
	private String colour;

	@SerializedName("text")
	private String text;

	@SerializedName("isBold")
	private boolean isBold;

	protected TextAttributes(Parcel in) {
		colour = in.readString();
		text = in.readString();
		isBold = in.readByte() != 0;
	}

	public static final Creator<TextAttributes> CREATOR = new Creator<TextAttributes>() {
		@Override
		public TextAttributes createFromParcel(Parcel in) {
			return new TextAttributes(in);
		}

		@Override
		public TextAttributes[] newArray(int size) {
			return new TextAttributes[size];
		}
	};

	public void setColour(String colour){
		this.colour = colour;
	}

	public String getColour(){
		return colour;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setIsBold(boolean isBold){
		this.isBold = isBold;
	}

	public boolean isIsBold(){
		return isBold;
	}

	@Override
 	public String toString(){
		return 
			"TextAttributes{" + 
			"colour = '" + colour + '\'' + 
			",text = '" + text + '\'' + 
			",isBold = '" + isBold + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(colour);
		dest.writeString(text);
		dest.writeByte((byte) (isBold ? 1 : 0));
	}
}