//
//package com.tokopedia.core.cart.model;
//
//import android.content.Context;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.tokopedia.core.R;
//
//public class GatewayList {
//
//    @SerializedName("toppay_flag")
//    @Expose
//    private String toppayFlag;
//    @SerializedName("gateway")
//    @Expose
//    private Integer gateway;
//    @SerializedName("gateway_image")
//    @Expose
//    private String gatewayImage;
//    @SerializedName("gateway_name")
//    @Expose
//    private String gatewayName;
//    @SerializedName("gateway_desc")
//    @Expose
//    private String gatewayDesc;
//
//    public String getToppayFlag() {
//        return toppayFlag;
//    }
//
//    public void setToppayFlag(String toppayFlag) {
//        this.toppayFlag = toppayFlag;
//    }
//
//    public Integer getGateway() {
//        return gateway;
//    }
//
//    public void setGateway(Integer gateway) {
//        this.gateway = gateway;
//    }
//
//    public String getGatewayImage() {
//        return gatewayImage;
//    }
//
//    public void setGatewayImage(String gatewayImage) {
//        this.gatewayImage = gatewayImage;
//    }
//
//    public String getGatewayName() {
//        return gatewayName;
//    }
//
//    public void setGatewayName(String gatewayName) {
//        this.gatewayName = gatewayName;
//    }
//
//    public String getFeeInformation(Context context) {
//        if (this.gatewayDesc != null) return gatewayDesc;
//        switch (gateway) {
//            case 10:
//                return context.getString(R.string.msg_tokopedia_indomaret_fee);
//            case 12:
//                return context.getString(R.string.installment_payment_label);
//            case 8:
//                return context.getString(R.string.msg_tokopedia_cc_fee);
//            default:
//                return context.getString(R.string.msg_tokopedia_free);
//        }
//    }
//}
