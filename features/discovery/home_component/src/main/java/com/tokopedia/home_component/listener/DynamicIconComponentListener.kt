package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.DynamicIconComponent

/**
 * Created by Lukas on 1/8/21.
 */
interface DynamicIconComponentListener {
    fun onClickIcon(dynamicIcon: DynamicIconComponent.DynamicIcon)
    fun onImpressIcon(dynamicIcon: DynamicIconComponent.DynamicIcon)
}