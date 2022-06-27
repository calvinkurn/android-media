package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.ItemSahNewOtherTokoPlusBinding
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.TokoPlusWidgetUiModel

class TokoPlusViewHolder(
    itemView: View,
    private val onClicked: () -> Unit,
    private val onImpressed: () -> Unit
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
        sendImpressionEvent(element)
        setOnClicked()
    }

    private fun setupView(element: TokoPlusWidgetUiModel) {

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