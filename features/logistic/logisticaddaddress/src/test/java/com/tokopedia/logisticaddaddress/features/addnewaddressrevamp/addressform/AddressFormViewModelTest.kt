package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.response.*
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.FieldType
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.Runs
import io.mockk.just
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
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

    private val districtDetailObserver: Observer<Result<KeroDistrictRecommendation>> =
        mockk(relaxed = true)
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

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        addressFormViewModel = AddressFormViewModel(repo)
        addressFormViewModel.districtDetail.observeForever(districtDetailObserver)
        addressFormViewModel.saveAddress.observeForever(saveAddressObserver)
        addressFormViewModel.defaultAddress.observeForever(defaultAddressObserver)
        addressFormViewModel.editAddress.observeForever(editAddressObserver)
        addressFormViewModel.addressDetail.observeForever(detailAddressObserver)
        addressFormViewModel.pinpointValidation.observeForever(pinpointValidationObserver)
    }

    @Test
    fun `Get District Detail Success`() {
        coEvery { repo.getZipCode(any()) } returns GetDistrictDetailsResponse()
        addressFormViewModel.getDistrictDetail()
        verify { districtDetailObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get District Detail Fail`() {
        coEvery { repo.getZipCode(any()) } throws defaultThrowable
        addressFormViewModel.getDistrictDetail()
        verify { districtDetailObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get Default Address Success`() {
        coEvery { repo.getDefaultAddress(any()) } returns GetDefaultAddressResponse()
        addressFormViewModel.getDefaultAddress("address")
        verify { defaultAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Default Address Fail`() {
        coEvery { repo.getDefaultAddress(any()) } throws defaultThrowable
        addressFormViewModel.getDefaultAddress("address")
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
        coEvery { repo.saveAddress(any(), any()) } returns fakeResponse

        // When
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.saveAddress()

        // Then
        verify { saveAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Address Data Fail`() {
        coEvery { repo.saveAddress(any(), any()) } throws defaultThrowable
        addressFormViewModel.saveDataModel = saveAddressDataModel
        addressFormViewModel.saveAddress()
        verify { saveAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `verify when call save address but save address model is null`() {
        // When
        addressFormViewModel.saveAddress()

        // Then
        Assert.assertNull(addressFormViewModel.saveDataModel)
        verify(exactly = 0) { saveAddressObserver.onChanged(match { true }) }
    }

    @Test
    fun `Get Address Detail Data Success`() {
        // Given
        coEvery { repo.getAddressDetail(any(), any()) } returns KeroGetAddressResponse.Data(
            keroGetAddress = KeroGetAddressResponse.Data.KeroGetAddress(
                data = arrayListOf(spyk())
            )
        )

        // When
        addressFormViewModel.addressId = addressId
        addressFormViewModel.getAddressDetail()

        // Then
        verify { detailAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Address Detail Data Success but empty data list`() {
        // Given
        coEvery { repo.getAddressDetail(any(), any()) } returns KeroGetAddressResponse.Data()

        // When
        addressFormViewModel.addressId = addressId
        addressFormViewModel.getAddressDetail()

        // Then
        verify(exactly = 0) { detailAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get Address Detail Data Fail`() {
        // Given
        coEvery { repo.getAddressDetail(any(), any()) } throws defaultThrowable

        // When
        addressFormViewModel.addressId = addressId
        addressFormViewModel.getAddressDetail()

        // Then
        verify { detailAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Save Edit Address Data Success`() {
        coEvery { repo.editAddress(any(), any()) } returns KeroEditAddressResponse.Data()
        addressFormViewModel.saveEditAddress(saveAddressDataModel)
        verify { editAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Save Edit Address Data Fail`() {
        coEvery { repo.editAddress(any(), any()) } throws defaultThrowable
        addressFormViewModel.saveEditAddress(saveAddressDataModel)
        verify { editAddressObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Pinpoint Validation Data Success`() {
        coEvery {
            repo.pinpointValidation(
                any(),
                any(),
                any(),
                any()
            )
        } returns PinpointValidationResponse()
        addressFormViewModel.validatePinpoint(saveAddressDataModel)
        verify { pinpointValidationObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Pinpoint Validation Data Fail`() {
        coEvery { repo.pinpointValidation(any(), any(), any(), any()) } throws defaultThrowable
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
        coEvery { addressFormViewModel.saveEditAddress(any()) } just Runs
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
    fun `verify when set page source is correctly`() {
        val source = "source"

        addressFormViewModel.source = source

        Assert.assertEquals(addressFormViewModel.source, source)
    }

    @Test
    fun `verify save edit address data success from tokonow`() {
        coEvery { repo.editAddress(any(), any()) } returns KeroEditAddressResponse.Data()
        addressFormViewModel.source = ManageAddressSource.TOKONOW.source
        addressFormViewModel.saveEditAddress(saveAddressDataModel)
        verify { editAddressObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `verify set gms availability flag is correct`() {
        val gmsAvailable = true
        addressFormViewModel.isGmsAvailable = gmsAvailable

        Assert.assertEquals(addressFormViewModel.isGmsAvailable, gmsAvailable)
    }

    @Test
    fun `verify set data from arguments when edit is false is correctly`() {
        // Inject
        val saveDataModel = spyk<SaveAddressDataModel>()
        val source = "source"

        // When
        addressFormViewModel.setDataFromArguments(
            isEdit = false,
            saveDataModel = saveDataModel,
            isPositiveFlow = false,
            addressId = "",
            source = source,
            onViewEditAddressPageNew = {}
        )

        // Then
        with(addressFormViewModel) {
            Assert.assertFalse(this.isEdit)
            Assert.assertEquals(this.saveDataModel, saveDataModel)
            Assert.assertFalse(this.isPositiveFlow)
            Assert.assertEquals(this.source, source)
        }
    }

    @Test
    fun `verify set data from arguments when edit is true is correctly`() {
        // Inject
        val addressId = "123"
        val source = "source"

        // When
        addressFormViewModel.setDataFromArguments(
            isEdit = true,
            saveDataModel = null,
            isPositiveFlow = false,
            addressId = addressId,
            source = source,
            onViewEditAddressPageNew = {}
        )

        // Then
        with(addressFormViewModel) {
            Assert.assertTrue(this.isEdit)
            Assert.assertEquals(this.saveDataModel, null)
            Assert.assertTrue(this.isPositiveFlow)
            Assert.assertEquals(this.addressId, addressId)
            Assert.assertEquals(this.source, source)
        }
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
    fun `verify isHaveLatLong is correctly`() {
        // when
        addressFormViewModel.saveDataModel = saveAddressDataModel.apply {
            latitude = "1.0"
            longitude = "1.0"
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
            isAnaPositive = isAnaPositive
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
            isAnaPositive = isAnaPositive
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
            onInvalidPhoneNumber = {},
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
            onInvalidPhoneNumber = {},
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
            onInvalidPhoneNumber = {},
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
            },
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
            onInvalidPhoneNumber = {},
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
            onBelowMinCharacter = {},
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
            onBelowMinCharacter = {},
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
            onBelowMinCharacter = {},
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
            onBelowMinCharacter = {},
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
            onBelowMinCharacter = {},
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
            onBelowMinCharacter = {},
        )

        // Then
        Assert.assertFalse(isError)
    }

    @Test
    fun `verify validate fields when positive flow is correctly`() {
        // Inject
        val validatePositiveFlow = arrayListOf(
            FieldType.PHONE_NUMBER,
            FieldType.RECEIVER_NAME,
            FieldType.COURIER_NOTE,
            FieldType.ADDRESS,
            FieldType.LABEL
        )

        // When
        addressFormViewModel.isPositiveFlow = true

        // Then
        Assert.assertEquals(addressFormViewModel.validateFields, validatePositiveFlow)
    }

    @Test
    fun `verify validate fields when negative flow is correctly`() {
        // Inject
        val validateNegativeFlow = arrayListOf(
            FieldType.COURIER_NOTE,
            FieldType.ADDRESS,
            FieldType.LABEL,
            FieldType.PHONE_NUMBER,
            FieldType.RECEIVER_NAME
        )

        // When
        addressFormViewModel.isPositiveFlow = false

        // Then
        Assert.assertEquals(addressFormViewModel.validateFields, validateNegativeFlow)
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
}
