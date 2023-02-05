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
import com.tokopedia.product.detail.postatc.base.CommonTracker
import com.tokopedia.product.detail.postatc.base.ComponentTrackData
import com.tokopedia.product.detail.postatc.base.PostAtcAdapter
import com.tokopedia.product.detail.postatc.base.PostAtcLayoutManager
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcTracking
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.component.error.ErrorUiModel
import com.tokopedia.product.detail.postatc.component.loading.LoadingUiModel
import com.tokopedia.product.detail.postatc.di.DaggerPostAtcComponent
import com.tokopedia.product.detail.postatc.di.PostAtcModule
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
            pageSource: String,
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
        ViewModelProvider(this, viewModelFactory).get(PostAtcViewModel::class.java)
    }

    private val adapter = PostAtcAdapter(this)

    private lateinit var productId: String
    private lateinit var cartId: String
    private lateinit var layoutId: String
    private lateinit var pageSource: String
    private lateinit var commonTracker: CommonTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        commonTracker = CommonTracker(userSession, viewModel.postAtcInfo)
        productId = arguments?.getString(ARG_PRODUCT_ID) ?: ""
        cartId = arguments?.getString(ARG_CART_ID) ?: ""
        layoutId = arguments?.getString(ARG_LAYOUT_ID) ?: ""
        pageSource = arguments?.getString(ARG_PAGE_SOURCE) ?: ""

        return super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        clearContentPadding = true

        val binding = PostAtcBottomSheetBinding.inflate(inflater, container, false)
        setupView(binding)

        setChild(binding.root)
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
        adapter.addItem(LoadingUiModel())
        adapter.notifyDataSetChanged()

        viewModel.fetchLayout(productId, cartId, layoutId, pageSource)
    }

    private val layoutsObserver = Observer<Result<List<PostAtcUiModel>>> { result ->
        result.doSuccessOrFail(success = {
            adapter.clearAllItems()
            adapter.addItems(it.data)
            adapter.notifyDataSetChanged()
        }, fail = {
            showError(it)
        })
    }

    private val recommendationsObserver = Observer<List<RecommendationWidget>> {
        adapter.updateRecommendation(it)
    }

    private fun showError(it: Throwable) {
        val errorType = if (it is SocketTimeoutException || it is UnknownHostException || it is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }

        adapter.clearAllItems()
        adapter.addItem(ErrorUiModel(errorType = errorType))
        adapter.notifyDataSetChanged()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
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

    override fun goToAppLink(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun goToProduct(productId: String) {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productId
        )
        dismiss()
    }

    override fun refreshPage() {
        adapter.clearAllItems()
        initData()
    }

    override fun fetchRecommendation(pageName: String) {
        val productId = arguments?.getString(ARG_PRODUCT_ID) ?: return
        viewModel.fetchRecommendation(productId, pageName)
    }

    override fun impressComponent(componentTrackData: ComponentTrackData) {
        PostAtcTracking.impressComponent(
            trackingQueue,
            commonTracker.get(),
            componentTrackData,
        )
    }

    override fun onClickLihatKeranjang(
        cartId: String,
        componentTrackData: ComponentTrackData
    ) {
        PostAtcTracking.sendClickLihatKeranjang(
            trackingQueue,
            commonTracker.get(),
            componentTrackData
        )

        goToCart(cartId)
    }

    /**
     * Listener Area - End
     */


}
