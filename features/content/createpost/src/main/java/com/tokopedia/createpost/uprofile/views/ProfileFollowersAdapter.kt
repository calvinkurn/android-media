package com.tokopedia.createpost.uprofile.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.common.view.plist.ShopPageProduct
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.model.ProfileFollowerV2
import com.tokopedia.createpost.uprofile.model.UserPostModel
import com.tokopedia.createpost.uprofile.viewmodels.FollowerFollowingViewModel
import com.tokopedia.createpost.uprofile.viewmodels.UserProfileViewModel
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.library.baseadapter.BaseItem

open class ProfileFollowersAdapter(
    val viewModel: FollowerFollowingViewModel,
    val callback: AdapterCallback
) : BaseAdapter<ProfileFollowerV2>(callback) {

    protected var cList: MutableList<BaseItem>? = null

    inner class ViewHolder(view: View) : BaseVH(view) {
//        internal var productView: ProductCardGridView = view.findViewById(R.id.product_card)
        var isVisited = false

        override fun bindView(item: ProfileFollowerV2, position: Int) {
            setData(this, item)
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.up_item_followers, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int) {
        super.loadData(pageNumber)
        //viewModel.getList(pageNumber)
        val list = mutableListOf<ProfileFollowerV2>()
        list.add(ProfileFollowerV2("hiii"))
        list.add(ProfileFollowerV2("hiii"))
        list.add(ProfileFollowerV2("hiii"))
        list.add(ProfileFollowerV2("hiii"))
        list.add(ProfileFollowerV2("hiii"))
        loadCompleted(list, null)
        isLastPage = true;
    }

    fun onSuccess(data: UserPostModel) {
        //loadCompleted(data.productList.data, data)
        val list = mutableListOf<UserPostModel>()

        isLastPage = true;
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: ProfileFollowerV2) {
        val itemContext = holder.itemView.context
        // holder.productView.setProductModel(toShopProductModel(item))

//        if (holder.itemView != null) {
//            holder.itemView.setOnClickListener { v ->
//                viewModel.setNewProductValue(item)
//                sendClickEvent(itemContext, item, holder.adapterPosition)
//            }
//        }
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

