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
import com.tokopedia.createpost.uprofile.views.UserProfileFragment.Companion.VAL_FEEDS_PROFILE
import com.tokopedia.createpost.uprofile.views.UserProfileFragment.Companion.VAL_SOURCE_BUYER
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem
import com.tokopedia.unifycomponents.ImageUnify
import java.lang.Exception

open class UserPostBaseAdapter(
    val viewModel: UserProfileViewModel,
    val callback: AdapterCallback,
    private var userName: String = ""
) : BaseAdapter<PlayPostContentItem>(callback) {

    protected var cList: MutableList<BaseItem>? = null
    private var cursor: String = ""

    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var imgCover: ImageUnify = view.findViewById(R.id.img_banner)
        internal var textLiveCount: TextView = view.findViewById(R.id.text_live_view_count)
        internal var textName: TextView = view.findViewById(R.id.text_display_name)
        internal var textUsername: TextView = view.findViewById(R.id.text_user_name)
        internal var textLive: TextView = view.findViewById(R.id.text_live)
        var isVisited = false

        override fun bindView(item: PlayPostContentItem, position: Int) {
            setData(this, item)
        }
    }

    public fun setUserName(userName: String) {
        this.userName = userName
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

    override fun loadData(currentPageIndex: Int, vararg args: String?) {
        super.loadData(currentPageIndex, *args)
        if (args.isNotEmpty()) {
            args[0]?.let {
                viewModel.getUPlayVideos(
                    VAL_FEEDS_PROFILE,
                    cursor,
                    VAL_SOURCE_BUYER,
                    it
                )
            }
        }
    }

    fun onSuccess(data: UserPostModel) {
        if (data == null
            || data.playGetContentSlot == null
            || data.playGetContentSlot.data == null
            || data.playGetContentSlot.data.size == 0
        ) {
            loadCompleted(mutableListOf(), data)
            isLastPage = true
            cursor = ""
        } else {
            loadCompleted(data?.playGetContentSlot?.data[0]?.items, data)
            isLastPage = data?.playGetContentSlot?.playGetContentSlot?.nextCursor?.isEmpty()
            cursor = data?.playGetContentSlot?.playGetContentSlot?.nextCursor;
        }
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: PlayPostContentItem) {
        val itemContext = holder.itemView.context
        holder.imgCover.setImageUrl(item.coverUrl)
        holder.textName.text = item.title

        try {
            if (item.stats.view.formatted.toInt() == 0) {
                holder.textLiveCount.hide()
            } else {
                holder.textLiveCount.show()
                holder.textLiveCount.text = item.stats.view.formatted
            }
        } catch (e: Exception) {

        }

        if (item.isLive) {
            holder.textLive.show()
        } else {
            holder.textLive.hide()
        }

        if (userName.isNotBlank()) {
            holder.textUsername.show()
            holder.textUsername.text = "@$userName"
        } else {
            holder.textUsername.hide()
        }

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

