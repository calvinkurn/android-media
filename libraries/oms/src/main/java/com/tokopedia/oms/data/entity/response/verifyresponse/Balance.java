package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class Balance{

	@SerializedName("threshold_limit_text")
	private String thresholdLimitText;

	@SerializedName("is_active")
	private boolean isActive;

	@SerializedName("balance")
	private Integer balance;

	@SerializedName("useable_balance")
	private Integer useableBalance;

	@SerializedName("useable_balance_text")
	private String useableBalanceText;

	@SerializedName("threshold_text")
	private String thresholdText;

	@SerializedName("threshold_limit")
	private Integer thresholdLimit;

	@SerializedName("balance_text")
	private String balanceText;

	@SerializedName("limit")
	private Integer limit;

	@SerializedName("threshold")
	private Integer threshold;

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

	public void setBalance(Integer balance){
		this.balance = balance;
	}

	public Integer getBalance(){
		return balance;
	}

	public void setUseableBalance(Integer useableBalance){
		this.useableBalance = useableBalance;
	}

	public Integer getUseableBalance(){
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

	public void setThresholdLimit(Integer thresholdLimit){
		this.thresholdLimit = thresholdLimit;
	}

	public Integer getThresholdLimit(){
		return thresholdLimit;
	}

	public void setBalanceText(String balanceText){
		this.balanceText = balanceText;
	}

	public String getBalanceText(){
		return balanceText;
	}

	public void setLimit(Integer limit){
		this.limit = limit;
	}

	public Integer getLimit(){
		return limit;
	}

	public void setThreshold(Integer threshold){
		this.threshold = threshold;
	}

	public Integer getThreshold(){
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