package com.tokopedia.logisticseller.ui.confirmshipping.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingMode
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingState
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestTextField
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.targetedticker.ui.compose.TargetedTickerWidgetCompose
import com.tokopedia.unifycomponents.compose.NestSwitch

@Composable
fun ConfirmShippingContent(
    paddingValues: PaddingValues,
    state: ConfirmShippingState,
    onClickBarcodeIcon: () -> Unit,
    onSwitchChanged: (Boolean) -> Unit,
    onChangeRefNum: (String) -> Unit,
    openWebview: (url: String) -> Unit,
    onSubmit: () -> Unit,
    onOpenBottomSheet: (bsState: ConfirmShippingBottomSheetState) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TargetedTickerWidgetCompose(
                tickerData = state.tickerData,
                openWebview = openWebview
            )
            InputReference(
                modifier = Modifier,
                state = state,
                onClickBarcodeIcon = onClickBarcodeIcon,
                onChangeRefNum = onChangeRefNum,
            )
            ChangeCourierSection(
                modifier = Modifier,
                state = state,
                onSwitchChanged = onSwitchChanged,
                onOpenBottomSheet = onOpenBottomSheet
            )
        }
        NestButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.btn_konfirmasi),
            onClick = onSubmit
        )
    }
}

@Composable
private fun InputReference(
    modifier: Modifier,
    state: ConfirmShippingState,
    onClickBarcodeIcon: () -> Unit,
    onChangeRefNum: (String) -> Unit
) {
    NestTextField(modifier = Modifier
        .fillMaxWidth()
        .then(modifier),
        value = state.referenceNumber,
        onValueChanged = onChangeRefNum,
        helper = stringResource(id = R.string.tf_no_resi_message),
        label = stringResource(id = R.string.nomor_resi),
        placeholder = stringResource(id = R.string.tf_no_resi_placeholder),
        icon1 = {
            NestImage(
                modifier = Modifier.clickable { onClickBarcodeIcon() },
                source = ImageSource.Painter(source = R.drawable.ic_scanbarcode)
            )
        }
    )
}

@Composable
private fun ChangeCourierSection(
    modifier: Modifier,
    state: ConfirmShippingState,
    onSwitchChanged: (Boolean) -> Unit,
    onOpenBottomSheet: (bsState: ConfirmShippingBottomSheetState) -> Unit
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (switch, title, courier, service) = createRefs()
        NestTypography(
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                top.linkTo(switch.top)
                bottom.linkTo(switch.bottom)
            }, textStyle = NestTheme.typography.body2.copy(color = NestTheme.colors.NN._950),
            text = stringResource(id = R.string.change_courier_label)
        )
        NestSwitch(
            isChecked = state.mode == ConfirmShippingMode.CHANGE_COURIER,
            onCheckedChanged = onSwitchChanged,
            modifier = Modifier.constrainAs(switch) {
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 30.dp)
            })
        if (state.mode == ConfirmShippingMode.CHANGE_COURIER && !state.courierList.isNullOrEmpty()) {
            ChangeCourierOptionItem(
                modifier = Modifier.constrainAs(courier) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
                title = stringResource(id = R.string.title_courier),
                value = state.chosenCourier?.shipmentName.orEmpty(),
                onClick = { onOpenBottomSheet(ConfirmShippingBottomSheetState.COURIER) }
            )
            ChangeCourierOptionItem(
                modifier = Modifier.constrainAs(service) {
                    top.linkTo(courier.bottom)
                    start.linkTo(parent.start)
                },
                title = stringResource(id = R.string.courier_service_label),
                value = state.chosenService?.name.orEmpty(),
                onClick = { onOpenBottomSheet(ConfirmShippingBottomSheetState.SERVICE) }
            )
        }
    }
}

@Composable
private fun ChangeCourierOptionItem(
    modifier: Modifier,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    ConstraintLayout(modifier.fillMaxWidth()) {
        val (tvTitle, tvValue, button, divider) = createRefs()
        NestTypography(text = title,
            textStyle = NestTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.constrainAs(tvTitle) {
                top.linkTo(button.top)
                bottom.linkTo(button.bottom)
                start.linkTo(parent.start)
            })
        NestTypography(
            text = value,
            textStyle = NestTheme.typography.body2.copy(color = NestTheme.colors.GN._500),
            modifier = Modifier.constrainAs(tvValue) {
                top.linkTo(button.top)
                bottom.linkTo(button.bottom)
                end.linkTo(button.start)
            })
        NestIcon(iconId = IconUnify.CHEVRON_RIGHT,
            colorLightEnable = NestTheme.colors.NN._500,
            colorLightDisable = NestTheme.colors.NN._500,
            colorNightEnable = NestTheme.colors.NN._500,
            colorNightDisable = NestTheme.colors.NN._500,
            modifier = Modifier
                .clickable { onClick() }
                .constrainAs(button) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 4.dp)
                }
        )
        NestDivider(size = NestDividerSize.Small, modifier = Modifier
            .constrainAs(divider) {
                top.linkTo(button.bottom, margin = 4.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth())
    }
}
