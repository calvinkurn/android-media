package com.tokopedia.tokopedianow.common.viewholder

import android.app.Activity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.view.NoAddressEmptyStateView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowEmptyStateOocBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowEmptyStateOocViewHolder(
        itemView: View,
        private val listener: TokoNowEmptyStateOocListener? = null,
) : AbstractViewHolder<TokoNowEmptyStateOocUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_empty_state_ooc
        const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
    }

    private var binding: ItemTokopedianowEmptyStateOocBinding? by viewBinding()
    private var hostSource = ""

    override fun bind(element: TokoNowEmptyStateOocUiModel) {
        val eventCategory = listener?.onGetEventCategory().orEmpty()
        val serviceType = element.serviceType
        hostSource = element.hostSource

        showEmptyStateTitle(serviceType)
        showEmptyStateDescription(serviceType)
        showPrimaryBtnText(serviceType)
        showSecondaryBtnText(serviceType)
        setActionListener(eventCategory, serviceType)
    }

    private fun showEmptyStateTitle(serviceType: String) {
        val resId = if(serviceType == ServiceType.NOW_2H) {
            R.string.tokopedianow_common_empty_state_title
        } else {
            R.string.tokopedianow_15m_empty_state_title
        }
        binding?.emptyStateOcc?.setTitle(getString(resId))
    }

    private fun showEmptyStateDescription(serviceType: String) {
        val resId = if(serviceType == ServiceType.NOW_2H) {
            R.string.tokopedianow_common_empty_state_desc
        } else {
            R.string.tokopedianow_15m_empty_state_desc
        }
        binding?.emptyStateOcc?.setDescription(getString(resId))
    }

    private fun showPrimaryBtnText(serviceType: String) {
        val resId = if(serviceType == ServiceType.NOW_2H) {
            R.string.tokopedianow_common_empty_state_button_change_address
        } else {
            R.string.tokopedianow_15m_empty_state_primary_btn
        }
        binding?.emptyStateOcc?.setPrimaryBtnText(getString(resId))
    }

    private fun showSecondaryBtnText(serviceType: String) {
        val resId = if(serviceType == ServiceType.NOW_2H) {
            R.string.tokopedianow_common_empty_state_button_return
        } else {
            R.string.tokopedianow_common_empty_state_button_change_address
        }
        binding?.emptyStateOcc?.setSecondaryBtnText(getString(resId))
    }

    private fun setActionListener(eventCategory: String, serviceType: String) {
        binding?.emptyStateOcc?.actionListener = object : NoAddressEmptyStateView.ActionListener {
            override fun onPrimaryBtnClicked() {
                if(serviceType == ServiceType.NOW_2H) {
                    showBottomSheetChooseAddress()
                } else {
                    listener?.onSwitchService()
                }
            }

            override fun onSecondaryBtnClicked() {
                if(serviceType == ServiceType.NOW_2H) {
                    (itemView.context as? Activity)?.finish()
                } else {
                    showBottomSheetChooseAddress()
                }
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
                listener?.onRefreshLayoutPage()
            }

            override fun onLocalizingAddressServerDown() { /* to do : nothing */ }

            override fun onLocalizingAddressLoginSuccessBottomSheet() { /* to do : nothing */ }

            override fun onDismissChooseAddressBottomSheet() { /* to do : nothing */ }
        })
        listener?.onGetFragmentManager()?.let {
            chooseAddressBottomSheet.show(it, SHIPPING_CHOOSE_ADDRESS_TAG)
        }
    }

    interface TokoNowEmptyStateOocListener {
        fun onRefreshLayoutPage()
        fun onGetFragmentManager(): FragmentManager
        fun onGetEventCategory(): String
        fun onSwitchService()
    }
}