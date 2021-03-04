
package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("user")
    @Expose
    private UserOwner user;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public UserOwner getUser() {
        return user;
    }

    public void setUser(UserOwner user) {
        this.user = user;
    }

}
