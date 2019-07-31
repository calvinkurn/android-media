package com.tokopedia.settingbank.addeditaccount.data

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.domain.pojo.EditBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.domain.pojo.ValidateBankAccountPojo
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Ade Fulki on 2019-05-16.
 * ade.hadian@tokopedia.com
 */

interface AddEditAccountApi{

    @FormUrlEncoded
    @POST(AddEditAccountUrl.PATH_VALIDATE_BANK_ACCOUNT)
    fun validateBankAccount(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<ValidateBankAccountPojo>>>

    @FormUrlEncoded
    @POST(AddEditAccountUrl.PATH_ADD_BANK_ACCOUNT)
    fun addBankAccount(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<AddBankAccountPojo>>>

    @FormUrlEncoded
    @POST(AddEditAccountUrl.PATH_EDIT_BANK_ACCOUNT)
    fun editBankAccount(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<EditBankAccountPojo>>>

}