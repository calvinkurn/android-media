package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.components.R as nestcomponentsR

@Composable
fun SubExploreCategoryItem(
    modifier: Modifier = Modifier,
    subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // the reason using old approach, we need to avoid the intermittent crash causes coil-compose
        val painter = rememberImagePainter(data = subExploreCategoryUiModel.imageUrl, builder = {
            placeholder(nestcomponentsR.drawable.imagestate_placeholder)
            error(nestcomponentsR.drawable.imagestate_error)
        })
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        NestTypography(
            text = subExploreCategoryUiModel.name,
            textStyle = NestTheme.typography.display3,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreCategoryScreenPreview() {
    val subExploreCategoryList = listOf(
        ExploreCategoryUiModel.SubExploreCategoryUiModel(
            id = "1",
            name = "Computer & Laptop Computer & Laptop Computer & Laptop Computer & Laptop",
            imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
            appLink = "",
            categoryLabel = "",
            buIdentifier = ""
        ),
        ExploreCategoryUiModel.SubExploreCategoryUiModel(
            id = "1",
            name = "Kamera Terkini",
            imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
            appLink = "",
            categoryLabel = "",
            buIdentifier = ""
        ),
        ExploreCategoryUiModel.SubExploreCategoryUiModel(
            id = "1",
            name = "Baju Terkini",
            imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
            appLink = "",
            categoryLabel = "",
            buIdentifier = ""
        ),
        ExploreCategoryUiModel.SubExploreCategoryUiModel(
            id = "1",
            name = "Sepatu Terkini",
            imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
            appLink = "",
            categoryLabel = "",
            buIdentifier = ""
        ),
        ExploreCategoryUiModel.SubExploreCategoryUiModel(
            id = "1",
            name = "Celana Terkini",
            imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
            appLink = "",
            categoryLabel = "",
            buIdentifier = ""
        ),
        ExploreCategoryUiModel.SubExploreCategoryUiModel(
            id = "1",
            name = "Rok Terkini",
            imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
            appLink = "",
            categoryLabel = "",
            buIdentifier = ""
        )
    )
    NestCard(
        modifier = Modifier.fillMaxWidth(),
        type = NestCardType.Border
    ) {
        LazyColumn(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(
                12.dp,
                Alignment.CenterVertically
            )
        ) {
            itemsIndexed(subExploreCategoryList) { index, item ->
                SubExploreCategoryItem(
                    subExploreCategoryUiModel = item
                )
            }
        }
    }
}
