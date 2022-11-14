package com.tokopedia.play.broadcaster.shorts.domain.manager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.util.orUnknown
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 14, 2022
 */
class PlayShortsAccountManagerImpl @Inject constructor(
    private val sharedPref: HydraSharedPreferences,
    private val dispatchers: CoroutineDispatchers
) : PlayShortsAccountManager {

    override suspend fun getBestEligibleAccount(
        accountList: List<ContentAccountUiModel>,
        preferredAccountType: String
    ): ContentAccountUiModel = withContext(dispatchers.io) {
        val lastSelectedAccount = sharedPref.getLastSelectedAccount()

        val selectedAccountType = when {
            preferredAccountType.isNotEmpty() -> preferredAccountType
            lastSelectedAccount.isNotEmpty() -> lastSelectedAccount
            else -> ""
        }

        val account = accountList.firstOrNull { it.type == selectedAccountType || selectedAccountType.isEmpty() }.orUnknown()

        val finalAccount = if (isAccountEligible(account)) {
            account
        } else {
            switchAccount(accountList, account.type)
        }

        if (isAccountEligible(finalAccount)) {
            finalAccount
        } else {
            ContentAccountUiModel.Empty
        }
    }

    override fun isAllowChangeAccount(accountList: List<ContentAccountUiModel>): Boolean {
        return if (GlobalConfig.isSellerApp()) {
            false
        } else {
            accountList.size > 1
        }
    }

    override fun switchAccount(
        accountList: List<ContentAccountUiModel>,
        currentAccountType: String
    ): ContentAccountUiModel {
        val switchAccountType = when (currentAccountType) {
            TYPE_USER -> TYPE_SHOP
            TYPE_SHOP -> TYPE_USER
            else -> ""
        }

        return accountList.firstOrNull { it.type == switchAccountType }.orUnknown()
    }

    private fun isAccountEligible(account: ContentAccountUiModel): Boolean {
        return !account.isUnknown && (account.isUser || (account.isShop && account.hasAcceptTnc))
    }
}
