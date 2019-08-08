package com.tokopedia.tradein.viewmodel;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
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
import com.tokopedia.tradein.model.DeviceAttr;
import com.tokopedia.tradein.model.DeviceDiagInput;
import com.tokopedia.tradein.model.DeviceDiagInputResponse;
import com.tokopedia.tradein.model.DeviceDiagnostics;
import com.tokopedia.tradein.model.TradeInParams;
import com.tokopedia.tradein.view.viewcontrollers.FinalPriceActivity;
import com.tokopedia.tradein_common.Constants;
import com.tokopedia.tradein_common.viewmodel.BaseViewModel;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class TradeInHomeViewModel extends BaseViewModel implements LifecycleObserver, Laku6TradeIn.TradeInListener {
    private MutableLiveData<Integer> insertResultData;
    private MutableLiveData<JSONObject> minPriceData;
    private MutableLiveData<JSONObject> priceFailData;
    private MutableLiveData<TradeInParams> finalPriceData;
    private TradeInParams inData;

    public TradeInHomeViewModel(Application application, Intent intent) {
        super(application);
        inData = intent.getParcelableExtra(TradeInParams.class.getSimpleName());
        insertResultData = new MutableLiveData<>();
        minPriceData = new MutableLiveData<>();
        priceFailData = new MutableLiveData<>();
    }

    public void processMessage(Intent intent) {
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
                    GraphqlRequest(GraphqlHelper.loadRawString(applicationInstance.getResources(),
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
                                finalPriceData = new MutableLiveData<>();
                                finalPriceData.setValue(inData);
                            } else {
                                insertResultData.setValue(diagnostics.getTradeInPrice());
                                errorMessage.setValue(deviceDiagInputResponse.getDeviceDiagInputRepsponse().getMessage());
                                //ToasterError.showClose(activityWeakReference.get(), deviceDiagInputResponse.getDeviceDiagInputRepsponse().getMessage());
                            }
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public MutableLiveData<TradeInParams> getFinalPriceData() {
        return finalPriceData;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        laku6TradeIn = null;
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        progBarVisibility.setValue(false);
        minPriceData.setValue(jsonObject);
    }

    @Override
    public void onError(JSONObject jsonObject) {
        progBarVisibility.setValue(false);
        priceFailData.setValue(jsonObject);
    }

    public void startGUITest(Laku6TradeIn laku6TradeIn) {
        laku6TradeIn.startGUITest();

    }



    public void getMaxPrice(Laku6TradeIn laku6TradeIn) {
        progBarVisibility.setValue(true);
        laku6TradeIn.getMinMaxPrice(this);
    }

    public TradeInParams getTradeInParams() {
        return inData;
    }
}
