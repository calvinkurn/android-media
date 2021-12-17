package com.tokopedia.tokopedianow.search.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCtaHomeBinding
import com.tokopedia.tokopedianow.search.presentation.listener.CTATokoNowHomeListener
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class CTATokopediaNowHomeViewHolder(
        itemView: View,
        private val ctaTokoNowHomeListener: CTATokoNowHomeListener,
): AbstractViewHolder<CTATokopediaNowHomeDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_cta_home
    }

    private var binding: ItemTokopedianowSearchCtaHomeBinding? by viewBinding()

    private val ctaHomeButton: UnifyButton? by lazy {
        binding?.tokoNowCTAHomeButton
    }

    override fun bind(element: CTATokopediaNowHomeDataView?) {
        ctaHomeButton?.setOnClickListener {
            ctaTokoNowHomeListener.onCTAToTokopediaNowHomeClick()
        }
    }
}