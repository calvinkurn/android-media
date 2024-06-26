package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import androidx.fragment.app.Fragment
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by devarafikry on 09/02/21
 */
class ChooseAddressWidgetCallback(
    val context: Context?,
    val homeCategoryListener: HomeCategoryListener,
    val fragment: Fragment,
    val homeThematicUtil: HomeThematicUtil,
) : ChooseAddressWidget.ChooseAddressWidgetListener {
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

    override fun onChangeTextColor(): Int {
        return homeThematicUtil.getThematicColorToken(unifyprinciplesR.color.Unify_NN1000)
    }

    override fun iconLocation(): Int {
        return IconUnify.LOCATION_FILLED
    }

    override fun iconLocationColor(): Int {
        return unifyprinciplesR.color.Unify_GN500
    }

    override fun onTokonowDataRefreshed() {
        homeCategoryListener.onChooseAddressUpdated()
    }

    override fun isNeedToRefreshTokonowData(): Boolean {
        return true
    }
}
