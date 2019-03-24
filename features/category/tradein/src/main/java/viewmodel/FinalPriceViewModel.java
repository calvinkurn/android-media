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
    private WeakReference<FragmentActivity> activityWeakReference;
    private TradeInParams tradeInParams;

    public FinalPriceViewModel(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        deviceDiagData = new MutableLiveData<>();
    }

    public MutableLiveData<DeviceDataResponse> getDeviceDiagData() {
        return deviceDiagData;
    }

    public TradeInParams getTradeInParams(){
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
                R.raw.gql_get_device_diag), DeviceDiagGQL.class, variables1,false));
        if (tradeInParams.isUseKyc() == 1) {
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
