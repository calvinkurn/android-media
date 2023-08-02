package com.tokopedia.sellerpersona.view.compose.screen.questionnaire

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUiEvent
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionnaireSuccessState(
    data: QuestionnaireState.Data,
    event: (QuestionnaireUiEvent) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (questionnairePager, prevBtn, nextBtn) = createRefs()

        val listState = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .constrainAs(questionnairePager) {
                    top.linkTo(parent.top)
                    bottom.linkTo(nextBtn.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            flingBehavior = rememberSnapFlingBehavior(listState),
            userScrollEnabled = false
        ) {
            itemsIndexed(items = data.questionnaireList) { i, questionnaire ->
                QuestionnairePager(i, questionnaire, event)
            }
        }

        NestButton(
            text = stringResource(com.tokopedia.sellerpersona.R.string.sp_previous),
            modifier = Modifier.constrainAs(prevBtn) {
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            variant = ButtonVariant.TEXT_ONLY,
            isEnabled = false,
            onClick = {
                event(QuestionnaireUiEvent.ClickPrevious)
            }
        )

        NestButton(
            text = stringResource(com.tokopedia.sellerpersona.R.string.sp_next),
            modifier = Modifier.constrainAs(nextBtn) {
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            onClick = {
                event(QuestionnaireUiEvent.ClickNext)
            }
        )
    }
}

@Composable
fun QuestionnairePager(
    pagePosition: Int,
    questionnaire: QuestionnairePagerUiModel,
    event: (QuestionnaireUiEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = questionnaire.questionTitle,
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = questionnaire.questionSubtitle,
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._600
            )
        )

        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(items = questionnaire.options.orEmpty()) { option ->
                when (option) {
                    is BaseOptionUiModel.QuestionOptionSingleUiModel -> {
                        QuestionnaireItemSingleAnswer(option, event)
                    }

                    is BaseOptionUiModel.QuestionOptionMultipleUiModel -> {
                        QuestionnaireItemMultipleAnswer(option, event)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionnaireItemMultipleAnswer(
    option: BaseOptionUiModel.QuestionOptionMultipleUiModel,
    event: (QuestionnaireUiEvent) -> Unit
) {

}

@Composable
fun QuestionnaireItemSingleAnswer(
    option: BaseOptionUiModel.QuestionOptionSingleUiModel,
    event: (QuestionnaireUiEvent) -> Unit
) {
    val isChecked = remember {
        mutableStateOf(option.isSelected)
    }
    val onClick: (Boolean) -> Unit = {
        event(QuestionnaireUiEvent.OnMultipleOptionChecked(option, it))
        isChecked.value = it
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = {
                    onClick(!isChecked.value)
                }
            )
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = {
                onClick(it)
            },
            modifier = Modifier.wrapContentWidth()
        )
        NestTypography(
            modifier = Modifier.weight(1f),
            text = option.title,
            textStyle = NestTheme.typography.display2.copy(
                color = NestTheme.colors.NN._600
            )
        )
    }
}

@Preview
@Composable
fun QuestionnaireSuccessStatePreview() {
    NestTheme(darkTheme = false) {
        QuestionnaireSuccessState(
            data = QuestionnaireState.Data(
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
                            ),
                            BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "b",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ),
                            BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "c",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ),
                            BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "d",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            )
                        )
                    ),
                    QuestionnairePagerUiModel(
                        id = "2",
                        questionTitle = "Apa yang biasa kamu lakukan di Tokopedia Seller?",
                        questionSubtitle = "(Bisa pilih lebih dari 1 jawaban)",
                        type = QuestionnairePagerUiModel.QuestionnaireType.MULTIPLE_ANSWER,
                        options = listOf(
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "a",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ),
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "b",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ),
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "c",
                                title = "Mengurus operasional toko (misal: balas chat dan diskusi, proses pesanan, request pick-up, update stok produk, dan lain-lain)"
                            ),
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
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