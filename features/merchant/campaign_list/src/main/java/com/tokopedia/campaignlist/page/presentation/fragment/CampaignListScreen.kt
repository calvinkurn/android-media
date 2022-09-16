package com.tokopedia.campaignlist.page.presentation.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2Response
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Success

@Composable
fun CampaignListScreen(viewModel: CampaignListViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        val campaignStatus = SortFilterItem(
            stringResource(id = R.string.campaign_list_label_status),
            ChipsUnify.TYPE_NORMAL,
            ChipsUnify.TYPE_NORMAL,
            {}
        )
        val campaignType = SortFilterItem(
            stringResource(R.string.campaign_type),
            ChipsUnify.TYPE_SELECTED,
            ChipsUnify.TYPE_NORMAL,
            {}
        )
        val items = arrayListOf(campaignStatus, campaignType)

        val onDismissed = {

        }
        ComposeSortFilter(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            items = items,
            onDismissed = onDismissed
        )

        List(viewModel = viewModel)
    }
}

@Composable
fun List(viewModel: CampaignListViewModel) {
    val data = viewModel.getCampaignListResult.observeAsState()
    if (data.value is Success) {
        val items =
            (data.value as Success<GetCampaignListV2Response>).data.getCampaignListV2.campaignList
        val formattedCampaigns = viewModel.mapCampaignListDataToActiveCampaignList(items)
        LazyColumn {
            items(formattedCampaigns) {
                CampaignItem(it)
            }
        }
    }

}


@Composable
fun CampaignItem(campaign: ActiveCampaign) {
    Card(
        shape = RoundedCornerShape(8.dp), modifier = Modifier
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

            Text(campaign.campaignType, modifier = Modifier.constrainAs(campaignType) {
                top.linkTo(statusImage.top)
                bottom.linkTo(statusImage.bottom)
                start.linkTo(statusImage.end, margin = 4.dp)
            })

            Text(campaign.campaignStatus, modifier = Modifier.constrainAs(campaignStatus) {
                top.linkTo(campaignType.top)
                bottom.linkTo(campaignType.bottom)
                end.linkTo(parent.end, margin = 16.dp)
            })

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

            Text(
                campaign.campaignName,
                modifier = Modifier.constrainAs(campaignName) {
                    top.linkTo(campaignImage.top)
                    start.linkTo(campaignImage.end, margin = 12.dp)
                }

            )

            Text(
                campaign.productQty,
                modifier = Modifier.constrainAs(productQty) {
                    top.linkTo(campaignName.bottom, margin = 12.dp)
                    start.linkTo(campaignName.start)
                }
            )

            Text(
                campaign.startDate,
                modifier = Modifier.constrainAs(campaignStartDate) {
                    top.linkTo(productQty.bottom, margin = 12.dp)
                    start.linkTo(productQty.start)
                }
            )


            Text(
                campaign.startTime,
                modifier = Modifier.constrainAs(campaignStartTime) {
                    top.linkTo(campaignStartDate.bottom)
                    start.linkTo(campaignStartDate.start)
                }
            )

            Text(
                "-",
                modifier = Modifier.constrainAs(separator) {
                    top.linkTo(campaignStartDate.top)
                    bottom.linkTo(campaignStartTime.bottom)
                    start.linkTo(campaignStartDate.end, margin = 12.dp)
                }
            )

            Text(
                campaign.endDate,
                modifier = Modifier.constrainAs(campaignEndDate) {
                    top.linkTo(campaignStartDate.top)
                    bottom.linkTo(campaignStartDate.bottom)
                    start.linkTo(separator.end, margin = 12.dp)
                }
            )

            Text(
                campaign.endTime,
                modifier = Modifier.constrainAs(campaignEndTime) {
                    top.linkTo(campaignStartTime.top)
                    bottom.linkTo(campaignStartTime.bottom)
                    start.linkTo(separator.end, margin = 12.dp)
                }
            )

            Button(onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .constrainAs(buttonShare) {
                        top.linkTo(campaignStartTime.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                Text(text = stringResource(id = R.string.action_share))
            }


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
fun ComposeSortFilter(modifier: Modifier = Modifier, items : ArrayList<SortFilterItem>, onDismissed: () -> Unit) {
    AndroidView(
        modifier = modifier,
        factory = { context ->

            SortFilter(context).apply {
                addItem(items)
                filterRelationship = SortFilter.RELATIONSHIP_AND
                filterType = SortFilter.TYPE_QUICK
                dismissListener = onDismissed
            }
        },
        update = { view ->

        }
    )
}
