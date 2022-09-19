package com.tokopedia.campaignlist.page.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.common.analytics.CampaignListTracker
import com.tokopedia.campaignlist.common.di.DaggerCampaignListComponent
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignStatusBottomSheet
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignTypeBottomSheet
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
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

        viewModel.getSellerMetaData()
        viewModel.getCampaignList()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CampaignListScreen(
                    viewModel = viewModel,
                    onCampaignStatusTap = { campaignStatuses -> showCampaignStatusBottomSheet(campaignStatuses) },
                    onCampaignTypeTap = { campaignType -> showCampaignTypeBottomSheet(campaignType) }
                )
            }
        }
    }


    private fun initViewTreeOwners() {
        // Set the view tree owners before setting the content view so that the inflation process
        // and attach listeners will see them already present
        val decoderView = requireActivity().window.decorView
        ViewTreeLifecycleOwner.set(decoderView, this)
        ViewTreeViewModelStoreOwner.set(decoderView, this)
        ViewTreeSavedStateRegistryOwner.set(decoderView, this)
    }

    private fun showCampaignStatusBottomSheet(campaignStatusSelections: List<CampaignStatusSelection>) {
        val bottomSheet = CampaignStatusBottomSheet.createInstance(campaignStatusSelections, object : CampaignStatusBottomSheet.OnApplyButtonClickListener {
            override fun onApplyCampaignStatusFilter(selectedCampaignStatus: CampaignStatusSelection) {

            }

            override fun onNoCampaignStatusSelected() {

            }

        })
        bottomSheet.show(childFragmentManager)
    }

    private fun showCampaignTypeBottomSheet(campaignTypeSelection: List<CampaignTypeSelection>) {
        val bottomSheet = CampaignTypeBottomSheet.createInstance(campaignTypeSelection, object : CampaignTypeBottomSheet.OnApplyButtonClickListener {
            override fun onApplyCampaignTypeFilter(selectedCampaignType: CampaignTypeSelection) {

            }
        })
        bottomSheet.show(childFragmentManager)
    }
}