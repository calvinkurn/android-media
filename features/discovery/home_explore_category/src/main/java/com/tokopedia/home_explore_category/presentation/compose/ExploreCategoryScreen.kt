package com.tokopedia.home_explore_category.presentation.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiEvent
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.impression
import com.tokopedia.home_explore_category.presentation.util.DURATION_CATEGORY_EASING
import com.tokopedia.home_explore_category.presentation.util.categoryToggleTween
import com.tokopedia.home_explore_category.presentation.util.enterExpandVertical
import com.tokopedia.home_explore_category.presentation.util.exitShrinkVertical
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import kotlinx.coroutines.delay
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val GRID_COLUMN = 3
const val MINIMUM_3_SUB_CAT = 3
const val DELAY_SCROLL_ANIMATION = 325L

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

    val context = LocalContext.current

    var nestCardDimensions by remember { mutableStateOf(Pair(0f, 0f)) }

    var subCategoryItemDimensions by remember { mutableStateOf(Pair(0f, 0f)) }

    val horizontalPadding = remember {
        if (DeviceScreenInfo.isTablet(context)) {
            val isLandscape =
                context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            if (isLandscape) {
                (123 * 2).dp
            } else {
                123.dp
            }
        } else {
            16.dp
        }
    }

    val localDensity = LocalDensity.current
    val localConfiguration = LocalConfiguration.current

    val screenHeight = with(localDensity) {
        localConfiguration.screenHeightDp.dp.toPx()
    }

    val verticalMargin = with(localDensity) {
        16.dp.toPx()
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            bottom = 8.dp
        ),
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(categories, key = { groupIndex, _ ->
            groupIndex.toString()
        }) { groupIndex, row ->

            val selectedCategory = remember(row) {
                row.find { it.isSelected }
            }

            val childCategorySize = selectedCategory?.subExploreCategoryList?.size.orZero()

            val isTopSideNotVisible by remember(groupIndex, lazyListState) {
                derivedStateOf {
                    groupIndex in lazyListState.layoutInfo.visibleItemsInfo
                        .filter { it.offset < lazyListState.layoutInfo.viewportStartOffset }
                        .map { it.index }
                }
            }

            val isBottomSideNotVisible by remember(
                groupIndex,
                lazyListState
            ) {
                derivedStateOf {
                    groupIndex in lazyListState.layoutInfo.visibleItemsInfo
                        .filter { it.offset + it.size > lazyListState.layoutInfo.viewportEndOffset }
                        .map { it.index }
                }
            }

            val nestCardHeight by remember(
                lazyListState,
                nestCardDimensions
            ) {
                derivedStateOf {
                    val visibleTop = maxOf(
                        0f,
                        nestCardDimensions.first - lazyListState.layoutInfo.viewportStartOffset
                    )
                    val visibleBottom = minOf(
                        screenHeight,
                        nestCardDimensions.second - lazyListState.layoutInfo.viewportStartOffset
                    )

                    maxOf(0f, visibleBottom - visibleTop)
                }
            }

            val subCategoriesHeight by remember(
                selectedCategory,
                subCategoryItemDimensions
            ) {
                derivedStateOf {
                    if (selectedCategory != null) {
                        ((subCategoryItemDimensions.second - subCategoryItemDimensions.first) * MINIMUM_3_SUB_CAT)
                    } else {
                        0f
                    }
                }
            }

            val isNestCardNotMinimumVisible by remember(
                selectedCategory,
                nestCardDimensions,
                subCategoryItemDimensions
            ) {
                derivedStateOf {
                    if (selectedCategory != null) {
                        val visibleTop = maxOf(
                            0f,
                            nestCardDimensions.first - lazyListState.layoutInfo.viewportStartOffset
                        )
                        val visibleBottom = minOf(
                            screenHeight,
                            nestCardDimensions.second - lazyListState.layoutInfo.viewportStartOffset
                        )

                        val nestCardHeight = maxOf(0f, visibleBottom - visibleTop)

                        val subCategoriesHeight =
                            (((subCategoryItemDimensions.second - subCategoryItemDimensions.first)) * MINIMUM_3_SUB_CAT) - verticalMargin

                        (nestCardHeight < subCategoriesHeight)
                    } else {
                        false
                    }
                }
            }

            val categoryItemHeight by remember(
                selectedCategory,
                lazyListState,
                groupIndex
            ) {
                derivedStateOf {
                    val layoutInfo = lazyListState.layoutInfo

                    val lazyItemInfo =
                        lazyListState.layoutInfo.visibleItemsInfo.find { it.index == groupIndex }

                    if (selectedCategory != null && lazyItemInfo != null) {
                        val itemTop = lazyItemInfo.offset
                        val itemBottom = itemTop + lazyItemInfo.size
                        val viewportBottom = layoutInfo.viewportEndOffset

                        (itemBottom - viewportBottom).toFloat()
                    } else {
                        0f
                    }
                }
            }

            val categoryIndex = remember(selectedCategory?.id) {
                getGroupIndexToScrollTo(categories, selectedCategory?.id.orEmpty())
            }

            CategoryRowItem(row, uiEvent)

            val isSubCategoryVisible = selectedCategory != null

            LaunchedEffect(isSubCategoryVisible) {
                if (isSubCategoryVisible) {
                    when {
                        isTopSideNotVisible && categoryIndex != -1 -> {
                            delay(DELAY_SCROLL_ANIMATION)
                            lazyListState.animateScrollToItem(categoryIndex)
                        }

                        isBottomSideNotVisible -> {
                            val remainSubItemsHeight =
                                (categoryItemHeight + subCategoriesHeight) - verticalMargin

                            delay(DELAY_SCROLL_ANIMATION)

                            if (nestCardHeight == 0f) {
                                lazyListState.animateScrollBy(remainSubItemsHeight)
                            }
                        }

                        else -> {
                            delay(DELAY_SCROLL_ANIMATION)

                            if (isNestCardNotMinimumVisible) {
                                val singleCategoryHeight = (subCategoriesHeight / MINIMUM_3_SUB_CAT)

                                val remainSubItemsHeight =
                                    if (childCategorySize < MINIMUM_3_SUB_CAT) {
                                        ((singleCategoryHeight * childCategorySize) - nestCardHeight)
                                    } else {
                                        if (nestCardHeight < subCategoriesHeight) {
                                            Math.abs(subCategoriesHeight - nestCardHeight)
                                        } else {
                                            nestCardHeight
                                        }
                                    }

                                val remainNeedToScroll =
                                    if (nestCardHeight < singleCategoryHeight) {
                                        remainSubItemsHeight + verticalMargin
                                    } else {
                                        remainSubItemsHeight
                                    }

                                lazyListState.animateScrollBy(remainNeedToScroll)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isSubCategoryVisible,
                enter = enterExpandVertical,
                exit = exitShrinkVertical
            ) {
                NestCard(
                    modifier = Modifier
                        .createNestCardModifier { nestCardDimens ->
                            nestCardDimensions = nestCardDimens
                        },
                    type = NestCardType.NoBorder
                ) {
                    Column {
                        selectedCategory?.subExploreCategoryList?.forEachIndexed { index, subcategory ->
                            val subCategoryKey = subcategory.name + index.toString()

                            key(subCategoryKey) {
                                SubExploreCategoryItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            uiEvent(
                                                ExploreCategoryUiEvent.OnSubExploreCategoryItemClicked(
                                                    subExploreCategoryUiModel = subcategory,
                                                    position = index,
                                                    categoryName = selectedCategory.categoryTitle
                                                )
                                            )
                                        }
                                        .onGloballyPositioned { layoutCoordinates ->
                                            val viewTop =
                                                layoutCoordinates.localToRoot(Offset.Zero).y

                                            val viewBottom =
                                                viewTop + layoutCoordinates.size.height.toFloat()

                                            subCategoryItemDimensions = Pair(
                                                viewTop,
                                                viewBottom
                                            )
                                        }
                                        .padding(
                                            top = 12.dp,
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = if (index == selectedCategory.subExploreCategoryList.size.orZero() - 1) 8.dp else 0.dp
                                        )
                                        .impression(
                                            key = subCategoryKey,
                                            onImpression = {
                                                uiEvent.invoke(
                                                    ExploreCategoryUiEvent.OnSubExploreCategoryItemImpressed(
                                                        selectedCategory.categoryTitle,
                                                        subcategory,
                                                        index
                                                    )
                                                )
                                            }
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

fun Modifier.createNestCardModifier(
    nestCardDimensions: (Pair<Float, Float>) -> Unit
) = this
    .fillMaxWidth()
    .padding(top = 8.dp)
    .animateContentSize(
        animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
    )
    .onGloballyPositioned {
        updateNestCardDimensions(it, nestCardDimensions)
    }

fun updateNestCardDimensions(
    layoutCoordinates: LayoutCoordinates,
    nestCardDimensions: (Pair<Float, Float>) -> Unit
) {
    val viewTop = layoutCoordinates.localToRoot(Offset.Zero).y
    val viewBottom = viewTop + layoutCoordinates.size.height.toFloat()
    nestCardDimensions.invoke(Pair(viewTop, viewBottom))
}

@Composable
fun CategoryRowItem(
    row: List<ExploreCategoryUiModel>,
    uiEvent: (ExploreCategoryUiEvent) -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        row.forEach { category ->

            val categoryKey = "${category.categoryTitle}_${category.categoryImageUrl}"
            key(categoryKey) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    ExploreCategoryItem(exploreCategoryUiModel = category, onClick = {
                        uiEvent(ExploreCategoryUiEvent.OnExploreCategoryItemClicked(category))
                    })
                }
            }
        }

        repeat(GRID_COLUMN - row.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

fun getGroupIndexToScrollTo(
    rows: List<List<ExploreCategoryUiModel>>,
    categoryId: String
): Int {
    rows.forEachIndexed { index, row ->
        val indexInRow = row.indexOfFirst { it.id == categoryId }
        if (indexInRow != -1) {
            return index
        }
    }
    return -1
}
