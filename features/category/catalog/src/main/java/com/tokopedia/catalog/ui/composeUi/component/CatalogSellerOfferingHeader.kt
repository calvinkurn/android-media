@file:OptIn(ExperimentalComposeUiApi::class)

package com.tokopedia.catalog.ui.composeUi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.header.compose.HeaderActionButton
import com.tokopedia.header.compose.HeaderNotification
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.header.compose.NestHeaderVariant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.imageassets.TokopediaImageUrl.CATALOG_SELLER_OFFERING_PRODUCT_LIST_ICON
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.IconSource
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.catalog.R as catalogR
import com.tokopedia.localizationchooseaddress.R as localizationchooseaddressR

@Preview
@Composable
fun CatalogSellerOfferingHeaderPreview() {
    CatalogSellerOfferingHeader(
        backgroundColor = android.graphics.Color.WHITE,
        lcaListener = {
        }
    )
}

@Composable
fun CatalogSellerOfferingHeader(
    backgroundColor: Int,
    lcaListener: (ChooseAddressWidget) -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color(backgroundColor))
            .fillMaxWidth()
    ) {
        Column {
            WidgetLCA(lcaListener)
            HeaderSellerOffering()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CatalogSellerOfferingToolbar(
    title: String,
    subTitle: String,
    totalItemCart: Int,
    onToolbarBackIconPressed: () -> Unit,
    onClickVariant: () -> Unit,
    onClickActionButtonCart: () -> Unit,
    onClickActionButtonMenu: () -> Unit
) {
    NestHeader(
        modifier = Modifier
            .padding(start = 0.dp, end = 0.dp, top = 24.dp, bottom = 0.dp),
        variant = NestHeaderVariant.Transparent,
        type = NestHeaderType.Dynamic(
            content = {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    val textStyleTitle = if (subTitle.isNotEmpty()) {
                        NestTheme.typography.display3.copy(color = colorResource(id = catalogR.color.catalog_dms_light_color))
                    } else {
                        NestTheme.typography.display1.copy(
                            color = colorResource(id = catalogR.color.catalog_dms_dark_color_text_common),
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    NestTypography(
                        text = title,
                        textStyle = textStyleTitle,
                        modifier = Modifier.testTag("txtCatalogName").semantics {
                            this.testTagsAsResourceId = true
                        }
                    )
                    if (subTitle.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                onClickVariant.invoke()
                            }
                        ) {
                            NestTypography(
                                text = subTitle,
                                textStyle = NestTheme.typography.display1.copy(
                                    color = colorResource(id = catalogR.color.catalog_dms_dark_color_text_common),
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                            Divider(modifier = Modifier.width(4.dp))
                            NestIcon(
                                iconId = IconUnify.CHEVRON_DOWN,
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(16.dp),
                                colorLightEnable = colorResource(id = catalogR.color.catalog_dms_dark_color_accordion_arrow)
                            )
                        }
                    }
                }
            },
            onBackClicked = onToolbarBackIconPressed,
            optionsButton = listOf(
                HeaderActionButton(
                    onClicked = {
                        onClickActionButtonCart.invoke()
                    },
                    contentDescription = "icAddToCartPage",
                    icon = IconSource.Nest(IconUnify.CART),
                    notification = if (totalItemCart > Int.ZERO) {
                        HeaderNotification(totalItemCart.toString(), color = com.tokopedia.nest.components.Color.PRIMARY)
                    } else {
                        null
                    },
                    modifier = Modifier.semantics {
                        this.testTagsAsResourceId = true
                    }
                ),
                HeaderActionButton(
                    onClicked = {
                        onClickActionButtonMenu.invoke()
                    },
                    contentDescription = "icMenuBar",
                    icon = IconSource.Nest(IconUnify.MENU_HAMBURGER),
                    modifier = Modifier.semantics {
                        this.testTagsAsResourceId = true
                    }
                )
            )
        )
    )
}

@Composable
fun WidgetLCA(
    lcaListener: (ChooseAddressWidget) -> Unit
) {
    AndroidView(factory = { context ->
        val lcaWidget = ChooseAddressWidget(context)
        lcaListener.invoke(lcaWidget)
        lcaWidget.rootView.findViewById<IconUnify>(localizationchooseaddressR.id.icon_location)
            .setMargin(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
        lcaWidget
    }, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 8.dp))
}

@Composable
fun HeaderSellerOffering() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 10.dp, end = 16.dp, bottom = 10.dp)
    ) {
        NestImage(
            source = ImageSource.Remote(CATALOG_SELLER_OFFERING_PRODUCT_LIST_ICON),
            modifier = Modifier
                .height(66.dp)
                .width(66.dp)
                .padding(6.dp)
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            NestTypography(
                text = stringResource(id = catalogR.string.catalog_text_title_seller_offering),
                textStyle = NestTheme.typography.display1.copy(
                    color = colorResource(
                        id = catalogR.color.catalog_dms_light_color
                    ),
                    fontWeight = FontWeight.ExtraBold
                )
            )
            NestTypography(
                text = stringResource(id = catalogR.string.catalog_text_subtitle_seller_offering),
                textStyle = NestTheme.typography.display3.copy(color = colorResource(id = catalogR.color.catalog_dms_light_color))
            )
        }
    }
}

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor("#$this"))
}
