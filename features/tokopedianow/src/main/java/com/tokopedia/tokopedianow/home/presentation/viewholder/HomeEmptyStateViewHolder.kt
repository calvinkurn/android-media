package com.tokopedia.tokopedianow.home.presentation.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeEmptyStateBinding
import com.tokopedia.tokopedianow.home.presentation.activity.TokoNowHomeActivity
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEmptyStateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeEmptyStateViewHolder(
        itemView: View,
        private val tokoNowView: TokoNowView? = null,
) : AbstractViewHolder<HomeEmptyStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_empty_state
        const val SHIPPING_CHOOSE_ADDRESS_TAG = "SHIPPING_CHOOSE_ADDRESS_TAG"
        const val EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE_IMAGE = TokopediaImageUrl.EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE_IMAGE
    }

    private var binding: ItemTokopedianowHomeEmptyStateBinding? by viewBinding()

    override fun bind(element: HomeEmptyStateUiModel?) {
        binding?.emptyStateNoAddressAndLocalCache?.apply {
            setImageUrl(EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE_IMAGE)
            setPrimaryCTAClickListener {
                showBottomSheetChooseAddress()
            }
            setSecondaryCTAClickListener {
                (itemView.context as? TokoNowHomeActivity)?.finish()
            }
        }?.show()
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