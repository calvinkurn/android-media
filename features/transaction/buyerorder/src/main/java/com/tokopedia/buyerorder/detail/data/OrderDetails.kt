package com.tokopedia.buyerorder.detail.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.buyerorder.recharge.data.response.AdditionalTickerInfo;

import java.util.List;

/**
 * Created by baghira on 10/05/18.
 */

public class OrderDetails {
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("conditionalInfo")
    @Expose
    private ConditionalInfo conditionalInfo;
    @SerializedName("flag")
    private Flags flags;
    @SerializedName("title")
    @Expose
    private List<Title> title;
    @SerializedName("invoice")
    @Expose
    private Invoice invoice;
    @SerializedName("detail")
    @Expose
    private List<Detail> detail;
    @SerializedName("additionalInfo")
    @Expose
    private List<AdditionalInfo> additionalInfo;
    @SerializedName("additionalTickerInfo")
    @Expose
    private List<AdditionalTickerInfo> additionalTickerInfo;
    @SerializedName("pricing")
    @Expose
    private List<Pricing> pricing;

    @SerializedName("payMethod")
    @Expose
    private List<PayMethod> payMethods;

    @SerializedName("paymentData")
    @Expose
    private PaymentData paymentData;
    @SerializedName("contactUs")
    @Expose
    private ContactUs contactUs;
    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtons;
    @SerializedName("items")
    @Expose
    private List<Items> items;

    @SerializedName("helpLink")
    @Expose
    private String helpLink;

    @SerializedName("metadata")
    @Expose
    private String metadata;

    public Status status() {
        return status;
    }

    public ConditionalInfo conditionalInfo() {
        return conditionalInfo;
    }

    public List<Title> title() {
        return title;
    }

    public Invoice invoice() {
        return invoice;
    }

    public List<Detail> detail() {
        return detail;
    }

    public List<AdditionalInfo> additionalInfo() {
        return additionalInfo;
    }

    public List<AdditionalTickerInfo> getAdditionalTickerInfo() {
        return additionalTickerInfo;
    }

    public List<Pricing> pricing() { return pricing; }

    public List<PayMethod> getPayMethods() {
        return payMethods;
    }

    public PaymentData paymentData() {
        return paymentData;
    }

    public ContactUs contactUs() {
        return contactUs;
    }

    public List<ActionButton> actionButtons() {
        return actionButtons;
    }

    public List<Items> getItems() {
        return items;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public String getHelpLink() {
        return helpLink;
    }

    @NonNull
    @Override
    public String toString() {
        return "[OrderDetails:{"
                + "status="+status +","
                + "conditionalInfo="+conditionalInfo +","
                + "title="+title +","
                + "invoice="+invoice +","
                + "detail="+detail +","
                + "additionalInfo="+additionalInfo +","
                + "additionalTickerInfo="+additionalTickerInfo +","
                + "pricing="+pricing +","
                + "paymethods="+payMethods +","
                + "paymentData="+paymentData +","
                + "contactUs="+contactUs +","
                + "actionButtons="+actionButtons + ","
                + "items="+items + ","
                + "helpLink="+helpLink
                + "}]";
    }

    public String getMetadata(){ return metadata;}
}
