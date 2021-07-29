package com.tokopedia.tokopedianow.search.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.search.presentation.listener.CTATokoNowHomeListener
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.unifycomponents.UnifyButton

class CTATokopediaNowHomeViewHolder(
        itemView: View,
        private val ctaTokoNowHomeListener: CTATokoNowHomeListener,
): AbstractViewHolder<CTATokopediaNowHomeDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_cta_home
    }

    private val ctaHomeButton: UnifyButton? by lazy {
        itemView.findViewById(R.id.tokoNowCTAHomeButton)
    }

    override fun bind(element: CTATokopediaNowHomeDataView?) {
        ctaHomeButton?.setOnClickListener {
            ctaTokoNowHomeListener.onCTAToTokopediaNowHomeClick()
        }
    }
}