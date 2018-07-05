package com.tokopedia.settingbank.choosebank.domain.usecase

import com.raizlabs.android.dbflow.config.FlowManager
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.core.database.DbFlowDatabase
import com.tokopedia.core.database.model.Bank
import com.tokopedia.settingbank.choosebank.data.BankListApi
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListWSMapper
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListWSUseCase(val api: BankListApi,
                           val mapper: GetBankListWSMapper) : UseCase<BankListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankListViewModel> {
        return api.getBankList(requestParams.parameters)
                .map(mapper)
                .map { t -> saveToDB(t, requestParams) }

    }

    private fun saveToDB(bankListViewModel: BankListViewModel, requestParams: RequestParams): BankListViewModel {

        val database = FlowManager.getDatabase(DbFlowDatabase.NAME).writableDatabase
        database.beginTransaction()
        try {
            val bankList = mapToDBModel(bankListViewModel.list!!)
            for (bank in bankList) {
                bank.setBankPage(requestParams.parameters[PARAM_PAGE] as Int)
                bank.save()
            }
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }

        return bankListViewModel
    }

    private fun mapToDBModel(list: ArrayList<BankViewModel>): List<Bank> {
        val listBank = ArrayList<Bank>()

        for (bankModel: BankViewModel in list) {
            val bank = Bank()
            bank.setBankId(bankModel.bankId)
            bank.setBankName(bankModel.bankName)
            listBank.add(bank)
        }
        return listBank
    }


    companion object {

        private val PARAM_PROFILE_USER_ID: String = "profile_user_id"
        val PARAM_PAGE: String = "page"
        val PARAM_KEYWORD: String = "keyword"


        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_DEVICE_ID: String = "device_id"
        private val PARAM_HASH: String = "hash"
        private val PARAM_OS_TYPE: String = "os_type"
        private val PARAM_DEVICE_TIME: String = "device_time"

        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(
                keyword: String,
                page: Int,
                userId: String,
                deviceId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            val hash = AuthUtil.md5("$userId~$deviceId")

            requestParams.putString(PARAM_PROFILE_USER_ID, userId)
            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putString(PARAM_KEYWORD, keyword)

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_DEVICE_ID, deviceId)
            requestParams.putString(PARAM_HASH, hash)
            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            requestParams.putString(PARAM_DEVICE_TIME, (Date().time / 1000).toString())

            return requestParams
        }
    }
}