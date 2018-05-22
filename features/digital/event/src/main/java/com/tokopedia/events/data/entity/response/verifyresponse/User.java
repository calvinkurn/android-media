package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("completion")
	private int completion;

	@SerializedName("gender")
	private int gender;

	@SerializedName("phone_masked")
	private String phoneMasked;

	@SerializedName("register_date")
	private String registerDate;

	@SerializedName("profile_picture")
	private String profilePicture;

	@SerializedName("created_password")
	private boolean createdPassword;

	@SerializedName("client_id")
	private String clientId;

	@SerializedName("remember")
	private int remember;

	@SerializedName("access_token")
	private String accessToken;

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("bday")
	private String bday;

	@SerializedName("phone_verified")
	private boolean phoneVerified;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("lang")
	private String lang;

	@SerializedName("w_refresh_token")
	private String wRefreshToken;

	@SerializedName("Balance")
	private Balance balance;

	@SerializedName("email")
	private String email;

	@SerializedName("age")
	private int age;

	@SerializedName("status")
	private int status;

	public void setCompletion(int completion){
		this.completion = completion;
	}

	public int getCompletion(){
		return completion;
	}

	public void setGender(int gender){
		this.gender = gender;
	}

	public int getGender(){
		return gender;
	}

	public void setPhoneMasked(String phoneMasked){
		this.phoneMasked = phoneMasked;
	}

	public String getPhoneMasked(){
		return phoneMasked;
	}

	public void setRegisterDate(String registerDate){
		this.registerDate = registerDate;
	}

	public String getRegisterDate(){
		return registerDate;
	}

	public void setProfilePicture(String profilePicture){
		this.profilePicture = profilePicture;
	}

	public String getProfilePicture(){
		return profilePicture;
	}

	public void setCreatedPassword(boolean createdPassword){
		this.createdPassword = createdPassword;
	}

	public boolean isCreatedPassword(){
		return createdPassword;
	}

	public void setClientId(String clientId){
		this.clientId = clientId;
	}

	public String getClientId(){
		return clientId;
	}

	public void setRemember(int remember){
		this.remember = remember;
	}

	public int getRemember(){
		return remember;
	}

	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}

	public String getAccessToken(){
		return accessToken;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setBday(String bday){
		this.bday = bday;
	}

	public String getBday(){
		return bday;
	}

	public void setPhoneVerified(boolean phoneVerified){
		this.phoneVerified = phoneVerified;
	}

	public boolean isPhoneVerified(){
		return phoneVerified;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLang(String lang){
		this.lang = lang;
	}

	public String getLang(){
		return lang;
	}

	public void setWRefreshToken(String wRefreshToken){
		this.wRefreshToken = wRefreshToken;
	}

	public String getWRefreshToken(){
		return wRefreshToken;
	}

	public void setBalance(Balance balance){
		this.balance = balance;
	}

	public Balance getBalance(){
		return balance;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setAge(int age){
		this.age = age;
	}

	public int getAge(){
		return age;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"completion = '" + completion + '\'' + 
			",gender = '" + gender + '\'' + 
			",phone_masked = '" + phoneMasked + '\'' + 
			",register_date = '" + registerDate + '\'' + 
			",profile_picture = '" + profilePicture + '\'' + 
			",created_password = '" + createdPassword + '\'' + 
			",client_id = '" + clientId + '\'' + 
			",remember = '" + remember + '\'' + 
			",access_token = '" + accessToken + '\'' + 
			",full_name = '" + fullName + '\'' + 
			",bday = '" + bday + '\'' + 
			",phone_verified = '" + phoneVerified + '\'' + 
			",user_id = '" + userId + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",lang = '" + lang + '\'' + 
			",w_refresh_token = '" + wRefreshToken + '\'' + 
			",balance = '" + balance + '\'' + 
			",email = '" + email + '\'' + 
			",age = '" + age + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}