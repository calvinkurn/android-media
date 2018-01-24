package com.tokopedia.core.analytics.model;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by herdimac on 6/24/16.
 */
public class Product extends BaseAnalyticsModel {

    private String brand;
    private String variant;
    private String quantity;
    private String shopType;
    private String shopName;
    private String cartId;
    private String price;
    private String type;
    private String categoryName;
    private String categoryId;
    private String url;
    private String imageUrl;
    private String shopId;
    private int position;
    private String list;
    private String userId;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, String> getAttr() {
        return extraAttr;
    }

    @Override
    public void setExtraAttr(Map<String, String> extraAttr) {
        this.extraAttr = extraAttr;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getList() {
        return list;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getProductAsDataLayerForFeedRecomItemClick() {
        return DataLayer.mapOf(
                "name", getName(),
                "id", getId(),
                "price", getPrice(),
                "brand", getBrand(),
                "category", getCategoryName(),
                "variant", getVariant(),
                "list", getList(),
                "position", getPosition(),
                "userId", getUserId()
        );
    }

    public Map<String, Object> getProductAsDataLayerForSearchResultItemClick() {
        return DataLayer.mapOf(
                "name", getName(),
                "id", getId(),
                "price", getPrice(),
                "brand", getBrand(),
                "category", getCategoryName(),
                "variant", getVariant(),
                "list", getList(),
                "position", Integer.toString(getPosition()),
                "userId", getUserId()
        );
    }
}
