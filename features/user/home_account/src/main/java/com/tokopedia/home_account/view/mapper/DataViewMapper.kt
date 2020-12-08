package com.tokopedia.home_account.view.mapper

import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
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

    fun mapToProfileDataView(accountDataModel: UserAccountDataModel): ProfileDataView {
        return ProfileDataView(
                name = accountDataModel.profile.fullName,
                phone = userSession.phoneNumber,
                email = userSession.email,
                avatar = accountDataModel.profile.profilePicture,
                members = mapToMemberDataView(accountDataModel),
                financial = mapSaldo(accountDataModel),
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
        if(shortcutResponse.tokopointsShortcutList.shortcutGroupList.isNotEmpty() && shortcutResponse.tokopointsShortcutList.shortcutGroupList[0].shortcutList.isNotEmpty()) {
            shortcutResponse.tokopointsShortcutList.shortcutGroupList[0].shortcutList.forEach {
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

        return items
    }

    fun mapToFinancialData(wallet: WalletModel): List<CommonDataView> {
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
                        body = "Points " + wallet.pointBalance.toEmptyStringIfNull(),
                        type = CommonViewHolder.TYPE_DEFAULT,
                        applink = wallet.applink.toEmptyStringIfNull(),
                        icon = R.drawable.ic_account_ovo
                )
                items.add(item)
            }
        }
        return items
    }

    fun mapSaldo(accountDataModel: UserAccountDataModel): SettingDataView {
        val dataView = SettingDataView("Dana & Investasi")
        val saldoItem = CommonDataView(
                title = CurrencyFormatUtil.convertPriceValueToIdrFormat(accountDataModel.saldo.deposit, false),
                body = "Saldo",
                type = CommonViewHolder.TYPE_DEFAULT,
                icon = R.drawable.ic_account_balance,
                applink = ApplinkConstInternalGlobal.SALDO_DEPOSIT
        )
        dataView.items.add(saldoItem)
        return dataView
    }

    private fun setOvoWalletItem(){

    }

    companion object {
        private val OVO = "OVO"
        private val OVO_PAY_LATER = "OVO PayLater"
        private val LABEL_ELIGIBLE = "Aktifkan"
        private val LABEL_HOLD = "Sedang Diproses"
        private val LABEL_BLOCKED = "Layanan Terblokir"
        private val LABEL_DEACTIVATED = "Dinonaktifkan"
        private val LABEL_KYC_PENDING = "Selesaikan Pengajuan Aplikasimu"
        private val UOH_AB_TEST_KEY = "uoh_android_v2"
        private val UOH_AB_TEST_VALUE = "uoh_android_v2"
    }
}