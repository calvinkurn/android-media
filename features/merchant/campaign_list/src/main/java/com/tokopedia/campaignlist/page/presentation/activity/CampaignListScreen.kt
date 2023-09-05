package com.tokopedia.campaignlist.page.presentation.activity

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.common.constant.CampaignStatusConstant.AVAILABLE_STATUS_ID
import com.tokopedia.campaignlist.common.constant.CampaignStatusConstant.ONGOING_STATUS_ID
import com.tokopedia.campaignlist.common.constant.CampaignStatusConstant.UPCOMING_IN_NEAR_TIME_STATUS_ID
import com.tokopedia.campaignlist.common.constant.CampaignStatusConstant.UPCOMING_STATUS_ID
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestLabel
import com.tokopedia.nest.components.NestLabelType
import com.tokopedia.nest.components.NestSearchBar
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.components.ticker.NestTickerData
import com.tokopedia.nest.components.ticker.TickerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.sortfilter.compose.NestSortFilter
import com.tokopedia.sortfilter.compose.SortFilter

@Composable
fun CampaignListScreen(
    uiState: CampaignListViewModel.UiState,
    onTapCampaignStatusFilter: (List<CampaignStatusSelection>) -> Unit,
    onTapCampaignTypeFilter: (List<CampaignTypeSelection>) -> Unit,
    onClearFilter: () -> Unit,
    searchBarKeyword: String,
    onSearchBarKeywordChanged: (String) -> Unit,
    onSearchBarKeywordSubmit: () -> Unit,
    onSearchbarCleared: () -> Unit,
    onTickerDismissed: () -> Unit,
    onTapShareCampaignButton: (ActiveCampaign) -> Unit,
    onToolbarBackIconPressed: () -> Unit,
    onCampaignScrolled: (ActiveCampaign) -> Unit
) {
    Surface(
        modifier = Modifier
            .background(NestNN.light._0)
            .fillMaxSize()
    ) {
        Column {
            Toolbar(
                title = stringResource(id = R.string.cl_active_campaign_list),
                onToolbarBackIconPressed = onToolbarBackIconPressed
            )

            SearchBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                searchBarKeyword = searchBarKeyword,
                onSearchBarKeywordChanged = onSearchBarKeywordChanged,
                onSearchBarKeywordSubmit = onSearchBarKeywordSubmit,
                onSearchbarCleared = onSearchbarCleared
            )

            Filter(
                modifier = Modifier.padding(horizontal = 16.dp),
                selectedCampaignStatus = uiState.selectedCampaignStatus?.statusText.orEmpty(),
                selectedCampaignType = uiState.selectedCampaignType?.campaignTypeName.orEmpty(),
                onTapCampaignStatusFilter = { onTapCampaignStatusFilter(uiState.campaignStatus) },
                onTapCampaignTypeFilter = { onTapCampaignTypeFilter(uiState.campaignType) },
                onClearFilter = onClearFilter,
                shouldShowClearFilterIcon = uiState.showClearFilterIcon
            )

            if (!uiState.isTickerDismissed) {
                CampaignTicker(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    onDismissed = onTickerDismissed
                )
            }

            List(
                modifier = Modifier.tag(tag = "List"),
                campaigns = uiState.campaigns,
                onTapShareButton = onTapShareCampaignButton,
                onCampaignScrolled = onCampaignScrolled
            )
        }
    }
}

@Composable
fun List(
    modifier: Modifier = Modifier,
    campaigns: List<ActiveCampaign>,
    onTapShareButton: (ActiveCampaign) -> Unit,
    onCampaignScrolled: (ActiveCampaign) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(modifier = modifier, state = lazyListState) {
        items(campaigns.size, key = { campaigns[it].campaignId.toIntOrZero() }) {
            val campaign = campaigns[it]
            CampaignItem(lazyListState, campaign, onTapShareButton, onCampaignScrolled)
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    searchBarKeyword: String,
    onSearchBarKeywordChanged: (String) -> Unit,
    onSearchBarKeywordSubmit: () -> Unit,
    onSearchbarCleared: () -> Unit
) {
    NestSearchBar(
        value = TextFieldValue(searchBarKeyword, selection = TextRange(searchBarKeyword.length)),
        placeholderText = stringResource(id = R.string.cl_search_active_campaign),
        modifier = modifier.fillMaxWidth(),
        onSearchBarCleared = onSearchbarCleared,
        onTextChanged = {
            onSearchBarKeywordChanged(it)
        },
        onKeyboardSearchAction = onSearchBarKeywordSubmit
    )
}

@Composable
private fun Toolbar(
    modifier: Modifier = Modifier,
    title: String,
    onToolbarBackIconPressed: () -> Unit
) {
    NestHeader(
        modifier = modifier,
        type = NestHeaderType.SingleLine(
            title = title,
            onBackClicked = onToolbarBackIconPressed
        )
    )
}

@Composable
private fun Filter(
    modifier: Modifier = Modifier,
    selectedCampaignStatus: String,
    selectedCampaignType: String,
    onTapCampaignStatusFilter: () -> Unit,
    onTapCampaignTypeFilter: () -> Unit,
    onClearFilter: () -> Unit,
    shouldShowClearFilterIcon: Boolean
) {
    val campaignStatus = SortFilter(
        title = if (selectedCampaignStatus.isEmpty()) stringResource(id = R.string.cl_campaign_list_label_status) else selectedCampaignStatus,
        isSelected = selectedCampaignStatus.isNotEmpty(),
        onClick = onTapCampaignStatusFilter,
        showChevron = true
    )

    val campaignType = SortFilter(
        title = if (selectedCampaignType.isEmpty()) stringResource(id = R.string.cl_campaign_type) else selectedCampaignType,
        isSelected = selectedCampaignType.isNotEmpty(),
        onClick = onTapCampaignTypeFilter,
        showChevron = true
    )

    val sortFilterItems = arrayListOf(campaignStatus, campaignType)

    NestSortFilter(
        items = sortFilterItems,
        showClearFilterIcon = shouldShowClearFilterIcon,
        modifier = modifier.fillMaxWidth(),
        onClearFilter = onClearFilter,
        onItemClicked = {}
    )
}

@Composable
private fun CampaignTicker(modifier: Modifier = Modifier, onDismissed: () -> Unit) {
    NestTicker(
        ticker = listOf(
            NestTickerData(
                tickerTitle = "",
                tickerType = TickerType.ANNOUNCEMENT,
                tickerDescription = stringResource(id = R.string.cl_another_campaign_type_wording)
            )
        ),
        modifier = modifier.fillMaxWidth(),
        onDismissed = onDismissed
    )
}

@Composable
fun CampaignItem(
    state: LazyListState,
    campaign: ActiveCampaign,
    onTapShareButton: (ActiveCampaign) -> Unit,
    onCampaignScrolled: (ActiveCampaign) -> Unit
) {
    ItemImpression(key = campaign.campaignId.toIntOrZero(), lazyListState = state) {
        onCampaignScrolled(campaign)
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (
                    ribbon,
                    statusImage,
                    campaignType,
                    campaignStatus,
                    campaignImage,
                    campaignName,
                    productQty,
                    campaignStartDate,
                    campaignStartTime,
                    separator,
                    campaignEndDate,
                    campaignEndTime
                ) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.ic_green_top_drawing),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(ribbon) {
                        top.linkTo(parent.top, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_rocket),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .constrainAs(statusImage) {
                            top.linkTo(parent.top, margin = 12.dp)
                            start.linkTo(ribbon.end, margin = 12.dp)
                        }
                )

                NestTypography(
                    text = campaign.campaignType,
                    modifier = Modifier.constrainAs(campaignType) {
                        top.linkTo(statusImage.top)
                        bottom.linkTo(statusImage.bottom)
                        start.linkTo(statusImage.end, margin = 4.dp)
                    },
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.GN._500,
                        fontWeight = FontWeight.Bold
                    )
                )

                CampaignLabel(
                    modifier = Modifier.constrainAs(campaignStatus) {
                        top.linkTo(campaignType.top)
                        bottom.linkTo(campaignType.bottom)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                    campaignStatus = campaign.campaignStatus,
                    campaignStatusId = campaign.campaignStatusId.toIntOrZero()
                )

                NestImage(
                    imageUrl = campaign.campaignPictureUrl,
                    modifier = Modifier
                        .size(62.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .constrainAs(campaignImage) {
                            start.linkTo(parent.start, margin = 12.dp)
                            top.linkTo(campaignType.bottom, margin = 12.dp)
                        }
                )

                NestTypography(
                    text = campaign.campaignName,
                    modifier = Modifier.constrainAs(campaignName) {
                        top.linkTo(campaignImage.top)
                        start.linkTo(campaignImage.end, margin = 12.dp)
                    },
                    textStyle = NestTheme.typography.display2.copy(
                        fontWeight = FontWeight.Bold,
                        color = NestTheme.colors.NN._950
                    )
                )

                NestTypography(
                    text = stringResource(
                        id = R.string.cl_campaign_list_product_quantity_label,
                        campaign.productQty
                    ),
                    modifier = Modifier.constrainAs(productQty) {
                        top.linkTo(campaignName.bottom, margin = 12.dp)
                        start.linkTo(campaignName.start)
                    },
                    textStyle = NestTheme.typography.display3
                )

                NestTypography(
                    text = campaign.startDate,
                    modifier = Modifier.constrainAs(campaignStartDate) {
                        top.linkTo(productQty.bottom, margin = 12.dp)
                        start.linkTo(productQty.start)
                    },
                    textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._950)
                )

                NestTypography(
                    text = stringResource(
                        id = R.string.cl_campaign_time_template,
                        campaign.startTime
                    ),
                    modifier = Modifier.constrainAs(campaignStartTime) {
                        top.linkTo(campaignStartDate.bottom)
                        start.linkTo(campaignStartDate.start)
                    },
                    textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
                )

                NestTypography(
                    text = "-",
                    modifier = Modifier.constrainAs(separator) {
                        top.linkTo(campaignStartDate.top)
                        bottom.linkTo(campaignStartTime.bottom)
                        start.linkTo(campaignStartDate.end, margin = 12.dp)
                    },
                    textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
                )

                NestTypography(
                    text = campaign.endDate,
                    modifier = Modifier.constrainAs(campaignEndDate) {
                        top.linkTo(campaignStartDate.top)
                        bottom.linkTo(campaignStartDate.bottom)
                        start.linkTo(separator.end, margin = 12.dp)
                    },
                    textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._950)
                )

                NestTypography(
                    text = stringResource(
                        id = R.string.cl_campaign_time_template,
                        campaign.endTime
                    ),
                    modifier = Modifier.constrainAs(campaignEndTime) {
                        top.linkTo(campaignStartTime.top)
                        bottom.linkTo(campaignStartTime.bottom)
                        start.linkTo(separator.end, margin = 12.dp)
                    },
                    textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
                )
            }
            NestButton(
                text = stringResource(id = R.string.cl_action_share),
                onClick = { onTapShareButton(campaign) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                size = ButtonSize.SMALL
            )
        }
    }
}

@Composable
fun ItemImpression(key: Int, lazyListState: LazyListState, onItemViewed: () -> Unit) {
    val isItemWithKeyInView by remember {
        derivedStateOf {
            lazyListState.layoutInfo
                .visibleItemsInfo
                .any { it.key == key }
        }
    }

    if (isItemWithKeyInView) {
        LaunchedEffect(Unit) {
            onItemViewed()
        }
    }
}

@Composable
fun CampaignLabel(modifier: Modifier, campaignStatus: String, campaignStatusId: Int) {
    val nestLabelType = when (campaignStatusId) {
        ONGOING_STATUS_ID.toIntOrZero() -> NestLabelType.HIGHLIGHT_LIGHT_GREEN
        UPCOMING_STATUS_ID.toIntOrZero(), UPCOMING_IN_NEAR_TIME_STATUS_ID.toIntOrZero() -> NestLabelType.HIGHLIGHT_LIGHT_ORANGE
        AVAILABLE_STATUS_ID.toIntOrZero() -> NestLabelType.HIGHLIGHT_LIGHT_GREY
        else -> NestLabelType.HIGHLIGHT_LIGHT_RED
    }
    NestLabel(labelText = campaignStatus, labelType = nestLabelType, modifier = modifier)
}

@Preview(name = "Campaign List - Light Mode")
@Preview(name = "Campaign List - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CampaignListPreview() {
    val campaign = ActiveCampaign(
        campaignType = "Rilisan Spesial",
        campaignStatus = "Ditolak",
        campaignName = "Flash Sale 9.9",
        productQty = "9000 Product",
        startDate = "17/01/2020",
        startTime = "08:30 WIB",
        endDate = "18/01/2020",
        endTime = "22:00 WIB"
    )

    val state = CampaignListViewModel.UiState(
        campaigns = listOf(campaign),
        campaignStatus = emptyList(),
        campaignType = emptyList(),
        selectedCampaignStatus = null,
        selectedCampaignType = null,
        isTickerDismissed = false,
        showClearFilterIcon = true
    )

    NestTheme {
        CampaignListScreen(
            uiState = state,
            onTapCampaignStatusFilter = {},
            onTapCampaignTypeFilter = {},
            onClearFilter = {},
            onSearchBarKeywordSubmit = {},
            onSearchbarCleared = {},
            onTickerDismissed = {},
            onTapShareCampaignButton = {},
            onToolbarBackIconPressed = {},
            onCampaignScrolled = {},
            searchBarKeyword = "",
            onSearchBarKeywordChanged = {}
        )
    }
}

@Preview(name = "Campaign List - Filter selected")
@Composable
private fun CampaignListFilterSelectedPreview() {
    val campaign = ActiveCampaign(
        campaignType = "Rilisan Spesial",
        campaignStatus = "Ditolak",
        campaignName = "Flash Sale 9.9",
        productQty = "9000 Product",
        startDate = "17/01/2020",
        startTime = "08:30 WIB",
        endDate = "18/01/2020",
        endTime = "22:00 WIB"
    )

    val state = CampaignListViewModel.UiState(
        campaigns = listOf(campaign),
        campaignStatus = emptyList(),
        campaignType = emptyList(),
        selectedCampaignStatus = CampaignStatusSelection(listOf(2), "Berlangsung", true),
        selectedCampaignType = CampaignTypeSelection("2", "Rilisan", "Rilisan", true),
        isTickerDismissed = false,
        showClearFilterIcon = true
    )

    NestTheme {
        CampaignListScreen(
            uiState = state,
            onTapCampaignStatusFilter = {},
            onTapCampaignTypeFilter = {},
            onClearFilter = {},
            onSearchBarKeywordSubmit = {},
            onSearchbarCleared = {},
            onTickerDismissed = {},
            onTapShareCampaignButton = {},
            onToolbarBackIconPressed = {},
            onCampaignScrolled = {},
            onSearchBarKeywordChanged = { },
            searchBarKeyword = ""
        )
    }
}
