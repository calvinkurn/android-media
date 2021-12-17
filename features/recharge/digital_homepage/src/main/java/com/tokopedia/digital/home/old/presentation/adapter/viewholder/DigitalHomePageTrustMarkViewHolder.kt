package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeTrustmarkBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageTrustMarkModel
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemTrustMarkAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DigitalHomePageTrustMarkViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageTrustMarkModel>(itemView) {

    override fun bind(element: DigitalHomePageTrustMarkModel) {
        val binding = LayoutDigitalHomeTrustmarkBinding.bind(itemView)
        val layoutManager = GridLayoutManager(itemView.context, TRUST_MARK_SPAN_COUNT)
        binding.rvDigitalHomepageTrustMark.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess
                    && element.data != null
                    && element.data.section.items.isNotEmpty()) {
                with (element.data.section) {
                    binding.viewDigitalHomepageTrustMarkShimmering.hide()
                    binding.viewDigitalHomepageTrustMarkContainer.show()
                    binding.rvDigitalHomepageTrustMark.adapter = DigitalItemTrustMarkAdapter(items)
                }
            } else {
                binding.viewDigitalHomepageTrustMarkShimmering.hide()
                binding.viewDigitalHomepageTrustMarkContainer.hide()
            }
        } else {
            binding.viewDigitalHomepageTrustMarkShimmering.show()
            binding.viewDigitalHomepageTrustMarkContainer.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_trustmark
        const val TRUST_MARK_SPAN_COUNT = 3
    }
}