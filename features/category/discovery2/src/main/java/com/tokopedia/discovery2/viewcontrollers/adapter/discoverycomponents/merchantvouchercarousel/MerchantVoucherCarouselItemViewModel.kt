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
//           Todo:: Try to call this
//            mapToCatalogMVCWithProductsListItem()
            _multiShopData.value = mapToShopModel(it)
        }
    }


    private fun mapToCatalogMVCWithProductsListItem(dataItem: DataItem):CatalogMVCWithProductsListItem?{
//        Todo:: Find a better way to map
        return try {
            Gson().fromJson(Gson().toJson(dataItem), CatalogMVCWithProductsListItem::class.java)
        }catch (e:Exception){
            null
        }
    }

    private fun mapToShopModel(item: DataItem):MultiShopModel{
        val catalogItem  = mapToCatalogMVCWithProductsListItem(item)
        return if(catalogItem != null){
            map(catalogItem)
        }else
            MultiShopModel(
    //            Todo: confirm do we need to pick icon from icon url or shopStatusIconUrl ??
                shopIcon = item.shopInfo?.shopStatusIconURL?:"",
                shopName = item.shopInfo?.name?:"",
                products = item.products,
                cashBackTitle = item.title ?:"",
                cashBackValue = item.maximumBenefitAmountStr ?:"",
                couponCount = item.subtitle ?:"",
                id = item.shopInfo?.id?:"",
                applink = item.shopInfo?.appLink?:"",
                AdInfo = null,
            )
    }
}