package com.tokopedia.sessioncommon.domain.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sessioncommon.data.admin.AdminInfoResponse
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

class GetAdminInfoSubscriber(
    private val userSession: UserSessionInterface,
    private val onSuccessGetUserProfile: () -> Unit,
    private val showLocationAdminPopUp: (() -> Unit)?,
    private val showLocationAdminError: ((e: Throwable) -> Unit)?
): Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        val error = response.getError(AdminInfoResponse::class.java)

        when {
            error.isNullOrEmpty() -> onGetAdminInfoSuccess(response)
            error.isNotEmpty() -> onGetAdminInfoError(error)
            else -> showLocationAdminError?.invoke(Throwable())
        }
    }

    private fun onGetAdminInfoSuccess(response: GraphqlResponse) {
        response.getData<AdminInfoResponse>(AdminInfoResponse::class.java).let { adminInfo ->
            val data = adminInfo.response.data.firstOrNull()
            val isShopOwner = data?.detail?.roleType?.isShopOwner == true
            val isLocationAdmin = data?.detail?.roleType?.isLocationAdmin == true
            val isShopAdmin = data?.detail?.roleType?.isShopAdmin == true
            val isMultiLocationShop = data?.locations.orEmpty().count() > 1

            userSession.apply {
                setIsShopOwner(isShopOwner)
                setIsLocationAdmin(isLocationAdmin)
                setIsShopAdmin(isShopAdmin)
                setIsMultiLocationShop(isMultiLocationShop)
            }

            if (isLocationAdmin) {
                showLocationAdminPopUp?.invoke()
            } else {
                onSuccessGetUserProfile.invoke()
            }
        }
    }

    private fun onGetAdminInfoError(error: MutableList<GraphqlError>) {
        val message = error.firstOrNull()?.message.orEmpty()
        showLocationAdminError?.invoke(MessageErrorException(message))
    }

    override fun onCompleted() { }

    override fun onError(e: Throwable) {
        showLocationAdminError?.invoke(e)
    }
}