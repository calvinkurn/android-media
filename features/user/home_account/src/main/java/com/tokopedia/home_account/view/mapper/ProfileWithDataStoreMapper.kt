package com.tokopedia.home_account.view.mapper

import android.util.Log
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import dagger.Lazy
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProfileWithDataStoreMapper @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dataStore: Lazy<UserSessionDataStore>
) {

    suspend operator fun invoke(accountDataModel: UserAccountDataModel): ProfileDataView {
        var linkStatus = false
        //force this to false to support SCP Login, remove the account linking
        val isShowLinkAccount = false

        if (accountDataModel.linkStatus.linkStatus.isNotEmpty()) {
            linkStatus = accountDataModel.linkStatus.linkStatus[0].status == "linked"
        }

        return ProfileDataView(
            name = accountDataModel.profile.fullName,
            phone = getPhoneNumberFromDataStore(),
            email = getEmailFromDataStore(),
            avatar = accountDataModel.profile.profilePicture,
            isLinked = linkStatus,
            isShowLinkStatus = isShowLinkAccount,
            offerInterruptData = accountDataModel.offerInterrupt
        )
    }

    private suspend fun getEmailFromDataStore(): String {
        return try {
            var email = dataStore.get().getEmail().first().ifEmpty { userSession.email }
            if (email != userSession.email) {
                email = userSession.email
                logDataStoreError("email", DIFFERENT_EXCEPTION)
            }
            email
        } catch (e: Exception) {
            logDataStoreError("email", e)
            userSession.email
        }
    }

    private suspend fun getPhoneNumberFromDataStore(): String {
        return try {
            var phone = dataStore.get().getPhoneNumber().first().ifEmpty { userSession.phoneNumber }
            if (phone != userSession.phoneNumber) {
                phone = userSession.phoneNumber
                logDataStoreError("phoneNumber", DIFFERENT_EXCEPTION)
            }
            phone
        } catch (e: Exception) {
            logDataStoreError("phoneNumber", e)
            userSession.phoneNumber
        }
    }

    private fun logDataStoreError(field: String, e: Throwable) {
        ServerLogger.log(
            Priority.P2,
            DataStoreMigrationWorker.USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "error_access_field",
                "field_name" to field,
                "error" to Log.getStackTraceString(e).take(LIMIT_STACKTRACE)
            )
        )
    }

    companion object {
        const val LIMIT_STACKTRACE = 1000

        private val DIFFERENT_EXCEPTION =
            Throwable(message = "Value is different from User Session")
    }
}
