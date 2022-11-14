package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_DEFAULT_TOGGLE
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_KEY
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_SP_NAME
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.STRICT_MODE_LEAK_PUBLISHER_DEFAULT_TOGGLE
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY
import com.tokopedia.developer_options.presentation.model.LeakCanaryUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class StrictModeLeakPublisherViewHolder(
    itemView: View
): AbstractViewHolder<LeakCanaryUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_strict_mode_leak_publisher
    }

    override fun bind(element: LeakCanaryUiModel) {
        itemView.context?.apply {
            val sharedPref = getSharedPreferences(
                LEAK_CANARY_TOGGLE_SP_NAME,
                BaseActivity.MODE_PRIVATE
            )
            val cb = itemView.findViewById<CheckboxUnify>(R.id.strict_mode_cb)
            cb.isChecked = sharedPref.getBoolean(STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY, STRICT_MODE_LEAK_PUBLISHER_DEFAULT_TOGGLE)
            cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
                val editor = sharedPref.edit().putBoolean(STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY, state)
                editor.apply()
                Toast.makeText(this, "Please Restart the App", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
