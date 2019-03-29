package com.tokopedia.updateinactivephone.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants;
import com.tokopedia.updateinactivephone.model.response.GqlValidateUserDataResponse;
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest;

import rx.Subscriber;

public class ValidateUserDataSubscriber extends Subscriber<GraphqlResponse> {


    private final ChangeInactiveFormRequest.View view;

    public ValidateUserDataSubscriber(ChangeInactiveFormRequest.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        view.showErrorValidateData(ErrorHandler.getErrorMessage(MainApplication.getAppContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        if (graphqlResponse != null &&
                graphqlResponse.getData(GqlValidateUserDataResponse.class) != null) {

            GqlValidateUserDataResponse gqlValidateUserDataResponse =
                    graphqlResponse.getData(GqlValidateUserDataResponse.class);

            if (gqlValidateUserDataResponse.getValidateUserDataResponse().isSuccess()) {
                view.onUserDataValidated();
            } else {
                view.dismissLoading();
                resolveError(gqlValidateUserDataResponse.getValidateUserDataResponse().getError());
            }
        } else {
            view.dismissLoading();
            view.onPhoneServerError();
        }
    }

    private void resolveError(String error) {
        if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SAME_MSISDN.equalsIgnoreCase(error)) {
            view.onSameMsisdn();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_SHORT.equalsIgnoreCase(error)) {
            view.onPhoneTooShort();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_LONG.equalsIgnoreCase(error)) {
            view.onPhoneTooLong();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_BLACKLISTED.equalsIgnoreCase(error)) {
            view.onPhoneBlackListed();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.WRONG_USER_ID.equalsIgnoreCase(error)) {
            view.onWrongUserIDInput();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_WITH_PENDING_REQUEST.equalsIgnoreCase(error)) {
            view.onPhoneDuplicateRequest();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equalsIgnoreCase(error)) {
            view.onPhoneServerError();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.REGISTERED_MSISDN.equalsIgnoreCase(error)) {
            view.onAlreadyRegisteredMsisdn();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.EMPTY_MSISDN.equalsIgnoreCase(error)) {
            view.onEmptyMsisdn();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_PHONE.equalsIgnoreCase(error)) {
            view.onInvalidPhone();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_EMAIL.equalsIgnoreCase(error)) {
            view.onEmailError();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equalsIgnoreCase(error)) {
            view.onPhoneServerError();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.MAX_REACHED_MSISDN.equalsIgnoreCase(error)) {
            view.onMaxReachedPhone();
        }
    }
}
