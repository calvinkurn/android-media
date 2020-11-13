package com.tokopedia.home_account.view.mapper

import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.navigation_common.model.TokopointsModel
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
                financial = mapToFinancialData(accountDataModel),
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

    fun mapMemberItemDataView(shortcutResponse: ShortcutResponse): List<MemberItemDataView> {
        return shortcutResponse.tokopointsShortcutList.shortcutGroupList.flatMap { it.shortcutList }.map {
            MemberItemDataView(
                    title = it.description,
                    subtitle = it.cta.text,
                    icon = it.iconImageURL,
                    applink = it.cta.appLink
            )
        }
    }

    fun mapToFinancialData(accountDataModel: UserAccountDataModel): SettingDataView {
        val cdnUrl = remoteConfig.getString(AccountConstants.Url.KEY_IMAGE_HOST, AccountConstants.Url.CDN_URL)

        val dataView = SettingDataView("Dana & Investasi")
        if(accountDataModel.wallet.walletType != null && accountDataModel.wallet.walletType == OVO) {
            if(!accountDataModel.wallet.isLinked) {
                val body =  if (accountDataModel.wallet.amountPendingCashback > 0) {
                    "(+" + accountDataModel.wallet.pendingCashback + ")"
                } else {
                    accountDataModel.wallet.text.toEmptyStringIfNull()
                }

                val item = CommonDataView(
                        title = accountDataModel.wallet.action?.text.toEmptyStringIfNull(),
                        body = body,
                        urlIcon = cdnUrl + AccountConstants.Url.OVO_IMG,
                        applink = accountDataModel.wallet.action?.applink.toEmptyStringIfNull(),
                        icon = R.drawable.ic_account_ovo
                )
                dataView.items.add(item)
            } else {
                val item = CommonDataView(
                    title = accountDataModel.wallet.cashBalance.toEmptyStringIfNull(),
                    body = "Points " + accountDataModel.wallet.pointBalance.toEmptyStringIfNull(),
                    type = CommonViewHolder.TYPE_DEFAULT,
                    applink = accountDataModel.wallet.applink.toEmptyStringIfNull(),
                    icon = R.drawable.ic_account_ovo
                )
                dataView.items.add(item)
            }

            val saldoItem = CommonDataView(
                    title = CurrencyFormatUtil.convertPriceValueToIdrFormat(accountDataModel.saldo.deposit, false),
                    body = "Saldo",
                    type = CommonViewHolder.TYPE_DEFAULT,
//                    urlIcon = cdnUrl + AccountConstants.Url.SALDO_IMG,
                    icon = R.drawable.ic_account_balance,
                    applink = ApplinkConstInternalGlobal.SALDO_DEPOSIT
            )
            val goldItem = CommonDataView(title = "Rp 20.000.000", body = "Emas", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_emas)
            dataView.items.add(saldoItem)
//            dataView.items.add(goldItem)
        }
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