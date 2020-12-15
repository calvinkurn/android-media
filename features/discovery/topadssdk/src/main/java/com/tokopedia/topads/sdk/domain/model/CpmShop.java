package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmShop implements Parcelable {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOMAIN = "domain";
    private static final String KEY_TAGLINE = "tagline";
    private static final String KEY_SLOGAN = "slogan";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_IMAGE_SHOP = "image_shop";
    private static final String KEY_IS_OFFICIAL_STORE = "shop_is_official";
    private static final String KEY_IS_POWER_MERCHANT = "gold_shop";
    private static final String KEY_MERCHANT_VOUCHERS = "merchant_vouchers";
    private static final String KEY_IS_FOLLOWED = "is_followed";

    @SerializedName(KEY_ID)
    private String id;
    @SerializedName(KEY_NAME)
    private String name;
    @SerializedName(KEY_DOMAIN)
    private String domain;
    @SerializedName(KEY_TAGLINE)
    private String tagline;
    @SerializedName(KEY_SLOGAN)
    private String slogan;
    @SerializedName(KEY_PRODUCT)
    private List<Product> products = new ArrayList<>();
    @SerializedName(KEY_IMAGE_SHOP)
    private ImageShop imageShop;
    @SerializedName(KEY_IS_OFFICIAL_STORE)
    private boolean isOfficial;
    @SerializedName(KEY_IS_POWER_MERCHANT)
    private boolean isPowerMerchant;
    @SerializedName(KEY_IS_FOLLOWED)
    private boolean isFollowed;
    @SerializedName(KEY_MERCHANT_VOUCHERS)
    private List<String> merchantVouchers = new ArrayList<>();

    public CpmShop(){
    }

    public CpmShop(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)){
            setId(object.getString(KEY_ID));
        }
        if(!object.isNull(KEY_NAME)){
            setName(object.getString(KEY_NAME));
        }
        if(!object.isNull(KEY_DOMAIN)){
            setDomain(object.getString(KEY_DOMAIN));
        }
        if(!object.isNull(KEY_TAGLINE)){
            setTagline(object.getString(KEY_TAGLINE));
        }
        if(!object.isNull(KEY_SLOGAN)){
            setSlogan(object.getString(KEY_SLOGAN));
        }
        if(!object.isNull(KEY_PRODUCT)){
            JSONArray productArray = object.getJSONArray(KEY_PRODUCT);
            for (int i = 0; i < productArray.length(); i++) {
                products.add(new Product(productArray.getJSONObject(i)));
            }
        }
        if(!object.isNull(KEY_IMAGE_SHOP)){
            setImageShop(new ImageShop(object.getJSONObject(KEY_IMAGE_SHOP)));
        }
        if(!object.isNull(KEY_IS_OFFICIAL_STORE)){
            setOfficial(object.getBoolean(KEY_IS_OFFICIAL_STORE));
        }
        if(!object.isNull(KEY_IS_POWER_MERCHANT)){
            setPowerMerchant(object.getBoolean(KEY_IS_POWER_MERCHANT));
        }
        if(!object.isNull(KEY_MERCHANT_VOUCHERS)){
            JSONArray merchantVouchersArray = object.getJSONArray(KEY_MERCHANT_VOUCHERS);
            for (int i = 0; i < merchantVouchersArray.length(); i++) {
                merchantVouchers.add(merchantVouchersArray.getString(i));
            }
        }
    }


    protected CpmShop(Parcel in) {
        id = in.readString();
        name = in.readString();
        domain = in.readString();
        tagline = in.readString();
        slogan = in.readString();
        products = in.createTypedArrayList(Product.CREATOR);
        imageShop = in.readParcelable(ImageShop.class.getClassLoader());
        isOfficial = in.readByte() != 0;
        isPowerMerchant = in.readByte() != 0;
        in.readStringList(merchantVouchers);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(domain);
        dest.writeString(tagline);
        dest.writeString(slogan);
        dest.writeTypedList(products);
        dest.writeParcelable(imageShop, flags);
        dest.writeByte((byte) (isOfficial ? 1 : 0));
        dest.writeByte((byte) (isPowerMerchant ? 1 : 0));
        dest.writeStringList(merchantVouchers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CpmShop> CREATOR = new Creator<CpmShop>() {
        @Override
        public CpmShop createFromParcel(Parcel in) {
            return new CpmShop(in);
        }

        @Override
        public CpmShop[] newArray(int size) {
            return new CpmShop[size];
        }
    };

    public boolean isPowerMerchant() {
        return isPowerMerchant;
    }

    public void setPowerMerchant(boolean powerMerchant) {
        isPowerMerchant = powerMerchant;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public ImageShop getImageShop() {
        return imageShop;
    }

    public void setImageShop(ImageShop imageShop) {
        this.imageShop = imageShop;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public void setMerchantVouchers(List<String> merchantVouchers) {
        this.merchantVouchers = merchantVouchers;
    }

    public List<String> getMerchantVouchers() {
        return this.merchantVouchers;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
