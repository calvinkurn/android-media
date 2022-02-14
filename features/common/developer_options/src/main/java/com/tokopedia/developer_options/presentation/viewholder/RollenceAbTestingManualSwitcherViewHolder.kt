package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.RollenceAbTestingManualSwitcherUiModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class RollenceAbTestingManualSwitcherViewHolder(
    itemView: View
): AbstractViewHolder<RollenceAbTestingManualSwitcherUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_rollence_ab_testing_manual_switcher
    }

    override fun bind(element: RollenceAbTestingManualSwitcherUiModel) {
        val tfRollenceKey = itemView.findViewById<TextFieldUnify>(R.id.input_rollence_key_tf)
        val tfRollenceVariant = itemView.findViewById<TextFieldUnify>(R.id.input_rollence_variant_tf)
        val btn = itemView.findViewById<UnifyButton>(R.id.apply_rollence_btn)
        btn.setOnClickListener {
            val rollenceKey = tfRollenceKey.textFieldInput.text
            val rollenceVariant = tfRollenceVariant.textFieldInput.text
            when {
                rollenceKey.isEmpty() -> {
                    Toast.makeText(itemView.context, "Please Insert Rollence Key", Toast.LENGTH_SHORT).show()
                }
                rollenceVariant.isEmpty() -> {
                    Toast.makeText(itemView.context, "Please Insert Rollence Variant", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    RemoteConfigInstance.getInstance().abTestPlatform.setString(rollenceKey.toString().trim { it <= ' ' }, rollenceVariant.toString().trim { it <= ' ' })
                    Toast.makeText(itemView.context, "Rollence Key Applied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}