package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Items implements Serializable {

    @SerializedName("categoryID")
    @Expose
    private int categoryID;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("tapActions")
    @Expose
    private List<ActionButton> tapActions;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtons;

    @SerializedName("metaData")
    @Expose
    private String metaData;

    @SerializedName("totalPrice")
    @Expose
    private String totalPrice;

    @SerializedName("trackingNumber")
    @Expose
    private String trackingNumber;

    @SerializedName("invoiceID")
    @Expose
    private String invoiceId;

    private boolean isTapActionsLoaded;

    private boolean isActionButtonLoaded;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ActionButton> getTapActions() {
        return tapActions;
    }

    public void setTapActions(List<ActionButton> tapActions) {
        this.tapActions = tapActions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<ActionButton> getActionButtons() {
        return actionButtons;
    }

    public void setActionButtons(List<ActionButton> actionButtons) {
        this.actionButtons = actionButtons;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public boolean isTapActionsLoaded() {
        return isTapActionsLoaded;
    }

    public void setTapActionsLoaded(boolean tapActionsLoaded) {
        isTapActionsLoaded = tapActionsLoaded;
    }

    public boolean isActionButtonLoaded() {
        return isActionButtonLoaded;
    }

    public void setActionButtonLoaded(boolean actionButtonLoaded) {
        isActionButtonLoaded = actionButtonLoaded;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public String toString() {
        return "ClassPojo [title = " + title + ", tapActions = " + tapActions + ", price = " + price + ", imageUrl = " + imageUrl + ", quantity = " + quantity + ", actionButtons = " + actionButtons + ", metaData = " + metaData + "]";
    }
}
