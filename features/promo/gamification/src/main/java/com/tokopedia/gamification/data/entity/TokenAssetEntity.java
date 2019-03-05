package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TokenAssetEntity implements Parcelable {

	@SerializedName("smallImgUrl")
	private String smallImgUrl;

	@SerializedName("floatingImgUrl")
	private String floatingImgUrl;

	@SerializedName("imageUrls")
	private List<String> imageUrls;

	@SerializedName("name")
	private String name;

	@SerializedName("imagev2Urls")
	private List<String> imagev2Urls;

	@SerializedName("spriteUrl")
	private String spriteUrl;

	@SerializedName("version")
	private int version;

	@SerializedName("smallImgv2Url")
	private String smallImgv2Url;

	@SerializedName("tokenSourceUrl")
	private String tokenSourceUrl;

    protected TokenAssetEntity(Parcel in) {
        smallImgUrl = in.readString();
        floatingImgUrl = in.readString();
        imageUrls = in.createStringArrayList();
        name = in.readString();
        imagev2Urls = in.createStringArrayList();
        spriteUrl = in.readString();
        version = in.readInt();
        smallImgv2Url = in.readString();
        tokenSourceUrl = in.readString();
    }

    public static final Creator<TokenAssetEntity> CREATOR = new Creator<TokenAssetEntity>() {
        @Override
        public TokenAssetEntity createFromParcel(Parcel in) {
            return new TokenAssetEntity(in);
        }

        @Override
        public TokenAssetEntity[] newArray(int size) {
            return new TokenAssetEntity[size];
        }
    };

    public void setSmallImgUrl(String smallImgUrl){
		this.smallImgUrl = smallImgUrl;
	}

	public String getSmallImgUrl(){
		return smallImgUrl;
	}

	public void setFloatingImgUrl(String floatingImgUrl){
		this.floatingImgUrl = floatingImgUrl;
	}

	public String getFloatingImgUrl(){
		return floatingImgUrl;
	}

	public void setImageUrls(List<String> imageUrls){
		this.imageUrls = imageUrls;
	}

	public List<String> getImageUrls(){
		return imageUrls;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setImagev2Urls(List<String> imagev2Urls){
		this.imagev2Urls = imagev2Urls;
	}

	public List<String> getImagev2Urls(){
		return imagev2Urls;
	}

	public void setSpriteUrl(String spriteUrl){
		this.spriteUrl = spriteUrl;
	}

	public String getSpriteUrl(){
		return spriteUrl;
	}

	public void setVersion(int version){
		this.version = version;
	}

	public int getVersion(){
		return version;
	}

	public void setSmallImgv2Url(String smallImgv2Url){
		this.smallImgv2Url = smallImgv2Url;
	}

	public String getSmallImgv2Url(){
		return smallImgv2Url;
	}

	public String getTokenSourceUrl() {
		return tokenSourceUrl;
	}

	public void setTokenSourceUrl(String tokenSourceUrl) {
		this.tokenSourceUrl = tokenSourceUrl;
	}

	@Override
 	public String toString(){
		return 
			"TokenAssetEntity{" + 
			"smallImgUrl = '" + smallImgUrl + '\'' + 
			",floatingImgUrl = '" + floatingImgUrl + '\'' + 
			",imageUrls = '" + imageUrls + '\'' + 
			",name = '" + name + '\'' + 
			",imagev2Urls = '" + imagev2Urls + '\'' + 
			",spriteUrl = '" + spriteUrl + '\'' + 
			",version = '" + version + '\'' + 
			",smallImgv2Url = '" + smallImgv2Url + '\'' + 
			"}";
		}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(smallImgUrl);
        dest.writeString(floatingImgUrl);
        dest.writeStringList(imageUrls);
        dest.writeString(name);
        dest.writeStringList(imagev2Urls);
        dest.writeString(spriteUrl);
        dest.writeInt(version);
        dest.writeString(smallImgv2Url);
        dest.writeString(tokenSourceUrl);
    }
}