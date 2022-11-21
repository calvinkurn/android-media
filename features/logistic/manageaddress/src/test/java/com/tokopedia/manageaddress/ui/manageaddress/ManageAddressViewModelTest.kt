package com.tokopedia.manageaddress.ui.manageaddress

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.logisticCommon.data.constant.AddressConstant.ANA_REVAMP_FEATURE_ID
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EDIT_ADDRESS_REVAMP_FEATURE_ID
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.model.EligibleForAddressFeatureModel
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.domain.response.*
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsReceiverResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsSenderResponse
import com.tokopedia.manageaddress.domain.usecase.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ValidateShareAddressAsReceiverUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ValidateShareAddressAsSenderUseCase
import com.tokopedia.manageaddress.ui.uimodel.ValidateShareAddressState
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.KEY_SHARE_ADDRESS_LOGI
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable

@ExperimentalCoroutinesApi
class ManageAddressViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getPeopleAddressUseCase: GetAddressCornerUseCase = mockk(relaxed = true)
    private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase = mockk(relaxed = true)
    private val setDefaultPeopleAddressUseCase =
        mockk<SetDefaultPeopleAddressUseCase>(relaxed = true)
    private val eligibleForAddressUseCase: EligibleForAddressUseCase = mockk(relaxed = true)
    private val chooseAddressRepo: ChooseAddressRepository = mockk(relaxed = true)
    private val chooseAddressMapper: ChooseAddressMapper = mockk(relaxed = true)
    private val chosenAddressObserver: Observer<Result<ChosenAddressModel>> = mockk(relaxed = true)
    private val eligibleForAddressFeatureObserver: Observer<Result<EligibleForAddressFeatureModel>> =
        mockk(relaxed = true)
    private val validateShareAddressAsReceiverUseCase: ValidateShareAddressAsReceiverUseCase =
        mockk(relaxed = true)
    private val validateShareAddressAsSenderUseCase: ValidateShareAddressAsSenderUseCase =
        mockk(relaxed = true)

    private var observerManageAddressState =
        mockk<Observer<ManageAddressState<String>>>(relaxed = true)
    private var observerManageAddressStateAddressList =
        mockk<Observer<ManageAddressState<AddressListModel>>>(relaxed = true)
    private var observerResultRemovedAddress =
        mockk<Observer<ManageAddressState<String>>>(relaxed = true)
    private var observerValidateShareAddressState =
        mockk<Observer<ValidateShareAddressState>>(relaxed = true)
    private val mockThrowable = mockk<Throwable>(relaxed = true)


    private lateinit var manageAddressViewModel: ManageAddressViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        manageAddressViewModel = ManageAddressViewModel(
            getPeopleAddressUseCase,
            deletePeopleAddressUseCase,
            setDefaultPeopleAddressUseCase,
            chooseAddressRepo,
            chooseAddressMapper,
            eligibleForAddressUseCase,
            validateShareAddressAsReceiverUseCase,
            validateShareAddressAsSenderUseCase
        )
        manageAddressViewModel.getChosenAddress.observeForever(chosenAddressObserver)
        manageAddressViewModel.setChosenAddress.observeForever(chosenAddressObserver)
        manageAddressViewModel.eligibleForAddressFeature.observeForever(
            eligibleForAddressFeatureObserver
        )
        manageAddressViewModel.setDefault.observeForever(observerManageAddressState)
        manageAddressViewModel.addressList.observeForever(observerManageAddressStateAddressList)
        manageAddressViewModel.resultRemovedAddress.observeForever(observerResultRemovedAddress)
        manageAddressViewModel.validateShareAddressState.observeForever(
            observerValidateShareAddressState
        )
    }

    @Test
    fun `Search Address Success`() {
        val response = AddressListModel()
        every {
            getPeopleAddressUseCase.execute(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Observable.just(response).doOnSubscribe {
            assertEquals(ManageAddressState.Loading, manageAddressViewModel.addressList.value)
        }

        manageAddressViewModel.searchAddress("", -1, -1, true)

        assertEquals(ManageAddressState.Success(response), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Search Address Failed`() {
        val response = Throwable()
        every {
            getPeopleAddressUseCase.execute(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Observable.error(response)

        manageAddressViewModel.searchAddress("", -1, -1, true)

        assertEquals(
            ManageAddressState.Fail(response, ""),
            manageAddressViewModel.addressList.value
        )
    }

    @Test
    fun `Load More Address Success`() {
        val response = AddressListModel()
        every {
            getPeopleAddressUseCase.loadMore(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Observable.just(response)
            .doOnSubscribe {
                assertEquals(ManageAddressState.Loading, manageAddressViewModel.addressList.value)
            }

        manageAddressViewModel.loadMore(-1, -1, true)

        assertEquals(ManageAddressState.Success(response), manageAddressViewModel.addressList.value)
    }

    @Test
    fun `Load More Address Failed`() {
        val response = Throwable()
        every {
            getPeopleAddressUseCase.loadMore(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Observable.error(response)

        manageAddressViewModel.loadMore(-1, -1, true)

        assertEquals(
            ManageAddressState.Fail(response, ""),
            manageAddressViewModel.addressList.value
        )
    }

    @Test
    fun `Set Default Address Success`() {
        val mockDefaultPeopleAddressGqlResponse = spyk(
            SetDefaultPeopleAddressGqlResponse(
                SetDefaultPeopleAddressResponse(
                    status = ManageAddressConstant.STATUS_OK,
                    data = DefaultPeopleAddressData(success = 1),
                )
            )
        )

        coEvery { setDefaultPeopleAddressUseCase.invoke(any()) } returns mockDefaultPeopleAddressGqlResponse
        manageAddressViewModel.setDefaultPeopleAddress("1", true, -1, -1, true)
        verify { observerManageAddressState.onChanged(match { it is ManageAddressState.Success }) }

    }

    @Test
    fun `Set Default Address Fail`() {
        coEvery { setDefaultPeopleAddressUseCase.invoke(any()) } throws mockThrowable
        manageAddressViewModel.setDefaultPeopleAddress("1", true, -1, -1, true)
        verify { observerManageAddressState.onChanged(match { it is ManageAddressState.Fail }) }
    }

    @Test
    fun `Delete Address Success`() {
        val mockResponseDeletePeopleAddressGqlResponse = DeletePeopleAddressGqlResponse(
            DeletePeopleAddressResponse(
                data = DeletePeopleAddressData(success = 1),
                status = ManageAddressConstant.STATUS_OK
            )
        )

        coEvery { deletePeopleAddressUseCase.invoke(any()) } returns mockResponseDeletePeopleAddressGqlResponse
        manageAddressViewModel.deletePeopleAddress("1")
        verify { observerResultRemovedAddress.onChanged(match { it is ManageAddressState.Success }) }
    }

    @Test
    fun `Delete Address Fail`() {
        coEvery { deletePeopleAddressUseCase.invoke(any()) } throws mockThrowable
        manageAddressViewModel.deletePeopleAddress("1")
        verify { observerResultRemovedAddress.onChanged(match { it is ManageAddressState.Fail }) }
    }

    @Test
    fun `Get Chosen Address Success`() {
        coEvery {
            chooseAddressRepo.getStateChosenAddress(
                any(),
                any()
            )
        } returns GetStateChosenAddressQglResponse()
        manageAddressViewModel.getStateChosenAddress("address")
        verify { chosenAddressObserver.onChanged(match { it is Success }) }
    }


    @Test
    fun `Get Chosen Address Fail`() {
        coEvery {
            chooseAddressRepo.getStateChosenAddress(
                any(),
                any()
            )
        } throws Throwable("test error")
        manageAddressViewModel.getStateChosenAddress("address")
        verify { chosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Set Chosen Address Success`() {
        val model = RecipientAddressModel()
        coEvery { chooseAddressRepo.setStateChosenAddressFromAddress(any()) } returns SetStateChosenAddressQqlResponse()
        manageAddressViewModel.setStateChosenAddress(model)
        verify { chosenAddressObserver.onChanged(match { it is Success }) }
    }


    @Test
    fun `Set Chosen Address Fail`() {
        val model = RecipientAddressModel()
        coEvery { chooseAddressRepo.setStateChosenAddressFromAddress(any()) } throws Throwable("test error")
        manageAddressViewModel.setStateChosenAddress(model)
        verify { chosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Eligible For Revamp Ana Success`() {
        onCheckEligibility_thenReturn(ANA_REVAMP_FEATURE_ID)
        manageAddressViewModel.checkUserEligibilityForAnaRevamp()
        verify { eligibleForAddressFeatureObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Eligible For Revamp Ana Fail`() {
        onCheckEligibility_thenThrow(ANA_REVAMP_FEATURE_ID)
        manageAddressViewModel.checkUserEligibilityForAnaRevamp()
        verify { eligibleForAddressFeatureObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Eligible For Revamp Edit Address Success`() {
        onCheckEligibility_thenReturn(EDIT_ADDRESS_REVAMP_FEATURE_ID)
        val data = RecipientAddressModel()
        manageAddressViewModel.checkUserEligibilityForEditAddressRevamp(data)
        verify { eligibleForAddressFeatureObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Eligible For Revamp Edit Address Fail`() {
        onCheckEligibility_thenThrow(EDIT_ADDRESS_REVAMP_FEATURE_ID)
        val data = RecipientAddressModel()
        manageAddressViewModel.checkUserEligibilityForEditAddressRevamp(data)
        verify { eligibleForAddressFeatureObserver.onChanged(match { it is Fail }) }
    }

    private fun onCheckEligibility_thenReturn(featureId: Int) {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), featureId)
        } answers {
            firstArg<(KeroAddrIsEligibleForAddressFeatureData) -> Unit>().invoke(
                KeroAddrIsEligibleForAddressFeatureData()
            )
        }
    }

    private fun onCheckEligibility_thenThrow(featureId: Int) {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), featureId)
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
    }

    @Test
    fun `verify when validate share address as receiver is success`() {
        val source = "notification"
        val mockResponse = spyk(
            ValidateShareAddressAsReceiverResponse(
                keroValidateShareAddressAsReceiver = spyk(
                    ValidateShareAddressAsReceiverResponse.ValidateShareAddressData(
                        isValid = true
                    )
                )
            )
        )

        coEvery {
            validateShareAddressAsReceiverUseCase.invoke(any())
        } returns mockResponse

        manageAddressViewModel.source = source
        manageAddressViewModel.senderUserId = "1"
        manageAddressViewModel.doValidateShareAddress()

        assertEquals(manageAddressViewModel.source, source)
        assertTrue(manageAddressViewModel.isReceiveShareAddress)
        assertTrue(manageAddressViewModel.isNeedValidateShareAddress)
        verify {
            observerValidateShareAddressState.onChanged(ValidateShareAddressState.Success())
        }
    }

    @Test
    fun `verify when validate share address as receiver success`() {
        manageAddressViewModel.senderUserId = "1"
        val mockResponse = spyk(
            ValidateShareAddressAsReceiverResponse(
                keroValidateShareAddressAsReceiver = spyk(
                    ValidateShareAddressAsReceiverResponse.ValidateShareAddressData(
                        isValid = false
                    )
                )
            )
        )

        coEvery {
            validateShareAddressAsReceiverUseCase.invoke(any())
        } returns mockResponse

        manageAddressViewModel.doValidateShareAddress()

        verify {
            observerValidateShareAddressState.onChanged(ValidateShareAddressState.Fail)
        }
    }

    @Test
    fun `verify when validate share address as receiver error`() {
        manageAddressViewModel.senderUserId = "1"
        coEvery {
            validateShareAddressAsReceiverUseCase.invoke(any())
        } throws mockThrowable

        manageAddressViewModel.doValidateShareAddress()

        verify {
            observerValidateShareAddressState.onChanged(ValidateShareAddressState.Fail)
        }
    }

    @Test
    fun `verify when validate share address as sender is success`() {
        manageAddressViewModel.receiverUserId = "1"
        val receiverUserName = "Ronaldo"
        val mockResponse = spyk(
            ValidateShareAddressAsSenderResponse(
                keroValidateShareAddressAsSender = spyk(
                    ValidateShareAddressAsSenderResponse.ValidateShareAddressData(
                        isValid = true,
                        receiverUserName = receiverUserName
                    )
                )
            )
        )

        coEvery {
            validateShareAddressAsSenderUseCase.invoke(any())
        } returns mockResponse

        manageAddressViewModel.doValidateShareAddress()

        assertTrue(manageAddressViewModel.isNeedToShareAddress)
        assertTrue(manageAddressViewModel.isNeedValidateShareAddress)
        verify {
            observerValidateShareAddressState.onChanged(
                ValidateShareAddressState.Success(
                    receiverUserName
                )
            )
        }
    }

    @Test
    fun `verify when validate share address as sender success`() {
        manageAddressViewModel.receiverUserId = "1"
        val mockResponse = spyk(
            ValidateShareAddressAsSenderResponse(
                keroValidateShareAddressAsSender = spyk(
                    ValidateShareAddressAsSenderResponse.ValidateShareAddressData(
                        isValid = false
                    )
                )
            )
        )

        coEvery {
            validateShareAddressAsSenderUseCase.invoke(any())
        } returns mockResponse

        manageAddressViewModel.doValidateShareAddress()

        verify {
            observerValidateShareAddressState.onChanged(ValidateShareAddressState.Fail)
        }
    }

    @Test
    fun `verify when validate share address as sender error`() {
        manageAddressViewModel.receiverUserId = "1"
        coEvery {
            validateShareAddressAsSenderUseCase.invoke(any())
        } throws mockThrowable

        manageAddressViewModel.doValidateShareAddress()

        verify {
            observerValidateShareAddressState.onChanged(ValidateShareAddressState.Fail)
        }
    }

    @Test
    fun `verify when delete address is not success`() {
        // Given
        val mockResponseDeletePeopleAddressGqlResponse = DeletePeopleAddressGqlResponse(
            DeletePeopleAddressResponse(
                data = DeletePeopleAddressData(success = 0)
            )
        )
        coEvery { deletePeopleAddressUseCase.invoke(any()) } returns mockResponseDeletePeopleAddressGqlResponse

        // When
        manageAddressViewModel.deletePeopleAddress("1")

        // Then
        verify { observerResultRemovedAddress.onChanged(match { it is ManageAddressState.Fail }) }
    }

    @Test
    fun `verify when set default address is not success`() {
        // Given
        val mockDefaultPeopleAddressGqlResponse = spyk(
            SetDefaultPeopleAddressGqlResponse(
                SetDefaultPeopleAddressResponse(
                    data = DefaultPeopleAddressData(success = 0)
                )
            )
        )
        coEvery { setDefaultPeopleAddressUseCase.invoke(any()) } returns mockDefaultPeopleAddressGqlResponse

        // When
        manageAddressViewModel.setDefaultPeopleAddress("1", true, -1, -1, true)

        // Then
        verify { observerManageAddressState.onChanged(match { it is ManageAddressState.Fail }) }
    }

    @Test
    fun `verify when validate share address as receiver from tokonow is success`() {
        // Given
        val source = ManageAddressSource.TOKONOW.source
        val mockResponse = spyk(
            ValidateShareAddressAsReceiverResponse(
                keroValidateShareAddressAsReceiver = spyk(
                    ValidateShareAddressAsReceiverResponse.ValidateShareAddressData(
                        isValid = true
                    )
                )
            )
        )
        coEvery {
            validateShareAddressAsReceiverUseCase.invoke(any())
        } returns mockResponse
        manageAddressViewModel.source = source
        manageAddressViewModel.senderUserId = "1"

        // When
        manageAddressViewModel.doValidateShareAddress()

        // Then
        assertEquals(manageAddressViewModel.source, source)
        assertTrue(manageAddressViewModel.isReceiveShareAddress)
        assertTrue(manageAddressViewModel.isNeedValidateShareAddress)
        verify {
            observerValidateShareAddressState.onChanged(ValidateShareAddressState.Success())
        }
    }

    @Test
    fun `verify when setupDataFromArgument is correct`() {
        // Given
        val ruid = "123"
        val suid = "456"
        val source = "source"
        mockkStatic(RemoteConfigInstance::class)

        val bundle = mockk<Bundle>()
        every {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                KEY_SHARE_ADDRESS_LOGI,
                ""
            )
        } returns KEY_SHARE_ADDRESS_LOGI
        every { bundle.getString(ManageAddressConstant.QUERY_PARAM_RUID) } returns ruid
        every { bundle.getString(ManageAddressConstant.QUERY_PARAM_SUID) } returns suid
        every { bundle.getString(PARAM_SOURCE) } returns source

        // When
        manageAddressViewModel.setupDataFromArgument(bundle)

        // Then
        assertEquals(manageAddressViewModel.source, source)
        assertEquals(manageAddressViewModel.receiverUserId, ruid)
        assertEquals(manageAddressViewModel.senderUserId, suid)
    }
}
