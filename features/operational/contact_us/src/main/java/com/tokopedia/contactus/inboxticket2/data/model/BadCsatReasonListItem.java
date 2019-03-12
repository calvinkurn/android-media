package com.tokopedia.contactus.inboxticket2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BadCsatReasonListItem implements Parcelable {
	@SerializedName("messageEn")
	private String messageEn;
	@SerializedName("id")
	private int id;
	@SerializedName("message")
	private String message;

	public void setMessageEn(String messageEn){
		this.messageEn = messageEn;
	}

	public String getMessageEn(){
		return messageEn;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"BadCsatReasonListItem{" + 
			"message_en = '" + messageEn + '\'' + 
			",id = '" + id + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.messageEn);
		dest.writeInt(this.id);
		dest.writeString(this.message);
	}

	public BadCsatReasonListItem() {
	}

	protected BadCsatReasonListItem(Parcel in) {
		this.messageEn = in.readString();
		this.id = in.readInt();
		this.message = in.readString();
	}

	public static final Parcelable.Creator<BadCsatReasonListItem> CREATOR = new Parcelable.Creator<BadCsatReasonListItem>() {
		@Override
		public BadCsatReasonListItem createFromParcel(Parcel source) {
			return new BadCsatReasonListItem(source);
		}

		@Override
		public BadCsatReasonListItem[] newArray(int size) {
			return new BadCsatReasonListItem[size];
		}
	};
}
