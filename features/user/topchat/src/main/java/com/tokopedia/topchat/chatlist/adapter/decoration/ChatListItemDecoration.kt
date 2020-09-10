package com.tokopedia.topchat.chatlist.adapter.decoration

import android.content.Context
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.topchat.R

class ChatListItemDecoration(context: Context?) : DividerItemDecoration(context) {

    override fun getDimenPaddingLeft(): Int {
        return R.dimen.dp_topchat_80
    }

}