package viewmodel;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;

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
import rx.Subscriber;
import view.viewcontrollers.TradeInHomeActivity;

public class TradeInTextViewModel extends ViewModel implements ITradeInParamReceiver {
    private MutableLiveData<ValidateTradeInResponse> responseData;
    private WeakReference<Activity> activityWeakReference;

    public TradeInTextViewModel(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
        responseData = new MutableLiveData<>();
    }

    public void startTradeIn() {
        activityWeakReference.get().startActivityForResult(new Intent(activityWeakReference.get(), TradeInHomeActivity.class), 1001);
    }

    public MutableLiveData<ValidateTradeInResponse> getResponseData() {
        return responseData;
    }

    public void setResponseData(ValidateTradeInResponse responseData) {
        this.responseData.setValue(responseData);
    }

    public void checkTradeIn(TradeInParams tradeInParams) {
        if (tradeInParams.getUsedPrice() <= 0) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("params", tradeInParams);
            GraphqlUseCase gqlValidatetradeIn = new GraphqlUseCase();
            gqlValidatetradeIn.clearRequest();
            gqlValidatetradeIn.addRequest(new
                    GraphqlRequest(GraphqlHelper.loadRawString(activityWeakReference.get().getResources(),
                    R.raw.gql_validate_tradein), ValidateTradeInResponse.class, variables));
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
                        ValidateTradeInResponse tradeInResponse = graphqlResponse.getData(ValidateTradeInResponse.class);
                        if (tradeInResponse != null) {
                            if (tradeInResponse.isEligible()) {
                                //todo add call to laku6.maxPrice()
                            } else
                                responseData.setValue(tradeInResponse);
                            if (tradeInResponse.isEligible() && tradeInResponse.isDiagnosed() && tradeInResponse.getUsedPrice() > 0) {
                                tradeInParams.setUsedPrice(tradeInResponse.getUsedPrice());
                                tradeInParams.setRemainingPrice(tradeInResponse.getRemainingPrice());
                                tradeInParams.setUseKyc(tradeInResponse.isUseKyc() ? 1 : 0);
                            }
                        }
                    }
                }
            });
        } else {
            ValidateTradeInResponse response = new ValidateTradeInResponse();
            response.setEligible(true);
            response.setDiagnosed(true);
            response.setRemainingPrice(tradeInParams.getRemainingPrice());
            response.setUsedPrice(tradeInParams.getUsedPrice());
            response.setUseKyc(tradeInParams.isUseKyc() != 0);
            responseData.setValue(response);
        }
    }
}
