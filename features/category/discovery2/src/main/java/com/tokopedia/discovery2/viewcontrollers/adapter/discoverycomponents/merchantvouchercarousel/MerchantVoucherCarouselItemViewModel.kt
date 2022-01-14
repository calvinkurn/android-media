package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.mvcwidget.multishopmvc.data.CatalogMVCWithProductsListItem
import com.tokopedia.mvcwidget.multishopmvc.data.DataMapperMultiShopView.map
import com.tokopedia.mvcwidget.multishopmvc.data.MultiShopModel
import java.lang.Exception

class MerchantVoucherCarouselItemViewModel(application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val _multiShopData = MutableLiveData<MultiShopModel>()
    val multiShopModel:LiveData<MultiShopModel> = _multiShopData


    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.data?.firstOrNull()?.let {
            _multiShopData.value = mapToShopModel(it)
        }
    }

    private fun mapToShopModel(dataItem: DataItem):MultiShopModel{
        val catalogItem  = CatalogMVCWithProductsListItem(dataItem.shopInfo,dataItem.subtitle,dataItem.title,dataItem.maximumBenefitAmountStr,null,dataItem.products)
        return  map(catalogItem)
    }
}