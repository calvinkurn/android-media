package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.hansel.HanselActivity
import com.tokopedia.developer_options.presentation.model.ViewHanselPatchUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ViewHanselPatchViewHolder(itemView: View): AbstractViewHolder<ViewHanselPatchUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_hansel_patch

        private const val KEY_IS_TEST_DEVICE = "is_in_testgroup"
        private const val PREFERENCE_NAME_HANSEL = "PREFERENCE_NAME_HANSEL"
    }

    override fun bind(element: ViewHanselPatchUiModel?) {
        val prefs = itemView.context.getSharedPreferences(PREFERENCE_NAME_HANSEL, Context.MODE_PRIVATE)
        val isTestDevice = prefs.getBoolean(KEY_IS_TEST_DEVICE, false)
        val tvTestDevice = itemView.findViewById<Typography>(R.id.tv_is_test_device)
        val button = itemView.findViewById<UnifyButton>(R.id.btn_hansel_patch)
        tvTestDevice.text = itemView.context.getString(R.string.label_is_device_test, isTestDevice.toString())
        button.text = itemView.context.resources.getString(com.tokopedia.developer_options.R.string.title_button_hansel_patch_list)
        button.setOnClickListener {
            val intent = Intent(itemView.context, HanselActivity::class.java)
            itemView.context.startActivity(intent)
        }
    }
}
