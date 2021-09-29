package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE
import com.tokopedia.tokopedianow.home.presentation.activity.TokoNowHomeActivity
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEmptyStateUiModel

class HomeEmptyStateViewHolder(
        itemView: View,
        private val tokoNowView: TokoNowView? = null,
) : AbstractViewHolder<HomeEmptyStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_empty_state
        const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
        const val EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE_IMAGE = "https://images.tokopedia.net/img/android/tokonow/ic_no_address_empty_state_and_local_chache.png"
    }

    private var emptyStateNoAddressAndLocalCache: EmptyStateUnify? = null

    init {
        emptyStateNoAddressAndLocalCache = itemView.findViewById(R.id.empty_state_no_address_and_local_cache)
    }

    override fun bind(element: HomeEmptyStateUiModel?) {
        emptyStateNoAddressAndLocalCache?.show()
        emptyStateNoAddressAndLocalCache?.setImageUrl(EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE_IMAGE)
        emptyStateNoAddressAndLocalCache?.setPrimaryCTAClickListener {
            showBottomSheetChooseAddress()
        }
        emptyStateNoAddressAndLocalCache?.setSecondaryCTAClickListener {
            (itemView.context as? TokoNowHomeActivity)?.finish()
        }
    }

    private fun showBottomSheetChooseAddress() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
            override fun getLocalizingAddressHostSourceBottomSheet(): String = SOURCE

            override fun onAddressDataChanged() {
                tokoNowView?.refreshLayoutPage()
            }

            override fun onLocalizingAddressServerDown() { /* to do : nothing */ }

            override fun onLocalizingAddressLoginSuccessBottomSheet() { /* to do : nothing */ }

            override fun onDismissChooseAddressBottomSheet() { /* to do : nothing */ }
        })
        tokoNowView?.getFragmentManagerPage()?.let {
            chooseAddressBottomSheet.show(it, SHIPPING_CHOOSE_ADDRESS_TAG)
        }
    }
}