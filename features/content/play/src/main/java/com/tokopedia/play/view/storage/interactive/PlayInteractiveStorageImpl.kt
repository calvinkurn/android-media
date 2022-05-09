package com.tokopedia.play.view.storage.interactive

import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import javax.inject.Inject

/**
 * Created by jegul on 01/07/21
 */
class PlayInteractiveStorageImpl @Inject constructor() : PlayInteractiveStorage {

    private val joinedSet = mutableSetOf<Long>()

    private val interactiveStatusMap = mutableMapOf<String, InteractiveStatus>()

    /**
     * Detail resembles the detail when inputted
     */
    private val interactiveDetailMap = mutableMapOf<String, InteractiveUiModel>()

    override fun save(model: InteractiveUiModel) {
        interactiveDetailMap[model.id] = model
    }

    override fun setJoined(id: Long) {
        joinedSet.add(id)
    }

    override fun hasJoined(id: Long): Boolean {
        return joinedSet.contains(id)
    }

    override fun setDetail(interactiveId: String, model: PlayCurrentInteractiveModel) {
//        interactiveDetailMap[interactiveId] = model
    }

    override fun setActive(interactiveId: String) {
        if (!interactiveStatusMap.containsKey(interactiveId)) {
            interactiveStatusMap.entries.forEach { entry ->
                val currValue = entry.value
                entry.setValue(currValue.copy(isActive = false))
            }
            interactiveStatusMap[interactiveId] = InteractiveStatus(isActive = true, isJoined = false)
        }
    }

    override fun setFinished(interactiveId: String) {
//        if (interactiveStatusMap.containsKey(interactiveId)) {
//            val currentStatus = interactiveStatusMap[interactiveId]!!
//            interactiveStatusMap[interactiveId] = currentStatus.copy(isActive = false)
//        }
    }

    override fun getDetail(interactiveId: String): PlayCurrentInteractiveModel? {
//        return interactiveDetailMap[interactiveId]
        return null
    }

    override fun getActiveInteractiveId(): String? {
        return interactiveStatusMap.entries.firstOrNull { it.value.isActive }?.key
    }

    override fun setJoined(id: String) {
        if (interactiveStatusMap.containsKey(id)) {
            val currentStatus = interactiveStatusMap[id]!!
            interactiveStatusMap[id] = currentStatus.copy(isJoined = true)
        }
    }

    override fun hasJoined(id: String): Boolean {
        return interactiveStatusMap[id]?.isJoined ?: false
    }

    data class InteractiveStatus(val isActive: Boolean, val isJoined: Boolean)
}