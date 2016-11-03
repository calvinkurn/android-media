package com.tokopedia.tkpd.addtocart.model.responseatcform;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail implements Parcelable {

    @SerializedName("product_picture")
    @Expose
    private String productPicture;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_cat_name")
    @Expose
    private String productCatName;
    @SerializedName("product_min_order")
    @Expose
    private String productMinOrder;
    @SerializedName("product_must_insurance")
    @Expose
    private Integer productMustInsurance;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_cat_id")
    @Expose
    private String productCatId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_weight_gram")
    @Expose
    private String productWeightGram;
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorder productPreorder;

    protected ProductDetail(Parcel in) {
        productPicture = in.readString();
        productPrice = in.readString();
        productCatName = in.readString();
        productMinOrder = in.readString();
        productMustInsurance = in.readByte() == 0x00 ? null : in.readInt();
        productId = in.readString();
        productWeight = in.readString();
        productCatId = in.readString();
        productName = in.readString();
        productWeightGram = in.readString();
        productPreorder = (ProductPreorder) in.readValue(ProductPreorder.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productPicture);
        dest.writeString(productPrice);
        dest.writeString(productCatName);
        dest.writeString(productMinOrder);
        if (productMustInsurance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productMustInsurance);
        }
        dest.writeString(productId);
        dest.writeString(productWeight);
        dest.writeString(productCatId);
        dest.writeString(productName);
        dest.writeString(productWeightGram);
        dest.writeValue(productPreorder);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductDetail> CREATOR = new Parcelable.Creator<ProductDetail>() {
        @Override
        public ProductDetail createFromParcel(Parcel in) {
            return new ProductDetail(in);
        }

        @Override
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };

    /**
     *
     * @return
     * The productPicture
     */
    public String getProductPicture() {
        return productPicture;
    }

    /**
     *
     * @param productPicture
     * The product_picture
     */
    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    /**
     *
     * @return
     * The productPrice
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     *
     * @param productPrice
     * The product_price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    /**
     *
     * @return
     * The productCatName
     */
    public String getProductCatName() {
        return productCatName;
    }

    /**
     *
     * @param productCatName
     * The product_cat_name
     */
    public void setProductCatName(String productCatName) {
        this.productCatName = productCatName;
    }

    /**
     *
     * @return
     * The productMinOrder
     */
    public String getProductMinOrder() {
        return productMinOrder;
    }

    /**
     *
     * @param productMinOrder
     * The product_min_order
     */
    public void setProductMinOrder(String productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    /**
     *
     * @return
     * The productMustInsurance
     */
    public Integer getProductMustInsurance() {
        return productMustInsurance;
    }

    /**
     *
     * @param productMustInsurance
     * The product_must_insurance
     */
    public void setProductMustInsurance(Integer productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    /**
     *
     * @return
     * The productId
     */
    public String  getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     * The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     * The productWeight
     */
    public String getProductWeight() {
        return productWeight;
    }

    /**
     *
     * @param productWeight
     * The product_weight
     */
    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    /**
     *
     * @return
     * The productCatId
     */
    public String getProductCatId() {
        return productCatId;
    }

    /**
     *
     * @param productCatId
     * The product_cat_id
     */
    public void setProductCatId(String productCatId) {
        this.productCatId = productCatId;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The productWeightGram
     */
    public String getProductWeightGram() {
        return productWeightGram;
    }

    /**
     *
     * @param productWeightGram
     * The product_weight_gram
     */
    public void setProductWeightGram(String productWeightGram) {
        this.productWeightGram = productWeightGram;
    }

    public ProductPreorder getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(ProductPreorder productPreorder) {
        this.productPreorder = productPreorder;
    }
}
