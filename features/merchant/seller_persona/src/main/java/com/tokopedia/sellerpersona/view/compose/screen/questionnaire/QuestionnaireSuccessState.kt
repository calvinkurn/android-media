package com.tokopedia.sellerpersona.view.compose.screen.questionnaire

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUiEvent
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.unifycomponents.compose.NestCheckbox
import com.tokopedia.sellerpersona.R as sellerpersonaR

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

@OptIn(ExperimentalPagerApi::class)
@Composable
fun QuestionnaireSuccessState(
    data: List<QuestionnairePagerUiModel>, onEvent: (QuestionnaireUiEvent) -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (questionnairePager, prevBtn, nextBtn) = createRefs()

        HorizontalPager(count = data.size, modifier = Modifier.constrainAs(questionnairePager) {
                top.linkTo(parent.top)
                bottom.linkTo(nextBtn.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }) { pagePosition ->
            val questionnaire = data[pagePosition]
            QuestionnairePager(pagePosition, questionnaire, onEvent)
        }

        NestButton(
            text = stringResource(sellerpersonaR.string.sp_previous),
            modifier = Modifier.constrainAs(prevBtn) {
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            variant = ButtonVariant.TEXT_ONLY,
            isEnabled = false,
            onClick = {
                onEvent(QuestionnaireUiEvent.ClickPrevious)
            })

        NestButton(
            text = stringResource(sellerpersonaR.string.sp_next),
            modifier = Modifier.constrainAs(nextBtn) {
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            onClick = {
                onEvent(QuestionnaireUiEvent.ClickNext)
            })
    }
}

@Composable
fun QuestionnairePager(
    pagePosition: Int,
    questionnaire: QuestionnairePagerUiModel,
    event: (QuestionnaireUiEvent) -> Unit
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
            items(items = questionnaire.options) { option ->
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

@Composable
fun QuestionnaireItemMultipleAnswer(
    pagePosition: Int,
    option: BaseOptionUiModel.QuestionOptionMultipleUiModel,
    event: (QuestionnaireUiEvent) -> Unit
) {
    val isChecked = remember { mutableStateOf(option.isSelected) }
    val onClick: (Boolean) -> Unit = {
        event(QuestionnaireUiEvent.OnOptionItemSelected(pagePosition, option, it))
        isChecked.value = it
    }
    NestCheckbox(
        text = option.title,
        isChecked = isChecked.value,
        onCheckedChange = {
            onClick(it)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun QuestionnaireItemSingleAnswer(
    pagePosition: Int,
    option: BaseOptionUiModel.QuestionOptionSingleUiModel,
    event: (QuestionnaireUiEvent) -> Unit
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

    Column(modifier = Modifier
        .fillMaxWidth()
        .border(width = 1.dp, color = borderColor, shape = roundedShape)
        .background(color = backgroundColor, shape = roundedShape)
        .clickable(
            indication = null, interactionSource = MutableInteractionSource()
        ) {
            val isChecked = !option.isSelected
            event(QuestionnaireUiEvent.OnOptionItemSelected(pagePosition, option, isChecked))
        }) {
        NestTypography(
            text = option.title, modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        )
    }
}

@Preview
@Composable
fun QuestionnaireSuccessStatePreview() {
    NestTheme(darkTheme = false) {
        QuestionnaireSuccessState(
            data = listOf(
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
        ) {

        }
    }
}