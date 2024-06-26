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
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.OUT_OF_COVERAGE_DESCRIPTION_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.OUT_OF_COVERAGE_PRIMARY_BUTTON_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.OUT_OF_COVERAGE_SECONDARY_BUTTON_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.OUT_OF_COVERAGE_TITLE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeRes
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
        const val SOURCE = "tokonow"
    }

    private var binding: ItemTokopedianowEmptyStateOocBinding? by viewBinding()
    private var hostSource = ""

    override fun bind(element: TokoNowEmptyStateOocUiModel) {
        val eventCategory = listener?.onGetEventCategory().orEmpty()
        val serviceType = element.serviceType
        hostSource = element.hostSource

        showEmptyStateTitle()
        showEmptyStateDescription()
        showPrimaryBtnText()
        showSecondaryBtnText()
        setActionListener(eventCategory, serviceType)
    }

    private fun showEmptyStateTitle() {
        getServiceTypeRes(OUT_OF_COVERAGE_TITLE_ID)?.let {
            binding?.emptyStateOcc?.setTitle(getString(it))
        }
    }

    private fun showEmptyStateDescription() {
        getServiceTypeRes(OUT_OF_COVERAGE_DESCRIPTION_ID)?.let {
            binding?.emptyStateOcc?.setDescription(getString(it))
        }
    }

    private fun showPrimaryBtnText() {
        getServiceTypeRes(OUT_OF_COVERAGE_PRIMARY_BUTTON_ID)?.let {
            binding?.emptyStateOcc?.setPrimaryBtnText(getString(it))
        }
    }

    private fun showSecondaryBtnText() {
        getServiceTypeRes(OUT_OF_COVERAGE_SECONDARY_BUTTON_ID)?.let {
            binding?.emptyStateOcc?.setSecondaryBtnText(getString(it))
        }
    }

    private fun setActionListener(eventCategory: String, serviceType: String) {
        binding?.emptyStateOcc?.actionListener = object : NoAddressEmptyStateView.ActionListener {
            override fun onPrimaryBtnClicked() {
                if (serviceType == ServiceType.NOW_2H ||
                    serviceType == ServiceType.NOW_OOC
                ) {
                    showBottomSheetChooseAddress()
                }
            }

            override fun onSecondaryBtnClicked() {
                if (serviceType == ServiceType.NOW_2H ||
                    serviceType == ServiceType.NOW_OOC
                ) {
                    (itemView.context as? Activity)?.finish()
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

            override fun isFromTokonowPage(): Boolean {
                return true
            }
        })
        listener?.onGetFragmentManager()?.let {
            chooseAddressBottomSheet.show(it, SHIPPING_CHOOSE_ADDRESS_TAG)
        }
    }

    interface TokoNowEmptyStateOocListener {
        fun onRefreshLayoutPage()
        fun onGetFragmentManager(): FragmentManager
        fun onGetEventCategory(): String
    }
}
