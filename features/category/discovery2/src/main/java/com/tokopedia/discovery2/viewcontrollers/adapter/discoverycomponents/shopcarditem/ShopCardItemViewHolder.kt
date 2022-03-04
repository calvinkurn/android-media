package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcarditem

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import java.lang.Exception

class ShopCardItemViewHolder(itemView: View, val fragment: Fragment) :
        AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var mShopCardItemViewModel: ShopCardItemViewModel
    private var parentCardView: CardUnify = itemView.findViewById(R.id.parentLayout)
    private var imageShop: ImageUnify = itemView.findViewById(R.id.imageShop)
    private var shopLogo: ImageUnify = itemView.findViewById(R.id.shop_logo)
    private var shopSubLogo: ImageUnify = itemView.findViewById(R.id.shop_sub_logo)
    private var shopNameTextView: Typography = itemView.findViewById(R.id.tv_shop_name)
    private var headerTitleTextView: Typography = itemView.findViewById(R.id.tv_header_title)
    private var benefitSymbol: Typography = itemView.findViewById(R.id.benefit_symbol)
    private var benefitAmount: Typography = itemView.findViewById(R.id.benefit_amount)
    private var benefitSymbolImage: ImageUnify = itemView.findViewById(R.id.benefit_symbol_image)
    private var timerLogo: ImageUnify = itemView.findViewById(R.id.timer_logo)
    private var timerTextView: Typography = itemView.findViewById(R.id.tv_timer)
    private var shopCardDataItem: DataItem? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopCardItemViewModel = discoveryBaseViewModel as ShopCardItemViewModel
        initView()
    }

    private fun initView() {
        parentCardView.setOnClickListener {
            handleUIClick(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            mShopCardItemViewModel.getComponentLiveData().observe(lifecycle, { item ->
                item.data?.firstOrNull()?.let {
                        shopCardDataItem = it
                        populateData(it)
                }
            })
            mShopCardItemViewModel.getSyncPageLiveData().observe(lifecycle, {
                if (it) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardItemViewModel.getComponentLiveData().removeObservers(it)
            mShopCardItemViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }

    private fun populateData(dataItem: DataItem) {
        try {
            imageShop.loadImage(dataItem.products?.firstOrNull()?.imageURL)
            shopLogo.loadImage(dataItem.shopLogo)
            if (dataItem.shopBadgeImageUrl.isNullOrEmpty()) {
                shopSubLogo.invisible()
            } else {
                shopSubLogo.show()
                shopSubLogo.loadImageWithoutPlaceholder(dataItem.shopBadgeImageUrl)
            }

            if (dataItem.shopName.isNullOrEmpty()) {
                shopNameTextView.invisible()
            } else {
                shopNameTextView.show()
                shopNameTextView.text = dataItem.shopName
            }
            if (dataItem.benefitTitle.isNullOrEmpty()) {
                headerTitleTextView.invisible()
            } else {
                headerTitleTextView.show()
                headerTitleTextView.text = dataItem.benefitTitle
            }
            if (dataItem.showBenefitCurrency == true) {
                benefitSymbol.show()
                benefitSymbol.text = "Rp"
            } else {
                benefitSymbol.hide()
            }
            if (dataItem.benefitAmount.isNullOrEmpty()) {
                benefitAmount.hide()
            } else {
                benefitAmount.show()
                benefitAmount.text = dataItem.benefitAmount
            }
            if (dataItem.benefitSymbolImageUrl.isNullOrEmpty()) {
                benefitSymbolImage.hide()
            } else {
                benefitSymbolImage.show()
                benefitSymbolImage.loadImageWithoutPlaceholder(dataItem.benefitSymbolImageUrl)
            }
            if (dataItem.showTimer == true) {
                timerLogo.show()
                timerTextView.show()
                timerTextView.text = dataItem.timeDescription
            } else {
                timerTextView.invisible()
                timerLogo.invisible()
            }
        } catch (exception: Exception) {
            parentCardView.hide()
            exception.printStackTrace()
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            parentCardView -> {
                mShopCardItemViewModel.navigate(fragment.context, shopCardDataItem?.applinks)
                sendClickEvent()
            }
        }
    }

    private fun sendClickEvent() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackEventClickShopCard(mShopCardItemViewModel.components)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackShopCardImpression(mShopCardItemViewModel.components)
    }


}