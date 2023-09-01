package com.tokopedia.manageaddress.ui.manageaddress

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.mapper.TargetedTickerMapper.convertTargetedTickerToUiModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.model.TickerModel
import com.tokopedia.logisticCommon.domain.param.GetTargetedTickerParam
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.manageaddress.domain.mapper.EligibleAddressFeatureMapper
import com.tokopedia.manageaddress.domain.model.DefaultAddressParam
import com.tokopedia.manageaddress.domain.model.DeleteAddressParam
import com.tokopedia.manageaddress.domain.model.EligibleForAddressFeatureModel
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.domain.request.shareaddress.ValidateShareAddressAsReceiverParam
import com.tokopedia.manageaddress.domain.request.shareaddress.ValidateShareAddressAsSenderParam
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressData
import com.tokopedia.manageaddress.domain.response.SetDefaultPeopleAddressResponse
import com.tokopedia.manageaddress.domain.usecase.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ValidateShareAddressAsReceiverUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ValidateShareAddressAsSenderUseCase
import com.tokopedia.manageaddress.ui.uimodel.ValidateShareAddressState
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.userconsent.common.CollectionPointDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst
import com.tokopedia.usercomponents.userconsent.common.UserConsentPayload
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.collection.GetConsentCollectionUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ManageAddressViewModel @Inject constructor(
    private val getPeopleAddressUseCase: GetAddressCornerUseCase,
    private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase,
    private val setDefaultPeopleAddressUseCase: SetDefaultPeopleAddressUseCase,
    private val chooseAddressRepo: ChooseAddressRepository,
    private val chooseAddressMapper: ChooseAddressMapper,
    private val eligibleForAddressUseCase: EligibleForAddressUseCase,
    private val validateShareAddressAsReceiverUseCase: ValidateShareAddressAsReceiverUseCase,
    private val validateShareAddressAsSenderUseCase: ValidateShareAddressAsSenderUseCase,
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase,
    private val getUserConsentCollection: GetConsentCollectionUseCase
) : ViewModel() {

    companion object {
        const val STATUS_SUCCESS = 1
        private const val DEFAULT_ERROR_CONSENT = "Terjadi kesalahan. Silahkan coba lagi."
    }

    var token: Token? = null
    var savedQuery: String = ""
    var page: Int = 1
    var canLoadMore: Boolean = true
    var isClearData: Boolean = true
    var receiverUserId: String? = null
    var senderUserId: String? = null
    var receiverUserName: String? = null
    val isNeedToShareAddress: Boolean
        get() = receiverUserId?.isNotBlank() == true
    val isReceiveShareAddress: Boolean
        get() = senderUserId?.isNotBlank() == true
    val isNeedValidateShareAddress: Boolean
        get() = receiverUserId?.isNotBlank() == true || senderUserId?.isNotBlank() == true
    var source = ""
    private val isTokonow: Boolean
        get() = source == ManageAddressSource.TOKONOW.source
    val isFromMoneyIn: Boolean
        get() = source == ManageAddressSource.MONEY_IN.source

    private val _addressList = MutableLiveData<ManageAddressState<AddressListModel>>()
    val addressList: LiveData<ManageAddressState<AddressListModel>>
        get() = _addressList

    private val _resultRemovedAddress = MutableLiveData<ManageAddressState<DeletePeopleAddressData>>()
    val resultRemovedAddress: LiveData<ManageAddressState<DeletePeopleAddressData>>
        get() = _resultRemovedAddress

    private val _setDefault = MutableLiveData<ManageAddressState<SetDefaultPeopleAddressResponse>>()
    val setDefault: LiveData<ManageAddressState<SetDefaultPeopleAddressResponse>>
        get() = _setDefault

    private val _getChosenAddress = MutableLiveData<Result<ChosenAddressModel>>()
    val getChosenAddress: LiveData<Result<ChosenAddressModel>>
        get() = _getChosenAddress

    private val _setChosenAddress = MutableLiveData<Result<ChosenAddressModel>>()
    val setChosenAddress: LiveData<Result<ChosenAddressModel>>
        get() = _setChosenAddress

    private val _eligibleForAddressFeature =
        MutableLiveData<Result<EligibleForAddressFeatureModel>>()
    val eligibleForAddressFeature: LiveData<Result<EligibleForAddressFeatureModel>>
        get() = _eligibleForAddressFeature

    private val _validateShareAddressState = MutableLiveData<ValidateShareAddressState>()
    val validateShareAddressState: LiveData<ValidateShareAddressState>
        get() = _validateShareAddressState

    private val _tickerState = MutableLiveData<Result<TickerModel>>()
    val tickerState: LiveData<Result<TickerModel>>
        get() = _tickerState

    val deleteCollectionId: String
        get() = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            ManageAddressConstant.DELETE_ADDRESS_COLLECTION_ID_STAGING
        } else {
            ManageAddressConstant.DELETE_ADDRESS_COLLECTION_ID_PRODUCTION
        }

    private val compositeSubscription = CompositeSubscription()

    fun setupDataFromArgument(bundle: Bundle?) {
        receiverUserId = bundle?.getString(ManageAddressConstant.QUERY_PARAM_RUID)
        senderUserId = bundle?.getString(ManageAddressConstant.QUERY_PARAM_SUID)
        source = bundle?.getString(ApplinkConstInternalLogistic.PARAM_SOURCE) ?: ""
    }

    fun searchAddress(
        query: String,
        prevState: Int,
        localChosenAddrId: Long,
        isWhiteListChosenAddress: Boolean
    ) {
        _addressList.value = ManageAddressState.Loading
        compositeSubscription.add(
            getPeopleAddressUseCase.execute(
                query,
                prevState = prevState,
                localChosenAddrId = localChosenAddrId,
                isWhitelistChosenAddress = isWhiteListChosenAddress,
                excludeSharedAddress = isNeedToShareAddress
            )
                .subscribe(object : rx.Observer<AddressListModel> {
                    override fun onError(it: Throwable?) {
                        _addressList.value = ManageAddressState.Fail(it, "")
                    }

                    override fun onNext(addressModel: AddressListModel) {
                        page = 1
                        token = addressModel.token
                        savedQuery = query
                        canLoadMore = true
                        _addressList.value = ManageAddressState.Success(addressModel)
                    }

                    override fun onCompleted() {
                        // no-op
                    }
                })
        )
    }

    fun loadMore(prevState: Int, localChosenAddrId: Long, isWhitelistChosenAddress: Boolean) {
        _addressList.value = ManageAddressState.Loading
        compositeSubscription.add(
            getPeopleAddressUseCase.loadMore(
                savedQuery,
                page + 1,
                prevState = prevState,
                localChosenAddrId = localChosenAddrId,
                isWhitelistChosenAddress = isWhitelistChosenAddress,
                excludeSharedAddress = isNeedToShareAddress
            )
                .subscribe(object : rx.Observer<AddressListModel> {
                    override fun onError(it: Throwable?) {
                        _addressList.value = ManageAddressState.Fail(it, "")
                    }

                    override fun onNext(addressModel: AddressListModel) {
                        page++
                        isClearData = false
                        if (addressModel.listAddress.isEmpty()) canLoadMore = false
                        _addressList.value = ManageAddressState.Success(addressModel)
                    }

                    override fun onCompleted() {
                        // no-op
                    }
                })
        )
    }

    fun deletePeopleAddress(id: String) {
        viewModelScope.launchCatchError(
            block = {
                val userConsentPayload = getUserConsentPayload()
                val resultDelete =
                    deletePeopleAddressUseCase(
                        DeleteAddressParam(
                            inputAddressId = id.toLong(),
                            isTokonowRequest = true,
                            consentJson = userConsentPayload
                        )
                    )
                if (resultDelete.response.status.equals(ManageAddressConstant.STATUS_OK, true) &&
                    resultDelete.response.data.success == STATUS_SUCCESS
                ) {
                    _resultRemovedAddress.value = ManageAddressState.Success(resultDelete.response.data)
                    isClearData = true
                    getStateChosenAddress("address")
                } else {
                    _resultRemovedAddress.value =
                        ManageAddressState.Fail(MessageErrorException(DEFAULT_ERROR_MESSAGE), "")
                }
            },
            onError = {
                _resultRemovedAddress.value = ManageAddressState.Fail(it, it.message.orEmpty())
            }
        )
    }

    private suspend fun getUserConsentPayload(): String {
        val userConsentParam = ConsentCollectionParam(collectionId = deleteCollectionId)
        val userConsent = getUserConsentCollection(userConsentParam)
        val isErrorGetConsent = userConsent.data.collectionPoints.isEmpty()
        if (isErrorGetConsent) {
            val message = if (userConsent.data.errorMessages.isNotEmpty()) {
                userConsent.data.errorMessages.first()
            } else {
                DEFAULT_ERROR_CONSENT
            }
            throw Throwable(message)
        }
        val collection = userConsent.data.collectionPoints.first()
        val dataElements = mutableMapOf<String, String>()
        userConsentParam.dataElements.forEach { it ->
            dataElements[it.elementName] = it.elementValue
        }
        val purposes: MutableList<UserConsentPayload.PurposeDataModel> = mutableListOf()
        collection.purposes.forEach {
            purposes.add(
                UserConsentPayload.PurposeDataModel(
                    purposeId = it.id,
                    version = it.version,
                    /*
                    * default value of transactionType is OPT_OUT, because the first time show checkbox always uncheck
                    * specially for consentTypeInfo (that no checkbox show) the value must be OPT_IN.
                    */
                    transactionType =
                    if (collection.isConsentTypeInfo()) {
                        UserConsentConst.CONSENT_OPT_IN
                    } else {
                        it.transactionType
                    },
                    dataElementType = it.attribute.dataElementType
                )
            )
        }
        return UserConsentPayload(
            identifier = userConsentParam.identifier,
            collectionId = collection.id,
            dataElements = dataElements,
            default = isErrorGetConsent,
            purposes = purposes
        ).toString()
    }

    private fun CollectionPointDataModel.isConsentTypeInfo(): Boolean {
        return (this.attributes.collectionPointPurposeRequirement == UserConsentConst.MANDATORY && this.attributes.collectionPointStatementOnlyFlag == UserConsentConst.NO_CHECKLIST)
    }

    fun setDefaultPeopleAddress(
        id: String,
        setAsStateChosenAddress: Boolean,
        prevState: Int,
        localChosenAddrId: Long,
        isWhiteListChosenAddress: Boolean
    ) {
        viewModelScope.launchCatchError(
            block = {
                val defaultAddressParam =
                    DefaultAddressParam(id.toLong(), setAsStateChosenAddress, true)
                val resultDefaultAddress = setDefaultPeopleAddressUseCase(defaultAddressParam)
                if (
                    resultDefaultAddress.response.status.equals(
                        ManageAddressConstant.STATUS_OK,
                        true
                    ) &&
                    resultDefaultAddress.response.data.success == STATUS_SUCCESS
                ) {
                    _setDefault.value = ManageAddressState.Success(resultDefaultAddress.response)
                    isClearData = true
                    searchAddress("", prevState, localChosenAddrId, isWhiteListChosenAddress)
                } else {
                    _setDefault.value =
                        ManageAddressState.Fail(MessageErrorException(DEFAULT_ERROR_MESSAGE), "")
                }
            },
            onError =
            {
                _setDefault.value = ManageAddressState.Fail(it, it.message.orEmpty())
            }
        )
    }

    fun getStateChosenAddress(source: String) {
        viewModelScope.launch(onErrorGetStateChosenAddress) {
            val getStateChosenAddress = chooseAddressRepo.getStateChosenAddress(source, true)
            _getChosenAddress.value =
                Success(chooseAddressMapper.mapGetStateChosenAddress(getStateChosenAddress.response))
        }
    }

    fun setStateChosenAddress(model: RecipientAddressModel) {
        viewModelScope.launch(onErrorSetStateChosenAddress) {
            val setStateChosenAddress = chooseAddressRepo.setStateChosenAddressFromAddress(model)
            _setChosenAddress.value =
                Success(chooseAddressMapper.mapSetStateChosenAddress(setStateChosenAddress.response))
        }
    }

    fun checkUserEligibilityForAnaRevamp() {
        eligibleForAddressUseCase.eligibleForAddressFeature(
            {
                _eligibleForAddressFeature.value =
                    Success(
                        EligibleAddressFeatureMapper.mapResponseToModel(
                            it,
                            AddressConstant.ANA_REVAMP_FEATURE_ID,
                            null
                        )
                    )
            },
            {
                _eligibleForAddressFeature.value = Fail(it)
            },
            AddressConstant.ANA_REVAMP_FEATURE_ID
        )
    }

    fun checkUserEligibilityForEditAddressRevamp(data: RecipientAddressModel) {
        eligibleForAddressUseCase.eligibleForAddressFeature(
            {
                _eligibleForAddressFeature.value = Success(
                    EligibleAddressFeatureMapper.mapResponseToModel(
                        it,
                        AddressConstant.EDIT_ADDRESS_REVAMP_FEATURE_ID,
                        data
                    )
                )
            },
            {
                _eligibleForAddressFeature.value = Fail(it)
            },
            AddressConstant.EDIT_ADDRESS_REVAMP_FEATURE_ID
        )
    }

    private val onErrorGetStateChosenAddress = CoroutineExceptionHandler { _, e ->
        _getChosenAddress.value = Fail(e)
    }

    private val onErrorSetStateChosenAddress = CoroutineExceptionHandler { _, e ->
        _setChosenAddress.value = Fail(e)
    }

    fun doValidateShareAddress() {
        if (isNeedToShareAddress) {
            validateShareAddressAsSender(receiverUserId.orEmpty())
        } else {
            validateShareAddressAsReceiver(senderUserId.orEmpty())
        }
    }

    private fun validateShareAddressAsReceiver(senderUserId: String) {
        viewModelScope.launchCatchError(
            block = {
                val params = ValidateShareAddressAsReceiverParam(
                    senderUserId = senderUserId,
                    source = getSourceValue()
                )
                val result = validateShareAddressAsReceiverUseCase(params)
                _validateShareAddressState.value =
                    if (result.keroValidateShareAddressAsReceiver?.isValid == true) {
                        ValidateShareAddressState.Success()
                    } else {
                        ValidateShareAddressState.Fail
                    }
            },
            onError = {
                _validateShareAddressState.value = ValidateShareAddressState.Fail
            }
        )
    }

    private fun validateShareAddressAsSender(receiverUserId: String) {
        viewModelScope.launchCatchError(
            block = {
                val params = ValidateShareAddressAsSenderParam(
                    receiverUserId = receiverUserId,
                    source = getSourceValue()
                )
                val result = validateShareAddressAsSenderUseCase(params)
                _validateShareAddressState.value =
                    if (result.keroValidateShareAddressAsSender?.isValid == true) {
                        ValidateShareAddressState.Success(result.keroValidateShareAddressAsSender.receiverUserName)
                    } else {
                        ValidateShareAddressState.Fail
                    }
            },
            onError = {
                _validateShareAddressState.value = ValidateShareAddressState.Fail
            }
        )
    }

    fun getSourceValue(): String {
        return if (isTokonow) {
            ManageAddressSource.LOCALIZED_ADDRESS_WIDGET.source
        } else {
            source
        }
    }

    fun getTargetedTicker(firstTickerContent: String? = null) {
        viewModelScope.launchCatchError(
            block = {
                val response = getTargetedTickerUseCase(GetTargetedTickerParam.ADDRESS_LIST_NON_OCC)
                _tickerState.value = Success(
                    convertTargetedTickerToUiModel(
                        targetedTickerData = response.getTargetedTickerData,
                        firstTickerContent = firstTickerContent
                    )
                )
            },
            onError = {
                if (firstTickerContent?.isNotBlank() == true) {
                    _tickerState.value = Success(
                        convertTargetedTickerToUiModel(
                            firstTickerContent = firstTickerContent
                        )
                    )
                } else {
                    _tickerState.value = Fail(it)
                }
            }
        )
    }
}
