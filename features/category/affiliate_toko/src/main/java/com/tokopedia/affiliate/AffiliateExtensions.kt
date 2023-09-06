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
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.user.session.UserSession
import timber.log.Timber

fun AffiliateTermsAndConditionFragment.createListForTermsAndCondition(
    context: Context?
): ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
    val itemList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
    context?.let {
        itemList.add(AffiliateHeaderModel(AffiliateHeaderItemData(isForPortfolio = false)))
        itemList.add(
            AffiliateTermsAndConditionModel(
                AffiliateTermsAndConditionData(
                    context.getString(
                        R.string.affiliate_onboarding_terms_1
                    )
                )
            )
        )
        itemList.add(
            AffiliateTermsAndConditionModel(
                AffiliateTermsAndConditionData(
                    context.getString(
                        R.string.affiliate_onboarding_terms_2
                    )
                )
            )
        )
        itemList.add(
            AffiliateTermsAndConditionModel(
                AffiliateTermsAndConditionData(
                    context.getString(
                        R.string.affiliate_onboarding_terms_3
                    )
                )
            )
        )
        itemList.add(
            AffiliateTermsAndConditionModel(
                AffiliateTermsAndConditionData(
                    context.getString(
                        R.string.affiliate_onboarding_terms_4
                    )
                )
            )
        )
    }
    return itemList
}

fun View.hideKeyboard(context: Context?) {
    try {
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: IllegalStateException) {
        Timber.e(e)
    } catch (e: ClassCastException) {
        Timber.e(e)
    }
}

fun Ticker.setAnnouncementData(
    announcementData: AffiliateAnnouncementDataV2,
    context: FragmentActivity?,
    source: Int = PAGE_ANNOUNCEMENT_ALL
) {
    when (announcementData.getAffiliateAnnouncementV2?.announcementData?.type) {
        AffiliateBaseFragment.WARNING -> {
            setupTickerView(
                source,
                announcementData.getAffiliateAnnouncementV2?.announcementData,
                this,
                Ticker.TYPE_WARNING,
                context
            )
        }

        AffiliateBaseFragment.ERROR -> {
            setupTickerView(
                source,
                announcementData.getAffiliateAnnouncementV2?.announcementData,
                this,
                Ticker.TYPE_ERROR,
                context
            )
        }

        AffiliateBaseFragment.ANNOUNCEMENT -> {
            setupTickerView(
                source,
                announcementData.getAffiliateAnnouncementV2?.announcementData,
                this,
                Ticker.TYPE_ANNOUNCEMENT,
                context
            )
        }

        else -> {
            setupTickerView(
                source,
                announcementData.getAffiliateAnnouncementV2?.announcementData,
                this,
                Ticker.TYPE_INFORMATION,
                context
            )
        }
    }
}

private fun setupTickerView(
    source: Int,
    data: AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data?,
    view: Ticker?,
    type: Int,
    context: FragmentActivity?
) {
    data?.tickerData?.size?.let {
        if (it > 0) {
            setTickerView(source, data, view, type, context)
        } else {
            view?.hide()
        }
    } ?: view?.hide()
}

private fun setTickerView(
    source: Int,
    data: AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data?,
    view: Ticker?,
    type: Int,
    context: FragmentActivity?
) {
    val tickers = getTickerData(data?.tickerData, type)
    val adapter = TickerPagerAdapter(context, tickers)
    view?.addPagerView(adapter, tickers)
    adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
            context?.supportFragmentManager?.let { fragmentManager ->
                AffiliateWebViewBottomSheet.newInstance("", linkUrl.toString())
                    .show(fragmentManager, "")
            }
            if (data?.id.isMoreThanZero()) {
                val userSession = UserSession(context)
                var item = ""
                var category = ""
                var position = PAGE_ANNOUNCEMENT_ALL
                when (source) {
                    PAGE_ANNOUNCEMENT_HOME -> {
                        item = AffiliateAnalytics.ItemKeys.AFFILIATE_HOME_TICKER_COMMUNICATION
                        category = AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE
                        position = PAGE_ANNOUNCEMENT_HOME
                    }

                    PAGE_ANNOUNCEMENT_PROMO_PERFORMA -> {
                        item = AffiliateAnalytics.ItemKeys.AFFILIATE_PROMOSIKAN_TICKER_COMMUNICATION
                        category = AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE
                        position = PAGE_ANNOUNCEMENT_HOME
                    }

                    PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY -> {
                        item = AffiliateAnalytics.ItemKeys.AFFILIATE_PENDAPATAN_TICKER_COMMUNICATION
                        category = AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE
                        position = PAGE_ANNOUNCEMENT_HOME
                    }
                }
                AffiliateAnalytics.sendTickerEvent(
                    AffiliateAnalytics.EventKeys.SELECT_CONTENT,
                    AffiliateAnalytics.ActionKeys.CLICK_TICKER_COMMUNICATION,
                    category,
                    "${data?.type} - ${data?.id}",
                    position,
                    data?.id!!,
                    item,
                    userSession.userId
                )
            }
        }
    })
}

private fun getTickerData(
    tickerData: List<AffiliateAnnouncementDataV2.GetAffiliateAnnouncementV2.Data.TickerData?>?,
    type: Int
): ArrayList<TickerData> {
    val tempList = arrayListOf<TickerData>()
    tickerData?.forEach { ticker ->
        val title = ticker?.announcementTitle ?: ""
        val desc = ticker?.announcementDescription ?: ""
        val htmlDesc = desc + "<a href=\"${ticker?.ctaLink?.androidURL}\"> ${ticker?.ctaText}</a>"
        tempList.add(TickerData(title, htmlDesc, type, isFromHtml = true))
    }
    return tempList
}
