//package com.tokopedia.core.payment.model.responsedynamicpayment;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
///**
// * Created by Angga.Prasetiyo on 19/05/2016.
// */
//public class Item implements Parcelable {
//    private static final String TAG = Item.class.getSimpleName();
//
//    @SerializedName("id")
//    @Expose
//    private String id;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("price")
//    @Expose
//    private String price;
//    @SerializedName("quantity")
//    @Expose
//    private String quantity;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
//
//    protected Item(Parcel in) {
//        id = in.readString();
//        name = in.readString();
//        price = in.readString();
//        quantity = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(id);
//        dest.writeString(name);
//        dest.writeString(price);
//        dest.writeString(quantity);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
//        @Override
//        public Item createFromParcel(Parcel in) {
//            return new Item(in);
//        }
//
//        @Override
//        public Item[] newArray(int size) {
//            return new Item[size];
//        }
//    };
//}
