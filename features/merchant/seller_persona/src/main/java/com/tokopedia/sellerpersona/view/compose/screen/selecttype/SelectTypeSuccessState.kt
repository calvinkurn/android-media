package com.tokopedia.sellerpersona.view.compose.screen.selecttype

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestGN
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.compose.model.state.SelectTypeState
import com.tokopedia.sellerpersona.view.compose.model.uievent.SelectTypeUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 26/07/23.
 */

private const val SUB_TITLE_FORMAT = "(%s)"
private val CARD_WIDTH = 300.dp
private val CARD_SPACE_DP = 16.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PersonSuccessState(
    data: SelectTypeState.Data, onEvent: (SelectTypeUiEvent) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchScrollToPosition(listState, data.ui.selectedIndex)

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (personaList, divider, selectButton) = createRefs()
        LazyRow(
            modifier = Modifier.constrainAs(personaList) {
                top.linkTo(anchor = parent.top)
                start.linkTo(anchor = parent.start)
                end.linkTo(anchor = parent.end)
                bottom.linkTo(anchor = divider.top)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            contentPadding = PaddingValues(all = CARD_SPACE_DP),
            horizontalArrangement = Arrangement.spacedBy(CARD_SPACE_DP)
        ) {
            items(items = data.personaList) { persona ->
                PersonaTypeItemCard(persona) {
                    onEvent(SelectTypeUiEvent.ClickPersonaCard(persona))
                }
            }
        }
        Divider(modifier = Modifier.constrainAs(divider) {
            start.linkTo(anchor = parent.start, margin = 16.dp)
            end.linkTo(anchor = parent.end, margin = 16.dp)
            bottom.linkTo(anchor = selectButton.top, margin = 16.dp)
            width = Dimension.fillToConstraints
        })
        NestButton(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(selectButton) {
                start.linkTo(anchor = parent.start, margin = 16.dp)
                end.linkTo(anchor = parent.end, margin = 16.dp)
                bottom.linkTo(anchor = parent.bottom, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            isLoading = data.ui.isSelectButtonLoading,
            text = stringResource(R.string.sp_select),
            variant = ButtonVariant.FILLED,
            onClick = {
                onEvent(SelectTypeUiEvent.ClickSelectButton)
            }
        )
    }
}

@Composable
@NonRestartableComposable
fun LaunchScrollToPosition(listState: LazyListState, selectedIndex: Int) {
    val configuration = LocalConfiguration.current
    val screenWidthInDp = configuration.screenWidthDp.dp

    val scrollOffset = remember(screenWidthInDp, selectedIndex) {
        return@remember derivedStateOf {
            val isFirstIndex = selectedIndex == 0
            if (isFirstIndex) return@derivedStateOf 0

            val screenWidth = screenWidthInDp.value
            val cardWidth = CARD_WIDTH.value
            val halfScreen = screenWidth.div(2)
            val halfCard = cardWidth.div(2)

            return@derivedStateOf halfScreen.minus(halfCard).times(-2).toInt()
        }
    }

    LaunchedEffect(key1 = selectedIndex, block = {
        //scroll to position on every selectedIndex changed
        val isValidIndex = selectedIndex >= 0
        if (isValidIndex) {
            listState.animateScrollToItem(
                index = selectedIndex,
                scrollOffset = scrollOffset.value
            )
        }
    })
}

@Composable
private fun PersonaTypeItemCard(persona: PersonaUiModel, onClicked: () -> Unit) {
    Row(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = MutableInteractionSource(),
            onClick = onClicked
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .width(CARD_WIDTH)
                .fillMaxHeight()
                .background(
                    brush = getBackgroundState(persona.isSelected),
                    shape = RoundedCornerShape(size = 16.dp)
                )
                .border(
                    width = 0.9.dp, color = if (persona.isSelected) {
                        NestGN.light._500
                    } else {
                        NestTheme.colors.NN._300
                    }, shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            val (radio, avatar, tvSellerTypeLbl, tvPersonaType, tvSellerTypeStatus, dividerItemSelectType, tvSellerTypeLblInfo, columnSelectTypeInfo) = createRefs()

            val startGuideline = createGuidelineFromStart(16.dp)
            val endGuideline = createGuidelineFromEnd(16.dp)

            RadioButtonComponent(
                modifier = Modifier.constrainAs(radio) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
                isChecked = persona.isSelected,
                onClicked = onClicked
            )

            NestImage(
                source = ImageSource.Remote(
                    source = persona.avatarImage, loaderType = ImageSource.Remote.LoaderType.SHIMMER
                ),
                contentScale = ContentScale.Crop,
                type = NestImageType.Circle,
                modifier = Modifier
                    .constrainAs(avatar) {
                        top.linkTo(anchor = parent.top, margin = 16.dp)
                        start.linkTo(anchor = startGuideline)
                    }
                    .size(size = 80.dp))

            NestTypography(
                modifier = Modifier.constrainAs(tvSellerTypeLbl) {
                    top.linkTo(avatar.bottom, margin = 12.dp)
                    start.linkTo(anchor = startGuideline)
                },
                text = stringResource(R.string.sp_label_seller_type),
                textStyle = NestTheme.typography.display2.copy(
                    color = rememberLabelTextColor(persona.isSelected)
                )
            )
            NestTypography(
                modifier = Modifier.constrainAs(tvPersonaType) {
                    top.linkTo(tvSellerTypeLbl.bottom, margin = 2.dp)
                    start.linkTo(anchor = startGuideline)
                }, text = persona.headerTitle, textStyle = NestTheme.typography.heading1.copy(
                    color = rememberPersonaTypeTextColor(persona.isSelected)
                )
            )
            NestTypography(
                modifier = Modifier.constrainAs(tvSellerTypeStatus) {
                    top.linkTo(tvPersonaType.bottom, margin = 2.dp)
                    start.linkTo(anchor = startGuideline)
                },
                text = String.format(SUB_TITLE_FORMAT, persona.headerSubTitle),
                textStyle = NestTheme.typography.heading5.copy(
                    color = rememberStatusTypeTextColor(persona.isSelected)
                )
            )

            Divider(
                modifier = Modifier
                    .background(
                        color = NestTheme.colors.NN._300
                    )
                    .constrainAs(dividerItemSelectType) {
                        top.linkTo(anchor = tvSellerTypeStatus.bottom, margin = 16.dp)
                        start.linkTo(anchor = startGuideline)
                        end.linkTo(anchor = endGuideline)
                        width = Dimension.fillToConstraints
                    }
            )

            val sectionTextColor = getSectionTextColor(persona.isSelected)
            NestTypography(
                modifier = Modifier.constrainAs(tvSellerTypeLblInfo) {
                    top.linkTo(dividerItemSelectType.bottom, margin = 16.dp)
                    start.linkTo(anchor = startGuideline)
                },
                text = stringResource(R.string.sp_select_this_type_if),
                textStyle = NestTheme.typography.display2.copy(
                    color = sectionTextColor
                )
            )

            LazyColumn(modifier = Modifier.constrainAs(columnSelectTypeInfo) {
                top.linkTo(anchor = tvSellerTypeLblInfo.bottom, margin = 16.dp)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
            }) {
                items(items = persona.itemList) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(com.tokopedia.iconunify.R.drawable.iconunify_check),
                            contentDescription = null,
                            tint = NestTheme.colors.GN._500,
                            modifier = Modifier.size(24.dp)
                        )
                        NestTypography(
                            modifier = Modifier.padding(
                                top = 6.dp, bottom = 6.dp, start = 8.dp
                            ), textStyle = NestTheme.typography.display2.copy(
                                color = sectionTextColor
                            ), text = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getBackgroundState(isSelected: Boolean): Brush {
    val colorStops = if (isSelected) {
        arrayOf(
            0.0f to NestGN.light._100,
            0.20f to NestGN.light._50,
            0.70f to NestGN.light._50,
            0.90f to NestGN.light._100,
            1f to NestGN.light._200
        )
    } else {
        arrayOf(0f to NestTheme.colors.NN._50, 1f to NestTheme.colors.NN._50)
    }
    return Brush.linearGradient(colorStops = colorStops)
}

@Composable
private fun rememberLabelTextColor(isSelected: Boolean): Color {
    val selectedColor = NestNN.light._900
    val unselectedColor = NestTheme.colors.NN._600
    return remember(isSelected) {
        when {
            isSelected -> selectedColor
            else -> unselectedColor
        }
    }
}

@Composable
private fun rememberPersonaTypeTextColor(isSelected: Boolean): Color {
    val isDarkTheme = isSystemInDarkTheme()
    val selectedColor = NestNN.light._950
    val unselectedColor = NestTheme.colors.NN._950
    return remember(isSelected, isDarkTheme) {
        when {
            isDarkTheme && isSelected -> selectedColor
            else -> unselectedColor
        }
    }
}

@Composable
private fun rememberStatusTypeTextColor(isSelected: Boolean): Color {
    val selectedColor = NestTheme.colors.GN._500
    val unselectedColor = NestTheme.colors.NN._600
    return remember(isSelected) {
        when {
            isSelected -> selectedColor
            else -> unselectedColor
        }
    }
}

@Composable
private fun getSectionTextColor(isSelected: Boolean): Color {
    val selectedDarkThemeColor = NestNN.light._950
    val selectedColor = NestNN.light._900
    val unselectedColor = NestTheme.colors.NN._600
    val isDarkTheme = isSystemInDarkTheme()
    return remember(isSelected, isDarkTheme) {
        when {
            isDarkTheme && isSelected -> selectedDarkThemeColor
            isSelected -> selectedColor
            else -> unselectedColor
        }
    }
}

@Preview
@Composable
fun SuccessStatePreview() {
    NestTheme(darkTheme = false) {
        PersonSuccessState(
            SelectTypeState.Data(
                ui = SelectTypeState.Ui(isSelectButtonLoading = false),
                personaList = listOf(
                    PersonaUiModel(
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
                        ),
                        isSelected = true
                    ), PersonaUiModel(
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
                    ), PersonaUiModel(
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
                    )
                )
            )
        ) {

        }
    }
}
