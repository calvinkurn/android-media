package com.tokopedia.deals.pdp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.databinding.FragmentDealsDetailBinding
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class DealsPDPFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var productId : String = ""
    private var binding by autoClearedNullable<FragmentDealsDetailBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DealsPDPViewModel::class.java)
    }

    private var progressBar: LoaderUnify? = null
    private var progressBarLayout: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = arguments?.getString(EXTRA_PRODUCT_ID, "") ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeFlowData()
        callPDP()
    }

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    private fun setupUi() {
        view?.apply {
            progressBar = binding?.progressBar
            progressBarLayout = binding?.progressBarLayout
        }
    }

    private fun observeFlowData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowPDP.collect {
                when (it) {
                    is Success -> {
                        hideLoading()
                    }

                    is Fail -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun callPDP() {
        showLoading()
        viewModel.setPDP(productId)
    }

    private fun showLoading() {
        progressBar?.show()
        progressBarLayout?.show()
    }

    private fun hideLoading() {
        progressBar?.hide()
        progressBarLayout?.hide()
    }

    companion object {

        fun createInstance(productId: String?): DealsPDPFragment {
            val fragment = DealsPDPFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}