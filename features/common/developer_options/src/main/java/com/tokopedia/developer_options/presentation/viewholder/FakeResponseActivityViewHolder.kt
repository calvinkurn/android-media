package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.fakeresponse.FakeResponseActivityProvider
import com.tokopedia.developer_options.presentation.model.FakeResponseActivityUiModel
import com.tokopedia.unifycomponents.UnifyButton

class FakeResponseActivityViewHolder(
    itemView: View
): AbstractViewHolder<FakeResponseActivityUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_fake_response_activity
    }

    override fun bind(element: FakeResponseActivityUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.fake_response_btn)
        btn.setOnClickListener {
            FakeResponseActivityProvider().startActivity(itemView.context);
        }
    }
}