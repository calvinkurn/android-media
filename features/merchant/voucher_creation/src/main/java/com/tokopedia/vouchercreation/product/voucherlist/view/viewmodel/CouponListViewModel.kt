package com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.NonNullLiveData
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.product.voucherlist.view.bottomsheet.CouponFilterBottomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.LIST_COUPON_PER_PAGE
import com.tokopedia.vouchercreation.product.voucherlist.view.mapper.CouponModelMapper.mapToTarget
import com.tokopedia.vouchercreation.product.voucherlist.view.mapper.CouponModelMapper.mapToType
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
    private val _selectedFilterType = NonNullLiveData(CouponFilterBottomSheet.FilterType.NOT_SELECTED)
    private val _selectedFilterTarget = NonNullLiveData(CouponFilterBottomSheet.FilterTarget.NOT_SELECTED)
    private var _isFreeBroadCastIconVisible: Boolean = false
    private var _isSuccessDialogDisplayed: Boolean = false
    private var _couponStatusFilter: String = VoucherStatus.NOT_STARTED_AND_ONGOING
    private var _couponSearchKeyword: String? = null

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
    val selectedFilterType: LiveData<CouponFilterBottomSheet.FilterType>
        get() = _selectedFilterType
    val selectedFilterTarget: LiveData<CouponFilterBottomSheet.FilterTarget>
        get() = _selectedFilterTarget
    val isFreeBroadCastIconVisible: Boolean
        get() = _isFreeBroadCastIconVisible
    val isSuccessDialogDisplayed: Boolean
        get() = _isSuccessDialogDisplayed
    val couponSearchKeyword: String?
        get() = _couponSearchKeyword

    fun setIsFreeBroadCastIconVisible(broadCastQuota: Int) {
        _isFreeBroadCastIconVisible = broadCastQuota.isMoreThanZero()
    }

    fun setIsSuccessDialogDisplayed(isDisplayed: Boolean) {
        _isSuccessDialogDisplayed = isDisplayed
    }

    fun setStatusFilter(@VoucherStatus couponStatus: String) {
        _couponStatusFilter = couponStatus
    }

    fun setCouponSearchKeyword(keyword: String) {
        _couponSearchKeyword = keyword
    }

    fun setSelectedFilterType(selectedType: CouponFilterBottomSheet.FilterType) {
        _selectedFilterType.value = selectedType
    }

    fun setSelectedFilterTarget(selectedTarget: CouponFilterBottomSheet.FilterTarget) {
        _selectedFilterTarget.value = selectedTarget
    }

    fun getCouponList(page: Int) {
        launchCatchError(block = {
            val ongoingVoucherRequestParam = VoucherListParam.createParamCouponList(
                type = mapToType(_selectedFilterType.value),
                target = mapToTarget(_selectedFilterTarget.value),
                status = _couponStatusFilter,
                page = page,
                perPage = LIST_COUPON_PER_PAGE,
                voucherName = _couponSearchKeyword
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