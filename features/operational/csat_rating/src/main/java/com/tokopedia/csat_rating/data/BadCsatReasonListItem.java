package com.tokopedia.csat_rating.data;

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

	protected BadCsatReasonListItem(Parcel in) {
		messageEn = in.readString();
		id = in.readInt();
		message = in.readString();
	}

	public BadCsatReasonListItem() {
	}

	public static final Creator<BadCsatReasonListItem> CREATOR = new Creator<BadCsatReasonListItem>() {
		@Override
		public BadCsatReasonListItem createFromParcel(Parcel in) {
			return new BadCsatReasonListItem(in);
		}

		@Override
		public BadCsatReasonListItem[] newArray(int size) {
			return new BadCsatReasonListItem[size];
		}
	};

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
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(messageEn);
		dest.writeInt(id);
		dest.writeString(message);
	}
}
