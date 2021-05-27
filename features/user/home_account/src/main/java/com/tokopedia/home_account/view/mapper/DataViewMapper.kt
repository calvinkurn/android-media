package com.tokopedia.home_account.view.mapper

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.Utils.formatIdrCurrency
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_DEFAULT
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_KUPON_SAYA
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_TOKOMEMBER
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_TOPQUEST
import com.tokopedia.home_account.view.viewholder.FinancialItemViewHolder
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
        private val userSession: UserSessionInterface
) {

    fun mapToProfileDataView(accountDataModel: UserAccountDataModel): ProfileDataView {
        return ProfileDataView(
                name = accountDataModel.profile.fullName,
                phone = userSession.phoneNumber,
                email = userSession.email,
                avatar = accountDataModel.profile.profilePicture,
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

    fun mapToFinancialData(context: Context?, wallet: WalletModel): CommonDataView {
        var item = CommonDataView()
        if (wallet.walletType != null && wallet.walletType == OVO) {
            if (!wallet.isLinked) {
                val body = if (wallet.amountPendingCashback > 0) {
                    "(+" + wallet.pendingCashback + ")"
                } else {
                    wallet.text.toEmptyStringIfNull()
                }

                item = CommonDataView(
                        id = AccountConstants.SettingCode.SETTING_OVO,
                        title = wallet.action?.text.toEmptyStringIfNull(),
                        body = body,
                        urlIcon = AccountConstants.Url.OVO_ICON,
                        applink = wallet.action?.applink.toEmptyStringIfNull(),
                        icon = R.drawable.ic_account_ovo,
                        type = FinancialItemViewHolder.TYPE_OVO_TOKOPOINTS
                )
            } else {
                item = CommonDataView(
                        title = renderOvoBalance(wallet),
                        body = getString(context, R.string.account_title_points_item) + " " + wallet.pointBalance.toEmptyStringIfNull(),
                        applink = wallet.applink.toEmptyStringIfNull(),
                        urlIcon = AccountConstants.Url.OVO_ICON,
                        type = FinancialItemViewHolder.TYPE_OVO_TOKOPOINTS
                )
            }
        }
        return item
    }

    private fun renderOvoBalance(wallet: WalletModel): String {
        return wallet.cashBalance.toEmptyStringIfNull()
    }

    fun mapSaldo(context: Context?, balance: Balance): CommonDataView {
        val totalBalance = balance.buyerUsable + balance.sellerUsable
        return CommonDataView(
                id = AccountConstants.SettingCode.SETTING_SALDO,
                title = formatIdrCurrency(totalBalance.toLong()),
                body = getString(context, R.string.account_title_saldo_item),
                type = FinancialItemViewHolder.TYPE_SALDO,
                urlIcon = AccountConstants.Url.SALDO_ICON,
                applink = ApplinkConstInternalGlobal.SALDO_DEPOSIT
        )
    }

    fun mapTokopoints(tokopointsDrawerList: TokopointsDrawerList): CommonDataView {
        val tokopoint: DrawerList? = tokopointsDrawerList.drawerList.find { it.type == TOKOPOINTS }
        val title = tokopoint?.sectionContent?.get(0)
        val body = tokopoint?.sectionContent?.get(1)
        return CommonDataView(
                id = AccountConstants.SettingCode.SETTING_TOKOPOINTS,
                title = title?.textAttributes?.text ?: "",
                body = body?.textAttributes?.text ?: "",
                type = FinancialItemViewHolder.TYPE_OVO_TOKOPOINTS,
                urlIcon = AccountConstants.Url.TOKOPOINTS_ICON,
                applink = tokopoint?.redirectAppLink ?: "",
                isTitleBold = title?.textAttributes?.isBold ?: false,
                isBodyBold = body?.textAttributes?.isBold ?: false,
                titleColor = title?.textAttributes?.color ?: "",
                bodyColor = body?.textAttributes?.color ?: ""
        )
    }

    fun mapError(context: Context?, type: Int, icon: Int): CommonDataView {
        return CommonDataView(
                title = getString(context, R.string.new_home_account_finance_failed_to_load),
                body = getString(context, R.string.new_home_account_finance_try_again),
                type = type,
                icon = icon
        )
    }

    private fun getString(context: Context?, stringResource: Int): String {
        return context?.getString(stringResource).toEmptyStringIfNull()
    }

    companion object {
        private val OVO = "OVO"
        private val TOKOPOINTS = "TokoPoints"
        private val TOKOMEMBER = "TokoMember"
        private val TOPQUEST = "TopQuest"
        private val KUPON_SAYA = "Kupon Saya"
    }
}