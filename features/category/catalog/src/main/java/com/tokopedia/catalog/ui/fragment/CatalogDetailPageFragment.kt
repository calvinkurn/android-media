package com.tokopedia.catalog.ui.fragment

import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.catalog.databinding.FragmentCatalogReimagineDetailPageBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.model.PriceCtaProperties
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.customview.CatalogToolbar
import com.tokopedia.catalogcommon.listener.HeroBannerListener
import com.tokopedia.catalogcommon.util.DrawableExtension
import com.tokopedia.catalogcommon.viewholder.StickyNavigationListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogDetailPageFragment : BaseDaggerFragment(), HeroBannerListener,
    StickyNavigationListener {

    companion object {
        private const val QUERY_CATALOG_ID = "catalog_id"
        private const val QUERY_PRODUCT_SORTING_STATUS = "product_sorting_status"

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

    @Inject
    lateinit var viewModel: CatalogDetailPageViewModel
    var title = ""
    var productSortingStatus = 0

    private var binding by autoClearedNullable<FragmentCatalogReimagineDetailPageBinding>()
    private val widgetAdapter by lazy {
        WidgetCatalogAdapter(
            CatalogAdapterFactoryImpl(
                heroBannerListener = this,
                navListener = this
            )
        )
    }
    private val recyclerViewScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager

                val indexVisible = layoutManager?.findFirstCompletelyVisibleItemPosition().orZero()
                binding?.rvContent?.post {
                    widgetAdapter.autoSelectNavigation(indexVisible)
                }
            }
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
        setupObservers(view)
        var catalogId = ""
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
            viewModel.getProductCatalog(catalogId, "")
            viewModel.refreshNotification()
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

    override fun onResume() {
        super.onResume()
        viewModel.refreshNotification()
    }

    private fun setupObservers(view: View) {
        viewModel.catalogDetailDataModel.observe(viewLifecycleOwner) {
            if (it is Success) {
                productSortingStatus = productSortingStatus
                widgetAdapter.addWidget(it.data.widgets)
                title = it.data.navigationProperties.title
                binding?.setupToolbar(it.data.navigationProperties)
                binding?.setupRvWidgets(it.data.navigationProperties)
                binding?.setupPriceCtaWidget(it.data.priceCtaProperties)
            } else if (it is Fail){
                binding?.showPageError(it.throwable)
            }
        }
        viewModel.totalCartItem.observe(viewLifecycleOwner) {
            binding?.toolbar?.cartCount = it
        }
        viewModel.errorsToaster.observe(viewLifecycleOwner) {
            val errorMessage = ErrorHandler.getErrorMessage(view.context, it)
            Toaster.build(view, errorMessage, duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR).show()
        }
    }

    private fun FragmentCatalogReimagineDetailPageBinding.setupRvWidgets(
        navigationProperties: NavigationProperties
    ) {
        val layoutManager = LinearLayoutManager(context)
        rvContent.layoutManager = layoutManager
        rvContent.adapter = widgetAdapter
        rvContent.addOnScrollListener(recyclerViewScrollListener)
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
    private fun FragmentCatalogReimagineDetailPageBinding.setupPriceCtaWidget(properties: PriceCtaProperties) {
        containerPriceCta.setBackgroundColor(properties.bgColor)
        tgpCatalogName.setTextColor(properties.textColor)
        tgpPriceRanges.setTextColor(properties.textColor)

        tgpCatalogName.text = properties.productName
        tgpPriceRanges.text = properties.price

        btnProductList.setOnClickListener {
            val catalogProductList =
                Uri.parse(UriUtil.buildUri(ApplinkConst.DISCOVERY_CATALOG_PRODUCT_LIST))
                    .buildUpon()
                    .appendQueryParameter(QUERY_CATALOG_ID, properties.catalogId)
                    .appendQueryParameter(QUERY_PRODUCT_SORTING_STATUS, productSortingStatus.toString())
                    .appendPath(properties.productName).toString()

            RouteManager.route(context, catalogProductList)
        }
    }

    private fun FragmentCatalogReimagineDetailPageBinding.showPageError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                gePageError.setType(GlobalError.NO_CONNECTION)
            }
            else -> {
                gePageError.setType(GlobalError.SERVER_ERROR)
            }
        }
        gePageError.errorDescription.text = errorMessage
        groupContent.gone()
        stickySingleHeaderView.show()
    }

    override fun onNavigateWidget(anchorTo: String, tabPosition: Int) {
        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        val anchorToPosition = widgetAdapter.findPositionWidget(anchorTo) - 1

        val layoutManager = binding?.rvContent?.layoutManager as? LinearLayoutManager
        if (anchorToPosition >= Int.ZERO){
            smoothScroller.targetPosition = anchorToPosition
            layoutManager?.startSmoothScroll(smoothScroller)
            view?.postDelayed({

                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition().orZero()
                val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPosition().orZero()

                if (anchorToPosition !in firstVisibleItemPosition..lastVisibleItemPosition) {
                    layoutManager?.scrollToPositionWithOffset(anchorToPosition, 0)

                }
            }, 500)
        }
    }
}
