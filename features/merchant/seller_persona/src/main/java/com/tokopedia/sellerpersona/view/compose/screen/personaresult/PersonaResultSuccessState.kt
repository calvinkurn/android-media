package com.tokopedia.sellerpersona.view.compose.screen.personaresult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.unifycomponents.compose.NestSwitch

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

private const val PERSONA_TITLE = "\uD83C\uDF1F %s \uD83C\uDF1F"
private const val CORPORATE_EMPLOYEE = "corporate-employee"

@Composable
internal fun ResultSuccessState(
    data: PersonaDataUiModel, hasImpressed: Boolean, onEvent: (ResultUiEvent) -> Unit
) {

    LaunchedEffect(key1 = hasImpressed, block = {
        if (!hasImpressed) {
            onEvent(ResultUiEvent.OnResultImpressedEvent)
        }
    })

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .tag("rvSpResultInfoList")
    ) {
        item {
            ResultHeaderSectionUi(
                backgroundImage = data.personaData.backgroundImage,
                avatarImage = data.personaData.avatarImage,
                headerTitle = data.personaData.headerTitle,
                headerSubTitle = data.personaData.headerSubTitle
            )
        }
        renderResultContentSectionUi(
            persona = data.persona,
            headerTitle = data.personaData.headerTitle,
            itemList = data.personaData.itemList,
            isShopOwner = data.isShopOwner
        )
        item {
            ResultFooterSectionUi(data, onEvent)
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
            modifier = Modifier.fillMaxWidth(),
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
                    ),
                    modifier = Modifier
                        .wrapContentWidth()
                        .tag("tvSpLblActivatePersonaStatus")
                )
            }
            NestSwitch(defaultIsChecked = data.isSwitchChecked) {
                onEvent(ResultUiEvent.OnSwitchCheckChanged(it))
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
                }, modifier = Modifier
                    .fillMaxWidth()
                    .tag("btnSpApplyPersona")
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        NestButton(
            text = stringResource(R.string.sp_retry_questionnaire),
            onClick = {
                onEvent(ResultUiEvent.RetakeQuiz)
            },
            modifier = Modifier
                .fillMaxWidth()
                .tag("btnSpRetryQuestionnaire"),
            variant = ButtonVariant.GHOST_ALTERNATE
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
            .fillMaxWidth()
            .tag("tvSpSelectManualType"),
        onClickText = { spannedRange ->
            when (spannedRange.item) {
                clickable -> onEvent(ResultUiEvent.SelectPersona(persona))
            }
        })
}

@Composable
private fun ResultHeaderSectionUi(
    backgroundImage: String,
    avatarImage: String,
    headerTitle: String,
    headerSubTitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(246.dp), contentAlignment = Alignment.Center
    ) {
        NestImage(
            source = ImageSource.Remote(source = backgroundImage),
            contentScale = ContentScale.Crop,
            type = NestImageType.Rect(rounded = 0.dp),
            modifier = Modifier
                .fillMaxSize()
                .tag("imgSpResultBackdrop")
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NestImage(
                source = ImageSource.Remote(
                    source = avatarImage, loaderType = ImageSource.Remote.LoaderType.NONE
                ),
                contentScale = ContentScale.Crop,
                type = NestImageType.Circle,
                modifier = Modifier
                    .size(120.dp)
                    .tag("imgSpResultAvatar")
            )
            Spacer(modifier = Modifier.height(16.dp))
            NestTypography(
                modifier = Modifier
                    .fillMaxWidth()
                    .tag("tvSpLblSellerType"),
                text = stringResource(R.string.sp_result_seller_type),
                textStyle = NestTheme.typography.display3.copy(
                    color = NestNN.light._0, textAlign = TextAlign.Center
                )
            )
            NestTypography(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
                    .tag("tvSpSellerType"),
                text = String.format(PERSONA_TITLE, headerTitle),
                textStyle = NestTheme.typography.heading2.copy(
                    color = NestNN.light._0, textAlign = TextAlign.Center
                )
            )
            NestTypography(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
                    .tag("tvSpSellerTypeNote"),
                text = stringResource(
                    R.string.sp_result_account_type, headerSubTitle
                ),
                textStyle = NestTheme.typography.small.copy(
                    color = NestNN.light._0,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

private fun LazyListScope.renderResultContentSectionUi(
    persona: String,
    headerTitle: String,
    itemList: List<String>,
    isShopOwner: Boolean
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            NestTypography(
                modifier = Modifier.padding(top = 16.dp), text = stringResource(
                    R.string.sp_result_list_section_gedongan, headerTitle
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
    items(items = itemList) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 16.dp)
        ) {
            NestIcon(
                iconId = IconUnify.CHECK,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorLightEnable = NestTheme.colors.GN._500
            )
            NestTypography(
                modifier = Modifier
                    .padding(
                        top = 6.dp, bottom = 6.dp, start = 8.dp
                    )
                    .tag("tvSpResultInfoItem"), textStyle = NestTheme.typography.display2.copy(
                    color = NestTheme.colors.NN._950
                ), text = it
            )
        }
    }
    corporateOwnerInfo(persona, isShopOwner)
}

private fun LazyListScope.corporateOwnerInfo(persona: String, isShopOwner: Boolean) {
    if (persona == CORPORATE_EMPLOYEE && isShopOwner) {
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
                NestIcon(
                    iconId = IconUnify.LIGHT_BULB,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    colorLightEnable = NestTheme.colors.GN._500
                )
                NestTypography(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .tag("tvSpLblOwnerInfo"),
                    text = stringResource(R.string.sp_result_create_admin_account),
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._600
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPersonaResultScreen() {
    NestTheme(darkTheme = false) {
        ResultSuccessState(data = PersonaDataUiModel(
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
        ), false, onEvent = {})
    }
}