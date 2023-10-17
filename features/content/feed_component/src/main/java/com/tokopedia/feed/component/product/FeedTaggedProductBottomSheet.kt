package com.tokopedia.feed.component.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.content.common.ui.adapter.ContentTaggedProductBottomSheetAdapter
import com.tokopedia.content.common.ui.viewholder.ContentTaggedProductBottomSheetViewHolder
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.feedcomponent.databinding.BottomSheetFeedTaggedProductBinding
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl
import com.tokopedia.mvcwidget.views.bottomsheets.MvcDetailBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlin.math.roundToInt

class FeedTaggedProductBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetFeedTaggedProductBinding? = null
    private val binding: BottomSheetFeedTaggedProductBinding get() = _binding!!

    private var activityId: String = ""
    private var shopId: String = ""

    private val maxHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENT).roundToInt()
    }

    private val minHeight by lazyThreadSafetyNone {
        (getScreenHeight() * MIN_HEIGHT_PERCENT).roundToInt()
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

    private val productListObserver = Observer<Result<List<ContentTaggedProductUiModel>>?> {
        when (it) {
            is Success -> {
                hideLoading()

                val productShopId =
                    it.data.firstOrNull { product -> product.shop.id.isNotEmpty() }?.shop?.id.orEmpty()
                if (productShopId.isNotEmpty()) {
                    shopId = productShopId
                }

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
            is Fail -> {
                hideLoading()
            }
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

        showLoading()

        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        binding.rvTaggedProduct.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

        listener?.productListLiveData?.observe(viewLifecycleOwner, productListObserver)
        listener?.mvcLiveData?.observe(viewLifecycleOwner, mvcObserver)
    }

    fun show(
        activityId: String,
        shopId: String,
        manager: FragmentManager,
        tag: String
    ) {
        this.activityId = activityId
        this.shopId = shopId
        show(manager, tag)
    }

    private fun showMerchantVoucherWidget(data: TokopointsCatalogMVCSummary) {
        setTitle(getString(com.tokopedia.content.common.R.string.content_product_bs_title_with_promo))
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
        listener = null
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

        val mvcLiveData: LiveData<Result<TokopointsCatalogMVCSummary>?>
        val productListLiveData: LiveData<Result<List<ContentTaggedProductUiModel>>?>
    }

    companion object {
        private const val HEIGHT_PERCENT = 0.8
        private const val MIN_HEIGHT_PERCENT = 0.2
    }
}
