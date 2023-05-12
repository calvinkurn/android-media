package com.tokopedia.feed.component.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.databinding.BottomSheetFeedTaggedProductBinding
import com.tokopedia.feedcomponent.presentation.viewmodel.FeedProductItemInfoViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * todo:
 * - Max bottom sheet height is 80% of device height
 */
class FeedTaggedProductBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetFeedTaggedProductBinding? = null
    private val binding: BottomSheetFeedTaggedProductBinding get() = _binding!!

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

        override fun onBuyProductButtonClicked(product: FeedTaggedProductUiModel, itemPosition: Int) {
            mListener?.onBuyProductButtonClicked(product, itemPosition)
        }
    }
    private val mAdapter: FeedTaggedProductBottomSheetAdapter by lazy {
        FeedTaggedProductBottomSheetAdapter(mAdapterListener)
    }

    private val mTaggedProducts = mutableListOf<FeedTaggedProductUiModel>()
    private fun setTaggedProducts(taggedProducts: List<FeedTaggedProductUiModel>) {
        mTaggedProducts.clear()
        mTaggedProducts.addAll(taggedProducts)
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetFeedTaggedProductBinding.inflate(inflater, container, false)
        setTitle(getString(R.string.content_product_bs_title))
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

        showExistingTaggedProduct()
    }

    private fun showExistingTaggedProduct() {
        mAdapter.setItemsAndAnimateChanges(mTaggedProducts)
    }

    fun show(
        taggedProducts: List<FeedTaggedProductUiModel>? = null,
        manager: FragmentManager,
        tag: String
    ) {
        if (taggedProducts != null) setTaggedProducts(taggedProducts)
        show(manager, tag)
    }

    fun showMerchantVoucherWidget(data: TokopointsCatalogMVCSummary) {
        setTitle(getString(R.string.content_product_bs_title_with_promo))
        val info = data.animatedInfoList
        if (info?.isNotEmpty() == true) {
            val shopId = mTaggedProducts.first().shopId
            binding.mvcTaggedProduct.setData(
                mvcData = MvcData(info),
                shopId = shopId,
                source = MvcSource.FEED_BOTTOM_SHEET,
                mvcTrackerImpl = DefaultMvcTrackerImpl()
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
}
