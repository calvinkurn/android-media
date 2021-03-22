package com.tokopedia.home_account.view.mapper

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_DEFAULT
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_KUPON_SAYA
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_TOKOMEMBER
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_TOPQUEST
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class DataViewMapper @Inject constructor(
        private val userSession: UserSessionInterface,
        private val remoteConfig: RemoteConfig
) {

    fun mapToProfileDataView(context: Context?, accountDataModel: UserAccountDataModel): ProfileDataView {
        return ProfileDataView(
                name = accountDataModel.profile.fullName,
                phone = userSession.phoneNumber,
                email = userSession.email,
                avatar = accountDataModel.profile.profilePicture,
                members = mapToMemberDataView(accountDataModel),
                financial = mapSaldo(context, accountDataModel),
                backdrop = accountDataModel.tokopoints.status.tier.backgroundImgUrl
        )
    }

    fun mapToMemberDataView(accountDataModel: UserAccountDataModel): MemberDataView {
        return MemberDataView(
                title = accountDataModel.tokopoints.status?.tier?.nameDesc.toEmptyStringIfNull(),
                icon = accountDataModel.tokopoints.status?.tier?.imageUrl.toEmptyStringIfNull(),
                items = mapMemberItemDataView(accountDataModel.shortcutResponse)
        )
    }

    fun mapMemberItemDataView(shortcutResponse: ShortcutResponse): ArrayList<MemberItemDataView> {
        val items = arrayListOf<MemberItemDataView>()
        val shortcutGroupList = shortcutResponse.tokopointsShortcutList.shortcutGroupList
        if(shortcutGroupList.isNotEmpty()) {
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

    fun mapToFinancialData(context: Context?, wallet: WalletModel): List<CommonDataView> {
        val cdnUrl = remoteConfig.getString(AccountConstants.Url.KEY_IMAGE_HOST, AccountConstants.Url.CDN_URL)

        val items = arrayListOf<CommonDataView>()
        if(wallet.walletType != null && wallet.walletType == OVO) {
            if(!wallet.isLinked) {
                val body =  if (wallet.amountPendingCashback > 0) {
                    "(+" + wallet.pendingCashback + ")"
                } else {
                    wallet.text.toEmptyStringIfNull()
                }

                val item = CommonDataView(
                        id = AccountConstants.SettingCode.SETTING_OVO,
                        title = wallet.action?.text.toEmptyStringIfNull(),
                        body = body,
                        urlIcon = cdnUrl + AccountConstants.Url.OVO_IMG,
                        applink = wallet.action?.applink.toEmptyStringIfNull(),
                        icon = R.drawable.ic_account_ovo
                )
                items.add(item)
            } else {
                val item = CommonDataView(
                        title = wallet.cashBalance.toEmptyStringIfNull(),
                        body = getString(context, R.string.account_title_points_item) + " " + wallet.pointBalance.toEmptyStringIfNull(),
                        type = CommonViewHolder.TYPE_DEFAULT,
                        applink = wallet.applink.toEmptyStringIfNull(),
                        icon = R.drawable.ic_account_ovo
                )
                items.add(item)
            }
        }
        return items
    }

    fun mapSaldo(context: Context?, accountDataModel: UserAccountDataModel): SettingDataView {
        val dataView = SettingDataView(getString(context, R.string.account_title_saldo_section))
        val saldoItem = CommonDataView(
                id = AccountConstants.SettingCode.SETTING_SALDO,
                title = CurrencyFormatUtil.convertPriceValueToIdrFormat(accountDataModel.saldo.deposit, false),
                body = getString(context, R.string.account_title_saldo_item),
                type = CommonViewHolder.TYPE_DEFAULT,
                icon = R.drawable.ic_account_balance,
                applink = ApplinkConstInternalGlobal.SALDO_DEPOSIT
        )
        dataView.items.add(saldoItem)
        return dataView
    }

    private fun getString(context: Context?, stringResource: Int): String {
       return context?.getString(stringResource).toEmptyStringIfNull()
    }

    companion object {
        private val OVO = "OVO"
        private val TOKOMEMBER = "TokoMember"
        private val TOPQUEST = "TopQuest"
        private val KUPON_SAYA = "Kupon Saya"
    }
}