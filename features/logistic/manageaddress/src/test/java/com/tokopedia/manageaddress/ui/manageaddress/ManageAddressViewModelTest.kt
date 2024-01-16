package com.tokopedia.manageaddress.ui.manageaddress

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetStateChosenAddressUseCase
import com.tokopedia.localizationchooseaddress.domain.usecase.SetStateChosenAddressFromAddressUseCase
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.domain.response.DefaultPeopleAddressData
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressData
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressGqlResponse
import com.tokopedia.manageaddress.domain.response.DeletePeopleAddressResponse
import com.tokopedia.manageaddress.domain.response.SetDefaultPeopleAddressGqlResponse
import com.tokopedia.manageaddress.domain.response.SetDefaultPeopleAddressResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsReceiverResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsSenderResponse
import com.tokopedia.manageaddress.domain.usecase.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ValidateShareAddressAsReceiverUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ValidateShareAddressAsSenderUseCase
import com.tokopedia.manageaddress.ui.uimodel.ValidateShareAddressState
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usercomponents.userconsent.common.AttributeDataModel
import com.tokopedia.usercomponents.userconsent.common.CollectionPointDataModel
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionResponse
import com.tokopedia.usercomponents.userconsent.common.PurposeDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst
import com.tokopedia.usercomponents.userconsent.domain.collection.GetConsentCollectionUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
    private val getUserConsentCollection: GetConsentCollectionUseCase = mockk(relaxed = true)
    private val setStateChosenAddressFromAddressUseCase: SetStateChosenAddressFromAddressUseCase =
        mockk(relaxed = true)
    private val getStateChosenAddressUseCase: GetStateChosenAddressUseCase = mockk(relaxed = true)

    private val chooseAddressMapper: ChooseAddressMapper = mockk(relaxed = true)
    private val chosenAddressObserver: Observer<Result<ChosenAddressModel>> = mockk(relaxed = true)
    private val validateShareAddressAsReceiverUseCase: ValidateShareAddressAsReceiverUseCase =
        mockk(relaxed = true)
    private val validateShareAddressAsSenderUseCase: ValidateShareAddressAsSenderUseCase =
        mockk(relaxed = true)
    private val tickerUseCase: GetTargetedTickerUseCase = mockk(relaxed = true)

    private var observerManageAddressState =
        mockk<Observer<ManageAddressState<SetDefaultPeopleAddressResponse>>>(relaxed = true)
    private var observerManageAddressStateAddressList =
        mockk<Observer<ManageAddressState<AddressListModel>>>(relaxed = true)
    private var observerResultRemovedAddress =
        mockk<Observer<ManageAddressState<DeletePeopleAddressData>>>(relaxed = true)
    private var observerValidateShareAddressState =
        mockk<Observer<ValidateShareAddressState>>(relaxed = true)
    private val mockThrowable = mockk<Throwable>(relaxed = true)

    private val context = mockk<Context>(relaxed = true)
    private val sharedPrefs = mockk<SharedPreferences>(relaxed = true)

    private lateinit var manageAddressViewModel: ManageAddressViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        stubSharePrefs()
        manageAddressViewModel = ManageAddressViewModel(
            getPeopleAddressUseCase,
            deletePeopleAddressUseCase,
            setDefaultPeopleAddressUseCase,
            chooseAddressMapper,
            validateShareAddressAsReceiverUseCase,
            validateShareAddressAsSenderUseCase,
            getUserConsentCollection,
            setStateChosenAddressFromAddressUseCase,
            getStateChosenAddressUseCase
        )
        manageAddressViewModel.getChosenAddress.observeForever(chosenAddressObserver)
        manageAddressViewModel.setChosenAddress.observeForever(chosenAddressObserver)
        manageAddressViewModel.setDefault.observeForever(observerManageAddressState)
        manageAddressViewModel.addressList.observeForever(observerManageAddressStateAddressList)
        manageAddressViewModel.resultRemovedAddress.observeForever(observerResultRemovedAddress)
        manageAddressViewModel.validateShareAddressState.observeForever(
            observerValidateShareAddressState
        )
    }

    @After
    fun tearDown() {
        TokopediaUrl.deleteInstance()
    }

    private fun stubSharePrefs() {
        coEvery { context.getSharedPreferences(any(), any()) } returns sharedPrefs
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
                    data = DefaultPeopleAddressData(success = 1)
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

        val mockCollectionPoints = mutableListOf(
            CollectionPointDataModel(
                id = "id",
                consentType = "type",
                attributes = AttributeDataModel(
                    collectionPointPurposeRequirement = UserConsentConst.MANDATORY,
                    collectionPointStatementOnlyFlag = UserConsentConst.NO_CHECKLIST
                ),
                purposes = mutableListOf(PurposeDataModel(id = "id", version = "version"))
            )
        )

        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                success = true,
                collectionPoints = mockCollectionPoints
            )
        )

        coEvery {
            getUserConsentCollection(any())
        } returns mockResponse
        coEvery { deletePeopleAddressUseCase.invoke(any()) } returns mockResponseDeletePeopleAddressGqlResponse

        manageAddressViewModel.deletePeopleAddress("1")
        verify { observerResultRemovedAddress.onChanged(match { it is ManageAddressState.Success }) }
    }

    @Test
    fun `WHEN delete address but doesnt get collection point THEN show error from user consent response`() {
        val mockResponseDeletePeopleAddressGqlResponse = DeletePeopleAddressGqlResponse(
            DeletePeopleAddressResponse(
                data = DeletePeopleAddressData(success = 1),
                status = ManageAddressConstant.STATUS_OK
            )
        )
        val errorResponse = "error response"

        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                errorMessages = listOf(errorResponse),
                success = false,
                collectionPoints = mutableListOf()
            )
        )

        coEvery {
            getUserConsentCollection(any())
        } returns mockResponse
        coEvery { deletePeopleAddressUseCase.invoke(any()) } returns mockResponseDeletePeopleAddressGqlResponse

        manageAddressViewModel.deletePeopleAddress("1")
        verify { observerResultRemovedAddress.onChanged(match { (it as ManageAddressState.Fail).throwable?.message == errorResponse }) }
    }

    @Test
    fun `WHEN delete address but doesnt get collection point and user consent response doesnt have error response THEN show default error message`() {
        val mockResponseDeletePeopleAddressGqlResponse = DeletePeopleAddressGqlResponse(
            DeletePeopleAddressResponse(
                data = DeletePeopleAddressData(success = 1),
                status = ManageAddressConstant.STATUS_OK
            )
        )

        val mockResponse = ConsentCollectionResponse(
            UserConsentCollectionDataModel(
                errorMessages = listOf(),
                success = true,
                collectionPoints = mutableListOf()
            )
        )

        coEvery {
            getUserConsentCollection(any())
        } returns mockResponse
        coEvery { deletePeopleAddressUseCase.invoke(any()) } returns mockResponseDeletePeopleAddressGqlResponse

        manageAddressViewModel.deletePeopleAddress("1")
        verify { observerResultRemovedAddress.onChanged(match { (it as ManageAddressState.Fail).throwable?.message == "Terjadi kesalahan. Silahkan coba lagi." }) }
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
            getStateChosenAddressUseCase(
                any()
            )
        } returns GetStateChosenAddressQglResponse()
        manageAddressViewModel.getStateChosenAddress("address")
        verify { chosenAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Chosen Address Fail`() {
        coEvery {
            getStateChosenAddressUseCase(
                any()
            )
        } throws Throwable("test error")
        manageAddressViewModel.getStateChosenAddress("address")
        verify { chosenAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Set Chosen Address Success`() {
        val model = RecipientAddressModel()
        coEvery { setStateChosenAddressFromAddressUseCase(any()) } returns SetStateChosenAddressQqlResponse()
        manageAddressViewModel.setStateChosenAddress(model)
        verify { chosenAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Set Chosen Address Fail`() {
        val model = RecipientAddressModel()
        coEvery { setStateChosenAddressFromAddressUseCase(any()) } throws Throwable("test error")
        manageAddressViewModel.setStateChosenAddress(model)
        verify { chosenAddressObserver.onChanged(match { it is Fail }) }
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
        every { bundle.getString(ManageAddressConstant.QUERY_PARAM_RUID) } returns ruid
        every { bundle.getString(ManageAddressConstant.QUERY_PARAM_SUID) } returns suid
        every { bundle.getString(PARAM_SOURCE) } returns source

        // When
        manageAddressViewModel.setupDataFromArgument(bundle)

        // Then
        assertEquals(manageAddressViewModel.source, source)
        assertEquals(manageAddressViewModel.receiverUserId, ruid)
        assertEquals(manageAddressViewModel.senderUserId, suid)
        assertFalse(manageAddressViewModel.isFromMoneyIn)
    }

    @Test
    fun `verify when setupDataFromArgument is incorrect`() {
        // When
        manageAddressViewModel.setupDataFromArgument(null)

        // Then
        assertEquals("", manageAddressViewModel.source)
        assertNull(manageAddressViewModel.senderUserId)
        assertNull(manageAddressViewModel.receiverUserId)
        assertFalse(manageAddressViewModel.isFromMoneyIn)
    }

    @Test
    fun `verify when source from money in is correct`() {
        // Given
        manageAddressViewModel.source = ManageAddressSource.MONEY_IN.source

        // Then
        assertTrue(manageAddressViewModel.isFromMoneyIn)
    }

    @Test
    fun `verify get delete collection id staging is correctly`() {
        // Given
        coEvery { sharedPrefs.getString(any(), any()) } returns Env.STAGING.value

        // When
        TokopediaUrl.init(context)

        // Then
        assertEquals(
            manageAddressViewModel.deleteCollectionId,
            ManageAddressConstant.DELETE_ADDRESS_COLLECTION_ID_STAGING
        )
    }

    @Test
    fun `verify get delete collection id production is correctly`() {
        // Given
        coEvery { sharedPrefs.getString(any(), any()) } returns Env.LIVE.value

        // When
        TokopediaUrl.init(context)

        // Then
        assertEquals(
            manageAddressViewModel.deleteCollectionId,
            ManageAddressConstant.DELETE_ADDRESS_COLLECTION_ID_PRODUCTION
        )
    }
}
