package com.tokopedia.events.data.entity.response.checkoutreponse;

import com.google.gson.annotations.SerializedName;

public class CheckoutResponse{

	@SerializedName("thanks_url")
	private String thanksUrl;

	@SerializedName("parameter")
	private Parameter parameter;

	@SerializedName("callback_url_failed")
	private String callbackUrlFailed;

	@SerializedName("redirect_url")
	private String redirectUrl;

	@SerializedName("callback_url_success")
	private String callbackUrlSuccess;

	@SerializedName("query_string")
	private String queryString;

	public void setThanksUrl(String thanksUrl){
		this.thanksUrl = thanksUrl;
	}

	public String getThanksUrl(){
		return thanksUrl;
	}

	public void setParameter(Parameter parameter){
		this.parameter = parameter;
	}

	public Parameter getParameter(){
		return parameter;
	}

	public void setCallbackUrlFailed(String callbackUrlFailed){
		this.callbackUrlFailed = callbackUrlFailed;
	}

	public String getCallbackUrlFailed(){
		return callbackUrlFailed;
	}

	public void setRedirectUrl(String redirectUrl){
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrl(){
		return redirectUrl;
	}

	public void setCallbackUrlSuccess(String callbackUrlSuccess){
		this.callbackUrlSuccess = callbackUrlSuccess;
	}

	public String getCallbackUrlSuccess(){
		return callbackUrlSuccess;
	}

	public void setQueryString(String queryString){
		this.queryString = queryString;
	}

	public String getQueryString(){
		return queryString;
	}

	@Override
 	public String toString(){
		return 
			"CheckoutResponse{" + 
			"thanks_url = '" + thanksUrl + '\'' + 
			",parameter = '" + parameter + '\'' + 
			",callback_url_failed = '" + callbackUrlFailed + '\'' + 
			",redirect_url = '" + redirectUrl + '\'' + 
			",callback_url_success = '" + callbackUrlSuccess + '\'' + 
			",query_string = '" + queryString + '\'' + 
			"}";
		}
}