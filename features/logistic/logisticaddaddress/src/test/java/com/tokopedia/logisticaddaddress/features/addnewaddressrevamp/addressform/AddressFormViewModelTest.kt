package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.AddAddressResponse
import com.tokopedia.logisticCommon.data.response.DataAddAddress
import com.tokopedia.logisticCommon.data.response.DefaultAddressData
import com.tokopedia.logisticCommon.data.response.GetDefaultAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroAddAddress
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.data.response.PinpointValidationResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddressFormViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val saveAddressDataModel = SaveAddressDataModel()
    private val addressId = "12345"
    private val sourceValue = ""

    private val saveAddressObserver: Observer<Result<DataAddAddress>> = mockk(relaxed = true)
    private val defaultAddressObserver: Observer<Result<DefaultAddressData>> = mockk(relaxed = true)
    private val editAddressObserver: Observer<Result<KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>> =
        mockk(relaxed = true)
    private val detailAddressObserver: Observer<Result<SaveAddressDataModel>> =
        mockk(relaxed = true)
    private val pinpointValidationObserver: Observer<Result<PinpointValidationResponse.PinpointValidations.PinpointValidationResponseData>> =
        mockk(relaxed = true)

    private lateinit var addressFormViewModel: AddressFormViewModel

    private val defaultThrowable = Throwable("test error")
    private val context = mockk<Context>(relaxed = true)
    private val sharedPrefs = mockk<SharedPreferences>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        stubSharePrefs()
        initObserver()
    }

    @After
    fun tearDown() {
        TokopediaUrl.deleteInstance()
    }

    private fun stubSharePrefs() {
        coEvery { context.getSharedPreferences(any(), any()) } returns sharedPrefs
    }

    private fun initObserver() {
        addressFormViewModel = AddressFormViewModel(repo)
        addressFormViewModel.saveAddress.observeForever(saveAddressObserver)
        addressFormViewModel.defaultAddress.observeForever(defaultAddressObserver)
        addressFormViewModel.editAddress.observeForever(editAddressObserver)
        addressFormViewModel.addressDetail.observeForever(detailAddressObserver)
        addressFormViewModel.pinpointValidation.observeForever(pinpointValidationObserver)
    }

    @Test
    fun `Get Default Address Success`() {
        // Given
        coEvery { repo.getDefaultAddress(any(), true) } returns GetDefaultAddressResponse()

        // When
        addressFormViewModel.getDefaultAddress("address")

        // Then
        verify { defaultAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Default Address Fail`() {
        // Given
        coEvery { repo.getDefaultAddress(any(), true) } throws defaultThrowable

        // When
        addressFormViewModel.getDefaultAddress("address")

        // Then
        verify { defaultAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Save Address Data Success`() {
        // Inject
        val fakeResponse = spyk(
            AddAddressResponse(
                keroAddAddress = spyk(
                    KeroAddAddress(
                        data = spyk(
                            DataAddAddress(
                                isSuccess = 1
                            )
                        )
                    )
                )
            )
        )

        // Given
        coEvery { repo.saveAddress(any(), any(), any()) } returns fakeResponse

        // When
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.saveAddress("", sourceValue)

        // Then
        verify { saveAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Address Data Not Success`() {
        // Inject
        val fakeResponse = spyk(
            AddAddressResponse(
                keroAddAddress = spyk(
                    KeroAddAddress(
                        data = spyk(
                            DataAddAddress(
                                isSuccess = 0
                            )
                        )
                    )
                )
            )
        )

        // Given
        coEvery { repo.saveAddress(any(), any(), any()) } returns fakeResponse

        // When
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.saveAddress("", sourceValue)

        // Then
        verify(exactly = 0) { saveAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Address Data Fail`() {
        coEvery { repo.saveAddress(any(), any(), any()) } throws defaultThrowable
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.saveAddress("", sourceValue)
        verify { saveAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `verify when call save address but save address model is null`() {
        // When
        addressFormViewModel.saveAddress("", sourceValue)

        // Then
        Assert.assertNull(addressFormViewModel.saveDataModel)
        verify(exactly = 0) { saveAddressObserver.onChanged(match { true }) }
    }

    @Test
    fun `Get Address Detail Data Success`() {
        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(spyk())
            )
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        verify { detailAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Address Detail Data Success but empty data list`() {
        // Given
        coEvery { repo.getAddressDetail(any(), any()) } returns KeroGetAddressResponse.Data()

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        verify(exactly = 0) { detailAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Address Detail Data Fail`() {
        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } throws defaultThrowable

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        verify { detailAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `WHEN user already editing address THEN dont hit detail address from BE again`() {
        // Given
        coEvery { repo.getAddressDetail(any(), any()) } throws defaultThrowable
        val saveDataModel = SaveAddressDataModel(
            receiverName = "name",
            phone = "081222222222",
            address1 = "detail alamat draft"
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, "", saveDataModel)

        // Then
        assert(addressFormViewModel.saveDataModel == saveDataModel)
        assert((addressFormViewModel.addressDetail.value as Success).data == saveDataModel)
    }

    @Test
    fun `Save Edit Address Data Success`() {
        // Given
        coEvery { repo.editAddress(any(), any()) } returns KeroEditAddressResponse.Data()

        // When
        addressFormViewModel.saveEditAddress(saveAddressDataModel, sourceValue)

        // Then
        verify { editAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Edit Address Data Fail`() {
        // Given
        coEvery { repo.editAddress(any(), any()) } throws defaultThrowable

        // When
        addressFormViewModel.saveEditAddress(saveAddressDataModel, sourceValue)

        // Then
        verify { editAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Pinpoint Validation Data Success`() {
        // Given
        coEvery {
            repo.pinpointValidation(
                any(),
                any(),
                any(),
                any()
            )
        } returns PinpointValidationResponse()

        // When
        addressFormViewModel.validatePinpoint(saveAddressDataModel)

        // Then
        verify { pinpointValidationObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Pinpoint Validation Data Fail`() {
        coEvery { repo.pinpointValidation(any(), any(), any(), any()) } throws Exception()
        addressFormViewModel.validatePinpoint(saveAddressDataModel)
        verify { pinpointValidationObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `verify pinpoint validation data success and call save edit address`() {
        // Inject
        val mockResponse = spyk(
            PinpointValidationResponse(
                pinpointValidations = spyk(
                    PinpointValidationResponse.PinpointValidations(
                        data = PinpointValidationResponse.PinpointValidations.PinpointValidationResponseData(
                            result = true
                        )
                    )
                )
            )
        )

        // Given
        coEvery { addressFormViewModel.saveEditAddress(any(), any()) } just Runs
        coEvery {
            repo.pinpointValidation(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockResponse

        // When
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.validatePinpoint(saveAddressDataModel)

        // Then
        verify { pinpointValidationObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `verify clearLatLong when save data model null is correctly`() {
        // when
        addressFormViewModel.clearLatLong()

        // Then
        Assert.assertFalse(addressFormViewModel.isHaveLatLong)
        Assert.assertNull(addressFormViewModel.saveDataModel)
    }

    @Test
    fun `verify clearLatLong when save data model not null is correctly`() {
        // when
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.clearLatLong()

        // Then
        Assert.assertFalse(addressFormViewModel.isHaveLatLong)
        Assert.assertNotNull(addressFormViewModel.saveDataModel)
    }

    @Test
    fun `verify isHaveLatLong when has latitude and longitude`() {
        // when
        addressFormViewModel.saveDataModel = saveAddressDataModel.apply {
            latitude = "1.0"
            longitude = "1.0"
        }

        // Then
        Assert.assertTrue(addressFormViewModel.isHaveLatLong)
    }

    @Test
    fun `verify isHaveLatLong when has latitude`() {
        // when
        addressFormViewModel.saveDataModel = saveAddressDataModel.apply {
            latitude = ""
            longitude = "1.0"
        }

        // Then
        Assert.assertTrue(addressFormViewModel.isHaveLatLong)
    }

    @Test
    fun `verify isHaveLatLong when has longitude`() {
        // when
        addressFormViewModel.saveDataModel = saveAddressDataModel.apply {
            latitude = "1.0"
            longitude = ""
        }

        // Then
        Assert.assertTrue(addressFormViewModel.isHaveLatLong)
    }

    @Test
    fun `verify removeSpecialChars is correct`() {
        val value = "123"
        val valueWithCharacter = "123&&"

        val result = addressFormViewModel.removeSpecialChars(valueWithCharacter)

        Assert.assertEquals(result, value)
    }

    @Test
    fun `verify isPhoneNumberValid is true`() {
        val phone = "081000000002"

        Assert.assertTrue(addressFormViewModel.isPhoneNumberValid(phone))
    }

    @Test
    fun `verify isPhoneNumberValid is false`() {
        val phone = "tokped123"

        Assert.assertFalse(addressFormViewModel.isPhoneNumberValid(phone))
    }

    @Test
    fun `verify update data save model when data is null`() {
        // Inject
        val receiverName = "Name"
        val phoneNo = "081000000003"
        val address1 = "Jl. Pramuka"
        val address1Notes = "Notes"
        val addressName = "Address Name"
        val isAnaPositive = "isAnaPositive"

        // When
        addressFormViewModel.updateDataSaveModel(
            receiverName = receiverName,
            phoneNo = phoneNo,
            address1 = address1,
            address1Notes = address1Notes,
            addressName = addressName,
            isAnaPositive = isAnaPositive,
            isTokonow = false
        )

        // Then
        addressFormViewModel.saveDataModel.let { saveAddressDataModel ->
            Assert.assertNotEquals(saveAddressDataModel?.receiverName, receiverName)
            Assert.assertNotEquals(saveAddressDataModel?.phone, phoneNo)
            Assert.assertNotEquals(saveAddressDataModel?.address1, address1)
            Assert.assertNotEquals(saveAddressDataModel?.address1Notes, address1Notes)
            Assert.assertNotEquals(saveAddressDataModel?.addressName, addressName)
            Assert.assertNotEquals(saveAddressDataModel?.isAnaPositive, isAnaPositive)
        }
    }

    @Test
    fun `verify update data save model when data not null`() {
        // Inject
        val receiverName = "Name"
        val phoneNo = "081000000003"
        val address1 = "Jl. Pramuka"
        val address1Notes = "Notes"
        val addressName = "Address Name"
        val isAnaPositive = "isAnaPositive"

        // When
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.updateDataSaveModel(
            receiverName = receiverName,
            phoneNo = phoneNo,
            address1 = address1,
            address1Notes = address1Notes,
            addressName = addressName,
            isAnaPositive = isAnaPositive,
            isTokonow = false
        )

        // Then
        addressFormViewModel.saveDataModel.let { saveAddressDataModel ->
            Assert.assertEquals(saveAddressDataModel?.receiverName, receiverName)
            Assert.assertEquals(saveAddressDataModel?.phone, phoneNo)
            Assert.assertEquals(saveAddressDataModel?.address1, address1)
            Assert.assertEquals(saveAddressDataModel?.address1Notes, address1Notes)
            Assert.assertEquals(saveAddressDataModel?.addressName, addressName)
            Assert.assertEquals(saveAddressDataModel?.isAnaPositive, isAnaPositive)
        }
    }

    @Test
    fun `verify validate phone number error when phone number null`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validatePhoneNumber(
            phoneNumber = null,
            onError = {
                isError = true
            },
            onEmptyPhoneNumber = {},
            onBelowMinCharacter = {},
            onInvalidPhoneNumber = {}
        )

        // Then
        Assert.assertTrue(isError)
    }

    @Test
    fun `verify validate phone number error when phone number empty`() {
        // Inject
        var isError = false
        var isEmpty = false

        // When
        addressFormViewModel.validatePhoneNumber(
            phoneNumber = "",
            onError = {
                isError = true
            },
            onEmptyPhoneNumber = {
                isEmpty = true
            },
            onBelowMinCharacter = {},
            onInvalidPhoneNumber = {}
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isEmpty)
    }

    @Test
    fun `verify validate phone number error when phone number below min character`() {
        // Inject
        var isError = false
        var isBelowMinCharacter = false

        // When
        addressFormViewModel.validatePhoneNumber(
            phoneNumber = "08100",
            onError = {
                isError = true
            },
            onEmptyPhoneNumber = {},
            onBelowMinCharacter = {
                isBelowMinCharacter = true
            },
            onInvalidPhoneNumber = {}
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isBelowMinCharacter)
    }

    @Test
    fun `verify validate phone number error when phone number invalid`() {
        // Inject
        var isError = false
        var isInvalid = false

        // When
        addressFormViewModel.validatePhoneNumber(
            phoneNumber = "08100ABC0182",
            onError = {
                isError = true
            },
            onEmptyPhoneNumber = {},
            onBelowMinCharacter = {},
            onInvalidPhoneNumber = {
                isInvalid = true
            }
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isInvalid)
    }

    @Test
    fun `verify validate phone number is valid`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validatePhoneNumber(
            phoneNumber = "081000000843",
            onError = {
                isError = true
            },
            onEmptyPhoneNumber = {},
            onBelowMinCharacter = {},
            onInvalidPhoneNumber = {}
        )

        // Then
        Assert.assertFalse(isError)
    }

    @Test
    fun `verify validate receiver name error when phone number null`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validateReceiverName(
            receiverName = null,
            onError = {
                isError = true
            },
            onEmptyReceiverName = {},
            onBelowMinCharacter = {}
        )

        // Then
        Assert.assertTrue(isError)
    }

    @Test
    fun `verify validate receiver name error when phone number empty`() {
        // Inject
        var isError = false
        var isEmpty = false

        // When
        addressFormViewModel.validateReceiverName(
            receiverName = "",
            onError = {
                isError = true
            },
            onEmptyReceiverName = {
                isEmpty = true
            },
            onBelowMinCharacter = {}
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isEmpty)
    }

    @Test
    fun `verify validate receiver name error when phone number below min character`() {
        // Inject
        var isError = false
        var isBelowMinCharacter = false

        // When
        addressFormViewModel.validateReceiverName(
            receiverName = "a",
            onError = {
                isError = true
            },
            onEmptyReceiverName = {},
            onBelowMinCharacter = {
                isBelowMinCharacter = true
            }
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isBelowMinCharacter)
    }

    @Test
    fun `verify validate receiver name is valid`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validateReceiverName(
            receiverName = "Bambang",
            onError = {
                isError = true
            },
            onEmptyReceiverName = {},
            onBelowMinCharacter = {}
        )

        // Then
        Assert.assertFalse(isError)
    }

    @Test
    fun `verify validate address error when phone number null`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validateAddress(
            address = null,
            onError = {
                isError = true
            },
            onEmptyAddress = {},
            onBelowMinCharacter = {},
            isErrorTextField = false
        )

        // Then
        Assert.assertTrue(isError)
    }

    @Test
    fun `verify validate address name error when phone number empty`() {
        // Inject
        var isError = false
        var isEmpty = false

        // When
        addressFormViewModel.validateAddress(
            address = "",
            onError = {
                isError = true
            },
            onEmptyAddress = {
                isEmpty = true
            },
            onBelowMinCharacter = {},
            isErrorTextField = false
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isEmpty)
    }

    @Test
    fun `verify validate address error when phone number below min character`() {
        // Inject
        var isError = false
        var isBelowMinCharacter = false

        // When
        addressFormViewModel.validateAddress(
            address = "ab",
            onError = {
                isError = true
            },
            onEmptyAddress = {},
            onBelowMinCharacter = {
                isBelowMinCharacter = true
            },
            isErrorTextField = false
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isBelowMinCharacter)
    }

    @Test
    fun `verify validate address error when is error text field`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validateAddress(
            address = "abcd",
            onError = {
                isError = true
            },
            onEmptyAddress = {},
            onBelowMinCharacter = {},
            isErrorTextField = true
        )

        // Then
        Assert.assertTrue(isError)
    }

    @Test
    fun `verify validate address is valid`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validateAddress(
            address = "Jl. Patimura 27 AB",
            onError = {
                isError = true
            },
            onEmptyAddress = {},
            onBelowMinCharacter = {},
            isErrorTextField = false
        )

        // Then
        Assert.assertFalse(isError)
    }

    @Test
    fun `verify validate label error when phone number null`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validateLabel(
            label = null,
            onError = {
                isError = true
            },
            onEmptyLabel = {},
            onBelowMinCharacter = {}
        )

        // Then
        Assert.assertTrue(isError)
    }

    @Test
    fun `verify validate label error when phone number empty`() {
        // Inject
        var isError = false
        var isEmpty = false

        // When
        addressFormViewModel.validateLabel(
            label = "",
            onError = {
                isError = true
            },
            onEmptyLabel = {
                isEmpty = true
            },
            onBelowMinCharacter = {}
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isEmpty)
    }

    @Test
    fun `verify validate label error when phone number below min character`() {
        // Inject
        var isError = false
        var isBelowMinCharacter = false

        // When
        addressFormViewModel.validateLabel(
            label = "ab",
            onError = {
                isError = true
            },
            onEmptyLabel = {},
            onBelowMinCharacter = {
                isBelowMinCharacter = true
            }
        )

        // Then
        Assert.assertTrue(isError)
        Assert.assertTrue(isBelowMinCharacter)
    }

    @Test
    fun `verify validate label is valid`() {
        // Inject
        var isError = false

        // When
        addressFormViewModel.validateLabel(
            label = "Rumah warna merah",
            onError = {
                isError = true
            },
            onEmptyLabel = {},
            onBelowMinCharacter = {}
        )

        // Then
        Assert.assertFalse(isError)
    }

    @Test
    fun `verify when isDifferentLatLong is true`() {
        // Inject
        val pinpointLat = "123"
        val pinpointLong = "123"

        // Then
        Assert.assertTrue(
            addressFormViewModel.isDifferentLatLong(
                pinpointLat = pinpointLat,
                pinpointLong = pinpointLong
            )
        )
    }

    @Test
    fun `verify when isDifferentLatLong is false`() {
        // Inject
        val pinpointLat = "123"
        val pinpointLong = "123"

        // When
        addressFormViewModel.saveDataModel = saveAddressDataModel.apply {
            latitude = pinpointLat
            longitude = pinpointLong
        }

        // Then
        Assert.assertFalse(
            addressFormViewModel.isDifferentLatLong(
                pinpointLat = pinpointLat,
                pinpointLong = pinpointLong
            )
        )
    }

    @Test
    fun `verify when isDifferentDistrictId is true`() {
        // Inject
        val pinpointDistrictId = 1L

        // Then
        Assert.assertTrue(
            addressFormViewModel.isDifferentDistrictId(
                pinpointDistrictId = pinpointDistrictId
            )
        )
    }

    @Test
    fun `verify when isDifferentDistrictId is false`() {
        // Inject
        val pinpointDistrictId = 1L

        // When
        addressFormViewModel.saveDataModel = saveAddressDataModel.apply {
            districtId = pinpointDistrictId
        }

        // Then
        Assert.assertFalse(
            addressFormViewModel.isDifferentDistrictId(
                pinpointDistrictId = pinpointDistrictId
            )
        )
    }

    @Test
    fun `verify is not different location is correct`() {
        val address = "Monas Jakarta"
        val latitude = "-6.175392"
        val longitude = "106.827153"

        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(
                    spyk(
                        KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse(
                            address1 = address,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                )
            )
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        Assert.assertFalse(
            addressFormViewModel.isDifferentLocation(
                address1 = address,
                address2 = "$latitude,$longitude"
            )
        )
    }

    @Test
    fun `verify is different location when address 1 not match is correct`() {
        val address = "Monas Jakarta"
        val latitude = "-6.175392"
        val longitude = "106.827153"

        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(
                    spyk(
                        KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse(
                            address1 = address,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                )
            )
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        Assert.assertTrue(
            addressFormViewModel.isDifferentLocation(
                address1 = "",
                address2 = "$latitude,$longitude"
            )
        )
    }

    @Test
    fun `verify is different location when address 2 not match is correct`() {
        val address = "Monas Jakarta"
        val latitude = "-6.175392"
        val longitude = "106.827153"

        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(
                    spyk(
                        KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse(
                            address1 = address,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                )
            )
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        Assert.assertTrue(
            addressFormViewModel.isDifferentLocation(
                address1 = address,
                address2 = "0.0,0.0"
            )
        )
    }

    @Test
    fun `verify is different location empty lat long is correct`() {
        val address = "Monas Jakarta"
        val latitude = "-6.175392"
        val longitude = "106.827153"

        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(
                    spyk(
                        KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse(
                            address1 = address,
                            latitude = "",
                            longitude = ""
                        )
                    )
                )
            )
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        Assert.assertTrue(
            addressFormViewModel.isDifferentLocation(
                address1 = address,
                address2 = "$latitude,$longitude"
            )
        )
    }

    @Test
    fun `verify is different location empty lat is correct`() {
        val address = "Monas Jakarta"
        val latitude = "-6.175392"
        val longitude = "106.827153"

        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(
                    spyk(
                        KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse(
                            address1 = address,
                            latitude = "",
                            longitude = longitude
                        )
                    )
                )
            )
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        Assert.assertTrue(
            addressFormViewModel.isDifferentLocation(
                address1 = address,
                address2 = "$latitude,$longitude"
            )
        )
    }

    @Test
    fun `verify is different location empty long is correct`() {
        val address = "Monas Jakarta"
        val latitude = "-6.175392"
        val longitude = "106.827153"

        // Given
        coEvery { repo.getAddressDetail(any(), any(), true) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(
                    spyk(
                        KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse(
                            address1 = address,
                            latitude = latitude,
                            longitude = ""
                        )
                    )
                )
            )
        )

        // When
        addressFormViewModel.getAddressDetail(addressId, sourceValue, null)

        // Then
        Assert.assertTrue(
            addressFormViewModel.isDifferentLocation(
                address1 = address,
                address2 = "$latitude,$longitude"
            )
        )
    }

    @Test
    fun `verify when generateSaveDataModel when parameter saveDataModel is null`() {
        // Inject
        val defaultName = "Mike"
        val defaultPhone = "0817389274839"
        // Given
        val saveDataModel = addressFormViewModel.generateSaveDataModel(
            saveDataModel = null,
            defaultName = defaultName,
            defaultPhone = defaultPhone
        )

        // Then
        with(saveDataModel) {
            Assert.assertEquals(receiverName, defaultName)
            Assert.assertEquals(phone, defaultPhone)
        }
    }

    @Test
    fun `verify when generateSaveDataModel when default name & phone is not empty`() {
        // Inject
        val defaultName = "Mike"
        val defaultPhone = "0817389274839"

        // Given
        val saveDataModel = addressFormViewModel.generateSaveDataModel(
            saveDataModel = spyk(),
            defaultName = defaultName,
            defaultPhone = defaultPhone
        )

        // Then
        with(saveDataModel) {
            Assert.assertEquals(receiverName, defaultName)
            Assert.assertEquals(phone, defaultPhone)
        }
    }

    @Test
    fun `verify when generateSaveDataModel when default name & phone is empty`() {
        // Inject
        val defaultName = ""
        val defaultPhone = ""

        // Given
        val saveDataModel = addressFormViewModel.generateSaveDataModel(
            saveDataModel = spyk(),
            defaultName = defaultName,
            defaultPhone = defaultPhone
        )

        // Then
        with(saveDataModel) {
            Assert.assertEquals(receiverName, defaultName)
            Assert.assertEquals(phone, defaultPhone)
        }
    }
}
