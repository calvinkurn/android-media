package com.tokopedia.tokopedianow.common.util

import android.content.Context
import android.net.Uri
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils

object TokoNowSwitcherUtil {
    private const val PARAM_TOKONOW_REFRESH = "tokonow_refresh"

    fun switchService(context: Context, param: String, onRefreshPage: (LocalCacheModel) -> Unit, onRedirectPage: (Context) -> Unit) {
        val gridParams = Uri.parse("?${param}")
        val tokoNowRefresh = gridParams.getQueryParameter(PARAM_TOKONOW_REFRESH).toBoolean()
        if(tokoNowRefresh) {
            onRefreshPage(ChooseAddressUtils.getLocalizingAddressData(context))
        } else {
            onRedirectPage(context)
        }
    }
}