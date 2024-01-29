package com.tokopedia.catalog.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.catalog.R
import com.tokopedia.catalog.analytics.CatalogReimagineDetailAnalytics
import com.tokopedia.catalog.analytics.CatalogTrackerConstant
import com.tokopedia.catalog.databinding.FragmentCatalogComparisonDetailBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.activity.CatalogSwitchingComparisonActivity
import com.tokopedia.catalog.ui.adapter.ComparisonHeaderAdapter
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment.Companion.CATALOG_CAMPARE_SWITCHING_REQUEST_CODE
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oldcatalog.usecase.detail.InvalidCatalogComparisonException
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogComparisonDetailFragment :
    BaseDaggerFragment(),
    ComparisonViewHolder.ComparisonItemListener {

    companion object {
        private const val HEADER_VISIBLE_THRESHOLD = 150
        private const val HEADER_ELEVATION = 5F
        const val CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG = "CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG"
        const val ARG_PARAM_CATALOG_ID = "catalogId"
        const val ARG_PARAM_CATEGORY_ID = "categoryId"
        const val ARG_PARAM_COMPARE_CATALOG_ID = "compareCatalogId"
        const val TOTAL_MINIMUM_SIZE_COMPARE_ID = 2
        fun newInstance(
            catalogId: String,
            categoryId: String,
            compareCatalogId: List<String>
        ): CatalogComparisonDetailFragment {
            val fragment = CatalogComparisonDetailFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_CATALOG_ID, catalogId)
            bundle.putString(ARG_PARAM_CATEGORY_ID, categoryId)
            bundle.putStringArrayList(ARG_PARAM_COMPARE_CATALOG_ID, ArrayList(compareCatalogId))
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: CatalogDetailPageViewModel
    private var binding by autoClearedNullable<FragmentCatalogComparisonDetailBinding>()
    private var catalogId = ""
    private var categoryId = ""
    private var compareCatalogIds = listOf<String>()
    private val widgetAdapter by lazy {
        WidgetCatalogAdapter(
            CatalogAdapterFactoryImpl(isDisplayingTopSpec = false, comparisonItemListener = this)
        )
    }

    private fun changeComparison(comparedCatalogIds: List<String>) {
        this.compareCatalogIds = comparedCatalogIds
        getComparison(catalogId, comparedCatalogIds)
    }

    private fun getComparison(catalogId: String, compareCatalogIds: List<String>) {
        binding?.loadingLayout?.root?.show()
        binding?.gePageError?.gone()
        binding?.rvContent?.gone()
        viewModel.getProductCatalogComparisons(catalogId, compareCatalogIds)
    }

    private fun setupObserver(view: View) {
        viewModel.catalogDetailDataModel.observe(viewLifecycleOwner) {
            if (it is Success) {
                binding?.gePageError?.gone()
                binding?.rvContent?.show()
                val comparison = it.data.widgets.find {
                    it is ComparisonUiModel
                }
                if (comparison != null) {
                    widgetAdapter.addWidget(listOf(comparison))
                    setupContentHeader(comparison as? ComparisonUiModel ?: return@observe)
                }
            } else if (it is Fail) {
                val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                when (it.throwable) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        binding?.gePageError?.setType(GlobalError.NO_CONNECTION)
                    }
                    else -> {
                        binding?.gePageError?.setType(GlobalError.SERVER_ERROR)
                    }
                }
                binding?.gePageError?.errorDescription?.text = errorMessage
                binding?.gePageError?.show()
                binding?.rvContent?.gone()
            }
            binding?.loadingLayout?.root?.gone()
        }
        viewModel.errorsToasterGetComparison.observe(viewLifecycleOwner) {
            val errorMessage = when (it) {
                is UnknownHostException -> {
                    getString(R.string.catalog_error_message_no_connection)
                }
                is InvalidCatalogComparisonException -> {
                    getString(R.string.catalog_error_message_inactive, it.invalidCatalogCount)
                }
                else -> {
                    ErrorHandler.getErrorMessage(requireView().context, it)
                }
            }

            Toaster.build(
                requireView(),
                errorMessage,
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR,
                actionText = if (it is InvalidCatalogComparisonException) {
                    ""
                } else {
                    getString(R.string.catalog_retry_action)
                }
            ) {
                changeComparison(compareCatalogIds)
            }.show()
        }
        viewModel.comparisonUiModel.observe(viewLifecycleOwner) {
            binding?.gePageError?.gone()
            binding?.rvContent?.show()
            binding?.loadingLayout?.root?.gone()
            if (it == null) {
                Toaster.build(
                    view,
                    getString(R.string.catalog_error_message_inactive)
                ).show()
                binding?.rvHeader?.gone()
                binding?.tfHeaderTitle?.gone()
            } else {
                setupContentHeader(it)
                if (widgetAdapter.isVisitableEmpty()) {
                    widgetAdapter.addWidget(listOf(it))
                } else {
                    widgetAdapter.changeComparison(it)
                }
            }
        }
    }

    private fun setupContent() {
        var scrollProgress = 0
        val layoutManager = LinearLayoutManager(context)
        binding?.apply {
            rvContent.layoutManager = layoutManager
            rvContent.adapter = widgetAdapter
            rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    scrollProgress += dy
                    updateHeaderVisibility(scrollProgress)
                }
            })
            gePageError.setActionClickListener {
                viewModel.getProductCatalogComparisons(catalogId, compareCatalogIds)
            }
        }
    }

    private fun setupToolbar() {
        context?.let {
            binding?.toolbar?.apply {
                title = getString(R.string.text_comparison_title)
                searchButton?.hide()
                cartButton?.hide()
                shareButton?.hide()
                editButton?.show()
                setColors(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN950))
                setNavigationOnClickListener {
                    onFragmentBackPressed()
                    activity?.finish()
                }
                editButton?.setOnClickListener {
                    Intent(activity, CatalogSwitchingComparisonActivity::class.java).apply {
                        putExtra(CatalogSwitchingComparisonFragment.ARG_CATALOG_ID, catalogId)
                        putStringArrayListExtra(CatalogSwitchingComparisonFragment.ARG_COMPARISON_CATALOG_ID, ArrayList(compareCatalogIds))
                        putExtra(CatalogSwitchingComparisonFragment.ARG_EXTRA_CATALOG_CATEGORY_ID, categoryId)
                        startActivityForResult(this, CATALOG_CAMPARE_SWITCHING_REQUEST_CODE)
                    }
                }
            }
        }
    }

    private fun FragmentCatalogComparisonDetailBinding.updateHeaderVisibility(scrollProgress: Int) {
        val showHeader = scrollProgress > HEADER_VISIBLE_THRESHOLD
        tfHeaderTitle.isVisible = showHeader
        rvHeader.isVisible = showHeader
        toolbar.elevation = if (showHeader) HEADER_ELEVATION.dpToPx() else Float.ZERO
        divider.elevation = toolbar.elevation
        tfHeaderTitle.elevation = toolbar.elevation
        rvHeader.elevation = toolbar.elevation
    }

    // TODO: Implement this when IOS team is ready
    private fun sendOpenPageTracker() {
        CatalogReimagineDetailAnalytics.sendEventOpenScreen(
            screenName = "${CatalogTrackerConstant.SCREEN_NAME_CATALOG_COMPARISON_PAGE} - $catalogId",
            trackerId = CatalogTrackerConstant.TRACKER_ID_OPEN_PAGE_CATALOG_COMPARISON,
            userId = viewModel.getUserId()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupContent()
        setupObserver(view)
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_PARAM_CATALOG_ID, "")
            categoryId = requireArguments().getString(ARG_PARAM_CATEGORY_ID, "")
            compareCatalogIds = requireArguments().getStringArrayList(ARG_PARAM_COMPARE_CATALOG_ID).orEmpty()
            getComparison(catalogId, compareCatalogIds)
        }
        val comparedId = if (compareCatalogIds.size >= TOTAL_MINIMUM_SIZE_COMPARE_ID) {
            compareCatalogIds.slice(Int.ONE until compareCatalogIds.size).joinToString(",")
        } else {
            String.EMPTY
        }

        val label = "$catalogId | compared catalog id: $comparedId"
        CatalogReimagineDetailAnalytics.sendEvent(
            event = CatalogTrackerConstant.EVENT_VIEW_PG_IRIS,
            action = CatalogTrackerConstant.EVENT_IMPRESSION_COMPARISON_DETAIL,
            category = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_COMPARISON,
            labels = label,
            trackerId = CatalogTrackerConstant.TRACKER_ID_IMPRESSION_COMPARISON_DETAIL
        )
        sendOpenPageTracker()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupContentHeader(comparisonUiModel: ComparisonUiModel) {
        val titleList = comparisonUiModel.content.map { it.productTitle }
        val comparisonAdapter = ComparisonHeaderAdapter(
            titleList.takeLast(comparisonUiModel.content.size.dec())
        )
        binding?.tfHeaderTitle?.text = titleList.firstOrNull().orEmpty()
        binding?.tfHeaderTitle?.visible()
        binding?.rvHeader?.apply {
            visible()
            layoutManager = LinearLayoutManager(context ?: return, LinearLayoutManager.HORIZONTAL, false)
            adapter = comparisonAdapter
            setOnTouchListener { _, _ -> return@setOnTouchListener true } // disable scrolling
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogComparisonDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun getScreenName() =
        CatalogComparisonDetailFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onComparisonSwitchButtonClicked(
        items: List<ComparisonUiModel.ComparisonContent>
    ) {
        // TODO: Ask to DA, should we remove this tracker?
        compareCatalogIds = items.map { it.id }
        val label = "$catalogId | compared catalog id: ${compareCatalogIds.joinToString()}"
        CatalogReimagineDetailAnalytics.sendEvent(
            event = CatalogTrackerConstant.EVENT_VIEW_CLICK_PG,
            action = CatalogTrackerConstant.EVENT_CLICK_CHANGE_COMPARISON_DETAIL,
            category = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_COMPARISON,
            labels = label,
            trackerId = CatalogTrackerConstant.TRACKER_ID_CHANGE_COMPARISON_IN_COMPARISON_DETAIL
        )
    }

    override fun onComparisonSeeMoreButtonClicked(items: List<ComparisonUiModel.ComparisonContent>) {
        // no-op
    }

    override fun onComparisonProductClick(id: String) {
        val catalogProductList =
            Uri.parse(UriUtil.buildUri(ApplinkConst.DISCOVERY_CATALOG))
                .buildUpon()
                .appendPath(id).toString()
        RouteManager.getIntent(context, catalogProductList).apply {
            startActivity(this)
        }
    }

    override fun onComparisonImpression(id: String, widgetName: String) {
        // no-op
    }

    override fun onComparisonScrolled(dx: Int, dy: Int, scrollProgress: Int) {
        binding?.rvHeader?.scrollBy(dx, dy)
        binding?.viewComparisonShadow?.isVisible = scrollProgress > Int.ZERO
    }

    override fun onFragmentBackPressed(): Boolean {
        val intent = Intent().apply {
            putStringArrayListExtra(ARG_PARAM_COMPARE_CATALOG_ID, ArrayList(compareCatalogIds))
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        return super.onFragmentBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CatalogDetailPageFragment.CATALOG_CAMPARE_SWITCHING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newComparedCatalogId = data?.getStringArrayListExtra(CatalogSwitchingComparisonFragment.ARG_COMPARISON_CATALOG_ID)
            if (!newComparedCatalogId.isNullOrEmpty()) changeComparison(newComparedCatalogId)
        }
    }
}
