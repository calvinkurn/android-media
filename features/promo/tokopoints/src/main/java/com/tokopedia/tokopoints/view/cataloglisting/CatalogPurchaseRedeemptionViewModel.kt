package com.tokopedia.tokopoints.view.cataloglisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokopoints.view.model.CatalogGqlError
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import com.tokopedia.tokopoints.view.util.ValidationError
import kotlinx.coroutines.Dispatchers

open class CatalogPurchaseRedeemptionViewModel(private val repository: CatalogPurchaseRedeemptionRepository) : BaseViewModel(Dispatchers.Main), CatalogPurchaseRedemptionPresenter {

    val startValidateCouponLiveData = MutableLiveData<ValidateMessageDialog>()
    val startSaveCouponLiveData = MutableLiveData<Resources<ConfirmRedeemDialog>>()
    val onRedeemCouponLiveData = MutableLiveData<String>()

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
            startSaveCouponLiveData.value = Success(ConfirmRedeemDialog(redeemCouponBaseEntity.hachikoRedeem.coupons?.get(0)?.cta,
                redeemCouponBaseEntity.hachikoRedeem.coupons?.get(0)?.code,
                redeemCouponBaseEntity.hachikoRedeem.coupons?.get(0)?.title,
                redeemCouponBaseEntity.hachikoRedeem.coupons?.get(0)?.description,
                redeemCouponBaseEntity.hachikoRedeem.redeemMessage

            ))
        }) {
            if (it is CatalogGqlError) {
                var responseCode = 0
                val errorsMessage = it.developerMessage?.split(",")?.get(0)?.split("|")?.toTypedArray()
                val onlyDigits: Boolean = errorsMessage.last().matches(Regex("[0-9]+"))
                if (onlyDigits) {
                    responseCode = errorsMessage.last().toIntOrZero()
                }
                val detailedErrorMessage = it.messageErrorException.message ?: ""
                    startSaveCouponLiveData.value = ValidationError(ValidateMessageDialog(detailedErrorMessage ,responseCode))
                }
            }
        }
    }

data class ValidateMessageDialog( val desc: String, val messageCode: Int)
data class ConfirmRedeemDialog(val cta: String?, val code: String?, val title: String?, val description: String ?, val redeemMessage:String?)
