package viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tradein.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import model.TradeInParams;
import model.ValidateTradeInResponse;
import model.ValidateTradePDP;
import rx.Subscriber;
import view.customview.TradeInTextView;
import view.viewcontrollers.AccessRequestFragment;

public class TradeInTextViewModel extends ViewModel implements ITradeInParamReceiver {
    private MutableLiveData<ValidateTradeInResponse> responseData;
    private WeakReference<FragmentActivity> activityWeakReference;

    public TradeInTextViewModel(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        responseData = new MutableLiveData<>();
    }

    public void showAccessRequestDialog() {
        if (activityWeakReference.get() != null) {
            FragmentManager fragmentManager = activityWeakReference.get().getSupportFragmentManager();
            AccessRequestFragment accessDialog = AccessRequestFragment.newInstance();
            accessDialog.show(fragmentManager, AccessRequestFragment.TAG);
        }
    }

    public MutableLiveData<ValidateTradeInResponse> getResponseData() {
        return responseData;
    }

    public void setResponseData(ValidateTradeInResponse responseData) {
        this.responseData.setValue(responseData);
    }

    public void checkTradeIn(TradeInParams tradeInParams, boolean hide) {
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
                            if(tradeInResponse.isEligible()){
                                LocalBroadcastManager.getInstance(activityWeakReference.get()).sendBroadcast(new Intent(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE));

                            }
                            tradeInParams.setIsEligible(tradeInResponse.isEligible() ? 1 : 0);
                            tradeInParams.setUsedPrice(tradeInResponse.getUsedPrice());
                            tradeInParams.setRemainingPrice(tradeInResponse.getRemainingPrice());
                            tradeInParams.setUseKyc(tradeInResponse.isUseKyc() ? 1 : 0);
                        }
                    }
                }
            });
        } else {
            if (!hide) {
                ValidateTradeInResponse response = new ValidateTradeInResponse();
                response.setEligible(true);
                response.setRemainingPrice(tradeInParams.getRemainingPrice());
                response.setUsedPrice(tradeInParams.getUsedPrice());
                response.setUseKyc(tradeInParams.isUseKyc() != 0);
                responseData.setValue(response);
            } else {
                ValidateTradeInResponse response = new ValidateTradeInResponse();
                response.setEligible(false);
                responseData.setValue(response);
            }
        }
    }
}
