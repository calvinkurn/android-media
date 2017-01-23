//package com.tokopedia.core.payment.model.responsecartstep1;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * CartProduct
// * Created by Angga.Prasetiyo on 05/07/2016.
// */
//public class CartProduct implements Parcelable {
//
//    @SerializedName("product_price_currency_value")
//    @Expose
//    private String productPriceCurrencyValue;
//    @SerializedName("errors")
//    @Expose
//    private List<Object> errors = new ArrayList<Object>();
//    @SerializedName("product_price_idr")
//    @Expose
//    private String productPriceIdr;
//    @SerializedName("product_total_price")
//    @Expose
//    private String productTotalPrice;
//    @SerializedName("product_status")
//    @Expose
//    private String productStatus;
//    @SerializedName("product_id")
//    @Expose
//    private String productId;
//    @SerializedName("product_cart_id")
//    @Expose
//    private String productCartId;
//    @SerializedName("product_use_insurance")
//    @Expose
//    private Integer productUseInsurance;
//    @SerializedName("product_price_original")
//    @Expose
//    private String productPriceOriginal;
//    @SerializedName("product_price")
//    @Expose
//    private String productPrice;
//    @SerializedName("product_returnable")
//    @Expose
//    private Integer productReturnable;
//    @SerializedName("product_total_weight")
//    @Expose
//    private String productTotalWeight;
//    @SerializedName("product_preorder")
//    @Expose
//    private ProductPreorder productPreorder;
//    @SerializedName("product_price_currency")
//    @Expose
//    private String productPriceCurrency;
//    @SerializedName("product_pic")
//    @Expose
//    private String productPic;
//    @SerializedName("product_min_order")
//    @Expose
//    private String productMinOrder;
//    @SerializedName("product_price_last")
//    @Expose
//    private String productPriceLast;
//    @SerializedName("product_must_insurance")
//    @Expose
//    private String productMustInsurance;
//    @SerializedName("product_total_price_idr")
//    @Expose
//    private String productTotalPriceIdr;
//    @SerializedName("product_notes")
//    @Expose
//    private String productNotes;
//    @SerializedName("product_quantity")
//    @Expose
//    private String productQuantity;
//    @SerializedName("product_weight")
//    @Expose
//    private String productWeight;
//    @SerializedName("product_price_updated")
//    @Expose
//    private String productPriceUpdated;
//    @SerializedName("product_error_msg")
//    @Expose
//    private String productErrorMsg;
//    @SerializedName("product_url")
//    @Expose
//    private String productUrl;
//    @SerializedName("product_name")
//    @Expose
//    private String productName;
//
//    public List<Object> getErrors() {
//        return errors;
//    }
//
//    public void setErrors(List<Object> errors) {
//        this.errors = errors;
//    }
//
//    public String getProductPriceIdr() {
//        return productPriceIdr;
//    }
//
//    public void setProductPriceIdr(String productPriceIdr) {
//        this.productPriceIdr = productPriceIdr;
//    }
//
//    public String getProductTotalPrice() {
//        return productTotalPrice;
//    }
//
//    public void setProductTotalPrice(String productTotalPrice) {
//        this.productTotalPrice = productTotalPrice;
//    }
//
//    public String getProductStatus() {
//        return productStatus;
//    }
//
//    public void setProductStatus(String productStatus) {
//        this.productStatus = productStatus;
//    }
//
//    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }
//
//    public String getProductCartId() {
//        return productCartId;
//    }
//
//    public void setProductCartId(String productCartId) {
//        this.productCartId = productCartId;
//    }
//
//    public Integer getProductUseInsurance() {
//        return productUseInsurance;
//    }
//
//    public void setProductUseInsurance(Integer productUseInsurance) {
//        this.productUseInsurance = productUseInsurance;
//    }
//
//    public String getProductPriceOriginal() {
//        return productPriceOriginal;
//    }
//
//    public void setProductPriceOriginal(String productPriceOriginal) {
//        this.productPriceOriginal = productPriceOriginal;
//    }
//
//    public String getProductPrice() {
//        return productPrice;
//    }
//
//    public void setProductPrice(String productPrice) {
//        this.productPrice = productPrice;
//    }
//
//    public Integer getProductReturnable() {
//        return productReturnable;
//    }
//
//    public void setProductReturnable(Integer productReturnable) {
//        this.productReturnable = productReturnable;
//    }
//
//    public String getProductTotalWeight() {
//        return productTotalWeight;
//    }
//
//    public void setProductTotalWeight(String productTotalWeight) {
//        this.productTotalWeight = productTotalWeight;
//    }
//
//    public ProductPreorder getProductPreorder() {
//        return productPreorder;
//    }
//
//    public void setProductPreorder(ProductPreorder productPreorder) {
//        this.productPreorder = productPreorder;
//    }
//
//    public String getProductPriceCurrency() {
//        return productPriceCurrency;
//    }
//
//    public void setProductPriceCurrency(String productPriceCurrency) {
//        this.productPriceCurrency = productPriceCurrency;
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
//    public String getProductMinOrder() {
//        return productMinOrder;
//    }
//
//    public void setProductMinOrder(String productMinOrder) {
//        this.productMinOrder = productMinOrder;
//    }
//
//    public String getProductPriceLast() {
//        return productPriceLast;
//    }
//
//    public void setProductPriceLast(String productPriceLast) {
//        this.productPriceLast = productPriceLast;
//    }
//
//    public String getProductMustInsurance() {
//        return productMustInsurance;
//    }
//
//    public void setProductMustInsurance(String productMustInsurance) {
//        this.productMustInsurance = productMustInsurance;
//    }
//
//    public String getProductTotalPriceIdr() {
//        return productTotalPriceIdr;
//    }
//
//    public void setProductTotalPriceIdr(String productTotalPriceIdr) {
//        this.productTotalPriceIdr = productTotalPriceIdr;
//    }
//
//    public String getProductNotes() {
//        return productNotes;
//    }
//
//    public void setProductNotes(String productNotes) {
//        this.productNotes = productNotes;
//    }
//
//    public String getProductQuantity() {
//        return productQuantity;
//    }
//
//    public void setProductQuantity(String productQuantity) {
//        this.productQuantity = productQuantity;
//    }
//
//    public String getProductWeight() {
//        return productWeight;
//    }
//
//    public void setProductWeight(String productWeight) {
//        this.productWeight = productWeight;
//    }
//
//    public String getProductPriceUpdated() {
//        return productPriceUpdated;
//    }
//
//    public void setProductPriceUpdated(String productPriceUpdated) {
//        this.productPriceUpdated = productPriceUpdated;
//    }
//
//    public String getProductErrorMsg() {
//        return productErrorMsg;
//    }
//
//    public void setProductErrorMsg(String productErrorMsg) {
//        this.productErrorMsg = productErrorMsg;
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
//    public String getProductPriceCurrencyValue() {
//        return productPriceCurrencyValue;
//    }
//
//    public void setProductPriceCurrencyValue(String productPriceCurrencyValue) {
//        this.productPriceCurrencyValue = productPriceCurrencyValue;
//    }
//
//    protected CartProduct(Parcel in) {
//        productPriceCurrencyValue = in.readString();
//        if (in.readByte() == 0x01) {
//            errors = new ArrayList<Object>();
//            in.readList(errors, Object.class.getClassLoader());
//        } else {
//            errors = null;
//        }
//        productPriceIdr = in.readString();
//        productTotalPrice = in.readString();
//        productStatus = in.readString();
//        productId = in.readString();
//        productCartId = in.readString();
//        productUseInsurance = in.readByte() == 0x00 ? null : in.readInt();
//        productPriceOriginal = in.readString();
//        productPrice = in.readString();
//        productReturnable = in.readByte() == 0x00 ? null : in.readInt();
//        productTotalWeight = in.readString();
//        productPreorder = (ProductPreorder) in.readValue(ProductPreorder.class.getClassLoader());
//        productPriceCurrency = in.readString();
//        productPic = in.readString();
//        productMinOrder = in.readString();
//        productPriceLast = in.readString();
//        productMustInsurance = in.readString();
//        productTotalPriceIdr = in.readString();
//        productNotes = in.readString();
//        productQuantity = in.readString();
//        productWeight = in.readString();
//        productPriceUpdated = in.readString();
//        productErrorMsg = in.readString();
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
//        dest.writeString(productPriceCurrencyValue);
//        if (errors == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeList(errors);
//        }
//        dest.writeString(productPriceIdr);
//        dest.writeString(productTotalPrice);
//        dest.writeString(productStatus);
//        dest.writeString(productId);
//        dest.writeString(productCartId);
//        if (productUseInsurance == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(productUseInsurance);
//        }
//        dest.writeString(productPriceOriginal);
//        dest.writeString(productPrice);
//        if (productReturnable == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(productReturnable);
//        }
//        dest.writeString(productTotalWeight);
//        dest.writeValue(productPreorder);
//        dest.writeString(productPriceCurrency);
//        dest.writeString(productPic);
//        dest.writeString(productMinOrder);
//        dest.writeString(productPriceLast);
//        dest.writeString(productMustInsurance);
//        dest.writeString(productTotalPriceIdr);
//        dest.writeString(productNotes);
//        dest.writeString(productQuantity);
//        dest.writeString(productWeight);
//        dest.writeString(productPriceUpdated);
//        dest.writeString(productErrorMsg);
//        dest.writeString(productUrl);
//        dest.writeString(productName);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<CartProduct> CREATOR = new Parcelable.Creator<CartProduct>() {
//        @Override
//        public CartProduct createFromParcel(Parcel in) {
//            return new CartProduct(in);
//        }
//
//        @Override
//        public CartProduct[] newArray(int size) {
//            return new CartProduct[size];
//        }
//    };
//}
