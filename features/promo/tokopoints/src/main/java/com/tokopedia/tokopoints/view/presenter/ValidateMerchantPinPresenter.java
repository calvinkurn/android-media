package com.tokopedia.tokopoints.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.contract.ValidateMerchantPinContract;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ValidateMerchantPinPresenter extends BaseDaggerPresenter<ValidateMerchantPinContract.View>
        implements ValidateMerchantPinContract.Presenter {
    private GraphqlUseCase mUseCase;

    @Inject
    public ValidateMerchantPinPresenter(GraphqlUseCase useCase) {
        this.mUseCase = useCase;
    }


    @Override
    public void destroyView() {
        if (mUseCase != null) {
            mUseCase.unsubscribe();
        }
    }

    public void swipeMyCoupon(String code, String pin) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(CommonConstant.GraphqlVariableKeys.CODE, code);
        variables.put(CommonConstant.GraphqlVariableKeys.PIN, pin);

        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tp_gql_swipe_coupon),
                CouponSwipeUpdateOuter.class,
                variables, false);
        mUseCase.clearRequest();
        mUseCase.addRequest(request);
        mUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //NA
            }

            @Override
            public void onNext(GraphqlResponse response) {
                CouponSwipeUpdateOuter data = response.getData(CouponSwipeUpdateOuter.class);
                if (data != null && data.getSwipeCoupon() != null) {
                    if (data.getSwipeCoupon().getResultStatus().getCode() == CommonConstant.CouponRedemptionCode.SUCCESS) {
                        getView().onSuccess();
                    } else {
                        if (data.getSwipeCoupon().getResultStatus().getMessages().size() > 0) {
                            getView().onError(data.getSwipeCoupon().getResultStatus().getMessages().get(0));
                        }
                    }
                }
            }
        });
    }
}
