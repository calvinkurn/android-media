package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_KEY
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_KEY_SELLER
import com.tokopedia.developer_options.presentation.model.LeakCanaryUiModel
import com.tokopedia.developer_options.utils.getIsEnableSharedPrefLeak
import com.tokopedia.developer_options.utils.setIsEnableSharedPrefLeak
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class LeakCanaryViewHolder(
    itemView: View
) : AbstractViewHolder<LeakCanaryUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_leak_canary
    }

    override fun bind(element: LeakCanaryUiModel) {
        itemView.context?.apply {
            val cb = itemView.findViewById<CheckboxUnify>(R.id.leak_canary_cb)
            cb.isChecked =
                getIsEnableSharedPrefLeak(LEAK_CANARY_TOGGLE_KEY, LEAK_CANARY_TOGGLE_KEY_SELLER)

            cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
                setIsEnableSharedPrefLeak(
                    LEAK_CANARY_TOGGLE_KEY,
                    LEAK_CANARY_TOGGLE_KEY_SELLER,
                    state
                )
                Toast.makeText(this, "Please Restart the App", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
