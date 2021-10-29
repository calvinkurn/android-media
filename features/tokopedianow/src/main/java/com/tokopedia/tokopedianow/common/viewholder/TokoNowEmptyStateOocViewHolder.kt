package com.tokopedia.tokopedianow.common.viewholder

import android.app.Activity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.view.NoAddressEmptyStateView
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowEmptyStateOocBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowEmptyStateOocViewHolder(
        itemView: View,
        private val tokoNowListener: TokoNowView? = null,
) : AbstractViewHolder<TokoNowEmptyStateOocUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_empty_state_ooc
        const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
    }

    private var binding: ItemTokopedianowEmptyStateOocBinding? by viewBinding()
    private var hostSource = ""

    override fun bind(element: TokoNowEmptyStateOocUiModel) {
        hostSource = element.hostSource
        showEmptyStateNoAddress(element.eventCategory)
    }

    private fun showEmptyStateNoAddress(eventCategory: String) {
        binding?.emptyStateOcc?.actionListener = object : NoAddressEmptyStateView.ActionListener {
            override fun onChangeAddressClicked() {
                showBottomSheetChooseAddress()
            }

            override fun onReturnClick() {
                (itemView.context as? Activity)?.finish()
            }

            override fun onGetNoAddressEmptyStateEventCategoryTracker(): String {
                return eventCategory
            }
        }
    }

    private fun showBottomSheetChooseAddress() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
            override fun getLocalizingAddressHostSourceBottomSheet(): String = hostSource

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