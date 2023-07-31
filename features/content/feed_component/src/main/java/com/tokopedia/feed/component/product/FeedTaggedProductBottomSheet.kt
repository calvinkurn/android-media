package com.tokopedia.feed.component.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.feedcomponent.databinding.BottomSheetFeedTaggedProductBinding
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlin.math.roundToInt

class FeedTaggedProductBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetFeedTaggedProductBinding? = null
    private val binding: BottomSheetFeedTaggedProductBinding get() = _binding!!

    private var activityId: String = ""
    private var viewModel: FeedTaggedProductViewModel? = null
    private var isFirst: Boolean = true

    private val maxHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENT).roundToInt()
    }

    private val minHeight by lazyThreadSafetyNone {
        (getScreenHeight() * MIN_HEIGHT_PERCENT).roundToInt()
    }

    private val mAdapterListener = object : FeedTaggedProductBottomSheetViewHolder.Listener {
        override fun onProductCardClicked(product: FeedTaggedProductUiModel, itemPosition: Int) {
            mListener?.onProductCardClicked(product, itemPosition)
        }

        override fun onAddToCartProductButtonClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        ) {
            mListener?.onAddToCartProductButtonClicked(product, itemPosition)
        }

        override fun onBuyProductButtonClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        ) {
            mListener?.onBuyProductButtonClicked(product, itemPosition)
        }
    }

    private val mAdapter: FeedTaggedProductBottomSheetAdapter by lazy {
        FeedTaggedProductBottomSheetAdapter(mAdapterListener)
    }

    private var mListener: Listener? = null

    fun setCustomListener(listener: Listener) {
        mListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetFeedTaggedProductBinding.inflate(inflater, container, false)
        setTitle(getString(com.tokopedia.content.common.R.string.content_product_bs_title))
        setChild(binding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTaggedProduct.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

        observeProducts()
    }

    override fun onResume() {
        super.onResume()
        if (isFirst) {
            showLoading()
            isFirst = false
        }
    }

    private fun observeProducts() {
        viewModel?.feedTagProductList?.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    mAdapter.setItemsAndAnimateChanges(it.data)
                    binding.root.let { view ->
                        view.viewTreeObserver.addOnGlobalLayoutListener(object :
                                OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                    view.layoutParams = view.layoutParams.apply {
                                        if (view.measuredHeight > maxHeight) {
                                            height = maxHeight
                                        } else if (view.measuredHeight < minHeight) {
                                            height = minHeight
                                        } else if (height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                                            height = ViewGroup.LayoutParams.WRAP_CONTENT
                                        }
                                    }
                                }
                            })
                    }
                }
                is Fail -> {}
            }
        }
    }

    fun show(
        activityId: String,
        viewModelOwner: ViewModelStoreOwner,
        viewModelFactory: ViewModelProvider.Factory,
        manager: FragmentManager,
        tag: String,
        products: List<FeedTaggedProductUiModel> = emptyList(),
        sourceType: FeedTaggedProductUiModel.SourceType
    ) {
        this.isFirst = true
        this.activityId = activityId
        viewModel = ViewModelProvider(
            viewModelOwner,
            viewModelFactory
        )[FeedTaggedProductViewModel::class.java]
        viewModel?.fetchFeedProduct(activityId, products, sourceType)
        show(manager, tag)
    }

    fun showMerchantVoucherWidget(data: TokopointsCatalogMVCSummary, tracker: MvcTrackerImpl) {
        setTitle(getString(com.tokopedia.content.common.R.string.content_product_bs_title_with_promo))
        val info = data.animatedInfoList
        if (info?.isNotEmpty() == true) {
            val shopId = viewModel?.shopId ?: ""
            binding.mvcTaggedProduct.setData(
                mvcData = MvcData(info),
                shopId = shopId,
                source = MvcSource.FEED_BOTTOM_SHEET,
                mvcTrackerImpl = tracker
            )
            binding.mvcTaggedProduct.show()
        } else {
            hideMerchantVoucherWidget()
        }
    }

    fun hideMerchantVoucherWidget() {
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
        mListener = null
        viewModel = null
    }

    private fun showLoading() {
        _binding?.feedProductLoadMore?.show()
        _binding?.rvTaggedProduct?.hide()
    }

    private fun hideLoading() {
        _binding?.feedProductLoadMore?.hide()
        _binding?.rvTaggedProduct?.show()
    }

    interface Listener {
        fun onProductCardClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        )

        fun onAddToCartProductButtonClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        )

        fun onBuyProductButtonClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        )
    }

    companion object {
        private const val HEIGHT_PERCENT = 0.8
        private const val MIN_HEIGHT_PERCENT = 0.2
    }
}
