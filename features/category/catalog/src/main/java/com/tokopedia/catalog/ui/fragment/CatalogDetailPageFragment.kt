package com.tokopedia.catalog.ui.fragment

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.databinding.FragmentCatalogReimagineDetailPageBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.customview.CatalogToolbar
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.PanelImageUiModel
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.util.DrawableExtension
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CatalogDetailPageFragment : BaseDaggerFragment(), HeroBannerListener {

    @Inject
    lateinit var viewModel: CatalogDetailPageViewModel

    private var binding by autoClearedNullable<FragmentCatalogReimagineDetailPageBinding>()

    private val widgetAdapter by lazy {
        WidgetCatalogAdapter(
            CatalogAdapterFactoryImpl(
                heroBannerListener = this
            )
        )
    }

    private val widgets by lazy {
        arrayListOf<Visitable<*>>()
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val COLOR_VALUE_MAX = 255
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

        setupObservers()

        viewModel.getProductCatalog(
            "73177",
            "73177", "213079258", "android")

        view.postDelayed( {
            widgets.add(TrustMakerUiModel.dummyTrustMaker())
            widgets.add(
                PanelImageUiModel(
                    "1", "2", "2",
                    content = listOf(
                        PanelImageUiModel.PanelImageItemData(
                            imageUrl = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png",
                            highlight = "",
                            title = "asd",
                            description = "ad"
                        ),
                        PanelImageUiModel.PanelImageItemData(
                            imageUrl = "https://placekitten.com/200/300", highlight = "",
                            title = "asd22", description = "ad22"
                        ),
                    )
                )
            )
            widgets.add(
                PanelImageUiModel(
                    "1", "2", "2",
                    content = listOf(
                        PanelImageUiModel.PanelImageItemData(
                            imageUrl = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png",
                            highlight = "",
                            title = "asd",
                            description = "ad"
                        ),
                        PanelImageUiModel.PanelImageItemData(
                            imageUrl = "https://placekitten.com/200/300", highlight = "",
                            title = "asd22", description = "ad22"
                        ),
                    )
                )
            )
            widgets.add(
                PanelImageUiModel(
                    "1", "2", "2",
                    content = listOf(
                        PanelImageUiModel.PanelImageItemData(
                            imageUrl = "https://images.tokopedia.net/android/shop_page/image_product_empty_state_buyer.png",
                            highlight = "",
                            title = "asd",
                            description = "ad"
                        ),
                        PanelImageUiModel.PanelImageItemData(
                            imageUrl = "https://placekitten.com/200/300", highlight = "",
                            title = "asd22", description = "ad22"
                        ),
                    )
                )
            )
            widgets.add(SliderImageTextUiModel.dummySliderImageText())
            widgets.add(AccordionInformationUiModel.dummyAccordion())

            widgetAdapter.addMoreData(widgets)
        }, 5000)

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

    private fun setupObservers() {
        viewModel.catalogDetailDataModel.observe(viewLifecycleOwner) {
            if (it is Success) {
                widgetAdapter.addMoreData(it.data.widgets)
                binding?.setupToolbar(it.data.navigationProperties)
                binding?.setupRvWidgets(it.data.navigationProperties)
                setPriceCtaWidgetTheme(it.data.priceCtaProperties.textColor, it.data.priceCtaProperties.bgColor)
            }
        }
    }

    private fun FragmentCatalogReimagineDetailPageBinding.setupRvWidgets(
        navigationProperties: NavigationProperties
    ) {
        val layoutManager = LinearLayoutManager(context)
        rvContent.layoutManager = layoutManager
        rvContent.adapter = widgetAdapter
        rvContent.setBackgroundColor(navigationProperties.bgColor)
        rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val bannerHeight = layoutManager.findViewByPosition(Int.ZERO)?.height.orZero()
                val bannerRect = Rect()
                layoutManager.findViewByPosition(Int.ZERO)?.getGlobalVisibleRect(bannerRect)
                val scrollProgress = Int.ONE - if (bannerRect.height()
                        .isMoreThanZero() && bannerHeight.isMoreThanZero()
                ) {
                    bannerRect.height() / bannerHeight.toFloat()
                } else {
                    Int.ZERO.toFloat()
                }
                toolbar.updateToolbarAppearance(scrollProgress, navigationProperties)
            }
        })
        widgetAdapter.refreshSticky()
    }

    private fun FragmentCatalogReimagineDetailPageBinding.setupToolbar(
        navigationProperties: NavigationProperties
    ) {
        val colorBgGradient = MethodChecker.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_44
        )
        val colorFontDark = MethodChecker.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        )
        val colorFontLight = MethodChecker.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        )
        val colorFont = if (navigationProperties.isDarkMode) colorFontDark else colorFontLight

        toolbarShadow.background =
            DrawableExtension.createGradientDrawable(colorTop = colorBgGradient)
        toolbar.setColors(colorFont)
        toolbarShadow.isVisible = !navigationProperties.isPremium
        toolbarBg.setBackgroundColor(navigationProperties.bgColor)
        toolbar.title = navigationProperties.title
        toolbar.setNavigationOnClickListener {
            onNavBackClicked()
        }
    }

    private fun CatalogToolbar.updateToolbarAppearance(
        scrollProgress: Float,
        navigationProperties: NavigationProperties
    ) {
        if (!navigationProperties.isDarkMode) {
            val colorProgress: Int = COLOR_VALUE_MAX - (COLOR_VALUE_MAX * scrollProgress).toInt()
            setColors(Color.rgb(colorProgress, colorProgress, colorProgress))
        }

        if (navigationProperties.isPremium) {
            alpha = scrollProgress
        }
        binding?.toolbarBg?.alpha = scrollProgress
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
