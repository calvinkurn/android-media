//package com.tokopedia.core.payment.model.responsedynamicpayment;
//
//import android.net.Uri;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
///**
// * Created by Angga.Prasetiyo on 19/05/2016.
// */
//public class DynamicPaymentData implements Parcelable {
//    private static final String TAG = DynamicPaymentData.class.getSimpleName();
//
//    @SerializedName("query_string")
//    @Expose
//    private String queryString;
//    @SerializedName("redirect_url")
//    @Expose
//    private String redirectUrl;
//    @SerializedName("parameter")
//    @Expose
//    private Parameter parameter;
//    @SerializedName("callback_url")
//    @Expose
//    private String callbackUrl;
//
//    public String getQueryString() {
//        return queryString;
//    }
//
//    public void setQueryString(String queryString) {
//        this.queryString = queryString;
//    }
//
//    public String getRedirectUrl() {
//        return redirectUrl;
//    }
//
//    public void setRedirectUrl(String redirectUrl) {
//        this.redirectUrl = redirectUrl;
//    }
//
//    public Parameter getParameter() {
//        return parameter;
//    }
//
//    public void setParameter(Parameter parameter) {
//        this.parameter = parameter;
//    }
//
//    public String getCallbackUrl() {
//        return callbackUrl;
//    }
//
//    public void setCallbackUrl(String callbackUrl) {
//        this.callbackUrl = callbackUrl;
//    }
//
//    public String getCallbackUrlPath() {
//        try {
//            Uri uri = Uri.parse(callbackUrl);
//            return uri.getPath();
//        } catch (Exception e) {
//            return "wrong";
//        }
//    }
//
//    protected DynamicPaymentData(Parcel in) {
//        queryString = in.readString();
//        redirectUrl = in.readString();
//        parameter = (Parameter) in.readValue(Parameter.class.getClassLoader());
//        callbackUrl = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(queryString);
//        dest.writeString(redirectUrl);
//        dest.writeValue(parameter);
//        dest.writeString(callbackUrl);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<DynamicPaymentData> CREATOR =
//            new Parcelable.Creator<DynamicPaymentData>() {
//                @Override
//                public DynamicPaymentData createFromParcel(Parcel in) {
//                    return new DynamicPaymentData(in);
//                }
//
//                @Override
//                public DynamicPaymentData[] newArray(int size) {
//                    return new DynamicPaymentData[size];
//                }
//            };
//}
