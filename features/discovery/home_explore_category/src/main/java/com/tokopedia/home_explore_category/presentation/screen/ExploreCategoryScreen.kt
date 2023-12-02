package com.tokopedia.home_explore_category.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val GRID_COLUMN = 3

@Composable
fun ExploreCategoryScreen(
    modifier: Modifier = Modifier,
    uiState: ExploreCategoryState<ExploreCategoryResultUiModel>,
    uiEvent: (ExploreCategoryUiEvent) -> Unit
) {
    when (uiState) {
        is ExploreCategoryState.Loading -> {
            ExploreCategoryShimmer(modifier)
        }

        is ExploreCategoryState.Fail -> {
            val nestGlobalErrorType = remember {
                when (uiState.throwable) {
                    is SocketTimeoutException, is UnknownHostException, is ConnectException -> NestGlobalErrorType.NoConnection
                    else -> NestGlobalErrorType.ServerError
                }
            }
            ExploreCategoryGlobalError(nestGlobalErrorType, uiEvent)
        }

        is ExploreCategoryState.Success -> {
            ExploreCategoryListGrid(
                exploreCategoryResultUiModel = uiState.data,
                modifier = modifier,
                uiEvent = uiEvent
            )
        }
    }
}

// @Composable
// fun ExploreCategoryListGridTemp() {

//
//    LazyColumn(state = scrollState, modifier = modifier) {
//
// //        categoryItemsInGrid(exploreCategoryResultUiModel.exploreCategoryList, uiEvent)
//
//        CategoryGridItems(modifier, exploreCategoryResultUiModel.exploreCategoryList, uiEvent)
//
//        selectedCategory?.let {
//            item {
//                SubExploreCategoryList(subExploreCategoryList = it.subExploreCategoryList, uiEvent)
//            }
//        }
//    }

//    val scrollLazyGridState = rememberLazyGridState()

//    LazyVerticalGrid(
//        columns = GridCells.Fixed(GRID_COLUMN),
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        state = scrollLazyGridState
//    ) {
//        items(exploreCategoryResultUiModel.exploreCategoryList) { category ->
//            ExploreCategoryItem(
//                exploreCategoryUiModel = category,
//                onClick = {
//                    uiEvent(ExploreCategoryUiEvent.OnExploreCategoryItemClicked(category.id))
//                }
//            )
//        }
//
//        exploreCategoryResultUiModel.exploreCategoryList.firstOrNull { it.isSelected }?.let { selectedCategory ->
//            item(span = { GridItemSpan(maxLineSpan) }) {
//                SubExploreCategoryList(subExploreCategoryList = selectedCategory.subExploreCategoryList, uiEvent)
//            }
//        }
//    }
// }

@Composable
fun ExploreCategoryListGrid(
    modifier: Modifier = Modifier,
    exploreCategoryResultUiModel: ExploreCategoryResultUiModel,
    uiEvent: (ExploreCategoryUiEvent) -> Unit = {}
) {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState, modifier = modifier.padding(horizontal = 16.dp)) {
        // Chunk the list into rows of three items for the grid layout
        val rows = exploreCategoryResultUiModel.exploreCategoryList.chunked(GRID_COLUMN)
        rows.forEach { row ->
            // Create a grid row
            item {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (category in row) {
                        Box(modifier = Modifier.weight(1f)) {
                            ExploreCategoryItem(
                                exploreCategoryUiModel = category,
                                onClick = {
                                    uiEvent(
                                        ExploreCategoryUiEvent.OnExploreCategoryItemClicked(
                                            category.id
                                        )
                                    )
                                }
                            )
                        }
                    }
                    if (GRID_COLUMN - row.size > Int.ZERO) {
                        for (i in 1..(GRID_COLUMN - row.size)) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            // Check if any category in the row is selected and show full-width item if so
            val selectedCategory = row.find { it.isSelected }
            selectedCategory?.let {
                item {
                    AnimatedVisibility(
                        visible = it.isSelected,
                        enter = expandVertically(
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = shrinkVertically(
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeOut(animationSpec = tween(durationMillis = 300))
                    ) {
                        NestCard(
                            modifier = modifier.fillMaxWidth(),
                            type = NestCardType.NoBorder
                        ) {
                            Column {
                                it.subExploreCategoryList.forEach { subcategory ->
                                    SubExploreCategoryItem(
                                        subExploreCategoryUiModel = subcategory,
                                        uiEvent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGridItems(
    modifier: Modifier = Modifier,
    categories: List<ExploreCategoryUiModel>,
    uiEvent: (ExploreCategoryUiEvent) -> Unit
) {
    val scrollLazyGridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMN),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollLazyGridState
    ) {
        items(categories) { category ->
            ExploreCategoryItem(
                exploreCategoryUiModel = category,
                onClick = {
                    uiEvent(ExploreCategoryUiEvent.OnExploreCategoryItemClicked(category.id))
                }
            )
        }
    }
}

fun LazyListScope.categoryItemsInGrid(
    categories: List<ExploreCategoryUiModel>,
    uiEvent: (ExploreCategoryUiEvent) -> Unit
) {
//    val rows = categories.chunked(GRID_COLUMN)
//    rows.forEachIndexed { index, rowCategories ->
//        item {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                rowCategories.forEach { category ->
//                    ExploreCategoryItem(
//                        exploreCategoryUiModel = category,
//                        onClick = {
//                            val scrollToIndex = index * GRID_COLUMN
//                            uiEvent(ExploreCategoryUiEvent.OnExploreCategoryItemClicked(category.id))
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(vertical = 8.dp)
//                    )
//                }
//
//                for (i in 1..(GRID_COLUMN - rowCategories.size)) {
//                    Spacer(modifier = Modifier.weight(1f))
//                }
//            }
//        }
//    }
}

@Composable
fun ExploreCategoryItem(
    modifier: Modifier = Modifier,
    exploreCategoryUiModel: ExploreCategoryUiModel,
    onClick: () -> Unit
) {
    val nestCardType = if (exploreCategoryUiModel.isSelected) {
        NestCardType.StateBorder(false)
    } else {
        NestCardType.NoBorder
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
        modifier = modifier
            .fillMaxWidth()
            .height(147.dp),
        enableBounceAnimation = true,
        type = nestCardType,
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(animationSpec = tween(durationMillis = 300))
                .fillMaxSize()
        ) {
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
                    exploreCategoryUiModel.categoryImageUrl
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
fun SubExploreCategoryList(
    subExploreCategoryList: List<ExploreCategoryUiModel.SubExploreCategoryUiModel>,
    onUiEvent: (ExploreCategoryUiEvent) -> Unit
) {
    NestCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        type = NestCardType.NoBorder
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(
                12.dp,
                Alignment.CenterVertically
            )
        ) {
            itemsIndexed(subExploreCategoryList) { index, item ->
                SubExploreCategoryItem(item, onUiEvent)
            }
        }
    }
}

@Composable
fun SubExploreCategoryItem(
    subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel,
    onUiEvent: (ExploreCategoryUiEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onUiEvent(
                    ExploreCategoryUiEvent.OnSubExploreCategoryItemClicked(
                        subExploreCategoryUiModel
                    )
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestImage(
            source = ImageSource.Remote(
                subExploreCategoryUiModel.imageUrl
            ),
            type = NestImageType.Rect(12.dp),
            modifier = Modifier.size(42.dp),
            contentDescription = null
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
                SubExploreCategoryItem(item) {}
            }
        }
    }
}
