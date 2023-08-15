package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.catalog.databinding.FragmentCatalogReimagineDetailPageBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.uimodel.BaseCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CatalogDetailPageFragment : BaseDaggerFragment() {

    @Inject
    lateinit var catalogDetailPageViewModel: CatalogDetailPageViewModel

    private var binding by autoClearedNullable<FragmentCatalogReimagineDetailPageBinding>()

    private val widgetAdapter by lazy {
        WidgetCatalogAdapter(CatalogAdapterFactoryImpl())
    }

    private val widgets by  lazy {
        arrayListOf<BaseCatalogUiModel>()
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        const val CATALOG_DETAIL_PAGE_FRAGMENT_TAG = "CATALOG_DETAIL_PAGE_FRAGMENT_TAG"
        fun newInstance(catalogId: String): CatalogDetailPageFragment {
            val fragment = CatalogDetailPageFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName() = CatalogDetailPageFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogReimagineDetailPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catalogDetailPageViewModel.getA()
        context?.let {
            binding?.rvContent?.apply {
                layoutManager = LinearLayoutManager(it)
                adapter = widgetAdapter
            }
            widgets.add(TopFeaturesUiModel("dummy", TopFeaturesUiModel.dummyTopFeatures()))
            widgets.add(HeroBannerUiModel("bannercoy", "konten"))
            widgetAdapter.addWidget(widgets)
        }

    }



}
