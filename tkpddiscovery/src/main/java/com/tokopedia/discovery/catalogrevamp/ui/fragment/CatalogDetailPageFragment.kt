package com.tokopedia.discovery.catalogrevamp.ui.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.adapter.CatalogImageAdapter
import com.tokopedia.discovery.catalogrevamp.analytics.CatalogDetailPageAnalytics
import com.tokopedia.discovery.catalogrevamp.di.CatalogComponent
import com.tokopedia.discovery.catalogrevamp.di.DaggerCatalogComponent
import com.tokopedia.discovery.catalogrevamp.model.ProductCatalogResponse.ProductCatalogQuery.Data.Catalog
import com.tokopedia.discovery.catalogrevamp.ui.activity.CatalogGalleryActivity
import com.tokopedia.discovery.catalogrevamp.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_catalog_detail_page.*
import javax.inject.Inject

class CatalogDetailPageFragment : Fragment(),
        HasComponent<CatalogComponent>,
        CatalogImageAdapter.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var catalogDetailPageViewModel: CatalogDetailPageViewModel
    private var catalogId: String = ""
    private lateinit var catalogImage: ArrayList<Catalog.CatalogImage>
    private lateinit var fragment: CatalogGalleryFragment
    private lateinit var catalog: Catalog
    private var listener: Listener? = null

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val LEFT = "left"
        private const val RIGHT = "right"

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
            catalogId = arguments!!.getString(ARG_EXTRA_CATALOG_ID, "")
        }
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            catalogDetailPageViewModel = viewModelProvider.get(CatalogDetailPageViewModel::class.java)
            catalogDetailPageViewModel.getProductCatalog(catalogId)
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

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun setUI(catalog: Catalog) {
        val marketPrice = "${catalog.marketPrice[0].minFmt} - ${catalog.marketPrice[0].maxFmt}"
        marketprice.text = marketPrice
        setBanner(catalog.catalogImage)
        setTopThreeSpecs(catalog.topthreespec)
        complete_specifications.setOnClickListener {
            CatalogDetailPageAnalytics.trackEventClickSpecification()
            val catalogSpecsAndDetailView = CatalogSpecsAndDetailBottomSheet.newInstance(catalog.description, catalog.specification)
            catalogSpecsAndDetailView.show(childFragmentManager, "")
        }
        listener?.deliverCatalogShareData(generateCatalogShareData(catalog.url, catalogId), catalog.name, catalog.departmentId)
    }

    private fun setBanner(catalogImage: ArrayList<Catalog.CatalogImage>) {
        val catalogImageAdapter = CatalogImageAdapter(catalogImage, this)
        var previousPosition: Int = -1
        view_pager_intermediary.adapter = catalogImageAdapter
        view_pager_intermediary.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                when {
                    previousPosition >= 0 -> when {
                        previousPosition > position -> CatalogDetailPageAnalytics.trackEventSwipeCatalogPicture(LEFT)
                        previousPosition < position -> CatalogDetailPageAnalytics.trackEventSwipeCatalogPicture(RIGHT)
                    }
                }
                previousPosition = position
            }

        })
        indicator_intermediary.fillColor = MethodChecker.getColor(context, R.color.g_500)
        indicator_intermediary.pageColor = MethodChecker.getColor(context, R.color.tp_ticker_indicator_default_color)
        indicator_intermediary.setViewPager(view_pager_intermediary)
        indicator_intermediary.notifyDataSetChanged()
        this.catalogImage = catalogImage
        if (catalogImage.size == 1)
            indicator_intermediary.hide()
    }

    override fun onImageClick() {
        CatalogDetailPageAnalytics.trackEventClickCatalogPicture()
        showImage(view_pager_intermediary.currentItem)
    }

    private fun showImage(currentItem: Int) {
        context?.startActivity(CatalogGalleryActivity.newIntent(context, currentItem, catalogImage))
    }

    private fun setTopThreeSpecs(topthreespec: ArrayList<Catalog.Topthreespec>) {
        top_three_specs.removeAllViews()
        for (specs in topthreespec) {
            val textView = TextView(context)
            textView.textSize = 12f
            textView.text = MethodChecker.fromHtml(resources.getString(R.string.bullet_string, specs.value)).toString()
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

    private fun generateCatalogShareData(catalogUrl: String, catalogId: String): LinkerData {
        return LinkerData.Builder.getLinkerBuilder()
                .setId(catalogId)
                .setName(activity?.getString(com.tokopedia.core2.R.string.message_share_catalog))
                .setType(LinkerData.CATALOG_TYPE)
                .setTextContent(activity?.getString(com.tokopedia.core2.R.string.share_text_content))
                .setUri(catalogUrl)
                .build()
    }

    interface Listener {
        fun deliverCatalogShareData(shareData: LinkerData, catalogHeading: String, departmentId:String)
    }
}
