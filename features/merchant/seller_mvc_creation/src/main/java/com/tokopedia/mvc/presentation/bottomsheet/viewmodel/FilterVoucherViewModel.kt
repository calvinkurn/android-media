package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherSource
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.list.model.FilterModel
import javax.inject.Inject

class FilterVoucherViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _filterData = MutableLiveData<FilterModel>()
    val filterData: LiveData<FilterModel> get() = _filterData

    fun setupFilterData(filter: FilterModel) {
        _filterData.value = filter
    }

    fun setStatusFilter(status: VoucherStatus) {
        if (status == VoucherStatus.PROCESSING) {
            _filterData.getValueOrDefault().status.apply {
                clear()
                add(VoucherStatus.NOT_STARTED)
                add(VoucherStatus.ONGOING)
                add(VoucherStatus.ENDED)
                add(VoucherStatus.STOPPED)
            }
        } else {
            _filterData.getValueOrDefault().status.apply {
                clear()
                add(status)
            }
        }
        _filterData.triggerChange()
    }

    fun setVoucherType(type: VoucherServiceType) {
        _filterData.getValueOrDefault().voucherType.toggleData(type)
        _filterData.triggerChange()
    }

    fun setPromoType(type: PromoType) {
        _filterData.getValueOrDefault().promoType.apply {
            clear()
            add(type)
        }
        _filterData.triggerChange()
    }

    fun setSource(source: VoucherSource) {
        _filterData.getValueOrDefault().source.apply {
            clear()
            add(source)
        }
        _filterData.triggerChange()
    }

    fun setTarget(target: VoucherTarget) {
        _filterData.getValueOrDefault().target.toggleData(target)
        _filterData.triggerChange()
    }

    fun setTargetBuyer(targetBuyer: VoucherTargetBuyer) {
        _filterData.getValueOrDefault().targetBuyer.toggleData(targetBuyer)
        _filterData.triggerChange()
    }

    fun MutableLiveData<FilterModel>.getValueOrDefault(): FilterModel {
        return value ?: FilterModel()
    }

    fun MutableLiveData<FilterModel>.triggerChange() {
        value = _filterData.value
    }

    fun resetSelection() {
        _filterData.value = FilterModel()
    }

    fun <E> MutableList<E>.toggleData(data: E) {
        if (data in this) {
            remove(data)
        } else {
            add(data)
        }
    }
}
