package com.tokopedia.updateinactivephone.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants;
import com.tokopedia.updateinactivephone.model.response.GqlCheckPhoneStatusResponse;
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone;

import rx.Subscriber;

public class CheckPhoneNumberStatusSubscriber extends Subscriber<GraphqlResponse> {

    private final ChangeInactivePhone.View view;

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
                view.onPhoneStatusSuccess(gqlCheckPhoneStatusResponse.getValidateInactivePhone().getUserId());
            } else {
                resolveError(gqlCheckPhoneStatusResponse.getValidateInactivePhone().getError());
            }
        } else {
            view.onPhoneServerError();
        }
        view.dismissLoading();

    }

    private void resolveError(String error) {
        if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_PHONE.equalsIgnoreCase(error)) {
            view.onPhoneInvalid();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_SHORT.equalsIgnoreCase(error)) {
            view.onPhoneTooShort();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_LONG.equalsIgnoreCase(error)) {
            view.onPhoneTooLong();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_BLACKLISTED.equalsIgnoreCase(error)) {
            view.onPhoneBlackListed();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_NOT_REGISTERED.equalsIgnoreCase(error)) {
            view.onPhoneNotRegistered();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_WITH_REGISTERED_EMAIL.equalsIgnoreCase(error)) {
            view.onPhoneRegisteredWithEmail();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_WITH_PENDING_REQUEST.equalsIgnoreCase(error)) {
            view.onPhoneDuplicateRequest();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equalsIgnoreCase(error)) {
            view.onPhoneServerError();
        }
    }
}
