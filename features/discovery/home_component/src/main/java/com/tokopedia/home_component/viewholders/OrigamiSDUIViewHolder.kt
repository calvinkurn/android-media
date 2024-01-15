package com.tokopedia.home_component.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.visitable.OrigamiSDUIDataModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentOrigamiSduiBinding
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import com.tokopedia.utils.view.binding.viewBinding

class OrigamiSDUIViewHolder(
    itemView: View
) : AbstractViewHolder<OrigamiSDUIDataModel>(itemView) {
    companion object {
        val LAYOUT: Int = R.layout.home_component_origami_sdui
    }

    private var binding: HomeComponentOrigamiSduiBinding? by viewBinding()

    override fun bind(element: OrigamiSDUIDataModel?) {
        binding?.homeComponentOrigamiContainer?.addView(View(itemView.context))
    }

}
