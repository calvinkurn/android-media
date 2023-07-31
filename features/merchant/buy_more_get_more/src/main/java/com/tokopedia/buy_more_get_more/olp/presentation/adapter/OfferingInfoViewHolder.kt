package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemOlpOfferingInfoBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.utils.view.binding.viewBinding

class OfferingInfoViewHolder(view: View): AbstractViewHolder<OfferInfoForBuyerUiModel>(view) {

    private val binding: ItemOlpOfferingInfoBinding? by viewBinding()

    companion object{
        @LayoutRes val LAYOUT = R.layout.item_olp_offering_info
    }

    override fun bind(data: OfferInfoForBuyerUiModel?) {
        binding?.tpgShopName?.text = "hakuna matata"
    }
}
