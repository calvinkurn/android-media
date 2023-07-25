package com.tokopedia.sellerpersona.view.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.compose.common.Switch
import com.tokopedia.sellerpersona.view.compose.model.state.PersonaResultState
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.iconunify.R as iconUnifyR

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

private const val PERSONA_TITLE = "\uD83C\uDF1F %s \uD83C\uDF1F"
private const val CORPORATE_EMPLOYEE = "corporate-employee"

@Composable
internal fun PersonaResultScreen(
    state: PersonaResultState, onEvent: (ResultUiEvent) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            ResultHeaderSectionUi(state.data.personaData)
        }
        renderResultContentSectionUi(state.data)
        item {
            ResultFooterSectionUi(state.data, onEvent)
        }
    }
}

@Composable
private fun ResultFooterSectionUi(data: PersonaDataUiModel, onEvent: (ResultUiEvent) -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Divider(
            color = NestTheme.colors.NN._50, thickness = 1.dp, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                NestTypography(
                    text = stringResource(
                        R.string.sp_result_persona_seller_type_status, data.personaData.headerTitle
                    ), textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._900
                    )
                )
                NestTypography(
                    text = stringResource(data.getActiveStatusStringRes()),
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._600
                    )
                )
            }
            Switch(isSwitchedOn = data.isSwitchChecked) {
                onEvent(ResultUiEvent.CheckChanged(it))
            }
        }
        NestTypography(
            text = stringResource(R.string.sp_persona_activation_content_description),
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._600
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (data.isApplyButtonVisible()) {
            NestButton(
                text = stringResource(data.getApplyButtonStringRes()), onClick = {
                    onEvent(ResultUiEvent.ApplyChanges(data.persona, data.isSwitchChecked))
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        NestButton(
            text = stringResource(R.string.sp_retry_questionnaire), onClick = {
                onEvent(ResultUiEvent.RetakeQuiz)
            }, modifier = Modifier.fillMaxWidth(), variant = ButtonVariant.GHOST_ALTERNATE
        )

        ManualSelectPersonaComponent(data.persona, onEvent)
    }
}

@Composable
private fun ManualSelectPersonaComponent(persona: String, onEvent: (ResultUiEvent) -> Unit) {
    val clickableTextColor = NestTheme.colors.GN._500
    val notClickable = stringResource(R.string.sp_persona_result_manual_select_1)
    val clickable = stringResource(R.string.sp_persona_result_manual_select_2)
    val annotatedString = buildAnnotatedString {
        append(notClickable)
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold, color = clickableTextColor
            )
        ) {
            pushStringAnnotation(tag = clickable, annotation = clickable)
            append(" $clickable")
        }
    }
    NestTypography(text = annotatedString,
        textStyle = NestTheme.typography.display3.copy(
            color = NestTheme.colors.NN._600, textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .padding(top = 26.dp, bottom = 18.dp)
            .fillMaxWidth(),
        onClickText = { offset ->
            annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.let { span ->
                when (span.item) {
                    clickable -> onEvent(ResultUiEvent.SelectPersona(persona))
                }
            }
        })
}

private fun LazyListScope.renderResultContentSectionUi(data: PersonaDataUiModel) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            NestTypography(
                modifier = Modifier.padding(top = 16.dp), text = stringResource(
                    R.string.sp_result_list_section_gedongan, data.personaData.headerTitle
                ), textStyle = NestTheme.typography.heading2.copy(
                    color = NestTheme.colors.NN._900
                )
            )
            Divider(
                color = NestTheme.colors.NN._50,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }
    items(items = data.personaData.itemList) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 16.dp)
        ) {
            Icon(
                painter = painterResource(iconUnifyR.drawable.iconunify_check),
                contentDescription = null,
                tint = NestTheme.colors.GN._500,
                modifier = Modifier.size(24.dp)
            )
            NestTypography(
                modifier = Modifier.padding(
                    top = 6.dp, bottom = 6.dp, start = 8.dp
                ), textStyle = NestTheme.typography.display2.copy(
                    color = NestTheme.colors.NN._950
                ), text = it
            )
        }
    }
    if (data.persona == CORPORATE_EMPLOYEE && data.isShopOwner) {
        item {
            Divider(
                color = NestTheme.colors.NN._50,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(iconUnifyR.drawable.iconunify_lightbulb),
                    contentDescription = null,
                    tint = NestTheme.colors.GN._500,
                    modifier = Modifier.size(20.dp)
                )
                NestTypography(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(R.string.sp_result_create_admin_account),
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._600
                    )
                )
            }
        }
    }
}

@Composable
private fun ResultHeaderSectionUi(persona: PersonaUiModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(246.dp),
        contentAlignment = Alignment.Center
    ) {
        NestImage(
            source = ImageSource.Remote(source = persona.backgroundImage),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NestImage(
                source = ImageSource.Remote(
                    source = persona.avatarImage, loaderType = ImageSource.Remote.LoaderType.NONE
                ),
                contentScale = ContentScale.Crop,
                type = NestImageType.Circle,
                modifier = Modifier.requiredSize(120.dp)
            )
            Spacer(modifier = Modifier.requiredHeight(16.dp))
            NestTypography(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.sp_result_seller_type),
                textStyle = NestTheme.typography.display3.copy(
                    color = NestNN.light._0, textAlign = TextAlign.Center
                )
            )
            NestTypography(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                text = String.format(PERSONA_TITLE, persona.headerTitle),
                textStyle = NestTheme.typography.heading2.copy(
                    color = NestNN.light._0, textAlign = TextAlign.Center
                )
            )
            NestTypography(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp), text = stringResource(
                    R.string.sp_result_account_type, persona.headerSubTitle
                ), textStyle = NestTheme.typography.small.copy(
                    color = NestNN.light._0,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun ResultLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        NestLoader(
            variant = NestLoaderType.Circular(
                size = NestLoaderSize.Small
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPersonaResultScreen() {
    NestTheme(darkTheme = false) {
        PersonaResultScreen(state = PersonaResultState(
            isLoading = false,
            error = Throwable(),
            data = PersonaDataUiModel(
                persona = "corporate-supervisor-owner",
                personaStatus = PersonaStatus.ACTIVE,
                personaData = PersonaUiModel(
                    value = "corporate-supervisor-owner",
                    headerTitle = "Gedongan",
                    headerSubTitle = "Pemilik Toko",
                    avatarImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_avatar_gedongan-min.png",
                    backgroundImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_background_gedongan-min.png",
                    bodyTitle = "Pilih tipe ini jika kamu:",
                    itemList = listOf(
                        "Menerima 1-10 pesanan per hari",
                        "Punya toko fisik (offline)",
                        "Punya pegawai yang mengurus operasional toko",
                        "Sering mencari peluang untuk strategi baru"
                    )
                ),
            )
        ), onEvent = {})
    }
}