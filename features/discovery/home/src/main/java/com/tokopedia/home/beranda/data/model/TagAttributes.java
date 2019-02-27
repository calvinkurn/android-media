package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TagAttributes implements Parcelable {

	@SerializedName("text")
	private String text;

	@SerializedName("backgroundColour")
	private String backgroundColour;

	protected TagAttributes(Parcel in) {
		text = in.readString();
		backgroundColour = in.readString();
	}

	public static final Creator<TagAttributes> CREATOR = new Creator<TagAttributes>() {
		@Override
		public TagAttributes createFromParcel(Parcel in) {
			return new TagAttributes(in);
		}

		@Override
		public TagAttributes[] newArray(int size) {
			return new TagAttributes[size];
		}
	};

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setBackgroundColour(String backgroundColour){
		this.backgroundColour = backgroundColour;
	}

	public String getBackgroundColour(){
		return backgroundColour;
	}

	@Override
 	public String toString(){
		return 
			"TagAttributes{" + 
			"text = '" + text + '\'' + 
			",backgroundColour = '" + backgroundColour + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(text);
		dest.writeString(backgroundColour);
	}
}