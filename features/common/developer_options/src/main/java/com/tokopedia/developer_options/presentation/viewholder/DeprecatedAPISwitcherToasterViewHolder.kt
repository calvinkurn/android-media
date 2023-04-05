package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.DeprecatedApiSwitcherToasterUiModel
import com.tokopedia.developer_options.utils.getIsEnableDeprecatedAPISwitcherToaster
import com.tokopedia.developer_options.utils.setIsEnableDeprecatedAPISwitcherToaster

import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class DeprecatedAPISwitcherToasterViewHolder (
    itemView: View
) : AbstractViewHolder<DeprecatedApiSwitcherToasterUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_deprecated_api_switcher_toaster
    }

    override fun bind(element: DeprecatedApiSwitcherToasterUiModel) {
        itemView.context?.apply {
            val cb = itemView.findViewById<CheckboxUnify>(R.id.switcher_toaster_cb)
            cb.isChecked = getIsEnableDeprecatedAPISwitcherToaster()


            cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
                setIsEnableDeprecatedAPISwitcherToaster(state)
                Toast.makeText(this, "Please Restart the App", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
