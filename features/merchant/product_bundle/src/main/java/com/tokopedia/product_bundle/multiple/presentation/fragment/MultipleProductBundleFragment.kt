package com.tokopedia.product_bundle.multiple.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.di.ProductBundleComponentBuilder
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.multiple.di.DaggerMultipleProductBundleComponent
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import javax.inject.Inject

class MultipleProductBundleFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ProductBundleViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_product_bundle, container, false)
    }

    override fun getScreenName(): String {
        return getString(R.string.product_bundle_page_title)
    }

    override fun initInjector() {
        DaggerMultipleProductBundleComponent.builder()
                .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .productBundleModule(ProductBundleModule())
                .build()
                .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MultipleProductBundleFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}