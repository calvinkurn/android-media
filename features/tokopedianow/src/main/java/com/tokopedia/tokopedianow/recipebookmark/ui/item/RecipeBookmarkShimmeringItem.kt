package com.tokopedia.tokopedianow.recipebookmark.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType

@Composable
fun RecipeBookmarkShimmeringItem() {
    NestCard(
        modifier = Modifier.fillMaxWidth(),
        type = NestCardType.Border
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            NestLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                variant = NestLoaderType.Shimmer(
                    type = NestShimmerType.Rect()
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NestLoader(
                    modifier = Modifier
                        .width(206.dp)
                        .height(16.dp),
                    variant = NestLoaderType.Shimmer(
                        type = NestShimmerType.Line
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                NestLoader(
                    modifier = Modifier
                        .height(18.dp)
                        .width(18.dp),
                    variant = NestLoaderType.Shimmer(
                        type = NestShimmerType.Rect(5.dp)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 4.dp,
                        bottom = 12.dp
                    )
            ) {
                NestLoader(
                    modifier = Modifier
                        .width(42.dp)
                        .height(12.dp),
                    variant = NestLoaderType.Shimmer(
                        type = NestShimmerType.Line
                    )
                )
                NestLoader(
                    modifier = Modifier
                        .width(42.dp)
                        .height(12.dp)
                        .padding(start = 4.dp),
                    variant = NestLoaderType.Shimmer(
                        type = NestShimmerType.Line
                    )
                )
            }
        }
    }
}
