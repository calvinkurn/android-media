package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Shop implements Parcelable {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOMAIN = "domain";
    private static final String KEY_TAGLINE = "tagline";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_CITY = "city";
    private static final String KEY_IMAGE_SHOP = "image_shop";
    private static final String KEY_GOLD_SHOP = "gold_shop";
    private static final String KEY_GOLD_SHOP_BADGE = "gold_shop_badge";
    private static final String KEY_LUCKY_SHOP = "lucky_shop";
    private static final String KEY_SHOP_IS_OFFICIAL = "shop_is_official";
    private static final String KEY_URI = "uri";
    private static final String KEY_IMAGE_PRODUCT = "image_product";
    private static final String KEY_OWNER_ID = "owner_id";
    private static final String KEY_IS_OWNER = "is_owner";
    private static final String KEY_BADGES = "badges";

    @SerializedName(KEY_ID)
    @Expose
    private String id;

    private String adRefKey;
    private String adId;

    @SerializedName(KEY_NAME)
    @Expose
    private String name;

    @SerializedName(KEY_DOMAIN)
    @Expose
    private String domain;

    @SerializedName(KEY_TAGLINE)
    @Expose
    private String tagline;

    @SerializedName(KEY_LOCATION)
    @Expose
    private String location;

    @SerializedName(KEY_CITY)
    @Expose
    private String city;

    @SerializedName(KEY_IMAGE_SHOP)
    @Expose
    private ImageShop imageShop;

    @SerializedName(KEY_GOLD_SHOP)
    @Expose
    private boolean goldShop;

    @SerializedName(KEY_GOLD_SHOP_BADGE)
    @Expose
    private boolean goldShopBadge;

    @SerializedName(KEY_LUCKY_SHOP)
    @Expose
    private String luckyShop;

    @SerializedName(KEY_SHOP_IS_OFFICIAL)
    @Expose
    private boolean shopIsOfficial;

    @SerializedName(KEY_URI)
    @Expose
    private String uri;

    @SerializedName(KEY_IMAGE_PRODUCT)
    @Expose
    private List<ImageProduct> imageProduct = new ArrayList<>();

    @SerializedName(KEY_OWNER_ID)
    @Expose
    private String ownerId;

    @SerializedName(KEY_IS_OWNER)
    @Expose
    private boolean isOwner;
    private boolean loaded;
    @SerializedName(KEY_BADGES)
    @Expose
    private List<Badge> badges = new ArrayList<>();

    public Shop() {
    }

    public Shop(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)) {
            setId(object.getString(KEY_ID));
        }
        if(!object.isNull(KEY_NAME)) {
            setName(object.getString(KEY_NAME));
        }
        if(!object.isNull(KEY_DOMAIN)) {
            setDomain(object.getString(KEY_DOMAIN));
        }
        if(!object.isNull(KEY_TAGLINE)) {
            setTagline(object.getString(KEY_TAGLINE));
        }
        if(!object.isNull(KEY_LOCATION)) {
            setLocation(object.getString(KEY_LOCATION));
        }
        if(!object.isNull(KEY_CITY)) {
            setCity(object.getString(KEY_CITY));
        }
        if(!object.isNull(KEY_IMAGE_SHOP)) {
            setImageShop(new ImageShop(object.getJSONObject(KEY_IMAGE_SHOP)));
        }
        if(!object.isNull(KEY_GOLD_SHOP)) {
            setGoldShop(object.getBoolean(KEY_GOLD_SHOP));
        }
        if(!object.isNull(KEY_GOLD_SHOP_BADGE)) {
            setGoldShopBadge(object.getBoolean(KEY_GOLD_SHOP_BADGE));
        }
        if(!object.isNull(KEY_LUCKY_SHOP)) {
            setLuckyShop(object.getString(KEY_LUCKY_SHOP));
        }
        if(!object.isNull(KEY_SHOP_IS_OFFICIAL)) {
            setShop_is_official(object.getBoolean(KEY_SHOP_IS_OFFICIAL));
        }
        if(!object.isNull(KEY_URI)) {
            setUri(object.getString(KEY_URI));
        }
        if(!object.isNull(KEY_OWNER_ID)) {
            setOwnerId(object.getString(KEY_OWNER_ID));
        }
        if(!object.isNull(KEY_IS_OWNER)) {
            setOwner(object.getBoolean(KEY_IS_OWNER));
        }
        if(!object.isNull(KEY_IMAGE_PRODUCT)) {
            JSONArray imageProductArray = object.getJSONArray(KEY_IMAGE_PRODUCT);
            for (int i = 0; i < imageProductArray.length(); i++) {
                imageProduct.add(new ImageProduct(imageProductArray.getJSONObject(i)));
            }
        }
        if(!object.isNull(KEY_BADGES)) {
            JSONArray badgeArray = object.getJSONArray(KEY_BADGES);
            for (int i = 0; i < badgeArray.length(); i++) {
                badges.add(new Badge(badgeArray.getJSONObject(i)));
            }
        }
    }

    protected Shop(Parcel in) {
        id = in.readString();
        adRefKey = in.readString();
        adId = in.readString();
        name = in.readString();
        domain = in.readString();
        tagline = in.readString();
        location = in.readString();
        city = in.readString();
        imageShop = in.readParcelable(ImageShop.class.getClassLoader());
        goldShop = in.readByte() != 0;
        goldShopBadge = in.readByte() != 0;
        luckyShop = in.readString();
        shopIsOfficial = in.readByte() != 0;
        uri = in.readString();
        imageProduct = in.createTypedArrayList(ImageProduct.CREATOR);
        ownerId = in.readString();
        isOwner = in.readByte() != 0;
        badges = in.createTypedArrayList(Badge.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(adRefKey);
        dest.writeString(adId);
        dest.writeString(name);
        dest.writeString(domain);
        dest.writeString(tagline);
        dest.writeString(location);
        dest.writeString(city);
        dest.writeParcelable(imageShop, flags);
        dest.writeByte((byte) (goldShop ? 1 : 0));
        dest.writeByte((byte) (goldShopBadge ? 1 : 0));
        dest.writeString(luckyShop);
        dest.writeByte((byte) (shopIsOfficial ? 1 : 0));
        dest.writeString(uri);
        dest.writeTypedList(imageProduct);
        dest.writeString(ownerId);
        dest.writeByte((byte) (isOwner ? 1 : 0));
        dest.writeTypedList(badges);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ImageShop getImageShop() {
        return imageShop;
    }

    public void setImageShop(ImageShop imageShop) {
        this.imageShop = imageShop;
    }

    public boolean isGoldShop() {
        return goldShop;
    }

    public void setGoldShop(boolean goldShop) {
        this.goldShop = goldShop;
    }

    public boolean isGoldShopBadge() {
        return goldShopBadge;
    }

    public void setGoldShopBadge(boolean goldShopBadge) {
        this.goldShopBadge = goldShopBadge;
    }

    public String getLuckyShop() {
        return luckyShop;
    }

    public void setLuckyShop(String luckyShop) {
        this.luckyShop = luckyShop;
    }

    public boolean isShop_is_official() {
        return shopIsOfficial;
    }

    public void setShop_is_official(boolean shop_is_official) {
        this.shopIsOfficial = shop_is_official;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<ImageProduct> getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(List<ImageProduct> imageProduct) {
        this.imageProduct = imageProduct;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean is_owner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        this.isOwner = owner;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
