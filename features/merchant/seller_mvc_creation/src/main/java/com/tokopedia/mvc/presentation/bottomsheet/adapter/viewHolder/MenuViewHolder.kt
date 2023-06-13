package com.tokopedia.mvc.presentation.bottomsheet.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemThreeDotsMenuBinding
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.utils.view.binding.viewBinding

class MenuViewHolder(itemView: View?, private val callback: (MoreMenuUiModel) -> Unit) :
    AbstractViewHolder<MoreMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_item_three_dots_menu
    }

    private var binding: SmvcItemThreeDotsMenuBinding? by viewBinding()
    override fun bind(menu: MoreMenuUiModel) {
        binding?.apply {
            bottomSheetText.text = menu.title?.getString(itemView.context).toBlankOrString()
            bottomSheetText.isEnabled = menu.enabled

            icon.setImage(menu.icon)
            icon.isEnabled = menu.enabled

            root.setOnClickListener {
                if (menu.enabled) {
                    callback(menu)
                }
            }
        }
    }
}
