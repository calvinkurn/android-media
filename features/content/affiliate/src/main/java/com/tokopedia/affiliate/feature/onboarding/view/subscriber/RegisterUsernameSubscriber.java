package com.tokopedia.affiliate.feature.onboarding.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.RegisterUsernameData;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by milhamj on 10/7/18.
 */
public class RegisterUsernameSubscriber extends Subscriber<GraphqlResponse> {
    private final UsernameInputContract.View view;

    public RegisterUsernameSubscriber(UsernameInputContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoading();
        RegisterUsernameData data = graphqlResponse.getData(RegisterUsernameData.class);
    }
}