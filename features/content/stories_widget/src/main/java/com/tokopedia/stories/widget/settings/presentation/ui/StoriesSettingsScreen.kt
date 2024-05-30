package com.tokopedia.stories.widget.settings.presentation.ui

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import java.net.UnknownHostException
import com.tokopedia.globalerror.R as globalerrorR

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

@Composable
private fun StoriesSettingsSuccess(
    pageInfo: StoriesSettingsPageUiModel, viewModel: StoriesSettingsViewModel
) {
    val isEligible = pageInfo.config.isEligible
    val itemFirst = pageInfo.options.firstOrNull() ?: StoriesSettingOpt("", "", false)
    val textColor = if (isEligible) NestTheme.colors.NN._950 else NestTheme.colors.NN._400
    val ctx = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .background(color = NestTheme.colors.NN._0)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (tickerEligible, tvHeader, ivIconHeader, switchHeader, tvDescription, tvAll, tvCategory, rvOptions) = createRefs()
        if (isEligible.not()) {
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
                    closeButtonVisibility = View.GONE
                    setTextDescription(ctx.getString(R.string.stories_ticker_title))
                }
            })
        }

        // Header - Story
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

        if (pageInfo.options.isNotEmpty()) {
            // Icon - Mobile
            NestIcon(iconId = IconUnify.SOCIAL_STORY,
                colorNightEnable = textColor,
                colorLightEnable = textColor,
                modifier = Modifier.constrainAs(ivIconHeader) {
                    top.linkTo(tvHeader.bottom)
                    start.linkTo(tvHeader.start)
                })

            // Text - Buat Stories Otomatis
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

            // Text - Buat Stories Otomatis - Toggle
            AndroidView(
                modifier = Modifier.constrainAs(switchHeader) {
                    top.linkTo(tvDescription.top)
                    bottom.linkTo(tvDescription.bottom)
                    end.linkTo(parent.end)
                },
                factory = { context ->
                    SwitchUnify(context).apply {
                        this.isChecked = itemFirst.isSelected
                        this.isEnabled = isEligible
                        this.setOnCheckedChangeListener { view, isActive ->
                            if (view.isPressed) {
                                viewModel.onAction(
                                    StoriesSettingsAction.SelectOption(
                                        itemFirst.copy(
                                            isSelected = !isActive
                                        )
                                    )
                                )
                            }
                        }
                    }
                },
                update = { switchUnify ->
                    switchUnify.isChecked = itemFirst.isSelected
                    switchUnify.isEnabled = isEligible
                }
            )

            // Text - Selengkapnya
            val text = buildAnnotatedString {
                append(pageInfo.config.articleCopy.replace(stringResource(id = R.string.stories_settings_expand_tagLink), ""))
                withStyle(
                    style = SpanStyle(
                        color = NestTheme.colors.GN._500, fontWeight = FontWeight.Bold
                    )
                ) {
                    pushStringAnnotation(stringResource(id = R.string.stories_settings_expand_tagLink), stringResource(id = R.string.stories_settings_expand_tagLink))
                    append(stringResource(id = R.string.stories_settings_expand_finalText))
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
                    viewModel.onAction(StoriesSettingsAction.Navigate(pageInfo.config.articleAppLink))
                })
            // Text - Kategori update produk
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

            // List of Options
            LazyColumn(modifier = Modifier
                .padding(start = 16.dp)
                .constrainAs(rvOptions) {
                    top.linkTo(tvCategory.bottom)
                    start.linkTo(tvCategory.start)
                    end.linkTo(parent.end)
                }) {
                items(pageInfo.options.drop(1)) { item ->
                    SettingOptItem(item, isEligible, textColor, onOptionClicked = {
                        viewModel.onAction(StoriesSettingsAction.SelectOption(it))
                    })
                }
            }
        }
    }
}

@Composable
private fun StoriesSettingsError(error: Throwable, viewModel: StoriesSettingsViewModel) {
    val ctx = LocalContext.current
    val (text, type) = when (error) {
        is UnknownHostException -> Pair(
            stringResource(id = globalerrorR.string.noConnectionSecondaryAction),
            NestGlobalErrorType.NoConnection
        )
        else -> Pair("", NestGlobalErrorType.PageNotFound)
    }
    NestGlobalError(
        type = type,
        actionText = stringResource(id = globalerrorR.string.noConnectionAction),
        secondaryActionText = text,
        onClickSecondaryAction = {
           viewModel.onAction(
               StoriesSettingsAction.Navigate(ctx.getString(R.string.stories_redirect_settings))
           )
        },
        onClickAction = {
            viewModel.onAction(
                StoriesSettingsAction.FetchPageInfo
            )
        })
}

@Composable
private fun SettingOptItem(
    item: StoriesSettingOpt,
    isEligible: Boolean,
    textColor: Color,
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
            text = item.text, textStyle = NestTheme.typography.paragraph2.copy(color = textColor)
        )
        AndroidView(factory = { context ->
            CheckboxUnify(context).apply {
                this.isChecked = item.isSelected
                this.isEnabled = isEligible
                this.setOnCheckedChangeListener { view, isActive ->
                    if (!view.isPressed) return@setOnCheckedChangeListener
                    onOptionClicked(item.copy(isSelected = !isActive))
                }
            }
        }, update = { v ->
            v.isChecked = item.isSelected
            v.isEnabled = isEligible
        })
    }
}
