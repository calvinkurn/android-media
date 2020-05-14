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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.Label


class ProductCardItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var productImage: ImageView
    private var topadsImage: ImageView
    private var productName: TextView
    private var labelDiscount: TextView
    private var textViewSlashedPrice: TextView
    private var textViewPrice: TextView
    private var textViewShopName: TextView
    private var textViewReviewCount: TextView
    private var textViewShopLocation: TextView
    private var labelPromo: Label
    private var shopImage: ImageView
    private var shopBadge: ImageView
    private var imageFreeOngkirPromo: ImageView
    //    private lateinit var stockPercentageProgress: ProgressBarUnify
    private var linearLayoutImageRating: LinearLayout
    private lateinit var productCardItemViewModel: ProductCardItemViewModel


    init {
        productImage = itemView.findViewById(R.id.imageProduct)
        topadsImage = itemView.findViewById(R.id.imageTopAds)
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
//        stockPercentageProgress = itemView.findViewById(R.id.stockPercentageProgress)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as ProductCardItemViewModel
        init()
    }

    fun init() {
        setUpObserver()
    }

    private fun setUpObserver() {
        val lifecycleOwner = fragment.viewLifecycleOwner
        productCardItemViewModel.getDataItemValue().observe(lifecycleOwner, Observer {
            populateData(it)
        })
        productCardItemViewModel.getShopBadge().observe(lifecycleOwner, Observer {
            if (it == OFFICAIL_STORE)
                shopBadge.setImageResource(R.drawable.discovery_official_store_icon)
            else if (it == GOLD_MERCHANT)
                shopBadge.setImageResource(R.drawable.discovery_gold_merchant_icon)
            else
                shopBadge.hide()
        })
        productCardItemViewModel.getFreeOngkirImage().observe(lifecycleOwner, Observer {
            showFreeOngKir(it)
        })

    }

    private fun showFreeOngKir(freeOngkirActive: String) {
        if (freeOngkirActive.isNotEmpty()) {
            ImageHandler.LoadImage(imageFreeOngkirPromo, freeOngkirActive)
            imageFreeOngkirPromo.show()
        } else {
            imageFreeOngkirPromo.hide()
        }
    }

    private fun populateData(dataItem: DataItem) {
        productName.setTextAndCheckShow(dataItem.name)
        textViewShopName.setTextAndCheckShow(dataItem.shopName)
        textViewPrice.setTextAndCheckShow(dataItem.price)

        setLabelDiscount(dataItem.discountPercentage.toString())
        setSlashedPrice(dataItem.discountedPrice)
        setRating(dataItem.rating.toIntOrZero())
        setTextViewReviewCount(dataItem.countReview.toIntOrZero())
        setLabelPromo(dataItem.cashback)
        textViewShopLocation.setTextAndCheckShow(dataItem.shopLocation)
        setShopIcon(dataItem.shopLogo)
        setProductImage(dataItem.imageUrlMobile)
        setTopads(dataItem.isTopads)
//        setStockProgress(dataItem.stockSoldPercentage)

    }

    private fun setTopads(topads: Boolean?) {
        if (topads!!) {
            topadsImage.show()
        } else {
            topadsImage.hide()
        }
    }

    private fun setProductImage(imageUrlMobile: String?) {
        ImageHandler.LoadImage(productImage, imageUrlMobile)
    }

//    private fun setStockProgress(stockPercent: String?) {
//        if (!stockPercent.isNullOrEmpty()){
//            stockPercentageProgress.setValue(stockPercent.toIntOrZero())
//            stockPercentageProgress.show()
//        }
//
//    }

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


    private fun setLabelDiscount(text: String) {
        if (text.isEmpty() || text == "0") {
            labelDiscount.hide()
        } else {
            labelDiscount.show()
            labelDiscount.text = String.format("%s", "$text%")
        }
    }
}

