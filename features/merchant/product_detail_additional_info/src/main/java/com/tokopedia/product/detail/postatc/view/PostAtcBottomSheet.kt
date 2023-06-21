package com.tokopedia.product.detail.postatc.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.product.detail.databinding.PostAtcBottomSheetBinding
import com.tokopedia.product.detail.postatc.base.ComponentTrackData
import com.tokopedia.product.detail.postatc.base.PostAtcAdapter
import com.tokopedia.product.detail.postatc.base.PostAtcLayoutManager
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.di.DaggerPostAtcComponent
import com.tokopedia.product.detail.postatc.di.PostAtcModule
import com.tokopedia.product.detail.postatc.tracker.CommonTracker
import com.tokopedia.product.detail.postatc.tracker.PostAtcTracking
import com.tokopedia.product.detail.postatc.tracker.RecommendationTracking
import com.tokopedia.product.detail.postatc.view.component.error.ErrorUiModel
import com.tokopedia.product.detail.postatc.view.component.fallback.FallbackUiModel
import com.tokopedia.product.detail.postatc.view.component.loading.LoadingUiModel
import com.tokopedia.product.detail.postatc.view.component.recommendation.RecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.doSuccessOrFail
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PostAtcBottomSheet : BottomSheetUnify(), PostAtcListener {

    companion object {
        const val TAG = "post_atc_bs"

        private const val ARG_PRODUCT_ID = "productId"
        private const val ARG_CART_ID = "cartId"
        private const val ARG_LAYOUT_ID = "layoutId"
        private const val ARG_PAGE_SOURCE = "pageSource"

        fun instance(
            productId: String,
            cartId: String,
            layoutId: String,
            pageSource: String
        ) = PostAtcBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_CART_ID, cartId)
                putString(ARG_LAYOUT_ID, layoutId)
                putString(ARG_PAGE_SOURCE, pageSource)
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val component by lazy {
        DaggerPostAtcComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .postAtcModule(PostAtcModule())
            .build()
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PostAtcViewModel::class.java]
    }

    private val adapter = PostAtcAdapter(this)

    private var binding: PostAtcBottomSheetBinding? = null
    private var productId: String? = null
    private var cartId: String? = null
    private var layoutId: String? = null
    private var pageSource: String? = null
    private var commonTracker: CommonTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        commonTracker = CommonTracker(userSession, viewModel.postAtcInfo)
        productId = arguments?.getString(ARG_PRODUCT_ID) ?: ""
        cartId = arguments?.getString(ARG_CART_ID) ?: ""
        layoutId = arguments?.getString(ARG_LAYOUT_ID) ?: ""
        pageSource = arguments?.getString(ARG_PAGE_SOURCE) ?: ""

        return super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        clearContentPadding = true
        isHideable = true
        showKnob = true
        showHeader = false

        binding = PostAtcBottomSheetBinding.inflate(inflater, container, false).also {
            setupView(it)
            setChild(it.root)
        }
    }

    private fun setupView(binding: PostAtcBottomSheetBinding) = binding.apply {
        postAtcRv.layoutManager = PostAtcLayoutManager()
        postAtcRv.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initData()
    }

    private fun observeViewModel() = with(viewModel) {
        layouts.observe(viewLifecycleOwner, layoutsObserver)
        recommendations.observe(viewLifecycleOwner, recommendationsObserver)
    }

    private fun initData() {
        /**
         * Init Loading
         */
        adapter.replaceComponents(listOf(LoadingUiModel()))

        viewModel.fetchLayout(
            productId.orEmpty(),
            cartId.orEmpty(),
            layoutId.orEmpty(),
            pageSource.orEmpty()
        )
    }

    private val layoutsObserver = Observer<Result<List<PostAtcUiModel>>> { result ->
        result.doSuccessOrFail(success = {
            adapter.replaceComponents(it.data)
        }, fail = {
                showError(it)
            })
        commonTracker?.let {
            PostAtcTracking.impressionPostAtcBottomSheet(trackingQueue, it.get())
        }
    }

    private val recommendationsObserver =
        Observer<Pair<Int, Result<RecommendationWidget>>> { result ->
            val uiModelId = result.first
            result.second.doSuccessOrFail(success = {
                val data = it.data
                adapter.updateComponent<RecommendationUiModel>(uiModelId) {
                    widget = data
                }
            }, fail = {
                    adapter.removeComponent(uiModelId)
                })
        }

    private fun showError(it: Throwable) {
        if (it is SocketTimeoutException || it is UnknownHostException || it is ConnectException) {
            adapter.replaceComponents(listOf(ErrorUiModel(errorType = GlobalError.NO_CONNECTION)))
        } else {
            adapter.replaceComponents(listOf(FallbackUiModel(cartId = cartId.orEmpty())))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stop()
    }

    private fun goToCart(cartId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.CART)
        intent.putExtra("cart_id", cartId)
        startActivity(intent)
        dismissAllowingStateLoss()
    }

    /**
     * Listener Area - Start
     */

    override fun goToAppLink(appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun refreshPage() {
        initData()
    }

    override fun fetchRecommendation(pageName: String, uniqueId: Int) {
        val productId = arguments?.getString(ARG_PRODUCT_ID) ?: return
        viewModel.fetchRecommendation(productId, pageName, uniqueId)
    }

    override fun impressComponent(componentTrackData: ComponentTrackData) {
        /**
         * Currently No OP, will needed in future
         */
    }

    override fun onClickLihatKeranjang(
        cartId: String,
        componentTrackData: ComponentTrackData
    ) {
        commonTracker?.let {
            PostAtcTracking.sendClickLihatKeranjang(
                it.get(),
                componentTrackData
            )
        }

        goToCart(cartId)
    }

    override fun onClickRecommendationItem(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        commonTracker?.let {
            RecommendationTracking.onClickProductCard(it, recommendationItem, trackingQueue)
        }
        onClickProduct(productId)
    }

    override fun onImpressRecommendationItem(recommendationItem: RecommendationItem) {
        commonTracker?.let {
            RecommendationTracking.onImpressionProductCard(it, recommendationItem, trackingQueue)
        }
    }

    /**
     * Listener Area - End
     */

    var onClickProduct: (String) -> Unit = { productId ->
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
        dismiss()
    }
}
