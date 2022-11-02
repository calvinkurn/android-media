package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.ItemSahNewOtherTokoPlusBinding
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.TokoPlusWidgetUiModel

class TokoPlusViewHolder(
    itemView: View,
    private val onClicked: () -> Unit,
    private val onImpressed: () -> Unit,
    private val onErrorClicked: () -> Unit,
) : AbstractViewHolder<TokoPlusWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_toko_plus
    }

    private val binding by lazy {
        ItemSahNewOtherTokoPlusBinding.bind(itemView)
    }

    override fun bind(element: TokoPlusWidgetUiModel) {
        setupView(element)
    }

    private fun setupView(element: TokoPlusWidgetUiModel) {
        when (val state = element.state) {
            is SettingResponseState.SettingSuccess -> {
                showPlusBadgeImage(state.data)
                sendImpressionEvent(element)
            }
            is SettingResponseState.SettingError -> setWidgetError()
            else -> setWidgetLoading()
        }
    }

    private fun setWidgetLoading() = with(binding) {
        imgSahPlusBadge.gone()
        errorStateSahPlusBadge.sahOtherFailedContainer.gone()
        shimmerSahPlusBadge.show()
        itemView.setOnClickListener(null)
    }

    private fun setWidgetError() = with(binding) {
        imgSahPlusBadge.gone()
        errorStateSahPlusBadge.sahOtherFailedContainer.run {
            show()
            setOnClickListener {
                onErrorClicked()
            }
        }
        shimmerSahPlusBadge.gone()
        itemView.setOnClickListener(null)
    }

    private fun showPlusBadgeImage(badgeUrl: String) = with(binding) {
        imgSahPlusBadge.run {
            show()
            setImageUrl(badgeUrl)
        }
        errorStateSahPlusBadge.sahOtherFailedContainer.gone()
        shimmerSahPlusBadge.gone()
        setOnClicked()
    }

    private fun setOnClicked() {
        binding.root.setOnClickListener {
            onClicked()
        }
    }

    private fun sendImpressionEvent(element: TokoPlusWidgetUiModel) {
        binding.root.addOnImpressionListener(element.impressHolder) {
            onImpressed()
        }
    }
}