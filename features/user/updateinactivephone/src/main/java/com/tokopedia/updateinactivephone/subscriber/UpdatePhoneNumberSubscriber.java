package com.tokopedia.updateinactivephone.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants;
import com.tokopedia.updateinactivephone.model.response.GqlUpdatePhoneStatusResponse;
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest;

import rx.Subscriber;

public class UpdatePhoneNumberSubscriber extends Subscriber<GraphqlResponse> {


    private final ChangeInactiveFormRequest.View view;

    public UpdatePhoneNumberSubscriber(ChangeInactiveFormRequest.View view) {
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
                graphqlResponse.getData(GqlUpdatePhoneStatusResponse.class) != null) {

            GqlUpdatePhoneStatusResponse gqlUpdatePhoneStatusResponse =
                    graphqlResponse.getData(GqlUpdatePhoneStatusResponse.class);

            if (gqlUpdatePhoneStatusResponse.getChangeInactivePhoneQuery().isSuccess()) {
                view.onUpdateDataRequestSuccess();
            } else {
                view.dismissLoading();
                resolveError(gqlUpdatePhoneStatusResponse.getChangeInactivePhoneQuery().getError());
            }
        } else {
            view.dismissLoading();
            view.onPhoneServerError();
        }
    }

    private void resolveError(String error) {
        if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.UNREGISTERED_USER.equalsIgnoreCase(error)) {
            view.onUserNotRegistered();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_FILE_UPLOADED.equalsIgnoreCase(error)) {
            view.onInvalidFileUploaded();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.REQUEST_FAILED.equalsIgnoreCase(error)) {
            view.onUpdateDataRequestFailed();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.WRONG_USER_ID.equalsIgnoreCase(error)) {
            view.onWrongUserIDInput();
        } else if (UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equalsIgnoreCase(error)) {
            view.onPhoneServerError();
        }
    }
}
