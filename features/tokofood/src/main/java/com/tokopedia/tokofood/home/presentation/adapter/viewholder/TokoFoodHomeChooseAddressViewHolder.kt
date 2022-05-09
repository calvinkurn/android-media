package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodChooseAddressBinding
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeView
import com.tokopedia.utils.view.binding.viewBinding

class TokoFoodHomeChooseAddressViewHolder(
    itemView: View,
    private val tokoFoodHomeView: TokoFoodHomeView? = null,
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
        tokoFoodHomeView?.getFragmentPage()?.let { fragment ->
            chooseAddressWidget?.bindChooseAddress(object : ChooseAddressWidget.ChooseAddressWidgetListener {
                override fun onLocalizingAddressUpdatedFromWidget() {
                    tokoFoodHomeView.refreshLayoutPage()
                }

                override fun onLocalizingAddressServerDown() {
                    tokoFoodChooseAddressWidgetListener?.onChooseAddressWidgetRemoved()
                }

                override fun getLocalizingAddressHostFragment(): Fragment {
                    return fragment
                }

                override fun getLocalizingAddressHostSourceData(): String {
                    return "home"
                }

                override fun onLocalizingAddressLoginSuccess() {}

                override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {}

                override fun onLocalizingAddressUpdatedFromBackground() {}

                override fun isSupportWarehouseLoc(): Boolean {
                    return false
                }

            })
        }
    }

    interface TokoFoodChooseAddressWidgetListener {
        fun onChooseAddressWidgetRemoved()
        fun onClickChooseAddressWidgetTracker()
    }
}