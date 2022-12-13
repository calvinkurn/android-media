package com.tokopedia.feedcomponent.bottomsheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.bottomsheet.ProductBottomSheetData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.databinding.ItemPosttagBinding
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.presentation.viewmodel.FeedProductItemInfoViewModel
import com.tokopedia.feedcomponent.view.adapter.bottomsheetadapter.ProductInfoBottomSheetAdapter
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class ProductItemInfoBottomSheet : BottomSheetUnify() {

    private var binding: ItemPosttagBinding? = null

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private var viewModel: FeedProductItemInfoViewModel? = null
    private var customMvcTracker: MvcTrackerImpl? = null

    private lateinit var listProducts: List<FeedXProduct>
    private var listener: Listener? = null
    private var postId: String = "0"
    private val adapter by lazy {
        listener?.let {
            ProductInfoBottomSheetAdapter(it)
        }
    }
    private var positionInFeed: Int = 0
    private var shopId: String = "0"
    private var shopName: String = ""
    private var mediaType: String = ""
    private var playChannelId: String = "0"
    private var postType: String = ""
    private var saleType: String = ""
    private var saleStatus: String = ""
    private var isFollowed: Boolean = false
    private var hasVoucher: Boolean = false
    var closeClicked: (() -> Unit)? = null
    var disMissed: (() -> Unit)? = null
    var dismissedByClosing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemPosttagBinding.inflate(inflater, container, false)
        setTitle(getString(R.string.content_product_bs_title))
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.cardTitlePostTag?.gone()
        binding?.rvPosttag?.show()
        binding?.rvPosttag?.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.rvPosttag?.isNestedScrollingEnabled = false
        binding?.rvPosttag?.layoutManager = layoutManager
        binding?.rvPosttag?.setPadding(0, 0, 0, 0)
        if (::listProducts.isInitialized) {
            setAdapter()
        } else {
            dismiss()
        }

        activity?.let {
            viewModelFactory?.let { factory ->
                viewModel = ViewModelProvider(
                    it,
                    factory
                ).get(FeedProductItemInfoViewModel::class.java)
            }
        }

        viewModel?.fetchMerchantVoucherSummary(shopId)
        observe()

        setCloseClickListener {
            dismissedByClosing = true
            closeClicked?.invoke()
            dismiss()
        }
        setOnDismissListener {
            if (!dismissedByClosing) {
                disMissed?.invoke()
            }
        }
    }

    private fun observe() {
        viewModel?.merchantVoucherSummary?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.animatedInfoList?.isNotEmpty() == true) {
                        binding?.merchantVoucherWidgetPostTag?.setData(
                            mvcData = MvcData(
                                it.data.animatedInfoList
                            ),
                            shopId = shopId,
                            source = MvcSource.FEED_BOTTOM_SHEET,
                            mvcTrackerImpl = customMvcTracker ?: DefaultMvcTrackerImpl()
                        )
                        binding?.merchantVoucherWidgetPostTag?.show()
                    } else {
                        binding?.merchantVoucherWidgetPostTag?.hide()
                    }
                }
                is Fail -> {
                    binding?.merchantVoucherWidgetPostTag?.hide()
                }
            }
        }
    }

    private fun setAdapter() {
        binding?.rvPosttag?.adapter = adapter
        if (listProducts.isNotEmpty()) {
            listener?.onTaggedProductCardImpressed(
                if (postType == TYPE_FEED_X_CARD_PLAY) playChannelId else postId.toString(),
                listProducts,
                postType,
                shopId,
                isFollowed,
                mediaType,
                hasVoucher
            )
            adapter?.setItemsAndAnimateChanges(mapPostTag(listProducts))
        }
    }

    private fun mapPostTag(postTagItemList: List<FeedXProduct>): List<ProductPostTagModelNew> {
        var postDescription = ""
        var adClickUrl = ""
        val desc = context?.getString(R.string.feed_share_default_text)
        val itemList: MutableList<ProductPostTagModelNew> = mutableListOf()
        for (postTagItem in postTagItemList) {
            postDescription = desc?.replace("%s", postTagItem.authorName).toString()
            adClickUrl = postTagItem.adClickUrl
            val item = ProductPostTagModelNew(
                postTagItem.id,
                postTagItem.name,
                postTagItem.coverURL,
                postTagItem.price.toString(),
                postTagItem.priceFmt,
                postTagItem.isDiscount,
                postTagItem.discountFmt,
                PRODUCT_TYPE,
                postTagItem.appLink,
                postTagItem.webLink,
                postTagItem,
                postTagItem.isBebasOngkir,
                postTagItem.bebasOngkirStatus,
                postTagItem.bebasOngkirURL,
                postTagItem.priceOriginal,
                postTagItem.priceOriginalFmt,
                postTagItem.priceDiscountFmt,
                postTagItem.totalSold,
                postTagItem.star,
                postTagItem.mods,
                postTagItem.shopID,
                shopName = shopName,
                description = postDescription,
                isTopads = postTagItem.isTopads,
                adClickUrl = adClickUrl,
                playChannelId = playChannelId,
                saleType = saleType,
                saleStatus = saleStatus
            )
            item.feedType = PRODUCT_TYPE
            item.postId = postId
            item.positionInFeed = positionInFeed
            item.postType = postType
            item.mediaType = mediaType
            item.isFollowed = isFollowed
            itemList.add(item)
        }
        return itemList
    }

    fun show(
        fragmentManager: FragmentManager,
        listener: Listener?,
        productBottomSheetData: ProductBottomSheetData,
        viewModelFactory: ViewModelProvider.Factory,
        customMvcTracker: MvcTrackerImpl? = null
    ) {
        this.listProducts = productBottomSheetData.products
        this.listener = listener
        this.postId = productBottomSheetData.postId
        this.shopId = productBottomSheetData.shopId
        this.postType = productBottomSheetData.postType
        this.isFollowed = productBottomSheetData.isFollowed
        this.hasVoucher = productBottomSheetData.hasVoucher
        this.positionInFeed = productBottomSheetData.positionInFeed
        this.playChannelId = productBottomSheetData.playChannelId
        this.shopName = productBottomSheetData.shopName
        this.mediaType = productBottomSheetData.mediaType
        this.saleType = productBottomSheetData.saleType
        this.saleStatus = productBottomSheetData.saleStatus
        this.viewModelFactory = viewModelFactory
        this.customMvcTracker = customMvcTracker

        dismissedByClosing = false
        show(fragmentManager, "")
    }

    fun changeWishlistIconOnWishlistSuccess(rowNumber: Int) {
        val item = adapter?.getItem(rowNumber)
        item?.isWishlisted = true
        val payload = Bundle().apply {
            putBoolean(WISHLIST_ITEM_CLICKED, true)
        }
        adapter?.notifyItemChanged(rowNumber, payload)
    }

    fun showToasterOnBottomSheetOnSuccessFollow(
        message: String,
        type: Int,
        actionText: String? = null
    ) {
        view?.rootView?.let {
            context?.resources?.let { resource ->
                Toaster.toasterCustomBottomHeight =
                    resource.getDimensionPixelSize(com.tokopedia.feedcomponent.R.dimen.feed_bottomsheet_toaster_margin_bottom)
            }
            if (actionText?.isEmpty() == false)
                Toaster.build(it, message, Toaster.LENGTH_LONG, type, actionText)
                    .show()
            else {
                Toaster.build(it, message, Toaster.LENGTH_LONG, type).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    interface Listener {
        fun onBottomSheetThreeDotsClicked(
            item: ProductPostTagModelNew,
            context: Context,
            shopId: String = ""
        )

        fun onTaggedProductCardImpressed(
            activityId: String,
            postTagItemList: List<FeedXProduct>,
            type: String,
            shopId: String,
            isFollowed: Boolean,
            mediaType: String,
            hasVoucher: Boolean
        )

        fun onTaggedProductCardClicked(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: FeedXProduct,
            itemPosition: Int,
            mediaType: String
        )

        fun onAddToCartButtonClicked(item: ProductPostTagModelNew)
        fun onAddToWishlistButtonClicked(item: ProductPostTagModelNew, rowNumber: Int)
    }

    companion object {
        private const val WISHLIST_ITEM_CLICKED = "wishlist_button_clicked"
        private const val PRODUCT_TYPE = "product"
    }
}
