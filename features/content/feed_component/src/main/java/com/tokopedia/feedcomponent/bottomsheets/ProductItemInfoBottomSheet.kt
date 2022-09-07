package com.tokopedia.feedcomponent.bottomsheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.bottomsheet.ProductBottomSheetData
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.view.adapter.bottomsheetadapter.ProductInfoBottomSheetAdapter
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagAdapter
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.item_posttag.*

class ProductItemInfoBottomSheet : BottomSheetUnify() {

    private lateinit var listProducts: List<FeedXProduct>
    private var listener: Listener? = null
    private var postId: Int = 0
    private var positionInFeed: Int = 0
    private var shopId: String = "0"
    private var shopName: String = ""
    private var mediaType: String = ""
    private var playChannelId: String = "0"
    private var postType: String = ""
    private var saleType: String = ""
    private var saleStatus: String = ""
    private var isFollowed: Boolean = false
    var closeClicked: (() -> Unit)? = null
    var disMissed: (() -> Unit)? = null
    var dismissedByClosing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, R.layout.item_posttag, null)
        setTitle(getString(R.string.content_product_bs_title))
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardTitlePostTag?.gone()
        rvPosttag.show()
        rvPosttag.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvPosttag.isNestedScrollingEnabled = false
        rvPosttag.layoutManager = layoutManager
        rvPosttag.setPadding(0, 0, 0, 0)
        if (::listProducts.isInitialized) {
            setAdapter()
        } else {
            dismiss()
        }

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
    private fun setAdapter() {
        listener?.let {
            val adapter = ProductInfoBottomSheetAdapter(
                it
               )
            rvPosttag.adapter = adapter

                if (listProducts.isNotEmpty()) {
                listener?.onTaggedProductCardImpressed(
                    if (postType == TYPE_FEED_X_CARD_PLAY) playChannelId else postId.toString(),
                    listProducts,
                    postType,
                    shopId,
                    isFollowed,
                    mediaType
                )
                adapter.setItemsAndAnimateChanges(mapPostTag(listProducts))
            }

        }

        if (rvPosttag != null && rvPosttag.adapter != null && rvPosttag.adapter is PostTagAdapter)
            (rvPosttag.adapter as PostTagAdapter).notifyDataSetChanged()
    }

    private fun mapPostTag(postTagItemList: List<FeedXProduct>): List<ProductPostTagViewModelNew> {
        var postDescription = ""
        var adClickUrl = ""
        val desc = context?.getString(R.string.feed_share_default_text)
        val itemList: MutableList<ProductPostTagViewModelNew> = mutableListOf()
        for (postTagItem in postTagItemList) {
                postDescription = desc?.replace("%s", postTagItem.authorName).toString()
                adClickUrl = postTagItem.adClickUrl
            val item = ProductPostTagViewModelNew(
                postTagItem.id,
                postTagItem.name,
                postTagItem.coverURL,
                postTagItem.price.toString(),
                postTagItem.priceFmt,
                postTagItem.isDiscount,
                postTagItem.discountFmt,
                "product",
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
                shopId,
                shopName = shopName,
                description = postDescription,
                isTopads = postTagItem.isTopads,
                adClickUrl = adClickUrl,
                playChannelId = playChannelId,
                saleType = saleType,
                saleStatus = saleStatus
            )
            item.feedType = "product"
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
        productBottomSheetData: ProductBottomSheetData
    ) {
        this.listProducts = productBottomSheetData.products
        this.listener = listener
        this.postId = productBottomSheetData.postId
        this.shopId = productBottomSheetData.shopId
        this.postType = productBottomSheetData.postType
        this.isFollowed = productBottomSheetData.isFollowed
        this.positionInFeed = productBottomSheetData.positionInFeed
        this.playChannelId = productBottomSheetData.playChannelId
        this.shopName = productBottomSheetData.shopName
        this.mediaType = productBottomSheetData.mediaType
        this.saleType = productBottomSheetData.saleType
        this.saleStatus = productBottomSheetData.saleStatus
        show(fragmentManager, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    interface Listener {
        fun onBottomSheetThreeDotsClicked(
            item: ProductPostTagViewModelNew,
            context: Context,
            shopId: String = ""
        )
        fun onTaggedProductCardImpressed(
            activityId: String,
            postTagItemList: List<FeedXProduct>,
            type: String,
            shopId: String,
            isFollowed: Boolean,
            mediaType: String
        )

        fun onTaggedProductCardClicked(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: FeedXProduct,
            itemPosition: Int,
            mediaType: String
        )
        fun onAddToCartButtonClicked(item: ProductPostTagViewModelNew)
        fun onAddToWishlistButtonClicked(item: ProductPostTagViewModelNew)
    }
}