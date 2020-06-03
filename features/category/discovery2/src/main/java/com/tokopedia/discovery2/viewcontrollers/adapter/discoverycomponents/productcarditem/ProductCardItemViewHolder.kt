package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.graphics.Color
import android.graphics.Paint
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
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem.ProductCardItemViewModel.Companion.OFFICIAL_STORE
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify


class ProductCardItemViewHolder(itemView: View, fragment: Fragment) : AbstractViewHolder(itemView) {

    private var productImage: ImageUnify = itemView.findViewById(R.id.imageProduct)
    private var topadsImage: ImageView = itemView.findViewById(R.id.imageTopAds)
    private var productName: TextView = itemView.findViewById(R.id.textViewProductName)
    private var labelDiscount: TextView = itemView.findViewById(R.id.labelDiscount)
    private var textViewSlashedPrice: TextView = itemView.findViewById(R.id.textViewSlashedPrice)
    private var textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    private var textViewShopName: TextView = itemView.findViewById(R.id.textViewShopName)
    private var textViewReviewCount: TextView = itemView.findViewById(R.id.textViewReviewCount)
    private var textViewShopLocation: TextView = itemView.findViewById(R.id.textViewShopLocation)
    private var labelCashbackPromo: Label = itemView.findViewById(R.id.labelCashBack)
    private var shopImage: ImageView = itemView.findViewById(R.id.imageShop)
    private var shopBadge: ImageView = itemView.findViewById(R.id.imageViewShopBadge)
    private var imageFreeOngkirPromo: ImageView = itemView.findViewById(R.id.imageFreeOngkirPromo)
    private var productCardView: CardView = itemView.findViewById(R.id.cardViewProductCard)
    private var stockPercentageProgress: ProgressBarUnify = itemView.findViewById(R.id.stockPercentageProgress)
    private var linearLayoutImageRating: LinearLayout = itemView.findViewById(R.id.linearLayoutImageRating)

    private var pdpViewImage: ImageView = itemView.findViewById(R.id.imageViewProductView)
    private var pdpViewCount: TextView = itemView.findViewById(R.id.textViewProductViewCount)
    private var stockTitle: TextView = itemView.findViewById(R.id.stockText)
    private var interestedView: TextView = itemView.findViewById(R.id.textViewProductInterestedView)

    private lateinit var productCardItemViewModel: ProductCardItemViewModel
    private var lifecycleOwner: LifecycleOwner = fragment.viewLifecycleOwner
    private var productCardName = ""


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as ProductCardItemViewModel
        initView()
    }

    private fun initView() {
        productCardItemViewModel.setContext(productCardView.context)
        productCardView.setOnClickListener {
            productCardItemViewModel.handleUIClick()
        }
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
        productCardName = productCardItemViewModel.getComponentName()

        productCardItemViewModel.getDataItemValue().observe(lifecycleOwner, Observer {
            populateData(it)
        })

        productCardItemViewModel.getShopBadge().observe(lifecycleOwner, Observer {
            when (it) {
                OFFICIAL_STORE -> shopBadge.setImageResource(R.drawable.discovery_official_store_icon)
                GOLD_MERCHANT -> shopBadge.setImageResource(R.drawable.discovery_gold_merchant_icon)
                else -> shopBadge.hide()
            }
        })
    }


    private fun populateData(dataItem: DataItem) {
        if (productCardName == "product_card_revamp_item" || productCardName == "product_card_carousel_item") {
            productName.setTextAndCheckShow(dataItem.name)
            setSlashedPrice(dataItem.discountedPrice)
            textViewPrice.setTextAndCheckShow(dataItem.price)
        } else {
            productName.setTextAndCheckShow(dataItem.title)
            setSlashedPrice(dataItem.price)
            textViewPrice.setTextAndCheckShow(dataItem.discountedPrice)
            setStockProgress(dataItem)
        }
        setLabelDiscount(dataItem.discountPercentage.toString())
        textViewShopName.setTextAndCheckShow(dataItem.shopName)
        setRating(dataItem.rating.toIntOrZero())
        setTextViewReviewCount(dataItem.countReview.toIntOrZero())
        setCashbackLabel(dataItem.cashback)
        textViewShopLocation.setTextAndCheckShow(dataItem.shopLocation)
        setShopIcon(dataItem.shopLogo)
        setProductImage(dataItem.imageUrlMobile)
        setTopads(dataItem.isTopads)
        showFreeOngKir(dataItem)
        setPDPView(dataItem)
        showInterestedView(dataItem)
    }


    private fun showFreeOngKir(dataItem: DataItem) {
        val freeOngkirImage = productCardItemViewModel.getFreeOngkirImage(dataItem)
        if (freeOngkirImage.isNotEmpty()) {
            ImageHandler.LoadImage(imageFreeOngkirPromo, freeOngkirImage)
            imageFreeOngkirPromo.show()
        } else {
            imageFreeOngkirPromo.hide()
        }
    }

    private fun setPDPView(dataItem: DataItem) {
        val pdpCount = productCardItemViewModel.getPDPViewCount(dataItem)
        if (pdpCount.isNotEmpty()) {
            pdpViewImage.show()
            pdpViewCount.show()
            pdpViewCount.setTextAndCheckShow(pdpCount)
        } else {
            pdpViewImage.hide()
            pdpViewCount.hide()
        }
    }

    private fun setStockProgress(dataItem: DataItem) {
        val stockSold = dataItem.stockSoldPercentage
        stockPercentageProgress.hide()
        stockTitle.hide()
        if (!stockSold.isNullOrEmpty()) {
            if (stockSold.toIntOrZero() < 100) {
                stockPercentageProgress.show()
                stockPercentageProgress.setValue(stockSold.toIntOrZero())
                showStockProgressTitle(dataItem)
            }
        }
    }


    private fun showStockProgressTitle(dataItem: DataItem) {
        val stockWording = productCardItemViewModel.getStockWord(dataItem)
        stockTitle.hide()
        if (!stockWording.title.isNullOrEmpty() && productCardName != "product_card_revamp_item" && productCardName != "product_card_carousel_item") {
            stockTitle.show()
            stockTitle.setTextAndCheckShow(stockWording.title)
            stockTitle.setTextColor(Color.parseColor(stockWording.color))
        }
    }

    private fun showInterestedView(dataItem: DataItem) {
        val interestCount = productCardItemViewModel.getInterestedCount(dataItem)
        interestedView.hide()
        if (interestCount.isNotEmpty()) {
            interestedView.show()
            interestedView.setTextAndCheckShow(interestCount)
        }
    }

    private fun setTopads(topads: Boolean?) {
        topadsImage.hide()
        if (topads!!) {
            topadsImage.show()
        }
    }

    private fun setProductImage(imageUrlMobile: String?) {
        productImage.loadImage(imageUrlMobile ?: "")
    }

    private fun setShopIcon(shopLogo: String?) {
        if (!shopLogo.isNullOrEmpty()) {
            ImageHandler.loadImageCircle2(itemView.context, shopImage, shopLogo)
        }
    }

    private fun setCashbackLabel(cashback: String?) {
        labelCashbackPromo.hide()
        if (!cashback.isNullOrEmpty()) {
            labelCashbackPromo.let {
                it.show()
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

