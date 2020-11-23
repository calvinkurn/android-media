package com.tokopedia.tokopoints.view.cataloglisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import com.tokopedia.tokopoints.view.util.ValidationError
import kotlinx.coroutines.Dispatchers

open class CatalogPurchaseRedeemptionViewModel(private val repository: CatalogPurchaseRedeemptionRepository) : BaseViewModel(Dispatchers.Main), CatalogPurchaseRedemptionPresenter {

    val startValidateCouponLiveData = MutableLiveData<ValidateMessageDialog>()
    val startSaveCouponLiveData = MutableLiveData<Resources<ConfirmRedeemDialog>>()
    val onRedeemCouponLiveData = MutableLiveData<String>()

    override fun startValidateCoupon(item: CatalogsValueEntity) {
        launchCatchError(block = {
            val validateResponseCode: Int
            val message: String
            val title: String
            val validateCoupon = repository.startValidateCoupon(item.id)
            if (validateCoupon.validateCoupon != null) {
                validateResponseCode = CommonConstant.CouponRedemptionCode.SUCCESS
                message = validateCoupon.validateCoupon.messageSuccess
                title = validateCoupon.validateCoupon.messageTitle
                startValidateCouponLiveData.value = ValidateMessageDialog(item, title, message, validateResponseCode)
            }
        }) {
            if (it is MessageErrorException) {
                val errorsMessage = it.message?.split(",")?.get(0)?.split("|")?.toTypedArray()
                if (errorsMessage != null && errorsMessage.isNotEmpty()) {
                    var desc: String? = null
                    var title: String? = errorsMessage[0]
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
                    if (errorsMessage.size == 2) {
                        title = ""
                        desc = errorsMessage[0]
                        validateResponseCode = Integer.parseInt(errorsMessage[1])
                    }
                    if (errorsMessage.size == 3) {
                        desc=errorsMessage[1]
                        validateResponseCode = Integer.parseInt(errorsMessage[2])
                    }
                    startValidateCouponLiveData.value = ValidateMessageDialog(item, title, desc
                            ?: "", validateResponseCode)
                }
            }
        }
    }

    override fun redeemCoupon(promoCode: String?, cta: String?) {
        launchCatchError(block = {
            if (promoCode != null) {
                repository.redeemCoupon(promoCode)
            }
            onRedeemCouponLiveData.value = cta
        }) {
            onRedeemCouponLiveData.value = cta
        }
    }

    override fun startSaveCoupon(item: CatalogsValueEntity) {
        launchCatchError(block = {
            val redeemCouponBaseEntity = repository.startSaveCoupon(item.id)
            if (redeemCouponBaseEntity != null && redeemCouponBaseEntity.hachikoRedeem != null) {
                startSaveCouponLiveData.value = Success(ConfirmRedeemDialog(redeemCouponBaseEntity.hachikoRedeem?.coupons?.get(0)?.cta,
                        redeemCouponBaseEntity.hachikoRedeem.coupons?.get(0)?.code,
                        redeemCouponBaseEntity.hachikoRedeem.coupons?.get(0)?.title,
                        redeemCouponBaseEntity.hachikoRedeem.coupons?.get(0)?.description,
                        redeemCouponBaseEntity.hachikoRedeem.redeemMessage

                ))
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
                    startSaveCouponLiveData.value = ValidationError(ValidateMessageDialog(item, title, desc
                            ?: "", validateResponseCode))
                }
            }
        }
    }

    override fun showRedeemCouponDialog(cta: String?, code: String?, title: String?) {
    }
}

data class ValidateMessageDialog(val item: CatalogsValueEntity, val title: String?, val desc: String, val messageCode: Int)
data class ConfirmRedeemDialog(val cta: String?, val code: String?, val title: String?, val description: String ?, val redeemMessage:String?)