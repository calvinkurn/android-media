package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcarditem

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.ShopCardItemLayoutBinding
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import java.lang.Exception

class ShopCardItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding: ShopCardItemLayoutBinding = ShopCardItemLayoutBinding.bind(itemView)
    private var mShopCardItemViewModel: ShopCardItemViewModel? = null
    private var shopCardDataItem: DataItem? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopCardItemViewModel = discoveryBaseViewModel as ShopCardItemViewModel
        initView()
    }

    private fun initView() {
        binding.parentLayout.setOnClickListener {
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
            with(binding) {
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
            }
        } catch (exception: Exception) {
            binding.parentLayout.hide()
            exception.printStackTrace()
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            binding.parentLayout -> {
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
        mShopCardItemViewModel?.components?.let { (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackShopCardImpression(it) }
    }
}
