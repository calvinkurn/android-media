package com.tokopedia.digital_deals.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartItems implements Parcelable {

	@SerializedName("cart_items")
	private List<CartItem> cartItems;

	@SerializedName("promocode")
	private String promocode;

	public void setCartItems(List<CartItem> cartItems){
		this.cartItems = cartItems;
	}

	public List<CartItem> getCartItems(){
		return cartItems;
	}

	public void setPromocode(String promocode){
		this.promocode = promocode;
	}

	public String getPromocode(){
		return promocode;
	}

	@Override
 	public String toString(){
		return 
			"CartItems{" + 
			"cart_items = '" + cartItems + '\'' + 
			",promocode = '" + promocode + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.cartItems);
		dest.writeString(this.promocode);
	}

	public CartItems() {
	}

	protected CartItems(Parcel in) {
		this.cartItems = new ArrayList<CartItem>();
		in.readList(this.cartItems, CartItem.class.getClassLoader());
		this.promocode = in.readString();
	}

	public static final Creator<CartItems> CREATOR = new Creator<CartItems>() {
		@Override
		public CartItems createFromParcel(Parcel source) {
			return new CartItems(source);
		}

		@Override
		public CartItems[] newArray(int size) {
			return new CartItems[size];
		}
	};
}