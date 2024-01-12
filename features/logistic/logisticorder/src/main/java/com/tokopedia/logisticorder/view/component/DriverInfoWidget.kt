package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.uimodel.LastDriverModel
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun DriverInfoWidget(
    modifier: Modifier,
    lastDriver: LastDriverModel,
    callDriver: (phoneNumber: String) -> Unit,
    openTippingInfo: () -> Unit
) {
    ConstraintLayout(modifier) {
        val (driverImage, driverName, driverLicense, driverInfo, callButton) = createRefs()
        NestImage(
            modifier = Modifier
                .size(48.dp, 48.dp)
                .constrainAs(driverImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            type = NestImageType.Circle,
            source = if (lastDriver.photo.isNotEmpty()) {
                ImageSource.Remote(lastDriver.photo)
            } else {
                ImageSource.Painter(
                    R.drawable.ic_find_driver
                )
            }
        ) {
            NestImage(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(48.dp, 48.dp),
                type = NestImageType.Circle,
                source = ImageSource.Painter(R.drawable.ic_find_driver)
            )
        }
        NestTypography(
            modifier = Modifier.constrainAs(driverName) {
                top.linkTo(parent.top)
                start.linkTo(driverImage.end, margin = 8.dp)
            },
            textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950),
            text = lastDriver.name.ifEmpty { stringResource(id = R.string.driver_not_found_title) }
        )
        NestIcon(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(13.dp, 13.dp)
                .constrainAs(driverInfo) {
                    start.linkTo(driverName.end)
                    top.linkTo(driverName.top)
                    bottom.linkTo(driverName.bottom)
                    visibility =
                        if (lastDriver.name.isNotEmpty()) Visibility.Visible else Visibility.Gone
                }
                .clickable { openTippingInfo() },
            iconId = IconUnify.INFORMATION
        )
        NestTypography(
            modifier = Modifier.constrainAs(driverLicense) {
                top.linkTo(driverName.bottom)
                start.linkTo(driverName.start)
            },
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950),
            text = if (lastDriver.name.isNotEmpty()) lastDriver.licenseNumber else stringResource(id = R.string.driver_not_found_subtitle)
        )
        if (lastDriver.phone.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(36.dp, 36.dp)
                    .border(1.dp, NestTheme.colors.NN._300, CircleShape)
                    .constrainAs(callButton) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .clickable { callDriver(lastDriver.phone) },
                contentAlignment = Alignment.Center
            ) {
                NestIcon(modifier = Modifier.size(20.dp, 20.dp), iconId = IconUnify.CALL)
            }
        }
    }
}

@Preview
@Composable
private fun DriveInfoWidgetPreview() {
    val data = LastDriverModel(
        name = "Budi",
        phone = "+6256648394543",
        licenseNumber = "B 1238 JED",
        isChanged = true
    )

    NestTheme {
        DriverInfoWidget(modifier = Modifier.fillMaxWidth(), lastDriver = data, callDriver = {}) {
        }
    }
}
