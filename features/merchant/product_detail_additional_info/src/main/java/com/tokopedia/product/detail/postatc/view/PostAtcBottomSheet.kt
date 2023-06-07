package com.tokopedia.product.detail.postatc.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.getBooleanArg
import com.tokopedia.kotlin.extensions.view.getStringArg
import com.tokopedia.kotlin.extensions.view.getStringArrayListArg
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.PostAtcBottomSheetBinding
import com.tokopedia.product.detail.postatc.base.PostAtcAdapter
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

class PostAtcBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "post_atc_bs"

        private const val ARG_PRODUCT_ID = "productId"
        private const val ARG_CART_ID = "cartId"
        private const val ARG_IS_FULFILLMENT = "is_fulfillment"
        private const val ARG_LAYOUT_ID = "layoutId"
        private const val ARG_PAGE_SOURCE = "pageSource"
        private const val ARG_SELECTED_ADDONS_IDS = "selected_addons_ids"
        private const val ARG_WAREHOUSE_ID = "warehouse_id"

        fun instance(
            productId: String,
            cartId: String,
            isFulfillment: Boolean,
            layoutId: String,
            pageSource: String,
            selectedAddonsIds: List<String>,
            warehouseId: String,
        ) = PostAtcBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_CART_ID, cartId)
                putBoolean(ARG_IS_FULFILLMENT, isFulfillment)
                putString(ARG_LAYOUT_ID, layoutId)
                putString(ARG_PAGE_SOURCE, pageSource)
                putStringArrayList(ARG_SELECTED_ADDONS_IDS, ArrayList(selectedAddonsIds))
                putString(ARG_WAREHOUSE_ID, warehouseId)
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

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PostAtcViewModel::class.java]
    }

    internal var binding: PostAtcBottomSheetBinding? = null
        private set
    private val argProductId: String by getStringArg(ARG_PRODUCT_ID)
    private val argCartId: String by getStringArg(ARG_CART_ID)
    private val argIsFulfillment: Boolean by getBooleanArg(ARG_IS_FULFILLMENT)
    private val argLayoutId: String by getStringArg(ARG_LAYOUT_ID)
    private val argPageSource by getStringArg(ARG_PAGE_SOURCE)
    private val argSelectedAddonsIds: List<String> by getStringArrayListArg(ARG_SELECTED_ADDONS_IDS)
    private val argWarehouseId: String by getStringArg(ARG_WAREHOUSE_ID)

    private val callback = PostAtcCallback(this)
    internal val adapter = PostAtcAdapter(callback)

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

    override fun onDestroy() {
        super.onDestroy()
        adapter.stop()
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

    private fun updateFooter() = with(binding) {
        if (this == null) return
        val data = viewModel.postAtcInfo.footer
        postAtcLayoutFooter.showWithCondition(data.shouldShow)
        if (data.shouldShow) {
            postAtcFooterProductImage.setImageUrl(data.image)
//            postAtcFooterDescription.text = data.description
            postAtcFooterButtonMain.text = data.buttonText
            postAtcFooterButtonMain.setOnClickListener {
//                callback.goToCart(data.cartId)
                if (postAtcFooterInfo.isVisible) {
                    val transition = AutoTransition()
                    transition.duration = 150
                    transition.interpolator = AccelerateDecelerateInterpolator()
                    TransitionManager.beginDelayedTransition(postAtcLayoutFooter, transition)
                    postAtcFooterInfo.hide()
                } else {
                    val loadingText =
                        context?.getString(R.string.pdp_post_atc_footer_info_loading) ?: ""
                    postAtcFooterInfo.text = loadingText
//                    fadeInTextView(postAtcFooterInfo)

                    val transition = AutoTransition()
                    transition.duration = 150
                    transition.interpolator = AccelerateDecelerateInterpolator()
                    TransitionManager.beginDelayedTransition(postAtcLayoutFooter, transition)
                    postAtcFooterInfo.show()
                }
            }
        }
    }

    private fun fadeInTextView(textView: TextView) {
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 500 // Set the duration of the animation in milliseconds
        fadeInAnimation.fillAfter = true // Maintain the final state of the animation

        textView.startAnimation(fadeInAnimation)
        textView.visibility = View.VISIBLE
    }

    private fun scrollUpTextView(textView: TextView) {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        animation.duration = 500 // Set the duration of the animation in milliseconds
        animation.fillAfter = true // Maintain the final state of the animation

        textView.startAnimation(animation)
        textView.visibility = View.VISIBLE
    }

    private fun fadeAndScrollUpTextView(textView: TextView) {
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 500 // Set the duration of the fade-in animation in milliseconds
        fadeInAnimation.fillAfter = true // Maintain the final state of the animation

        val scrollUpAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        scrollUpAnimation.duration = 300 // Set the duration of the scroll-up animation in milliseconds
        scrollUpAnimation.fillAfter = true // Maintain the final state of the animation

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(fadeInAnimation)
        animationSet.addAnimation(scrollUpAnimation)

        textView.startAnimation(animationSet)
        textView.visibility = View.VISIBLE
    }

    private fun showError(it: Throwable) {
        if (it is SocketTimeoutException || it is UnknownHostException || it is ConnectException) {
            adapter.replaceComponents(listOf(ErrorUiModel(errorType = GlobalError.NO_CONNECTION)))
        } else {
            adapter.replaceComponents(listOf(FallbackUiModel(cartId = argCartId)))
        }
    }

    internal fun initData() {
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
            argWarehouseId
        )
    }
}
