package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHeaderSpaceBinding
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowHeaderSpaceViewHolder(
    itemView: View
) : AbstractViewHolder<TokoNowHeaderSpaceUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_header_space
    }

    private var binding: ItemTokopedianowHeaderSpaceBinding? by viewBinding()

    override fun bind(data: TokoNowHeaderSpaceUiModel) {
        binding?.root?.apply {
            layoutParams.height = data.space
            setBackgroundColor(
                data.backgroundColor ?: safeParseColor(
                    color = if (context.isDarkMode()) data.backgroundDarkColor else data.backgroundLightColor,
                    defaultColor = ContextCompat.getColor(
                        context,
                        R.color.tokopedianow_card_dms_color
                    )
                )
            )
        }
    }
}
