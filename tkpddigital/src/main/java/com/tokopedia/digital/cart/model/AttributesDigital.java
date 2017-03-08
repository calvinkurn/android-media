package com.tokopedia.digital.cart.model;

/**
 * Created by Nabilla Sabbaha on 3/1/2017.
 */

public class AttributesDigital {

    private String userId;

    private String clientNumber;

    private String price;

    private String categoryName;

    private String operatorName;

    private long pricePlain;

    private UserInputPriceDigital userInputPrice;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getPricePlain() {
        return pricePlain;
    }

    public void setPricePlain(long pricePlain) {
        this.pricePlain = pricePlain;
    }

    public UserInputPriceDigital getUserInputPrice() {
        return userInputPrice;
    }

    public void setUserInputPrice(UserInputPriceDigital userInputPrice) {
        this.userInputPrice = userInputPrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
