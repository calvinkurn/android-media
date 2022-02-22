package com.tokopedia.createpost.uprofile.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.createpost.common.view.plist.ShopPageProduct
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.model.PlayPostContent
import com.tokopedia.createpost.uprofile.model.PlayPostContentItem
import com.tokopedia.createpost.uprofile.model.UserPostModel
import com.tokopedia.createpost.uprofile.viewmodels.UserProfileViewModel
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.unifycomponents.ImageUnify

open class UserPostBaseAdapter(
    val viewModel: UserProfileViewModel,
    val callback: AdapterCallback
) : BaseAdapter<PlayPostContentItem>(callback) {

    protected var cList: MutableList<BaseItem>? = null
    private var cursor: String = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var imgCover: ImageUnify = view.findViewById(R.id.img_banner)
        internal var textLiveCount: TextView = view.findViewById(R.id.text_live_view_count)
        internal var textName: TextView = view.findViewById(R.id.text_display_name)
        internal var textUsername: TextView = view.findViewById(R.id.text_user_name)
        var isVisited = false

        override fun bindView(item: PlayPostContentItem, position: Int) {
            setData(this, item)
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.up_item_user_post, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int) {
        super.loadData(pageNumber)
        viewModel.getUPlayVideos( "feeds-profile",cursor,"buyer","5510248")
    }

    fun onSuccess(data: UserPostModel) {
        loadCompleted(data.playGetContentSlot.data[0].items, data)
        isLastPage = data.playGetContentSlot.playGetContentSlot.nextCursor.isEmpty()
        cursor = data.playGetContentSlot.playGetContentSlot.nextCursor;
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: PlayPostContentItem) {
        val itemContext = holder.itemView.context
        holder.imgCover.setImageUrl(item.coverUrl)
        holder.textName.text = item.title
        holder.textUsername.text = item.description
        holder.textLiveCount.text = item.stats.view.formatted

        holder.itemView.setOnClickListener { v ->
           RouteManager.route(itemContext, item.appLink)
        }
    }


    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

        if (vh is ViewHolder) {
            val holder = vh as ViewHolder
            val data = items[holder.adapterPosition] ?: return
            //listener.shopProductImpressed(holder.adapterPosition, data)
        }
    }

    private fun sendClickEvent(context: Context, data: ShopPageProduct, position: Int) {
//        listener.shopProductClicked(position, data)
    }

//    private fun toShopProductModel(item: ShopPageProduct): ProductCardModel {
//        val isDiscount = !item.campaign?.dPrice?.toInt().isZero()
//        return ProductCardModel(
//            productImageUrl = item.pImage?.img!!,
//            productName = item.name ?: "",
//            formattedPrice = item.price?.priceIdr!!,
//            discountPercentage = if (isDiscount)
//                ("${item.campaign?.dPrice!!}%") else "",
//            slashedPrice = if (isDiscount) item.campaign?.oPriceFormatted!! else ""
//        )
//    }
}

