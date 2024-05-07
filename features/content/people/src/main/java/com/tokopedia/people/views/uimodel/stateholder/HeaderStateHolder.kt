package com.tokopedia.people.views.uimodel.stateholder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tokopedia.config.GlobalConfig
import com.tokopedia.header.compose.HeaderActionButton
import com.tokopedia.header.compose.HeaderOptionals
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.nest.principles.utils.IconSource
import com.tokopedia.people.views.uimodel.state.HeaderUiState

/**
 * Created by Jonathan Darwin on 28 February 2024
 */
class HeaderStateHolder {

    var state by mutableStateOf<HeaderUiState>(HeaderUiState.HideUserInfo)

    fun getOptions(
        onShareClicked: () -> Unit,
        onMenuClicked: () -> Unit,
    ): List<HeaderOptionals> {
        return mutableListOf<HeaderOptionals>().apply {
            add(
                HeaderActionButton(
                    icon = IconSource.Nest(IconUnify.SOCIAL_SHARE),
                    onClicked = onShareClicked,
                )
            )

            if (!GlobalConfig.isSellerApp()) {
                add(
                    HeaderActionButton(
                        icon = IconSource.Nest(IconUnify.MENU_HAMBURGER),
                        onClicked = onMenuClicked,
                    )
                )
            }
        }
    }
}
