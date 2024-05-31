package com.tokopedia.feed.component.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.ui.adapter.ContentTaggedProductBottomSheetAdapter
import com.tokopedia.content.common.ui.viewholder.ContentTaggedProductBottomSheetViewHolder
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.feedcomponent.databinding.BottomSheetFeedTaggedProductBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl
import com.tokopedia.mvcwidget.views.bottomsheets.MvcDetailBottomSheet
import com.tokopedia.play_common.view.BottomSheetHeader
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.tokopedia.content.common.R as contentcommonR

class FeedTaggedProductBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetFeedTaggedProductBinding? = null
    private val binding: BottomSheetFeedTaggedProductBinding get() = _binding!!

    private var activityId: String = ""
    private var shopId: String = ""
    private var sourceType: ContentTaggedProductUiModel.SourceType =
        ContentTaggedProductUiModel.SourceType.Unknown

    private val maxHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENT).roundToInt()
    }

    private val mAdapterListener = object : ContentTaggedProductBottomSheetViewHolder.Listener {
        override fun onProductCardClicked(product: ContentTaggedProductUiModel, itemPosition: Int) {
            listener?.onProductCardClicked(product, itemPosition)
        }

        override fun onAddToCartProductButtonClicked(
            product: ContentTaggedProductUiModel,
            itemPosition: Int
        ) {
            listener?.onAddToCartProductButtonClicked(product, itemPosition)
        }

        override fun onBuyProductButtonClicked(
            product: ContentTaggedProductUiModel,
            itemPosition: Int
        ) {
            listener?.onBuyProductButtonClicked(product, itemPosition)
        }
    }

    private val mAdapter: ContentTaggedProductBottomSheetAdapter by lazy {
        ContentTaggedProductBottomSheetAdapter(mAdapterListener)
    }

    var listener: Listener? = null
    var tracker: MvcTrackerImpl = DefaultMvcTrackerImpl()

    private val productListObserver = FlowCollector<FeedProductPaging> { data ->
        when (data.state) {
            is ResultState.Success -> {
                showLoading(false)
                showError(false)
                val productShopId =
                    data.products.firstOrNull { product -> product.shop.id.isNotEmpty() }?.shop?.id.orEmpty()
                if (productShopId.isNotEmpty()) {
                    shopId = productShopId
                }

                mAdapter.setItemsAndAnimateChanges(data.products)
            }

            is ResultState.Fail -> {
                showLoading(false)
                showError(true)
            }

            ResultState.Loading -> {
                showLoading(true)
                showError(false)
            }

            else -> {}
        }
    }

    private val mvcObserver = Observer<Result<TokopointsCatalogMVCSummary>?> { result ->
        when (result) {
            is Success -> {
                if (!result.data.animatedInfoList.isNullOrEmpty()) {
                    listener?.sendMvcImpressionTracker(result.data.animatedInfoList!!)
                }
                showMerchantVoucherWidget(result.data)
            }

            else -> hideMerchantVoucherWidget()
        }
    }

    private val scrollListener
        get() = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isLastItem) {
                    listener?.onFeedProductNextPage(activityId, sourceType)
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }

    private val isLastItem: Boolean
        get() {
            val layoutManager = binding.rvTaggedProduct.layoutManager as? LinearLayoutManager
            val position = layoutManager?.findLastCompletelyVisibleItemPosition().orZero()
            val numItems: Int = binding.rvTaggedProduct.adapter?.itemCount.orZero()
            return position >= numItems - 5 //threshold
        }

    private val rvLayoutChangeListener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            if (binding.feedProductLoadMore.isVisible) {
                val btnHeight = binding.feedProductLoadMore.height
                binding.rvTaggedProduct.setPadding(
                    binding.rvTaggedProduct.paddingLeft,
                    binding.rvTaggedProduct.paddingTop,
                    binding.rvTaggedProduct.paddingRight,
                    btnHeight
                )
            }
            if (!isListHeightTaller) return
            binding.root.layoutParams = binding.root.layoutParams.apply { height = maxHeight }
        }
    }

    private val isListHeightTaller: Boolean
        get() = binding.rvTaggedProduct.height > binding.root.height

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetFeedTaggedProductBinding.inflate(inflater, container, false)
        clearContentPadding = true
        setChild(binding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        showLoading(true)
        renderError()

        binding.root.maxHeight = maxHeight
        binding.rvTaggedProduct.apply {
            addOnScrollListener(scrollListener)
            addOnLayoutChangeListener(rvLayoutChangeListener)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

        setShowListener {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(BOTTOM_SHEET_SHOW_DELAY)
                repeatOnLifecycle(Lifecycle.State.RESUMED){
                    listener?.productListLiveData?.collect(productListObserver)
                }
                listener?.mvcLiveData?.observe(viewLifecycleOwner, mvcObserver)
            }
        }
    }

    fun show(
        activityId: String,
        shopId: String,
        manager: FragmentManager,
        tag: String,
        sourceType: ContentTaggedProductUiModel.SourceType
    ) {
        this.activityId = activityId
        this.shopId = shopId
        this.sourceType = sourceType
        show(manager, tag)
    }

    private fun showMerchantVoucherWidget(data: TokopointsCatalogMVCSummary) {
        setTitle(getString(contentcommonR.string.content_product_bs_title_with_promo))
        val info = data.animatedInfoList
        if (info?.isNotEmpty() == true) {
            binding.mvcTaggedProduct.setData(
                mvcData = MvcData(info),
                shopId = shopId,
                source = MvcSource.FEED_BOTTOM_SHEET,
                mvcTrackerImpl = tracker,
                startActivityForResultFunction = {
                    MvcDetailBottomSheet().also {
                        it.show(
                            shopId = shopId,
                            source = MvcSource.FEED_BOTTOM_SHEET,
                            manager = parentFragmentManager,
                        )
                    }
                }
            )
            binding.mvcTaggedProduct.show()
        } else {
            hideMerchantVoucherWidget()
        }
    }

    private fun hideMerchantVoucherWidget() {
        binding.mvcTaggedProduct.hide()
    }

    fun doShowToaster(
        type: Int = Toaster.TYPE_NORMAL,
        message: String,
        actionText: String = "",
        actionClickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        if (context == null) return
        val parentView = view?.rootView ?: return
        Toaster.build(
            parentView,
            message,
            type = type,
            actionText = actionText,
            clickListener = actionClickListener
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvTaggedProduct.removeOnScrollListener(scrollListener)
        binding.rvTaggedProduct.removeOnLayoutChangeListener(rvLayoutChangeListener)
        _binding = null
        listener = null
    }

    private fun showLoading(isVisible: Boolean) {
        binding.feedProductLoadMore.showWithCondition(isVisible)
    }

    private fun showError(isVisible: Boolean) {
        binding.layoutErrorLoadMore.root.showWithCondition(isVisible)
    }

    private fun renderError() {
        binding.layoutErrorLoadMore.tvErrorRetry.setOnClickListener {
            listener?.onFeedProductNextPage(activityId, sourceType)
        }
    }

    private fun setupHeader() {
        val header = BottomSheetHeader(requireContext()).apply {
            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

            setTitle(getString(contentcommonR.string.content_product_bs_title))
            setIconNotification(IconUnify.CART)
            setIconNotificationText("")
            setListener(object : BottomSheetHeader.Listener {
                override fun onCloseClicked(view: BottomSheetHeader) {
                    dismiss()
                }

                override fun onIconClicked(view: BottomSheetHeader) {
                    listener?.onCartClicked()
                }

                override fun impressIcon(view: BottomSheetHeader) {
                    /** No implementation */
                }
            })

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    listener?.cartCount?.collectLatest {
                        setIconNotificationText(getCartCountText(it))
                    }
                }
            }
        }

        bottomSheetHeader.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        bottomSheetHeader.removeAllViews()
        bottomSheetHeader.addView(header)
    }

    private fun getCartCountText(cartCount: Int): String {
        return if (cartCount <= 0) ""
        else if (cartCount >= 100) "99+"
        else cartCount.toString()
    }

    interface Listener {
        fun onProductCardClicked(
            product: ContentTaggedProductUiModel,
            itemPosition: Int
        )

        fun onAddToCartProductButtonClicked(
            product: ContentTaggedProductUiModel,
            itemPosition: Int
        )

        fun onBuyProductButtonClicked(
            product: ContentTaggedProductUiModel,
            itemPosition: Int
        )

        fun sendMvcImpressionTracker(mvcList: List<AnimatedInfos?>)

        fun onCartClicked()

        val mvcLiveData: LiveData<Result<TokopointsCatalogMVCSummary>?>

        val productListLiveData: Flow<FeedProductPaging>

        val cartCount: StateFlow<Int>

        fun onFeedProductNextPage(
            activityId: String,
            sourceType: ContentTaggedProductUiModel.SourceType
        )
    }

    companion object {
        private const val HEIGHT_PERCENT = 0.8
        private const val BOTTOM_SHEET_SHOW_DELAY = 500L
    }
}
