package com.tokopedia.logisticseller.common

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.logisticseller.common.LogisticSellerConst.UNIFY_TICKER_TYPE_ANNOUNCEMENT
import com.tokopedia.logisticseller.common.LogisticSellerConst.UNIFY_TICKER_TYPE_ERROR
import com.tokopedia.logisticseller.common.LogisticSellerConst.UNIFY_TICKER_TYPE_INFO
import com.tokopedia.logisticseller.common.LogisticSellerConst.UNIFY_TICKER_TYPE_WARNING
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * Created by irpan on 23/08/23.
 */
object Utils {

    fun Fragment?.updateShopActive() {
        this?.context?.let { UpdateShopActiveWorker.execute(it) }
    }

    fun Activity.updateShopActive() {
        UpdateShopActiveWorker.execute(this)
    }

    @JvmStatic
    fun showToasterError(message: String, view: View?) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, LogisticSellerConst.ACTION_OK)
        }
    }

    @JvmStatic
    fun mapStringTickerTypeToUnifyTickerType(typeString: String): Int {
        return when (typeString) {
            UNIFY_TICKER_TYPE_ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            UNIFY_TICKER_TYPE_INFO -> Ticker.TYPE_INFORMATION
            UNIFY_TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
            UNIFY_TICKER_TYPE_ERROR -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }
}
