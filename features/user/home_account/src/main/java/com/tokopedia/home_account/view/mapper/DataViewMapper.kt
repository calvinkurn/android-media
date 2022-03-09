package com.tokopedia.home_account.view.mapper

import com.tokopedia.home_account.data.model.MemberItemDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_DEFAULT
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_KUPON_SAYA
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_TOKOMEMBER
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_TOPQUEST
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.toBlocking
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class DataViewMapper @Inject constructor(
        private val userSession: UserSessionInterface,
        private val userSessionDataStore: UserSessionDataStore
) {

    fun mapToProfileDataView(accountDataModel: UserAccountDataModel, isEnableLinkAccount: Boolean = false): ProfileDataView {
        var linkStatus = false
        var isShowLinkAccount = false

        if (accountDataModel.linkStatus.linkStatus.isNotEmpty()) {
            linkStatus = accountDataModel.linkStatus.linkStatus[0].status == "linked"
        }

        if(isEnableLinkAccount || linkStatus) {
            isShowLinkAccount = true
        }

        return ProfileDataView(
            name = accountDataModel.profile.fullName,
            phone = userSessionDataStore.getPhoneNumber().toBlocking(),
            email = userSessionDataStore.getEmail().toBlocking(),
            avatar = accountDataModel.profile.profilePicture,
            isLinked = linkStatus,
            isShowLinkStatus = isShowLinkAccount
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
    }
}