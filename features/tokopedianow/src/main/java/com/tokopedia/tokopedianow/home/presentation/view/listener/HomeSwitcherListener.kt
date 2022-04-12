package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSwitcherViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class HomeSwitcherListener(
    private val context: Context,
    private val viewModel: TokoNowHomeViewModel,
    private val userSession: UserSessionInterface,
    private val analytics: HomeAnalytics
): HomeSwitcherViewHolder.HomeSwitcherListener {

    override fun onClickSwitcher() {
        val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(context)
        val whIdDestination = localCacheModel.warehouses.findLast { it.service_type != localCacheModel.service_type }?.warehouse_id?.orZero().toString()

        viewModel.switchService(localCacheModel)
        analytics.sendClickSwitcherWidget(
            userId = userSession.userId,
            whIdOrigin = localCacheModel.warehouse_id,
            whIdDestination = whIdDestination,
            isNow15 = viewModel.targetServiceType(localCacheModel.service_type) != ServiceType.NOW_15M
        )
    }

    override fun onImpressSwitcher() {
        val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(context)
        val whIdDestination = localCacheModel.warehouses.findLast { it.service_type != localCacheModel.service_type }?.warehouse_id?.orZero().toString()
        analytics.sendImpressSwitcherWidget(
            userId = userSession.userId,
            whIdOrigin = localCacheModel.warehouse_id,
            whIdDestination = whIdDestination,
            isNow15 = viewModel.targetServiceType(localCacheModel.service_type) != ServiceType.NOW_15M
        )
    }
}