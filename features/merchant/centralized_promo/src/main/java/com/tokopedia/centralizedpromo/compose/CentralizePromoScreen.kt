package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.centralizedpromo.R.drawable
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
@Preview(name = "NEXUS_7", device = Devices.NEXUS_5)
private fun PromotionCardPreview() {
    NestTheme {
        Surface(
            modifier = Modifier.fillMaxHeight(),
            contentColor = NestTheme.colors.NN._0
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(drawable.bg_bottom_circle),
                    contentDescription = null
                )
            }

            LazyVerticalGrid(columns = Fixed(2)) {
                item(span = { GridItemSpan(2) }) {
                    PromotionCard()
                }
                item(span = { GridItemSpan(2) }) {
                    LazyRow {
                        items(5) {
                            OnGoingCard()
                        }
                    }
                }
                items(10) {
                    PromotionCard()
                }
            }
        }
    }
}