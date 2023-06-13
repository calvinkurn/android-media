package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY_SELLER
import com.tokopedia.developer_options.presentation.model.StrictModeLeakPublisherUiModel
import com.tokopedia.developer_options.utils.getIsEnableSharedPrefLeak
import com.tokopedia.developer_options.utils.setIsEnableSharedPrefLeak
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class StrictModeLeakPublisherViewHolder(
    itemView: View
) : AbstractViewHolder<StrictModeLeakPublisherUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_strict_mode_leak_publisher
    }

    override fun bind(element: StrictModeLeakPublisherUiModel) {
        itemView.context?.apply {
            val cb = itemView.findViewById<CheckboxUnify>(R.id.strict_mode_cb)
            cb.isChecked = getIsEnableSharedPrefLeak(
                STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY,
                STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY_SELLER
            )
            cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
                setIsEnableSharedPrefLeak(
                    STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY,
                    STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY_SELLER,
                    state
                )

                Toast.makeText(this, "Please Restart the App", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
