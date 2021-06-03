package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagAdapter
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactoryImpl
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.BasePostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.item_posttag.*

class ProductItemInfoBottomSheet : BottomSheetUnify() {

    private lateinit var listProducts: List<FeedXProduct>
    private lateinit var listener: DynamicPostViewHolder.DynamicPostListener
    private var postId: Int = 0
    var closeClicked: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        rvPosttag.adapter = PostTagAdapter(
            mapPostTag(listProducts),
            PostTagTypeFactoryImpl(listener, DeviceScreenInfo.getScreenWidth(requireContext()))
        )
        (rvPosttag.adapter as PostTagAdapter).notifyDataSetChanged()
        setCloseClickListener {
            closeClicked?.invoke()
            dismiss()
        }
    }

    private fun mapPostTag(postTagItemList: List<FeedXProduct>): MutableList<BasePostTagViewModel> {
        val itemList: MutableList<BasePostTagViewModel> = ArrayList()
        for (postTagItem in postTagItemList) {
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
                    "product"
            )
            item.feedType = "product"
            item.postId = postId
            item.positionInFeed = 0
            itemList.add(item)
        }
        return itemList
    }

    fun show(fragmentManager: FragmentManager, products: List<FeedXProduct>, dynamicPostListener: DynamicPostViewHolder.DynamicPostListener, postId: Int) {
        this.listProducts = products
        this.listener = dynamicPostListener
        this.postId = postId
        show(fragmentManager, "")
    }
}