package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSwitcherViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class HomeSwitcherListener(
    private val context: Context,
    private val viewModel: TokoNowHomeViewModel
): HomeSwitcherViewHolder.HomeSwitcherListener {

    override fun onClickSwitcher() {
        val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(context)
        viewModel.switchService(localCacheModel)
    }
}