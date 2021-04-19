package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster


private const val OFFICIAL_STORE = 1
private const val GOLD_MERCHANT = 2
private const val SOLD_PERCENTAGE_UPPER_LIMIT = 100
private const val SOLD_PERCENTAGE_LOWER_LIMIT = 0
private const val SALE_PRODUCT_STOCK = 100
private const val PRODUCT_STOCK = 0
private const val PRODUCT_CAROUSEL_WIDTH = 2.3

class ProductCardItemViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var productImage: ImageUnify = itemView.findViewById(R.id.imageProduct)
    private var topadsTextView: TextView = itemView.findViewById(R.id.textViewTopAds)
    private var productName: TextView = itemView.findViewById(R.id.textViewProductName)
    private var labelDiscount: TextView = itemView.findViewById(R.id.labelDiscount)
    private var textViewSlashedPrice: TextView = itemView.findViewById(R.id.textViewSlashedPrice)
    private var textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    private var textViewShopLocation: TextView = itemView.findViewById(R.id.textViewShopLocation)
    private var shopBadge: ImageView = itemView.findViewById(R.id.imageViewShopBadge)
    private var imageFreeOngkirPromo: ImageView = itemView.findViewById(R.id.imageFreeOngkirPromo)
    private var productCardView: CardView = itemView.findViewById(R.id.cardViewProductCard)
    private var stockPercentageProgress: ProgressBar = itemView.findViewById(R.id.stockPercentageProgress)
    private var pdpViewImage: ImageView = itemView.findViewById(R.id.imageViewProductViewCount)
    private var pdpViewCount: TextView = itemView.findViewById(R.id.textViewProductViewCount)
    private var stockTitle: TextView = itemView.findViewById(R.id.stockText)
    private var interestedView: TextView = itemView.findViewById(R.id.textViewProductInterestedView)
    private var notifyMeView: TextView = itemView.findViewById(R.id.textViewNotifyMe)
    private var linearLayoutImageRating: LinearLayout = itemView.findViewById(R.id.linearLayoutImageRating)
    private var textViewReviewCount: TextView = itemView.findViewById(R.id.textViewReviewCount)
    private var statusLabel: Label = itemView.findViewById(R.id.statusLabel)
    private var priceLabel: Label = itemView.findViewById(R.id.cashback_labelPromo)
    private var outOfStockOverlay: View = itemView.findViewById(R.id.outOfStockOverlay)
    private var componentPosition: Int? = null
    private lateinit var productCardItemViewModel: ProductCardItemViewModel
    private var productCardName = ""
    private var context: Context? = fragment.activity
    private var dataItem: DataItem? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardItemViewModel = discoveryBaseViewModel as ProductCardItemViewModel
        getSubComponent().inject(productCardItemViewModel)
        initView()
    }

    private fun initView() {
        productCardItemViewModel.setContext(productCardView.context)
        productCardView.setOnClickListener {
            handleUIClick(it)
        }
        notifyMeView.setOnClickListener {
            handleUIClick(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        productCardName = productCardItemViewModel.getComponentName()
        lifecycleOwner?.let { lifecycleOwner ->
            productCardItemViewModel.getDataItemValue().observe(lifecycleOwner, Observer {
                dataItem = it
                populateData(it)
            })

            productCardItemViewModel.getShowLoginData().observe(lifecycleOwner, Observer {
                if (it == true) {
                    componentPosition?.let { position -> (fragment as DiscoveryFragment).openLoginScreen(position) }
                }
            })
            productCardItemViewModel.notifyMeCurrentStatus().observe(lifecycleOwner, Observer {
                updateNotifyMeState(it)
            })

            productCardItemViewModel.showNotifyToastMessage().observe(lifecycleOwner, Observer {
                showNotifyResultToast(it)
            })
            productCardItemViewModel.getComponentPosition().observe(lifecycleOwner, Observer {
                componentPosition = it
            })
            productCardItemViewModel.getSyncPageLiveData().observe(lifecycleOwner, Observer {
                if (it) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            productCardItemViewModel.getDataItemValue().removeObservers(it)
            productCardItemViewModel.notifyMeCurrentStatus().removeObservers(it)
            productCardItemViewModel.showNotifyToastMessage().removeObservers(it)
            productCardItemViewModel.getShowLoginData().removeObservers(it)
            productCardItemViewModel.getComponentPosition().removeObservers(it)
        }
    }

    private fun populateData(dataItem: DataItem) {
        if (productCardName == ComponentNames.ProductCardRevampItem.componentName || productCardName == ComponentNames.ProductCardCarouselItem.componentName) {
            productName.setTextAndCheckShow(dataItem.name)
            setSlashedPrice(dataItem.discountedPrice)
            textViewPrice.setTextAndCheckShow(dataItem.price)
            showOutOfStockLabel(dataItem.stock, PRODUCT_STOCK)
        } else {
            productName.setTextAndCheckShow(dataItem.title)
            setSlashedPrice(dataItem.price)
            textViewPrice.setTextAndCheckShow(dataItem.discountedPrice)
            setStockProgress(dataItem.stockSoldPercentage)
            showOutOfStockLabel(dataItem.stockSoldPercentage, SALE_PRODUCT_STOCK)
        }
        carouselProductWidth()
        setLabelDiscount(dataItem.discountPercentage.toString())
        dataItem.rating?.let { setRating(it, dataItem.countReview) }
        setProductImage(dataItem.imageUrlMobile)
        setTopads(dataItem.isTopads)
        showShopBadgeUI(dataItem)
        showFreeOngKir(dataItem)
        setPDPView(dataItem)
        showInterestedView(dataItem)
        showNotifyMe(dataItem)
        priceLabel.initLabelGroup(dataItem.getLabelPrice())
        showStatusLabel(dataItem)
    }

    private fun carouselProductWidth() {
        if(productCardName == ComponentNames.ProductCardCarouselItem.componentName || productCardName == ComponentNames.ProductCardSprintSaleCarouselItem.componentName){
            val displayMetrics = getDisplayMetric(context)
            productCardView.layoutParams.width = (displayMetrics.widthPixels/PRODUCT_CAROUSEL_WIDTH).toInt()
        }
    }

    private fun showStatusLabel(dataItem: DataItem) {
        if (statusLabel.visibility == View.GONE) {
            statusLabel.initLabelGroup(dataItem.getLabelProductStatus())
        }
    }

    private fun getDisplayMetric(context: Context?): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    private fun showOutOfStockLabel(productStock: String?, saleStockValidation: Int = 0) {
        when (saleStockValidation) {
            productStock?.toIntOrNull() -> {
                statusLabel.apply {
                    unlockFeature = true
                    val colorHexString = "#${Integer.toHexString(ContextCompat.getColor(context, R.color.clr_AD31353B))}"
                    statusLabel.setLabelType(colorHexString)
                    show()
                }
                outOfStockOverlay.show()
            }
            else -> {
                statusLabel.hide()
                outOfStockOverlay.hide()
            }
        }
    }

    private fun showShopBadgeUI(dataItem: DataItem) {
        when (productCardItemViewModel.chooseShopBadge()) {
            OFFICIAL_STORE -> {
                shopBadge.show()
                shopBadge.setImageResource(R.drawable.discovery_official_store_icon)
            }
            GOLD_MERCHANT -> {
                shopBadge.show()
                shopBadge.setImageResource(R.drawable.discovery_gold_merchant_icon)
            }
            else -> shopBadge.hide()
        }
        if (!dataItem.shopLocation.isNullOrEmpty()) {
            textViewShopLocation.setTextAndCheckShow(dataItem.shopLocation)
        } else if (!dataItem.shopName.isNullOrEmpty()) {
            textViewShopLocation.setTextAndCheckShow(dataItem.shopName)
        } else {
            textViewShopLocation.hide()
        }
    }

    private fun showNotifyMe(dataItem: DataItem) {
        if (productCardItemViewModel.notifyMeVisibility()) {
            dataItem.notifyMe?.let {
                notifyMeView.show()
                updateNotifyMeState(it)
            }
        } else {
            notifyMeView.hide()
        }
    }

    private fun updateNotifyMeState(notifyMeStatus: Boolean) {
        if (notifyMeStatus) {
            notifyMeActiveState()
        } else {
            notifyMeInActiveState()
        }
    }

    private fun notifyMeActiveState() {
        notifyMeView.text = itemView.context.getString(R.string.active_reminder_text)
        notifyMeView.setTextColor(MethodChecker.getColor(itemView.context, R.color.clr_ae31353b))
        notifyMeView.setBackgroundResource(R.drawable.productcard_module_bg_button_active)
    }

    private fun notifyMeInActiveState() {
        notifyMeView.text = itemView.context.getString(R.string.remind_me_text)
        notifyMeView.setTextColor(MethodChecker.getColor(itemView.context, R.color.greenColor))
        notifyMeView.setBackgroundResource(R.drawable.productcard_module_bg_button_inactive)
    }

    private fun showFreeOngKir(dataItem: DataItem) {
        val freeOngkirImage = productCardItemViewModel.getFreeOngkirImage(dataItem)
        if (freeOngkirImage.isNotEmpty()) {
            imageFreeOngkirPromo.loadIcon(freeOngkirImage)
            imageFreeOngkirPromo.show()
        } else {
            imageFreeOngkirPromo.hide()
        }
    }

    private fun setPDPView(dataItem: DataItem) {
        val pdpCount = productCardItemViewModel.getPDPViewCount(dataItem)
        if (pdpCount.isNotEmpty()) {
            pdpViewImage.show()
            pdpViewCount.setTextAndCheckShow(pdpCount)
        } else {
            pdpViewImage.hide()
            pdpViewCount.hide()
        }
    }


    private fun setStockProgress(stockSoldPercentage: String?) {
        if (stockSoldPercentage?.toIntOrNull() == null || stockSoldPercentage.isEmpty()) {
            hideStockProgressUI()
        } else {
            if (stockSoldPercentage.toIntOrZero() in (SOLD_PERCENTAGE_LOWER_LIMIT) until SOLD_PERCENTAGE_UPPER_LIMIT) {
                stockPercentageProgress.progress = stockSoldPercentage.toIntOrZero()
                stockPercentageProgress.show()
                showStockProgressTitle()
            } else {
                hideStockProgressUI()
            }
        }
    }

    private fun hideStockProgressUI() {
        stockPercentageProgress.hide()
        stockTitle.hide()
    }

    private fun showStockProgressTitle() {
        val stockWording = productCardItemViewModel.getStockWord()
        if (stockWording.title.isNullOrEmpty()) {
            stockTitle.hide()
        } else {
            stockTitle.setTextAndCheckShow(stockWording.title)
            if (!stockWording.color.isNullOrEmpty()) {
                stockTitle.setTextColor(Color.parseColor(stockWording.color))
            }
        }
    }

    private fun showInterestedView(dataItem: DataItem) {
        val interestCount = productCardItemViewModel.getInterestedCount(dataItem)
        interestedView.hide()
        if (interestCount.isNotEmpty()) {
            interestedView.setTextAndCheckShow(interestCount)
        }
    }

    private fun setTopads(topads: Boolean?) {
        if (topads == true) {
            topadsTextView.show()
        } else {
            topadsTextView.hide()
        }
    }

    private fun setProductImage(imageUrlMobile: String?) {
        productImage.loadImage(imageUrlMobile ?: "")
    }

    private fun setLabelDiscount(text: String) {
        if (text.isEmpty() || text == "0") {
            labelDiscount.hide()
        } else {
            labelDiscount.show()
            labelDiscount.text = String.format("%s", "$text%")
        }
    }

    private fun setSlashedPrice(discountedPrice: String?) {
        if (!discountedPrice.isNullOrEmpty()) {
            textViewSlashedPrice.setTextAndCheckShow(discountedPrice)
            textViewSlashedPrice.paintFlags = textViewSlashedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textViewSlashedPrice.hide()
        }
    }

    private fun setRating(rating: String, countReview: String?) {
        val ratingData = rating.toIntOrZero()
        if (ratingData in 1..5) {
            for (r in 0 until ratingData) {
                linearLayoutImageRating.show()
                (linearLayoutImageRating.getChildAt(r) as ImageView).setImageResource(R.drawable.product_card_ic_rating_active)
            }
            setTextViewReviewCount(countReview)

        } else {
            linearLayoutImageRating.hide()
        }
    }

    private fun setTextViewReviewCount(reviewCount: String?) {
        reviewCount?.let {
            if (it.toIntOrZero() > 0) {
                textViewReviewCount.show()
                textViewReviewCount.text = String.format("%s", "($it)")
            } else {
                textViewReviewCount.hide()
            }
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            productCardView -> {
                productCardItemViewModel.sendTopAdsClick()
                productCardItemViewModel.handleNavigation()
                sendClickEvent()
            }
            notifyMeView -> {
                sentNotifyButtonEvent()
                productCardItemViewModel.subscribeUser()
            }
        }
    }

    private fun sentNotifyButtonEvent() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackNotifyClick(productCardItemViewModel.components, productCardItemViewModel.isUserLoggedIn(),productCardItemViewModel.getUserID())
    }

    private fun showNotifyResultToast(toastData: Triple<Boolean, String?, Int?>) {
        try {
            if (!toastData.first && !toastData.second.isNullOrEmpty()) {
                Toaster.make(itemView.rootView, toastData.second!!, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
            } else if (!toastData.second.isNullOrEmpty()) {
                Toaster.make(itemView.rootView, toastData.second!!, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendClickEvent() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackProductCardClick(productCardItemViewModel.components, productCardItemViewModel.isUserLoggedIn())
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        productCardItemViewModel.sendTopAdsView()
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().viewProductsList(productCardItemViewModel.components, productCardItemViewModel.isUserLoggedIn())
    }
}