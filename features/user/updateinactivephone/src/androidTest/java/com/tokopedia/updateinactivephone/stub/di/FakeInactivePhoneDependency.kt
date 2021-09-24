package com.tokopedia.updateinactivephone.stub.di

import com.tokopedia.updateinactivephone.common.AndroidFileUtil
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.*
import com.tokopedia.updateinactivephone.stub.domain.usecase.*
import javax.inject.Inject

class FakeInactivePhoneDependency {

    @Inject
    lateinit var getAccountListUseCaseStub: GetAccountListUseCaseStub

    @Inject
    lateinit var getStatusInactivePhoneNumberUseCaseStub: GetStatusInactivePhoneNumberUseCaseStub

//    @Inject
//    lateinit var imageUploadUseCaseStub: ImageUploadUseCaseStub

    @Inject
    lateinit var phoneValidationUseCaseStub: PhoneValidationUseCaseStub

    @Inject
    lateinit var submitDataUseCaseStub: SubmitDataUseCaseStub

    @Inject
    lateinit var submitExpeditedInactivePhoneUseCaseStub: SubmitExpeditedInactivePhoneUseCaseStub

    var accountListDataModel = AccountListDataModel()
    var statusInactivePhoneNumberDataModel = StatusInactivePhoneNumberDataModel()
    var phoneValidationDataModel = PhoneValidationDataModel()
    var inactivePhoneSubmitDataModel = InactivePhoneSubmitDataModel()
    var submitExpeditedInactivePhoneDataModel = SubmitExpeditedInactivePhoneDataModel()

    /*
     * Default response = success response
     */
    fun setDefaultResponse() {
        accountListDataModel = AndroidFileUtil.parseRaw(
            InactivePhoneConstant.JsonRaw.ACCOUNT_LIST,
            AccountListDataModel::class.java
        )

        statusInactivePhoneNumberDataModel = AndroidFileUtil.parseRaw(
            InactivePhoneConstant.JsonRaw.STATUS_INACTIVE_PHONE,
            StatusInactivePhoneNumberDataModel::class.java
        )

        phoneValidationDataModel = AndroidFileUtil.parseRaw(
            InactivePhoneConstant.JsonRaw.PHONE_VALIDATION,
            PhoneValidationDataModel::class.java
        )

        inactivePhoneSubmitDataModel = AndroidFileUtil.parseRaw(
            InactivePhoneConstant.JsonRaw.SUBMIT_REGULAR_DATA,
            InactivePhoneSubmitDataModel::class.java
        )

        submitExpeditedInactivePhoneDataModel = AndroidFileUtil.parseRaw(
            InactivePhoneConstant.JsonRaw.SUBMIT_EXPEDITED_DATA,
            SubmitExpeditedInactivePhoneDataModel::class.java
        )
    }
}