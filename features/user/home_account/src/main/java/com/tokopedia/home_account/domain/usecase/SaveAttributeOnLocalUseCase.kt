package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SaveAttributeOnLocalUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    private val walletPref: WalletPref,
    dispatcher: CoroutineDispatchers,
) : CoroutineUseCase<UserAccountDataModel, Unit>(dispatcher.io) {

    /**
     * No-op, not a graphql use case
     * */
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: UserAccountDataModel) {
        params.let {
            if (it.debitInstant.data != null) {
                walletPref.saveDebitInstantUrl(it.debitInstant.data.redirectUrl)
            }
            userSession.setIsMSISDNVerified(it.profile.isPhoneVerified)
        }
    }

}
