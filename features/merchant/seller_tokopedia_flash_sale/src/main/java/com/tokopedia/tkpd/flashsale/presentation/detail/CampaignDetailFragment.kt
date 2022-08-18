package com.tokopedia.tkpd.flashsale.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.*
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = CampaignDetailFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }

    //View Binding
    private var binding by autoClearedNullable<StfsFragmentCampaignDetailBinding>()
    private var upcomingCdpHeaderBinding: StfsCdpUpcomingHeaderBinding? = null
    private var upcomingCdpMidBinding: StfsCdpUpcomingMidBinding? = null
    private var upcomingCdpBodyBinding: StfsCdpUpcomingBodyBinding? = null

    override fun getScreenName(): String =
        CampaignDetailFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentCampaignDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUpcoming()
    }

    private fun setupUpcoming() {
        setupUpcomingHeader()
        setupUpcomingMid()
        setupUpcomingBody()
    }

    private fun setupUpcomingHeader() {
        val binding = binding ?: return
        val inflatedView = binding.layoutHeader
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_header
        inflatedView.inflate()
        inflatedView.setOnInflateListener { _, view ->
            upcomingCdpHeaderBinding = StfsCdpUpcomingHeaderBinding.bind(view)
        }

        upcomingCdpHeaderBinding.run {

        }
    }

    private fun setupUpcomingMid() {
        val binding = binding ?: return
        val inflatedView = binding.layoutMid
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_mid
        inflatedView.inflate()
        inflatedView.setOnInflateListener { viewStub, view ->
            upcomingCdpMidBinding = StfsCdpUpcomingMidBinding.bind(view)
        }

        upcomingCdpMidBinding.run {

        }
    }

    private fun setupUpcomingBody() {
        val binding = binding ?: return
        val inflatedView = binding.layoutBody
        inflatedView.layoutResource = R.layout.stfs_cdp_upcoming_body
        inflatedView.inflate()
        inflatedView.setOnInflateListener { viewStub, view ->
            upcomingCdpBodyBinding = StfsCdpUpcomingBodyBinding.bind(view)
        }

        upcomingCdpBodyBinding.run {

        }
    }
}