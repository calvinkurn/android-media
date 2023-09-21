package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.UserIdUiModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

class UserIdViewHolder(
    itemView: View,
    private val listener: UserIdListener
) : AbstractViewHolder<UserIdUiModel>(itemView) {

    override fun bind(element: UserIdUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.user_id)
        btn.text = String.format(Locale.getDefault(), "User Id: ${listener.getUserId()}")
        btn.setOnClickListener {
            listener.onClickUserIdButton()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_user_id
    }

    interface UserIdListener {
        fun onClickUserIdButton()
        fun getUserId(): String
    }
}
