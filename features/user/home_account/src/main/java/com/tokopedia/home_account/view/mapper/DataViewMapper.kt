package com.tokopedia.home_account.view.mapper

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.home_account.view.viewholder.FinancialItemViewHolder
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.net.URLEncoder
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
        if (shortcutGroupList.isNotEmpty()) {
            val shortcutList = shortcutResponse.tokopointsShortcutList.shortcutGroupList[0].shortcutList
            if (shortcutList.isNotEmpty()) {
                shortcutList.forEach {
                    items.add(
                            MemberItemDataView(
                                    title = it.description,
                                    subtitle = it.cta.text,
                                    icon = it.iconImageURL,
                                    applink = it.cta.appLink
                            )
                    )
                }
            }
        }

        return items
    }

    fun mapToFinancialData(context: Context?, wallet: WalletModel): CommonDataView {
        val cdnUrl = remoteConfig.getString(AccountConstants.Url.KEY_IMAGE_HOST, AccountConstants.Url.CDN_URL)

        var item = CommonDataView()
        if (wallet.walletType != null && wallet.walletType == OVO) {
            if (!wallet.isLinked) {
                val body = if (wallet.amountPendingCashback > 0) {
                    "(+" + wallet.pendingCashback + ")"
                } else {
                    wallet.text.toEmptyStringIfNull()
                }

                item = CommonDataView(
                        title = wallet.action?.text.toEmptyStringIfNull(),
                        body = body,
                        urlIcon = cdnUrl + AccountConstants.Url.OVO_IMG,
                        applink = wallet.action?.applink.toEmptyStringIfNull(),
                        icon = R.drawable.ic_account_ovo,
                        type = FinancialItemViewHolder.TYPE_OVO_TOKOPOINTS
                )
            } else {
                item = CommonDataView(
                        title = CurrencyFormatUtil.convertPriceValueToIdrFormat(wallet.rawCashBalance, false),
                        body = getString(context, R.string.account_title_points_item) + " " + wallet.pointBalance.toEmptyStringIfNull(),
                        applink = wallet.applink.toEmptyStringIfNull(),
                        icon = R.drawable.ic_account_ovo,
                        type = FinancialItemViewHolder.TYPE_OVO_TOKOPOINTS
                )
            }
        }
        return item
    }

    fun mapSaldo(context: Context?, balance: Balance): CommonDataView {
        return CommonDataView(
                title = CurrencyFormatUtil.convertPriceValueToIdrFormat(balance.buyerAll.toLong(), false),
                body = getString(context, R.string.account_title_saldo_item),
                type = FinancialItemViewHolder.TYPE_SALDO,
                icon = R.drawable.ic_account_balance,
                applink = ApplinkConstInternalGlobal.SALDO_DEPOSIT
        )
    }

    fun mapTokopoints(context: Context?, tokopointsDrawerList: TokopointsDrawerList): CommonDataView {
        val tokopoint: DrawerList? = tokopointsDrawerList.drawerList.find { it.type == TOKOPOINTS }
        return CommonDataView(
                title = tokopoint?.sectionContent?.get(0)?.textAttributes?.text ?: "",
                body = tokopoint?.sectionContent?.get(1)?.textAttributes?.text ?: "",
                type = FinancialItemViewHolder.TYPE_OVO_TOKOPOINTS,
                icon = R.drawable.ic_account_tokopoint,
                applink = tokopoint?.redirectAppLink ?: ""
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
    }
}