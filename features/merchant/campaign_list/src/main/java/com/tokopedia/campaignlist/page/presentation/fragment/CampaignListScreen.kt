package com.tokopedia.campaignlist.page.presentation.fragment

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.principles.NestImage
import com.tokopedia.common_compose.principles.NestLabel
import com.tokopedia.common_compose.principles.NestLabelType
import com.tokopedia.common_compose.principles.NestSearchBar
import com.tokopedia.common_compose.principles.NestSortFilter
import com.tokopedia.common_compose.principles.NestTicker
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.principles.SortFilter
import com.tokopedia.common_compose.principles.TickerType
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.kotlin.extensions.view.toIntOrZero

@Composable
fun CampaignListScreen(
    uiState: State<CampaignListViewModel.UiState>,
    onTapCampaignStatusFilter: (List<CampaignStatusSelection>) -> Unit,
    onTapCampaignTypeFilter: (List<CampaignTypeSelection>) -> Unit,
    onClearFilter: () -> Unit,
    onSearchBarKeywordSubmit: (String) -> Unit,
    onSearchbarCleared: () -> Unit,
    onTickerDismissed: () -> Unit,
    onTapShareCampaignButton : (ActiveCampaign) -> Unit,
    onToolbarBackIconPressed: () -> Unit
) {
    Column(modifier = Modifier
        .background(color = MaterialTheme.colors.primary)
        .fillMaxSize()
        .semantics { contentDescription = "Campaign List" }
    ) {
        Toolbar(
            title = stringResource(id = R.string.active_campaign_list),
            onToolbarBackIconPressed = onToolbarBackIconPressed
        )

        SearchBar(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            onSearchBarKeywordSubmit = onSearchBarKeywordSubmit,
            onSearchbarCleared = onSearchbarCleared
        )

        PageSortFilter(
            modifier = Modifier.padding(horizontal = 16.dp),
            selectedCampaignStatus = uiState.value.selectedCampaignStatus?.statusText.orEmpty(),
            selectedCampaignType = uiState.value.selectedCampaignType?.campaignTypeName.orEmpty(),
            onTapCampaignStatusFilter = { onTapCampaignStatusFilter(uiState.value.campaignStatus) },
            onTapCampaignTypeFilter = { onTapCampaignTypeFilter(uiState.value.campaignType) },
            onClearFilter = onClearFilter,
            shouldShowClearFilterIcon = uiState.value.showClearFilterIcon
        )

        if (!uiState.value.isTickerDismissed) {
            CampaignTicker(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                onDismissed = onTickerDismissed
            )
        }

        List(campaigns = uiState.value.campaigns, onTapShareCampaignButton)
    }
}

@Composable
fun List(campaigns: List<ActiveCampaign>, onTapShareButton: (ActiveCampaign) -> Unit) {
    LazyColumn {
        items(campaigns) {
            CampaignItem(it, onTapShareButton)
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchBarKeywordSubmit: (String) -> Unit,
    onSearchbarCleared: () -> Unit
) {
    NestSearchBar(
        modifier = modifier.fillMaxWidth(),
        placeholderText = stringResource(id = R.string.search_active_campaign),
        onSearchBarCleared = onSearchbarCleared,
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
        title = title,
        onToolbarBackIconPressed = onToolbarBackIconPressed
    )
}

@Composable
private fun PageSortFilter(
    modifier: Modifier = Modifier,
    selectedCampaignStatus: String,
    selectedCampaignType: String,
    onTapCampaignStatusFilter: () -> Unit,
    onTapCampaignTypeFilter: () -> Unit,
    onClearFilter: () -> Unit,
    shouldShowClearFilterIcon: Boolean
) {

    val campaignStatus = SortFilter(
        title = if (selectedCampaignStatus.isEmpty()) stringResource(id = R.string.campaign_list_label_status) else selectedCampaignStatus,
        isSelected = selectedCampaignStatus.isNotEmpty(),
        onClick = onTapCampaignStatusFilter
    )

    val campaignType = SortFilter(
        title = if (selectedCampaignType.isEmpty()) stringResource(id = R.string.campaign_type) else selectedCampaignType,
        isSelected = selectedCampaignType.isNotEmpty(),
        onClick = onTapCampaignTypeFilter
    )

    val sortFilterItems = arrayListOf(campaignStatus, campaignType)

    NestSortFilter(
        modifier = modifier.fillMaxWidth(),
        items = sortFilterItems,
        onClearFilter = onClearFilter,
        showClearFilterIcon = shouldShowClearFilterIcon
    )
}

@Composable
private fun CampaignTicker(modifier: Modifier = Modifier, onDismissed : () -> Unit) {
    NestTicker(
        modifier = modifier.fillMaxWidth(),
        title = "",
        description = stringResource(id = R.string.another_campaign_type_wording),
        onDismissed = onDismissed,
        type = TickerType.ANNOUNCEMENT
    )
}

@Composable
fun CampaignItem(campaign: ActiveCampaign, onTapShareButton : (ActiveCampaign) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (ribbon,
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
                campaignEndTime,
                buttonShare
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
                modifier = Modifier
                    .size(62.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .constrainAs(campaignImage) {
                        start.linkTo(parent.start, margin = 12.dp)
                        top.linkTo(campaignType.bottom, margin = 12.dp)
                    },
                imageUrl = campaign.campaignPictureUrl
            )

            NestTypography(
                text = campaign.campaignName,
                modifier = Modifier.constrainAs(campaignName) {
                    top.linkTo(campaignImage.top)
                    start.linkTo(campaignImage.end, margin = 12.dp)
                },
                textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold, color = NestTheme.colors.NN._950)
            )

            NestTypography(
                text = stringResource(id = R.string.campaign_list_product_quantity_label, campaign.productQty),
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
                text = campaign.startTime,
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
                text = campaign.endTime,
                modifier = Modifier.constrainAs(campaignEndTime) {
                    top.linkTo(campaignStartTime.top)
                    bottom.linkTo(campaignStartTime.bottom)
                    start.linkTo(separator.end, margin = 12.dp)
                },
                textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
            )

            NestButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .constrainAs(buttonShare) {
                        top.linkTo(campaignStartTime.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(id = R.string.action_share),
                onClick = { onTapShareButton(campaign) }
            )


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
    NestLabel(modifier = modifier, labelText = campaignStatus, nestLabelType = nestLabelType)
}

@Preview(name = "Campaign List Page")
@Composable
fun CampaignListPagePreview() {
    NestTheme {
        val campaigns = listOf(
            ActiveCampaign(
                campaignName = "Flash Sale 9.9",
                campaignStatus = "Dibatalkan",
                startDate = "09-09-2020",
                endDate = "10-09-2020",
                startTime = "00:00",
                endTime = "23:59",
                productQty = "9",
                campaignType = "Rilisan Spesial"
            )
        )
        val state = remember { mutableStateOf(CampaignListViewModel.UiState(campaigns = campaigns)) }
        CampaignListScreen(
            uiState = state,
            onTapCampaignStatusFilter = {},
            onTapCampaignTypeFilter = {},
            onTapShareCampaignButton = {},
            onClearFilter = {},
            onSearchBarKeywordSubmit = {},
            onSearchbarCleared = {},
            onTickerDismissed = {},
            onToolbarBackIconPressed = {}
        )
    }

}


@Preview(name = "Campaign List Page [Dark]", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CampaignListPageDarkPreview() {
    NestTheme {
        val campaigns = listOf(
            ActiveCampaign(
                campaignName = "Flash Sale 9.9",
                campaignStatus = "Dibatalkan",
                startDate = "09-09-2020",
                endDate = "10-09-2020",
                startTime = "00:00",
                endTime = "23:59",
                productQty = "9",
                campaignType = "Rilisan Spesial"
            )
        )
        val state = remember { mutableStateOf(CampaignListViewModel.UiState(campaigns = campaigns)) }
        CampaignListScreen(
            uiState = state,
            onTapCampaignStatusFilter = {},
            onTapCampaignTypeFilter = {},
            onTapShareCampaignButton = {},
            onClearFilter = {},
            onSearchBarKeywordSubmit = {},
            onSearchbarCleared = {},
            onTickerDismissed = {},
            onToolbarBackIconPressed = {}
        )
    }

}

@Preview(name = "Campaign Item")
@Composable
fun CampaignItemPreview() {
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

    CampaignItem(campaign, {})
}

