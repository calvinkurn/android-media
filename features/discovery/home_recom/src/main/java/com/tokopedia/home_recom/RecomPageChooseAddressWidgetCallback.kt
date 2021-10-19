package com.tokopedia.home_recom

import android.content.Context
import androidx.fragment.app.Fragment
import com.tokopedia.home_recom.listener.RecomPageListener
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget

/**
 * Created by yfsx on 01/09/21.
 */
class RecomPageChooseAddressWidgetCallback(
        val context: Context?,
        val listener: RecomPageListener,
        val fragment: Fragment
) : ChooseAddressWidget.ChooseAddressWidgetListener {
    companion object {
        const val LOCALIZING_HOST_FRAGMENT = "recom infinite tokonow"
        const val LOCALIZING_HOST_TRACKING_DATA = "recom infinite tokonow page"
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
        return LOCALIZING_HOST_FRAGMENT
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return LOCALIZING_HOST_TRACKING_DATA
    }

    override fun onLocalizingAddressLoginSuccess() {

    }

    override fun onChangeTextColor(): Int {
        return com.tokopedia.unifyprinciples.R.color.Unify_Static_White
    }
}