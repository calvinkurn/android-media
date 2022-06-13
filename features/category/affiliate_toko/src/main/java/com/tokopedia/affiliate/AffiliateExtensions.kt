package com.tokopedia.affiliate

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.pojo.AffiliateTermsAndConditionData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateWebViewBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback


fun AffiliateTermsAndConditionFragment.createListForTermsAndCondition(context: Context?): ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
    val itemList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
    context?.let {
        itemList.add(AffiliateHeaderModel(AffiliateHeaderItemData(isForPortfolio = false)))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(
            R.string.affiliate_onboarding_terms_1))))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(
            R.string.affiliate_onboarding_terms_2))))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(
            R.string.affiliate_onboarding_terms_3))))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(
            R.string.affiliate_onboarding_terms_4))))
    }
    return itemList
}

fun View.hideKeyboard(context: Context?) {
    try {
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Ticker.setAnnouncementData(announcementData: AffiliateAnnouncementDataV2, context: FragmentActivity?) {
    when (announcementData.getAffiliateAnnouncementV2?.data?.type) {
        AffiliateBaseFragment.WARNING -> {
            setupTickerView(announcementData.getAffiliateAnnouncementV2?.data?.tickerData, this,
                Ticker.TYPE_WARNING, context)
        }
        AffiliateBaseFragment.ERROR -> {
            setupTickerView(announcementData.getAffiliateAnnouncementV2?.data?.tickerData, this,
                Ticker.TYPE_ERROR,
                context)
        }
        AffiliateBaseFragment.ANNOUNCEMENT -> {
            setupTickerView(announcementData.getAffiliateAnnouncementV2?.data?.tickerData, this,
                Ticker.TYPE_ANNOUNCEMENT,
                context)
        }
        else -> {
            setupTickerView(announcementData.getAffiliateAnnouncementV2?.data?.tickerData, this,
                Ticker.TYPE_INFORMATION,
                context)
        }
    }
}

private fun setupTickerView(tickerData: List<AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData?>?, view: Ticker?, type: Int, context: FragmentActivity?) {
    tickerData?.size?.let {
        if (it > 0) setTickerView(tickerData, view, type, context)
        else view?.hide()
    } ?: view?.hide()

}

private fun setTickerView(tickerData: List<AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData?>?, view: Ticker?, type: Int, context: FragmentActivity?) {
    val data = getTickerData(tickerData, type)
    val adapter = TickerPagerAdapter(context, data)
    view?.addPagerView(adapter, data)
    adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
            context?.supportFragmentManager?.let { fragmentManager ->
                AffiliateWebViewBottomSheet.newInstance("", linkUrl.toString())
                    .show(fragmentManager, "")
            }
        }

    })
}

private fun getTickerData(tickerData: List<AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData?>?, type: Int, ): ArrayList<TickerData> {
    val tempList = arrayListOf<TickerData>()
    tickerData?.forEach { ticker ->
        val title = ticker?.announcementTitle ?: ""
        val desc = ticker?.announcementDescription ?: ""
        val htmlDesc = desc + "<a href=\"${ticker?.ctaLink?.androidURL}\">${ticker?.ctaText}</a>"
        tempList.add(TickerData(title, htmlDesc, type, isFromHtml = true))
    }
    return tempList
}
