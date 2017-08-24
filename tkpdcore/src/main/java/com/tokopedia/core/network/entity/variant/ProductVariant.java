
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariant implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("variant_option")
    @Expose
    private List<VariantOption> variantOption = null;
    @SerializedName("variant_data")
    @Expose
    private List<VariantDatum> variantData = null;

    private String productName="";
    private String productPrice="";
    private String productImageUrl="";

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<VariantOption> getVariantOption() {
        return variantOption;
    }

    public void setVariantOption(List<VariantOption> variantOption) {
        this.variantOption = variantOption;
    }

    public List<VariantDatum> getVariantData() {
        return variantData;
    }

    public void setVariantData(List<VariantDatum> variantData) {
        this.variantData = variantData;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    protected ProductVariant(Parcel in) {
        productId = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            variantOption = new ArrayList<VariantOption>();
            in.readList(variantOption, VariantOption.class.getClassLoader());
        } else {
            variantOption = null;
        }
        if (in.readByte() == 0x01) {
            variantData = new ArrayList<VariantDatum>();
            in.readList(variantData, VariantDatum.class.getClassLoader());
        } else {
            variantData = null;
        }
        productName = in.readString();
        productPrice = in.readString();
        productImageUrl = in.readString();
    }

    public List<Integer> getCombinationFromSelectedVariant(int variantOptionId) {
        List<Integer> products = new ArrayList<>();
        for (VariantDatum variantDatum: getVariantData()) {
            if (variantDatum.getOptionIds().contains(variantOptionId)) {
                products.addAll(variantDatum.getOptionIds());
            }
        }
        products.removeAll(Collections.singleton(variantOptionId));
        products.add(variantOptionId);
        return products;
    }

    public int getLevel1Variant() {
        if (variantOption!=null && variantOption.size()>1 && variantOption.get(0).getPosition()!=1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (productId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productId);
        }
        if (variantOption == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(variantOption);
        }
        if (variantData == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(variantData);
        }
        dest.writeString(productName);
        dest.writeString(productPrice);
        dest.writeString(productImageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductVariant> CREATOR = new Parcelable.Creator<ProductVariant>() {
        @Override
        public ProductVariant createFromParcel(Parcel in) {
            return new ProductVariant(in);
        }

        @Override
        public ProductVariant[] newArray(int size) {
            return new ProductVariant[size];
        }
    };
}