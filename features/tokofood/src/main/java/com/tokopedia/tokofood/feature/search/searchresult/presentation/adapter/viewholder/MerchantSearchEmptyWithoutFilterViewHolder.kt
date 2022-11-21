package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodSearchMerchantEmptyHorizontalBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithoutFilterUiModel
import com.tokopedia.utils.view.binding.viewBinding

class MerchantSearchEmptyWithoutFilterViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MerchantSearchEmptyWithoutFilterUiModel>(itemView) {

    private val binding: ItemTokofoodSearchMerchantEmptyHorizontalBinding? by viewBinding()

    private var onCheckKeywordButtonClickedAction: () -> Unit = {}
    private var onSearchInTokopediaButtonClickedAction: () -> Unit = {}

    init {
        initButtonClickListener()
    }

    override fun bind(element: MerchantSearchEmptyWithoutFilterUiModel) {
        setupLayout(element)
    }

    private fun initButtonClickListener() {
        binding?.emptyStateTokofoodSearchMerchantHorizontal?.emptyStateCTAID?.setOnClickListener {
            onCheckKeywordButtonClickedAction()
        }
        binding?.emptyStateTokofoodSearchMerchantHorizontal?.emptyStateSecondaryCTAID?.setOnClickListener {
            onSearchInTokopediaButtonClickedAction()
        }
    }

    private fun setupLayout(element: MerchantSearchEmptyWithoutFilterUiModel) {
        loadImage()
        setupKeywordMessage(element.keyword)
        setupButtonClickListener()
    }

    private fun loadImage() {
        binding?.emptyStateTokofoodSearchMerchantHorizontal?.emptyStateImageID?.loadImage(
            EMPTY_STATE_URL
        )
    }

    private fun setupKeywordMessage(keyword: String) {
        binding?.emptyStateTokofoodSearchMerchantHorizontal?.emptyStateDescriptionID?.text =
            itemView.context.getString(
                com.tokopedia.tokofood.R.string.search_srp_empty_without_filter_description,
                keyword
            )
    }

    private fun setupButtonClickListener() {
        onCheckKeywordButtonClickedAction = listener::onCheckKeywordButtonClicked
        onSearchInTokopediaButtonClickedAction = listener::onSearchInTokopediaButtonClicked
    }

    interface Listener {
        fun onCheckKeywordButtonClicked()
        fun onSearchInTokopediaButtonClicked()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_search_merchant_empty_horizontal

        private const val EMPTY_STATE_URL =
            "https://images.tokopedia.net/img/android/tokofood/merchant_not_found.png"
    }
}