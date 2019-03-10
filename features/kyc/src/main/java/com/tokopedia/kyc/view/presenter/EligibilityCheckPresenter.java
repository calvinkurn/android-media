package com.tokopedia.kyc.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.kyc.model.EligibilityBase;
import com.tokopedia.kyc.view.KycUtil;
import com.tokopedia.kyc.view.interfaces.GenericOperationsView;

import javax.inject.Inject;

import rx.Subscriber;

public class EligibilityCheckPresenter  extends BaseDaggerPresenter<GenericOperationsView> implements IEligibilityCheckListener{

    @Inject
    public EligibilityCheckPresenter(){

    }

    @Override
    public void makeEligibilityRequest() {
        getView().showHideProgressBar(true);
        KycUtil.executeEligibilityCheck(getView().getActivity(), getEligibilityCheckSubscriber());
    }

    private Subscriber getEligibilityCheckSubscriber(){
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showHideProgressBar(false);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                getView().showHideProgressBar(false);
                EligibilityBase eligibilityBase = graphqlResponse.getData(EligibilityBase.class);
                if(eligibilityBase != null && eligibilityBase.getGoalKYCRequest().getKycRequestId() > 0){
                    getView().success(eligibilityBase);
                }else {
                    if(eligibilityBase != null && eligibilityBase.getGoalKYCRequest() != null
                            && eligibilityBase.getGoalKYCRequest().getErrors() != null) {
                        getView().failure(eligibilityBase);
                    }
                }
            }
        };
    }

}
