package com.tokopedia.updateinactivephone.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.core.app.MainApplication
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.model.response.GqlCheckPhoneStatusResponse
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone

import rx.Subscriber

class CheckPhoneNumberStatusSubscriber(private val view: ChangeInactivePhone.View) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        view.dismissLoading()
        view.showErrorPhoneNumber(ErrorHandler.getErrorMessage(MainApplication.getAppContext(), e))
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (graphqlResponse?.getData<Any>(GqlCheckPhoneStatusResponse::class.java) != null) {

            val gqlCheckPhoneStatusResponse = graphqlResponse.getData<GqlCheckPhoneStatusResponse>(GqlCheckPhoneStatusResponse::class.java)

            if (gqlCheckPhoneStatusResponse.validateInactivePhone?.isSuccess == true) {
                gqlCheckPhoneStatusResponse.validateInactivePhone?.userId?.let { view.onPhoneStatusSuccess(it) }
            } else {
                gqlCheckPhoneStatusResponse.validateInactivePhone?.error?.let { resolveError(it) }
            }
        } else {
            view.onPhoneServerError()
        }
        view.dismissLoading()

    }

    private fun resolveError(error: String) {
        when {
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.INVALID_PHONE.equals(error, ignoreCase = true) -> view.onPhoneInvalid()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_SHORT.equals(error, ignoreCase = true) -> view.onPhoneTooShort()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_TOO_LONG.equals(error, ignoreCase = true) -> view.onPhoneTooLong()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_BLACKLISTED.equals(error, ignoreCase = true) -> view.onPhoneBlackListed()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_NOT_REGISTERED.equals(error, ignoreCase = true) -> view.onPhoneNotRegistered()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_WITH_REGISTERED_EMAIL.equals(error, ignoreCase = true) -> view.onPhoneRegisteredWithEmail()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.PHONE_WITH_PENDING_REQUEST.equals(error, ignoreCase = true) -> view.onPhoneDuplicateRequest()
            UpdateInactivePhoneConstants.RESPONSE_CONSTANTS.SERVER_ERROR.equals(error, ignoreCase = true) -> view.onPhoneServerError()
        }
    }
}
