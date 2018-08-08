
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("user")
    @Expose
    private User_ user;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

}
