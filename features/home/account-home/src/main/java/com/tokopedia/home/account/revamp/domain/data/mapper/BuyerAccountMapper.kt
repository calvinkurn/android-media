package com.tokopedia.home.account.revamp.domain.data.mapper

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.home.account.AccountConstants.VccStatus
import com.tokopedia.home.account.AccountHomeUrl
import com.tokopedia.home.account.R
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutGroupListItem
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutListItem
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse
import com.tokopedia.home.account.data.util.StaticBuyerModelGenerator.Companion.getModel
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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

        buyerCardViewModel.apply {
            userId = if(accountDataModel.profile.userId != null) {
                accountDataModel.profile.userId
            } else {
                ""
            }
            name = if(accountDataModel.profile.fullName != null) {
                accountDataModel.profile.fullName
            } else {
                ""
            }

            setShortcutResponse(accountDataModel, this)

            imageUrl = if(accountDataModel.profile.profilePicture != null) {
                accountDataModel.profile.profilePicture
            } else {
                ""
            }
            progress = accountDataModel.userProfileCompletion.completionScore
            isAffiliate = accountDataModel.isAffiliate
        }

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

        buyerCardViewModel.eggImageUrl = if(accountDataModel.tokopoints.status?.tier?.imageUrl != null) {
            accountDataModel.tokopoints.status?.tier?.imageUrl
        } else {
            ""
        }
        buyerCardViewModel.memberStatus = if(accountDataModel.tokopoints.status?.tier?.nameDesc != null) {
            accountDataModel.tokopoints.status?.tier?.nameDesc
        } else {
            ""
        }
        buyerCardViewModel.imageUrl = if(accountDataModel.profile.profilePicture != null) {
            accountDataModel.profile.profilePicture
        } else {
            ""
        }
        buyerCardViewModel.progress = accountDataModel.userProfileCompletion.completionScore
    }

    //SET BUYER PAY
    private fun getTokopediaPayModel(accountDataModel: AccountDataModel): TokopediaPayViewModel {
        val tokopediaPayViewModel = TokopediaPayViewModel()

        tokopediaPayViewModel.isLinked = accountDataModel.wallet.isLinked
        tokopediaPayViewModel.walletType = if(accountDataModel.wallet.walletType != null) {
            accountDataModel.wallet.walletType
        } else {
            ""
        }

        val cdnUrl = remoteConfig.getString(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL)

        if(accountDataModel.wallet.walletType != null && accountDataModel.wallet.walletType == OVO) {
            setOvoWalletType(tokopediaPayViewModel, accountDataModel, cdnUrl)
        } else {
            setWalletNonOvoType(tokopediaPayViewModel, accountDataModel, cdnUrl)
        }

        tokopediaPayViewModel.iconUrlRight = cdnUrl + AccountHomeUrl.ImageUrl.SALDO_IMG
        tokopediaPayViewModel.labelRight = context.getString(R.string.label_tokopedia_pay_deposit)
        tokopediaPayViewModel.isRightSaldo = true

        tokopediaPayViewModel.amountRight = CurrencyFormatUtil.convertPriceValueToIdrFormat(accountDataModel.saldo.depositLong, false)
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
                tokopediaPayViewModel.labelLeft = if(accountDataModel.wallet.text != null) {
                    accountDataModel.wallet.text
                } else {
                    ""
                }
            }

            tokopediaPayViewModel.amountLeft = if(accountDataModel.wallet.action?.text != null) {
                accountDataModel.wallet.action?.text
            } else {
                ""
            }
            tokopediaPayViewModel.applinkLeft = if(accountDataModel.wallet.action?.applink != null) {
                accountDataModel.wallet.action?.applink
            } else {
                ""
            }

        } else {
            tokopediaPayViewModel.labelLeft = "Points " + if(accountDataModel.wallet.pointBalance != null) {
                accountDataModel.wallet.pointBalance
            } else {
                ""
            }
            tokopediaPayViewModel.amountLeft = if(accountDataModel.wallet.cashBalance != null) {
                accountDataModel.wallet.cashBalance
            } else {
                ""
            }
            tokopediaPayViewModel.applinkLeft = if(accountDataModel.wallet.applink != null) {
                accountDataModel.wallet.applink
            } else {
                ""
            }
        }
    }

    private fun setWalletNonOvoType(tokopediaPayViewModel: TokopediaPayViewModel, accountDataModel: AccountDataModel, cdnUrl: String) {
        tokopediaPayViewModel.iconUrlLeft = cdnUrl + AccountHomeUrl.ImageUrl.TOKOCASH_IMG

        if (!accountDataModel.wallet.isLinked) {
            tokopediaPayViewModel.labelLeft = if(accountDataModel.wallet.text != null) {
                accountDataModel.wallet.text
            } else {
                ""
            }
            tokopediaPayViewModel.amountLeft = if(accountDataModel.wallet.action?.text != null) {
                accountDataModel.wallet.action?.text
            } else {
                ""
            }
            tokopediaPayViewModel.applinkLeft = if(accountDataModel.wallet.action?.applink != null) {
                accountDataModel.wallet.action?.applink
            } else {
                ""
            }
        } else {
            tokopediaPayViewModel.labelLeft = if(accountDataModel.wallet.text != null) {
                accountDataModel.wallet.text
            } else {
                ""
            }
            tokopediaPayViewModel.amountLeft = if(accountDataModel.wallet.balance != null) {
                accountDataModel.wallet.balance
            } else {
                ""
            }
            tokopediaPayViewModel.applinkLeft = if(accountDataModel.wallet.applink != null) {
                accountDataModel.wallet.applink
            } else {
                ""
            }
        }
    }

    private fun setVCCBuyer(tokopediaPayViewModel: TokopediaPayViewModel, accountDataModel: AccountDataModel) {
        if (accountDataModel.vccUserStatus.title!= null && accountDataModel.vccUserStatus.title.equals(OVO_PAY_LATER, ignoreCase = true)) {

            val vccUserStatus = accountDataModel.vccUserStatus

            tokopediaPayViewModel.iconUrlCentre = if(vccUserStatus.icon != null) {
                vccUserStatus.icon
            } else {
                ""
            }
            tokopediaPayViewModel.applinkCentre = if(vccUserStatus.redirectionUrl != null) {
                vccUserStatus.redirectionUrl
            } else {
                ""
            }
            tokopediaPayViewModel.amountCentre = if(accountDataModel.vccUserStatus.body != null) {
                accountDataModel.vccUserStatus.body
            } else {
                ""
            }

            setVCCBasedOnStatus(tokopediaPayViewModel, vccUserStatus)

        } else {
            tokopediaPayViewModel.bsDataCentre = null
        }
    }

    private fun setVCCBasedOnStatus(tokopediaPayViewModel: TokopediaPayViewModel, vccUserStatus: VccUserStatus) {
        val tokopediaPayBSModel = TokopediaPayBSModel()
        val title = if(vccUserStatus.title != null) {
            vccUserStatus.title
        } else {
            ""
        }
        when (vccUserStatus.status) {
            VccStatus.ELIGIBLE -> {
                tokopediaPayViewModel.amountCentre = title
                tokopediaPayViewModel.labelCentre = LABEL_ELIGIBLE
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.HOLD -> {
                tokopediaPayViewModel.amountCentre = title
                tokopediaPayViewModel.labelCentre = LABEL_HOLD
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.ACTIVE -> {
                tokopediaPayViewModel.labelCentre = title
                val oplLimit: String? = try {
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(vccUserStatus.body.toLongOrZero(), true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    if(vccUserStatus.body != null) {
                        vccUserStatus.body
                    } else {
                        ""
                    }
                }
                tokopediaPayViewModel.amountCentre = oplLimit
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.REJECTED -> {
                tokopediaPayViewModel.labelCentre = title
                tokopediaPayViewModel.bsDataCentre = null
            }
            VccStatus.BLOCKED -> {
                tokopediaPayViewModel.amountCentre = title
                tokopediaPayViewModel.labelCentre = LABEL_BLOCKED
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.DEACTIVATED -> {
                tokopediaPayViewModel.amountCentre = title
                tokopediaPayViewModel.labelCentre = LABEL_DEACTIVATED
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
            VccStatus.KYC_PENDING -> {
                tokopediaPayViewModel.amountCentre = title
                tokopediaPayViewModel.labelCentre = LABEL_KYC_PENDING
                tokopediaPayViewModel.bsDataCentre = tokopediaPayBSModel
            }
        }
    }
}