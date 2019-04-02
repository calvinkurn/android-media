package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Data implements Parcelable {

    private static final String KEY_ID = "id";
    private static final String KEY_AD_REF = "ad_ref_key";
    private static final String KEY_REDIRECT = "redirect";
    private static final String KEY_STICKER_ID = "sticker_id";
    private static final String KEY_STICKER_IMAGE = "sticker_image";
    private static final String KEY_PRODUCT_CLICK_URL = "product_click_url";
    private static final String KEY_PRODUCT_WISHLIST_URL = "product_wishlist_url";
    private static final String KEY_SHOP_CLICK_URL = "shop_click_url";
    private static final String KEY_SHOP = "shop";
    private static final String KEY_PRODUCT = "product";

    @SerializedName(KEY_ID)
    @Expose
    private String id = "";

    @SerializedName(KEY_AD_REF)
    @Expose
    private String adRefKey = "";

    @SerializedName(KEY_REDIRECT)
    @Expose
    private String redirect = "";

    @SerializedName(KEY_STICKER_ID)
    @Expose
    private String stickerId = "";

    @SerializedName(KEY_STICKER_IMAGE)
    @Expose
    private String stickerImage = "";

    @SerializedName(KEY_PRODUCT_CLICK_URL)
    @Expose
    private String productClickUrl = "";

    @SerializedName(KEY_PRODUCT_WISHLIST_URL)
    @Expose
    private String productWishlistUrl = "";

    @SerializedName(KEY_SHOP_CLICK_URL)
    @Expose
    private String shopClickUrl = "";

    @SerializedName(KEY_SHOP)
    @Expose
    private Shop shop = new Shop();

    @SerializedName(KEY_PRODUCT)
    @Expose
    private Product product = new Product();
    private boolean favorit = false;

    public Data() {
    }

    public Data(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)) {
            setId(object.getString(KEY_ID));
        }
        if(!object.isNull(KEY_AD_REF)) {
            setAdRefKey(object.getString(KEY_AD_REF));
        }
        if(!object.isNull(KEY_REDIRECT)) {
            setRedirect(object.getString(KEY_REDIRECT));
        }
        if(!object.isNull(KEY_STICKER_ID)) {
            setStickerId(object.getString(KEY_STICKER_ID));
        }
        if(!object.isNull(KEY_STICKER_IMAGE)) {
            setStickerImage(object.getString(KEY_STICKER_IMAGE));
        }
        if(!object.isNull(KEY_PRODUCT_CLICK_URL)) {
            setProductClickUrl(object.getString(KEY_PRODUCT_CLICK_URL));
        }
        if(!object.isNull(KEY_PRODUCT_WISHLIST_URL)) {
            setProductWishlistUrl(object.getString(KEY_PRODUCT_WISHLIST_URL));
        }
        if(!object.isNull(KEY_SHOP_CLICK_URL)) {
            setShopClickUrl(object.getString(KEY_SHOP_CLICK_URL));
        }
        if(!object.isNull(KEY_PRODUCT)) {
            setProduct(new Product(object.getJSONObject(KEY_PRODUCT)));
        }
        if(!object.isNull(KEY_SHOP)) {
            setShop(new Shop(object.getJSONObject(KEY_SHOP)));
        }
    }

    protected Data(Parcel in) {
        id = in.readString();
        adRefKey = in.readString();
        redirect = in.readString();
        stickerId = in.readString();
        stickerImage = in.readString();
        productClickUrl = in.readString();
        productWishlistUrl = in.readString();
        shopClickUrl = in.readString();
        shop = in.readParcelable(Shop.class.getClassLoader());
        product = in.readParcelable(Product.class.getClassLoader());
        favorit = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(adRefKey);
        dest.writeString(redirect);
        dest.writeString(stickerId);
        dest.writeString(stickerImage);
        dest.writeString(productClickUrl);
        dest.writeString(productWishlistUrl);
        dest.writeString(shopClickUrl);
        dest.writeParcelable(shop, flags);
        dest.writeParcelable(product, flags);
        dest.writeByte((byte) (favorit ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getShopClickUrl() {
        return shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerImage() {
        return stickerImage;
    }

    public void setStickerImage(String stickerImage) {
        this.stickerImage = stickerImage;
    }

    public String getProductClickUrl() {
        return productClickUrl;
    }

    public void setProductClickUrl(String productClickUrl) {
        this.productClickUrl = productClickUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isFavorit() {
        return favorit;
    }

    public void setFavorit(boolean favorit) {
        this.favorit = favorit;
    }

    public String getProductWishlistUrl() {
        return productWishlistUrl;
    }

    public void setProductWishlistUrl(String productWishlistUrl) {
        this.productWishlistUrl = productWishlistUrl;
    }
}
