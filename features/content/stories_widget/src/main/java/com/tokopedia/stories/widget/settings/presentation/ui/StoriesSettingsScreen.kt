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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.stories.widget.R
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsAction
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsViewModel
import com.tokopedia.unifycomponents.compose.NestCheckbox
import com.tokopedia.unifycomponents.compose.NestSwitch

/**
 * @author by astidhiyaa on 3/22/24
 */
@Composable
internal fun StoriesSettingsScreen(viewModel: StoriesSettingsViewModel) {
    val pageInfo by viewModel.pageInfo.collectAsState(initial = StoriesSettingsPageUiModel.Empty)
    val isStoryEnable = pageInfo.options.any { it.isSelected }
    NestTheme(isOverrideStatusBarColor = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                        text = pageInfo.options.firstOrNull()?.text.orEmpty(),
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
                            SettingOptItem(item) {
                                viewModel.onEvent(StoriesSettingsAction.SelectOption(it))
                            }
                        }
                    }
                }
                NestSwitch(
                    isChecked = isStoryEnable,
                    onCheckedChanged = {
                        viewModel.onEvent(
                            StoriesSettingsAction.SelectOption(
                                pageInfo.options.firstOrNull() ?: return@NestSwitch
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingOptItem(item: StoriesSettingOpt, onOptionClicked: (StoriesSettingOpt) -> Unit) {
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
        NestCheckbox(isChecked = item.isSelected, onCheckedChange = {
            onOptionClicked(item)
        })
    }
}

data class StoriesSettingOpt(
    val text: String,
    val optionType: String,
    val isSelected: Boolean
)

data class StoriesSettingConfig(
    val articleCopy: String,
    val articleAppLink: String,
    val articleWebLink: String
)

data class StoriesSettingsPageUiModel(
    val options: List<StoriesSettingOpt>,
    val config: StoriesSettingConfig
) {
    companion object {
        val Empty
            get() = StoriesSettingsPageUiModel(
                options = emptyList(),
                config = StoriesSettingConfig("", "", "")
            )
    }
}
