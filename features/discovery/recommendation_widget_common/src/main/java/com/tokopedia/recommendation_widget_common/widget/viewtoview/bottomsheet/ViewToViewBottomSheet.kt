package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.BottomSheetViewToViewBinding
import com.tokopedia.recommendation_widget_common.viewutil.doSuccessOrFail
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewItemData
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Result
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ViewToViewBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : BottomSheetUnify(), ViewToViewListener {

    private val viewModel: ViewToViewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get()
    }
    private var binding: BottomSheetViewToViewBinding? = null
    private var recommendationAdapter: ViewToViewAdapter? = null

    private val trackingQueue: TrackingQueue? by lazy {
        activity?.let { TrackingQueue(it) }
    }

    private val queryParams: String
        get() = arguments?.getString(KEY_RECOMMENDATION_PARAMS, "") ?: ""

    private val headerTitle: String
        get() = arguments?.getString(KEY_RECOMMENDATION_NAME) ?: ""

    private val departmentId: String
        get() = arguments?.getString(KEY_RECOMMENDATION_DEPARTMENT_ID) ?: ""

    private val productAnchorId: String
        get() = arguments?.getString(KEY_PRODUCT_ANCHOR_ID) ?: ""

    private val hasAtcButton: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getViewToViewProductRecommendation(
            queryParams,
            hasAtcButton,
        )
        initView()
    }

    private fun initView() {
        initBottomSheetSettings()
        setTitle(headerTitle)

        binding = BottomSheetViewToViewBinding.inflate(
            LayoutInflater.from(requireContext()),
            null,
            false
        )
        setChild(binding?.root)
    }

    private fun getScreenHeightBelowStatusBar(): Int {
        val statusBarHeight = WindowInsetsCompat.CONSUMED.getInsets(
            WindowInsetsCompat.Type.systemBars()
        ).top
        return (getScreenHeight() - statusBarHeight).toDp()
    }

    private fun initBottomSheetSettings() {
        showCloseIcon = true
        isDragable = true
        isHideable = true
        customPeekHeight = getScreenHeightBelowStatusBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListeners()
        observeLiveData()
    }

    private fun initListeners() {
        binding?.geViewToView?.setActionClickListener {
            bottomSheetClose.visible()
            bottomSheetHeader.visible()
            binding?.let {
                it.geViewToView.hide()
                it.rvViewToViewRecommendation.visible()
            }
            viewModel.retryViewToViewProductRecommendation(
                queryParams,
                hasAtcButton,
            )
        }
    }

    private fun observeLiveData() {
        viewModel.viewToViewRecommendationLiveData.observe(viewLifecycleOwner) {
            renderRecommendationResult(it)
        }
        viewModel.viewToViewATCStatusLiveData.observe(viewLifecycleOwner) {
            handleATCStatus(it)
        }
    }

    private fun renderRecommendationResult(result: Result<ViewToViewRecommendationResult>) {
        result.doSuccessOrFail(
            success = {
                when(val data = it.data) {
                    is ViewToViewRecommendationResult.Loading -> {
                        binding?.loadingViewToView?.run {
                            if(!data.hasATCButton) {
                                shimmering1.button.gone()
                                shimmering2.button.gone()
                                shimmering3.button.gone()
                                shimmering4.button.gone()
                            }
                            root.visible()
                        }
                    }
                    is ViewToViewRecommendationResult.Product -> {
                        binding?.loadingViewToView?.root?.gone()
                        recommendationAdapter?.submitList(data.products)
                    }
                }
            },
            fail = { showGlobalError(it) }
        )
    }

    private fun showGlobalError(exception: Throwable?) {
        val binding = binding ?: return
        binding.rvViewToViewRecommendation.hide()
        bottomSheetClose.hide()
        bottomSheetHeader.hide()
        binding.geViewToView.apply {
            setType(getGlobalErrorType(exception))
            errorAction.text = context.resources.getString(R.string.view_to_view_action_retry)
            visible()
        }
    }

    private fun getGlobalErrorType(exception: Throwable?): Int {
        return when (exception) {
            is TimeoutException, is SocketTimeoutException -> GlobalError.NO_CONNECTION
            else -> GlobalError.PAGE_NOT_FOUND
        }
    }

    private fun handleATCStatus(atcStatus: ViewToViewATCStatus) {
        when (atcStatus) {
            is ViewToViewATCStatus.Success -> {
                trackATCSuccess(atcStatus)
                showATCToaster(atcStatus.message)
            }
            is ViewToViewATCStatus.Failure -> {
                showATCToaster(atcStatus.message)
            }
            is ViewToViewATCStatus.NonLogin -> {
                showLogin()
            }
        }
    }

    private fun trackATCSuccess(atcStatus: ViewToViewATCStatus.Success) {
        ViewToViewBottomSheetTracker.eventAddToCart(
            atcStatus.product.recommendationItem,
            headerTitle,
            viewModel.getUserId(),
            productAnchorId,
        )
    }

    private fun showATCToaster(
        message: String,
    ) {
        val view = view?.rootView ?: return
        Toaster.apply { toasterCustomBottomHeight = 40.toPx() }
            .build(
                view,
                message,
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
            )
            .show()
    }

    private fun showLogin() {
        activity?.let {
            startActivityForResult(
                RouteManager.getIntent(it, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
        }
    }

    private fun initRecyclerView() {
        val binding = binding ?: return
        recommendationAdapter = ViewToViewAdapter(this)
        binding.rvViewToViewRecommendation.apply {
            adapter = recommendationAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onProductImpressed(product: ViewToViewDataModel, position: Int) {
        ViewToViewBottomSheetTracker.eventImpressProduct(
            product.recommendationItem,
            headerTitle,
            position,
            viewModel.getUserId(),
            productAnchorId,
            trackingQueue,
        )
    }

    override fun onProductClicked(product: ViewToViewDataModel, position: Int) {
        ViewToViewBottomSheetTracker.eventProductClick(
            product.recommendationItem,
            headerTitle,
            viewModel.getUserId(),
            position,
            productAnchorId,
        )

        RouteManager.route(context, ApplinkConst.PRODUCT_INFO, product.id)
    }

    override fun onAddToCartClicked(product: ViewToViewDataModel, position: Int) {
        viewModel.addToCart(product)
    }

    companion object {
        const val TAG = "ViewToViewBottomSheet"
        private const val KEY_RECOMMENDATION_NAME = "RECOMMENDATION_NAME"
        private const val KEY_RECOMMENDATION_PARAMS = "RECOMMENDATION_PARAMS"
        private const val KEY_RECOMMENDATION_DEPARTMENT_ID = "RECOMMENDATION_DEPARTMENT_ID"
        private const val KEY_PRODUCT_ANCHOR_ID = "PRODUCT_ANCHOR_ID"

        const val REQUEST_CODE_LOGIN = 561

        private fun createBundle(
            data: ViewToViewItemData,
            productAnchorId: String,
        ): Bundle {
            return Bundle().apply {
                putString(KEY_RECOMMENDATION_NAME, data.name)
                putString(KEY_RECOMMENDATION_PARAMS, data.url)
                putString(KEY_RECOMMENDATION_DEPARTMENT_ID, data.departmentId)
                putString(KEY_PRODUCT_ANCHOR_ID, productAnchorId)
            }
        }

        fun show(
            classLoader: ClassLoader,
            fragmentFactory: FragmentFactory,
            fragmentManager: FragmentManager,
            data: ViewToViewItemData,
            productAnchorId: String,
        ): ViewToViewBottomSheet {
            val fragment = fragmentFactory.instantiate(
                classLoader,
                ViewToViewBottomSheet::class.java.name,
            ) as ViewToViewBottomSheet
            fragment.arguments = createBundle(data, productAnchorId)
            fragment.show(fragmentManager, TAG)
            return fragment
        }
    }
}
