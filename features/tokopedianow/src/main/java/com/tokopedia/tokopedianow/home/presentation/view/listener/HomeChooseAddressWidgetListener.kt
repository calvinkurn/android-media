package com.tokopedia.tokopedianow.home.presentation.view.listener

import androidx.fragment.app.Fragment
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HomeChooseAddressWidgetListener(
    private val tokoNowView: TokoNowView,
    private val chooseAddressWidget: ChooseAddressWidget,
    private val chooseAddressListener: TokoNowChooseAddressWidgetListener?
) : ChooseAddressWidget.ChooseAddressWidgetListener {
    override fun onLocalizingAddressUpdatedFromWidget() {
        chooseAddressWidget.updateWidget()
        tokoNowView.refreshLayoutPage()
    }

    override fun onLocalizingAddressServerDown() {
        chooseAddressListener?.onChooseAddressWidgetRemoved()
    }

    override fun onClickChooseAddressTokoNowTracker() {
        chooseAddressListener?.onClickChooseAddressWidgetTracker()
    }

    override fun needToTrackTokoNow(): Boolean = true

    override fun getLocalizingAddressHostFragment(): Fragment = tokoNowView.getFragmentPage()

    override fun getLocalizingAddressHostSourceData(): String =
        TokoNowHomeFragment.SOURCE

    override fun getLocalizingAddressHostSourceTrackingData(): String =
        TokoNowHomeFragment.SOURCE_TRACKING

    override fun onLocalizingAddressUpdatedFromBackground() { /* to do : nothing */
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) { /* to do : nothing */
    }

    override fun onLocalizingAddressLoginSuccess() { /* to do : nothing */
    }

    override fun isFromTokonowPage(): Boolean {
        return true
    }

    override fun onChangeTextColor(): Int {
        return unifyprinciplesR.color.Unify_Static_White
    }
}
