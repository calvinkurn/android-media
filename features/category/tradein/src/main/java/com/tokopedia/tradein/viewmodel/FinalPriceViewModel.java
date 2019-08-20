package com.tokopedia.tradein.viewmodel;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.track.TrackApp;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.model.DeviceDataResponse;
import com.tokopedia.tradein.model.DeviceDiagGQL;
import com.tokopedia.tradein.model.DeviceDiagParams;
import com.tokopedia.tradein.model.KYCDetailGQL;
import com.tokopedia.tradein.model.KYCDetails;
import com.tokopedia.tradein.model.TradeInParams;
import com.tokopedia.tradein_common.viewmodel.BaseViewModel;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class FinalPriceViewModel extends BaseViewModel implements LifecycleObserver {
    private MutableLiveData<DeviceDataResponse> deviceDiagData;
    private WeakReference<FragmentActivity> activityWeakReference;
    private TradeInParams tradeInParams;

    public FinalPriceViewModel(Application application) {
        super(application);
        deviceDiagData = new MutableLiveData<>();
    }

    public MutableLiveData<DeviceDataResponse> getDeviceDiagData() {
        return deviceDiagData;
    }

    public TradeInParams getTradeInParams() {
        return tradeInParams;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void getDiagnosticData() {
        Intent inIntent = activityWeakReference.get().getIntent();
        tradeInParams = inIntent.getParcelableExtra(TradeInParams.class.getSimpleName());
        int productid = tradeInParams.getProductId();
        String deviceid = tradeInParams.getDeviceId();
        int newprice = tradeInParams.getNewPrice();
        DeviceDiagParams params = new DeviceDiagParams();
        params.setProductId(productid);
        params.setDeviceId(deviceid);
        params.setNewPrice(newprice);
        Map<String, Object> variables1 = new HashMap<>();
        variables1.put("params", params);
        GraphqlUseCase gqlDeviceDiagInput = new GraphqlUseCase();
        gqlDeviceDiagInput.clearRequest();
        gqlDeviceDiagInput.addRequest(new
                GraphqlRequest(GraphqlHelper.loadRawString(activityWeakReference.get().getResources(),
                R.raw.gql_get_device_diag), DeviceDiagGQL.class, variables1, false));
        if (tradeInParams.isUseKyc() == 1) {
            Map<String, Object> variables2 = new HashMap<>();
            variables2.put("projectID", 4);
            gqlDeviceDiagInput.addRequest(new
                    GraphqlRequest(GraphqlHelper.loadRawString(activityWeakReference.get().getResources(),
                    R.raw.gql_get_kyc_status), KYCDetailGQL.class, variables2, false));
        }

        gqlDeviceDiagInput.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                NetworkErrorHelper.createSnackbarRedWithAction(activityWeakReference.get(),
                        activityWeakReference.get().getString(R.string.default_request_error_timeout), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getDiagnosticData();
                            }
                        });
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (graphqlResponse != null) {
                    DeviceDiagGQL deviceDiagGQL = graphqlResponse.getData(DeviceDiagGQL.class);
                    if (deviceDiagGQL != null) {
                        DeviceDataResponse deviceDataResponse = deviceDiagGQL.getDiagResponse();
                        KYCDetailGQL kycDetailGQL = graphqlResponse.getData(KYCDetailGQL.class);
                        if (deviceDataResponse != null) {
                            if (kycDetailGQL != null) {
                                KYCDetails kycDetails = kycDetailGQL.getKycDetails();
                                if (kycDetails != null)
                                    deviceDataResponse.setKycDetails(kycDetails);
                            }
                            deviceDiagData.setValue(deviceDataResponse);
                        } else throw new RuntimeException("");
                    } else throw new RuntimeException("");
                }
            }
        });

        TrackApp.getInstance().getGTM().sendGeneralEvent("viewTradeIn",
                "harga final trade in",
                "view harga final",
                "");
    }

}
