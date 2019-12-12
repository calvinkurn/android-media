package com.tokopedia.play.ui.stats

import android.view.ViewGroup
import com.tokopedia.play.component.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Created by jegul on 03/12/19
 */
class StatsComponent(
        container: ViewGroup
) : UIComponent<Unit> {

    private val uiView = initView(container)

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<Unit> {
        return emptyFlow()
    }

    private fun initView(container: ViewGroup): StatsView =
            StatsView(container)
}