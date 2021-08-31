package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.view.NoAddressEmptyStateView
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.activity.TokoNowHomeActivity
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE

class TokoNowEmptyStateOocViewHolder(
        itemView: View,
        private val tokoNowListener: TokoNowView? = null,
) : AbstractViewHolder<TokoNowEmptyStateOocUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_empty_state_ooc
        const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
    }

    private var emptyStateAddressOoc: NoAddressEmptyStateView? = null
    private var params: ViewGroup.LayoutParams? = null
    private var layout: RelativeLayout? = null
    private var divider: View? = null

    init {
        emptyStateAddressOoc = itemView.findViewById(R.id.empty_state_occ)
        divider = itemView.findViewById(R.id.divider)
        layout = itemView.findViewById(R.id.layout)
        params = layout?.layoutParams
    }

    override fun bind(element: TokoNowEmptyStateOocUiModel?) {
        showEmptyStateNoAddress()
    }

    private fun showEmptyStateNoAddress() {
        divider?.show()
        emptyStateAddressOoc?.show()
        emptyStateAddressOoc?.setDescriptionCityName(itemView.context.getString(R.string.tokopedianow_city_name_empty_state_no_address))
        emptyStateAddressOoc?.actionListener = object : NoAddressEmptyStateView.ActionListener {
            override fun onChangeAddressClicked() {
                showBottomSheetChooseAddress()
            }

            override fun onReturnClick() {
                (itemView.context as? TokoNowHomeActivity)?.finish()
            }
        }
        params?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layout?.layoutParams = params
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
}