package com.tokopedia.updateinactivephone.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.core.app.MainApplication
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.model.response.GqlValidateUserDataResponse
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest

import rx.Subscriber

class ValidateUserDataSubscriber(private val view: ChangeInactiveFormRequest.View) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        view.dismissLoading()
        view.showErrorValidateData(ErrorHandler.getErrorMessage(MainApplication.getAppContext(), e))
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (graphqlResponse?.getData<Any>(GqlValidateUserDataResponse::class.java) != null) {
            val gqlValidateUserDataResponse = graphqlResponse.getData<GqlValidateUserDataResponse>(GqlValidateUserDataResponse::class.java)
            val userId = gqlValidateUserDataResponse.validateUserDataResponse?.userId.toString()

            if (gqlValidateUserDataResponse.validateUserDataResponse?.isSuccess == true) {
                view.onUserDataValidated(userId)
            } else {
                view.dismissLoading()
                gqlValidateUserDataResponse.validateUserDataResponse?.error?.let { resolveError(it) }
            }
        } else {
            view.dismissLoading()
            view.onPhoneServerError()
        }
    }

    private fun resolveError(error: String) {
        when {
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SAME_MSISDN.equals(error, ignoreCase = true) -> view.onSameMsisdn()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_SHORT.equals(error, ignoreCase = true) -> view.onPhoneTooShort()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_LONG.equals(error, ignoreCase = true) -> view.onPhoneTooLong()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_BLACKLISTED.equals(error, ignoreCase = true) -> view.onPhoneBlackListed()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.WRONG_USER_ID.equals(error, ignoreCase = true) -> view.onWrongUserIDInput()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_WITH_PENDING_REQUEST.equals(error, ignoreCase = true) -> view.onPhoneDuplicateRequest()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equals(error, ignoreCase = true) -> view.onPhoneServerError()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.REGISTERED_MSISDN.equals(error, ignoreCase = true) -> view.onAlreadyRegisteredMsisdn()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.EMPTY_MSISDN.equals(error, ignoreCase = true) -> view.onEmptyMsisdn()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_PHONE.equals(error, ignoreCase = true) -> view.onInvalidPhone()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_EMAIL.equals(error, ignoreCase = true) -> view.onEmailError()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equals(error, ignoreCase = true) -> view.onPhoneServerError()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.MAX_REACHED_MSISDN.equals(error, ignoreCase = true) -> view.onMaxReachedPhone()
        }
    }
}
