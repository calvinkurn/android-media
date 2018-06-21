package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class EntityAddress{

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("district")
	private String district;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("name")
	private String name;

	@SerializedName("state")
	private String state;

	@SerializedName("email")
	private String email;

	@SerializedName("longitude")
	private String longitude;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		return district;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"EntityAddress{" + 
			"address = '" + address + '\'' + 
			",city = '" + city + '\'' + 
			",district = '" + district + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",name = '" + name + '\'' + 
			",state = '" + state + '\'' + 
			",email = '" + email + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}