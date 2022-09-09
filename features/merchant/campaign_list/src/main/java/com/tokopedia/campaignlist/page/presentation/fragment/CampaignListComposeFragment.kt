package com.tokopedia.campaignlist.page.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
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

        return ComposeView(requireContext()).apply {
            setContent {

                Surface(modifier = Modifier.fillMaxSize()) {
                    Greeting(name = "Hi")
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
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