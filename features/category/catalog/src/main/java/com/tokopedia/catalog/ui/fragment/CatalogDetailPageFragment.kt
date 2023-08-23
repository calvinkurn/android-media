package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.catalog.databinding.FragmentCatalogReimagineDetailPageBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.PanelImageUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CatalogDetailPageFragment : BaseDaggerFragment(), HeroBannerListener {

    @Inject
    lateinit var catalogDetailPageViewModel: CatalogDetailPageViewModel

    private var binding by autoClearedNullable<FragmentCatalogReimagineDetailPageBinding>()

    private val widgetAdapter by lazy {
        WidgetCatalogAdapter(
            CatalogAdapterFactoryImpl(
                heroBannerListener = this
            )
        )
    }

    private val widgets by  lazy {
        arrayListOf<Visitable<*>>()
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
            widgets.add(
                HeroBannerUiModel(
                    "bannercoy",
                    "",
                    "",
                    widgetBackgroundColor = null,
                    widgetTextColor = null,
                    brandTitle = "Samsung",
                    brandDesc = "360 RTrfvds Dewsign",
                    brandIconUrl = "https://www.samsung.com/etc.clientlibs/samsung/clientlibs/consumer/global/clientlib-common/resources/images/gnb-desktop-120x32.png",
                    //brandIconUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/UNIQLO_logo.svg/1200px-UNIQLO_logo.svg.png",
                    brandImageUrls = listOf(
                        "https://placekitten.com/200/300",
                        "https://placekitten.com/201/301",
                        "https://placekitten.com/205/302"
                    ),
                    isPremium = true
                )
            )
            widgets.add(
                DummyUiModel(
                    "dummybann",
                    "",
                    "",
                    widgetBackgroundColor = null,
                    widgetTextColor = null,
                    "konten"
                )
            )
            widgets.add(TrustMakerUiModel.dummyTrustMaker())
            widgets.add(TopFeaturesUiModel.dummyTopFeatures())
            widgets.add(
                PanelImageUiModel("1", "2", "2",
                    content = listOf(
                        PanelImageUiModel.PanelImageItemData(imageUrl = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png", highlight = "",
                            title = "asd", description = "ad"),
                        PanelImageUiModel.PanelImageItemData(imageUrl = "https://placekitten.com/200/300", highlight = "",
                            title = "asd22", description = "ad22"),
                    )
                )
            )
            widgetAdapter.addWidget(widgets)
        }

    }

    override fun onNavBackClicked() {
        activity?.finish()
    }

    override fun onNavShareClicked() {
        // no-op
    }

    override fun onNavMoreMenuClicked() {
        // no-op
    }

    // Call this methods if you want to override the CTA & Price widget's theme
    private fun setPriceCtaWidgetTheme(fontColor: Int, bgColor: Int) {
        binding?.let {
            it.containerPriceCta.setBackgroundColor(bgColor)
            it.tgpCatalogName.setTextColor(fontColor)
            it.tgpPriceRanges.setTextColor(fontColor)
        }
    }
}
