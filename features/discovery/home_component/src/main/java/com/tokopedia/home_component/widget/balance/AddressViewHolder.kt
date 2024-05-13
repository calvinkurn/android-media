package com.tokopedia.home_component.widget.balance

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.databinding.LayoutAddressBinding
import com.tokopedia.home_component.databinding.LayoutBalanceItemBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by frenzel
 */
class AddressViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemUiModel>(v) {
    companion object {
        val LAYOUT = home_componentR.layout.layout_address
    }

    private val binding: LayoutAddressBinding? by viewBinding()

    override fun bind(
        model: BalanceItemUiModel,
    ) {

    }

    override fun bind(
        model: BalanceItemUiModel,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNotEmpty()) {
        } else {
            bind(model)
        }
    }
}
