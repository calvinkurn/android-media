package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.ab_test_rollence.AbTestRollenceConfigFragmentActivity
import com.tokopedia.developer_options.presentation.model.RollenceAbTestingManualSwitcherUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class RollenceAbTestingManualSwitcherViewHolder(
    itemView: View
) : AbstractViewHolder<RollenceAbTestingManualSwitcherUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_rollence_ab_testing_manual_switcher
    }

    override fun bind(element: RollenceAbTestingManualSwitcherUiModel) {
        val tfRollenceKey = itemView.findViewById<TextFieldUnify>(R.id.input_rollence_key_tf)
        val tfRollenceVariant = itemView.findViewById<TextFieldUnify>(R.id.input_rollence_variant_tf)
        val btnApplyRollence = itemView.findViewById<UnifyButton>(R.id.apply_rollence_btn)
        val btnListAbTest = itemView.findViewById<UnifyButton>(R.id.list_ab_test_btn)

        btnApplyRollence.setOnClickListener {
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
                    DevOpsTracker.trackEntryEvent(DevopsFeature.AB_TEST_MANUAL_APPLY)
                    RemoteConfigInstance.getInstance().abTestPlatform.setString(rollenceKey.toString().trim { it <= ' ' }, rollenceVariant.toString().trim { it <= ' ' })
                    Toast.makeText(itemView.context, "Rollence Key Applied", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnListAbTest.setOnClickListener {
            itemView.context.apply {
                val intent = Intent(this, AbTestRollenceConfigFragmentActivity::class.java)
                DevOpsTracker.trackEntryEvent(DevopsFeature.VIEW_AB_TEST_LIST)
                startActivity(intent)
            }
        }
    }
}
