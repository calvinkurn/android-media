package com.tokopedia.stories.widget.settings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.globalerror.compose.NestGlobalError
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.stories.widget.R
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsAction
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsViewModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import java.net.UnknownHostException

/**
 * @author by astidhiyaa on 3/22/24
 */
@Composable
internal fun StoriesSettingsScreen(viewModel: StoriesSettingsViewModel) {
    NestTheme(isOverrideStatusBarColor = false) {
        val pageInfo by viewModel.pageInfo.collectAsState(initial = StoriesSettingsPageUiModel.Empty)

        when (val state = pageInfo.state) {
            ResultState.Success -> StoriesSettingsSuccess(
                viewModel = viewModel,
                pageInfo = pageInfo
            )

            is ResultState.Fail -> StoriesSettingsError(error = state.error, viewModel = viewModel)
            else -> {}
        }
    }
}

@Composable
private fun StoriesSettingsSuccess(
    pageInfo: StoriesSettingsPageUiModel,
    viewModel: StoriesSettingsViewModel
) {
    val isStoryEnable = pageInfo.options.any { it.isSelected }
    val isEligible = pageInfo.config.isEligible
    val itemFirst = pageInfo.options.firstOrNull() ?: StoriesSettingOpt("", "", false)

    val ctx = LocalContext.current

    var checked by remember { mutableStateOf(isStoryEnable) }

    if (isEligible.not()) {
        AndroidView(modifier = Modifier.padding(16.dp), factory = {
            Ticker(it).apply {
                tickerType = Ticker.TYPE_ANNOUNCEMENT
                setTextDescription(ctx.getString(R.string.stories_ticker_title))
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .alpha(if (!pageInfo.config.isEligible) 0.4f else 1f)
    ) {
        NestTypography(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(id = R.string.stories_settings_header),
            textStyle = NestTheme.typography.display1.copy(fontWeight = FontWeight.Bold)
        )
        Row {
            NestIcon(iconId = IconUnify.SOCIAL_STORY, modifier = Modifier.padding(end = 12.dp))
            Column {
                NestTypography(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = itemFirst.text,
                    textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold)
                )
                NestTypography(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = pageInfo.config.articleCopy,
                    textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
                )
                NestTypography(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = stringResource(id = R.string.stories_settings_body),
                    textStyle = NestTheme.typography.paragraph3
                )

                LazyColumn {
                    items(pageInfo.options.drop(1)) { item ->
                        SettingOptItem(item, isEligible) {
                            viewModel.onEvent(StoriesSettingsAction.SelectOption(it))
                        }
                    }
                }
            }
            AndroidView(
                factory = { context ->
                    SwitchUnify(context).apply {
                        this.setOnCheckedChangeListener { view, isActive ->
                            if (view.isPressed) {
                                checked = isActive
                                viewModel.onEvent(
                                    StoriesSettingsAction.SelectOption(itemFirst)
                                )
                            }
                        }
                    }
                },
                update = { switchUnify ->
                    switchUnify.isChecked = checked
                    switchUnify.isEnabled = isEligible
                },
            )
        }
    }
}

@Composable
private fun StoriesSettingsError(error: Throwable, viewModel: StoriesSettingsViewModel) {
    val (text, type, action) = when (error) {
        is UnknownHostException -> Triple(
            stringResource(id = R.string.stories_settings_page_header),
            NestGlobalErrorType.NoConnection,
            {})

        else -> Triple("", NestGlobalErrorType.PageNotFound) {
            viewModel.onEvent(
                StoriesSettingsAction.FetchPageInfo
            )
        }
    }
    NestGlobalError(type = type, secondaryActionText = text) { action() }
}

@Composable
private fun SettingOptItem(
    item: StoriesSettingOpt,
    isEligible: Boolean,
    onOptionClicked: (StoriesSettingOpt) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NestTypography(
            text = item.text,
            textStyle = NestTheme.typography.paragraph3
        )
        AndroidView(
            factory = { context ->
                CheckboxUnify(context).apply {
                    this.setOnCheckedChangeListener { view, _ ->
                        if (view.isPressed) {
                            onOptionClicked(item)
                        }
                    }
                }
            },
            update = { v ->
                v.isChecked = item.isSelected
                v.isEnabled = isEligible
            },
        )
    }
}

data class StoriesSettingOpt(
    val text: String,
    val optionType: String,
    val isSelected: Boolean,
)

data class StoriesSettingConfig(
    val articleCopy: String,
    val articleAppLink: String,
    val articleWebLink: String,
    val isEligible: Boolean,
)

data class StoriesSettingsPageUiModel(
    val options: List<StoriesSettingOpt>,
    val config: StoriesSettingConfig,
    val state: ResultState,
) {
    companion object {
        val Empty
            get() = StoriesSettingsPageUiModel(
                options = emptyList(),
                config = StoriesSettingConfig("", "", "", false),
                state = ResultState.Loading,
            )
    }
}
