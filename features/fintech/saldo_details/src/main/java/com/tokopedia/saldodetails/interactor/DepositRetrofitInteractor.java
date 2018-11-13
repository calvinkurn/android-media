//package com.tokopedia.saldodetails.interactor;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import com.tokopedia.saldodetails.response.model.SummaryWithdraw;
//
//import java.util.Map;
//
//public interface DepositRetrofitInteractor {
//
//    void getSummaryDeposit(@NonNull Context context, @NonNull Map<String, Object> params,
//                           @NonNull DepositListener listener);
//
//    void unsubscribe();
//
//    void setRequesting(boolean isRequesting);
//
//    boolean isRequesting();
//
//    interface DepositListener {
//
//        void onSuccess(@NonNull SummaryWithdraw data);
//
//        void onTimeout(String message);
//
//        void onError(String error);
//
//        void onNullData();
//
//        void onNoNetworkConnection();
//    }
//}
