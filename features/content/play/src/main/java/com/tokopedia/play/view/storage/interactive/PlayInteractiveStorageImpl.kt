package com.tokopedia.play.view.storage.interactive

import javax.inject.Inject

/**
 * Created by jegul on 01/07/21
 */
class PlayInteractiveStorageImpl @Inject constructor() : PlayInteractiveStorage {

    private val interactiveMap = mutableMapOf<String, InteractiveStatus>()

    override fun setActive(interactiveId: String) {
        if (!interactiveMap.containsKey(interactiveId)) {
            interactiveMap.entries.forEach { entry ->
                val currValue = entry.value
                entry.setValue(currValue.copy(isActive = false))
            }
            interactiveMap[interactiveId] = InteractiveStatus(isActive = true, isJoined = false)
        }
    }

    override fun setInactive(interactiveId: String) {
        if (interactiveMap.containsKey(interactiveId)) {
            val currentStatus = interactiveMap[interactiveId]!!
            interactiveMap[interactiveId] = currentStatus.copy(isActive = false)
        }
    }

    override fun setJoined(interactiveId: String) {
        if (interactiveMap.containsKey(interactiveId)) {
            val currentStatus = interactiveMap[interactiveId]!!
            interactiveMap[interactiveId] = currentStatus.copy(isJoined = true)
        }
    }

    override fun getActiveInteractiveId(): String? {
        return interactiveMap.entries.firstOrNull { it.value.isActive }?.key
    }

    override fun hasJoined(interactiveId: String): Boolean {
        return interactiveMap[interactiveId]?.isJoined ?: false
    }

    data class InteractiveStatus(val isActive: Boolean, val isJoined: Boolean)
}