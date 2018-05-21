package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class PageViewModel implements Parcelable
{

	private String uriNext;
	private String uriPrev;

	public final static Parcelable.Creator<PageViewModel> CREATOR = new Creator<PageViewModel>() {


		@SuppressWarnings({
				"unchecked"
		})
		public PageViewModel createFromParcel(Parcel in) {
			return new PageViewModel(in);
		}

		public PageViewModel[] newArray(int size) {
			return (new PageViewModel[size]);
		}

	}
			;

	protected PageViewModel(Parcel in) {
		this.uriNext = ((String) in.readValue((String.class.getClassLoader())));
		this.uriPrev = ((String) in.readValue((String.class.getClassLoader())));
	}

	public PageViewModel() {
	}

	public String getUriNext() {
		return uriNext;
	}

	public void setUriNext(String uriNext) {
		this.uriNext = uriNext;
	}

	public String getUriPrev() {
		return uriPrev;
	}

	public void setUriPrev(String uriPrev) {
		this.uriPrev = uriPrev;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(uriNext);
		dest.writeValue(uriPrev);
	}

	public int describeContents() {
		return 0;
	}

}