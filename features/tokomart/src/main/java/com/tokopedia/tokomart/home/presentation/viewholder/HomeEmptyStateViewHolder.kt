package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.view.NoAddressEmptyStateView
import com.tokopedia.tokomart.common.view.TokoNowView
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment.Companion.SOURCE
import com.tokopedia.tokomart.home.presentation.uimodel.HomeEmptyStateUiModel

class HomeEmptyStateViewHolder(
        itemView: View,
        private val tokoNowListener: TokoNowView? = null,
) : AbstractViewHolder<HomeEmptyStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokonow_empty_state_no_address
        const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
    }

    private var emptyStateNoAddress: NoAddressEmptyStateView? = null
    private var emptyStateFailedToFetchData: GlobalError? = null

    init {
        emptyStateNoAddress = itemView.findViewById(R.id.empty_state_no_address)
        emptyStateFailedToFetchData = itemView.findViewById(R.id.empty_state_failed_to_fetch_data)
    }

    override fun bind(element: HomeEmptyStateUiModel?) {
        when(element?.id) {
            EMPTY_STATE_NO_ADDRESS -> showEmptyStateNoAddress()
            EMPTY_STATE_FAILED_TO_FETCH_DATA -> showEmptyStateFailedToFetchData()
        }
    }

    private fun showEmptyStateNoAddress() {
        emptyStateFailedToFetchData?.hide()
        emptyStateNoAddress?.show()
        emptyStateNoAddress?.setDescriptionCityName("EMPTY NO ADDRESS WOI")
        emptyStateNoAddress?.actionListener = object : NoAddressEmptyStateView.ActionListener {
            override fun onChangeAddressClicked() {
                showBottomSheetChooseAddress()
            }

            override fun onReturnClick() {
                RouteManager.route(itemView.context, ApplinkConst.HOME)
            }
        }
    }

    private fun showBottomSheetChooseAddress() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
            override fun getLocalizingAddressHostSourceBottomSheet(): String = SOURCE

            override fun onAddressDataChanged() {
                tokoNowListener?.refreshLayoutPage()
            }

            override fun onLocalizingAddressServerDown() { /* to do : nothing */ }

            override fun onLocalizingAddressLoginSuccessBottomSheet() { /* to do : nothing */ }

            override fun onDismissChooseAddressBottomSheet() { /* to do : nothing */ }
        })
        tokoNowListener?.getFragmentManagerPage()?.let {
            chooseAddressBottomSheet.show(it, SHIPPING_CHOOSE_ADDRESS_TAG)
        }
    }

    private fun showEmptyStateFailedToFetchData() {
        emptyStateNoAddress?.hide()
        emptyStateFailedToFetchData?.show()
        emptyStateFailedToFetchData?.setActionClickListener {
            tokoNowListener?.refreshLayoutPage()
        }
    }
}