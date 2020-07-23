package com.tokopedia.home.account.revamp.domain.data.mapper

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.home.account.AccountConstants.VccStatus
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.util.StaticBuyerModelGenerator.Companion.getModel
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.home.account.revamp.domain.data.model.AccountModel
import com.tokopedia.navigation_common.model.VccUserStatus
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import rx.functions.Func1
import javax.inject.Inject

class BuyerAccountMapper @Inject constructor(
        private val context: Context,
        private val remoteConfig: RemoteConfig,
        private val userSession: UserSession
): Func1<AccountModel, BuyerViewModel> {

    private val OVO = "OVO"
    private val OVO_PAY_LATER = "OVO PayLater"
    private val LABEL_ELIGIBLE = "Aktifkan"
    private val LABEL_HOLD = "Sedang Diproses"
    private val LABEL_BLOCKED = "Layanan Terblokir"
    private val LABEL_DEACTIVATED = "Dinonaktifkan"
    private val LABEL_KYC_PENDING = "Selesaikan Pengajuan Aplikasimu"

    val items: ArrayList<ParcelableViewModel<*>> = ArrayList()

    override fun call(t: AccountModel): BuyerViewModel {
        return getBuyerModel(t)
    }

    private fun getBuyerModel(accountModel: AccountModel) : BuyerViewModel {
        items.clear()

        val model = BuyerViewModel()
        items.add(getBuyerProfile(accountModel))
        items.add(getTokopediaPayModel(accountModel))
        items.addAll(getModel(context, accountModel, remoteConfig))
        model.items = items

        return model
    }

    //SET BUYER PROFILE
    private fun getBuyerProfile(accountModel: AccountModel): BuyerCardViewModel {
        val buyerCardViewModel = BuyerCardViewModel()

        buyerCardViewModel.userId = accountModel.profile.userId
        buyerCardViewModel.name = accountModel.profile.fullName
        buyerCardViewModel.tokopoint = accountModel.tokopoints.status.points.rewardStr
        buyerCardViewModel.eggImageUrl = accountModel.tokopoints.status.tier.imageUrl
        buyerCardViewModel.coupons = accountModel.tokopointsSumCoupon.sumCouponStr
        buyerCardViewModel.tokomember = accountModel.membershipSumUserCard.sumUserCardStr
        buyerCardViewModel.imageUrl = accountModel.profile.profilePicture
        buyerCardViewModel.progress = accountModel.userProfileCompletion.completionScore
        buyerCardViewModel.isAffiliate = accountModel.isAffiliate

        userSession.setHasPassword(accountModel.userProfileCompletion.isCreatedPassword)

        return buyerCardViewModel
    }

    //SET BUYER PAY
    private fun getTokopediaPayModel(accountModel: AccountModel): TokopediaPayViewModel {
        val tokopediaPayViewModel = TokopediaPayViewModel()

        tokopediaPayViewModel.isLinked = accountModel.wallet.isLinked
        tokopediaPayViewModel.walletType = accountModel.wallet.walletType

        val cdnUrl = remoteConfig.getString(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL)

        if(accountModel.wallet.walletType == OVO) {
            setOvoWalletType(tokopediaPayViewModel, accountModel, cdnUrl)
        } else {
            setWalletNonOvoType(tokopediaPayViewModel, accountModel, cdnUrl)
        }

        tokopediaPayViewModel.iconUrlRight = cdnUrl + AccountHomeUrl.ImageUrl.SALDO_IMG
        tokopediaPayViewModel.labelRight = context.getString(R.string.label_tokopedia_pay_deposit)
        tokopediaPayViewModel.isRightSaldo = true

        tokopediaPayViewModel.amountRight = CurrencyFormatUtil.convertPriceValueToIdrFormat(accountModel.saldo.depositLong, true)
        tokopediaPayViewModel.applinkRight = ApplinkConstInternalGlobal.SALDO_DEPOSIT

        setVCCBuyer(tokopediaPayViewModel, accountModel)

        return tokopediaPayViewModel
    }

    private fun setOvoWalletType(tokopediaPayViewModel: TokopediaPayViewModel, accountModel: AccountModel, cdnUrl: String) {
        tokopediaPayViewModel.iconUrlLeft = cdnUrl + AccountHomeUrl.ImageUrl.OVO_IMG

        if(!accountModel.wallet.isLinked) {
            if (accountModel.wallet.amountPendingCashback > 0) {
                tokopediaPayViewModel.labelLeft = "(+" + accountModel.wallet.pendingCashback + ")"
            } else {
                tokopediaPayViewModel.labelLeft = accountModel.wallet.text
            }

            tokopediaPayViewModel.amountLeft = accountModel.wallet.action.text
            tokopediaPayViewModel.applinkLeft = accountModel.wallet.action.applink

        } else {
            tokopediaPayViewModel.labelLeft = "Points " + accountModel.wallet.pointBalance
            tokopediaPayViewModel.amountLeft = accountModel.wallet.cashBalance
            tokopediaPayViewModel.applinkLeft = accountModel.wallet.applink
        }
    }

    private fun setWalletNonOvoType(tokopediaPayViewModel: TokopediaPayViewModel, accountModel: AccountModel, cdnUrl: String) {
        tokopediaPayViewModel.iconUrlLeft = cdnUrl + AccountHomeUrl.ImageUrl.TOKOCASH_IMG

        if (!accountModel.wallet.isLinked) {
            tokopediaPayViewModel.labelLeft = accountModel.wallet.text
            tokopediaPayViewModel.amountLeft = accountModel.wallet.action.text
            tokopediaPayViewModel.applinkLeft = accountModel.wallet.action.applink
        } else {
            tokopediaPayViewModel.labelLeft = accountModel.wallet.text
            tokopediaPayViewModel.amountLeft = accountModel.wallet.balance
            tokopediaPayViewModel.applinkLeft = accountModel.wallet.applink
        }
    }

    private fun setVCCBuyer(tokopediaPayViewModel: TokopediaPayViewModel, accountModel: AccountModel) {
        if (accountModel.vccUserStatus.title.equals(OVO_PAY_LATER, ignoreCase = true)) {

            val vccUserStatus = accountModel.vccUserStatus

            tokopediaPayViewModel.iconUrlCentre = vccUserStatus.icon
            tokopediaPayViewModel.applinkCentre = vccUserStatus.redirectionUrl
            tokopediaPayViewModel.amountCentre = accountModel.vccUserStatus.body

            setVCCBasedOnStatus(tokopediaPayViewModel, vccUserStatus)

        } else {
            tokopediaPayViewModel.bsDataCentre = null
        }
    }

    private fun setVCCBasedOnStatus(tokopediaPayViewModel: TokopediaPayViewModel, vccUserStatus: VccUserStatus) {
        val tokopediaPayBSModel = TokopediaPayBSModel()
        when (vccUserStatus.status) {
            VccStatus.ELIGIBLE -> {
                tokopediaPayViewModel.amountCentre = vccUserStatus.title
                tokopediaPayViewModel.labelCentre = LABEL_ELIGIBLE
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.HOLD -> {
                tokopediaPayViewModel.amountCentre = vccUserStatus.title
                tokopediaPayViewModel.labelCentre = LABEL_HOLD
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.ACTIVE -> {
                tokopediaPayViewModel.labelCentre = vccUserStatus.title
                val oplLimit: String? = try {
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(vccUserStatus.body.toLong(), true)
                } catch (e: Exception) {
                    vccUserStatus.body
                }
                tokopediaPayViewModel.amountCentre = oplLimit
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.REJECTED -> {
                tokopediaPayViewModel.labelCentre = vccUserStatus.title
                tokopediaPayViewModel.bsDataCentre = null
            }
            VccStatus.BLOCKED -> {
                tokopediaPayViewModel.amountCentre = vccUserStatus.title
                tokopediaPayViewModel.labelCentre = LABEL_BLOCKED
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.DEACTIVATED -> {
                tokopediaPayViewModel.amountCentre = vccUserStatus.title
                tokopediaPayViewModel.labelCentre = LABEL_DEACTIVATED
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.KYC_PENDING -> {
                tokopediaPayViewModel.amountCentre = vccUserStatus.title
                tokopediaPayViewModel.labelCentre = LABEL_KYC_PENDING
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
        }
    }
}