package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.util.constant.CommonConstant
import javax.inject.Inject

class VoucherEditCalendarViewModel  @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel(dispatchers.main) {

    private val _isCoachMarkShown = MutableLiveData<Boolean>()
    val isCoachMarkShown: LiveData<Boolean>
        get() = _isCoachMarkShown

    fun checkToShowCoachmark() {
        _isCoachMarkShown.value = isCoachMarkShown()
    }

    private fun isCoachMarkShown(): Boolean {
        return sharedPreferences.getBoolean(
            CommonConstant.SHARED_PREF_VOUCHER_CREATION_EDIT_PERIOD_COACH_MARK,
            false
        )
    }

    fun setSharedPrefCoachMarkAlreadyShown() {
        sharedPreferences.edit()
            .putBoolean(CommonConstant.SHARED_PREF_VOUCHER_CREATION_EDIT_PERIOD_COACH_MARK, true)
            .apply()
    }
}
