package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder

import com.tokopedia.imageassets.ImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodSearchMerchantEmptyFullBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithFilterUiModel
import com.tokopedia.utils.view.binding.viewBinding

class MerchantSearchEmptyWithFilterViewHolder(
    itemView: View,
    private val listener: Listener
): AbstractViewHolder<MerchantSearchEmptyWithFilterUiModel>(itemView) {

    private val binding: ItemTokofoodSearchMerchantEmptyFullBinding? by viewBinding()

    private var onResetFilterButtonClickAction: () -> Unit = {}

    init {
        binding?.emptyStateTokofoodSearchMerchantFull?.emptyStateCTAID?.setOnClickListener {
            onResetFilterButtonClickAction()
        }
    }

    override fun bind(element: MerchantSearchEmptyWithFilterUiModel?) {
        setupLayout()
    }

    private fun setupLayout() {
        loadImage()
        setButtonClickListener()
    }

    private fun loadImage() {
        binding?.emptyStateTokofoodSearchMerchantFull?.emptyStateImageID?.loadImage(EMPTY_STATE_URL)
    }

    private fun setButtonClickListener() {
        onResetFilterButtonClickAction = listener::onResetFilterButtonClicked
    }

    interface Listener {
        fun onResetFilterButtonClicked()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_search_merchant_empty_full

        private const val EMPTY_STATE_URL = ImageUrl.EMPTY_STATE_URL
    }

}