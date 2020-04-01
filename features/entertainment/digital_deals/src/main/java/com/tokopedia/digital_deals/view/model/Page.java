package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Page implements Parcelable {

	@SerializedName("uri_prev")
	private String uriPrev;

	@SerializedName("uri_next")
	private String uriNext;

	public void setUriPrev(String uriPrev) {
		this.uriPrev = uriPrev;
	}

	public String getUriPrev() {
		return uriPrev;
	}

	public void setUriNext(String uriNext) {
		this.uriNext = uriNext;
	}

	public String getUriNext() {
		return uriNext;
	}

	@Override
	public String toString() {
		return
				"Page{" +
						"uri_prev = '" + uriPrev + '\'' +
						",uri_next = '" + uriNext + '\'' +
						"}";
	}

	public final static Parcelable.Creator<Page> CREATOR = new Parcelable.Creator<Page>() {


		@SuppressWarnings({
				"unchecked"
		})
		public Page createFromParcel(Parcel in) {
			return new Page(in);
		}

		public Page[] newArray(int size) {
			return (new Page[size]);
		}

	};

	protected Page(Parcel in) {
		this.uriNext = in.readString();
		this.uriPrev = in.readString();
	}

	public Page() {
	}


	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(uriNext);
		dest.writeString(uriPrev);
	}

	public int describeContents() {
		return 0;
	}

}