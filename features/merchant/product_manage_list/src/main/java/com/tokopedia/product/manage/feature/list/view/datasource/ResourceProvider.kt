package com.tokopedia.product.manage.feature.list.view.datasource

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.manage.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context?) {
    private fun getString(@StringRes id: Int, vararg args: Any): String? {
        return try {
            context?.getString(id, *args).orEmpty()
        } catch (t: Throwable) {
            null
        }
    }

    fun getTickerMultiLocationTitle(): String {
        return getString(com.tokopedia.product.manage.common.R.string.product_manage_closable_stock_ticker_title).orEmpty()
    }

    fun getTickerMultiLocationDescription(): String {
        return getString(com.tokopedia.product.manage.common.R.string.product_manage_closable_stock_ticker_description).orEmpty()
    }

    fun getTickerGoPayCoinsCashbackInfoTitle(): String {
        return getString(R.string.product_manage_list_gopay_coins_cashback_info_ticker_title).orEmpty()
    }

    fun getTickerGoPayCoinsCashbackInfoDescription(): String {
        return getString(R.string.product_manage_list_gopay_coins_cashback_info_ticker_description).orEmpty()
    }
}