package com.tokopedia.people.views.compose

import androidx.compose.runtime.Composable
import com.tokopedia.header.compose.HeaderOptionals
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.people.views.uimodel.state.HeaderUiState

/**
 * Created by Jonathan Darwin on 28 February 2024
 */
@Composable
fun UserProfileHeader(
    headerState: HeaderUiState,
    optionsButton: List<HeaderOptionals>,
    onBackClicked: () -> Unit,
) {
    NestTheme(
        isOverrideStatusBarColor = false,
    ) {
        NestHeader(
            type = when (headerState) {
                is HeaderUiState.ShowUserInfo -> {
                    if (headerState.username.isEmpty()) {
                        NestHeaderType.SingleLine(
                            title = headerState.name,
                            showBackButton = true,
                            onBackClicked = onBackClicked,
                            optionsButton = optionsButton,
                        )
                    } else {
                        NestHeaderType.DoubleLine(
                            title = headerState.name,
                            subTitle = headerState.username,
                            showBackButton = true,
                            onBackClicked = onBackClicked,
                            optionsButton = optionsButton,
                        )
                    }
                }
                is HeaderUiState.HideUserInfo -> {
                    NestHeaderType.SingleLine(
                        title = "",
                        showBackButton = true,
                        onBackClicked = onBackClicked,
                        optionsButton = optionsButton,
                    )
                }
            }
        )
    }
}
