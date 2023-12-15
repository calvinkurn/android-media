package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.home_explore_category.presentation.util.DURATION_CATEGORY_EASING
import com.tokopedia.home_explore_category.presentation.util.categoryToggleTween
import com.tokopedia.home_explore_category.presentation.util.enterExpandVertical
import com.tokopedia.home_explore_category.presentation.util.exitShrinkVertical
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.utils.addImpression
import kotlinx.coroutines.delay
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.max
import kotlin.math.min

const val GRID_COLUMN = 3
const val MINIMUM_3_SUB_CAT = 3
const val HEIGHT_SUB_CAT = 54
const val DELAY_SCROLL_ANIMATION = 300L

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

    val notFullyTopItemSideVisibleIndices by remember(lazyListState) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                visibleItemsInfo.filter { item ->
                    val itemTop = item.offset
                    val viewportTop = layoutInfo.viewportStartOffset

                    // Check if the top side of the item is fully visible
                    itemTop < viewportTop
                }.map { it.index }
            }
        }
    }

    val notFullyBottomItemSideVisibleIndices by remember(lazyListState) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                visibleItemsInfo.filter { item ->
                    val itemTop = item.offset
                    val itemBottom = itemTop + item.size
                    val viewportBottom = layoutInfo.viewportEndOffset

                    // Check if the bottom side of the item is fully visible
                    itemBottom > viewportBottom
                }.map { it.index }
            }
        }
    }

    var isEligibleScrollToTopSide by remember {
        mutableStateOf(false)
    }

    var isEligibleScrollToBottomSide by remember {
        mutableStateOf(false)
    }

    var isEligibleScrollToThreeSubCategoryIndex by remember {
        mutableStateOf(false)
    }

    val localDensity = LocalDensity.current

    val minNestCardVisibleHeight =
        with(localDensity) { (MINIMUM_3_SUB_CAT * (HEIGHT_SUB_CAT.dp).toPx()) }

    var nestCardDimensions by remember { mutableStateOf(Pair(0f, 0f)) }

    var categoryItemDimensions by remember {
        mutableStateOf(Pair(0f, 0f))
    }

    var subCategoryItemDimensions by remember { mutableStateOf(Pair(0f, 0f)) }

    val viewportHeight = with(localDensity) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    var prevNestCardHeight by remember {
        mutableStateOf(0f)
    }

    val categoryItemTop by remember(categoryItemDimensions) {
        derivedStateOf {
            categoryItemDimensions.first
        }
    }

    val categoryItemBottom by remember(categoryItemDimensions) {
        derivedStateOf {
            categoryItemDimensions.second
        }
    }

    val subCategoryItemTop by remember(subCategoryItemDimensions) {
        derivedStateOf {
            subCategoryItemDimensions.first
        }
    }

    val subCategoryItemBottom by remember(subCategoryItemDimensions) {
        derivedStateOf {
            subCategoryItemDimensions.second
        }
    }

    val nestCardTop by remember(nestCardDimensions) {
        derivedStateOf {
            nestCardDimensions.first
        }
    }

    val nestCardBottom by remember(nestCardDimensions) {
        derivedStateOf {
            nestCardDimensions.second
        }
    }

    var categoryName by remember {
        mutableStateOf("")
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

            var isFirstItemHeightMeasured by remember { mutableStateOf(false) }

            val isNotVisible by remember(lazyListState, groupIndex, fullyVisibleIndices) {
                derivedStateOf {
                    groupIndex !in fullyVisibleIndices
                }
            }

            val isTopSideNotVisible by remember(
                lazyListState,
                groupIndex,
                notFullyTopItemSideVisibleIndices
            ) {
                derivedStateOf {
                    groupIndex in notFullyTopItemSideVisibleIndices
                }
            }

            val isBottomSideNotVisible by remember(
                lazyListState,
                groupIndex,
                notFullyBottomItemSideVisibleIndices
            ) {
                derivedStateOf {
                    groupIndex in notFullyBottomItemSideVisibleIndices
                }
            }

            val isNestCardNotVisible by remember(
                nestCardDimensions,
                groupIndex
            ) {
                derivedStateOf {
                    val layoutInfo = lazyListState.layoutInfo
                    val viewportTop = layoutInfo.viewportStartOffset
                    val viewportBottom = layoutInfo.viewportEndOffset

                    val visibleHeight = max(
                        0f,
                        min(nestCardBottom, viewportBottom.toFloat()) - max(
                            nestCardTop,
                            viewportTop.toFloat()
                        )
                    )

                    visibleHeight < minNestCardVisibleHeight
                }
            }

            val nestCardHeight by remember(
                nestCardDimensions,
                groupIndex
            ) {
                derivedStateOf {
                    val layoutInfo = lazyListState.layoutInfo
                    val viewportTop = layoutInfo.viewportStartOffset
                    val viewportBottom = layoutInfo.viewportEndOffset

                    // Debugging: Print relevant values
                    println("viewportTop: $viewportTop, viewportBottom: $viewportBottom")
                    println("nestCardTop: $nestCardTop, nestCardBottom: $nestCardBottom")

                    val calculatedHeight = max(
                        0f,
                        min(nestCardDimensions.second, viewportBottom.toFloat()) - max(
                            nestCardDimensions.first,
                            viewportTop.toFloat()
                        )
                    )

                    // Debugging: Print the calculated height
                    println("calculatedHeight: $calculatedHeight")

                    calculatedHeight
                }
            }

            val categoryItemHeight by remember(
                lazyListState,
                groupIndex,
                categoryItemDimensions
            ) {
                derivedStateOf {
                    val layoutInfo = lazyListState.layoutInfo
                    val viewportTop = layoutInfo.viewportStartOffset
                    val viewportBottom = layoutInfo.viewportEndOffset

                    val visibleHeight = max(
                        0f,
                        min(categoryItemBottom, viewportBottom.toFloat()) - max(
                            categoryItemTop,
                            viewportTop.toFloat()
                        )
                    )

                    println("categoryItemHeight: $visibleHeight")

                    visibleHeight
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

            CategoryRowItem(
                row,
                uiEvent,
                categoryName = { categoryName = it },
                onExploreCategoryItemClicked = { isSelected ->
//                isEligibleScrollToTopSide = !isSelected && isTopSideNotVisible
                    isEligibleScrollToTopSide = !isSelected && isNotVisible

//                isEligibleScrollToBottomSide = !isSelected && isBottomSideNotVisible
                    isEligibleScrollToThreeSubCategoryIndex = !isSelected && isNestCardNotVisible
                },
                categoryItemDimensionsCallback = { categoryItemDimensions = it }
            )

            selectedCategory.let {
                val isVisible = it != null

                AnimatedVisibility(
                    visible = isVisible,
                    enter = enterExpandVertical,
                    exit = exitShrinkVertical
                ) {
                    LaunchedEffect(isVisible) {
                        if (isEligibleScrollToTopSide && isNotVisible) {
                            if (categoryIndex != -1) {
                                delay(DELAY_SCROLL_ANIMATION)
                                lazyListState.animateScrollToItem(categoryIndex)
                            }
                        } else if (isEligibleScrollToThreeSubCategoryIndex && isNestCardNotVisible) {
                            val verticalMargin = with(localDensity) {
                                8.dp.toPx()
                            }

                            prevNestCardHeight = max(0f, min(nestCardBottom, viewportHeight) - max(nestCardTop, 0f))
                            delay(DELAY_SCROLL_ANIMATION)

                            val heightSubCat = (
                                with(localDensity) {
                                    HEIGHT_SUB_CAT.dp.toPx()
                                } * 3
                                ) + verticalMargin

                            val nestCardHeight = max(0f, min(nestCardBottom, viewportHeight) - max(nestCardTop, 0f))

                            val subCategoriesHeight = ((subCategoryItemBottom - subCategoryItemTop) * 3)

//                            val nestCardHeight = max(0f, min(nestCardBottom, viewportHeight) - max(nestCardTop, 0f))
//                            val nestCardHeight = nestCardBottom - nestCardTop

                            println("nestCardHeight: $nestCardHeight")
                            println("subCategoryItemsHeight: $subCategoriesHeight")
                            println("prevNestCardHeight: $prevNestCardHeight")

                            val remainSubItemsHeight =
                                if (nestCardHeight < subCategoriesHeight) {
                                    Math.abs(subCategoriesHeight - nestCardHeight)
                                } else {
                                    nestCardHeight
                                } - prevNestCardHeight

                            lazyListState.animateScrollBy(remainSubItemsHeight)
                        }
                    }
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(
                                animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
                            )
                            .onGloballyPositioned { layoutCoordinates ->
//                                val viewTop = layoutCoordinates.localToWindow(Offset.Zero).y
//
//                                val viewBottom = layoutCoordinates.localToWindow(
//                                    Offset(
//                                        0f,
//                                        layoutCoordinates.size.height.toFloat()
//                                    )
//                                ).y

                                val viewTop =
                                    layoutCoordinates.localToRoot(Offset.Zero).y

                                val viewBottom = viewTop + layoutCoordinates.size.height.toFloat()

                                nestCardDimensions =
                                    Pair(
                                        viewTop,
                                        viewBottom
                                    )

                                if (!isFirstItemHeightMeasured) {
//                                    prevNestCardHeight = viewBottom - viewTop
                                    isFirstItemHeightMeasured = true
                                }
                            }
                    ) {
                        NestCard(
                            type = NestCardType.NoBorder
                        ) {
                            Column(
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                it?.subExploreCategoryList?.forEachIndexed { index, subcategory ->
                                    SubExploreCategoryItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                uiEvent(
                                                    ExploreCategoryUiEvent.OnSubExploreCategoryItemClicked(
                                                        subExploreCategoryUiModel = subcategory,
                                                        position = index,
                                                        categoryName = categoryName
                                                    )
                                                )
                                            }
                                            .onGloballyPositioned { layoutCoordinates ->
                                                val viewTop =
                                                    layoutCoordinates.localToRoot(Offset.Zero).y

//                                                val viewBottom = layoutCoordinates.localToWindow(
//                                                    Offset(
//                                                        0f,
//                                                        layoutCoordinates.size.height.toFloat()
//                                                    )
//                                                ).y

                                                val viewBottom = viewTop + layoutCoordinates.size.height.toFloat()

                                                subCategoryItemDimensions = Pair(
                                                    viewTop,
                                                    viewBottom
                                                )
                                            }
                                            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
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
                                        subExploreCategoryUiModel = subcategory
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
    onExploreCategoryItemClicked: (isSelected: Boolean) -> Unit,
    categoryItemDimensionsCallback: (categoryItemDimens: Pair<Float, Float>) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        row.forEach { category ->
            categoryName.invoke(category.categoryTitle)

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                ExploreCategoryItem(
                    modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                        val localPosition = layoutCoordinates.localToRoot(Offset(0f, 0f))
                        categoryItemDimensionsCallback.invoke(
                            Pair(
                                localPosition.y,
                                localPosition.y + layoutCoordinates.size.height
                            )
                        )
                    },
                    exploreCategoryUiModel = category,
                    onClick = {
                        onExploreCategoryItemClicked(category.isSelected)
                        uiEvent(ExploreCategoryUiEvent.OnExploreCategoryItemClicked(category))
                    }
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
