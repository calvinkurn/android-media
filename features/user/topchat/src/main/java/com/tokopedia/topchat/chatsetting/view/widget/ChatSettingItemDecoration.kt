package com.tokopedia.topchat.chatsetting.view.widget

import android.content.Context
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration

class ChatSettingItemDecoration(context: Context?) : DividerItemDecoration(context) {
    override fun shouldDrawOnLastItem(): Boolean {
        return true
    }
}