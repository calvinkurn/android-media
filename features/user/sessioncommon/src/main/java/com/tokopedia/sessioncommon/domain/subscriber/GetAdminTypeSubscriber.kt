package com.tokopedia.sessioncommon.domain.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber

class GetAdminTypeSubscriber(
    private val userSession: UserSessionInterface,
    private val onSuccessGetAdminType: ((AdminData) -> Unit)?,
    private val onErrorGetAdminType: ((e: Throwable) -> Unit)?
): Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        val error = response.getError(AdminTypeResponse::class.java)

        when {
            error.isNullOrEmpty() -> onGetAdminTypeSuccess(response)
            error.isNotEmpty() -> onGetAdminTypeError(error)
            else -> onErrorGetAdminType?.invoke(Throwable())
        }
    }

    private fun onGetAdminTypeSuccess(response: GraphqlResponse) {
        response.getData<AdminTypeResponse>(AdminTypeResponse::class.java).let {
            val adminInfo = it.response
            val adminData = adminInfo.data
            val roleType = adminData.detail.roleType
            val isShopOwner = roleType.isShopOwner
            val isLocationAdmin = roleType.isLocationAdmin
            val isShopAdmin = roleType.isShopAdmin
            val isMultiLocationShop = adminInfo.isMultiLocationShop

            userSession.apply {
                setIsShopOwner(isShopOwner)
                setIsLocationAdmin(isLocationAdmin)
                setIsShopAdmin(isShopAdmin)
                setIsMultiLocationShop(isMultiLocationShop)
            }

            onSuccessGetAdminType?.invoke(adminData)
        }
    }

    private fun onGetAdminTypeError(error: MutableList<GraphqlError>) {
        val message = error.firstOrNull()?.message.orEmpty()
        onErrorGetAdminType?.invoke(MessageErrorException(message))
    }

    override fun onCompleted() { }

    override fun onError(e: Throwable) {
        onErrorGetAdminType?.invoke(e)
    }
}