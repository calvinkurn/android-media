package com.tokopedia.thankyou_native.domain.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.thankyou_native.domain.model.ThankPageTopTickerData
import com.tokopedia.thankyou_native.domain.model.Tickers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class TopTickerUseCase @Inject constructor() : UseCase<ThankPageTopTickerData>() {

    private val PARAM_THANKS_PAGE_DATA = "param_thanks_page_data"

    fun getTopTickerData(configList: String?, onSuccess: (ThankPageTopTickerData) -> Unit,
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

    override suspend fun executeOnBackground(): ThankPageTopTickerData {
        val configList = useCaseRequestParams.getString(PARAM_THANKS_PAGE_DATA, null)
                ?: throw NullPointerException()
        val tickers = Gson().fromJson<Tickers>(configList, Tickers::class.java)
        val listType = object : TypeToken<List<ThankPageTopTickerData>>() {}.type
        val topTickerDataList = Gson().fromJson<List<ThankPageTopTickerData>>(tickers.tickerDataListStr?:"[]", listType)
        if (topTickerDataList.isNotEmpty()) {
            return topTickerDataList[0]
        } else
            throw NullPointerException()
    }
}