package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetEnum
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetUiModel
import javax.inject.Inject

class CreateCouponDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val mCouponTargetList = MutableLiveData<List<CouponTargetUiModel>>()
    val couponTargetList: LiveData<List<CouponTargetUiModel>>
        get() = mCouponTargetList

    private val mSelectedCouponTarget = MutableLiveData<CouponTargetEnum>()
    val selectedCouponTarget: LiveData<CouponTargetEnum>
        get() = mSelectedCouponTarget

    fun populateCouponTarget() {
        mCouponTargetList.value = listOf(
            CouponTargetUiModel(
                R.drawable.ic_im_umum,
                R.string.mvc_create_target_public,
                R.string.mvc_create_coupon_target_public_desc,
                CouponTargetEnum.PUBLIC,
                selected = false,
            ),
            CouponTargetUiModel(
                R.drawable.ic_im_terbatas,
                R.string.mvc_create_target_private,
                R.string.mvc_create_coupon_target_private_desc,
                CouponTargetEnum.PRIVATE,
                selected = false,
            ),
        )
    }

    fun setSelectedCouponTarget(couponTarget: CouponTargetEnum) {
        mSelectedCouponTarget.value = couponTarget
    }
}