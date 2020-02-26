package com.tokopedia.shop.newproduct.view.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.newproduct.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.newproduct.view.datamodel.EtalaseHighlightCarouselViewModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductFeaturedViewModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.newproduct.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.newproduct.view.listener.ShopProductClickedListener
import com.tokopedia.shop.newproduct.view.listener.ShopProductImpressionListener

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductCarouselViewHolder(itemView: View, deviceWidth: Int,
                                    private val shopProductClickedListener: ShopProductClickedListener?,
                                    private val shopProductImpressionListener: ShopProductImpressionListener?,
                                    titleString: String,
                                    @ShopTrackProductTypeDef private val shopTrackType: Int,
                                    private val shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?) : AbstractViewHolder<Visitable<*>>(itemView) {

    private var tvTitle: TextView? = null
    private var tvSeeAll: TextView? = null
    private var recyclerView: CarouselProductCardView? = null
    private var ivBadge: ImageView? = null

    init {
        findViews(itemView)
        tvTitle!!.text = titleString
    }

    override fun bind(visitable: Visitable<*>) {
        if (visitable is ShopProductFeaturedViewModel) {
            bindShopProductCarousel(visitable.shopProductFeaturedViewModelList)

            tvSeeAll!!.visibility = View.GONE
        } else if (visitable is EtalaseHighlightCarouselViewModel) {
            bindShopProductCarousel(visitable.shopProductViewModelList)

            val shopEtalaseViewModel = visitable.shopEtalaseViewModel
            tvTitle!!.text = shopEtalaseViewModel.etalaseName
            if (!TextUtils.isEmpty(shopEtalaseViewModel.etalaseBadge)) {
                ImageHandler.LoadImage(ivBadge!!, shopEtalaseViewModel.etalaseBadge)
                ivBadge!!.visibility = View.VISIBLE
            } else {
                ivBadge!!.visibility = View.GONE
            }
            tvSeeAll!!.setOnClickListener {
                shopCarouselSeeAllClickedListener?.onSeeAllClicked(shopEtalaseViewModel)
            }
            tvSeeAll!!.visibility = View.VISIBLE
        }
    }

    private fun bindShopProductCarousel(shopProductViewModelList: List<ShopProductViewModel>) {
        val productCardModelList = shopProductViewModelList.map {
            ShopPageProductListMapper.mapToProductCardModel(it)
        }

        recyclerView?.bindCarouselProductCardViewGrid(
                productCardModelList = productCardModelList,
                carouselProductCardOnItemClickListener = object: CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, adapterPosition: Int) {
                        if (adapterPosition > shopProductViewModelList.size) return

                        shopProductClickedListener?.onProductClicked(shopProductViewModelList[adapterPosition], shopTrackType, adapterPosition)
                    }
                },
                carouselProductCardOnItemImpressedListener = object: CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(productCardModel: ProductCardModel, adapterPosition: Int) {
                        if (adapterPosition > shopProductViewModelList.size) return

                        shopProductImpressionListener?.onProductImpression(shopProductViewModelList[adapterPosition], shopTrackType, adapterPosition)
                    }

                    override fun getImpressHolder(adapterPosition: Int): ImpressHolder {
                        return shopProductViewModelList[adapterPosition]
                    }
                }
        )
    }

    private fun findViews(view: View) {
        tvTitle = view.findViewById(R.id.tv_title)
        ivBadge = view.findViewById(R.id.image_view_etalase_badge)
        tvSeeAll = view.findViewById(R.id.tvSeeAll)
        recyclerView = view.findViewById(R.id.recyclerViewCarousel)
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_carousel
    }

}
