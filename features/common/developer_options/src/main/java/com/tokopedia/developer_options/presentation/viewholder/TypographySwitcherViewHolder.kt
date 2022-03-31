package com.tokopedia.developer_options.presentation.viewholder

import android.app.Activity
import android.view.View
import android.widget.CheckBox
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.TypographySwitchUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class TypographySwitcherViewHolder (view: View): AbstractViewHolder<TypographySwitchUiModel>(view) {

    private val checkbox = itemView.findViewById<CheckBox>(R.id.dev_opt_new_typography)

    override fun bind(element: TypographySwitchUiModel?) {
        checkbox.isChecked = Typography.isFontTypeOpenSauceOne

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            Typography.isFontTypeOpenSauceOne = isChecked
            (itemView.context as Activity).recreate()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_typography_switcher
    }
}