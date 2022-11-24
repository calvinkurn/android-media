package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_DEFAULT_TOGGLE
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_KEY
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_KEY_SELLER
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_SP_NAME
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.LEAK_CANARY_TOGGLE_SP_NAME_SELLER
import com.tokopedia.developer_options.presentation.model.LeakCanaryUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class LeakCanaryViewHolder(
    itemView: View
): AbstractViewHolder<LeakCanaryUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_leak_canary
    }

    override fun bind(element: LeakCanaryUiModel) {
        itemView.context?.apply {
            val sharedPref = getSharedPreferences(
                LEAK_CANARY_TOGGLE_SP_NAME_SELLER,
                BaseActivity.MODE_PRIVATE
            )
            val cb = itemView.findViewById<CheckboxUnify>(R.id.leak_canary_cb)
            if (GlobalConfig.isSellerApp()){
                cb.isChecked = sharedPref.getBoolean(LEAK_CANARY_TOGGLE_KEY_SELLER, LEAK_CANARY_DEFAULT_TOGGLE)
            }else{
                cb.isChecked = sharedPref.getBoolean(LEAK_CANARY_TOGGLE_KEY, LEAK_CANARY_DEFAULT_TOGGLE)
            }
            cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
                if (GlobalConfig.isSellerApp()){
                    val editor = sharedPref.edit().putBoolean(LEAK_CANARY_TOGGLE_KEY_SELLER, state)
                    editor.apply()
                }else{
                    val editor = sharedPref.edit().putBoolean(LEAK_CANARY_TOGGLE_KEY, state)
                    editor.apply()
                }
                Toast.makeText(this, "Please Restart the App", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
