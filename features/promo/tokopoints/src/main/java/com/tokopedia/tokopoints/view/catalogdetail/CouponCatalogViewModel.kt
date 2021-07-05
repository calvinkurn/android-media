package com.tokopedia.tokopoints.view.catalogdetail

import androidx.lifecycle.MutableLiveData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.di.BundleModule
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.cataloglisting.CatalogPurchaseRedeemptionViewModel
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import javax.inject.Inject
import javax.inject.Named

@TokoPointScope
class CouponCatalogViewModel @Inject constructor(private val repository: CouponCatalogRepository, @Named(BundleModule.tp_send_gift_failed_title) val tp_send_gift_failed_title: String, @Named(BundleModule.tp_send_gift_failed_message) val tp_send_gift_failed_message: String) : CatalogPurchaseRedeemptionViewModel(repository), CouponCatalogContract.Presenter {

    val catalogDetailLiveData = MutableLiveData<Resources<CatalogsValueEntity>>()
    val sendGiftPageLiveData = MutableLiveData<Resources<SendGiftPage>>()
    val latestStatusLiveData = MutableLiveData<CatalogStatusItem>()

    override fun getCatalogDetail(uniqueCatalogCode: String) {
        launchCatchError(block = {
            catalogDetailLiveData.value = Loading()
            val response = repository.getcatalogDetail(uniqueCatalogCode)
            val data = response.getData<CatalogDetailOuter>(CatalogDetailOuter::class.java)
            data?.let { catalogDetailLiveData.value = Success(data.detail!!) }
        }) {
            catalogDetailLiveData.value = ErrorMessage(it.localizedMessage)
        }
    }

    override fun fetchLatestStatus(catalogsIds: List<Int>) {
        launchCatchError(block = {
            val data = repository.fetchLatestStatus(catalogsIds)
            if (data.catalogStatus != null) { //For detail page we only interested in one item
                latestStatusLiveData.value = data.catalogStatus.catalogs[0]
            }
        }) {}
    }

    override fun startSendGift(id: Int, title: String?, pointStr: String?, banner: String?) {
        launchCatchError(block = {
            val data = repository.startSendGift(id)
            if (data.preValidateRedeem != null && data.preValidateRedeem.isValid == 1) {
                sendGiftPageLiveData.value = Success(SendGiftPage(id, title, pointStr, banner))
            } else throw NullPointerException()
        }) {

            if (it is MessageErrorException) {
                var errorTitle = tp_send_gift_failed_title
                var errorMessage = tp_send_gift_failed_message
                val errors = it.message?.split(",")
                if (errors != null && errors.size > 0) {
                    val mesList = errors[0].split("|").toTypedArray()
                    if (mesList.size >= 2) {
                        errorTitle = mesList[0]
                        errorMessage = mesList[1]
                    } else if (mesList.size >= 1) {
                        errorMessage = mesList[0]
                    }
                }
                sendGiftPageLiveData.value = ValidationError(PreValidateError(errorTitle, errorMessage))
            }
        }
    }

}

data class SendGiftPage(val id: Int, val title: String?, val pointStr: String?, val banner: String?)
data class PreValidateError(val title: String, val message: String)
