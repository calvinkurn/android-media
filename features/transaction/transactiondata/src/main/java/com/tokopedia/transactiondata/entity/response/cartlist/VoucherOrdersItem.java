package com.tokopedia.transactiondata.entity.response.cartlist;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class VoucherOrdersItem{

	@SerializedName("cart_id")
	private int cartId;

	@SerializedName("shop_id")
	private int shopId;

	@SerializedName("code")
	private String code;

	@SerializedName("unique_id")
	private String uniqueId;

	@SerializedName("cashback_wallet_amount")
	private int cashbackWalletAmount;

	@SerializedName("success")
	private boolean success;

	@SerializedName("discount_amount")
	private int discountAmount;

	@SerializedName("address_id")
	private int addressId;

	@SerializedName("is_po")
	private int isPo;

	@SerializedName("invoice_description")
	private String invoiceDescription;

	@SerializedName("type")
	private String type;

	@SerializedName("message")
	private Message message;

	@SerializedName("title_description")
	private String titleDescription;

	public void setCartId(int cartId){
		this.cartId = cartId;
	}

	public int getCartId(){
		return cartId;
	}

	public void setShopId(int shopId){
		this.shopId = shopId;
	}

	public int getShopId(){
		return shopId;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setUniqueId(String uniqueId){
		this.uniqueId = uniqueId;
	}

	public String getUniqueId(){
		return uniqueId;
	}

	public void setCashbackWalletAmount(int cashbackWalletAmount){
		this.cashbackWalletAmount = cashbackWalletAmount;
	}

	public int getCashbackWalletAmount(){
		return cashbackWalletAmount;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setDiscountAmount(int discountAmount){
		this.discountAmount = discountAmount;
	}

	public int getDiscountAmount(){
		return discountAmount;
	}

	public void setAddressId(int addressId){
		this.addressId = addressId;
	}

	public int getAddressId(){
		return addressId;
	}

	public void setIsPo(int isPo){
		this.isPo = isPo;
	}

	public int getIsPo(){
		return isPo;
	}

	public void setInvoiceDescription(String invoiceDescription){
		this.invoiceDescription = invoiceDescription;
	}

	public String getInvoiceDescription(){
		return invoiceDescription;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	public String getTitleDescription() {
		return titleDescription;
	}

	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}

	@Override
 	public String toString(){
		return 
			"VoucherOrdersItem{" + 
			"cart_id = '" + cartId + '\'' + 
			",shop_id = '" + shopId + '\'' + 
			",code = '" + code + '\'' + 
			",unique_id = '" + uniqueId + '\'' + 
			",cashback_wallet_amount = '" + cashbackWalletAmount + '\'' + 
			",success = '" + success + '\'' + 
			",discount_amount = '" + discountAmount + '\'' + 
			",address_id = '" + addressId + '\'' + 
			",is_po = '" + isPo + '\'' + 
			",invoice_description = '" + invoiceDescription + '\'' + 
			",type = '" + type + '\'' + 
			",message = '" + message + '\'' +
			",titleDescription = '" + titleDescription + '\'' +
			"}";
		}
}