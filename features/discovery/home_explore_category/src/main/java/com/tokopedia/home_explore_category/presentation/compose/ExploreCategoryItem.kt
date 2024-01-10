package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun ExploreCategoryItem(
    exploreCategoryUiModel: ExploreCategoryUiModel,
    onClick: () -> Unit
) {
    val nonSelectedColor = NestTheme.typography.display3.copy(
        fontWeight = FontWeight.Normal,
        color = NestTheme.colors.NN._950,
        textAlign = TextAlign.Center
    )

    val selectedColor = NestTheme.typography.display3.copy(
        fontWeight = FontWeight.Bold,
        color = NestTheme.colors.GN._500,
        textAlign = TextAlign.Center
    )

    val (nestCardType, titleTextStyle) = remember(exploreCategoryUiModel.isSelected) {
        if (exploreCategoryUiModel.isSelected) {
            Pair(NestCardType.StateBorder(false), selectedColor)
        } else {
            Pair(NestCardType.NoBorder, nonSelectedColor)
        }
    }

    NestCard(
        modifier = Modifier.height(147.dp),
        enableBounceAnimation = true,
        type = nestCardType,
        onClick = onClick
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                NestTypography(
                    text = exploreCategoryUiModel.categoryTitle,
                    textStyle = titleTextStyle,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // the reason using old approach, we need to avoid the intermittent crash causes coil-compose
            NestImage(
                imageUrl = exploreCategoryUiModel.categoryImageUrl,
                modifier = Modifier
                    .height(84.dp)
                    .fillMaxWidth()
            )
        }
    }
}
