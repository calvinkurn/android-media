package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodSearchOocBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchOOCUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class MerchantSearchOOCViewHolder(
    itemView: View,
    private val listener: Listener?
) : AbstractViewHolder<MerchantSearchOOCUiModel>(itemView) {

    private val binding: ItemTokofoodSearchOocBinding? by viewBinding()

    init {
        setEmptyStateManualFontSize()
    }

    override fun bind(element: MerchantSearchOOCUiModel) {
        when (element.type) {
            MerchantSearchOOCUiModel.NO_ADDRESS, MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP -> {
                setNoAddressGlobalEmptyState(element.type)
            }
            MerchantSearchOOCUiModel.NO_PINPOINT -> {
                setNoPinpointEmptyState(element.type)
            }
            else -> {
                setOutOfCoverageEmptyState(element.type)
            }
        }
    }

    private fun setNoAddressGlobalEmptyState(type: Int) {
        setImageUrl(IMG_STATIC_URI_NO_ADDRESS)
        setNoAddressWording()
        setGlobalErrorActionClick(type)
    }

    private fun setNoPinpointEmptyState(type: Int) {
        setImageUrl(IMG_STATIC_URI_NO_PIN_POIN)
        setNoPinpointWording()
        setGlobalErrorActionClick(type)
    }

    private fun setOutOfCoverageEmptyState(type: Int) {
        setImageUrl(IMG_STATIC_URI_NO_PIN_POIN)
        setOutOfCoverageWording()
        setGlobalErrorActionClick(type)
        setGlobalErrorSecondaryActionClick()
    }

    private fun setNoAddressWording() {
        val title = getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_no_address_title)
        val subtitle =
            getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_no_address_subtitle)
        val buttonMessage =
            getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_no_address_button)

        setGlobalErrorWording(title, subtitle, buttonMessage)
    }

    private fun setNoPinpointWording() {
        val title = getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_no_pinpoint_title)
        val subtitle =
            getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_no_pinpoint_subtitle)
        val buttonMessage =
            getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_no_pinpoint_button)

        setGlobalErrorWording(title, subtitle, buttonMessage)
    }

    private fun setOutOfCoverageWording() {
        val title = getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_out_of_coverage_title)
        val subtitle =
            getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_out_of_coverage_subtitle)
        val buttonMessage =
            getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_out_of_coverage_button)
        val secondaryButtonMessage =
            getStringFromId(com.tokopedia.tokofood.R.string.search_srp_ooc_out_of_coverage_secondary_button)


        setGlobalErrorWording(title, subtitle, buttonMessage, secondaryButtonMessage)
    }

    private fun getStringFromId(@StringRes resId: Int): String {
        return try {
            itemView.context?.getString(resId).orEmpty()
        } catch (ex: Exception) {
            String.EMPTY
        }
    }

    private fun setImageUrl(imageUrl: String) {
        binding?.emptyStateSearchOoc?.setImageUrl(imageUrl)
    }

    private fun setGlobalErrorWording(
        title: String,
        subtitle: String,
        buttonMessage: String,
        secondaryButtonMessage: String? = null
    ) {
        binding?.emptyStateSearchOoc?.run {
            setTitle(title)
            setDescription(subtitle)
            setPrimaryCTAText(buttonMessage)
            setSecondaryCTAText(secondaryButtonMessage.orEmpty())
        }
    }

    private fun setGlobalErrorActionClick(type: Int) {
        binding?.emptyStateSearchOoc?.setPrimaryCTAClickListener {
            listener?.onOOCActionButtonClicked(type)
        }
    }

    private fun setGlobalErrorSecondaryActionClick() {
        binding?.emptyStateSearchOoc?.setSecondaryCTAClickListener {
            listener?.onGoToHomepageButtonClicked()
        }
    }

    private fun setEmptyStateManualFontSize() {
        binding?.emptyStateSearchOoc?.emptyStateCTAID?.buttonSize = UnifyButton.Size.MEDIUM
    }

    interface Listener {
        fun onOOCActionButtonClicked(type: Int)
        fun onGoToHomepageButtonClicked()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_search_ooc
        
        private const val IMG_STATIC_URI_NO_PIN_POIN = TokopediaImageUrl.IMG_STATIC_URI_NO_PIN_POIN
        private const val IMG_STATIC_URI_NO_ADDRESS = TokopediaImageUrl.IMG_STATIC_URI_NO_ADDRESS
    }

}
