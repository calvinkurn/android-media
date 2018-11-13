//package com.tokopedia.saldodetails.interactor;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.google.gson.reflect.TypeToken;
//import com.tokopedia.abstraction.common.data.model.response.DataResponse;
//import com.tokopedia.abstraction.common.utils.MapNulRemover;
//import com.tokopedia.common.network.data.model.RestResponse;
//import com.tokopedia.saldodetails.response.model.SummaryWithdraw;
//import com.tokopedia.saldodetails.usecase.GetSaldoSummaryUseCase;
//import com.tokopedia.usecase.RequestParams;
//import com.tokopedia.user.session.UserSession;
//
//import java.lang.reflect.Type;
//import java.net.SocketTimeoutException;
//import java.net.UnknownHostException;
//import java.util.Date;
//import java.util.Map;
//
//import javax.inject.Inject;
//
//import rx.Subscriber;
//
//import static com.tokopedia.abstraction.common.utils.network.AuthUtil.md5;
//
//public class DepositRetrofitInteractorImpl implements DepositRetrofitInteractor {
//    private static final String TAG = DepositRetrofitInteractorImpl.class.getSimpleName();
//
//    private static final String PARAM_USER_ID = "user_id";
//    private static final String PARAM_DEVICE_ID = "device_id";
//    private static final String PARAM_HASH = "hash";
//    private static final String PARAM_OS_TYPE = "os_type";
//    private static final String PARAM_TIMESTAMP = "device_time";
//    private static final String PARAM_X_TKPD_USER_ID = "x-tkpd-userid";
//    private static final String PARAM_BEARER = "Bearer ";
////    private UserSession userSession;
//
//    public UserSession userSession;
//
//    private boolean isRequesting = false;
//    private GetSaldoSummaryUseCase getSaldoSummaryUseCase;
//
//    public DepositRetrofitInteractorImpl(Context context,
//                                         GetSaldoSummaryUseCase getSaldoSummaryUseCase) {
//        this.getSaldoSummaryUseCase = getSaldoSummaryUseCase;
//        userSession = new UserSession(context);
////        userSession = ((SaldoDetailsRouter) context.getApplicationContext()).getSession();
//    }
//
//    @Override
//    public void getSummaryDeposit(@NonNull final Context context, @NonNull Map<String, Object> params, @NonNull final DepositListener listener) {
//        setRequesting(true);
//
//        RequestParams requestParams = RequestParams.create();
//
//        params = MapNulRemover.removeNullFromObjectMap(params);
//
//        String deviceId = userSession.getDeviceId();
//        String userId = userSession.getUserId();
//        String hash = md5(userId + "~" + deviceId);
//
//        params.put(PARAM_USER_ID, userId);
//        params.put(PARAM_DEVICE_ID, deviceId);
//        params.put(PARAM_HASH, hash);
//        params.put(PARAM_OS_TYPE, "1");
//        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
//
//        requestParams.putAll(params);
//        getSaldoSummaryUseCase.setRequestParams(requestParams);
//
//        getSaldoSummaryUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
//            @Override
//            public void onCompleted() {
//                setRequesting(false);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG, e.toString());
//
//                if (e instanceof UnknownHostException) {
//                    listener.onNoNetworkConnection();
//                } else if (e instanceof SocketTimeoutException) {
//                    listener.onTimeout("Timeout connection," +
//                            " Mohon ulangi beberapa saat lagi");
//                } else {
//                    listener.onError("Terjadi Kesalahan, " +
//                            "Mohon ulangi beberapa saat lagi");
//                }
//            }
//
//            @Override
//            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
//
//                Type token = new TypeToken<DataResponse<SummaryWithdraw>>() {
//                }.getType();
//                RestResponse restResponse = typeRestResponseMap.get(token);
//                DataResponse dataResponse = restResponse.getData();
//                SummaryWithdraw summaryWithdraw = (SummaryWithdraw) dataResponse.getData();
//
//                if (!restResponse.isError()) {
//                    listener.onSuccess(summaryWithdraw);
//                } else {
//                    listener.onError(restResponse.getErrorBody());
//                }
//            }
//        });
//    }
//
//    @Override
//    public void unsubscribe() {
//        if (getSaldoSummaryUseCase != null) {
//            getSaldoSummaryUseCase.unsubscribe();
//        }
//    }
//
//    @Override
//    public void setRequesting(boolean isRequesting) {
//        this.isRequesting = isRequesting;
//    }
//
//    @Override
//    public boolean isRequesting() {
//        return isRequesting;
//    }
//}
