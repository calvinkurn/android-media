package com.tokopedia.campaignlist.page.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.common.analytics.CampaignListTracker
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2Response
import com.tokopedia.campaignlist.common.di.DaggerCampaignListComponent
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CampaignListComposeFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: CampaignListTracker

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CampaignListViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun createInstance() = CampaignListComposeFragment()
    }

    override fun getScreenName(): String {
        return getString(R.string.active_campaign_list)
    }

    override fun initInjector() {
        DaggerCampaignListComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        initViewTreeOwners()

        viewModel.getCampaignList()
        return ComposeView(requireContext()).apply {
            setContent {
                CampaignListScreen()
            }
        }
    }

    @Composable
    fun CampaignListScreen() {
        Surface(modifier = Modifier.fillMaxSize()) {
            List(viewModel = viewModel)
        }
    }

    @Composable
    fun List(viewModel: CampaignListViewModel) {
        val data = viewModel.getCampaignListResult.observeAsState()
        if (data.value is Success) {
            val items = (data.value as Success<GetCampaignListV2Response>).data.getCampaignListV2.campaignList
            val formattedCampaigns = viewModel.mapCampaignListDataToActiveCampaignList(items)
            LazyColumn{
                items(formattedCampaigns) {
                    CampaignItem(it)
                }
            }
        }

    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Composable
    fun CampaignItem(campaign: ActiveCampaign) {
        Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().padding(12.dp)) {
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
                    modifier = Modifier.fillMaxWidth().padding(12.dp).constrainAs(buttonShare) {
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

    private fun initViewTreeOwners() {
        // Set the view tree owners before setting the content view so that the inflation process
        // and attach listeners will see them already present
        val decoderView = requireActivity().window.decorView
        ViewTreeLifecycleOwner.set(decoderView, this)
        ViewTreeViewModelStoreOwner.set(decoderView, this)
        ViewTreeSavedStateRegistryOwner.set(decoderView, this)
    }
}