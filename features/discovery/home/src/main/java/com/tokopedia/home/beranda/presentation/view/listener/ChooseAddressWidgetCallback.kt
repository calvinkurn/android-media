package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import androidx.fragment.app.Fragment
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget

/**
 * @author by devarafikry on 09/02/21
 */
class ChooseAddressWidgetCallback(
        val context: Context?,
        val homeCategoryListener: HomeCategoryListener,
        val fragment: Fragment
): ChooseAddressWidget.ChooseAddressWidgetListener {
    override fun onLocalizingAddressUpdatedFromWidget() {
        homeCategoryListener.onChooseAddressUpdated()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {

    }

    override fun onLocalizingAddressServerDown() {
        homeCategoryListener.onChooseAddressServerDown()
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {

    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return fragment
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return "home"
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return "home page"
    }

    override fun onLocalizingAddressLoginSuccess() {

    }
}
