package com.tokopedia.home_account.view.mapper

import android.util.Log
import com.tokopedia.home_account.data.model.MemberItemDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_DEFAULT
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_KUPON_SAYA
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_TOKOMEMBER
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_TOPQUEST
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.toBlocking
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class DataViewMapper @Inject constructor(
        private val userSession: UserSessionInterface,
        private val userSessionDataStore: Lazy<UserSessionDataStore>) {

    fun mapToProfileDataView(accountDataModel: UserAccountDataModel): ProfileDataView {
        var linkStatus = false
        val isShowLinkAccount = true

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

    private fun getEmailFromDataStore(): String {
        return try {
            var email = userSessionDataStore.get().getEmail().toBlocking().ifEmpty { userSession.email }
            if(email != userSession.email) {
                email = userSession.email
                logDataStoreError("email", DIFFERENT_EXCEPTION)
            }
            email
        } catch (e: Exception) {
            logDataStoreError("email", e)
            userSession.email
        }
    }

    private fun getPhoneNumberFromDataStore(): String {
        return try {
            var phone = userSessionDataStore.get().getPhoneNumber().toBlocking().ifEmpty { userSession.phoneNumber  }
            if(phone != userSession.phoneNumber) {
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
            Priority.P2, DataStoreMigrationWorker.USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "error_access_field",
                "field_name" to field,
                "error" to Log.getStackTraceString(e).take(LIMIT_STACKTRACE),
            )
        )
    }

    fun mapMemberItemDataView(shortcutResponse: ShortcutResponse): ArrayList<MemberItemDataView> {
        val items = arrayListOf<MemberItemDataView>()
        val shortcutGroupList = shortcutResponse.tokopointsShortcutList.shortcutGroupList
        if (shortcutGroupList.isNotEmpty()) {
            val shortcutList = shortcutResponse.tokopointsShortcutList.shortcutGroupList[0].shortcutList
            if (shortcutList.isNotEmpty()) {
                shortcutList.forEach {
                    val type = when (it.cta.text) {
                        TOKOMEMBER -> TYPE_TOKOMEMBER
                        TOPQUEST -> TYPE_TOPQUEST
                        KUPON_SAYA -> TYPE_KUPON_SAYA
                        else -> TYPE_DEFAULT
                    }
                    items.add(
                            MemberItemDataView(
                                    title = it.description,
                                    subtitle = it.cta.text,
                                    icon = it.iconImageURL,
                                    applink = it.cta.appLink,
                                    type = type
                            )
                    )
                }
            }
        }

        return items
    }

    companion object {
        private val TOKOMEMBER = "TokoMember"
        private val TOPQUEST = "TopQuest"
        private val KUPON_SAYA = "Kupon Saya"
        const val LIMIT_STACKTRACE = 1000

        private val DIFFERENT_EXCEPTION = Throwable(message = "Value is different from User Session")
    }
}
