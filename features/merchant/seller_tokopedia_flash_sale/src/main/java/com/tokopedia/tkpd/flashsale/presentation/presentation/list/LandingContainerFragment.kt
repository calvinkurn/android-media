package com.tokopedia.tkpd.flashsale.presentation.presentation.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.entity.Result
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentLandingContainerBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class LandingContainerFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = LandingContainerFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<StfsFragmentLandingContainerBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(LandingContainerViewModel::class.java) }

    override fun getScreenName(): String = LandingContainerFragment::class.java.canonicalName.orEmpty()

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
        binding = StfsFragmentLandingContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
             viewModel.tabsMetadata.collectLatest { result ->
                when(result) {
                    Result.Loading -> {
                        println()
                    }
                    is Result.Success -> {
                        val data = result.data
                        println(data)
                    }
                    is Result.Failure -> {
                        println()
                    }
                }
            }

        }

        viewModel.getTabsMetaData()
        viewModel.submitProduct()


    }

    private fun setupView() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
        }
    }
}