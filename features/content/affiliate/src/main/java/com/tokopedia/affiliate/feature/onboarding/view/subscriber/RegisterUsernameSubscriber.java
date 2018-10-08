package com.tokopedia.affiliate.feature.onboarding.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.RegisterUsernameData;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by milhamj on 10/7/18.
 */
public class RegisterUsernameSubscriber extends Subscriber<GraphqlResponse> {
    private static final String ERROR_VALIDATION = "validation";

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
        view.onErrorRegisterUsername(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        view.hideLoading();
        RegisterUsernameData data = graphqlResponse.getData(RegisterUsernameData.class);

        if (data.getBymeRegisterAffiliateName().getError() != null
                && !TextUtils.isEmpty(
                        data.getBymeRegisterAffiliateName().getError().getMessage())) {
            if (ERROR_VALIDATION.equalsIgnoreCase(
                    data.getBymeRegisterAffiliateName().getError().getType())) {
                view.onErrorInputRegisterUsername(
                        data.getBymeRegisterAffiliateName().getError().getMessage()
                );
            } else {
                view.onErrorRegisterUsername(
                        data.getBymeRegisterAffiliateName().getError().getMessage()
                );
            }
            return;
        }

        if (!data.getBymeRegisterAffiliateName().isSuccess()) {
            throw new RuntimeException();
        } else {
            view.onSuccessRegisterUsername();
        }
    }
}