package com.tokopedia.ovo.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.ovo.R;
import com.tokopedia.ovo.model.ConfirmData;
import com.tokopedia.ovo.model.WalletData;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class PaymentQrSummaryPresenterImpl extends BaseDaggerPresenter<PaymentQrSummaryContract.View> implements PaymentQrSummaryContract.Presenter {

    private Context mContext;

    public PaymentQrSummaryPresenterImpl(Context context) {
        mContext = context;
    }

    private static final String QR_ID = "qr_id";
    private static final String TRANSFER_ID = "transfer_id";
    private static final String AMOUNT = "amount";
    private static final String FEE = "fee";
    private static final String USE_POINT = "use_point";

    public void fetchWalletDetails() {
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(mContext.getResources(), R.raw.wallet_detail),
                WalletData.class);
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e.getMessage());

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                WalletData walletData = graphqlResponse.getData(WalletData.class);
                if (walletData != null && walletData.getWallet() != null) {
                    if (walletData.getWallet().getErrors() == null
                            || walletData.getWallet().getErrors().size() == 0)
                        getView().setWalletBalance(walletData.getWallet());
                    else
                        getView().showError(walletData.getWallet().getErrors().get(0).getMessage());
                }
            }
        });
    }

    @Override
    public void confirmQrPayment(String imeiNumber, int transferId, float amount, float fees, boolean showUsePointToggle) {
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        Map<String, Object> variables = new HashMap<>();

        variables.put(QR_ID, imeiNumber);
        variables.put(TRANSFER_ID, transferId);
        variables.put(AMOUNT, amount);
        variables.put(FEE, fees);
        variables.put(USE_POINT, showUsePointToggle);
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(mContext.getResources(), R.raw.confirm_imei),
                ConfirmData.class,
                variables);
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e.getMessage());

            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ConfirmData response = graphqlResponse.getData(ConfirmData.class);
                if (response != null
                        && response.getImeiConfirmResponse() != null) {
                    if (response.getImeiConfirmResponse().getErrors().size() > 0
                            && !TextUtils.isEmpty(response.getImeiConfirmResponse().getErrors().get(0).getMessage())) {
                        getView().showError(response.getImeiConfirmResponse().getErrors().get(0).getMessage());
                    } else {
                        getView().goToUrl(response.getImeiConfirmResponse());
                    }
                }
            }
        });
    }
}

