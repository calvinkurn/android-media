package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.SendGiftContract;
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity;
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class SendGiftPresenter extends BaseDaggerPresenter<SendGiftContract.View>
        implements SendGiftContract.Presenter {
    private GraphqlUseCase mRedeemCouponUseCase;
    private GraphqlUseCase mStartSendGift;

    @Inject
    public SendGiftPresenter(GraphqlUseCase startSendGift,
                             GraphqlUseCase redeemCouponUseCase
    ) {

        this.mRedeemCouponUseCase = redeemCouponUseCase;
        this.mStartSendGift = startSendGift;
    }


    @Override
    public void destroyView() {
        if (mStartSendGift != null) {
            mStartSendGift.unsubscribe();
        }

        if (mRedeemCouponUseCase != null) {
            mRedeemCouponUseCase.unsubscribe();
        }
    }

    @Override
    public void sendGift(int id, String email, String notes) {
        getView().showLoadingSendNow();
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_ID, id);
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 1);
        variables.put(CommonConstant.GraphqlVariableKeys.GIFT_EMAIL, email);
        variables.put(CommonConstant.GraphqlVariableKeys.NOTES, notes);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_tokopoint_redeem_coupon),
                com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity.class,
                variables, false);
        mRedeemCouponUseCase.clearRequest();
        mRedeemCouponUseCase.addRequest(request);
        mRedeemCouponUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
                getView().hideLoadingSendNow();
            }

            @Override
            public void onNext(GraphqlResponse response) {
                getView().hideLoadingSendNow();
                RedeemCouponBaseEntity redeemCouponBaseEntity = response.getData(RedeemCouponBaseEntity.class);
                String title;
                String message;
                if (redeemCouponBaseEntity != null && redeemCouponBaseEntity.getHachikoRedeem() != null) {
                    title = getView().getAppContext().getString(R.string.tp_send_gift_success_title);
                    message = getView().getAppContext().getString(R.string.tp_send_gift_success_message);
                } else {

                    //show error
                    List<GraphqlError> errors = response.getError(RedeemCouponBaseEntity.class);

                    title = getView().getAppContext().getString(R.string.tp_send_gift_failed_title);
                    message = getView().getAppContext().getString(R.string.tp_send_gift_failed_message);

                    if (errors != null && errors.size() > 0) {
                        String[] mesList = errors.get(0).getMessage().split("|");
                        if (mesList.length == 3) {
                            title = mesList[0];
                            message = mesList[1];
                        } else if (mesList.length == 2) {
                            message = mesList[0];
                        }
                    }
                }

                getView().showPopup(title, message);
            }
        });
    }

    @Override
    public void preValidateGift(int id, String email) {
        getView().showLoading();
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CATALOG_ID, id);
        variables.put(CommonConstant.GraphqlVariableKeys.IS_GIFT, 1);
        variables.put(CommonConstant.GraphqlVariableKeys.GIFT_EMAIL, email);
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(), R.raw.tp_gql_tokopoint_validate_redeem),
                ValidateCouponBaseEntity.class, variables, false);
        mStartSendGift.clearRequest();
        mStartSendGift.addRequest(graphqlRequest);
        mStartSendGift.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
                getView().hideLoading();
            }

            @Override
            public void onNext(GraphqlResponse response) {
                getView().hideLoading();
                ValidateCouponBaseEntity validateCoupon = response.getData(ValidateCouponBaseEntity.class);

                if (validateCoupon.getValidateCoupon() != null && validateCoupon.getValidateCoupon().isValid() == 1) {
                    getView().openPreConfirmationWindow();
                } else {
                    String[] errorsMessage = response.getError(ValidateCouponBaseEntity.class).get(0).getMessage().split("\\|");
                    getView().onErrorPreValidate(errorsMessage[0].split("]")[1]);
                }
            }
        });
    }
}
