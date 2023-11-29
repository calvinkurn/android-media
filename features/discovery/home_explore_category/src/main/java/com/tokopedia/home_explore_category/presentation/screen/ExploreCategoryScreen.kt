package com.tokopedia.home_explore_category.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType

@Composable
fun ExploreCategoryListGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
    }
}

@Composable
fun ExploreCategoryItem() {
    NestCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(147.dp),
        enableBounceAnimation = true,
        type = NestCardType.Border
    ) {
    }
}

@Composable
fun SubExploreCategoryItem() {
}
