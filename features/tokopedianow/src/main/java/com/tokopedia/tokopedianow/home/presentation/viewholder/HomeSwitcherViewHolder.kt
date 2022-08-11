package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
            tpTitle.text = MethodChecker.fromHtml(getString(uiModel.title))
            tpSubtitle.text = MethodChecker.fromHtml(getString(uiModel.subtitle))

            val drawable = VectorDrawableCompat.create(
                itemView.context.resources,
                R.drawable.tokopedianow_bg_switcher_switching_part,
                itemView.context.theme
            )
            aivIntendedService.background = drawable

            cvIntendedService.setOnClickListener {
                listener?.onClickSwitcher()
            }

            itemView.addOnImpressionListener(uiModel) {
                listener?.onImpressSwitcher()
            }
        }
    }

    interface HomeSwitcherListener {
        fun onClickSwitcher()
        fun onImpressSwitcher()
    }
}