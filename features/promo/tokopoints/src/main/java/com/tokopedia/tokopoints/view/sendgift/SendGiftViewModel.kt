package com.tokopedia.tokopoints.view.sendgift

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Dispatchers
import java.lang.NullPointerException
import javax.inject.Inject

class SendGiftViewModel @Inject constructor(private val repository: SendGiftRespository) : BaseViewModel(Dispatchers.Main), SendGiftContract.Presenter {
    private var success = 0

    val sendGiftLiveData = MutableLiveData<Resources<SendGiftData<out Any, out Any>>>()
    val prevalidateLiveData = MutableLiveData<Resources<Nothing?>>()

    override fun sendGift(id: Int?, email: String, notes: String) {
        launchCatchError(block = {
            sendGiftLiveData.value = Loading()
            val redeemCouponBaseEntity = repository.sendGift(id, email, notes)
            if (redeemCouponBaseEntity.hachikoRedeem != null) {
                val title = R.string.tp_send_gift_success_title
                val message = R.string.tp_send_gift_success_message
                success = 1
                sendGiftLiveData.value = Success(SendGiftData(title = title, messsage = message, success = success))
            } else throw NullPointerException()
        }) {
            //show error
            if (it is MessageErrorException) {
                val errors = it.message?.split(",")
                var title: String? = null
                var message: String? = null
                success = 0
                if (errors != null && errors.size > 0) {
                    val mesList = errors[0].split("|").toTypedArray()
                    if (mesList.size == 3) {
                        title = mesList[0]
                        message = mesList[1]
                    } else if (mesList.size == 2) {
                        message = mesList[0]
                    }
                }
                sendGiftLiveData.value = Success(SendGiftData(title
                        ?: R.string.tp_send_gift_failed_title, message
                        ?: R.string.tp_send_gift_failed_message, success))
            } else {
                sendGiftLiveData.value = ErrorMessage("")
            }
        }
    }

    override fun preValidateGift(id: Int?, email: String) {
        launchCatchError(block = {
            prevalidateLiveData.value = Loading()
            val validateCoupon = repository.preValidateGift(id, email)
            if (validateCoupon.validateCoupon != null && validateCoupon.validateCoupon.isValid == 1) {
                prevalidateLiveData.value = Success(null)
            } else throw NullPointerException()
        }) {
            var msg : String? = null
            if (it is MessageErrorException) {
                val errorsMessage = it.message?.split(",")?.get(0)?.split("|")?.toTypedArray()
                msg = errorsMessage?.get(0)?.split("]")?.toTypedArray()?.get(1)
            }
            prevalidateLiveData.value = ErrorMessage(msg ?: "")
        }
    }

}

data class SendGiftData<T, E>(val title: T, val messsage: E, val success: Int)