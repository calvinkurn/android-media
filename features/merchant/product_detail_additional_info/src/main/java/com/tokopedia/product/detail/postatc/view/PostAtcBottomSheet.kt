package com.tokopedia.product.detail.postatc.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getBooleanArg
import com.tokopedia.kotlin.extensions.view.getIntArg
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getStringArg
import com.tokopedia.kotlin.extensions.view.getStringArrayListArg
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.PostAtcBottomSheetBinding
import com.tokopedia.product.detail.databinding.ViewPostAtcFooterBinding
import com.tokopedia.product.detail.postatc.base.PostAtcAdapter
import com.tokopedia.product.detail.postatc.base.PostAtcBottomSheetDelegate
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcLayoutManager
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.di.DaggerPostAtcComponent
import com.tokopedia.product.detail.postatc.di.PostAtcModule
import com.tokopedia.product.detail.postatc.tracker.PostAtcTracking
import com.tokopedia.product.detail.postatc.view.component.error.ErrorUiModel
import com.tokopedia.product.detail.postatc.view.component.fallback.FallbackUiModel
import com.tokopedia.product.detail.postatc.view.component.loading.LoadingUiModel
import com.tokopedia.product.detail.postatc.view.component.recommendation.RecommendationUiModel
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
import kotlin.math.roundToInt

class PostAtcBottomSheet : BottomSheetUnify(), PostAtcBottomSheetDelegate {

    companion object {
        const val TAG = "post_atc_bs"

        private const val ARG_PRODUCT_ID = "productId"
        private const val ARG_CART_ID = "cartId"
        private const val ARG_IS_FULFILLMENT = "is_fulfillment"
        private const val ARG_LAYOUT_ID = "layoutId"
        private const val ARG_PAGE_SOURCE = "pageSource"
        private const val ARG_SELECTED_ADDONS_IDS = "selected_addons_ids"
        private const val ARG_WAREHOUSE_ID = "warehouse_id"
        private const val ARG_QUANTITY = "quantity"

        fun instance(
            productId: String,
            cartId: String,
            isFulfillment: Boolean,
            layoutId: String,
            pageSource: String,
            selectedAddonsIds: List<String>,
            warehouseId: String,
            quantity: Int
        ) = PostAtcBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_CART_ID, cartId)
                putBoolean(ARG_IS_FULFILLMENT, isFulfillment)
                putString(ARG_LAYOUT_ID, layoutId)
                putString(ARG_PAGE_SOURCE, pageSource)
                putStringArrayList(ARG_SELECTED_ADDONS_IDS, ArrayList(selectedAddonsIds))
                putString(ARG_WAREHOUSE_ID, warehouseId)
                putInt(ARG_QUANTITY, quantity)
            }
        }
    }

    @Inject
    override lateinit var userSession: UserSessionInterface

    @Inject
    override lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val component by getComponent()
    override val viewModel by getViewModel()

    private val callback by lazy { PostAtcCallback(this) }
    override val adapter by lazy { PostAtcAdapter(callback) }

    private val argProductId: String by getStringArg(ARG_PRODUCT_ID)
    private val argCartId: String by getStringArg(ARG_CART_ID)
    private val argIsFulfillment: Boolean by getBooleanArg(ARG_IS_FULFILLMENT)
    private val argLayoutId: String by getStringArg(ARG_LAYOUT_ID)
    private val argPageSource by getStringArg(ARG_PAGE_SOURCE)
    private val argSelectedAddonsIds: List<String> by getStringArrayListArg(ARG_SELECTED_ADDONS_IDS)
    private val argWarehouseId: String by getStringArg(ARG_WAREHOUSE_ID)
    private val argQuantity: Int by getIntArg(ARG_QUANTITY)

    override var binding: PostAtcBottomSheetBinding? = null
        private set
    override var footer: ViewPostAtcFooterBinding? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViewMaxHeight()
        observeViewModel()
        initData()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.stop()
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        clearContentPadding = true

        val title = context?.getString(R.string.pdp_post_atc_title) ?: ""
        setTitle(title)

        binding = PostAtcBottomSheetBinding.inflate(inflater, container, false).also {
            setupView(it)
            setChild(it.root)
        }
    }

    private fun setupView(binding: PostAtcBottomSheetBinding) = binding.apply {
        postAtcRv.layoutManager = PostAtcLayoutManager()
        postAtcRv.adapter = adapter
    }

    /**
     * Please Call this function when bottomSheetHeader finish render
     */
    private fun setRecyclerViewMaxHeight() {
        val view = binding?.postAtcRv ?: return
        val layoutParams = view.layoutParams as? ConstraintLayout.LayoutParams ?: return
        val maxHeight = (getScreenHeight() * 0.7) - bottomSheetHeader.height
        layoutParams.matchConstraintMaxHeight = maxHeight.roundToInt()
        view.layoutParams = layoutParams
    }

    /**
     * Section of Observe ViewModel
     */

    private fun observeViewModel() = with(viewModel) {
        layouts.observe(viewLifecycleOwner, layoutsObserver)
        recommendations.observe(viewLifecycleOwner, recommendationsObserver)
    }

    private val layoutsObserver = Observer<Result<List<PostAtcUiModel>>> { result ->
        result.doSuccessOrFail(success = {
            adapter.replaceComponents(it.data)
            updateFooter()
        }, fail = {
                showError(it)
            })
        PostAtcTracking.impressionPostAtcBottomSheet(
            trackingQueue,
            userSession.userId,
            viewModel.postAtcInfo
        )
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

    /**
     * End of Observe ViewModel
     */

    private fun updateFooter() {
        val data = viewModel.postAtcInfo.footer
        if (!data.shouldShow) return

        val footerView = binding?.postAtcViewStubFooter?.inflate() ?: return
        footer = ViewPostAtcFooterBinding.bind(footerView).apply {
            postAtcFooterProductImage.setImageUrl(data.image)
            postAtcFooterButtonMain.text = data.buttonText
            postAtcFooterButtonMain.setOnClickListener {
                callback.goToCart(data.cartId)
            }
        }

        footerView.addOneTimeGlobalLayoutListener {
            footer?.root?.height?.let {
                binding?.postAtcRv?.updatePadding(bottom = it)
            }
        }
    }

    private fun showError(it: Throwable) {
        if (it is SocketTimeoutException || it is UnknownHostException || it is ConnectException) {
            adapter.replaceComponents(listOf(ErrorUiModel(errorType = GlobalError.NO_CONNECTION)))
        } else {
            adapter.replaceComponents(listOf(FallbackUiModel(cartId = argCartId)))
        }
    }

    override fun initData() {
        /**
         * Init Loading
         */
        adapter.replaceComponents(listOf(LoadingUiModel()))
        viewModel.initializeParameters(
            argProductId,
            argCartId,
            argIsFulfillment,
            argLayoutId,
            argPageSource,
            argSelectedAddonsIds,
            argWarehouseId,
            argQuantity
        )
    }

    private fun getComponent() = lazy {
        DaggerPostAtcComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .postAtcModule(PostAtcModule())
            .build()
    }

    private fun getViewModel() = lazy {
        ViewModelProvider(this, viewModelFactory)[PostAtcViewModel::class.java]
    }
}
