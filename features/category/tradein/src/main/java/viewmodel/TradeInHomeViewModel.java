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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.laku6.tradeinsdk.api.Laku6TradeIn;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
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
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import tradein_common.Constants;
import view.viewcontrollers.FinalPriceActivity;

public class TradeInHomeViewModel extends ViewModel implements LifecycleObserver, Laku6TradeIn.TradeInListener {
    private MutableLiveData<String> eligibileTickerData;
    private MutableLiveData<JSONObject> minPriceData;
    private MutableLiveData<JSONObject> priceFailData;

    private WeakReference<FragmentActivity> activityWeakReference;
    private TradeInParams inData;
    private Laku6TradeIn laku6TradeIn;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;

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
                deviceDiagInput.setNewPrice(activityWeakReference.get().getIntent().getIntExtra(TradeInParams.PARAM_NEW_PRICE, 0));
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
                            if (graphqlResponse.getData(DeviceDiagInputResponse.class) != null) {
                                Intent finalPriceIntent = new Intent(activityWeakReference.get(), FinalPriceActivity.class);
                                finalPriceIntent.putExtra(TradeInParams.class.getSimpleName(),inData);
                                activityWeakReference.get().startActivityForResult(finalPriceIntent, FinalPriceActivity.FINAL_PRICE_REQUEST_CODE);
                            } else {
                                List<GraphqlError> errors = graphqlResponse.getError(DeviceDiagInputResponse.class);
                                if (errors.get(0).getMessage().contains("duplicate key value")) {
                                    Intent finalPriceIntent = new Intent(activityWeakReference.get(), FinalPriceActivity.class);
                                    finalPriceIntent.putExtra(TradeInParams.class.getSimpleName(),inData);
                                    activityWeakReference.get().startActivityForResult(finalPriceIntent, FinalPriceActivity.FINAL_PRICE_REQUEST_CODE);
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
        eligibileTickerData = new MutableLiveData<>();
        minPriceData = new MutableLiveData<>();
        priceFailData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getEligibileTickerData() {
        return eligibileTickerData;
    }

    public MutableLiveData<JSONObject> getMinPriceData() {
        return minPriceData;
    }

    public MutableLiveData<JSONObject> getPriceFailData() {
        return priceFailData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void getPriceFromSDK() {
        laku6TradeIn = Laku6TradeIn.getInstance(activityWeakReference.get(), "tokopediaSandbox",
                Constants.APPID, Constants.APIKEY, Constants.LAKU6_BASEURL);
        laku6TradeIn.setCampaignTradeInId("tokopediaSandbox");
        inData = activityWeakReference.get().getIntent().getParcelableExtra(TradeInParams.class.getSimpleName());
        requestPermission();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void registerReceivers() {
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            LocalBroadcastManager.getInstance(activityWeakReference.get()).registerReceiver(mMessageReceiver, new IntentFilter("laku6-test-end"));
            LocalBroadcastManager.getInstance(activityWeakReference.get()).registerReceiver(mBackReceiver, new IntentFilter("laku6-back-action"));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void unRegisterReceivers() {
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            LocalBroadcastManager.getInstance(activityWeakReference.get()).unregisterReceiver(mMessageReceiver);
            LocalBroadcastManager.getInstance(activityWeakReference.get()).unregisterReceiver(mBackReceiver);
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

    public TradeInParams getTradeInParams(){
        return inData;
    }
}
