package com.tokopedia.thankyou_native.domain.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.thankyou_native.domain.model.ThankPageTopTickerData
import com.tokopedia.thankyou_native.domain.model.Tickers
import com.tokopedia.thankyou_native.presentation.fragment.ThankYouBaseFragment
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class TopTickerUseCase @Inject constructor() : UseCase<List<TickerData>>() {

    private val PARAM_THANKS_PAGE_DATA = "param_thanks_page_data"

    fun getTopTickerData(configList: String?, onSuccess: (List<TickerData>) -> Unit,
                         onFail: (Throwable) -> Unit) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_THANKS_PAGE_DATA, configList)
        }
        execute({
            onSuccess(it)
        }, {
            onFail(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): List<TickerData> {
        val configList = useCaseRequestParams.getString(PARAM_THANKS_PAGE_DATA, null)
                ?: throw NullPointerException()
        val tickers = Gson().fromJson<Tickers>(configList, Tickers::class.java)
        val listType = object : TypeToken<List<ThankPageTopTickerData>>() {}.type
        val topTickerDataList = Gson().fromJson<List<ThankPageTopTickerData>>(tickers.tickerDataListStr
                ?: "[]", listType)
        if (topTickerDataList.isNotEmpty()) {
            return topTickerDataList.map {
                TickerData(title = it.tickerTitle,
                        description = getHtmlDescription(it.tickerDescription,
                                it.tickerCTATitle, it.getURL()),
                        type = when (it.ticketType) {
                            ThankYouBaseFragment.TICKER_WARNING -> Ticker.TYPE_WARNING
                            ThankYouBaseFragment.TICKER_INFO -> Ticker.TYPE_INFORMATION
                            ThankYouBaseFragment.TICKER_ERROR -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }, isFromHtml = it.tickerCTAUrl != null, itemData = it)
            }
        } else
            throw NullPointerException()
    }

    private fun getHtmlDescription(description: String?, ctaTitle: String?, url: String?): String {
        return if (url == null || ctaTitle == null)
            description ?: ""
        else
            "$description <a href=\"$url\">$ctaTitle</a>"
    }
}