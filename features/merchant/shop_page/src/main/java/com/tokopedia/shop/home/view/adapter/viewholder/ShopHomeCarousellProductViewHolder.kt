package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopPageHomeCarousellAdapter
import com.tokopedia.shop.home.view.adapter.ShopPageHomeCarousellAdapterTypeFactory
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopHomeCarousellProductViewHolder(itemView: View) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {
    private var textViewTitle: TextView? = null
    private var textViewCta: TextView? = null
    private var ivBadge: ImageView? = null
    private var etalaseHeaderContainer: View? = null
    private var recyclerView: RecyclerView? = null
    private val adapterTypeFactory: ShopPageHomeCarousellAdapterTypeFactory by lazy {
        ShopPageHomeCarousellAdapterTypeFactory()
    }
    private val adapterCarousell: ShopPageHomeCarousellAdapter by lazy {
        ShopPageHomeCarousellAdapter(adapterTypeFactory).apply {
            adapterTypeFactory.shopPageHomeCarAdapter = this
        }
    }

    init {
        initView(itemView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_carousel
    }

    private fun initView(view: View) {
        textViewTitle = view.findViewById(R.id.tv_title)
        ivBadge = view.findViewById(R.id.image_view_etalase_badge)
        textViewCta = view.findViewById(R.id.tvSeeAll)
        etalaseHeaderContainer= view.findViewById(R.id.etalase_header_container)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.apply {
            adapter = adapterCarousell
            layoutManager = LinearLayoutManager(
                    view.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
        }
    }

    override fun bind(uiModel: ShopHomeCarousellProductUiModel) {
        val title = uiModel.header.title
        val ctaText = uiModel.header.ctaText
        if(title.isEmpty() && ctaText.isEmpty()){
            etalaseHeaderContainer?.hide()
        }
        ivBadge?.visibility = View.GONE
        textViewTitle?.text = MethodChecker.fromHtml(title)
        if(ctaText.isNotEmpty()){
            textViewCta?.apply {
                visibility = View.VISIBLE
                text = MethodChecker.fromHtml(uiModel.header.ctaText)
            }
        }
        adapterCarousell.uiModel = uiModel
        adapterCarousell.clearAllElements()
        adapterCarousell.setProductListData(uiModel.productList)
    }
}
