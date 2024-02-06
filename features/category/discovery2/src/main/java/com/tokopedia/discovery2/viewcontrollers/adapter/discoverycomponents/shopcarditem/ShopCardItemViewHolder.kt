package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcarditem

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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

class ShopCardItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mShopCardItemViewModel: ShopCardItemViewModel? = null
    private var shopCardDataItem: DataItem? = null

    private val parentLayout = itemView.findViewById<CardUnify>(R.id.parentLayout)

    private val imageShop = itemView.findViewById<ImageUnify>(R.id.imageShop)
    private val shopLogo = itemView.findViewById<ImageUnify>(R.id.shop_logo)
    private val shopSubLogo = itemView.findViewById<ImageUnify>(R.id.shop_sub_logo)
    private val benefitSymbolImage = itemView.findViewById<ImageUnify>(R.id.benefit_symbol_image)
    private val timerLogo = itemView.findViewById<ImageUnify>(R.id.timer_logo)

    private val tvShopName = itemView.findViewById<Typography>(R.id.tv_shop_name)
    private val tvHeaderTitle = itemView.findViewById<Typography>(R.id.tv_header_title)
    private val tvTimer = itemView.findViewById<Typography>(R.id.tv_timer)
    private val benefitAmount = itemView.findViewById<Typography>(R.id.benefit_amount)
    private val benefitSymbol = itemView.findViewById<Typography>(R.id.benefit_symbol)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopCardItemViewModel = discoveryBaseViewModel as ShopCardItemViewModel
        initView()
    }

    private fun initView() {
        parentLayout.setOnClickListener {
            handleUIClick(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            mShopCardItemViewModel?.getComponentLiveData()?.observe(lifecycle) { item ->
                item.data?.firstOrNull()?.let {
                    shopCardDataItem = it
                    populateData(it)
                }
            }
            mShopCardItemViewModel?.getSyncPageLiveData()?.observe(lifecycle) {
                if (it) (fragment as DiscoveryFragment).reSync()
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardItemViewModel?.getComponentLiveData()?.removeObservers(it)
            mShopCardItemViewModel?.getSyncPageLiveData()?.removeObservers(it)
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
                tvShopName.invisible()
            } else {
                tvShopName.show()
                tvShopName.text = MethodChecker.fromHtml(dataItem.shopName)
            }
            if (dataItem.benefitTitle.isNullOrEmpty()) {
                tvHeaderTitle.invisible()
            } else {
                tvHeaderTitle.show()
                tvHeaderTitle.text = dataItem.benefitTitle
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
                tvTimer.show()
                tvTimer.text = dataItem.timeDescription
            } else {
                tvTimer.invisible()
                timerLogo.invisible()
            }
        } catch (exception: Exception) {
            parentLayout.hide()
            exception.printStackTrace()
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            parentLayout -> {
                mShopCardItemViewModel?.navigate(fragment.context, shopCardDataItem?.applinks)
                sendClickEvent()
            }
        }
    }

    private fun sendClickEvent() {
        mShopCardItemViewModel?.components?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackEventClickShopCard(it)
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        mShopCardItemViewModel?.components?.let {
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackShopCardImpression(it)
        }
    }
}
