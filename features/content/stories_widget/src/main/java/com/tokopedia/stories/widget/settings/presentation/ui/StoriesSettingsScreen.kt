package com.tokopedia.stories.widget.settings.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
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
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.coroutines.delay
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
                viewModel = viewModel, pageInfo = pageInfo
            )

            is ResultState.Fail -> StoriesSettingsError(error = state.error, viewModel = viewModel)
            else -> {}
        }
    }
}

val finalText = "Selengkapnya"
val tagLink = "{{Selengkapnya}}"

@Composable
private fun StoriesSettingsSuccess(
    pageInfo: StoriesSettingsPageUiModel, viewModel: StoriesSettingsViewModel
) {
    val isStoryEnable = pageInfo.options.drop(1).any { it.isSelected }
    val isEligible = pageInfo.config.isEligible
    val itemFirst = pageInfo.options.firstOrNull() ?: StoriesSettingOpt("", "", false)

    var checked by remember { mutableStateOf(isStoryEnable) }
    var dismissed by remember { mutableStateOf(false) }
    var coolingDown by remember { mutableStateOf(false) }

    val textColor = if (isEligible) NestTheme.colors.NN._950 else NestTheme.colors.NN._400

    val ctx = LocalContext.current

    LaunchedEffect(coolingDown) {
        if (coolingDown) {
            delay(5000L)
            coolingDown = false
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val (tickerEligible, tvHeader, ivIconHeader, switchHeader, tvDescription, tvAll, tvCategory, rvOptions) = createRefs()
        if (isEligible.not() && dismissed.not()) {
            AndroidView(modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .constrainAs(tickerEligible) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }, factory = {
                Ticker(it).apply {
                    tickerType = Ticker.TYPE_ANNOUNCEMENT
                    tickerShape = Ticker.SHAPE_LOOSE
                    tickerTitle = ""
                    setTextDescription(ctx.getString(R.string.stories_ticker_title))
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDismiss() {
                            dismissed = true
                        }

                        override fun onDescriptionViewClick(linkUrl: CharSequence) {}
                    })
                }
            })
        }

        //Header - Story
        NestTypography(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .constrainAs(tvHeader) {
                    top.linkTo(tickerEligible.bottom)
                    start.linkTo(tickerEligible.start)
                },
            text = stringResource(id = R.string.stories_settings_header),
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold, color = textColor
            )
        )

        //Icon - Mobile
        NestIcon(iconId = IconUnify.SOCIAL_STORY,
            colorNightDisable = textColor,
            colorLightEnable = textColor,
            modifier = Modifier.constrainAs(ivIconHeader) {
                top.linkTo(tvHeader.bottom)
                start.linkTo(tvHeader.start)
            })

        //Text - Buat Stories Otomatis
        NestTypography(
            modifier = Modifier
                .padding(start = 12.dp)
                .constrainAs(tvDescription) {
                    start.linkTo(ivIconHeader.end)
                    top.linkTo(ivIconHeader.top)
                    bottom.linkTo(ivIconHeader.bottom)
                }, text = itemFirst.text, textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold, color = textColor
            )
        )

        //Text - Buat Stories Otomatis - Toggle
        AndroidView(
            modifier = Modifier.constrainAs(switchHeader) {
                top.linkTo(tvDescription.top)
                bottom.linkTo(tvDescription.bottom)
                end.linkTo(parent.end)
            },
            factory = { context ->
                SwitchUnify(context).apply {
                    this.setOnCheckedChangeListener { view, isActive ->
                        if (!view.isPressed) return@setOnCheckedChangeListener
                        if (!coolingDown) {
                            checked = isActive
                            coolingDown = true
                            viewModel.onEvent(
                                StoriesSettingsAction.SelectOption(itemFirst)
                            )
                        } else {
                            viewModel.onEvent(
                                StoriesSettingsAction.ShowCoolingDown(
                                    MessageErrorException("Tunggu 5 detik dulu ya")
                                )
                            )
                        }
                    }
                }
            },
            update = { switchUnify ->
                switchUnify.isChecked = pageInfo.options.any { it.isSelected }
                switchUnify.isEnabled = isEligible
            },
        )
        //Text - Selengkapnya
        val text = buildAnnotatedString {
            append(pageInfo.config.articleCopy.replace(tagLink, ""))
            withStyle(
                style = SpanStyle(
                    color = NestTheme.colors.GN._500,
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                pushStringAnnotation(tagLink, tagLink)
                append(finalText)
            }
        }
        NestTypography(modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 12.dp)
            .constrainAs(tvAll) {
                top.linkTo(tvDescription.bottom)
                start.linkTo(tvDescription.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            text = text,
            textStyle = NestTheme.typography.display3.copy(color = textColor),
            onClickText = {
                viewModel.onEvent(StoriesSettingsAction.Navigate(pageInfo.config.articleAppLink))
            })
        //Text - Kategori update produk
        NestTypography(
            modifier = Modifier
                .padding(bottom = 16.dp, start = 12.dp)
                .constrainAs(tvCategory) {
                    top.linkTo(tvAll.bottom)
                    start.linkTo(tvAll.start)
                },
            text = stringResource(id = R.string.stories_settings_body),
            textStyle = NestTheme.typography.paragraph3.copy(color = textColor)
        )

        //List of Options
        LazyColumn(modifier = Modifier
            .padding(start = 16.dp)
            .constrainAs(rvOptions) {
                top.linkTo(tvCategory.bottom)
                start.linkTo(tvCategory.start)
                end.linkTo(parent.end)
            }) {
            items(pageInfo.options.drop(1)) { item ->
                SettingOptItem(item,
                    isEligible,
                    textColor,
                    checked,
                    onOptionClicked = {
                        if (!coolingDown) {
                            coolingDown = true
                            viewModel.onEvent(StoriesSettingsAction.SelectOption(it))
                        } else {
                            viewModel.onEvent(
                                StoriesSettingsAction.ShowCoolingDown(
                                    MessageErrorException("Tunggu 5 detik dulu ya")
                                )
                            )
                        }
                    })
            }
        }
    }
}

@Composable
private fun StoriesSettingsError(error: Throwable, viewModel: StoriesSettingsViewModel) {
    val (text, type, action) = when (error) {
        is UnknownHostException -> Triple(
            stringResource(id = R.string.stories_settings_try_again),
            NestGlobalErrorType.NoConnection
        ) {}

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
    textColor: Color,
    checked: Boolean,
    onOptionClicked: (StoriesSettingOpt) -> Unit,
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
            textStyle = NestTheme.typography.paragraph2.copy(color = textColor),
        )
        AndroidView(
            factory = { context ->
                CheckboxUnify(context).apply {
                    this.setOnCheckedChangeListener { view, isActive ->
                        if (!view.isPressed) return@setOnCheckedChangeListener
                        onOptionClicked(item)
                    }
                }
            },
            update = { v ->
                v.isChecked = item.isSelected || checked
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
