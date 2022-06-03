package com.tokopedia.affiliate.ui.custom

import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter

abstract class AffiliateBaseFragment< T : BaseViewModel> : BaseViewModelFragment<T>(){
    companion object{
        const val WARNING = "warning"
        const val ERROR = "error"
        const val ANNOUNCEMENT = "announcement"
    }
    fun onGetValidateUserData(validateUserdata: AffiliateValidateUserData?) {
        if(validateUserdata?.validateAffiliateUserStatus?.data?.isRegistered == true &&
            validateUserdata.validateAffiliateUserStatus.data?.isReviewed == false &&
            validateUserdata.validateAffiliateUserStatus.data?.isSystemDown == false) onUserRegistered()
        else if(validateUserdata?.validateAffiliateUserStatus?.data?.isSystemDown == true) onSystemDown()
        else if(validateUserdata?.validateAffiliateUserStatus?.data?.isReviewed == true) onReviewed()
    }

    abstract fun onSystemDown()
    abstract fun onReviewed()
    abstract fun onUserRegistered()
}