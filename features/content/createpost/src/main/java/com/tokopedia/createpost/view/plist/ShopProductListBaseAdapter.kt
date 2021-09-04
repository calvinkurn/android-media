package com.tokopedia.createpost.view.plist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.createpost.createpost.R
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel

class ShopProductListBaseAdapter(
    val viewModel: ShopPageProductListViewModel,
    val callback: AdapterCallback
) : BaseAdapter<ShopPageProduct>(callback) {


    inner class ViewHolder(view: View) : BaseVH(view) {
        internal var productView: ProductCardGridView
//        internal var imgBanner: ImageView
        var isVisited = false

        init {
            productView = view.findViewById(R.id.product_card)
//            imgBanner = view.findViewById(R.id.img_banner)
        }

        override fun bindView(item: ShopPageProduct, position: Int) {
            setData(this, item)
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_shop_plist_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun loadData(pageNumber: Int) {
        super.loadData(pageNumber)
        viewModel.getList(pageNumber)
    }

    fun onSuccess(data: GetShopProduct) {
        loadCompleted(data.productList.data, data)
        isLastPage = data.productList.paging.next.isEmpty()
    }

    fun onError() {
        loadCompletedWithError()
    }

    private fun setData(holder: ViewHolder, item: ShopPageProduct) {
        val itemContext = holder.itemView.context
        holder.productView.setProductModel(toShopProductModel(item))

        if (holder.itemView != null) {
            holder.itemView.setOnClickListener { v ->
//                val bundle = Bundle()
//                bundle.putString(CommonConstant.EXTRA_COUPON_CODE, item.code)
//                (holder.imgBanner.context as FragmentActivity).startActivityForResult(CouponDetailActivity.getCouponDetail(holder.imgBanner.context, bundle), REQUEST_CODE_STACKED_IN_ADAPTER)
//
//                sendClickEvent(itemContext, item, holder.getAdapterPosition())
            }
        }
    }


    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(vh)

//        if (vh is ViewHolder) {
//            val holder = vh as ViewHolder
//
//            val data = items[holder.getAdapterPosition()] ?: return
//
//            if (!holder.isVisited) {
//                val item = HashMap<String, String?>()
//                item["id"] = data.catalogId.toString()
//                item["name"] = String.format(KUPON_ITEMNAME, vh.adapterPosition + 1)
//                item["position"] = (holder.adapterPosition + 1).toString()
//                item["creative"] = data.title
//                item["creative_url"] = data.imageUrlMobile
//                item["promo_code"] = data.code
//
//                val promotions = HashMap<String, List<Map<String, String?>>>()
//                promotions["promotions"] = Arrays.asList<Map<String, String?>>(item)
//
//                val promoView = HashMap<String, Map<String, List<Map<String, String?>>>>()
//                promoView["promoView"] = promotions
//
//                var eventLabel = ""
//                if (data.title != null && data.title.isNotEmpty()) {
//                    eventLabel = data.title
//                }
//                AnalyticsTrackerUtil.sendECommerceEvent(holder.itemView.context,
//                    AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
//                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
//                    AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON,
//                    eventLabel, promoView)
//                holder.isVisited = true
//            }
//        }
    }

    private fun sendClickEvent(context: Context, data: ShopPageProduct, position: Int) {
//        val item = HashMap<String, String?>()
//        item["id"] = data.catalogId.toString()
//        item["name"] = String.format(KUPON_ITEMNAME, position+1)
//        item["position"] = (position + 1).toString()
//        item["creative"] = data.title
//        item["creative_url"] = data.imageUrlMobile
//        item["promo_code"] = data.code
//
//        val promotions = HashMap<String, List<Map<String, String?>>>()
//        promotions["promotions"] = Arrays.asList<Map<String, String?>>(item)
//
//        val promoClick: HashMap<String, Map<String, List<Map<String, String?>>>> = HashMap()
//        promoClick["promoClick"] = promotions
//
//        var eventLabel = ""
//        if (data.title != null && data.title.isNotEmpty()) {
//            eventLabel = data.title
//        }
//        AnalyticsTrackerUtil.sendECommerceEvent(context,
//            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
//            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_KUPON_SAYA,
//            AnalyticsTrackerUtil.ActionKeys.CLICK_COUPON,
//            eventLabel, promoClick)
    }


    fun toShopProductModel(item : ShopPageProduct) : ProductCardModel {
        return ProductCardModel(
            productImageUrl = item.pImage.img,
            productName = item.name ?: "",
            formattedPrice = item.price.priceIdr
        )
    }
}
