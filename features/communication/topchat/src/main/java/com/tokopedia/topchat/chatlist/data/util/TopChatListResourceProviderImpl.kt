package com.tokopedia.topchat.chatlist.data.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class TopChatListResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
): TopChatListResourceProvider {
    override fun getStringResource(res: Int): String {
        return context.getString(res)
    }
}
