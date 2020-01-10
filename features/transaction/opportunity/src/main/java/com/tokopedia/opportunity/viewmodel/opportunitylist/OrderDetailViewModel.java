package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderDetailViewModel implements Parcelable{

    private String detailInsurancePrice;
    private String detailOpenAmount;
    private String detailDropshipName;
    private String detailTotalAddFee;
    private String detailPartialOrder;
    private int detailQuantity;
    private String detailProductPriceIdr;
    private String detailInvoice;
    private String detailShippingPriceIdr;
    private String detailFreeReturn;
    private String detailPdfPath;
    private String detailFreeReturnMsg;
    private String detailAdditionalFeeIdr;
    private String detailProductPrice;
    private DetailPreorderViewModel detailPreorder;
    private DetailCancelRequestViewModel detailCancelRequest;
    private String detailForceInsurance;
    private String detailOpenAmountIdr;
    private String detailAdditionalFee;
    private String detailDropshipTelp;
    private int detailOrderId;
    private String detailTotalAddFeeIdr;
    private String detailOrderDate;
    private String detailShippingPrice;
    private String detailPayDueDate;
    private String detailTotalWeight;
    private String detailInsurancePriceIdr;
    private String detailPdfUri;
    private String detailShipRefNum;
    private String detailPrintAddressUri;
    private String detailPdf;
    private int detailOrderStatus;

    public OrderDetailViewModel() {
    }

    protected OrderDetailViewModel(Parcel in) {
        detailInsurancePrice = in.readString();
        detailOpenAmount = in.readString();
        detailDropshipName = in.readString();
        detailTotalAddFee = in.readString();
        detailPartialOrder = in.readString();
        detailQuantity = in.readInt();
        detailProductPriceIdr = in.readString();
        detailInvoice = in.readString();
        detailShippingPriceIdr = in.readString();
        detailFreeReturn = in.readString();
        detailPdfPath = in.readString();
        detailFreeReturnMsg = in.readString();
        detailAdditionalFeeIdr = in.readString();
        detailProductPrice = in.readString();
        detailPreorder = in.readParcelable(DetailPreorderViewModel.class.getClassLoader());
        detailCancelRequest = in.readParcelable(DetailCancelRequestViewModel.class.getClassLoader());
        detailForceInsurance = in.readString();
        detailOpenAmountIdr = in.readString();
        detailAdditionalFee = in.readString();
        detailDropshipTelp = in.readString();
        detailOrderId = in.readInt();
        detailTotalAddFeeIdr = in.readString();
        detailOrderDate = in.readString();
        detailShippingPrice = in.readString();
        detailPayDueDate = in.readString();
        detailTotalWeight = in.readString();
        detailInsurancePriceIdr = in.readString();
        detailPdfUri = in.readString();
        detailShipRefNum = in.readString();
        detailPrintAddressUri = in.readString();
        detailPdf = in.readString();
        detailOrderStatus = in.readInt();
    }

    public static final Creator<OrderDetailViewModel> CREATOR = new Creator<OrderDetailViewModel>() {
        @Override
        public OrderDetailViewModel createFromParcel(Parcel in) {
            return new OrderDetailViewModel(in);
        }

        @Override
        public OrderDetailViewModel[] newArray(int size) {
            return new OrderDetailViewModel[size];
        }
    };

    public String getDetailInsurancePrice() {
        return detailInsurancePrice;
    }

    public void setDetailInsurancePrice(String detailInsurancePrice) {
        this.detailInsurancePrice = detailInsurancePrice;
    }

    public String getDetailOpenAmount() {
        return detailOpenAmount;
    }

    public void setDetailOpenAmount(String detailOpenAmount) {
        this.detailOpenAmount = detailOpenAmount;
    }

    public String getDetailDropshipName() {
        return detailDropshipName;
    }

    public void setDetailDropshipName(String detailDropshipName) {
        this.detailDropshipName = detailDropshipName;
    }

    public String getDetailTotalAddFee() {
        return detailTotalAddFee;
    }

    public void setDetailTotalAddFee(String detailTotalAddFee) {
        this.detailTotalAddFee = detailTotalAddFee;
    }

    public String getDetailPartialOrder() {
        return detailPartialOrder;
    }

    public void setDetailPartialOrder(String detailPartialOrder) {
        this.detailPartialOrder = detailPartialOrder;
    }

    public int getDetailQuantity() {
        return detailQuantity;
    }

    public void setDetailQuantity(int detailQuantity) {
        this.detailQuantity = detailQuantity;
    }

    public String getDetailProductPriceIdr() {
        return detailProductPriceIdr;
    }

    public void setDetailProductPriceIdr(String detailProductPriceIdr) {
        this.detailProductPriceIdr = detailProductPriceIdr;
    }

    public String getDetailInvoice() {
        return detailInvoice;
    }

    public void setDetailInvoice(String detailInvoice) {
        this.detailInvoice = detailInvoice;
    }

    public String getDetailShippingPriceIdr() {
        return detailShippingPriceIdr;
    }

    public void setDetailShippingPriceIdr(String detailShippingPriceIdr) {
        this.detailShippingPriceIdr = detailShippingPriceIdr;
    }

    public String getDetailFreeReturn() {
        return detailFreeReturn;
    }

    public void setDetailFreeReturn(String detailFreeReturn) {
        this.detailFreeReturn = detailFreeReturn;
    }

    public String getDetailPdfPath() {
        return detailPdfPath;
    }

    public void setDetailPdfPath(String detailPdfPath) {
        this.detailPdfPath = detailPdfPath;
    }

    public String getDetailFreeReturnMsg() {
        return detailFreeReturnMsg;
    }

    public void setDetailFreeReturnMsg(String detailFreeReturnMsg) {
        this.detailFreeReturnMsg = detailFreeReturnMsg;
    }

    public String getDetailAdditionalFeeIdr() {
        return detailAdditionalFeeIdr;
    }

    public void setDetailAdditionalFeeIdr(String detailAdditionalFeeIdr) {
        this.detailAdditionalFeeIdr = detailAdditionalFeeIdr;
    }

    public String getDetailProductPrice() {
        return detailProductPrice;
    }

    public void setDetailProductPrice(String detailProductPrice) {
        this.detailProductPrice = detailProductPrice;
    }

    public DetailPreorderViewModel getDetailPreorder() {
        return detailPreorder;
    }

    public void setDetailPreorder(DetailPreorderViewModel detailPreorder) {
        this.detailPreorder = detailPreorder;
    }

    public DetailCancelRequestViewModel getDetailCancelRequest() {
        return detailCancelRequest;
    }

    public void setDetailCancelRequest(DetailCancelRequestViewModel detailCancelRequest) {
        this.detailCancelRequest = detailCancelRequest;
    }

    public String getDetailForceInsurance() {
        return detailForceInsurance;
    }

    public void setDetailForceInsurance(String detailForceInsurance) {
        this.detailForceInsurance = detailForceInsurance;
    }

    public String getDetailOpenAmountIdr() {
        return detailOpenAmountIdr;
    }

    public void setDetailOpenAmountIdr(String detailOpenAmountIdr) {
        this.detailOpenAmountIdr = detailOpenAmountIdr;
    }

    public String getDetailAdditionalFee() {
        return detailAdditionalFee;
    }

    public void setDetailAdditionalFee(String detailAdditionalFee) {
        this.detailAdditionalFee = detailAdditionalFee;
    }

    public String getDetailDropshipTelp() {
        return detailDropshipTelp;
    }

    public void setDetailDropshipTelp(String detailDropshipTelp) {
        this.detailDropshipTelp = detailDropshipTelp;
    }

    public int getDetailOrderId() {
        return detailOrderId;
    }

    public void setDetailOrderId(int detailOrderId) {
        this.detailOrderId = detailOrderId;
    }

    public String getDetailTotalAddFeeIdr() {
        return detailTotalAddFeeIdr;
    }

    public void setDetailTotalAddFeeIdr(String detailTotalAddFeeIdr) {
        this.detailTotalAddFeeIdr = detailTotalAddFeeIdr;
    }

    public String getDetailOrderDate() {
        return detailOrderDate;
    }

    public void setDetailOrderDate(String detailOrderDate) {
        this.detailOrderDate = detailOrderDate;
    }

    public String getDetailShippingPrice() {
        return detailShippingPrice;
    }

    public void setDetailShippingPrice(String detailShippingPrice) {
        this.detailShippingPrice = detailShippingPrice;
    }

    public String getDetailPayDueDate() {
        return detailPayDueDate;
    }

    public void setDetailPayDueDate(String detailPayDueDate) {
        this.detailPayDueDate = detailPayDueDate;
    }

    public String getDetailTotalWeight() {
        return detailTotalWeight;
    }

    public void setDetailTotalWeight(String detailTotalWeight) {
        this.detailTotalWeight = detailTotalWeight;
    }

    public String getDetailInsurancePriceIdr() {
        return detailInsurancePriceIdr;
    }

    public void setDetailInsurancePriceIdr(String detailInsurancePriceIdr) {
        this.detailInsurancePriceIdr = detailInsurancePriceIdr;
    }

    public String getDetailPdfUri() {
        return detailPdfUri;
    }

    public void setDetailPdfUri(String detailPdfUri) {
        this.detailPdfUri = detailPdfUri;
    }

    public String getDetailShipRefNum() {
        return detailShipRefNum;
    }

    public void setDetailShipRefNum(String detailShipRefNum) {
        this.detailShipRefNum = detailShipRefNum;
    }

    public String getDetailPrintAddressUri() {
        return detailPrintAddressUri;
    }

    public void setDetailPrintAddressUri(String detailPrintAddressUri) {
        this.detailPrintAddressUri = detailPrintAddressUri;
    }

    public String getDetailPdf() {
        return detailPdf;
    }

    public void setDetailPdf(String detailPdf) {
        this.detailPdf = detailPdf;
    }

    public int getDetailOrderStatus() {
        return detailOrderStatus;
    }

    public void setDetailOrderStatus(int detailOrderStatus) {
        this.detailOrderStatus = detailOrderStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(detailInsurancePrice);
        dest.writeString(detailOpenAmount);
        dest.writeString(detailDropshipName);
        dest.writeString(detailTotalAddFee);
        dest.writeString(detailPartialOrder);
        dest.writeInt(detailQuantity);
        dest.writeString(detailProductPriceIdr);
        dest.writeString(detailInvoice);
        dest.writeString(detailShippingPriceIdr);
        dest.writeString(detailFreeReturn);
        dest.writeString(detailPdfPath);
        dest.writeString(detailFreeReturnMsg);
        dest.writeString(detailAdditionalFeeIdr);
        dest.writeString(detailProductPrice);
        dest.writeParcelable(detailPreorder, flags);
        dest.writeParcelable(detailCancelRequest, flags);
        dest.writeString(detailForceInsurance);
        dest.writeString(detailOpenAmountIdr);
        dest.writeString(detailAdditionalFee);
        dest.writeString(detailDropshipTelp);
        dest.writeInt(detailOrderId);
        dest.writeString(detailTotalAddFeeIdr);
        dest.writeString(detailOrderDate);
        dest.writeString(detailShippingPrice);
        dest.writeString(detailPayDueDate);
        dest.writeString(detailTotalWeight);
        dest.writeString(detailInsurancePriceIdr);
        dest.writeString(detailPdfUri);
        dest.writeString(detailShipRefNum);
        dest.writeString(detailPrintAddressUri);
        dest.writeString(detailPdf);
        dest.writeInt(detailOrderStatus);
    }
}
