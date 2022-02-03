package com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.shop.detail.domain.usecase.VoucherDetailUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.remote.ChatBlastSellerMetadata
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVoucherListUseCase: GetVoucherListUseCase,
    private val cancelVoucherUseCase: CancelVoucherUseCase,
    private val getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase,
    private val voucherDetailUseCase: VoucherDetailUseCase,
    private val shopBasicDataUseCase: ShopBasicDataUseCase,
) : BaseViewModel(dispatchers.main) {

    private val _couponList = MutableLiveData<Result<List<VoucherUiModel>>>()
    private val _cancelCoupon = MutableLiveData<Result<Int>>()
    private val _stopCoupon = MutableLiveData<Result<Int>>()
    private val _broadCastMetaData = MutableLiveData<Result<ChatBlastSellerMetadata>>()
    private val _detailCoupon = MutableLiveData<Result<VoucherUiModel>>()
    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataResult>>()

    val couponList: LiveData<Result<List<VoucherUiModel>>>
        get() = _couponList
    val cancelCoupon: LiveData<Result<Int>>
        get() = _cancelCoupon
    val stopCoupon: LiveData<Result<Int>>
        get() = _stopCoupon
    val broadCastMetadata: LiveData<Result<ChatBlastSellerMetadata>>
        get() = _broadCastMetaData
    val detailCoupon: LiveData<Result<VoucherUiModel>>
        get() = _detailCoupon
    val shopBasicData: LiveData<Result<ShopBasicDataResult>>
        get() = _shopBasicData

    private var isFreeBroadCastIconVisible: Boolean = false
    private var isSuccessDialogDisplayed: Boolean = false

    fun setIsFreeBroadCastIconVisible(broadCastQuota: Int) {
        isFreeBroadCastIconVisible = (broadCastQuota > 0)
    }

    fun setIsSuccessDialogDisplayed(isDisplayed: Boolean) {
        this.isSuccessDialogDisplayed = isDisplayed
    }

    fun getIsFreeBroadCastIconVisible(): Boolean = isFreeBroadCastIconVisible

    fun getIsSuccessDialogDisplayed(): Boolean = isSuccessDialogDisplayed

    fun getVoucherList() {
        launchCatchError(block = {
            val ongoingVoucherRequestParam = VoucherListParam.createParamCouponList(
                status = VoucherStatus.NOT_STARTED_AND_ONGOING
            )
            _couponList.value = Success(withContext(dispatchers.io) {
                getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(ongoingVoucherRequestParam)
                getVoucherListUseCase.executeOnBackground()
            })

        }, onError = {
            _couponList.value = Fail(it)
        })
    }

    fun cancelCoupon(couponId: Int, @CancelVoucherUseCase.CancelStatus status: String) {
        val liveData = if (status == CancelVoucherUseCase.CancelStatus.STOP) {
            _stopCoupon
        } else {
            _cancelCoupon
        }

        launchCatchError(block = {
            cancelVoucherUseCase.params = CancelVoucherUseCase.createRequestParam(couponId, status)
            liveData.value = Success(withContext(dispatchers.io) {
                cancelVoucherUseCase.executeOnBackground()
            })
        }, onError = {
            liveData.value = Fail(it)
        })
    }

    fun getBroadCastMetaData() {
        launchCatchError(block = {
            _broadCastMetaData.value = Success(withContext(dispatchers.io) {
                getBroadCastMetaDataUseCase.executeOnBackground()
            })
        }, onError = {
            _broadCastMetaData.value = Fail(it)
        })
    }

    fun getDetailCoupon(couponId: Int) {
        launchCatchError(block = {
            voucherDetailUseCase.params = VoucherDetailUseCase.createRequestParam(couponId)
            _detailCoupon.value = Success(withContext(dispatchers.io) {
                voucherDetailUseCase.executeOnBackground()
            })
        }, onError = {
            _detailCoupon.value = Fail(it)
        })
    }

    fun getShopBasicData() {
        launchCatchError(block = {
            _shopBasicData.value = Success(withContext(dispatchers.io) {
                shopBasicDataUseCase.executeOnBackground()
            })
        }, onError = {
            _shopBasicData.value = Fail(it)
        })
    }
}