package com.tokopedia.updateinactivephone.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.core.app.MainApplication
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.model.response.GqlUpdatePhoneStatusResponse
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest

import rx.Subscriber

class UpdatePhoneNumberSubscriber(private val view: ChangeInactiveFormRequest.View) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        view.dismissLoading()
        view.showErrorValidateData(ErrorHandler.getErrorMessage(MainApplication.getAppContext(), e))
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (graphqlResponse?.getData<Any>(GqlUpdatePhoneStatusResponse::class.java) != null) {
            val gqlUpdatePhoneStatusResponse = graphqlResponse.getData<GqlUpdatePhoneStatusResponse>(GqlUpdatePhoneStatusResponse::class.java)
            if (gqlUpdatePhoneStatusResponse.changeInactivePhoneQuery?.isSuccess == true) {
                view.onUpdateDataRequestSuccess()
            } else {
                view.dismissLoading()
                gqlUpdatePhoneStatusResponse.changeInactivePhoneQuery?.error?.let { resolveError(it) }
            }
        } else {
            view.dismissLoading()
            view.onPhoneServerError()
        }
    }

    private fun resolveError(error: String) {
        when {
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.UNREGISTERED_USER.equals(error, ignoreCase = true) -> view.onUserNotRegistered()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_FILE_UPLOADED.equals(error, ignoreCase = true) -> view.onInvalidFileUploaded()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.REQUEST_FAILED.equals(error, ignoreCase = true) -> view.onUpdateDataRequestFailed()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.WRONG_USER_ID.equals(error, ignoreCase = true) -> view.onWrongUserIDInput()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equals(error, ignoreCase = true) -> view.onPhoneServerError()
        }
    }
}
