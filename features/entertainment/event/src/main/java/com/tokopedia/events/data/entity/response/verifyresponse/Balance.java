package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class Balance{

	@SerializedName("threshold_limit_text")
	private String thresholdLimitText;

	@SerializedName("is_active")
	private boolean isActive;

	@SerializedName("balance")
	private int balance;

	@SerializedName("useable_balance")
	private int useableBalance;

	@SerializedName("useable_balance_text")
	private String useableBalanceText;

	@SerializedName("threshold_text")
	private String thresholdText;

	@SerializedName("threshold_limit")
	private int thresholdLimit;

	@SerializedName("balance_text")
	private String balanceText;

	@SerializedName("limit")
	private int limit;

	@SerializedName("threshold")
	private int threshold;

	@SerializedName("limit_text")
	private String limitText;

	public void setThresholdLimitText(String thresholdLimitText){
		this.thresholdLimitText = thresholdLimitText;
	}

	public String getThresholdLimitText(){
		return thresholdLimitText;
	}

	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}

	public boolean isIsActive(){
		return isActive;
	}

	public void setBalance(int balance){
		this.balance = balance;
	}

	public int getBalance(){
		return balance;
	}

	public void setUseableBalance(int useableBalance){
		this.useableBalance = useableBalance;
	}

	public int getUseableBalance(){
		return useableBalance;
	}

	public void setUseableBalanceText(String useableBalanceText){
		this.useableBalanceText = useableBalanceText;
	}

	public String getUseableBalanceText(){
		return useableBalanceText;
	}

	public void setThresholdText(String thresholdText){
		this.thresholdText = thresholdText;
	}

	public String getThresholdText(){
		return thresholdText;
	}

	public void setThresholdLimit(int thresholdLimit){
		this.thresholdLimit = thresholdLimit;
	}

	public int getThresholdLimit(){
		return thresholdLimit;
	}

	public void setBalanceText(String balanceText){
		this.balanceText = balanceText;
	}

	public String getBalanceText(){
		return balanceText;
	}

	public void setLimit(int limit){
		this.limit = limit;
	}

	public int getLimit(){
		return limit;
	}

	public void setThreshold(int threshold){
		this.threshold = threshold;
	}

	public int getThreshold(){
		return threshold;
	}

	public void setLimitText(String limitText){
		this.limitText = limitText;
	}

	public String getLimitText(){
		return limitText;
	}

	@Override
 	public String toString(){
		return 
			"Balance{" + 
			"threshold_limit_text = '" + thresholdLimitText + '\'' + 
			",is_active = '" + isActive + '\'' + 
			",balance = '" + balance + '\'' + 
			",useable_balance = '" + useableBalance + '\'' + 
			",useable_balance_text = '" + useableBalanceText + '\'' + 
			",threshold_text = '" + thresholdText + '\'' + 
			",threshold_limit = '" + thresholdLimit + '\'' + 
			",balance_text = '" + balanceText + '\'' + 
			",limit = '" + limit + '\'' + 
			",threshold = '" + threshold + '\'' + 
			",limit_text = '" + limitText + '\'' + 
			"}";
		}
}