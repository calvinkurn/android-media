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
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.navigation_common.model.VccUserStatus
import com.tokopedia.remoteconfig.RemoteConfig
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

    override fun call(t: AccountDataModel): BuyerViewModel {
        return getBuyerModel(t)
    }

    private fun getBuyerModel(accountDataModel: AccountDataModel) : BuyerViewModel {
        val items: ArrayList<ParcelableViewModel<*>> = ArrayList()

        val model = BuyerViewModel()
        items.add(getBuyerProfile(accountDataModel))
        items.add(getTokopediaPayModel(accountDataModel))
        items.addAll(getModel(context, accountDataModel, remoteConfig))
        model.items = items

        return model
    }

    //SET BUYER PROFILE
    private fun getBuyerProfile(accountDataModel: AccountDataModel): BuyerCardViewModel {
        val buyerCardViewModel = BuyerCardViewModel()

        buyerCardViewModel.userId = accountDataModel.profile.userId
        buyerCardViewModel.name = accountDataModel.profile.fullName
        buyerCardViewModel.tokopoint = accountDataModel.tokopoints.status.points.rewardStr
        buyerCardViewModel.eggImageUrl = accountDataModel.tokopoints.status.tier.imageUrl
        buyerCardViewModel.coupons = accountDataModel.tokopointsSumCoupon.sumCouponStr
        buyerCardViewModel.tokomember = accountDataModel.membershipSumUserCard.sumUserCardStr
        buyerCardViewModel.imageUrl = accountDataModel.profile.profilePicture
        buyerCardViewModel.progress = accountDataModel.userProfileCompletion.completionScore
        buyerCardViewModel.isAffiliate = accountDataModel.isAffiliate

        userSession.setHasPassword(accountDataModel.userProfileCompletion.isCreatedPassword)

        return buyerCardViewModel
    }

    //SET BUYER PAY
    private fun getTokopediaPayModel(accountDataModel: AccountDataModel): TokopediaPayViewModel {
        val tokopediaPayViewModel = TokopediaPayViewModel()

        tokopediaPayViewModel.isLinked = accountDataModel.wallet.isLinked
        tokopediaPayViewModel.walletType = accountDataModel.wallet.walletType

        val cdnUrl = remoteConfig.getString(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL)

        if(accountDataModel.wallet.walletType == OVO) {
            setOvoWalletType(tokopediaPayViewModel, accountDataModel, cdnUrl)
        } else {
            setWalletNonOvoType(tokopediaPayViewModel, accountDataModel, cdnUrl)
        }

        tokopediaPayViewModel.iconUrlRight = cdnUrl + AccountHomeUrl.ImageUrl.SALDO_IMG
        tokopediaPayViewModel.labelRight = context.getString(R.string.label_tokopedia_pay_deposit)
        tokopediaPayViewModel.isRightSaldo = true

        tokopediaPayViewModel.amountRight = CurrencyFormatUtil.convertPriceValueToIdrFormat(accountDataModel.saldo.depositLong, true)
        tokopediaPayViewModel.applinkRight = ApplinkConstInternalGlobal.SALDO_DEPOSIT

        setVCCBuyer(tokopediaPayViewModel, accountDataModel)

        return tokopediaPayViewModel
    }

    private fun setOvoWalletType(tokopediaPayViewModel: TokopediaPayViewModel, accountDataModel: AccountDataModel, cdnUrl: String) {
        tokopediaPayViewModel.iconUrlLeft = cdnUrl + AccountHomeUrl.ImageUrl.OVO_IMG

        if(!accountDataModel.wallet.isLinked) {
            if (accountDataModel.wallet.amountPendingCashback > 0) {
                tokopediaPayViewModel.labelLeft = "(+" + accountDataModel.wallet.pendingCashback + ")"
            } else {
                tokopediaPayViewModel.labelLeft = accountDataModel.wallet.text
            }

            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.action.text
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.action.applink

        } else {
            tokopediaPayViewModel.labelLeft = "Points " + accountDataModel.wallet.pointBalance
            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.cashBalance
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.applink
        }
    }

    private fun setWalletNonOvoType(tokopediaPayViewModel: TokopediaPayViewModel, accountDataModel: AccountDataModel, cdnUrl: String) {
        tokopediaPayViewModel.iconUrlLeft = cdnUrl + AccountHomeUrl.ImageUrl.TOKOCASH_IMG

        if (!accountDataModel.wallet.isLinked) {
            tokopediaPayViewModel.labelLeft = accountDataModel.wallet.text
            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.action.text
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.action.applink
        } else {
            tokopediaPayViewModel.labelLeft = accountDataModel.wallet.text
            tokopediaPayViewModel.amountLeft = accountDataModel.wallet.balance
            tokopediaPayViewModel.applinkLeft = accountDataModel.wallet.applink
        }
    }

    private fun setVCCBuyer(tokopediaPayViewModel: TokopediaPayViewModel, accountDataModel: AccountDataModel) {
        if (accountDataModel.vccUserStatus.title.equals(OVO_PAY_LATER, ignoreCase = true)) {

            val vccUserStatus = accountDataModel.vccUserStatus

            tokopediaPayViewModel.iconUrlCentre = vccUserStatus.icon
            tokopediaPayViewModel.applinkCentre = vccUserStatus.redirectionUrl
            tokopediaPayViewModel.amountCentre = accountDataModel.vccUserStatus.body

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