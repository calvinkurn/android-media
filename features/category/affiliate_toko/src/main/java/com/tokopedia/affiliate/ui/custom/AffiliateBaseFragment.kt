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
    fun onGetValidateUserData(validateUserdata: AffiliateValidateUserData?) {
        if(validateUserdata?.validateAffiliateUserStatus?.data?.isEligible == false) onUserNotEligible()
        else if(validateUserdata?.validateAffiliateUserStatus?.data?.isRegistered == true &&
            validateUserdata.validateAffiliateUserStatus.data?.isReviewed == false) onUserRegistered()
        else if(validateUserdata?.validateAffiliateUserStatus?.data?.isSystemDown == true) onSystemDown()
        else if(validateUserdata?.validateAffiliateUserStatus?.data?.isReviewed == true) onReviewed()
    }

    fun onGetAnnouncementData(announcementData: AffiliateAnnouncementDataV2,view: Ticker?) {
        when(announcementData.getAffiliateAnnouncementV2?.data?.type){
            "warning" ->{
                setupTickerView(announcementData.getAffiliateAnnouncementV2?.data?.tickerData,view,Ticker.TYPE_WARNING)
            }
        }
    }
    private fun setupTickerView(
        tickerData: List<AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData?>?,
        view: Ticker?,
        type: Int, ) {
        tickerData?.size?.let {
            if(it>0) setTickerView(tickerData,view,type)
            else view?.hide()
        } ?: view?.hide()

    }

    private fun setTickerView(tickerData: List<AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData?>?, view: Ticker?, type: Int, ) {
        view?.showContextMenu()
        val data = arrayListOf<TickerData>()
        tickerData?.forEach {ticker ->
            val title = ticker?.announcementTitle ?: ""
            val desc = ticker?.announcementDescription ?: ""

            val htmlDesc = desc + "<a href=\"${ticker?.ctaLink}\">${ticker?.ctaText}</a>"
            data.add(TickerData(title,htmlDesc,type,isFromHtml = true))
        } ?: view?.hide()
        view?.addPagerView(TickerPagerAdapter(context, data),data)
    }

    abstract fun onSystemDown()
    abstract fun onReviewed()
    abstract fun onUserNotEligible()
    abstract fun onUserRegistered()
}