package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.FragmentCatalogComparisonDetailBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CatalogComparisonDetailFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentCatalogComparisonDetailBinding>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

    }

    private fun setupToolbar(){
        binding?.toolbar?.cartButton?.gone()
        binding?.toolbar?.searchButton?.gone()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_comparison_detail, container, false)
    }

    override fun getScreenName() = CatalogComparisonDetailFragment::class.java.canonicalName.orEmpty()


    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    companion object {
        const val CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG = "CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG"
        private const val ARG_PARAM_CATALOG_ID = "catalogId"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(catalogId: String = "", param2: String= "") =
            CatalogComparisonDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_CATALOG_ID, catalogId)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
