package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 19/01/2016.
 * - this didn't support wholesaleprice
 */
public class ProductFeedData2 {
    @SerializedName("status")
    @Expose
    Status status;

    @SerializedName("data")
    @Expose
    ArrayList<ProductFeed2> data;

    @SerializedName("header")
    @Expose
    Header header;

    @SerializedName("category")
    @Expose
    Category category;

//    @SerializedName("filter")
//    @Expose
//    String filter;


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<ProductFeed2> getData() {
        return data;
    }

    public void setData(ArrayList<ProductFeed2> data) {
        this.data = data;
    }

//    public String getFilter() {
//        return filter;
//    }
//
//    public void setFilter(String filter) {
//        this.filter = filter;
//    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public static class Category {
        @SerializedName("selected_id")
        @Expose
        String selectedId;
    }

    public static class Status {
        @SerializedName("message")
        @Expose
        String message;

        @SerializedName("error_code")
        @Expose
        String errorCode;
    }

    public static class Header {
        @SerializedName("total_data")
        @Expose
        String totalData;

        public String getTotalData() {
            return totalData;
        }

        public void setTotalData(String totalData) {
            this.totalData = totalData;
        }

        public int toInteger(){
            if(totalData!=null&&!totalData.equals("")){
                return Integer.parseInt(totalData);
            }else{
                return 0;
            }
        }
    }

    public static class ProductFeed2{
        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("uri")
        @Expose
        String uri;

        @SerializedName("price")
        @Expose
        String price;

        @SerializedName("shop")
        @Expose
        Shop shop;

        @SerializedName("image_uri")
        @Expose
        String imageUri;


        @SerializedName("shop_location")
        @Expose
        String shopLocation;


//        @SerializedName("wholesale_price")
//        @Expose
//        ArrayList<Wholesale_price> wholesalePrice;


        @SerializedName("condition")
        @Expose
        String condition;
    }

    public static class Wholesale_price {
        @SerializedName("price")
        @Expose
        String price;

        @SerializedName("count_min")
        @Expose
        String countMin;

        @SerializedName("count_max")
        @Expose
        String countMax;
    }

    public static class Shop {
        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("name")
        @Expose
        String name;

        @SerializedName("uri")
        @Expose
        String uri;

        @SerializedName("is_gold")
        @Expose
        String isGold;

        @SerializedName("rating")
        @Expose
        Rating rating;

        @SerializedName("location")
        @Expose
        String location;

        @SerializedName("reputation_image_uri")
        @Expose
        String reputationImageUri;

        @SerializedName("shop_lucky")
        @Expose
        String shopLucky;
    }

    public static class Rating {
        @SerializedName("speed")
        @Expose
        String speed;

        @SerializedName("reputation_score")
        @Expose
        String reputationScore;

        @SerializedName("service")
        @Expose
        String service;

        @SerializedName("accuracy")
        @Expose
        String accuracy;
    }

    public static ProductItem toProductItem(ProductFeed2 productFeed2){
        ProductItem productItem = new ProductItem();
        productItem.setId(productFeed2.id);// 1
        productItem.setImgUri(productFeed2.imageUri);// 6
        productItem.setIsNewGold(Integer.parseInt(productFeed2.shop.isGold));// 4
        productItem.setName(productFeed2.name);// 2
        productItem.setLuckyShop(productFeed2.shop.shopLucky);// 7
        productItem.setPrice(productFeed2.price);// 3
        productItem.setShop(productFeed2.shop.name);// 5
        productItem.setShopId(Integer.parseInt(productFeed2.shop.id));// 8
        productItem.setShopLocation(productFeed2.shopLocation);// 9
        return productItem;
    }

    public static ArrayList<ProductItem> toProductItems(List<ProductFeed2> productFeed2List){
        ArrayList<ProductItem> result = new ArrayList<>();
        for(ProductFeed2 productFeed2 : productFeed2List){
            result.add(toProductItem(productFeed2));
        }
        return result;
    }
}
