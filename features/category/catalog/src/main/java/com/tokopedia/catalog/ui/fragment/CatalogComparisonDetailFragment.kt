package com.tokopedia.catalog.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.FragmentCatalogComparisonDetailBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.WidgetCatalogAdapter
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.viewholder.ComparisonViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.ui.bottomsheet.CatalogComponentBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogComparisonDetailFragment :
    BaseDaggerFragment(),
    ComparisonViewHolder.ComparisonItemListener,
    CatalogDetailListener {

    companion object {
        const val CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG = "CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG"
        const val ARG_PARAM_CATALOG_ID = "catalogId"
        const val ARG_PARAM_CATEGORY_ID = "categoryId"
        const val ARG_PARAM_COMPARE_CATALOG_ID = "compareCatalogId"
        private const val COMPARISON_CHANGED_POSITION = 1

        fun newInstance(
            catalogId: String,
            categoryId: String,
            compareCatalogId: String
        ): CatalogComparisonDetailFragment {
            val fragment = CatalogComparisonDetailFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_CATALOG_ID, catalogId)
            bundle.putString(ARG_PARAM_CATEGORY_ID, categoryId)
            bundle.putString(ARG_PARAM_COMPARE_CATALOG_ID, compareCatalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: CatalogDetailPageViewModel

    private var binding by autoClearedNullable<FragmentCatalogComparisonDetailBinding>()

    var catalogId = ""
    var categoryId = ""
    var compareCatalogId = ""

    private var catalogAllReviewBottomSheet: CatalogComponentBottomSheet? = null

    private val widgetAdapter by lazy {
        WidgetCatalogAdapter(
            CatalogAdapterFactoryImpl(isDisplayingTopSpec = false, comparisonItemListener = this)
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
            compareCatalogId = requireArguments().getString(ARG_PARAM_COMPARE_CATALOG_ID, "")
            getComparison(catalogId, compareCatalogId)
            catalogAllReviewBottomSheet = CatalogComponentBottomSheet.newInstance(
                "",
                catalogId,
                "",
                categoryId,
                "",
                CatalogComponentBottomSheet.ORIGIN_ULTIMATE_VERSION,
                this
            )
        }
    }

    private fun getComparison(catalogId: String, compareCatalogId: String) {
        binding?.loadingLayout?.root?.show()
        binding?.gePageError?.gone()
        binding?.rvContent?.gone()
        viewModel.getProductCatalog(catalogId, "")
        if (compareCatalogId.isNotEmpty()) {
            viewModel.getProductCatalogComparisons(catalogId, compareCatalogId)
        }
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
                }
            } else if (it is Fail){
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
            val errorMessage = if (it is UnknownHostException) {
                getString(R.string.catalog_error_message_no_connection)
            } else ErrorHandler.getErrorMessage(requireView().context, it)

            Toaster.build(
                requireView(), errorMessage, duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR,
                actionText = getString(R.string.catalog_retry_action)
            ) {
                changeComparison(compareCatalogId)
            }.show()
        }
        viewModel.comparisonUiModel.observe(viewLifecycleOwner) {
            // COMPARISON_CHANGED_POSITION is hardcoded position, will changed at next phase
            if (it == null)
                Toaster.build(
                    view,
                    getString(R.string.catalog_error_message_inactive)
                ).show()
            else
                widgetAdapter.changeComparison(COMPARISON_CHANGED_POSITION, it)
        }
    }

    private fun setupContent() {
        val layoutManager = LinearLayoutManager(context)
        binding?.rvContent?.layoutManager = layoutManager
        binding?.rvContent?.adapter = widgetAdapter
        binding?.gePageError?.setActionClickListener {
            viewModel.getProductCatalogComparisons(catalogId, compareCatalogId)
        }
    }

    private fun setupToolbar() {
        context?.let {
            binding?.toolbar?.apply {
                title = getString(R.string.text_comparison_title)
                searchButton?.hide()
                cartButton?.hide()
                shareButton?.hide()
                setColors(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN950))
                setNavigationOnClickListener {
                    activity?.finish()
                }
            }
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

    override fun onComparisonSwitchButtonClicked(position: Int) {
        catalogAllReviewBottomSheet?.show(childFragmentManager, "")
    }

    override fun onComparisonSeeMoreButtonClicked() {
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

    override fun changeComparison(comparedCatalogId: String) {
        this.compareCatalogId = comparedCatalogId
        getComparison(catalogId, comparedCatalogId)
    }
}
