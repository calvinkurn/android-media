package com.tokopedia.tokopoints.view.validatePin

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.ErrorMessageException
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Dispatchers
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class ValidateMerchantPinViewModel @Inject constructor(private val mUseCase: SwipeCouponUseCase) : BaseViewModel(Dispatchers.Main), ValidateMerchantPinContract.Presenter {

    val swipeCouponLiveData = MutableLiveData<Resources<CouponSwipeUpdate>>()
    override fun swipeMyCoupon(code: String, pin: String) {
        launchCatchError(block = {
            val data = mUseCase.execute(code,pin)
            if (data.swipeCoupon != null) {
                if (data.swipeCoupon.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                    swipeCouponLiveData.value = Success(data.swipeCoupon)
                } else {
                    if (data.swipeCoupon.resultStatus.messages.size > 0) {
                        swipeCouponLiveData.value = ErrorMessage(data.swipeCoupon.resultStatus.messages[0])
                    }
                }
            }
        }){}

    }

}