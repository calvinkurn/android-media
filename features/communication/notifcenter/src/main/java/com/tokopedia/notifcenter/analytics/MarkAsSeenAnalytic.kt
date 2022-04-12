package com.tokopedia.notifcenter.analytics

import javax.inject.Inject

class MarkAsSeenAnalytic @Inject constructor() {

    private val trackedNotif = mutableSetOf<String>()
    private val queueNotif = mutableListOf<String>()

    fun markAsSeen(notifId: String) {
        if (trackedNotif.add(notifId)) {
            queueNotif.add(notifId)
        }
    }

    fun dequeue(): ArrayList<String>? {
        val notifIds = ArrayList(queueNotif)
        queueNotif.clear()
        return notifIds
    }

    fun isEmpty(): Boolean {
        return queueNotif.isEmpty()
    }

}