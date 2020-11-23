package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderPriority {
    @SerializedName("is_now")
    @Expose
    private Boolean isNow;

    @SerializedName("price")
    @Expose
    private Integer price;

    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;

    @SerializedName("inactive_message")
    @Expose
    private String inactiveMessage;

    @SerializedName("static_messages")
    @Expose
    private OrderPriorityStaticMessage staticMessage;

    public Boolean getNow() {
        return isNow;
    }

    public void setNow(Boolean now) {
        isNow = now;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getInactiveMessage() {
        return inactiveMessage;
    }

    public void setInactiveMessage(String inactiveMessage) {
        this.inactiveMessage = inactiveMessage;
    }

    public OrderPriorityStaticMessage getStaticMessage() {
        return staticMessage;
    }

    public void setStaticMessage(OrderPriorityStaticMessage staticMessage) {
        this.staticMessage = staticMessage;
    }
}
