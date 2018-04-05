package com.tokopedia.core.shopinfo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 10/13/2015.
 */
/**
 * Get shop info object from tkpd shop
 */
@Deprecated
public class ShopModel implements Parcelable {

    public String name;
    public String shopId = "";
    public String domain = "";
    public String avatar;
    public String cover;
    public String description;
    public String tag;
    public String adKey;
    public String ownerId;
    public String ownerName;
    public String ownerPicture;
    public String ownerEmail;
    public String reputation;
    public String status;
    public String closeNote;
    public String closeUntil;
    public String lastLogin;
    public String location;
    public String openSince;
    public String lucky;
    public String isGold;
    public String isOwner;
    public String totalEtalase;
    public String totalSale;
    public String totalSuccess;
    public String totalProduct;
    public String shopAddressName = "";
    public String shopAddress = "";
    public String shopAddressLocation = "";
    public String shopPhone = "0";
    public String shopEmail = "0";
    public String shopFax = "0";
    public int hasMoreAddress;
    public int isFav;
    public int totalFav;
    public int repLevel;
    public int repSet;
    public String repScore;
    public List<String> shippingAgency = new ArrayList<>();
    public List<String> shippingPackage = new ArrayList<>();
    public List<String> shippingLogo = new ArrayList<>();
    public String shopInfoJSON;

    public ShopModel(){}

    protected ShopModel(Parcel in) {
        name = in.readString();
        shopId = in.readString();
        domain = in.readString();
        avatar = in.readString();
        cover = in.readString();
        description = in.readString();
        tag = in.readString();
        adKey = in.readString();
        ownerId = in.readString();
        ownerName = in.readString();
        ownerPicture = in.readString();
        ownerEmail = in.readString();
        reputation = in.readString();
        status = in.readString();
        closeNote = in.readString();
        closeUntil = in.readString();
        lastLogin = in.readString();
        location = in.readString();
        openSince = in.readString();
        lucky = in.readString();
        isGold = in.readString();
        isOwner = in.readString();
        totalEtalase = in.readString();
        totalSale = in.readString();
        totalSuccess = in.readString();
        totalProduct = in.readString();
        shopAddressName = in.readString();
        shopAddress = in.readString();
        shopAddressLocation = in.readString();
        shopPhone = in.readString();
        shopEmail = in.readString();
        shopFax = in.readString();
        hasMoreAddress = in.readInt();
        isFav = in.readInt();
        totalFav = in.readInt();
        repLevel = in.readInt();
        repSet = in.readInt();
        repScore = in.readString();
        shippingAgency = in.createStringArrayList();
        shippingPackage = in.createStringArrayList();
        shippingLogo = in.createStringArrayList();
        shopInfoJSON = in.readString();
    }

    public static final Creator<ShopModel> CREATOR = new Creator<ShopModel>() {
        @Override
        public ShopModel createFromParcel(Parcel in) {
            return new ShopModel(in);
        }

        @Override
        public ShopModel[] newArray(int size) {
            return new ShopModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(shopId);
        dest.writeString(domain);
        dest.writeString(avatar);
        dest.writeString(cover);
        dest.writeString(description);
        dest.writeString(tag);
        dest.writeString(adKey);
        dest.writeString(ownerId);
        dest.writeString(ownerName);
        dest.writeString(ownerPicture);
        dest.writeString(ownerEmail);
        dest.writeString(reputation);
        dest.writeString(status);
        dest.writeString(closeNote);
        dest.writeString(closeUntil);
        dest.writeString(lastLogin);
        dest.writeString(location);
        dest.writeString(openSince);
        dest.writeString(lucky);
        dest.writeString(isGold);
        dest.writeString(isOwner);
        dest.writeString(totalEtalase);
        dest.writeString(totalSale);
        dest.writeString(totalSuccess);
        dest.writeString(totalProduct);
        dest.writeString(shopAddressName);
        dest.writeString(shopAddress);
        dest.writeString(shopAddressLocation);
        dest.writeString(shopPhone);
        dest.writeString(shopEmail);
        dest.writeString(shopFax);
        dest.writeInt(hasMoreAddress);
        dest.writeInt(isFav);
        dest.writeInt(totalFav);
        dest.writeInt(repLevel);
        dest.writeInt(repSet);
        dest.writeString(repScore);
        dest.writeStringList(shippingAgency);
        dest.writeStringList(shippingPackage);
        dest.writeStringList(shippingLogo);
        dest.writeString(shopInfoJSON);
    }
}
