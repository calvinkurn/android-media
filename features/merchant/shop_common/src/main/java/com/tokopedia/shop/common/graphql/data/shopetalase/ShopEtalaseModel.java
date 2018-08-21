package com.tokopedia.shop.common.graphql.data.shopetalase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_DEFAULT;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopEtalaseModel {
    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("count")
    @Expose
    private int count = 0;
    @SerializedName("type")
    @Expose
    private int type = ETALASE_DEFAULT;

    public ShopEtalaseModel(){}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public int getType() {
        return type;
    }
}
