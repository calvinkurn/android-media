package com.tokopedia.core.product.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.apiservices.product.ProductActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.product.model.report.ReportProductPass;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ProductInfoIntentService extends IntentService {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_RESULT = "EXTRA_RESULT";


    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.tokopedia.tkpd.product.intentservice.action.FOO";
    private static final String ACTION_BAZ = "com.tokopedia.tkpd.product.intentservice.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.tokopedia.tkpd.product.intentservice.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tokopedia.tkpd.product.intentservice.extra.PARAM2";
    public static final int STATUS_SUCCESS_REPORT_PRODUCT = 789764156;
    public static final int STATUS_ERROR_REPORT_PRODUCT = 789764157;


    ResultReceiver receiver;
    ProductActService productActService;
    Bundle recentBundle;

    public ProductInfoIntentService() {
        super("ProductInfoIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ProductInfoIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startAction(Context context, Bundle bundle, ProductInfoResultReceiver receiver) {
        Intent intent = new Intent(context, ProductInfoIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
            handleReportProduct(bundle);
        }
    }

    private void handleReportProduct(Bundle bundle) {
        setRecentBundle(bundle);
        ReportProductPass pass = (ReportProductPass) bundle.get(ReportProductPass.TAG);
        Map<String, String> param = pass.getParamReport();
        param = AuthUtil.generateParams(getBaseContext(), param);
        productActService = new ProductActService();
        productActService.getApi().report(param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            sendResult(getResources().getString(R.string.msg_no_connection)
                                    , STATUS_ERROR_REPORT_PRODUCT);
                        } else if (e instanceof SocketTimeoutException) {
                            sendResult(getResources().getString(R.string.default_request_error_timeout)
                                    , STATUS_ERROR_REPORT_PRODUCT);
                        } else {
                            sendResult(getResources().getString(R.string.default_request_error_unknown)
                                    , STATUS_ERROR_REPORT_PRODUCT);
                        }
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> responseData) {
                        Log.d("reportProduct", "success " + responseData);
                        if (responseData.isSuccessful()) {

                            TkpdResponse response = responseData.body();
                            if (!response.isError()) {
                                JSONObject result = response.getJsonData();
                                if ("1".equals(result.optString("is_success"))) {
                                    sendResult(getResources().getString(R.string.toast_success_report), STATUS_SUCCESS_REPORT_PRODUCT);
                                    sendGTMSuccessReport();
                                } else {
                                    sendResult(getResources().getString(R.string.default_request_error_unknown), STATUS_ERROR_REPORT_PRODUCT);
                                }
                            } else {
                                String error = responseData.body().getErrorMessages().get(0);
                                sendResult(error, STATUS_ERROR_REPORT_PRODUCT);
                            }
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    sendResult(getResources().getString(R.string.default_request_error_unknown), STATUS_ERROR_REPORT_PRODUCT);
                                }

                                @Override
                                public void onTimeout() {
                                    sendResult(getResources().getString(R.string.default_request_error_timeout), STATUS_ERROR_REPORT_PRODUCT);
                                }

                                @Override
                                public void onServerError() {
                                    sendResult(getResources().getString(R.string.default_request_error_internal_server), STATUS_ERROR_REPORT_PRODUCT);
                                }

                                @Override
                                public void onBadRequest() {
                                    sendResult(getResources().getString(R.string.default_request_error_unknown), STATUS_ERROR_REPORT_PRODUCT);
                                }

                                @Override
                                public void onForbidden() {

                                }
                            }, responseData.code());
                        }

                    }
                });

    }

    private void sendResult(String result, int status) {
        final Bundle resultData = new Bundle();
        resultData.putString(EXTRA_RESULT, result);
        if (status == STATUS_ERROR_REPORT_PRODUCT) {
            resultData.putBundle(EXTRA_BUNDLE, recentBundle);
        }
        receiver.send(status, resultData);
    }

    public void setRecentBundle(Bundle recentBundle) {
        this.recentBundle = recentBundle;
    }

    private void sendGTMSuccessReport(){
        UnifyTracking.eventSuccessReport(getApplicationContext());
    }
}
