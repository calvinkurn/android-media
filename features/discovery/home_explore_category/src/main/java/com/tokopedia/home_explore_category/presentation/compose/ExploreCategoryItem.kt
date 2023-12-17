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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun ExploreCategoryItem(
    exploreCategoryUiModel: ExploreCategoryUiModel,
    onClick: () -> Unit
) {
    val nestCardType = remember(exploreCategoryUiModel.isSelected) {
        if (exploreCategoryUiModel.isSelected) {
            NestCardType.StateBorder(false)
        } else {
            NestCardType.NoBorder
        }
    }

    val titleTextStyle = if (exploreCategoryUiModel.isSelected) {
        NestTheme.typography.display3.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.GN._500
        )
    } else {
        NestTheme.typography.display3.copy(
            fontWeight = FontWeight.Normal,
            color = NestTheme.colors.NN._950
        )
    }

    NestCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(147.dp),
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
            NestImage(
                source = ImageSource.Remote(
                    source = exploreCategoryUiModel.categoryImageUrl,
                    shouldRetried = true
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}
