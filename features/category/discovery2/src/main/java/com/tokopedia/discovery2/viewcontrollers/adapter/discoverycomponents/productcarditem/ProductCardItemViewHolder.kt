package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewModel.Companion.GOLD_MERCHANT
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewModel.Companion.OFFICAIL_STORE
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify


class ProductCardItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var productImage: ImageUnify
    private var topadsImage: ImageView
    private var productName: TextView
    private var labelDiscount: TextView
    private var textViewSlashedPrice: TextView
    private var textViewPrice: TextView
    private var textViewShopName: TextView
    private var textViewReviewCount: TextView
    private var textViewShopLocation: TextView
    private var labelCashbackPromo: Label
    private var shopImage: ImageView
    private var shopBadge: ImageView
    private var imageFreeOngkirPromo: ImageView
    private var productCardView: CardView
    private var stockPercentageProgress: ProgressBarUnify
    private var linearLayoutImageRating: LinearLayout
    private lateinit var productCardItemViewModel: ProductCardItemViewModel
    var lifecycleOwner: LifecycleOwner


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
        labelCashbackPromo = itemView.findViewById(R.id.labelCashBack)
        shopBadge = itemView.findViewById(R.id.imageViewShopBadge)
        textViewShopLocation = itemView.findViewById(R.id.textViewShopLocation)
        imageFreeOngkirPromo = itemView.findViewById(R.id.imageFreeOngkirPromo)
        stockPercentageProgress = itemView.findViewById(R.id.stockPercentageProgress)
        productCardView = itemView.findViewById(R.id.cardViewProductCard)
        lifecycleOwner = fragment.viewLifecycleOwner
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as ProductCardItemViewModel
        init()
    }

    fun init() {
        productCardItemViewModel.setContext(productCardView.context)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        setUpObserver()
    }

    override fun onViewDetachedToWindow() {
        if (productCardItemViewModel.getDataItemValue().hasObservers()) {
            productCardItemViewModel.getDataItemValue().removeObservers(lifecycleOwner)
        }
    }

    private fun setUpObserver() {
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
        productCardView.setOnClickListener {
            productCardItemViewModel.handleUIClick(it)
        }
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
        Log.d("populateData", this.toString() + dataItem)
        productName.setTextAndCheckShow(dataItem.name)
        textViewShopName.setTextAndCheckShow(dataItem.shopName)
        textViewPrice.setTextAndCheckShow(dataItem.price)

        setLabelDiscount(dataItem.discountPercentage.toString())
        setSlashedPrice(dataItem.discountedPrice)
        setRating(dataItem.rating.toIntOrZero())
        setTextViewReviewCount(dataItem.countReview.toIntOrZero())
        setCashbackLabel(dataItem.cashback)
        textViewShopLocation.setTextAndCheckShow(dataItem.shopLocation)
        setShopIcon(dataItem.shopLogo)
        setProductImage(dataItem.imageUrlMobile)
        setTopads(dataItem.isTopads)
        setStockProgress(dataItem.stockSoldPercentage)

    }

    private fun setTopads(topads: Boolean?) {
        if (topads!!) {
            topadsImage.show()
        } else {
            topadsImage.hide()
        }
    }

    private fun setProductImage(imageUrlMobile: String?) {
        Log.d("setProductImage", this.toString() + imageUrlMobile)
        productImage.loadImage(imageUrlMobile ?: "")
    }

    private fun setStockProgress(stockPercent: String?) {
        if (!stockPercent.isNullOrEmpty()) {
            stockPercentageProgress.setValue(stockPercent.toIntOrZero())
            stockPercentageProgress.show()
        }

    }

    private fun setShopIcon(shopLogo: String?) {
        if (!shopLogo.isNullOrEmpty()) {
            ImageHandler.loadImageCircle2(itemView.context, shopImage, shopLogo)
        }
    }

    private fun setCashbackLabel(cashback: String?) {
        if (!cashback.isNullOrEmpty()) {
            labelCashbackPromo.let {
                it.text = String.format("%s", "Cashback $cashback%")
                it.setLabelType(Label.GENERAL_LIGHT_GREEN)
                it.show()
            }
        } else {
            labelCashbackPromo.hide()
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

