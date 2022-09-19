package com.tokopedia.campaignlist.page.presentation.fragment

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2Response
import com.tokopedia.campaignlist.common.data.model.response.GetSellerCampaignSellerAppMetaResponse
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder.Companion.AVAILABLE_STATUS_ID
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder.Companion.ONGOING_STATUS_ID
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder.Companion.UPCOMING_IN_NEAR_TIME_STATUS_ID
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder.Companion.UPCOMING_STATUS_ID
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success

@Composable
fun CampaignListScreen(
    viewModel: CampaignListViewModel,
    onCampaignStatusTap: (List<CampaignStatusSelection>) -> Unit,
    onCampaignTypeTap: (List<CampaignTypeSelection>) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val response = viewModel.getCampaignListResult.observeAsState()
        val meta = viewModel.getSellerMetaDataResult.observeAsState()

        SearchBar(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))

        if (meta.value is Success) {
            val unformattedCampaignType =
                (meta.value as Success<GetSellerCampaignSellerAppMetaResponse>).data.getSellerCampaignSellerAppMeta.campaignTypeData
            val campaignType =
                viewModel.mapCampaignTypeDataToCampaignTypeSelections(unformattedCampaignType)

            val unformattedCampaignStatus =
                (meta.value as Success<GetSellerCampaignSellerAppMetaResponse>).data.getSellerCampaignSellerAppMeta.campaignStatus
            val campaignStatus: List<CampaignStatusSelection> =
                viewModel.mapCampaignStatusToCampaignStatusSelections(unformattedCampaignStatus)

            SortFilter(
                modifier = Modifier.padding(horizontal = 16.dp),
                onCampaignStatusTap = { onCampaignStatusTap(campaignStatus) },
                onCampaignTypeTap = { onCampaignTypeTap(campaignType) },
                onDismissed = { viewModel.getCampaignList() })
        }

        Ticker(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))

        if (response.value is Success) {
            val items =
                (response.value as Success<GetCampaignListV2Response>).data.getCampaignListV2.campaignList
            val formattedCampaigns = viewModel.mapCampaignListDataToActiveCampaignList(items)
            List(campaigns = formattedCampaigns)
        }
    }
}

@Composable
fun List(campaigns: List<ActiveCampaign>) {
    LazyColumn {
        items(campaigns) {
            CampaignItem(it)
        }
    }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    val editorAction: (TextView, Int, KeyEvent) -> Boolean = { textView, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
            val query = textView.text.toString()
            //viewModel.setCampaignName(query)
            //   viewModel.getCampaignList(campaignName = query)
            true
        } else {
            false
        }
    }
    UnifySearchBar(
        modifier = modifier.fillMaxWidth(),
        placeholderText = stringResource(id = R.string.search_active_campaign),
        onTextChanged = { text ->

        }, onEditorAction = editorAction
    )

}

@Composable
private fun SortFilter(
    modifier: Modifier = Modifier,
    onCampaignStatusTap: () -> Unit,
    onCampaignTypeTap: () -> Unit,
    onDismissed: () -> Unit
) {
    val campaignStatus = SortFilterItem(
        stringResource(id = R.string.campaign_list_label_status),
        ChipsUnify.TYPE_NORMAL,
        ChipsUnify.TYPE_NORMAL,
        onCampaignStatusTap
    )
    val campaignType = SortFilterItem(
        stringResource(R.string.campaign_type),
        ChipsUnify.TYPE_SELECTED,
        ChipsUnify.TYPE_NORMAL,
        onCampaignTypeTap
    )

    UnifySortFilter(
        modifier = modifier.fillMaxWidth(),
        items = arrayListOf(campaignStatus, campaignType),
        filterRelationship = SortFilter.RELATIONSHIP_AND,
        filterType = SortFilter.TYPE_QUICK,
        onDismissed = onDismissed
    )
}

@Composable
private fun Ticker(modifier: Modifier = Modifier) {
    UnifyTicker(
        modifier = modifier.fillMaxWidth(),
        text = stringResource(id = R.string.another_campaign_type_wording),
        onHyperlinkClicked = {},
        onDismissed = {

        }
    )
}

@Composable
fun CampaignItem(campaign: ActiveCampaign) {
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

            UnifyTypography(
                text = campaign.campaignType,
                modifier = Modifier.constrainAs(campaignType) {
                    top.linkTo(statusImage.top)
                    bottom.linkTo(statusImage.bottom)
                    start.linkTo(statusImage.end, margin = 4.dp)
                },
                type = Typography.DISPLAY_3,
                weight = Typography.BOLD,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_GN500
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

            Image(
                painter = painterResource(id = R.drawable.ic_rocket),
                contentDescription = null,
                modifier = Modifier
                    .size(62.dp)
                    .constrainAs(campaignImage) {
                        start.linkTo(parent.start, margin = 12.dp)
                        top.linkTo(campaignType.bottom, margin = 12.dp)
                    }
            )

            UnifyTypography(
                text = campaign.campaignName,
                modifier = Modifier.constrainAs(campaignName) {
                    top.linkTo(campaignImage.top)
                    start.linkTo(campaignImage.end, margin = 12.dp)
                },
                type = Typography.DISPLAY_2,
                weight = Typography.BOLD,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )

            UnifyTypography(
                campaign.productQty,
                modifier = Modifier.constrainAs(productQty) {
                    top.linkTo(campaignName.bottom, margin = 12.dp)
                    start.linkTo(campaignName.start)
                },
                type = Typography.DISPLAY_3,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )

            UnifyTypography(
                campaign.startDate,
                modifier = Modifier.constrainAs(campaignStartDate) {
                    top.linkTo(productQty.bottom, margin = 12.dp)
                    start.linkTo(productQty.start)
                },
                type = Typography.DISPLAY_3,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )


            UnifyTypography(
                campaign.startTime,
                modifier = Modifier.constrainAs(campaignStartTime) {
                    top.linkTo(campaignStartDate.bottom)
                    start.linkTo(campaignStartDate.start)
                },
                type = Typography.DISPLAY_3,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN600
            )

            Text(
                "-",
                modifier = Modifier.constrainAs(separator) {
                    top.linkTo(campaignStartDate.top)
                    bottom.linkTo(campaignStartTime.bottom)
                    start.linkTo(campaignStartDate.end, margin = 12.dp)
                }
            )

            UnifyTypography(
                campaign.endDate,
                modifier = Modifier.constrainAs(campaignEndDate) {
                    top.linkTo(campaignStartDate.top)
                    bottom.linkTo(campaignStartDate.bottom)
                    start.linkTo(separator.end, margin = 12.dp)
                },
                type = Typography.DISPLAY_3,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )

            UnifyTypography(
                campaign.endTime,
                modifier = Modifier.constrainAs(campaignEndTime) {
                    top.linkTo(campaignStartTime.top)
                    bottom.linkTo(campaignStartTime.bottom)
                    start.linkTo(separator.end, margin = 12.dp)
                },
                type = Typography.DISPLAY_3,
                colorId = com.tokopedia.unifyprinciples.R.color.Unify_NN600
            )

            UnifyButton(
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
                buttonSize = UnifyButton.Size.SMALL,
                buttonType = UnifyButton.Type.MAIN,
                buttonVariant = UnifyButton.Variant.FILLED,
                onClick = {}
            )


        }

    }
}

@Preview
@Composable
fun CampaignItemPreview() {
    val campaign = ActiveCampaign(
        campaignType = "Rilisan Spesial",
        campaignStatus = "Berlangsung",
        campaignName = "Flash Sale 9.9",
        productQty = "9000 Product",
        startDate = "17/01/2020",
        startTime = "08:30 WIB",
        endDate = "18/01/2020",
        endTime = "22:00 WIB"
    )

    CampaignItem(campaign)
}

@Composable
fun CampaignLabel(modifier: Modifier, campaignStatus: String, campaignStatusId: Int) {
    val labelType = when (campaignStatusId) {
        ONGOING_STATUS_ID.toIntOrZero() -> Label.HIGHLIGHT_LIGHT_GREEN
        UPCOMING_STATUS_ID.toIntOrZero(), UPCOMING_IN_NEAR_TIME_STATUS_ID.toIntOrZero() -> Label.HIGHLIGHT_LIGHT_ORANGE
        AVAILABLE_STATUS_ID.toIntOrZero() -> Label.HIGHLIGHT_LIGHT_GREY
        else -> Label.HIGHLIGHT_LIGHT_GREEN
    }
    UnifyLabel(modifier = modifier, labelText = campaignStatus, labelType = labelType)
}