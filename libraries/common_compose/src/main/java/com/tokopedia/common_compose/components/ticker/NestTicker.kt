package com.tokopedia.common_compose.components.ticker
import com.tokopedia.common_compose.R

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestGN
import com.tokopedia.common_compose.ui.NestNN
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.IconUnify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

enum class TickerType {
    ANNOUNCEMENT, WARNING, ERROR
}

enum class TickerVariant {
    LOOSE, FULL
}

data class ColorType<T>(
    val default: T,
    val light: T = default,
    val dark: T = default,
    val disabled: T = default
)

data class NestTickerData(
    val tickerTitle: CharSequence,
    val tickerDescription: CharSequence,
    val tickerVariant: TickerVariant = TickerVariant.LOOSE,
    val tickerType: TickerType = TickerType.ANNOUNCEMENT,
)

data class TickerIndicator (
    var isActive: Boolean = false
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NestTicker(
    ticker: List<NestTickerData>,
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit = {},
    closeButtonVisibility: Boolean = true,
    autoScrollEnabled: Boolean = true,
    autoScrollDuration: Long = 3000L
) {
    Box(modifier) {
        val pagerState = rememberPagerState()
        val isCarousel = ticker.size > 1

        /**
         * Reserve height for carousel type
         */
        val tickerHeightState = remember { mutableStateOf(0.dp) }
        LargestTickerComponent(ticker, tickerHeightState, Modifier)

        HorizontalPager(
            modifier = Modifier.height(tickerHeightState.value),
            state = pagerState,
            count = ticker.size) { page ->
            val tickerData = ticker.getOrNull(page)
            NestTickerCard(
                modifier = Modifier.fillMaxSize(),
                tickerTitle = tickerData?.tickerTitle?:"",
                tickerDescription = tickerData?.tickerDescription?:"",
                tickerType = tickerData?.tickerType?:TickerType.ANNOUNCEMENT,
                tickerVariant = tickerData?.tickerVariant?:TickerVariant.LOOSE,
                isCarousel = isCarousel,
                onDismissed = onDismissed,
                closeButtonVisibility = closeButtonVisibility
            )
        }

        if (isCarousel) {
            TickerIndicators(
                Modifier.Companion.align(Alignment.BottomCenter),
                ticker,
                pagerState
            )

            if (autoScrollEnabled) {
                SetupAutoScroll(pagerState, autoScrollDuration)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SetupAutoScroll(pagerState: PagerState, autoScrollDuration: Long) {
    LaunchedEffect(Unit) {
        while (true) {
            delay(autoScrollDuration)

            val size = pagerState.pageCount
            var currentPosition = pagerState.currentPage
            val targetPage = if (currentPosition < size - 1) ++currentPosition else 0

            pagerState.animateScrollToPage(page = targetPage)

        }
    }
}

@Composable
private fun LargestTickerComponent(
    ticker: List<NestTickerData>,
    tickerHeightState: MutableState<Dp>,
    modifier: Modifier
    ) {
    val desc = findLongestTickerDescription(ticker)
    val density = LocalDensity.current
    NestTickerCard(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                tickerHeightState.value = with(density) { layoutCoordinates.size.height.toDp() }
            }
            .wrapContentHeight()
            .fillMaxWidth()
            .alpha(0f),
        tickerTitle = "Test Ticker",
        tickerDescription = desc,
        isCarousel = ticker.size > 1,
        onDismissed = {},
        closeButtonVisibility = true
    )
}

@Composable
private fun findLongestTickerDescription(
    ticker: List<NestTickerData>
): String {
    var desc1 = ""
    ticker.forEach {
        if (it.tickerDescription.length > desc1.length) {
            desc1 = it.tickerDescription.toString()
        }
    }
    return desc1
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TickerIndicators(
    indicatorModifier: Modifier,
    ticker: List<NestTickerData>,
    pagerState: PagerState
) {
    val indicatorList = ticker.map { TickerIndicator() }
    val indicatorState = remember { mutableStateOf(indicatorList) }

    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect {
            val updatedList = ticker.map { TickerIndicator() }
            updatedList[it].isActive = true

            indicatorState.value = updatedList
        }
    }
    Column(indicatorModifier) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = indicatorState.value) {
                TickerIndicatorDot(isActive = it.isActive)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun NestTickerCard(
    modifier: Modifier = Modifier,
    tickerTitle: CharSequence,
    tickerDescription: CharSequence,
    tickerVariant: TickerVariant = TickerVariant.LOOSE,
    tickerType: TickerType = TickerType.ANNOUNCEMENT,
    onDismissed: () -> Unit = {},
    closeButtonVisibility: Boolean = true,
    isCarousel: Boolean = false
) {
    // Implementation are specifically to cater ANNOUNCEMENT ticker type
    val backgroundColor = getBackgroundColor(tickerType)
    val strokeColor = getStrokeColor(tickerType)
    val iconColor = getIconColor(LocalContext.current, tickerType)
    val surfaceShape = getSurfaceShape(tickerVariant)
    val tickerIcon = getIcon(tickerType)
    val closeIconColor = NestTheme.colors.NN._900

    Box {
        Surface(
            modifier = modifier,
            shape = surfaceShape,
            color = backgroundColor.default,
            border = getTickerBorderShape(tickerVariant, strokeColor),
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    TickerimageIcon(Modifier.align(Alignment.CenterVertically), tickerIcon, iconColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    TickerContent(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f), tickerTitle, tickerDescription)
                    Spacer(modifier = Modifier.width(10.dp))
                    if (closeButtonVisibility) {
                        TickerCloseIcon(onDismissed, closeIconColor)
                    }
                }
                if (isCarousel) {
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
        if (tickerVariant == TickerVariant.FULL) {
            Divider(Modifier.align(Alignment.BottomEnd), strokeColor.default)
        }
    }
}

@Composable
private fun TickerIndicatorDot(
    selectedColor: Color = NestGN.light._500,
    onClick: () -> Unit = {},
    shape: Shape = CircleShape,
    notSelectedColor: Color = NestNN.light._200,
    isActive: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .size(6.dp)
            .background(
                if (isActive) {
                    selectedColor
                } else {
                    notSelectedColor
                }
            )
            .then(
                Modifier
                    .clickable {
                        onClick.invoke()
                    }))
}

@Composable
private fun TickerContent(
    modifier: Modifier,
    tickerTitle: CharSequence,
    tickerDescription: CharSequence
) {
    Column(modifier) {
        if (tickerTitle.isNotEmpty()) {
            NestTypography(
                text = tickerTitle,
                modifier = Modifier.fillMaxWidth(),
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.NN._950,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        if (tickerDescription.isNotEmpty()) {
            NestTypography(
                text = tickerDescription,
                modifier = Modifier.fillMaxWidth(),
                textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950)
            )
        }
    }
}

@Composable
private fun TickerCloseIcon(
    onDismissed: () -> Unit,
    closeIconColor: Color = NestTheme.colors.NN._900
) {
    Icon(
        imageVector = Icons.Outlined.Close,
        modifier = Modifier.clickable { onDismissed() },
        contentDescription = "Close Icon",
        tint = closeIconColor
    )
}

@Composable
private fun TickerimageIcon(
    modifier: Modifier,
    tickerIcon: Int,
    iconColor: ColorType<Int>
) {
    AndroidView(
        modifier = modifier.size(24.dp),
        factory = {
            val iconUnify = IconUnify(it)
            iconUnify.apply {
                this.setImage(
                    newIconId = tickerIcon,
                    newLightEnable = iconColor.light,
                    newDarkEnable = iconColor.dark,
                    newDarkDisable = iconColor.disabled,
                    newLightDisable = iconColor.disabled
                )
            }
        }
    )
}

@Composable
private fun getTickerBorderShape(
    tickerVariant: TickerVariant,
    strokeColor: ColorType<Color>
) = when (tickerVariant) {
    TickerVariant.FULL -> {
        null
    }
    TickerVariant.LOOSE -> {
        BorderStroke(1.dp, strokeColor.default)
    }
}

@Composable
private fun getIcon(tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            IconUnify.INFORMATION
        }
        TickerType.ERROR -> {
            IconUnify.ERROR
        }
        TickerType.WARNING -> {
            IconUnify.WARNING
        }
    }

@Composable
private fun getSurfaceShape(tickerVariant: TickerVariant) =
    when (tickerVariant) {
        TickerVariant.LOOSE -> {
            RoundedCornerShape(6.dp)
        }
        TickerVariant.FULL -> {
            RoundedCornerShape(0.dp)
        }
    }

@Composable
private fun getIconColor(context: Context, tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            ColorType(
                default = ContextCompat.getColor(context, R.color.Unify_BN400)
            )
        }
        TickerType.WARNING -> {
            ColorType(
                default = ContextCompat.getColor(context, R.color.Unify_YN300)
            )
        }
        TickerType.ERROR -> {
            ColorType(
                default = ContextCompat.getColor(context, R.color.Unify_RN500)
            )
        }
    }

@Composable
private fun getStrokeColor(tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            ColorType(default = NestTheme.colors.BN._200)
        }
        TickerType.WARNING -> {
            ColorType(default = NestTheme.colors.YN._200)
        }
        TickerType.ERROR -> {
            ColorType(default = NestTheme.colors.RN._200)
        }
    }

@Composable
private fun getBackgroundColor(tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            ColorType(default = NestTheme.colors.BN._50)
        }
        TickerType.WARNING -> {
            ColorType(default = NestTheme.colors.YN._50)
        }
        TickerType.ERROR -> {
            ColorType(default = NestTheme.colors.RN._50)
        }
    }

@Preview(name = "All ticker")
@Composable
fun NestAllTickerSample() {
    Column {
        NestTickerLoosePreview()
        NestTickerFullPreview()
        NestSingleTickerLoosePreview()
        NestSingleTickerFullPreview()
    }
}

@Preview(name = "All ticker dark")
@Composable
fun NestAllTickerDarkSample() {
    NestTheme(darkTheme = true) {
        Column {
            NestTickerLoosePreview()
            NestTickerFullPreview()
            NestSingleTickerLoosePreview()
            NestSingleTickerFullPreview()
        }
    }
}
@Preview(name = "Ticker")
@Composable
fun NestTickerLoosePreview() {
    val ticker = listOf<NestTickerData>(
        NestTickerData(
            tickerTitle = "Maaf ada gangguan",
            tickerType = TickerType.WARNING,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        ),
        NestTickerData(
            tickerTitle = "Gak boleh!",
            tickerType = TickerType.ERROR,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        ),
        NestTickerData(
            tickerTitle = "Ada diskon juga loh!",
            tickerType = TickerType.ANNOUNCEMENT,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        ),
    )
    NestTicker(
        modifier = Modifier.padding(8.dp),
        ticker = ticker,
        onDismissed = {},
        closeButtonVisibility = true
    )
}

@Preview(name = "Ticker")
@Composable
fun NestTickerFullPreview() {
    val ticker = listOf<NestTickerData>(
        NestTickerData(
            tickerTitle = "Maaf ada gangguan",
            tickerType = TickerType.WARNING,
            tickerVariant = TickerVariant.FULL,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        ),
        NestTickerData(
            tickerTitle = "Gak boleh!",
            tickerType = TickerType.ERROR,
            tickerVariant = TickerVariant.FULL,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        ),
        NestTickerData(
            tickerTitle = "Ada diskon juga loh!",
            tickerType = TickerType.ANNOUNCEMENT,
            tickerVariant = TickerVariant.FULL,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        ),
    )
    NestTicker(
        modifier = Modifier.padding(8.dp),
        ticker = ticker,
        onDismissed = {},
        closeButtonVisibility = true
    )
}

@Preview(name = "Ticker")
@Composable
fun NestSingleTickerFullPreview() {
    val ticker = listOf<NestTickerData>(
        NestTickerData(
            tickerTitle = "Maaf ada gangguan",
            tickerType = TickerType.WARNING,
            tickerVariant = TickerVariant.FULL,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        )
    )
    NestTicker(
        modifier = Modifier.padding(8.dp),
        ticker = ticker,
        onDismissed = {},
        closeButtonVisibility = true
    )
}

@Preview(name = "Ticker")
@Composable
fun NestSingleTickerLoosePreview() {
    val ticker = listOf<NestTickerData>(
        NestTickerData(
            tickerTitle = "Maaf ada gangguan",
            tickerType = TickerType.ERROR,
            tickerVariant = TickerVariant.LOOSE,
            tickerDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,",
        )
    )
    NestTicker(
        modifier = Modifier.padding(8.dp),
        ticker = ticker,
        onDismissed = {},
        closeButtonVisibility = true
    )
}
