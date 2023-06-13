package com.tokopedia.play.broadcaster.shorts.domain.manager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.orUnknown
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

    /**
     * Best Eligible Account Rules:
     * 1. If user has no account, return empty account
     * 2. Else if user only has 1 account, return the account
     * 3. Else, get account by preferred / last selected account
     * 4. Check account:
     *    a. Eligible -> return that user / shop account
     *    b. Not Eligible
     *          i. Switch account
     *          ii. Check account
     *              - Eligible -> return that user / shop account
     *              - Not eligible & prev was shop -> return shop account
     *              - Not eligible & prev was not shop -> return empty account
     */
    override suspend fun getBestEligibleAccount(
        accountList: List<ContentAccountUiModel>,
        preferredAccountType: String
    ): ContentAccountUiModel = withContext(dispatchers.io) {

        if (accountList.isEmpty()) return@withContext ContentAccountUiModel.Empty
        if (accountList.size == 1) return@withContext accountList.first()

        val lastSelectedAccountType = sharedPref.getLastSelectedAccountType()

        val selectedAccountType = when {
            preferredAccountType.isNotEmpty() -> preferredAccountType
            lastSelectedAccountType.isNotEmpty() -> lastSelectedAccountType
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
        } else if (account.isShop) {
            /**
             * Need to at least return shop account if there's no eligible account
             * to determine which ineligible bottom sheet is shown
             */
            account
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

        if(accountList.size == 1) return accountList.first()

        val switchAccountType = when (currentAccountType) {
            TYPE_USER -> TYPE_SHOP
            TYPE_SHOP -> TYPE_USER
            else -> ""
        }

        return accountList.firstOrNull { it.type == switchAccountType || switchAccountType.isEmpty() }.orUnknown()
    }

    private fun isAccountEligible(account: ContentAccountUiModel): Boolean {
        return !account.isUnknown && (account.isUser || (account.isShop && account.enable))
    }
}
