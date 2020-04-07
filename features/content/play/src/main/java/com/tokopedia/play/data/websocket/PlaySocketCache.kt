package com.tokopedia.play.data.websocket


/**
 * Created by mzennis on 2020-02-25.
 */
class PlaySocketCache {

    var chatList: MutableList<Chat> = arrayListOf()

    fun putChat(chat: Chat) {
        chatList.add(chat)
    }

    fun clear() {
        val iterate = chatList.listIterator()
        while (iterate.hasNext()) {
            val currentItem = iterate.next()
            if (currentItem.isDeleted) iterate.remove()
        }
    }

    data class Chat(
            val message: String,
            var isDeleted: Boolean = false
    )
}