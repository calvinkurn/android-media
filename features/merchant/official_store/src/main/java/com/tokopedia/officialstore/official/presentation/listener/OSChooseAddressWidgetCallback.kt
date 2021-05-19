package com.tokopedia.officialstore.official.presentation.listener

import android.content.Context
import androidx.fragment.app.Fragment
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.officialstore.category.presentation.listener.OSContainerListener

/**
 * @author by devarafikry on 09/02/21
 */
class OSChooseAddressWidgetCallback(
        val context: Context?,
        val listener: OSContainerListener,
        val fragment: Fragment
): ChooseAddressWidget.ChooseAddressWidgetListener {

    companion object {
        const val sourcePageName = "OS"
        const val trackingPageName = "official store page"
    }
    override fun onLocalizingAddressUpdatedFromWidget() {
        listener.onChooseAddressUpdated()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {

    }

    override fun onLocalizingAddressServerDown() {
        listener.onChooseAddressServerDown()
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {

    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return fragment
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return sourcePageName
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return trackingPageName
    }

    override fun onLocalizingAddressLoginSuccess() {

    }
}
