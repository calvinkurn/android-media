package com.tokopedia.home_component.widget.mission

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface MissionWidgetVisitable: Visitable<MissionWidgetTypeFactory> {
    fun getId(): String
    fun equalsWith(visitable: MissionWidgetVisitable): Boolean
    fun getChangePayloadFrom(visitable: MissionWidgetVisitable): Bundle = Bundle()
}
