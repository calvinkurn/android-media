package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeSwitcherBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeSwitcherViewHolder(
    itemView: View,
    private val listener: HomeSwitcherListener?
): AbstractViewHolder<HomeSwitcherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_switcher
    }

    private var binding: ItemTokopedianowHomeSwitcherBinding? by viewBinding()

    override fun bind(uiModel: HomeSwitcherUiModel) {
        binding?.run {
            textTitle.text = itemView.context.getString(uiModel.title)
            textSubtitle.text = itemView.context.getString(uiModel.subtitle)
            ivIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, uiModel.icon))
            root.setOnClickListener { listener?.onClickSwitcher() }
        }
    }

    interface HomeSwitcherListener {
        fun onClickSwitcher()
    }
}