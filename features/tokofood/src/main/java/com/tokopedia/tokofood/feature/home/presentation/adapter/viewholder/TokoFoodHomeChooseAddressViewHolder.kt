package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodChooseAddressBinding
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment.Companion.SOURCE
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodView
import com.tokopedia.utils.view.binding.viewBinding

class TokoFoodHomeChooseAddressViewHolder(
    itemView: View,
    private val tokoFoodView: TokoFoodView? = null,
    private val tokoFoodChooseAddressWidgetListener: TokoFoodChooseAddressWidgetListener? = null
): AbstractViewHolder<TokoFoodHomeChooseAddressWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_choose_address
    }

    private var binding: ItemTokofoodChooseAddressBinding? by viewBinding()
    private var chooseAddressWidget: ChooseAddressWidget? = null

    override fun bind(element: TokoFoodHomeChooseAddressWidgetUiModel?) {
        setupChooseAddressWidget()
        bindChooseAddressWidget()
    }

    private fun setupChooseAddressWidget(){
        chooseAddressWidget = binding?.chooseAddressWidget
    }

    private fun bindChooseAddressWidget() {
        tokoFoodView?.getFragmentPage()?.let { fragment ->
            chooseAddressWidget?.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
                override fun onLocalizingAddressUpdatedFromWidget() {
                    tokoFoodView.refreshLayoutPage()
                }

                override fun onLocalizingAddressServerDown() {}

                override fun getLocalizingAddressHostFragment(): Fragment {
                    return fragment
                }

                override fun getLocalizingAddressHostSourceData(): String {
                    return SOURCE
                }

                override fun onClickChooseAddressTokoNowTracker() {
                    tokoFoodChooseAddressWidgetListener?.onClickChooseAddressWidgetTracker()
                }

                override fun onLocalizingAddressLoginSuccess() {}

                override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {}

                override fun onLocalizingAddressUpdatedFromBackground() {}

                override fun needToTrackTokoNow(): Boolean =  true
            })
        }
    }

    interface TokoFoodChooseAddressWidgetListener {
        fun onClickChooseAddressWidgetTracker()
    }
}