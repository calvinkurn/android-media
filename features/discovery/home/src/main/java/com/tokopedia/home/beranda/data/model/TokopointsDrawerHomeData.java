package com.tokopedia.home.beranda.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TokopointsDrawerHomeData implements Parcelable {

	@SerializedName("tokopointsDrawer")
	private TokopointsDrawer tokopointsDrawer;

	protected TokopointsDrawerHomeData(Parcel in) {
	}

	public static final Creator<TokopointsDrawerHomeData> CREATOR = new Creator<TokopointsDrawerHomeData>() {
		@Override
		public TokopointsDrawerHomeData createFromParcel(Parcel in) {
			return new TokopointsDrawerHomeData(in);
		}

		@Override
		public TokopointsDrawerHomeData[] newArray(int size) {
			return new TokopointsDrawerHomeData[size];
		}
	};

	public void setTokopointsDrawer(TokopointsDrawer tokopointsDrawer){
		this.tokopointsDrawer = tokopointsDrawer;
	}

	public TokopointsDrawer getTokopointsDrawer(){
		return tokopointsDrawer;
	}

	@Override
 	public String toString(){
		return 
			"TokopointsDrawerHomeData{" + 
			"tokopointsDrawer = '" + tokopointsDrawer + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
}