package viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.tradein.R;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import model.TradeInParams;
import model.ValidateTradeInResponse;
import model.ValidateTradePDP;
import rx.Subscriber;
import view.viewcontrollers.AccessRequestFragment;

public class TradeInTextViewModel extends ViewModel implements ITradeInParamReceiver {
    private MutableLiveData<ValidateTradeInResponse> responseData;
    private WeakReference<FragmentActivity> activityWeakReference;
    private int newPrice;

    public TradeInTextViewModel(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        responseData = new MutableLiveData<>();
    }

    public void showAccessRequestDialog() {
        if (activityWeakReference.get() != null) {
            FragmentManager fragmentManager = activityWeakReference.get().getSupportFragmentManager();
            AccessRequestFragment accessDialog = AccessRequestFragment.newInstance(newPrice);
            accessDialog.show(fragmentManager, AccessRequestFragment.TAG);
        }
    }

    public MutableLiveData<ValidateTradeInResponse> getResponseData() {
        return responseData;
    }

    public void setResponseData(ValidateTradeInResponse responseData) {
        this.responseData.setValue(responseData);
    }

    public void checkTradeIn(TradeInParams tradeInParams) {
        if (tradeInParams.getIsEligible() == 0) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("params", tradeInParams);
            GraphqlUseCase gqlValidatetradeIn = new GraphqlUseCase();
            gqlValidatetradeIn.clearRequest();
            gqlValidatetradeIn.addRequest(new
                    GraphqlRequest(GraphqlHelper.loadRawString(activityWeakReference.get().getResources(),
                    R.raw.gql_validate_tradein), ValidateTradePDP.class, variables));
            gqlValidatetradeIn.execute(new Subscriber<GraphqlResponse>() {
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
                        ValidateTradePDP tradePDP = graphqlResponse.getData(ValidateTradePDP.class);
                        assert tradePDP != null;
                        ValidateTradeInResponse tradeInResponse = tradePDP.getResponse();
                        if (tradeInResponse != null) {
                            responseData.setValue(tradeInResponse);
                            tradeInParams.setIsEligible(tradeInResponse.isEligible() ? 1 : 0);
                            tradeInParams.setUsedPrice(tradeInResponse.getUsedPrice());
                            tradeInParams.setRemainingPrice(tradeInResponse.getRemainingPrice());
                            tradeInParams.setUseKyc(tradeInResponse.isUseKyc() ? 1 : 0);
                        }
                    }
                }
            });
        } else {
            ValidateTradeInResponse response = new ValidateTradeInResponse();
            response.setEligible(true);
            response.setRemainingPrice(tradeInParams.getRemainingPrice());
            response.setUsedPrice(tradeInParams.getUsedPrice());
            response.setUseKyc(tradeInParams.isUseKyc() != 0);
            newPrice = tradeInParams.getPrice();
            responseData.setValue(response);
        }
    }
}
