@file:OptIn(ExperimentalPagerApi::class)

package com.tokopedia.sellerpersona.view.compose.screen.questionnaire

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUserEvent
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnaireDataUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.compose.NestCheckbox
import com.tokopedia.unifycomponents.compose.NestProgressBar
import com.tokopedia.sellerpersona.R as sellerpersonaR

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

private const val MAX_PROGRESS = 100

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun QuestionnaireSuccessState(
    data: QuestionnaireDataUiModel,
    onEvent: (QuestionnaireUserEvent) -> Unit
) {
    val questionnaireList = data.questionnaireList
    if (questionnaireList.isEmpty()) return
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (progressBar, questionnairePager, prevBtn, nextBtn) = createRefs()
            val pagerState = rememberPagerState()

            LaunchOnPagerSwipeEffect(pagerState, onEvent)
            LaunchedEffect(key1 = data.currentPage, block = {
                moveToNextPage(pagerState, data.currentPage)
            })

            val progressBarValue by rememberProgressValue(data)

            NestProgressBar(
                isSmooth = true,
                value = progressBarValue,
                colorType = ProgressBarUnify.COLOR_GREEN,
                modifier = Modifier
                    .constrainAs(progressBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .height(2.dp)
            )

            HorizontalPager(
                count = questionnaireList.size,
                modifier = Modifier.constrainAs(questionnairePager) {
                    top.linkTo(progressBar.bottom)
                    bottom.linkTo(nextBtn.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                key = { pagePosition ->
                    questionnaireList[pagePosition].id
                },
                state = pagerState
            ) { pagePosition ->
                val questionnaire = questionnaireList[pagePosition]
                QuestionnairePager(pagePosition, questionnaire, onEvent)
            }

            if (pagerState.currentPage > 0) {
                NestButton(
                    text = stringResource(sellerpersonaR.string.sp_previous),
                    modifier = Modifier.constrainAs(prevBtn) {
                        start.linkTo(parent.start, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    },
                    variant = ButtonVariant.TEXT_ONLY,
                    onClick = {
                        onEvent(QuestionnaireUserEvent.ClickPrevious)
                    }
                )
            }

            val isNextButtonEnabled by rememberNextButtonState(
                pagerState.currentPage,
                questionnaireList
            )

            NestButton(
                text = stringResource(sellerpersonaR.string.sp_next),
                modifier = Modifier.constrainAs(nextBtn) {
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    width = Dimension.preferredWrapContent.atLeast(120.dp)
                },
                isEnabled = isNextButtonEnabled,
                isLoading = data.isNextButtonLoading,
                onClick = {
                    onEvent(QuestionnaireUserEvent.ClickNext)
                }
            )
        }
    }
}

@Composable
fun rememberProgressValue(data: QuestionnaireDataUiModel): State<Int> {
    val currentPage = data.currentPage.plus(1)
    val pageCount = data.questionnaireList.size
    return remember(data.currentPage) {
        derivedStateOf {
            return@derivedStateOf if (pageCount == 0) {
                0
            } else {
                currentPage.times(MAX_PROGRESS).div(pageCount)
            }
        }
    }
}

@Composable
fun QuestionnairePager(
    pagePosition: Int,
    questionnaire: QuestionnairePagerUiModel,
    event: (QuestionnaireUserEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp)
    ) {
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = questionnaire.questionTitle,
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = questionnaire.questionSubtitle,
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._600
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            questionnaire.options.forEach { option ->
                item(key = option.value) {
                    when (option) {
                        is BaseOptionUiModel.QuestionOptionSingleUiModel -> {
                            QuestionnaireItemSingleAnswer(pagePosition, option, event)
                        }

                        is BaseOptionUiModel.QuestionOptionMultipleUiModel -> {
                            QuestionnaireItemMultipleAnswer(pagePosition, option, event)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionnaireItemMultipleAnswer(
    pagePosition: Int,
    option: BaseOptionUiModel.QuestionOptionMultipleUiModel,
    event: (QuestionnaireUserEvent) -> Unit
) {
    NestCheckbox(
        text = option.title,
        isChecked = option.isSelected,
        modifier = Modifier.fillMaxWidth(),
        onCheckedChange = {
            event(QuestionnaireUserEvent.OnOptionItemSelected(pagePosition, option, it))
        }
    )
}

@Composable
fun QuestionnaireItemSingleAnswer(
    pagePosition: Int,
    option: BaseOptionUiModel.QuestionOptionSingleUiModel,
    event: (QuestionnaireUserEvent) -> Unit
) {
    val borderColor = if (option.isSelected) {
        NestTheme.colors.GN._400
    } else {
        NestTheme.colors.NN._300
    }
    val backgroundColor = if (option.isSelected) {
        NestTheme.colors.GN._50
    } else {
        NestTheme.colors.NN._0
    }
    val roundedShape = remember {
        RoundedCornerShape(
            bottomStart = 12.dp, bottomEnd = 12.dp, topStart = 12.dp, topEnd = 12.dp
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor, shape = roundedShape)
            .background(color = backgroundColor, shape = roundedShape)
            .clickable(
                indication = null, interactionSource = MutableInteractionSource()
            ) {
                val isChecked = !option.isSelected
                event(QuestionnaireUserEvent.OnOptionItemSelected(pagePosition, option, isChecked))
            }
    ) {
        NestTypography(
            text = option.title, modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
suspend fun moveToNextPage(pagerState: PagerState, nextPage: Int) {
    val canMove = nextPage < pagerState.pageCount
    if (pagerState.currentPage != nextPage && canMove) {
        pagerState.animateScrollToPage(nextPage)
    }
}

@Composable
@NonRestartableComposable
fun LaunchOnPagerSwipeEffect(pagerState: PagerState, onEvent: (QuestionnaireUserEvent) -> Unit) {
    LaunchedEffect(key1 = pagerState, block = {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onEvent(QuestionnaireUserEvent.OnPagerSwipe(page))
        }
    })
}

@Composable
fun rememberNextButtonState(
    currentPage: Int, questionnaireList: List<QuestionnairePagerUiModel>
): State<Boolean> {
    val questionnaire = questionnaireList[currentPage]
    return remember(questionnaire) {
        derivedStateOf {
            questionnaire.options.any { it.isSelected }
        }
    }
}

@Preview
@Composable
fun QuestionnaireSuccessStatePreview() {
    NestTheme(darkTheme = false) {
        QuestionnaireSuccessState(
            data = QuestionnaireDataUiModel(
                questionnaireList = listOf(
                    QuestionnairePagerUiModel(
                        id = "1",
                        questionTitle = "Apa yang biasa kamu lakukan di Tokopedia Seller?",
                        questionSubtitle = "(Bisa pilih lebih dari 1 jawaban)",
                        type = QuestionnairePagerUiModel.QuestionnaireType.SINGLE_ANSWER,
                        options = listOf(
                            BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "a",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ), BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "b",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ), BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "c",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ), BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "d",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            )
                        )
                    ), QuestionnairePagerUiModel(
                        id = "2",
                        questionTitle = "Apa yang biasa kamu lakukan di Tokopedia Seller?",
                        questionSubtitle = "(Bisa pilih lebih dari 1 jawaban)",
                        type = QuestionnairePagerUiModel.QuestionnaireType.MULTIPLE_ANSWER,
                        options = listOf(
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "a",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ), BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "b",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ), BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "c",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ), BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "d",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            )
                        )
                    )
                )
            )
        ) {

        }
    }
}