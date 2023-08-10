package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowChooseAddressWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE_TRACKING
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowChooseAddressWidgetViewHolder(
        itemView: View,
        private val tokoNowView: TokoNowView? = null,
        private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetListener? = null
): AbstractViewHolder<TokoNowChooseAddressWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_choose_address_widget
        const val ENABLE_CHOOSE_ADDRESS_WIDGET = "android_tokopedianow_enable_choose_address_widget_on_home_page"
    }

    private var binding: ItemTokopedianowChooseAddressWidgetBinding? by viewBinding()

    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var coachMark: CoachMark2? = null

    override fun bind(element: TokoNowChooseAddressWidgetUiModel) {
        setupChooseAddressWidget(element)
    }

    private fun bindChooseAddressWidget(element: TokoNowChooseAddressWidgetUiModel) {
        tokoNowView?.getFragmentPage()?.let { fragment ->
            chooseAddressWidget?.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
                override fun onLocalizingAddressUpdatedFromWidget() {
                    chooseAddressWidget?.updateWidget()
                    tokoNowView.refreshLayoutPage()
                }

                override fun onLocalizingAddressServerDown() {
                    tokoNowChooseAddressWidgetListener?.onChooseAddressWidgetRemoved()
                }

                override fun onClickChooseAddressTokoNowTracker() {
                    tokoNowChooseAddressWidgetListener?.onClickChooseAddressWidgetTracker()
                }

                override fun needToTrackTokoNow(): Boolean = true

                override fun getLocalizingAddressHostFragment(): Fragment = fragment

                override fun getLocalizingAddressHostSourceData(): String = SOURCE

                override fun getLocalizingAddressHostSourceTrackingData(): String = SOURCE_TRACKING

                override fun onLocalizingAddressUpdatedFromBackground() { /* to do : nothing */ }

                override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) { /* to do : nothing */ }

                override fun onLocalizingAddressLoginSuccess() { /* to do : nothing */ }

                override fun isFromTokonowPage(): Boolean {
                    return true
                }

                override fun onChangeTextColor(): Int {
                    return if (element.isColorStatic) {
                        com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                    } else {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                    }
                }
            })
        }
    }

    private fun setupChooseAddressWidget(element: TokoNowChooseAddressWidgetUiModel) {
        chooseAddressWidget = binding?.chooseAddressWidget
        bindChooseAddressWidget(element)
        showCoachMark()
        setBackgroundColor(element)
    }

    private fun setBackgroundColor(element: TokoNowChooseAddressWidgetUiModel) {
        if (element.backgroundLightColor.isNotBlank() || element.backgroundDarkColor.isNotBlank()) {
            binding?.root?.apply {
                setBackgroundColor(
                    safeParseColor(
                        color = if (context.isDarkMode()) element.backgroundDarkColor else element.backgroundLightColor,
                        defaultColor = ContextCompat.getColor(
                            itemView.context,
                            R.color.tokopedianow_card_dms_color
                        )
                    )
                )
            }
        }
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
                        itemView.context?.getString(R.string.tokopedianow_home_choose_address_widget_coachmark_title).orEmpty(),
                        itemView.context?.getString(R.string.tokopedianow_home_choose_address_widget_coachmark_description).orEmpty()
                )
            }
        } else {
            null
        }
    }

    interface TokoNowChooseAddressWidgetListener {
        fun onChooseAddressWidgetRemoved()
        fun onClickChooseAddressWidgetTracker()
    }
}
