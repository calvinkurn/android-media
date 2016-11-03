package com.tokopedia.tkpd.rescenter.edit.model.passdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created on 8/31/16.
 */
public class AppealResCenterFormData implements Parcelable {

    @SerializedName("form")
    private Form form;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public static class Form implements Parcelable {

        @SerializedName("resolution_last")
        private ResolutionLast resolutionLast;
        @SerializedName("resolution_order")
        private ResolutionOrder resolutionOrder;
        @SerializedName("resolution_solution_list")
        private List<EditResCenterFormData.SolutionData> resolutionSolutionList;

        public ResolutionLast getResolutionLast() {
            return resolutionLast;
        }

        public void setResolutionLast(ResolutionLast resolutionLast) {
            this.resolutionLast = resolutionLast;
        }

        public ResolutionOrder getResolutionOrder() {
            return resolutionOrder;
        }

        public void setResolutionOrder(ResolutionOrder resolutionOrder) {
            this.resolutionOrder = resolutionOrder;
        }

        public List<EditResCenterFormData.SolutionData> getResolutionSolutionList() {
            return resolutionSolutionList;
        }

        public void setResolutionSolutionList(List<EditResCenterFormData.SolutionData> resolutionSolutionList) {
            this.resolutionSolutionList = resolutionSolutionList;
        }

        public Form() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.resolutionLast, flags);
            dest.writeParcelable(this.resolutionOrder, flags);
            dest.writeTypedList(this.resolutionSolutionList);
        }

        protected Form(Parcel in) {
            this.resolutionLast = in.readParcelable(ResolutionLast.class.getClassLoader());
            this.resolutionOrder = in.readParcelable(ResolutionOrder.class.getClassLoader());
            this.resolutionSolutionList = in.createTypedArrayList(EditResCenterFormData.SolutionData.CREATOR);
        }

        public static final Creator<Form> CREATOR = new Creator<Form>() {
            @Override
            public Form createFromParcel(Parcel source) {
                return new Form(source);
            }

            @Override
            public Form[] newArray(int size) {
                return new Form[size];
            }
        };
    }

    public static class ResolutionLast implements Parcelable {
        @SerializedName("last_trouble_string")
        private String lastTroubleString;
        @SerializedName("last_resolution_id")
        private Integer lastResolutionId;
        @SerializedName("last_refund_amt")
        private Integer lastRefundAmt;
        @SerializedName("last_solution")
        private Integer lastSolution;
        @SerializedName("last_product_related")
        private Integer lastProductRelated;
        @SerializedName("last_action_by")
        private Integer lastActionBy;
        @SerializedName("last_refund_amt_idr")
        private String lastRefundAmtIdr;
        @SerializedName("last_rival_accepted")
        private Integer lastRivalAccepted;
        @SerializedName("last_flag_received")
        private Integer lastFlagReceived;
        @SerializedName("last_solution_string")
        private String lastSolutionString;
        @SerializedName("last_category_trouble_type")
        private Integer lastCategoryTroubleType;
        @SerializedName("last_category_trouble_string")
        private String lastCategoryTroubleString;
        @SerializedName("last_solution_remark")
        private String lastSolutionRemark;

        public String getLastTroubleString() {
            return lastTroubleString;
        }

        public void setLastTroubleString(String lastTroubleString) {
            this.lastTroubleString = lastTroubleString;
        }

        public Integer getLastResolutionId() {
            return lastResolutionId;
        }

        public void setLastResolutionId(Integer lastResolutionId) {
            this.lastResolutionId = lastResolutionId;
        }

        public Integer getLastRefundAmt() {
            return lastRefundAmt;
        }

        public void setLastRefundAmt(Integer lastRefundAmt) {
            this.lastRefundAmt = lastRefundAmt;
        }

        public Integer getLastSolution() {
            return lastSolution;
        }

        public void setLastSolution(Integer lastSolution) {
            this.lastSolution = lastSolution;
        }

        public Integer getLastProductRelated() {
            return lastProductRelated;
        }

        public void setLastProductRelated(Integer lastProductRelated) {
            this.lastProductRelated = lastProductRelated;
        }

        public Integer getLastActionBy() {
            return lastActionBy;
        }

        public void setLastActionBy(Integer lastActionBy) {
            this.lastActionBy = lastActionBy;
        }

        public String getLastRefundAmtIdr() {
            return lastRefundAmtIdr;
        }

        public void setLastRefundAmtIdr(String lastRefundAmtIdr) {
            this.lastRefundAmtIdr = lastRefundAmtIdr;
        }

        public Integer getLastRivalAccepted() {
            return lastRivalAccepted;
        }

        public void setLastRivalAccepted(Integer lastRivalAccepted) {
            this.lastRivalAccepted = lastRivalAccepted;
        }

        public Integer getLastFlagReceived() {
            return lastFlagReceived;
        }

        public void setLastFlagReceived(Integer lastFlagReceived) {
            this.lastFlagReceived = lastFlagReceived;
        }

        public String getLastSolutionString() {
            return lastSolutionString;
        }

        public void setLastSolutionString(String lastSolutionString) {
            this.lastSolutionString = lastSolutionString;
        }

        public String getLastCategoryTroubleString() {
            return lastCategoryTroubleString;
        }

        public void setLastCategoryTroubleString(String lastCategoryTroubleString) {
            this.lastCategoryTroubleString = lastCategoryTroubleString;
        }

        public Integer getLastCategoryTroubleType() {
            return lastCategoryTroubleType;
        }

        public void setLastCategoryTroubleType(Integer lastCategoryTroubleType) {
            this.lastCategoryTroubleType = lastCategoryTroubleType;
        }

        public ResolutionLast() {
        }

        public void setLastSolutionRemark(String lastSolutionRemark) {
            this.lastSolutionRemark = lastSolutionRemark;
        }

        public String getLastSolutionRemark() {
            return lastSolutionRemark;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.lastTroubleString);
            dest.writeValue(this.lastResolutionId);
            dest.writeValue(this.lastRefundAmt);
            dest.writeValue(this.lastSolution);
            dest.writeValue(this.lastProductRelated);
            dest.writeValue(this.lastActionBy);
            dest.writeString(this.lastRefundAmtIdr);
            dest.writeValue(this.lastRivalAccepted);
            dest.writeValue(this.lastFlagReceived);
            dest.writeString(this.lastSolutionString);
            dest.writeValue(this.lastCategoryTroubleType);
            dest.writeString(this.lastCategoryTroubleString);
            dest.writeString(this.lastSolutionRemark);
        }

        protected ResolutionLast(Parcel in) {
            this.lastTroubleString = in.readString();
            this.lastResolutionId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastRefundAmt = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastSolution = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastProductRelated = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastActionBy = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastRefundAmtIdr = in.readString();
            this.lastRivalAccepted = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastFlagReceived = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastSolutionString = in.readString();
            this.lastCategoryTroubleType = (Integer) in.readValue(Integer.class.getClassLoader());
            this.lastCategoryTroubleString = in.readString();
            this.lastSolutionRemark = in.readString();
        }

        public static final Creator<ResolutionLast> CREATOR = new Creator<ResolutionLast>() {
            @Override
            public ResolutionLast createFromParcel(Parcel source) {
                return new ResolutionLast(source);
            }

            @Override
            public ResolutionLast[] newArray(int size) {
                return new ResolutionLast[size];
            }
        };
    }

    public static class ResolutionOrder implements Parcelable {
        @SerializedName("order_shipping_fee_idr")
        private String orderShippingFeeIdr;
        @SerializedName("order_shop_url")
        private String orderShopUrl;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("order_open_amount")
        private Integer orderOpenAmount;
        @SerializedName("order_pdf_url")
        private String orderPdfUrl;
        @SerializedName("order_shipping_fee")
        private Integer orderShippingFee;
        @SerializedName("order_open_amount_idr")
        private String orderOpenAmountIdr;
        @SerializedName("order_product_fee")
        private Integer orderProductFee;
        @SerializedName("order_shop_name")
        private String orderShopName;
        @SerializedName("order_is_customer")
        private Integer orderIsCustomer;
        @SerializedName("order_product_fee_idr")
        private String orderProductFeeIdr;
        @SerializedName("order_free_return")
        private Integer orderFreeReturn;
        @SerializedName("order_invoice_ref_num")
        private String orderInvoiceRefNum;

        public String getOrderShippingFeeIdr() {
            return orderShippingFeeIdr;
        }

        public void setOrderShippingFeeIdr(String orderShippingFeeIdr) {
            this.orderShippingFeeIdr = orderShippingFeeIdr;
        }

        public String getOrderShopUrl() {
            return orderShopUrl;
        }

        public void setOrderShopUrl(String orderShopUrl) {
            this.orderShopUrl = orderShopUrl;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getOrderOpenAmount() {
            return orderOpenAmount;
        }

        public void setOrderOpenAmount(Integer orderOpenAmount) {
            this.orderOpenAmount = orderOpenAmount;
        }

        public String getOrderPdfUrl() {
            return orderPdfUrl;
        }

        public void setOrderPdfUrl(String orderPdfUrl) {
            this.orderPdfUrl = orderPdfUrl;
        }

        public Integer getOrderShippingFee() {
            return orderShippingFee;
        }

        public void setOrderShippingFee(Integer orderShippingFee) {
            this.orderShippingFee = orderShippingFee;
        }

        public String getOrderOpenAmountIdr() {
            return orderOpenAmountIdr;
        }

        public void setOrderOpenAmountIdr(String orderOpenAmountIdr) {
            this.orderOpenAmountIdr = orderOpenAmountIdr;
        }

        public Integer getOrderProductFee() {
            return orderProductFee;
        }

        public void setOrderProductFee(Integer orderProductFee) {
            this.orderProductFee = orderProductFee;
        }

        public String getOrderShopName() {
            return orderShopName;
        }

        public void setOrderShopName(String orderShopName) {
            this.orderShopName = orderShopName;
        }

        public Integer getOrderIsCustomer() {
            return orderIsCustomer;
        }

        public void setOrderIsCustomer(Integer orderIsCustomer) {
            this.orderIsCustomer = orderIsCustomer;
        }

        public String getOrderProductFeeIdr() {
            return orderProductFeeIdr;
        }

        public void setOrderProductFeeIdr(String orderProductFeeIdr) {
            this.orderProductFeeIdr = orderProductFeeIdr;
        }

        public Integer getOrderFreeReturn() {
            return orderFreeReturn;
        }

        public void setOrderFreeReturn(Integer orderFreeReturn) {
            this.orderFreeReturn = orderFreeReturn;
        }

        public String getOrderInvoiceRefNum() {
            return orderInvoiceRefNum;
        }

        public void setOrderInvoiceRefNum(String orderInvoiceRefNum) {
            this.orderInvoiceRefNum = orderInvoiceRefNum;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.orderShippingFeeIdr);
            dest.writeString(this.orderShopUrl);
            dest.writeString(this.orderId);
            dest.writeValue(this.orderOpenAmount);
            dest.writeString(this.orderPdfUrl);
            dest.writeValue(this.orderShippingFee);
            dest.writeString(this.orderOpenAmountIdr);
            dest.writeValue(this.orderProductFee);
            dest.writeString(this.orderShopName);
            dest.writeValue(this.orderIsCustomer);
            dest.writeString(this.orderProductFeeIdr);
            dest.writeValue(this.orderFreeReturn);
            dest.writeString(this.orderInvoiceRefNum);
        }

        public ResolutionOrder() {
        }

        protected ResolutionOrder(Parcel in) {
            this.orderShippingFeeIdr = in.readString();
            this.orderShopUrl = in.readString();
            this.orderId = in.readString();
            this.orderOpenAmount = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderPdfUrl = in.readString();
            this.orderShippingFee = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderOpenAmountIdr = in.readString();
            this.orderProductFee = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderShopName = in.readString();
            this.orderIsCustomer = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderProductFeeIdr = in.readString();
            this.orderFreeReturn = (Integer) in.readValue(Integer.class.getClassLoader());
            this.orderInvoiceRefNum = in.readString();
        }

        public static final Creator<ResolutionOrder> CREATOR = new Creator<ResolutionOrder>() {
            @Override
            public ResolutionOrder createFromParcel(Parcel source) {
                return new ResolutionOrder(source);
            }

            @Override
            public ResolutionOrder[] newArray(int size) {
                return new ResolutionOrder[size];
            }
        };
    }

    public AppealResCenterFormData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.form, flags);
    }

    protected AppealResCenterFormData(Parcel in) {
        this.form = in.readParcelable(Form.class.getClassLoader());
    }

    public static final Creator<AppealResCenterFormData> CREATOR = new Creator<AppealResCenterFormData>() {
        @Override
        public AppealResCenterFormData createFromParcel(Parcel source) {
            return new AppealResCenterFormData(source);
        }

        @Override
        public AppealResCenterFormData[] newArray(int size) {
            return new AppealResCenterFormData[size];
        }
    };
}
