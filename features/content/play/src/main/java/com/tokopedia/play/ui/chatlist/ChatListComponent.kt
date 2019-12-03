package com.tokopedia.play.ui.chatlist

import android.view.ViewGroup
import com.tokopedia.play.component.UIComponent
import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 03/12/19
 */
class ChatListComponent(
        container: ViewGroup
) : UIComponent<Unit> {

    private val uiView = initView(container)

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<Unit> {
        throw IllegalArgumentException()
    }

    private fun initView(container: ViewGroup): ChatListView =
            ChatListView(container)
}