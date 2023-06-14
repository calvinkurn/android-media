package com.tokopedia.common_compose.components.ticker
import com.tokopedia.common_compose.R

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestGN
import com.tokopedia.common_compose.ui.NestNN
import com.tokopedia.common_compose.ui.NestTheme
import kotlinx.coroutines.delay

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
    val tickerTitle: CharSequence = "",
    val tickerDescription: CharSequence = "",
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
    val title = findLongestTickerTitle(ticker)
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
        tickerTitle = title,
        tickerDescription = desc,
        isCarousel = ticker.size > 1,
        onDismissed = {},
        closeButtonVisibility = true
    )
}

@Composable
private fun findLongestTickerTitle(
    ticker: List<NestTickerData>
): String {
    var title = ""
    ticker.forEach {
        if (it.tickerTitle.length > title.length) {
            title = it.tickerDescription.toString()
        }
    }
    return title
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
    val iconColor = getIconColor(tickerType)
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
                    TickerimageIcon(tickerIcon, Modifier.align(Alignment.CenterVertically), iconColor)
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
        painter = painterResource(R.drawable.iconunify_close),
        modifier = Modifier.clickable { onDismissed() }.size(16.dp),
        contentDescription = "Close Icon",
        tint = closeIconColor
    )
}

@Composable
private fun TickerimageIcon(
    tickerIconRes: Int,
    modifier: Modifier = Modifier,
    tickerIconColor: Color = NestTheme.colors.NN._900
) {
    Icon(
        painter = painterResource(tickerIconRes),
        modifier = modifier.size(24.dp),
        contentDescription = "Close Icon",
        tint = tickerIconColor
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
            R.drawable.iconunify_information
        }
        TickerType.ERROR -> {
            R.drawable.iconunify_error
        }
        TickerType.WARNING -> {
            R.drawable.iconunify_warning
        }
    }

@Composable
private fun getSurfaceShape(tickerVariant: TickerVariant) =
    when (tickerVariant) {
        TickerVariant.LOOSE -> {
            RoundedCornerShape(8.dp)
        }
        TickerVariant.FULL -> {
            RoundedCornerShape(0.dp)
        }
    }

@Composable
private fun getIconColor(tickerType: TickerType) =
    when (tickerType) {
        TickerType.ANNOUNCEMENT -> {
            NestTheme.colors.BN._400
        }
        TickerType.WARNING -> {
            NestTheme.colors.YN._300
        }
        TickerType.ERROR -> {
            NestTheme.colors.RN._500
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
    CombinedTickerPreview(false)
}

@Preview(name = "All ticker dark")
@Composable
fun NestAllTickerDarkSample() {
    CombinedTickerPreview(true)
}

@Composable
private fun CombinedTickerPreview(darkTheme: Boolean = false) {
    NestTheme(darkTheme) {
        Column {
            NestTickerLoosePreview()
            NestTickerFullPreview()
            NestSingleTickerLoosePreview()
            NestSingleTickerFullPreview()
            NestSingleTickerLooseWithoutTitlePreview()
            NestSingleTickerFullWithoutTitlePreview()
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

@Preview(name = "Ticker")
@Composable
fun NestSingleTickerFullWithoutTitlePreview() {
    val ticker = listOf<NestTickerData>(
        NestTickerData(
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
fun NestSingleTickerLooseWithoutTitlePreview() {
    val ticker = listOf<NestTickerData>(
        NestTickerData(
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
