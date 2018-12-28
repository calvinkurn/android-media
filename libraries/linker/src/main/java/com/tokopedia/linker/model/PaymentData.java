package com.tokopedia.linker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentData {

    List<HashMap<String, String>> products = new ArrayList<>();
    public String paymentId = "";
    public String productType = "";
    public String revenue = "";
    public String revenueIDRtoDouble = "";
    public String shipping = "";
    public String shippingIDRtoDouble = "";
    public String orderId = "";
    public String marketType = "";
    public String itemPrice = "";

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<HashMap<String, String>> getProducts() {
        return products;
    }

    public void setProducts(List<HashMap<String, String>> products) {
        this.products = products;
    }

    public void setProduct(HashMap<String, String> product) {
        products.add(product);
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getRevenueIDRtoDouble() {
        return revenueIDRtoDouble;
    }

    public void setRevenueIDRtoDouble(String revenueIDRtoDouble) {
        this.revenueIDRtoDouble = revenueIDRtoDouble;
    }

    public String getShippingIDRtoDouble() {
        return shippingIDRtoDouble;
    }

    public void setShippingIDRtoDouble(String shippingIDRtoDouble) {
        this.shippingIDRtoDouble = shippingIDRtoDouble;
    }
}
