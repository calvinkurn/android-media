package com.tokopedia.transactiondata.entity.response.cartlist;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AutoapplyStack {

	@SerializedName("codes")
	private List<String> codes;

	@SerializedName("promo_code_id")
	private int promoCodeId;

	@SerializedName("voucher_orders")
	private List<VoucherOrdersItem> voucherOrders;

	@SerializedName("cashback_advocate_referral_amount")
	private int cashbackAdvocateReferralAmount;

	@SerializedName("cashback_wallet_amount")
	private int cashbackWalletAmount;

	@SerializedName("discount_amount")
	private int discountAmount;

	@SerializedName("title_description")
	private String titleDescription;

	@SerializedName("global_success")
	private boolean globalSuccess;

	@SerializedName("message")
	private Message message;

	@SerializedName("gateway_id")
	private String gatewayId;

	@SerializedName("is_coupon")
	private int isCoupon;

	@SerializedName("coupon_description")
	private String couponDescription;

	@SerializedName("success")
	private boolean success;

	@SerializedName("invoice_description")
	private String invoiceDescription;

	@SerializedName("cashback_voucher_description")
	private String cashbackVoucherDescription;

	public void setCodes(List<String> codes){
		this.codes = codes;
	}

	public List<String> getCodes(){
		return codes;
	}

	public void setPromoCodeId(int promoCodeId){
		this.promoCodeId = promoCodeId;
	}

	public int getPromoCodeId(){
		return promoCodeId;
	}

	public void setVoucherOrders(List<VoucherOrdersItem> voucherOrders){
		this.voucherOrders = voucherOrders;
	}

	public List<VoucherOrdersItem> getVoucherOrders(){
		return voucherOrders;
	}

	public void setCashbackAdvocateReferralAmount(int cashbackAdvocateReferralAmount){
		this.cashbackAdvocateReferralAmount = cashbackAdvocateReferralAmount;
	}

	public int getCashbackAdvocateReferralAmount(){
		return cashbackAdvocateReferralAmount;
	}

	public void setCashbackWalletAmount(int cashbackWalletAmount){
		this.cashbackWalletAmount = cashbackWalletAmount;
	}

	public int getCashbackWalletAmount(){
		return cashbackWalletAmount;
	}

	public void setDiscountAmount(int discountAmount){
		this.discountAmount = discountAmount;
	}

	public int getDiscountAmount(){
		return discountAmount;
	}

	public void setTitleDescription(String titleDescription){
		this.titleDescription = titleDescription;
	}

	public String getTitleDescription(){
		return titleDescription;
	}

	public void setGlobalSuccess(boolean globalSuccess){
		this.globalSuccess = globalSuccess;
	}

	public boolean isGlobalSuccess(){
		return globalSuccess;
	}

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	public void setGatewayId(String gatewayId){
		this.gatewayId = gatewayId;
	}

	public String getGatewayId(){
		return gatewayId;
	}

	public void setIsCoupon(int isCoupon){
		this.isCoupon = isCoupon;
	}

	public int getIsCoupon(){
		return isCoupon;
	}

	public void setCouponDescription(String couponDescription){
		this.couponDescription = couponDescription;
	}

	public String getCouponDescription(){
		return couponDescription;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setInvoiceDescription(String invoiceDescription){
		this.invoiceDescription = invoiceDescription;
	}

	public String getInvoiceDescription(){
		return invoiceDescription;
	}

	public void setCashbackVoucherDescription(String cashbackVoucherDescription){
		this.cashbackVoucherDescription = cashbackVoucherDescription;
	}

	public String getCashbackVoucherDescription(){
		return cashbackVoucherDescription;
	}
}