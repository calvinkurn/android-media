package com.tokopedia.tokopoints.view.catalogdetail

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.BundleModule
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import rx.Subscriber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@TokoPointScope
class CouponCatalogViewModel @Inject constructor(private val repository: CouponCatalogRepository,@Named(BundleModule.tp_send_gift_failed_title) val tp_send_gift_failed_title : String,@Named(BundleModule.tp_send_gift_failed_message) val tp_send_gift_failed_message : String ) : BaseViewModel(Dispatchers.Main), CouponCatalogContract.Presenter, CatalogPurchaseRedemptionPresenter {

    val catalogDetailLiveData = MutableLiveData<Resources<CatalogsValueEntity>>()
    val sendGiftPageLiveData = MutableLiveData<Resources<SendGiftPage>>()
    val startValidateCouponLiveData = MutableLiveData<ValidateMessageDialog>()
    val latestStatusLiveData = MutableLiveData<CatalogStatusItem>()
    val startSaveCouponLiveData = MutableLiveData<Resources<ConfirmRedeemDialog>>()
    val onRedeemCouponLiveData = MutableLiveData<String>()
    val pointQueryLiveData = MutableLiveData<Resources<String>>()


    override fun startValidateCoupon(item: CatalogsValueEntity) {
        launchCatchError(block = {
            val validateResponseCode: Int
            val message: String
            val title: String
            val validateCoupon = repository.startValidateCoupon(item.id)
            if ( validateCoupon.validateCoupon != null) {
                validateResponseCode = CommonConstant.CouponRedemptionCode.SUCCESS
                message = validateCoupon.validateCoupon.messageSuccess
                title = validateCoupon.validateCoupon.messageTitle
                startValidateCouponLiveData.value = ValidateMessageDialog(item,title, message, validateResponseCode)
            }
        }) {
            if (it is MessageErrorException) {
                val errorsMessage = it.message?.split(",")?.get(0)?.split("|")?.toTypedArray()
                if (errorsMessage != null && errorsMessage.size >= 3) {
                    val title = errorsMessage[0]
                    val message = errorsMessage[1]
                    val validateResponseCode = errorsMessage[2].toInt()
                    startValidateCouponLiveData.value = ValidateMessageDialog(item,title, message, validateResponseCode)
                }
            }
        }
    }

    override fun redeemCoupon(promoCode: String, cta: String) {
        launchCatchError(block = {
            repository.redeemCoupon(promoCode)
            onRedeemCouponLiveData.value = cta
        }) {
            onRedeemCouponLiveData.value = cta
        }
    }

    override fun startSaveCoupon(item: CatalogsValueEntity) {
        launchCatchError(block = {
            val redeemCouponBaseEntity = repository.startSaveCoupon(item.id)
            if (redeemCouponBaseEntity != null && redeemCouponBaseEntity.hachikoRedeem != null) {
                startSaveCouponLiveData.value = Success(ConfirmRedeemDialog(redeemCouponBaseEntity.hachikoRedeem.coupons[0].cta,
                        redeemCouponBaseEntity.hachikoRedeem.coupons[0].code,
                        redeemCouponBaseEntity.hachikoRedeem.coupons[0].title))
            }
        }) {
            if (it is MessageErrorException) {
                val errorsMessage = it.message?.split(",")?.get(0)?.split("|")?.toTypedArray()
                if (errorsMessage != null && errorsMessage.size > 0) {
                    var desc: String? = null
                    var title: String = errorsMessage[0]
                    var validateResponseCode = 0
                    if (errorsMessage.size == 1) {
                        val rawString = errorsMessage[0].split(".").toTypedArray()
                        if (rawString.size >= 2) {
                            val rawTitle = rawString[0]
                            val rawDesc = rawString[1]

                            if (rawTitle.length > 0) {
                                title = rawTitle
                            }
                            if (rawDesc.length > 0) {
                                desc = rawDesc
                            }
                        }
                    }
                    if (errorsMessage.size >= 2) {
                        desc = errorsMessage[1]
                    }
                    if (errorsMessage.size >= 3) validateResponseCode = errorsMessage[2].toInt()
                    startSaveCouponLiveData.value = ValidationError(ValidateMessageDialog(item, title , desc ?: "",validateResponseCode))
                }
            }
        }


    }

    override fun showRedeemCouponDialog(cta: String?, code: String?, title: String?) {
    }

    override fun getCatalogDetail(uniqueCatalogCode: String) {
        launchCatchError(block = {
            catalogDetailLiveData.value = Loading()
            val response = repository.getcatalogDetail(uniqueCatalogCode)
            val data = response.getData<CatalogDetailOuter>(CatalogDetailOuter::class.java)
            data?.let { catalogDetailLiveData.value = Success(data.detail!!) }
            handlePointQuery(response.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java))
        }) {
            catalogDetailLiveData.value = ErrorMessage(it.toString())
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


    private fun handlePointQuery(pointDetailEntity: TokoPointDetailEntity?) { //Handling the point
        if (pointDetailEntity == null || pointDetailEntity.tokoPoints == null || pointDetailEntity.tokoPoints.resultStatus == null || pointDetailEntity.tokoPoints.status == null || pointDetailEntity.tokoPoints.status.points == null) {
            pointQueryLiveData.value = ErrorMessage("")
        } else {
            if (pointDetailEntity.tokoPoints.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                pointQueryLiveData.value = Success(pointDetailEntity.tokoPoints.status.points.rewardStr)
            }
        }
    }

    override fun startSendGift(id: Int, title: String, pointStr: String, banner: String) {
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

data class SendGiftPage(val id: Int, val title: String, val pointStr: String, val banner: String)
data class PreValidateError(val title: String, val message: String)
data class ValidateMessageDialog(val item: CatalogsValueEntity, val title: String, val desc: String, val messageCode: Int)
data class ConfirmRedeemDialog(val cta : String, val code : String, val title : String)