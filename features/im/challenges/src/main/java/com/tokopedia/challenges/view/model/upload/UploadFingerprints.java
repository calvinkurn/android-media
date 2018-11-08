package com.tokopedia.challenges.view.model.upload;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadFingerprints implements Serializable{

	@SerializedName("AuthorizationDate")
	private String authorizationDate;

	@SerializedName("ManifestId")
	private String manifestId;

	@SerializedName("BlockStart")
	private int blockStart;

	@SerializedName("UploadUrl")
	private String uploadUrl;

	@SerializedName("PartNumber")
	private int partNumber;

	@SerializedName("FileName")
	private String fileName;

	@SerializedName("PartsCompleted")
	private int partsCompleted;

	@SerializedName("StatusCode")
	private int statusCode;

	@SerializedName("StatusMessage")
	private String statusMessage;

	@SerializedName("NewPostId")
	private String newPostId;

	@SerializedName("Authorization")
	private String authorization;

	@SerializedName("EntityType")
	private String entityType;

	@SerializedName("FileUrl")
	private String fileUrl;

	@SerializedName("BlockEnd")
	private int blockEnd;

	@SerializedName("FileType")
	private String fileType;

	@SerializedName("TotalParts")
	private int totalParts;

	@SerializedName("Region")
	private String region;

	public void setAuthorizationDate(String authorizationDate){
		this.authorizationDate = authorizationDate;
	}

	public String getAuthorizationDate(){
		return authorizationDate;
	}

	public void setManifestId(String manifestId){
		this.manifestId = manifestId;
	}

	public String getManifestId(){
		return manifestId;
	}

	public void setBlockStart(int blockStart){
		this.blockStart = blockStart;
	}

	public int getBlockStart(){
		return blockStart;
	}

	public void setUploadUrl(String uploadUrl){
		this.uploadUrl = uploadUrl;
	}

	public String getUploadUrl(){
		return uploadUrl;
	}

	public void setPartNumber(int partNumber){
		this.partNumber = partNumber;
	}

	public int getPartNumber(){
		return partNumber;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileName(){
		return fileName;
	}

	public void setPartsCompleted(int partsCompleted){
		this.partsCompleted = partsCompleted;
	}

	public int getPartsCompleted(){
		return partsCompleted;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage(){
		return statusMessage;
	}

	public void setNewPostId(String newPostId){
		this.newPostId = newPostId;
	}

	public String getNewPostId(){
		return newPostId;
	}

	public void setAuthorization(String authorization){
		this.authorization = authorization;
	}

	public String getAuthorization(){
		return authorization;
	}

	public void setEntityType(String entityType){
		this.entityType = entityType;
	}

	public String getEntityType(){
		return entityType;
	}

	public void setFileUrl(String fileUrl){
		this.fileUrl = fileUrl;
	}

	public String getFileUrl(){
		return fileUrl;
	}

	public void setBlockEnd(int blockEnd){
		this.blockEnd = blockEnd;
	}

	public int getBlockEnd(){
		return blockEnd;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getFileType(){
		return fileType;
	}

	public void setTotalParts(int totalParts){
		this.totalParts = totalParts;
	}

	public int getTotalParts(){
		return totalParts;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}

	@Override
 	public String toString(){
		return 
			"UploadFingerprints{" + 
			"authorizationDate = '" + authorizationDate + '\'' + 
			",manifestId = '" + manifestId + '\'' + 
			",blockStart = '" + blockStart + '\'' + 
			",uploadUrl = '" + uploadUrl + '\'' + 
			",partNumber = '" + partNumber + '\'' + 
			",fileName = '" + fileName + '\'' + 
			",partsCompleted = '" + partsCompleted + '\'' + 
			",statusCode = '" + statusCode + '\'' + 
			",statusMessage = '" + statusMessage + '\'' + 
			",newPostId = '" + newPostId + '\'' + 
			",authorization = '" + authorization + '\'' + 
			",entityType = '" + entityType + '\'' + 
			",fileUrl = '" + fileUrl + '\'' + 
			",blockEnd = '" + blockEnd + '\'' + 
			",fileType = '" + fileType + '\'' + 
			",totalParts = '" + totalParts + '\'' + 
			",region = '" + region + '\'' + 
			"}";
		}
}