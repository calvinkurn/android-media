package com.tokopedia.wishlist.common.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveWishListResponse {

    @SerializedName("wishlist_remove_v2")
    @Expose
    private WishlistRemove wishlist_remove;

    public WishlistRemove getWishlistRemove() {
        return wishlist_remove;
    }

    public void setWishlist_add(WishlistRemove wishlist_add) {
        this.wishlist_remove = wishlist_add;
    }

    @Override
    public String toString() {
        return "ClassPojo [wishlist_add = " + wishlist_remove + "]";
    }


    public class WishlistRemove {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("__typename")
        @Expose
        private String __typename;

        @SerializedName("success")
        @Expose
        private boolean success;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String get__typename() {
            return __typename;
        }

        public void set__typename(String __typename) {
            this.__typename = __typename;
        }

        public boolean getSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        @Override
        public String toString() {
            return "ClassPojo [id = " + id + ", __typename = " + __typename + ", success = " + success + "]";
        }
    }

}

