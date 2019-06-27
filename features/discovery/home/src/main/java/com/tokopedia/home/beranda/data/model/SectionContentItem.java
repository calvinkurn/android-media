package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SectionContentItem implements Parcelable {

	@SerializedName("textAttributes")
	private TextAttributes textAttributes;

	@SerializedName("tagAttributes")
	private TagAttributes tagAttributes;

	@SerializedName("type")
	private String type;

	protected SectionContentItem(Parcel in) {
		type = in.readString();
	}

	public static final Creator<SectionContentItem> CREATOR = new Creator<SectionContentItem>() {
		@Override
		public SectionContentItem createFromParcel(Parcel in) {
			return new SectionContentItem(in);
		}

		@Override
		public SectionContentItem[] newArray(int size) {
			return new SectionContentItem[size];
		}
	};

	public void setTextAttributes(TextAttributes textAttributes){
		this.textAttributes = textAttributes;
	}

	public TextAttributes getTextAttributes(){
		return textAttributes;
	}

	public void setTagAttributes(TagAttributes tagAttributes){
		this.tagAttributes = tagAttributes;
	}

	public TagAttributes getTagAttributes(){
		return tagAttributes;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"SectionContentItem{" + 
			"textAttributes = '" + textAttributes + '\'' + 
			",tagAttributes = '" + tagAttributes + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type);
	}
}