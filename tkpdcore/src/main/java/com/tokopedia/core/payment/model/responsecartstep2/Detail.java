//package com.tokopedia.core.payment.model.responsecartstep2;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
///**
// * Detail
// * Created by Angga.Prasetiyo on 15/07/2016.
// */
//public class Detail implements Parcelable {
//    @SerializedName("p_name_enc")
//    @Expose
//    private String pNameEnc;
//    @SerializedName("category_name")
//    @Expose
//    private String categoryName;
//    @SerializedName("product_pic")
//    @Expose
//    private String productPic;
//    @SerializedName("qty")
//    @Expose
//    private Integer qty;
//    @SerializedName("is_preorder")
//    @Expose
//    private Integer isPreorder;
//    @SerializedName("category_id")
//    @Expose
//    private String categoryId;
//    @SerializedName("product_id")
//    @Expose
//    private Integer productId;
//    @SerializedName("catalog_id")
//    @Expose
//    private String catalogId;
//    @SerializedName("category")
//    @Expose
//    private String category;
//    @SerializedName("price")
//    @Expose
//    private String price;
//    @SerializedName("product_url")
//    @Expose
//    private String productUrl;
//    @SerializedName("product_name")
//    @Expose
//    private String productName;
//
//    public String getpNameEnc() {
//        return pNameEnc;
//    }
//
//    public void setpNameEnc(String pNameEnc) {
//        this.pNameEnc = pNameEnc;
//    }
//
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
//
//    public String getProductPic() {
//        return productPic;
//    }
//
//    public void setProductPic(String productPic) {
//        this.productPic = productPic;
//    }
//
//    public Integer getQty() {
//        return qty;
//    }
//
//    public void setQty(Integer qty) {
//        this.qty = qty;
//    }
//
//    public Integer getIsPreorder() {
//        return isPreorder;
//    }
//
//    public void setIsPreorder(Integer isPreorder) {
//        this.isPreorder = isPreorder;
//    }
//
//    public String getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(String categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public Integer getProductId() {
//        return productId;
//    }
//
//    public void setProductId(Integer productId) {
//        this.productId = productId;
//    }
//
//    public String getCatalogId() {
//        return catalogId;
//    }
//
//    public void setCatalogId(String catalogId) {
//        this.catalogId = catalogId;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
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
//    public String getProductUrl() {
//        return productUrl;
//    }
//
//    public void setProductUrl(String productUrl) {
//        this.productUrl = productUrl;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//    protected Detail(Parcel in) {
//        pNameEnc = in.readString();
//        categoryName = in.readString();
//        productPic = in.readString();
//        qty = in.readByte() == 0x00 ? null : in.readInt();
//        isPreorder = in.readByte() == 0x00 ? null : in.readInt();
//        categoryId = in.readString();
//        productId = in.readByte() == 0x00 ? null : in.readInt();
//        catalogId = in.readString();
//        category = in.readString();
//        price = in.readString();
//        productUrl = in.readString();
//        productName = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(pNameEnc);
//        dest.writeString(categoryName);
//        dest.writeString(productPic);
//        if (qty == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(qty);
//        }
//        if (isPreorder == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(isPreorder);
//        }
//        dest.writeString(categoryId);
//        if (productId == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(productId);
//        }
//        dest.writeString(catalogId);
//        dest.writeString(category);
//        dest.writeString(price);
//        dest.writeString(productUrl);
//        dest.writeString(productName);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<Detail> CREATOR = new Parcelable.Creator<Detail>() {
//        @Override
//        public Detail createFromParcel(Parcel in) {
//            return new Detail(in);
//        }
//
//        @Override
//        public Detail[] newArray(int size) {
//            return new Detail[size];
//        }
//    };
//}
