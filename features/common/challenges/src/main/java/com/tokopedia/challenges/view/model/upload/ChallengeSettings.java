package com.tokopedia.challenges.view.model.upload;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.challenges.view.model.Me;
import com.tokopedia.challenges.view.model.upload.Collection;

public class ChallengeSettings{

	@SerializedName("UploadAllowed")
	private boolean uploadAllowed;

	@SerializedName("Me")
	private Me me;

	@SerializedName("Collection")
	private Collection collection;

	@SerializedName("AllowVideos")
	private boolean allowVideos;

	@SerializedName("AllowPhotos")
	private boolean allowPhotos;

	public void setUploadAllowed(boolean uploadAllowed){
		this.uploadAllowed = uploadAllowed;
	}

	public boolean isUploadAllowed(){
		return uploadAllowed;
	}

	public void setMe(Me me){
		this.me = me;
	}

	public Me getMe(){
		return me;
	}

	public void setCollection(Collection collection){
		this.collection = collection;
	}

	public Collection getCollection(){
		return collection;
	}

	public void setAllowVideos(boolean allowVideos){
		this.allowVideos = allowVideos;
	}

	public boolean isAllowVideos(){
		return allowVideos;
	}

	public void setAllowPhotos(boolean allowPhotos){
		this.allowPhotos = allowPhotos;
	}

	public boolean isAllowPhotos(){
		return allowPhotos;
	}

	@Override
 	public String toString(){
		return 
			"ChallengeSettings{" + 
			"uploadAllowed = '" + uploadAllowed + '\'' + 
			",me = '" + me + '\'' + 
			",collection = '" + collection + '\'' + 
			",allowVideos = '" + allowVideos + '\'' + 
			",allowPhotos = '" + allowPhotos + '\'' + 
			"}";
		}
}