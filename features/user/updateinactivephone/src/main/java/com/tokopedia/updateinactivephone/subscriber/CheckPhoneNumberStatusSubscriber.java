package com.tokopedia.updateinactivephone.subscriber;

import com.tokoepdia.updateinactivephone.model.response.GqlCheckPhoneStatusResponse;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone;

import rx.Subscriber;

public class CheckPhoneNumberStatusSubscriber extends Subscriber<GraphqlResponse> {

    private final ChangeInactivePhone.View view;

    private final String INVALID_PHONE = "invalid_msisdn";
    private final String PHONE_TOO_SHORT = "too_short_msisdn";
    private final String PHONE_TOO_LONG = "too_long_msisdn";
    private final String PHONE_BLACKLISTED = "blacklisted_msisdn";
    private final String PHONE_NOT_REGISTERED = "unregistered_msisdn";
    private final String PHONE_WITH_REGISTERED_EMAIL = "registered_email";
    private final String PHONE_WITH_PENDING_REQUEST = "has_pending_request";
    private final String SERVER_ERROR = "server_error";


    public CheckPhoneNumberStatusSubscriber(ChangeInactivePhone.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        view.showErrorPhoneNumber(ErrorHandler.getErrorMessage(MainApplication.getAppContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null &&
                graphqlResponse.getData(GqlCheckPhoneStatusResponse.class) != null) {

            GqlCheckPhoneStatusResponse gqlCheckPhoneStatusResponse =
                    graphqlResponse.getData(GqlCheckPhoneStatusResponse.class);

            if (gqlCheckPhoneStatusResponse.getValidateInactivePhone().isSuccess()) {
                view.onPhoneStatusSuccess();
            } else {
                resolveError(gqlCheckPhoneStatusResponse.getValidateInactivePhone().getError());
            }
        } else {
            view.onPhoneServerError();
        }
        view.dismissLoading();

    }

    private void resolveError(String error) {
        if (INVALID_PHONE.equalsIgnoreCase(error)) {
            view.onPhoneInvalid();
        } else if (PHONE_TOO_SHORT.equalsIgnoreCase(error)) {
            view.onPhoneTooShort();
        } else if (PHONE_TOO_LONG.equalsIgnoreCase(error)) {
            view.onPhoneTooLong();
        } else if (PHONE_BLACKLISTED.equalsIgnoreCase(error)) {
            view.onPhoneBlackListed();
        } else if (PHONE_NOT_REGISTERED.equalsIgnoreCase(error)) {
            view.onPhoneNotRegistered();
        } else if (PHONE_WITH_REGISTERED_EMAIL.equalsIgnoreCase(error)) {
            view.onPhoneRegisteredWithEmail();
        } else if (PHONE_NOT_REGISTERED.equalsIgnoreCase(error)) {
            view.onPhoneNotRegistered();
        } else if (PHONE_WITH_PENDING_REQUEST.equalsIgnoreCase(error)) {
            view.onPhoneDuplicateRequest();
        } else if (SERVER_ERROR.equalsIgnoreCase(error)) {
            view.onPhoneServerError();
        }
    }
}
