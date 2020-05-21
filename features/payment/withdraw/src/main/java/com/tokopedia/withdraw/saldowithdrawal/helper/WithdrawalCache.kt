package com.tokopedia.withdraw.helper

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import javax.inject.Inject

class WithdrawalCache @Inject constructor(){

    fun saveRekeningPremiumWidgetClicked(context: Context?) {
        val localCacheHandler = LocalCacheHandler(context, SALDO_WITHDRAWAL_CACHE_FILE)
        localCacheHandler.putBoolean(KEY_PREMIUM_ACCOUNT_NEW_TAG, true)
        localCacheHandler.applyEditor()
    }

    fun isRekeningPremiumWidgetClicked(context: Context?): Boolean {
        val localCacheHandler = LocalCacheHandler(context, SALDO_WITHDRAWAL_CACHE_FILE)
        return localCacheHandler.getBoolean(KEY_PREMIUM_ACCOUNT_NEW_TAG, false)
    }

    companion object{
        private const val SALDO_WITHDRAWAL_CACHE_FILE = "saldo_withdrawal_cache_file"
        private const val KEY_PREMIUM_ACCOUNT_NEW_TAG = "swd_new_tag"

    }
}