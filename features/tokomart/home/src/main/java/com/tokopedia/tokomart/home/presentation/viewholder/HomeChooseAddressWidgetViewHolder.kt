package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokomart.home.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeChooseAddressWidgetUiModel

class HomeChooseAddressWidgetViewHolder(
        itemView: View,
        private val fragment: Fragment
): AbstractViewHolder<HomeChooseAddressWidgetUiModel>(itemView), ChooseAddressWidget.ChooseAddressWidgetListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_choose_address_widget

        const val ENABLE_CHOOSE_ADDRESS_WIDGET = "android_shop_page_enable_choose_address_widget_on_shop_page_header"
        const val SOURCE = "tokomart page"
    }

    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var remoteConfig: RemoteConfig? = null
    private var coachMark: CoachMark2? = null

    init {
        setupRemoteConfig()
    }

    override fun bind(element: HomeChooseAddressWidgetUiModel?) {
        remoteConfig?.run {
            element?.isMyShop?.let {
                setupChooseAddressWidget(this, it)
            }
        }
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        chooseAddressWidget?.updateWidget()
    }

    override fun onLocalizingAddressServerDown() {
        chooseAddressWidget?.hide()
    }

    override fun onLocalizingAddressUpdatedFromBackground() { /* to do : nothing */ }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) { /* to do : nothing */ }

    override fun onLocalizingAddressLoginSuccess() { /* to do : refresh page */ }

    override fun getLocalizingAddressHostFragment(): Fragment = fragment

    override fun getLocalizingAddressHostSourceData(): String = SOURCE

    override fun getLocalizingAddressHostSourceTrackingData(): String = SOURCE

    private fun setupRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(itemView.context)
    }

    private fun setupChooseAddressWidget(remoteConfig: RemoteConfig, isMyShop: Boolean) {
        chooseAddressWidget = itemView.findViewById(R.id.choose_address_widget)
        chooseAddressWidget?.let { widget ->
            val isRollOutUser = ChooseAddressUtils.isRollOutUser(itemView.context)
            val isRemoteConfigChooseAddressWidgetEnabled = remoteConfig.getBoolean(
                    ENABLE_CHOOSE_ADDRESS_WIDGET,
                    true
            )
            if (isRollOutUser && isRemoteConfigChooseAddressWidgetEnabled && !isMyShop) {
                widget.show()
                widget.bindChooseAddress(this)
            } else {
                widget.hide()
            }
        }
        showCoachMark()
    }

    private fun showCoachMark(){
        val coachMarkList = arrayListOf<CoachMark2Item>().apply {
            getChooseAddressWidgetCoachMarkItem()?.let {
                add(it)
            }
        }
        if (!coachMarkList.isNullOrEmpty()) {
            coachMark = CoachMark2(itemView.context)
            coachMark?.isOutsideTouchable = true
            coachMark?.showCoachMark(coachMarkList)
        }
    }

    private fun getChooseAddressWidgetCoachMarkItem(): CoachMark2Item? {
        val isNeedToShowCoachMark = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(itemView.context)
        return if (isNeedToShowCoachMark == true && chooseAddressWidget?.isShown == true) {
            chooseAddressWidget?.let {
                CoachMark2Item(
                        it,
                        itemView.context?.getString(R.string.home_choose_address_widget_coachmark_title).orEmpty(),
                        itemView.context?.getString(R.string.home_choose_address_widget_coachmark_description).orEmpty()
                )
            }
        } else {
            null
        }
    }
}
