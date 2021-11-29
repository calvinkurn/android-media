package com.tokopedia.updateinactivephone.stub.di

import com.tokopedia.updateinactivephone.common.AndroidFileUtil
import com.tokopedia.updateinactivephone.domain.data.*
import com.tokopedia.updateinactivephone.stub.domain.usecase.*
import com.tokopedia.updateinactivephone.test.R
import javax.inject.Inject

class FakeInactivePhoneDependency {

    @Inject
    lateinit var getAccountListUseCaseStub: GetAccountListUseCaseStub

    @Inject
    lateinit var getStatusInactivePhoneNumberUseCaseStub: GetStatusInactivePhoneNumberUseCaseStub

    @Inject
    lateinit var phoneValidationUseCaseStub: PhoneValidationUseCaseStub

    @Inject
    lateinit var submitDataUseCaseStub: SubmitDataUseCaseStub

    @Inject
    lateinit var submitExpeditedInactivePhoneUseCaseStub: SubmitExpeditedInactivePhoneUseCaseStub

    @Inject
    lateinit var verifyNewPhoneUseCaseStub: VerifyNewPhoneUseCaseStub

    var accountListDataModel = AccountListDataModel()
    var statusInactivePhoneNumberDataModel = StatusInactivePhoneNumberDataModel()
    var phoneValidationDataModel = PhoneValidationDataModel()
    var inactivePhoneSubmitDataModel = InactivePhoneSubmitDataModel()
    var submitExpeditedInactivePhoneDataModel = SubmitExpeditedDataModel()
    var verifyNewPhoneDataModel = VerifyNewPhoneDataModel()

    /*
     * Default response = success response
     */
    fun setDefaultResponse() {
        accountListDataModel = AndroidFileUtil.parseRaw(
            R.raw.account_list_success,
            AccountListDataModel::class.java
        )

        statusInactivePhoneNumberDataModel = AndroidFileUtil.parseRaw(
            R.raw.get_status_inactive_phone_number,
            StatusInactivePhoneNumberDataModel::class.java
        )

        phoneValidationDataModel = AndroidFileUtil.parseRaw(
            R.raw.validate_inactive_phone_number,
            PhoneValidationDataModel::class.java
        )

        inactivePhoneSubmitDataModel = AndroidFileUtil.parseRaw(
            R.raw.submit_inactive_phone_user,
            InactivePhoneSubmitDataModel::class.java
        )

        submitExpeditedInactivePhoneDataModel = AndroidFileUtil.parseRaw(
            R.raw.submit_expedited_inactive_phone,
            SubmitExpeditedDataModel::class.java
        )

        verifyNewPhoneDataModel = AndroidFileUtil.parseRaw(
            R.raw.verify_new_phone_inactive_phone_user,
            VerifyNewPhoneDataModel::class.java
        )
    }
}