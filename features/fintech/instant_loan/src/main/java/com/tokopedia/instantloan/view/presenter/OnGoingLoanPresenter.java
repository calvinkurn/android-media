package com.tokopedia.instantloan.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus;
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase;
import com.tokopedia.instantloan.view.contractor.OnGoingLoanContractor;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class OnGoingLoanPresenter extends BaseDaggerPresenter<OnGoingLoanContractor.View>
        implements OnGoingLoanContractor.Presenter {

    private GetLoanProfileStatusUseCase mGetLoanProfileStatusUseCase;

    @Inject
    public OnGoingLoanPresenter(GetLoanProfileStatusUseCase getLoanProfileStatusUseCase) {
        this.mGetLoanProfileStatusUseCase = getLoanProfileStatusUseCase;
    }

    @Override
    public void checkUserOnGoingLoanStatus() {

        mGetLoanProfileStatusUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                RestResponse restResponse = typeRestResponseMap.get(ResponseUserProfileStatus.class);
                ResponseUserProfileStatus responseUserProfileStatus = restResponse.getData();
                getView().setUserOnGoingLoanStatus(
                        (responseUserProfileStatus.getUserProfileLoanEntity().getOnGoingLoanId() != 0),
                        responseUserProfileStatus.getUserProfileLoanEntity().getOnGoingLoanId());
            }
        });
    }

}
