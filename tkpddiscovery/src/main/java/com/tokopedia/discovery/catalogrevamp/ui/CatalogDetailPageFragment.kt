package com.tokopedia.discovery.catalogrevamp.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.adapter.CatalogImageAdapter
import com.tokopedia.discovery.catalogrevamp.di.CatalogComponent
import com.tokopedia.discovery.catalogrevamp.di.DaggerCatalogComponent
import com.tokopedia.discovery.catalogrevamp.model.ProductCatalogResponse.ProductCatalogQuery.Data.Catalog
import com.tokopedia.discovery.catalogrevamp.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_catalog_detail_page.*
import javax.inject.Inject

class CatalogDetailPageFragment : Fragment(),
        HasComponent<CatalogComponent>,
        CatalogImageAdapter.Listener,
        CatalogGalleryFragment.Listener
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var catalogDetailPageViewModel: CatalogDetailPageViewModel
    private var categoryId: String = ""
    private lateinit var catalogImage: ArrayList<Catalog.CatalogImage>
    private lateinit var fragment: CatalogGalleryFragment
    private lateinit var catalog: Catalog

    companion object {
        private const val TAG_FRAGMENT = "TAG_FRAGMENT"
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"

        fun newInstance(catalogId: String): CatalogDetailPageFragment {
            val fragment = CatalogDetailPageFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getComponent(): CatalogComponent {
        return DaggerCatalogComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog_detail_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        if (arguments != null) {
            categoryId = arguments!!.getString(ARG_EXTRA_CATALOG_ID, "")
        }
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            catalogDetailPageViewModel = viewModelProvider.get(CatalogDetailPageViewModel::class.java)
            catalogDetailPageViewModel.getProductCatalog(categoryId)
        }
        setObservers()
    }

    private fun setObservers() {
        catalogDetailPageViewModel.getProductCatalogResponse().observe(this, Observer {
            when (it) {
                is Success -> {
                    catalog_layout.show()
                    catalog = it.data.productCatalogQuery.data.catalog
                    setUI(catalog)
                }
                is Fail -> {
                }
            }

        })
    }

    private fun setUI(catalog: Catalog) {
        catalog_toolbar.title = catalog.name
        val marketPrice = "${catalog.marketPrice[0].minFmt} - ${catalog.marketPrice[0].maxFmt}"
        marketprice.text = marketPrice
        setBanner(catalog.catalogImage)
        setTopThreeSpecs(catalog.topthreespec)
        complete_specifications.setOnClickListener {
            val catalogSpecsAndDetailView = CatalogSpecsAndDetailBottomSheet.newInstance(catalog.description, catalog.specification)
            catalogSpecsAndDetailView.show(childFragmentManager, "")
        }
    }

    private fun setBanner(catalogImage: ArrayList<Catalog.CatalogImage>) {
        val catalogImageAdapter = CatalogImageAdapter(catalogImage, this)
        view_pager_intermediary.adapter = catalogImageAdapter
        indicator_intermediary.fillColor = MethodChecker.getColor(context, R.color.g_500)
        indicator_intermediary.pageColor = MethodChecker.getColor(context, R.color.tp_ticker_indicator_default_color)
        indicator_intermediary.setViewPager(view_pager_intermediary)
        indicator_intermediary.notifyDataSetChanged()
        this.catalogImage = catalogImage
        if (catalogImage.size == 1)
            indicator_intermediary.hide()
    }

    override fun onImageClick() {
        showImage(view_pager_intermediary.currentItem)
    }

    override fun onCrossClick() {
        onBackPress()
    }

    private fun showImage(currentItem: Int) {
        fragment = CatalogGalleryFragment.newInstance(currentItem, catalogImage)
        fragment.setListener(this)
        childFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                .replace(R.id.frame_layout, fragment, TAG_FRAGMENT)
                .addToBackStack(TAG_FRAGMENT)
                .commit()
    }

    private fun setTopThreeSpecs(topthreespec: ArrayList<Catalog.Topthreespec>) {
        top_three_specs.removeAllViews()
        for (specs in topthreespec) {
            val textView = TextView(context)
            textView.textSize = 12f
            textView.text = resources.getString(R.string.bullet_string, specs.value)
            textView.setTextColor(MethodChecker.getColor(context, R.color.grey_796))
            top_three_specs.addView(textView)
        }
    }

    fun onBackPress() {
        if (::fragment.isInitialized && fragment.isAdded) {
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.exit_bottom, R.animator.exit_bottom)
                    .remove(fragment)
                    .commit()
        } else {
            activity?.finish()
        }
    }

}
