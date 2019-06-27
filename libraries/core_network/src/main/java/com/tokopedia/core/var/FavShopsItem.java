package com.tokopedia.core.var;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Deprecated
public class FavShopsItem extends RecyclerViewItem implements Parcelable {

	@SerializedName("image")
	@Expose
	private String image;
	@SerializedName("badge")
	@Expose
	private List<Badge> badge;

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("reputation")
	@Expose
	private Reputation reputation;
	@SerializedName("location")
	@Expose
	private String location;
	@SerializedName("id")
	@Expose
	private String id;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setBadge(List<Badge> badge){
		this.badge = badge;
	}

	public List<Badge> getBadge(){
		return badge;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setReputation(Reputation reputation){
		this.reputation = reputation;
	}

	public Reputation getReputation(){
		return reputation;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.image);
		dest.writeString(this.name);
		dest.writeString(this.location);
		dest.writeString(this.id);
		dest.writeTypedList(this.badge);
	}

	protected FavShopsItem(Parcel in) {
		super(in);
		this.image = in.readString();
		this.name = in.readString();
		this.location = in.readString();
		this.id = in.readString();
		this.badge = in.createTypedArrayList(Badge.CREATOR);
	}

	public static final Creator<FavShopsItem> CREATOR = new Creator<FavShopsItem>() {
		@Override
		public FavShopsItem createFromParcel(Parcel source) {
			return new FavShopsItem(source);
		}

		@Override
		public FavShopsItem[] newArray(int size) {
			return new FavShopsItem[size];
		}
	};

	@Override
 	public String toString(){
		return 
			"FavShopsItem{" +
			"image = '" + image + '\'' + 
			",badge = '" + badge + '\'' +
			",name = '" + name + '\'' + 
			",reputation = '" + reputation + '\'' + 
			",location = '" + location + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}