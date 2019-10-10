package com.tokopedia.tradein.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.model.TradeInParams;
import com.tokopedia.tradein.model.ValidateTradeInResponse;
import com.tokopedia.tradein.model.ValidateTradePDP;
import com.tokopedia.tradein.view.customview.TradeInTextView;
import com.tokopedia.design.dialog.AccessRequestDialogFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class TradeInTextViewModel extends AndroidViewModel implements ITradeInParamReceiver {
    private MutableLiveData<ValidateTradeInResponse> responseData;
    private WeakReference<FragmentActivity> activityWeakReference;
    private Application application;

    public TradeInTextViewModel(Application application) {
        super(application);
        responseData = new MutableLiveData<>();
        this.application = application;
    }

    public void setActivity(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    public void showAccessRequestDialog() {
        if (activityWeakReference.get() != null) {
            FragmentManager fragmentManager = activityWeakReference.get().getSupportFragmentManager();
            FragmentActivity activity = activityWeakReference.get();

            AccessRequestDialogFragment accessDialog = AccessRequestDialogFragment.newInstance();
            accessDialog.setBodyText(activity.getString(R.string.tradein_text_permission_description));
            accessDialog.setTitle(activity.getString(R.string.tradein_text_request_access));
            accessDialog.setNegativeButton("");
            accessDialog.show(fragmentManager, AccessRequestDialogFragment.TAG);
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
                    GraphqlRequest(GraphqlHelper.loadRawString(application.getResources(),
                    R.raw.gql_validate_tradein), ValidateTradePDP.class, variables, false));
            gqlValidatetradeIn.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    broadcastDefaultResponse();
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    if (graphqlResponse != null) {
                        ValidateTradePDP tradePDP = graphqlResponse.getData(ValidateTradePDP.class);
                        assert tradePDP != null;
                        ValidateTradeInResponse tradeInResponse = tradePDP.getResponse();
                        if (tradeInResponse != null) {
                            responseData.setValue(tradeInResponse);
                            Intent intent = new Intent(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE);
                            intent.putExtra(TradeInTextView.EXTRA_ISELLIGIBLE, tradeInResponse.isEligible());
                            LocalBroadcastManager.getInstance(activityWeakReference.get()).sendBroadcast(intent);

                            tradeInParams.setIsEligible(tradeInResponse.isEligible() ? 1 : 0);
                            tradeInParams.setUsedPrice(tradeInResponse.getUsedPrice());
                            tradeInParams.setRemainingPrice(tradeInResponse.getRemainingPrice());
                            tradeInParams.setUseKyc(tradeInResponse.isUseKyc() ? 1 : 0);
                        } else {
                            broadcastDefaultResponse();
                        }
                    } else {
                        broadcastDefaultResponse();
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

    private void broadcastDefaultResponse() {
        if (activityWeakReference.get() != null) {
            Intent intent = new Intent(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE);
            intent.putExtra(TradeInTextView.EXTRA_ISELLIGIBLE, false);
            LocalBroadcastManager.getInstance(activityWeakReference.get()).sendBroadcast(intent);
        }
    }
}
