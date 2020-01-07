package com.tokopedia.shop.newproduct.view.viewholder

import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapter
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.newproduct.view.datamodel.EtalaseHighlightCarouselViewModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductEtalaseChipItemViewModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductFeaturedViewModel
import com.tokopedia.shop.newproduct.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.newproduct.view.listener.ShopProductClickedListener

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductCarouselViewHolder(itemView: View, deviceWidth: Int,
                                    shopProductClickedListener: ShopProductClickedListener?,
                                    titleString: String,
                                    @ShopTrackProductTypeDef shopTrackType: Int,
                                    private val shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?) : AbstractViewHolder<Visitable<*>>(itemView) {

    private var tvTitle: TextView? = null
    private var tvSeeAll: TextView? = null
    private var recyclerView: RecyclerView? = null
    private val shopProductCarouselAdapter: ShopProductAdapter
    private var ivBadge: ImageView? = null

    init {
        shopProductCarouselAdapter = ShopProductAdapter(ShopProductAdapterTypeFactory(null,
                shopProductClickedListener, null, null, null, null, null,
                false, deviceWidth, shopTrackType))
        findViews(itemView)
        tvTitle!!.text = titleString
    }

    override fun bind(visitable: Visitable<*>) {
        val recyclerViewState = recyclerView!!.layoutManager!!.onSaveInstanceState()

        if (visitable is ShopProductFeaturedViewModel) {
            shopProductCarouselAdapter.replaceProductList(
                    visitable.shopProductFeaturedViewModelList)
            tvSeeAll!!.visibility = View.GONE
        } else if (visitable is EtalaseHighlightCarouselViewModel) {
            shopProductCarouselAdapter.replaceProductList(
                    visitable.shopProductViewModelList)
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

        shopProductCarouselAdapter.notifyDataSetChanged()

        if (recyclerViewState != null) {
            recyclerView!!.layoutManager!!.onRestoreInstanceState(recyclerViewState)
        }
    }

    private fun findViews(view: View) {
        tvTitle = view.findViewById(R.id.tv_title)
        ivBadge = view.findViewById(R.id.image_view_etalase_badge)
        tvSeeAll = view.findViewById(R.id.tvSeeAll)
        recyclerView = view.findViewById(R.id.recyclerViewCarousel)
        val layoutManager = LinearLayoutManager(view.context,
                LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = layoutManager
        val animator = recyclerView!!.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        recyclerView!!.adapter = shopProductCarouselAdapter

    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_carousel
    }

}
