package com.tokopedia.developer_options.presentation.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.hansel.HanselActivity
import com.tokopedia.developer_options.presentation.model.ViewHanselPatchUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewHanselPatchViewHolder(itemView: View): AbstractViewHolder<ViewHanselPatchUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_hansel_patch
    }

    override fun bind(element: ViewHanselPatchUiModel?) {
        val button = itemView.findViewById<UnifyButton>(R.id.btn_hansel_patch)
        button.text = itemView.context.resources.getString(com.tokopedia.developer_options.R.string.title_button_hansel_patch_list)
        button.setOnClickListener {
            val intent = Intent(itemView.context, HanselActivity::class.java)
            itemView.context.startActivity(intent)
        }
    }
}
