package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewModel.Companion.GOLD_MERCHANT
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewModel.Companion.OFFICAIL_STORE
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify


class ProductCardItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var labelDiscount: TextView
    private lateinit var textViewSlashedPrice: TextView
    private lateinit var textViewPrice: TextView
    private lateinit var textViewShopName: TextView
    private lateinit var textViewReviewCount: TextView
    private lateinit var textViewShopLocation: TextView
    private lateinit var labelPromo: Label
    private lateinit var shopImage: ImageView
    private lateinit var shopBadge: ImageView
    private lateinit var imageFreeOngkirPromo: ImageView
    private lateinit var stockPercentageProgress: ProgressBarUnify
    private lateinit var linearLayoutImageRating: LinearLayout
    private lateinit var productCardItemViewModel: ProductCardItemViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as ProductCardItemViewModel
        initView()
        productCardItemViewModel.getDataItemValue().observe(fragment.viewLifecycleOwner, Observer {
            populateData(it)
        })
        productCardItemViewModel.getShopBadge().observe(fragment.viewLifecycleOwner, Observer {
            if (it == OFFICAIL_STORE)
                shopBadge.setImageResource(R.drawable.discovery_official_store_icon)
            else if (it == GOLD_MERCHANT)
                shopBadge.setImageResource(R.drawable.discovery_gold_merchant_icon)
            else
                shopBadge.hide()
        })
        productCardItemViewModel.getFreeOngkirImage().observe(fragment.viewLifecycleOwner, Observer {
            showFreeOngKir(it)
        })

    }

    private fun showFreeOngKir(url: String) {
        if (url.isNotEmpty()) {
            ImageHandler.LoadImage(imageFreeOngkirPromo, url)
            imageFreeOngkirPromo.show()
        }
    }

    private fun populateData(dataItem: DataItem) {
        ImageHandler.LoadImage(productImage, dataItem.imageUrlMobile)
        ImageHandler.LoadImage(shopImage, dataItem.shopLogo)
        productName.setTextAndCheckShow(dataItem.name)
        textViewShopName.setTextAndCheckShow(dataItem.shopName)
        textViewPrice.setTextAndCheckShow(dataItem.price)
        setLabelDiscount(dataItem.discountPercentage.toString())
        setSlashedPrice(dataItem.discountedPrice)
        setRating(dataItem.rating.toIntOrZero())
        setTextViewReviewCount(dataItem.countReview.toIntOrZero())
        setLabelPromo(dataItem.cashback)
        setShopIcon(dataItem.shopLogo)
        textViewShopLocation.setTextAndCheckShow(dataItem.shopLocation)
        setStockProgress(dataItem.stockSoldPercentage)

    }

    private fun setStockProgress(stockPercent: String?) {
        if (!stockPercent.isNullOrEmpty()){
            stockPercentageProgress.setValue(stockPercent.toIntOrZero())
            stockPercentageProgress.show()
        }

    }

    private fun setShopIcon(shopLogo: String?) {
        if (!shopLogo.isNullOrEmpty()) {
            ImageHandler.loadImageCircle2(itemView.context, shopImage, shopLogo)
        }
    }

    private fun setLabelPromo(cashback: String?) {
        if (!cashback.isNullOrEmpty()) {
            labelPromo.let {
                it.text = String.format("%s", "Cashback $cashback%")
                it.setLabelType(Label.GENERAL_LIGHT_GREEN)
            }
        }
    }

    private fun setTextViewReviewCount(reviewCount: Int?) {
        if (reviewCount != 0) {
            textViewReviewCount.show()
            textViewReviewCount.text = String.format("%s", "($reviewCount)")
        }

    }

    private fun setRating(rating: Int?) {
        if (rating != null && rating <= 5) {
            for (r in 0 until rating) {
                linearLayoutImageRating.show()
                (linearLayoutImageRating.getChildAt(r) as ImageView).setImageResource(R.drawable.product_card_ic_rating_active)
            }
        }

    }

    private fun setSlashedPrice(discountedPrice: String?) {
        textViewSlashedPrice.let {
            it.setTextAndCheckShow(discountedPrice)
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun initView() {
        productImage = itemView.findViewById(R.id.imageProduct)
        productName = itemView.findViewById(R.id.textViewProductName)
        shopImage = itemView.findViewById(R.id.imageShop)
        textViewShopName = itemView.findViewById(R.id.textViewShopName)
        textViewPrice = itemView.findViewById(R.id.textViewPrice)
        labelDiscount = itemView.findViewById(R.id.labelDiscount)
        textViewSlashedPrice = itemView.findViewById(R.id.textViewSlashedPrice)
        linearLayoutImageRating = itemView.findViewById(R.id.linearLayoutImageRating)
        textViewReviewCount = itemView.findViewById(R.id.textViewReviewCount)
        labelPromo = itemView.findViewById(R.id.labelPromo)
        shopBadge = itemView.findViewById(R.id.imageViewShopBadge)
        textViewShopLocation = itemView.findViewById(R.id.textViewShopLocation)
        imageFreeOngkirPromo = itemView.findViewById(R.id.imageFreeOngkirPromo)
        stockPercentageProgress = itemView.findViewById(R.id.stockPercentageProgress)
    }

    private fun setLabelDiscount(text: String) {
        if (text.isEmpty() || text == "0") {
            labelDiscount.hide()
        } else {
            labelDiscount.show()
            labelDiscount.text = String.format("%s", "$text%")
        }
    }
}

