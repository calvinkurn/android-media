package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.view.TokoMartHomeView
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment.Companion.SOURCE
import com.tokopedia.tokomart.home.presentation.uimodel.HomeChooseAddressWidgetUiModel

class HomeChooseAddressWidgetViewHolder(
        itemView: View,
        private val tokoMartHomeListener: TokoMartHomeView? = null,
        private val homeChooseAddressWidgetListener: HomeChooseAddressWidgetListener? = null
): AbstractViewHolder<HomeChooseAddressWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_choose_address_widget
        const val ENABLE_CHOOSE_ADDRESS_WIDGET = "android_tokomart_enable_choose_address_widget_on_home_page"
    }

    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var coachMark: CoachMark2? = null

    override fun bind(element: HomeChooseAddressWidgetUiModel?) {
        setupChooseAddressWidget()
    }

    private fun bindChooseAddressWidget() {
        tokoMartHomeListener?.getFragment()?.let { fragment ->
            chooseAddressWidget?.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
                override fun onLocalizingAddressUpdatedFromWidget() {
                    chooseAddressWidget?.updateWidget()
                }

                override fun onLocalizingAddressServerDown() {
                    homeChooseAddressWidgetListener?.onRemoveChooseAddressWidget()
                }

                override fun onLocalizingAddressLoginSuccess() {
                    homeChooseAddressWidgetListener?.onRefresh()
                }

                override fun onLocalizingAddressUpdatedFromBackground() { /* to do : nothing */ }

                override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) { /* to do : nothing */ }

                override fun getLocalizingAddressHostFragment(): Fragment = fragment

                override fun getLocalizingAddressHostSourceData(): String = SOURCE

                override fun getLocalizingAddressHostSourceTrackingData(): String = SOURCE
            })
        }
    }

    private fun setupChooseAddressWidget() {
        chooseAddressWidget = itemView.findViewById(R.id.choose_address_widget)
        bindChooseAddressWidget()
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

    interface HomeChooseAddressWidgetListener {
        fun onRefresh()
        fun onRemoveChooseAddressWidget()
    }
}
