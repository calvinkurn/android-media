package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromo.R.drawable
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoComposeViewModel
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun CentralizedPromoScreen(viewModel: CentralizedPromoComposeViewModel) {

    val uiState = viewModel.layoutList.collectAsState().value

    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine().copy(
                title = "Iklan dan Promosi"
            )
        )
    }) {
        Surface(
            modifier = Modifier.fillMaxHeight().padding(it),
            contentColor = NestTheme.colors.NN._0
        ) {
            BackgroundDrawable()

            if (uiState.isLoading) {
                Text("Loading")
            } else {
                LazyVerticalGrid(
                    columns = Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    uiState.data.forEach {
                        if (it is OnGoingPromoListUiModel) {
                            OnGoingPromoSection(
                                lazyGridScope = this,
                                data = it
                            )
                        } else if (it is PromoCreationListUiModel) {
                            CreatePromoSection(
                                lazyGridScope = this,
                                data = it
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnGoingPromoSection(
    lazyGridScope: LazyGridScope,
    data: OnGoingPromoListUiModel
) = with(lazyGridScope) {
    val context = LocalContext.current

    item(span = { GridItemSpan(2) }) {
        PromoSectionTitle(
            text = "Fitur promosi aktifmu",
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }

    item(span = { GridItemSpan(2) }) {
        LazyRow() {
            items(data.items) { onGoingData ->
                OnGoingCard(
                    title = onGoingData.title,
                    counter = onGoingData.status.count,
                    counterTitle = onGoingData.status.text,
                    onTitleClicked = {
                        RouteManager.route(
                            context,
                            onGoingData.status.url
                        )
                    },
                    onFooterClicked = {
                        RouteManager.route(
                            context,
                            onGoingData.footer.url
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun CreatePromoSection(
    lazyGridScope: LazyGridScope,
    data: PromoCreationListUiModel
) = with(lazyGridScope) {
    item(span = { GridItemSpan(2) }) {
        PromoSectionTitle(
            text = "Buat Promosi",
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }

    items(
        items = data.items,
        key = {
            it.pageId
        },
        contentType = {
            LayoutType.PROMO_CREATION
        }) { promoCreationData ->
        PromotionCard(
            title = promoCreationData.title,
            labelNew = promoCreationData.titleSuffix,
            description = promoCreationData.description,
            imageUrl = promoCreationData.icon,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}


@Composable
private fun BackgroundDrawable() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(drawable.bg_bottom_circle),
            contentDescription = null
        )
    }
}

@Preview(name = "NEXUS_7", device = Devices.NEXUS_5)
@Composable
private fun CentralizedPromoScreenPreview() {
//    CentralizedPromoScreen()
}