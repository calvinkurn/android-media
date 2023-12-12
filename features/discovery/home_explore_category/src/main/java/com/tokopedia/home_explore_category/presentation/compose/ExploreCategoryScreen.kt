package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.home_explore_category.presentation.util.DURATION_CATEGORY_EASING
import com.tokopedia.home_explore_category.presentation.util.categoryToggleTween
import com.tokopedia.home_explore_category.presentation.util.enterExpandVertical
import com.tokopedia.home_explore_category.presentation.util.exitShrinkVertical
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.addImpression
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val GRID_COLUMN = 3
const val MINIMUM_3_SUB_CAT = 3
const val DELAY_SCROLL_SUB_CAT = 200L

enum class VisibilityState {
    FULLY_VISIBLE,
    PARTIALLY_VISIBLE_TOP,
    PARTIALLY_VISIBLE_BOTTOM,
    NOT_VISIBLE
}

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
            val nestGlobalErrorType = remember(uiState.throwable) {
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

@Composable
fun ExploreCategoryListGrid(
    modifier: Modifier = Modifier,
    exploreCategoryResultUiModel: ExploreCategoryResultUiModel,
    uiEvent: (ExploreCategoryUiEvent) -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState()
) {
    // Chunk the list into rows of three items for the grid layout
    val categories = exploreCategoryResultUiModel.exploreCategoryList.chunked(GRID_COLUMN)

    val fullyVisibleIndices by remember(lazyListState) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                visibleItemsInfo.filter { item ->
                    val itemTop = item.offset
                    val itemBottom = itemTop + item.size
                    val viewportTop = layoutInfo.viewportStartOffset
                    val viewportBottom = layoutInfo.viewportEndOffset

                    // Check if the item is fully visible on both the top and bottom sides
                    itemTop >= viewportTop && itemBottom <= viewportBottom
                }.map { it.index }
            }
        }
    }

    var oldCategoryGroupIndex by remember {
        mutableStateOf(-1)
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        itemsIndexed(categories, key = { groupIndex, _ ->
            groupIndex
        }) { groupIndex, row ->

            var nestCardDimensions by remember { mutableStateOf(Pair(0f, 0f)) }
            var subCategoryItemDimensions by remember { mutableStateOf(Pair(0f, 0f)) }

            val (subCategoryItemTop, subCategoryItemBottom) = subCategoryItemDimensions
            val (nestCardTop, nestCardBottom) = nestCardDimensions

            var categoryName by remember {
                mutableStateOf("")
            }

            var isEligibleScrollToGroupIndex by remember {
                mutableStateOf(false)
            }

            var isEligibleScrollToThreeSubItems by remember {
                mutableStateOf(false)
            }

            var isCategorySelected by remember {
                mutableStateOf(false)
            }

            val isNotVisible by remember(lazyListState, groupIndex, fullyVisibleIndices) {
                derivedStateOf {
                    groupIndex !in fullyVisibleIndices
                }
            }

            val selectedCategory = remember(row) {
                row.find { it.isSelected }
            }

            val categoryIndex by remember(selectedCategory) {
                derivedStateOf {
                    getGroupIndexToScrollTo(categories, selectedCategory?.id.orEmpty())
                }
            }

            val isNestCardVisible by remember(lazyListState, nestCardTop, nestCardBottom) {
                derivedStateOf {
                    val layoutInfo = lazyListState.layoutInfo
                    val viewportTop = layoutInfo.viewportStartOffset
                    val viewportBottom = layoutInfo.viewportEndOffset

                    nestCardTop < viewportBottom && nestCardBottom > viewportTop
                }
            }

            LaunchedEffect(
                isEligibleScrollToGroupIndex
            ) {
                if (isEligibleScrollToGroupIndex && categoryIndex != -1) {
                    lazyListState.animateScrollToItem(categoryIndex)
                }
            }

            LaunchedEffect(isCategorySelected, nestCardDimensions) {
                if (!isCategorySelected && !isNestCardVisible) {
                    // we need to set delay to wait until animateScrollToItem has finished
                    val nestCardHeight = nestCardBottom - nestCardTop
                    val subCategoryItemHeight = subCategoryItemBottom - subCategoryItemTop

                    val remainSubItemsHeight =
                        Math.abs(nestCardHeight - (MINIMUM_3_SUB_CAT * subCategoryItemHeight))

                    lazyListState.scrollBy(remainSubItemsHeight)
                }
            }

            CategoryRowItem(row, uiEvent, categoryName = { categoryName = it }) { isSelected ->
                isEligibleScrollToGroupIndex = isNotVisible && !isSelected && oldCategoryGroupIndex != groupIndex
                isCategorySelected = isSelected
                oldCategoryGroupIndex = groupIndex
            }

            selectedCategory.let {
                AnimatedVisibility(
                    visible = it != null && it.isSelected,
                    enter = enterExpandVertical,
                    exit = exitShrinkVertical
                ) {
                    NestCard(
                        modifier = Modifier
                            .animateContentSize(
                                animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
                            )
                            .onGloballyPositioned { layoutCoordinates ->
                                val localPosition = layoutCoordinates.localToRoot(Offset(0f, 0f))
                                val nestCardTop = localPosition.y
                                nestCardDimensions =
                                    Pair(nestCardTop, nestCardTop + layoutCoordinates.size.height)
                            }
                            .fillMaxWidth(),
                        type = NestCardType.NoBorder
                    ) {
                        Column {
                            it?.subExploreCategoryList?.forEachIndexed { index, subcategory ->
                                key(subcategory) {
                                    SubExploreCategoryItem(
                                        modifier = Modifier
                                            .onGloballyPositioned { layoutCoordinates ->
                                                val localPosition =
                                                    layoutCoordinates.localToRoot(Offset(0f, 0f))
                                                val subCategoryItemTop = localPosition.y
                                                subCategoryItemDimensions = Pair(
                                                    subCategoryItemTop,
                                                    subCategoryItemTop + layoutCoordinates.size.height
                                                )
                                            }
                                            .addImpression(
                                                uniqueIdentifier = subcategory.name + index.toString(),
                                                impressionState = subcategory.impressHolderCompose,
                                                state = lazyListState,
                                                onItemViewed = {
                                                    uiEvent.invoke(
                                                        ExploreCategoryUiEvent.OnSubExploreCategoryItemImpressed(
                                                            categoryName,
                                                            subcategory,
                                                            index
                                                        )
                                                    )
                                                },
                                                impressInterval = 0L
                                            ),
                                        subExploreCategoryUiModel = subcategory,
                                        onUiEvent = uiEvent,
                                        categoryName = categoryName,
                                        actualPosition = index
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
fun CategoryRowItem(
    row: List<ExploreCategoryUiModel>,
    uiEvent: (ExploreCategoryUiEvent) -> Unit,
    categoryName: (String) -> Unit,
    onExploreCategoryItemClicked: (isSelected: Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        row.forEach { category ->
            categoryName.invoke(category.categoryTitle)

            Box(
                modifier = Modifier.weight(1f)
            ) {
                ExploreCategoryItem(
                    exploreCategoryUiModel = category,
                    onClick = {
                        uiEvent(ExploreCategoryUiEvent.OnExploreCategoryItemClicked(category))
                        onExploreCategoryItemClicked(category.isSelected)
                    },
                    modifier = Modifier
                )
            }
        }

        repeat(GRID_COLUMN - row.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

fun getGroupIndexToScrollTo(rows: List<List<ExploreCategoryUiModel>>, categoryId: String): Int {
    rows.forEachIndexed { index, row ->
        val indexInRow = row.indexOfFirst { it.id == categoryId }
        if (indexInRow != -1) {
            return index
        }
    }
    return -1
}

@Composable
fun ExploreCategoryItem(
    exploreCategoryUiModel: ExploreCategoryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
        modifier = modifier
            .fillMaxWidth()
            .height(147.dp),
        enableBounceAnimation = true,
        type = nestCardType,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    subExploreCategoryUiModel = item,
                    onUiEvent = {},
                    actualPosition = 0
                )
            }
        }
    }
}
