package viewmodel;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.laku6.tradeinsdk.api.Laku6TradeIn;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tradein.R;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DeviceAttr;
import model.DeviceDiagInput;
import model.DeviceDiagInputResponse;
import model.DeviceDiagnostics;
import model.TradeInParams;
import rx.Subscriber;
import tradein_common.Constants;
import view.viewcontrollers.FinalPriceActivity;

public class TradeInHomeViewModel extends ViewModel implements LifecycleObserver, Laku6TradeIn.TradeInListener {
    private MutableLiveData<Integer> insertResultData;
    private MutableLiveData<JSONObject> minPriceData;
    private MutableLiveData<JSONObject> priceFailData;

    private WeakReference<FragmentActivity> activityWeakReference;
    private TradeInParams inData;
    private Laku6TradeIn laku6TradeIn;
    private Laku6GTMReceiver laku6GTMReceiver;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;


    private class Laku6GTMReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "laku6-gtm".equals(intent.getAction())) {
                String page = intent.getStringExtra("page");
                String action = intent.getStringExtra("action");
                String value = intent.getStringExtra("value");
                if ("cek fisik".equals(page)) {
                    if ("click salin".equals(action) || "click social share".equals(action))
                        sendGeneralEvent("clickTradeIn", "cek fisik trade in", action, value);
                } else if ("cek fungsi trade in".equals(page)) {
                    sendGeneralEvent("clickTradeIn", "cek fungsi trade in", action, value);
                } else if ("cek fisik result trade in".equals(page)) {
                    sendGeneralEvent("viewTradeIn", "cek fisik result trade in", action, value);
                }
            }
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("test-result");
            DeviceDiagnostics diagnostics = new Gson().fromJson(result, DeviceDiagnostics.class);
            Map<String, Object> variables = new HashMap<>();
            try {
                DeviceDiagInput deviceDiagInput = new DeviceDiagInput();
                deviceDiagInput.setUniqueCode(diagnostics.getTradeInUniqueCode());
                DeviceAttr deviceAttr = new DeviceAttr();
                deviceAttr.setBrand(diagnostics.getBrand());
                deviceAttr.setGrade(diagnostics.getGrade());
                List<String> imei = new ArrayList<>();
                imei.add(diagnostics.getImei());
                deviceAttr.setImei(imei);
                deviceAttr.setModel(diagnostics.getModel());
                deviceAttr.setModelId(diagnostics.getModelId());
                deviceAttr.setRam(diagnostics.getRam());
                deviceAttr.setStorage(diagnostics.getStorage());
                deviceDiagInput.setDeviceAttr(deviceAttr);
                deviceDiagInput.setDeviceId(diagnostics.getImei());
                inData.setDeviceId(diagnostics.getImei());
                deviceDiagInput.setDeviceReview(diagnostics.getReviewDetails());
                deviceDiagInput.setNewPrice(inData.getNewPrice());
                deviceDiagInput.setOldPrice(diagnostics.getTradeInPrice());
                variables.put("params", deviceDiagInput);
                GraphqlUseCase gqlDeviceDiagInput = new GraphqlUseCase();
                gqlDeviceDiagInput.clearRequest();
                gqlDeviceDiagInput.addRequest(new
                        GraphqlRequest(GraphqlHelper.loadRawString(activityWeakReference.get().getResources(),
                        R.raw.gql_insert_device_diag), DeviceDiagInputResponse.class, variables, false));
                gqlDeviceDiagInput.execute(new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GraphqlResponse graphqlResponse) {
                        if (graphqlResponse != null) {
                            DeviceDiagInputResponse deviceDiagInputResponse = graphqlResponse.getData(DeviceDiagInputResponse.class);
                            if (deviceDiagInputResponse != null && deviceDiagInputResponse.getDeviceDiagInputRepsponse() != null) {
                                if (deviceDiagInputResponse.getDeviceDiagInputRepsponse().isEligible()) {
                                    Intent finalPriceIntent = new Intent(activityWeakReference.get(), FinalPriceActivity.class);
                                    finalPriceIntent.putExtra(TradeInParams.class.getSimpleName(), inData);
                                    activityWeakReference.get().startActivityForResult(finalPriceIntent, FinalPriceActivity.FINAL_PRICE_REQUEST_CODE);
                                } else {
                                    insertResultData.setValue(diagnostics.getTradeInPrice());
                                    ToasterError.showClose(activityWeakReference.get(), deviceDiagInputResponse.getDeviceDiagInputRepsponse().getMessage());
                                }
                            }
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private BroadcastReceiver mBackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent. DO BACK TO PARENT
            Log.d("receiver", "Do back action to parent");
        }
    };

    public TradeInHomeViewModel(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        insertResultData = new MutableLiveData<>();
        minPriceData = new MutableLiveData<>();
        priceFailData = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getInsertResult() {
        return insertResultData;
    }

    public MutableLiveData<JSONObject> getMinPriceData() {
        return minPriceData;
    }

    public MutableLiveData<JSONObject> getPriceFailData() {
        return priceFailData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void getPriceFromSDK() {
        String campaignId = Constants.CAMPAIGN_ID_PROD;
        if (Constants.LAKU6_BASEURL.equals(Constants.LAKU6_BASEURL_STAGING))
            campaignId = Constants.CAMPAIGN_ID_STAGING;
        laku6TradeIn = Laku6TradeIn.getInstance(activityWeakReference.get(), campaignId,
                Constants.APPID, Constants.APIKEY, Constants.LAKU6_BASEURL);
        inData = activityWeakReference.get().getIntent().getParcelableExtra(TradeInParams.class.getSimpleName());
        requestPermission();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void registerReceivers() {
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            FragmentActivity activity = activityWeakReference.get();
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(activity);
            localBroadcastManager.registerReceiver(mMessageReceiver, new IntentFilter("laku6-test-end"));
            localBroadcastManager.registerReceiver(mBackReceiver, new IntentFilter("laku6-back-action"));
            laku6GTMReceiver = new Laku6GTMReceiver();
            localBroadcastManager.registerReceiver(laku6GTMReceiver, new IntentFilter("laku6-gtm"));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void unRegisterReceivers() {
        if (activityWeakReference.get().isFinishing()) {
            if (activityWeakReference != null && activityWeakReference.get() != null) {
                FragmentActivity activity = activityWeakReference.get();
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(activity);
                localBroadcastManager.unregisterReceiver(mMessageReceiver);
                localBroadcastManager.unregisterReceiver(mBackReceiver);
                localBroadcastManager.unregisterReceiver(laku6GTMReceiver);
            }
        }
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        minPriceData.setValue(jsonObject);
    }


    @Override
    public void onError(JSONObject jsonObject) {
        priceFailData.setValue(jsonObject);
    }

    public void startGUITest() {
        laku6TradeIn.startGUITest();

    }

    public void requestPermission() {
        if (!laku6TradeIn.permissionGranted()) {
            ActivityCompat.requestPermissions(activityWeakReference.get(),
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            getMaxPrice();
        }
    }

    public void getMaxPrice() {
        laku6TradeIn.getMinMaxPrice(this);
    }

    public TradeInParams getTradeInParams() {
        return inData;
    }

    private void sendGeneralEvent(String event, String category, String action, String label) {
        if (TrackApp.getInstance() != null && TrackApp.getInstance().getGTM() != null) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(event,
                    category,
                    action,
                    label);
        }
    }
}
