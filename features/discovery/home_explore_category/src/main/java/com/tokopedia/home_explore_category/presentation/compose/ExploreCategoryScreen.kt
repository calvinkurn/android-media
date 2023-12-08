package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.home_explore_category.presentation.util.enterExpandVertical
import com.tokopedia.home_explore_category.presentation.util.exitShrinkVertical
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.addImpression
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    var subCategoryItemHeightInPx by remember {
        mutableStateOf(0f)
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

            var categoryName by remember {
                mutableStateOf("")
            }

            var isEligibleSelected by remember {
                mutableStateOf(false)
            }

            var isNestCardVisible by remember {
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

            val subItemHeights = remember(selectedCategory) {
                val sizeItems = selectedCategory?.subExploreCategoryList?.take(3)?.size.orZero()
                (subCategoryItemHeightInPx * sizeItems)
            }

            LaunchedEffect(isEligibleSelected) {
                if (isEligibleSelected) {
                    if (categoryIndex != -1) {
                        lazyListState.animateScrollToItem(categoryIndex)
                    }
                } else {
                    if (isNestCardVisible) {
                        lazyListState.scrollBy(subItemHeights)
                    }
                }
            }

            CategoryRowItem(row, groupIndex = groupIndex,
                uiEvent = uiEvent, categoryName = {
                    categoryName = it
                }, onExploreCategoryItemClicked = { isSelected ->
                    isEligibleSelected =
                        isNotVisible && !isSelected && oldCategoryGroupIndex != groupIndex
                    oldCategoryGroupIndex = groupIndex
                })

            if (selectedCategory != null) {

                selectedCategory.let {

                    AnimatedVisibility(
                        visible = it.isSelected,
                        enter = enterExpandVertical,
                        exit = exitShrinkVertical
                    ) {

                        NestCard(
                            modifier = Modifier
                                .fillMaxWidth(),
                            type = NestCardType.NoBorder
                        ) {

                            isNestCardVisible = !isNotVisible

                            Column {
                                it.subExploreCategoryList.forEachIndexed { index, subcategory ->
                                    key(subcategory) {
                                        SubExploreCategoryItem(
                                            modifier = Modifier
                                                .onGloballyPositioned { layoutCoordinates ->
                                                    subCategoryItemHeightInPx =
                                                        layoutCoordinates.size.height.toFloat()
                                                }
                                                .addImpression(
                                                    uniqueIdentifier = subcategory.name + index.toString(),
                                                    impressionState = subcategory.impressHolderCompose,
                                                    state = lazyListState,
                                                    onItemViewed = {
                                                        uiEvent.invoke(
                                                            ExploreCategoryUiEvent.OnSubExploreCategoryItemImpressed(
                                                                categoryName, subcategory, index
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
            } else {
                isNestCardVisible = false
            }
        }
    }
}

@Composable
fun CategoryRowItem(
    row: List<ExploreCategoryUiModel>,
    groupIndex: Int,
    uiEvent: (ExploreCategoryUiEvent) -> Unit,
    categoryName: (String) -> Unit,
    onExploreCategoryItemClicked: (isSelected: Boolean) -> Unit,
) {

    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        row.forEachIndexed { index, category ->

            categoryName.invoke(category.categoryTitle)

            Box(
                modifier = Modifier.weight(1f)
            ) {
                ExploreCategoryItem(
                    exploreCategoryUiModel = category,
                    onClick = {

                        uiEvent(
                            ExploreCategoryUiEvent.OnExploreCategoryItemClicked(
                                category
                            )
                        )

                        onExploreCategoryItemClicked(category.isSelected)
                    },
                    modifier = Modifier
                )
            }
        }

        repeat(GRID_COLUMN - row.size) {
            val actualPosition = (groupIndex * GRID_COLUMN) + it
            key(actualPosition) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

fun scrollToCategoryItemClicked(
    categoryId: String,
    categories: List<List<ExploreCategoryUiModel>>,
    lazyListState: LazyListState,
    coroutineScope: CoroutineScope
) {
    val categoryIndex = getGroupIndexToScrollTo(categories, categoryId)
    if (categoryIndex != -1) {
        coroutineScope.launch {
            lazyListState.animateScrollToItem(categoryIndex)
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

@Composable
fun SubExploreCategoryItem(
    modifier: Modifier = Modifier,
    subExploreCategoryUiModel: ExploreCategoryUiModel.SubExploreCategoryUiModel,
    onUiEvent: (ExploreCategoryUiEvent) -> Unit,
    actualPosition: Int,
    categoryName: String = ""
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onUiEvent(
                    ExploreCategoryUiEvent.OnSubExploreCategoryItemClicked(
                        subExploreCategoryUiModel = subExploreCategoryUiModel,
                        position = actualPosition,
                        categoryName = categoryName
                    )
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestImage(
            source = ImageSource.Remote(
                subExploreCategoryUiModel.imageUrl,
                shouldRetried = true
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

// Function to check if a rect is fully visible in the viewport
fun isRectFullyVisibleInViewport(rect: Rect, viewportHeight: Float): Boolean {
    return rect.top >= 0 && rect.bottom <= viewportHeight
}

// Function to get the visible rect of a composable in the LazyColumn
fun getVisibleRectInViewport(
    lazyListState: LazyListState,
    composableIndex: Int
): Rect {
    val layoutInfo = lazyListState.layoutInfo
    val composableInfo = layoutInfo.visibleItemsInfo[composableIndex]
    return Rect(
        left = 0f,
        top = composableInfo.offset.toFloat(),
        right = layoutInfo.viewportEndOffset.toFloat(),
        bottom = (composableInfo.offset + composableInfo.size).toFloat()
    )
}

fun isSubExploreCategoryItemVisible(
    lazyListState: LazyListState,
    groupIndex: Int,
    itemIndex: Int
): Boolean {
    val visibleRect = getVisibleRectInViewport(lazyListState, groupIndex)
    val itemRect = getVisibleRectInViewport(lazyListState, itemIndex)
    val viewportHeight = lazyListState.layoutInfo.viewportEndOffset

    return isRectFullyVisibleInViewport(itemRect, viewportHeight.toFloat())
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
                    actualPosition = 0,
                )
            }
        }
    }
}
