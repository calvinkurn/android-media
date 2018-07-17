package com.tokopedia.core.network.entity.wishlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GqlWishListDataResponse {

    /*@SerializedName("data")
    @Expose
    GqlWishListData gqlWishListData;

    public GqlWishListData getGqlWishListData() {
        return gqlWishListData;
    }

    public void setWishlist(GqlWishListData gqlWishListData) {
        this.gqlWishListData = gqlWishListData;
    }*/


    @SerializedName("wishlist")
    @Expose
    GqlWishList gqlWishList;

    public GqlWishList getGqlWishList() {
        return gqlWishList;
    }

    public void setGqlWishList(GqlWishList gqlWishList) {
        this.gqlWishList = gqlWishList;
    }

    public class GqlWishList {

        @SerializedName("count")
        @Expose
        int count;

        @SerializedName("has_next_page")
        @Expose
        boolean hasNextPage;

        @SerializedName("items")
        @Expose
        List<Wishlist> wishlistDataList = new ArrayList<>();

        @SerializedName("pagination")
        @Expose
        Pagination pagination;

        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public List<Wishlist> getWishlistDataList() {
            return wishlistDataList;
        }

        public void setWishlistDataList(List<Wishlist> wishlistDataList) {
            this.wishlistDataList = wishlistDataList;
        }
    }
}
