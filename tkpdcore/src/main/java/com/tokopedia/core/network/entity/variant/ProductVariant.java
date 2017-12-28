
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariant implements Parcelable {

    @SerializedName("parent_id")
    @Expose
    private long parentId;
    @SerializedName("default_child")
    @Expose
    private long defaultChild;
    @SerializedName("variant")
    @Expose
    private List<Variant> variant = null;
    @SerializedName("children")
    @Expose
    private List<Child> children = null;
    @SerializedName("sizechart")
    @Expose
    private String sizechart;

    private String productName="";
    private String productPrice="";
    private String productImageUrl="";


    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getDefaultChild() {
        return defaultChild;
    }

    public void setDefaultChild(long defaultChild) {
        this.defaultChild = defaultChild;
    }

    public List<Variant> getVariant() {
        return variant;
    }

    public void setVariant(List<Variant> variant) {
        this.variant = variant;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public String getSizechart() {
        return sizechart;
    }

    public void setSizechart(String sizechart) {
        this.sizechart = sizechart;
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
        parentId = in.readLong();
        defaultChild = in.readLong();
        if (in.readByte() == 0x01) {
            variant = new ArrayList<Variant>();
            in.readList(variant, Variant.class.getClassLoader());
        } else {
            variant = null;
        }
        if (in.readByte() == 0x01) {
            children = new ArrayList<Child>();
            in.readList(children, Child.class.getClassLoader());
        } else {
            children = null;
        }
        sizechart = in.readString();
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
        dest.writeLong(parentId);
        dest.writeLong(defaultChild);
        if (variant == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(variant);
        }
        if (children == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(children);
        }
        dest.writeString(sizechart);
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
