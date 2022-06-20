package com.tokopedia.play.view.storage.interactive

import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel

/**
 * Created by jegul on 01/07/21
 */
interface PlayInteractiveStorage {

    fun save(model: InteractiveUiModel)

    fun setJoined(id: String)

    fun hasJoined(id: String): Boolean

    fun setHasProcessedWinner(interactiveId: String)

    fun hasProcessedWinner(interactiveId: String): Boolean

    /////////

    fun setDetail(interactiveId: String, model: PlayCurrentInteractiveModel)

    fun setActive(interactiveId: String)

    fun getDetail(interactiveId: String): PlayCurrentInteractiveModel?

    fun getActiveInteractiveId(): String?


}