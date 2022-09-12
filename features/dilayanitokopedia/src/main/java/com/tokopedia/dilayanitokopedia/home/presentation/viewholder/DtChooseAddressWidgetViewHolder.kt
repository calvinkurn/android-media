package com.tokopedia.dilayanitokopedia.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dilayanitokopedia.common.model.DtChooseAddressWidgetUiModel
import com.tokopedia.dilayanitokopedia.common.view.DtView
import com.tokopedia.dilayanitokopedia.databinding.ItemDtChooseAddressWidgetBinding
import com.tokopedia.dilayanitokopedia.home.presentation.fragment.DtHomeFragment.Companion.SOURCE
import com.tokopedia.dilayanitokopedia.home.presentation.fragment.DtHomeFragment.Companion.SOURCE_TRACKING
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.utils.view.binding.viewBinding


class DtChooseAddressWidgetViewHolder(
    itemView: View,
    private val dtView: DtView? = null,
    private val dtChooseAddressWidgetListener: DtChooseAddressWidgetListener? = null
) : AbstractViewHolder<DtChooseAddressWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dt_choose_address_widget
        const val ENABLE_CHOOSE_ADDRESS_WIDGET = "android_tokopedianow_enable_choose_address_widget_on_home_page"
    }

    private var binding: ItemDtChooseAddressWidgetBinding? by viewBinding()

    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var coachMark: CoachMark2? = null

    override fun bind(element: DtChooseAddressWidgetUiModel?) {
        setupChooseAddressWidget()
    }

    private fun bindChooseAddressWidget() {
        dtView?.getFragmentPage()?.let { fragment ->
            chooseAddressWidget?.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
                override fun onLocalizingAddressUpdatedFromWidget() {
                    dtView.refreshLayoutPage()
                }

                override fun onLocalizingAddressServerDown() {
                    dtChooseAddressWidgetListener?.onChooseAddressWidgetRemoved()
                }

                override fun onClickChooseAddressTokoNowTracker() {
                   dtChooseAddressWidgetListener?.onClickChooseAddressWidgetTracker()
                }

                override fun needToTrackTokoNow(): Boolean = true

                override fun getLocalizingAddressHostFragment(): Fragment = fragment

                override fun getLocalizingAddressHostSourceData(): String = SOURCE

                override fun getLocalizingAddressHostSourceTrackingData(): String = SOURCE_TRACKING

                override fun onLocalizingAddressUpdatedFromBackground() { /* to do : nothing */
                }

                override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) { /* to do : nothing */
                }

                override fun onLocalizingAddressLoginSuccess() { /* to do : nothing */
                }
            })
        }
    }

    private fun setupChooseAddressWidget() {
        chooseAddressWidget = binding?.chooseAddressWidget
        bindChooseAddressWidget()
        showCoachMark()
    }

    private fun showCoachMark() {
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
//        val isNeedToShowCoachMark = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(itemView.context)
//        return if (isNeedToShowCoachMark == true && chooseAddressWidget?.isShown == true) {
//            chooseAddressWidget?.let {
//                CoachMark2Item(
//                    it,
//                    itemView.context?.getString(R.string.tokopedianow_home_choose_address_widget_coachmark_title).orEmpty(),
//                    itemView.context?.getString(R.string.tokopedianow_home_choose_address_widget_coachmark_description).orEmpty()
//                )
//            }
//        } else {
            return null
//        }
    }

    interface DtChooseAddressWidgetListener {
        fun onChooseAddressWidgetRemoved()
        fun onClickChooseAddressWidgetTracker()
    }
}
