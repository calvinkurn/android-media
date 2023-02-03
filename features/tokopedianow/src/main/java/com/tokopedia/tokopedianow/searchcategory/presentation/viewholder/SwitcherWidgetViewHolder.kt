package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ImageUtil.setBackgroundImage
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategorySwitcherWidgetBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import com.tokopedia.utils.view.binding.viewBinding

class SwitcherWidgetViewHolder(
    itemView: View,
    private val listener: SwitcherWidgetListener? = null
): AbstractViewHolder<SwitcherWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_switcher_widget
        private const val SWITCHER_WIDGET_BG_URL = TokopediaImageUrl.SWITCHER_WIDGET_BG_URL
        private const val SWITCHER_WIDGET_CARD_2H_BG_URL = TokopediaImageUrl.SWITCHER_WIDGET_CARD_2H_BG_URL
        private const val SWITCHER_WIDGET_CARD_20M_BG_URL = TokopediaImageUrl.SWITCHER_WIDGET_CARD_20M_BG_URL
    }

    private var binding: ItemTokopedianowSearchCategorySwitcherWidgetBinding? by viewBinding()

    override fun bind(element: SwitcherWidgetDataView) {
        setImage()
        setText()
        setListener()
    }

    private fun setImage() {
        binding?.root?.context?.let {
            setBackgroundImage(it, SWITCHER_WIDGET_BG_URL, binding?.switcherLayout)
            setBackgroundImage(it, SWITCHER_WIDGET_CARD_2H_BG_URL, binding?.container2h)
            setBackgroundImage(it, SWITCHER_WIDGET_CARD_20M_BG_URL, binding?.container20m)
        }
    }

    private fun setText() {
        binding?.apply {
            tpDesc20m.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_search_category_description_twenty_minutes))
            tpDesc2h.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_search_category_description_two_hours))
        }
    }

    private fun setListener() {
        binding?.apply {
            cv20m.setOnClickListener {
                listener?.onClickSwitcherTo15M()
            }
            cv2h.setOnClickListener {
                listener?.onClickSwitcherTo2H()
            }
        }
    }
}