package com.tokopedia.home.account.revamp.domain.data.mapper

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutGroupListItem
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutListItem
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse
import com.tokopedia.home.account.data.util.StaticBuyerModelGenerator.Companion.getModel
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import rx.functions.Func1
import javax.inject.Inject

class BuyerAccountMapper @Inject constructor(
        private val context: Context,
        private val remoteConfig: RemoteConfig,
        private val userSession: UserSession
): Func1<AccountDataModel, BuyerViewModel> {

    private val OVO = "OVO"
    private val OVO_PAY_LATER = "OVO PayLater"
    private val LABEL_ELIGIBLE = "Aktifkan"
    private val LABEL_HOLD = "Sedang Diproses"
    private val LABEL_BLOCKED = "Layanan Terblokir"
    private val LABEL_DEACTIVATED = "Dinonaktifkan"
    private val LABEL_KYC_PENDING = "Selesaikan Pengajuan Aplikasimu"
    private val UOH_AB_TEST_KEY = "uoh_android_v2"
    private val UOH_AB_TEST_VALUE = "uoh_android_v2"

    override fun call(t: AccountDataModel): BuyerViewModel {
        return getBuyerModel(t)
    }

    private fun getBuyerModel(accountDataModel: AccountDataModel) : BuyerViewModel {
        val items: ArrayList<ParcelableViewModel<*>> = ArrayList()

        val model = BuyerViewModel()
        items.add(getBuyerProfile(accountDataModel))
        items.add(getTokopediaPayModel(accountDataModel))
        items.addAll(getModel(context, accountDataModel, remoteConfig, useUoh()))
        model.items = items

        return model
    }

    //SET BUYER PROFILE
    private fun getBuyerProfile(accountDataModel: AccountDataModel): BuyerCardViewModel {
        val buyerCardViewModel = BuyerCardViewModel()

        buyerCardViewModel.apply {
            userId = accountDataModel.profile.userId.toEmptyStringIfNull()
            name = accountDataModel.profile.fullName.toEmptyStringIfNull()

            shopName = userSession.shopName.toEmptyStringIfNull()
            isHasShop = userSession.hasShop()
            roleName =
                    if (userSession.isShopOwner) {
                        null
                    } else {
                        accountDataModel.adminTypeText
                    }

            setShortcutResponse(accountDataModel, this)

            imageUrl = accountDataModel.profile.profilePicture.toEmptyStringIfNull()
            progress = accountDataModel.userProfileCompletion.completionScore
            isAffiliate = accountDataModel.isAffiliate
        }

        userSession.shopName = buyerCardViewModel.shopName
        userSession.setHasPassword(accountDataModel.userProfileCompletion.isCreatedPassword)

        return buyerCardViewModel
    }

    private fun setShortcutResponse(accountDataModel: AccountDataModel, buyerCardViewModel: BuyerCardViewModel) {
        val shortcutResponse: ShortcutResponse = accountDataModel.shortcutResponse
        val shortcutListItem: ShortcutGroupListItem
        val shortcutListItems = ArrayList<ShortcutListItem>()

        if (shortcutResponse.tokopointsShortcutList.shortcutGroupList.isNotEmpty()) {
            shortcutListItem = shortcutResponse.tokopointsShortcutList.shortcutGroupList[0]
            shortcutListItems.addAll(shortcutListItem.shortcutList)
        }

        if (shortcutListItems.size > 0) {
            buyerCardViewModel.tokopointSize = 1
            buyerCardViewModel.tokopointTitle = shortcutListItems[0].cta.text
            buyerCardViewModel.tokopoint = shortcutListItems[0].description
            buyerCardViewModel.tokopointAppplink = shortcutListItems[0].cta.appLink
            buyerCardViewModel.tokopointImageUrl = shortcutListItems[0].iconImageURL

            if (shortcutListItems.size > 1) {
                buyerCardViewModel.couponSize = 1
                buyerCardViewModel.couponTitle = shortcutListItems[1].cta.text
                buyerCardViewModel.coupons = shortcutListItems[1].description
                buyerCardViewModel.couponApplink = shortcutListItems[1].cta.appLink
                buyerCardViewModel.couponImageUrl = shortcutListItems[1].iconImageURL
            }

            if (shortcutListItems.size > 2) {
                buyerCardViewModel.tokomemberSize = 2
                buyerCardViewModel.tokomemberTitle = shortcutListItems[2].cta.text
                buyerCardViewModel.tokomember = shortcutListItems[2].description
                buyerCardViewModel.tokomemberApplink = shortcutListItems[2].cta.appLink
                buyerCardViewModel.tokomemberImageUrl = shortcutListItems[2].iconImageURL
            }
        }

        buyerCardViewModel.eggImageUrl = accountDataModel.tokopoints.status?.tier?.imageUrl.toEmptyStringIfNull()
        buyerCardViewModel.memberStatus = accountDataModel.tokopoints.status?.tier?.nameDesc.toEmptyStringIfNull()
        buyerCardViewModel.imageUrl = accountDataModel.profile.profilePicture.toEmptyStringIfNull()
        buyerCardViewModel.progress = accountDataModel.userProfileCompletion.completionScore
    }

    //SET BUYER PAY
    private fun getTokopediaPayModel(accountDataModel: AccountDataModel): TokopediaPayViewModel {
        val tokopediaPayViewModel = TokopediaPayViewModel()

        tokopediaPayViewModel.isLinked = accountDataModel.wallet.isLinked
        tokopediaPayViewModel.walletType = accountDataModel.wallet.walletType.toEmptyStringIfNull()

        val cdnUrl = remoteConfig.getString(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL)

        if(accountDataModel.wallet.walletType != null && accountDataModel.wallet.walletType == OVO) {
            setOvoWalletType(tokopediaPayViewModel, accountDataModel, cdnUrl)
        } else {
            setWalletNonOvoType(tokopediaPayViewModel, accountDataModel, cdnUrl)
        }

        tokopediaPayViewModel.iconUrlRight = cdnUrl + AccountHomeUrl.ImageUrl.SALDO_IMG
        tokopediaPayViewModel.labelRight = context.getString(R.string.label_tokopedia_pay_deposit)
        tokopediaPayViewModel.isRightSaldo = true

        tokopediaPayViewModel.amountRight = CurrencyFormatUtil.convertPriceValueToIdrFormat(accountDataModel.saldo.deposit, false)
        tokopediaPayViewModel.applinkRight = ApplinkConstInternalGlobal.SALDO_DEPOSIT

        return tokopediaPayViewModel
    }

    private fun setOvoWalletType(tokopediaPayViewModel: TokopediaPayViewModel, accountDataModel: AccountDataModel, cdnUrl: String) {
        tokopediaPayViewModel.iconUrlLeft = cdnUrl + AccountHomeUrl.ImageUrl.OVO_IMG

        if(!accountDataModel.wallet.isLinked) {
            if (accountDataModel.wallet.amountPendingCashback > 0) {
                tokopediaPayViewModel.labelLeft = "(+" + accountDataModel.wallet.pendingCashback + ")"
            } else {
                tokopediaPayViewModel.labelLeft = accountDataModel.wallet.text.toEmptyStringIfNull()
            }

            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.action?.text.toEmptyStringIfNull()
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.action?.applink.toEmptyStringIfNull()

        } else {
            tokopediaPayViewModel.labelLeft = "Points " + accountDataModel.wallet.pointBalance.toEmptyStringIfNull()
            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.cashBalance.toEmptyStringIfNull()
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.applink.toEmptyStringIfNull()
        }
    }

    private fun setWalletNonOvoType(tokopediaPayViewModel: TokopediaPayViewModel, accountDataModel: AccountDataModel, cdnUrl: String) {
        tokopediaPayViewModel.iconUrlLeft = cdnUrl + AccountHomeUrl.ImageUrl.TOKOCASH_IMG

        if (!accountDataModel.wallet.isLinked) {
            tokopediaPayViewModel.labelLeft = accountDataModel.wallet.text.toEmptyStringIfNull()
            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.action?.text.toEmptyStringIfNull()
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.action?.applink.toEmptyStringIfNull()
        } else {
            tokopediaPayViewModel.labelLeft = accountDataModel.wallet.text.toEmptyStringIfNull()
            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.balance.toEmptyStringIfNull()
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.applink.toEmptyStringIfNull()
        }
    }

    private fun useUoh(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(UOH_AB_TEST_KEY, "")

            val remoteConfigFirebase: Boolean = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_UOH)
            return (remoteConfigRollenceValue == UOH_AB_TEST_VALUE && remoteConfigFirebase)

        } catch (e: Exception) {
            false
        }
    }
}