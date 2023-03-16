package com.tokopedia.play.view.storage.interactive

import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import javax.inject.Inject

/**
 * Created by jegul on 01/07/21
 */
class PlayInteractiveStorageImpl @Inject constructor() : PlayInteractiveStorage {

    private val joinedSet = mutableSetOf<String>()
    private val hasProcessedWinnerSet = mutableSetOf<String>()

    private val interactiveStatusMap = mutableMapOf<String, InteractiveStatus>()

    /**
     * Detail resembles the detail when inputted
     */
    private val interactiveDetailMap = mutableMapOf<String, GameUiModel>()

    override fun save(model: GameUiModel) {
        interactiveDetailMap[model.id] = model
    }

    override fun setJoined(id: String) {
        joinedSet.add(id)
    }

    override fun hasJoined(id: String): Boolean {
        return joinedSet.contains(id)
    }

    override fun setHasProcessedWinner(interactiveId: String) {
        hasProcessedWinnerSet.add(interactiveId)
    }

    override fun hasProcessedWinner(interactiveId: String): Boolean {
        return hasProcessedWinnerSet.contains(interactiveId)
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

    override fun getDetail(interactiveId: String): PlayCurrentInteractiveModel? {
//        return interactiveDetailMap[interactiveId]
        return null
    }

    override fun getActiveInteractiveId(): String? {
        return interactiveStatusMap.entries.firstOrNull { it.value.isActive }?.key
    }

    data class InteractiveStatus(val isActive: Boolean, val isJoined: Boolean)
}
