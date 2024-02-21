package com.tokopedia.discovery.common.analytics

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object SearchSessionObserver: DefaultLifecycleObserver {

    private val groupIdList: MutableList<MutableList<String>> = mutableListOf()

    var sessionId: String = ""; private set

    private val idList
        get() = groupIdList.flatten()

    private val idIndex: Int
        get() = idList.lastIndex

    val id: String
        get() = idList.getOrNull(idIndex) ?: ""

    private val previousIdIndex: Int
        get() = idIndex - 1

    val previousId: String
        get() = idList.getOrNull(previousIdIndex) ?: ""

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        groupIdList.add(mutableListOf())
        updateSessionId()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        groupIdList.removeLast()
        updateSessionId()

        owner.lifecycle.removeObserver(this)
    }

    fun appendId(searchId: String) {
        groupIdList.lastOrNull()?.add(searchId)
    }

    private fun updateSessionId() {
        if (groupIdList.size == 1)
            sessionId = System.currentTimeMillis().toString()
        else if (groupIdList.size == 0)
            sessionId = ""
    }
}
