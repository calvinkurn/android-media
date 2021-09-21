package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel

/**
 * Created by Lukas on 1/8/21.
 */
interface DynamicIconComponentListener {
    fun onClickIcon(dynamicIcon: DynamicIconComponent.DynamicIcon, position: Int, iconPosition: Int, type: Int)
    fun onImpressIcon(dynamicIcon: DynamicIconComponent.DynamicIcon, iconPosition: Int, parentPosition: Int, type: Int)
    fun onIconChannelImpressed(iconComponentModel: DynamicIconComponentDataModel, parentPosition: Int)
}