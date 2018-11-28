package com.tokopedia.chatbot.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by nisie on 27/11/18.
 */
interface ChatbotTypeFactory {

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}