package viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tradein.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import model.DeviceDataResponse;
import model.DeviceDiagGQL;
import model.DeviceDiagParams;
import model.KYCDetailGQL;
import model.KYCDetails;
import model.TradeInParams;
import rx.Subscriber;

public class FinalPriceViewModel extends ViewModel implements LifecycleObserver {
    private MutableLiveData<DeviceDataResponse> deviceDiagData;
    private MutableLiveData<KYCDetails> kycDetailsData;
    private WeakReference<FragmentActivity> activityWeakReference;

    public FinalPriceViewModel(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        deviceDiagData = new MutableLiveData<>();
        kycDetailsData = new MutableLiveData<>();
    }

    public MutableLiveData<DeviceDataResponse> getDeviceDiagData() {
        return deviceDiagData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void getDiagnosticData() {
        Intent inIntent = activityWeakReference.get().getIntent();
        Bundle tradeIn = inIntent.getExtras();
        int productid = tradeIn.getInt(TradeInParams.PARAM_PRODUCT_ID, 0);
        String deviceid = tradeIn.getString(TradeInParams.PARAM_DEVICE_ID);
        int newprice = tradeIn.getInt(TradeInParams.PARAM_NEW_PRICE, 0);
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
                R.raw.gql_get_device_diag), DeviceDiagGQL.class, variables1,false));
        if (tradeIn.getBoolean(TradeInParams.PARAM_USE_KYC)) {
            Map<String, Object> variables2 = new HashMap<>();
            variables2.put("projectID", 4);
            gqlDeviceDiagInput.addRequest(new
                    GraphqlRequest(GraphqlHelper.loadRawString(activityWeakReference.get().getResources(),
                    R.raw.gql_get_kyc_status), KYCDetailGQL.class, variables2,false));
        }

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
                    DeviceDiagGQL deviceDiagGQL = graphqlResponse.getData(DeviceDiagGQL.class);
                    assert deviceDiagGQL != null;
                    DeviceDataResponse deviceDataResponse = deviceDiagGQL.getDiagResponse();
                    KYCDetailGQL kycDetailGQL = graphqlResponse.getData(KYCDetailGQL.class);
                    assert kycDetailGQL != null;
                    KYCDetails kycDetails = kycDetailGQL.getKycDetails();
                    if (deviceDataResponse != null && kycDetails != null) {
                        deviceDataResponse.setKycDetails(kycDetails);
                        deviceDiagData.setValue(deviceDataResponse);
                    }
                }
            }
        });

    }

}
