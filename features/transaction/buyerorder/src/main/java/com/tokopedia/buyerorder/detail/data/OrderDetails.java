package com.tokopedia.buyerorder.detail.data;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.buyerorder.list.data.ConditionalInfo;
import com.tokopedia.buyerorder.list.data.PaymentData;

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
    @SerializedName("orderToken")
    @Expose
    private OrderToken orderToken;
    @SerializedName("detail")
    @Expose
    private List<Detail> detail;
    @SerializedName("additionalInfo")
    @Expose
    private List<AdditionalInfo> additionalInfo;
    @SerializedName("additionalTickerInfo")
    @Expose
    private List<AdditionalTickerInfo> additionalTickerInfos;
    @SerializedName("ticker_info")
    @Expose
    private TickerInfo tickerInfo;
    @SerializedName("pricing")
    @Expose
    private List<Pricing> pricing;
    @SerializedName("discount")
    @Expose
    private List<Discount> discounts;

    @SerializedName("paymentMethod")
    @Expose
    private PaymentMethod paymentMethod;

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

    @SerializedName("driverDetails")
    @Expose
    private DriverDetails driverDetails;

    @SerializedName("dropShipper")
    @Expose
    private DropShipper dropShipper;

    @SerializedName("shopDetails")
    @Expose
    private ShopInfo shopInfo;

    @SerializedName("helpLink")
    @Expose
    private String helpLink;

    @SerializedName("requestCancelInfo")
    @Expose
    private RequestCancelInfo requestCancelInfo;

    @SerializedName("metadata")
    @Expose
    private String metadata;

    static final String ATTRIBUTE_BOUGHT_DATE = "Tanggal Pembelian";
    static final String ATTRIBUTE_ID = "id";

    public OrderDetails(Status status, ConditionalInfo conditionalInfo, List<Title> title, Invoice invoice, OrderToken orderToken, List<Detail> detail, List<AdditionalInfo> additionalInfo, List<Pricing> pricing, List<Discount> discounts, PaymentMethod paymentMethod, List<PayMethod> payMethods, PaymentData paymentData, ContactUs contactUs, List<ActionButton> actionButtons, List<Items> items, DriverDetails driverDetails, DropShipper dropShipper, ShopInfo shopInfo,String helpLink, RequestCancelInfo requestCancelInfo) {
        this.status = status;
        this.conditionalInfo = conditionalInfo;
        this.title = title;
        this.invoice = invoice;
        this.orderToken = orderToken;
        this.detail = detail;
        this.additionalInfo = additionalInfo;
        this.pricing = pricing;
        this.discounts = discounts;
        this.paymentMethod = paymentMethod;
        this.payMethods = payMethods;
        this.paymentData = paymentData;
        this.contactUs = contactUs;
        this.actionButtons = actionButtons;
        this.items = items;
        this.driverDetails = driverDetails;
        this.dropShipper = dropShipper;
        this.shopInfo = shopInfo;
        this.helpLink = helpLink;
        this.requestCancelInfo = requestCancelInfo;
    }

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

    public OrderToken orderToken() {
        return orderToken;
    }

    public List<Detail> detail() {
        return detail;
    }

    public List<AdditionalInfo> additionalInfo() {
        return additionalInfo;
    }

    public List<AdditionalTickerInfo> getAdditionalTickerInfos() {
        return additionalTickerInfos;
    }

    public TickerInfo getTickerInfo() { return tickerInfo; }

    public List<Pricing> pricing() { return pricing; }

    public List<Discount> discounts() { return discounts; }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

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

    public DriverDetails getDriverDetails() {
        return driverDetails;
    }

    public DropShipper getDropShipper() {
        return dropShipper;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
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

    public RequestCancelInfo getRequestCancelInfo() {
        return requestCancelInfo;
    }
    @Override
    public String toString() {
        return "[OrderDetails:{"
                + "status="+status +","
                + "requestCancelInfo="+requestCancelInfo +","
                + "conditionalInfo="+conditionalInfo +","
                + "title="+title +","
                + "invoice="+invoice +","
                + "orderToken="+orderToken +","
                + "detail="+detail +","
                + "additionalInfo="+additionalInfo +","
                + "additionalTickerInfo="+additionalTickerInfos +","
                + "tickerInfo="+tickerInfo +","
                + "pricing="+pricing +","
                + "discounts="+discounts +","
                + "paymentMethod="+paymentMethod +","
                + "paymethods="+payMethods +","
                + "paymentData="+paymentData +","
                + "contactUs="+contactUs +","
                + "actionButtons="+actionButtons + ","
                + "items="+items + ","
                + "driverDetails="+driverDetails +","
                + "dropShipper="+dropShipper + ","
                + "shopInfo="+shopInfo + ","
                + "helpLink="+helpLink
                + "}]";
    }

    public String getStatusId() {
        return status.status();
    }

    public String getStatusInfo() {
        return status.statusText();
    }

    public String getTotalPriceAmount() {
        return paymentData.value();
    }

    public String getBoughtDate() {
        String date = "";
        for (Title ttl : title) {
            if (ttl.label().equals(ATTRIBUTE_BOUGHT_DATE)) {
                date = ttl.value();
            }
        }

        if (!date.isEmpty()) {
            date = stripHourFromDate(date);
        }

        return date;
    }

    private String stripHourFromDate(String date) {
        String strippedDate = date;
        if (strippedDate.length() >= 11) {
            strippedDate = strippedDate.substring(0, 11);
        }

        return strippedDate;
    }

    public String getInvoiceId() {
        String invoiceUrl = getInvoiceUrl();
        Uri invoiceUri = Uri.parse(invoiceUrl);

        return invoiceUri.getQueryParameter(ATTRIBUTE_ID);
    }

    public String getProductImageUrl() {
        String productImageUrl = "";

        if (!items.isEmpty()) {
            productImageUrl = items.get(0).getImageUrl();
        }

        return productImageUrl;
    }

    public String getProductName() {
        String productName = "";

        if (!items.isEmpty()) {
            productName = items.get(0).getTitle();
        }

        return productName;
    }

    public String getInvoiceCode() {
        return invoice.invoiceRefNum();
    }

    public String getInvoiceUrl() {
        return invoice.invoiceUrl();
    }

    public String getMetadata(){ return metadata;}
}
