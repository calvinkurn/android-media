package com.tokopedia.oneclickcheckout.preference.edit.view.summary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.AddressModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.PaymentModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ShipmentModel
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.FakeCreatePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.delete.FakeDeletePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.get.GetPreferenceByIdUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.UpdatePreferenceUseCase
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddress
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddressRequestHelper
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreferenceSummaryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val successMessage = "Testing Success"

    private val getPreferenceByIdUseCase: GetPreferenceByIdUseCase = mockk(relaxed = true)
    private val createPreferenceUseCase = FakeCreatePreferenceUseCase()
    private val deletePreferenceUseCase = FakeDeletePreferenceUseCase()
    private val updatePreferenceUseCase: UpdatePreferenceUseCase = mockk()
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper = mockk()

    private lateinit var preferenceSummaryViewModel: PreferenceSummaryViewModel

    @Before
    fun setUp() {
        preferenceSummaryViewModel = PreferenceSummaryViewModel(getPreferenceByIdUseCase, createPreferenceUseCase, deletePreferenceUseCase, updatePreferenceUseCase)
    }

    @Test
    fun `Get Preference Detail Success`() {
        val response = ProfilesItemModel()
        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")

        assertEquals(OccState.Success(response), preferenceSummaryViewModel.preference.value)
    }

    @Test
    fun `Get Preference Detail Failed`() {
        val response = Throwable()
        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[1] as ((Throwable) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")

        assertEquals(OccState.Failed(Failure(response)), preferenceSummaryViewModel.preference.value)
    }

    @Test
    fun `Delete Preference Success`() {
        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(ProfilesItemModel())
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        preferenceSummaryViewModel.deletePreference(0)

        assertEquals(OccState.Loading, preferenceSummaryViewModel.editResult.value)

        deletePreferenceUseCase.invokeOnSuccess(successMessage)

        assertEquals(OccState.Success(successMessage), preferenceSummaryViewModel.editResult.value)
    }

    @Test
    fun `Delete Preference Failed`() {
        val response = Throwable()
        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(ProfilesItemModel())
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        preferenceSummaryViewModel.deletePreference(0)

        assertEquals(OccState.Loading, preferenceSummaryViewModel.editResult.value)

        deletePreferenceUseCase.invokeOnError(response)

        assertEquals(OccState.Failed(Failure(response)), preferenceSummaryViewModel.editResult.value)
    }

    @Test
    fun `Delete Preference On Invalid State`() {
        preferenceSummaryViewModel.deletePreference(0)

        assertNull(preferenceSummaryViewModel.editResult.value)
    }

    @Test
    fun `Create Preference Success`() {
        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        preferenceSummaryViewModel.createPreference(0, 0, "", "", true, 0, AddressModel(), false)

        assertEquals(OccState.Loading, preferenceSummaryViewModel.editResult.value)

        createPreferenceUseCase.invokeOnSuccess(successMessage)

        assertEquals(OccState.Success(successMessage), preferenceSummaryViewModel.editResult.value)
    }

    @Test
    fun `Create Preference Failed`() {
        val response = Throwable()

        every { chosenAddressRequestHelper.getChosenAddress() } returns ChosenAddress()

        preferenceSummaryViewModel.createPreference(0, 0, "", "", true, 0, AddressModel(), false)

        assertEquals(OccState.Loading, preferenceSummaryViewModel.editResult.value)

        createPreferenceUseCase.invokeOnError(response)

        assertEquals(OccState.Failed(Failure(response)), preferenceSummaryViewModel.editResult.value)
    }

    @Test
    fun `Update Preference Success`() {
        every {
            updatePreferenceUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.editResult.value)
            (secondArg() as ((String) -> Unit)).invoke(successMessage)
        }

        preferenceSummaryViewModel.updatePreference(0, 0, 0, "", "", true, 0, AddressModel(), false)

        assertEquals(OccState.Success(successMessage), preferenceSummaryViewModel.editResult.value)
    }

    @Test
    fun `Update Preference Failed`() {
        val response = Throwable()
        every {
            updatePreferenceUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.editResult.value)
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.updatePreference(0, 0, 0, "", "", true, 0, AddressModel(), false)

        assertEquals(OccState.Failed(Failure(response)), preferenceSummaryViewModel.editResult.value)
    }

    @Test
    fun `Consume Edit Result Fail`() {
        val response = Throwable()
        every {
            updatePreferenceUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.editResult.value)
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.updatePreference(0, 0, 0, "", "", true, 0, AddressModel(), false)

        //consume failure
        (preferenceSummaryViewModel.editResult.value as OccState.Failed).getFailure()

        assertEquals(null, (preferenceSummaryViewModel.editResult.value as OccState.Failed).getFailure())
    }

    @Test
    fun `Is Data Changed First State`() {
        val isDataChanged = preferenceSummaryViewModel.isDataChanged()

        assertEquals(false, isDataChanged)
    }

    @Test
    fun `Is Data Changed With Same Data`() {
        val response = ProfilesItemModel(AddressModel(addressId = 1), ShipmentModel(serviceId = 1), 1, PaymentModel(gatewayCode = "1", metadata = "1"))

        preferenceSummaryViewModel.apply {
            setProfileAddressId(1)
            setProfileServiceId(1)
            setProfileGatewayCode("1")
            setProfilePaymentMetadata("1")
        }

        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        val isDataChanged = preferenceSummaryViewModel.isDataChanged()

        assertEquals(false, isDataChanged)
    }

    @Test
    fun `Is Data Changed With All Different Data`() {
        val response = ProfilesItemModel(AddressModel(addressId = 1), ShipmentModel(serviceId = 1), 1, PaymentModel(gatewayCode = "1", metadata = "1"))

        preferenceSummaryViewModel.apply {
            setProfileAddressId(2)
            setProfileServiceId(2)
            setProfileGatewayCode("2")
            setProfilePaymentMetadata("2")
        }

        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        val isDataChanged = preferenceSummaryViewModel.isDataChanged()

        assertEquals(true, isDataChanged)
    }

    @Test
    fun `Is Data Changed With Different Address`() {
        val response = ProfilesItemModel(AddressModel(addressId = 1), ShipmentModel(serviceId = 1), 1, PaymentModel(gatewayCode = "1", metadata = "1"))

        preferenceSummaryViewModel.apply {
            setProfileAddressId(2)
            setProfileServiceId(1)
            setProfileGatewayCode("1")
            setProfilePaymentMetadata("1")
        }

        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        val isDataChanged = preferenceSummaryViewModel.isDataChanged()

        assertEquals(true, isDataChanged)
    }

    @Test
    fun `Is Data Changed With Different Shipping`() {
        val response = ProfilesItemModel(AddressModel(addressId = 1), ShipmentModel(serviceId = 1), 1, PaymentModel(gatewayCode = "1", metadata = "1"))

        preferenceSummaryViewModel.apply {
            setProfileAddressId(1)
            setProfileServiceId(2)
            setProfileGatewayCode("1")
            setProfilePaymentMetadata("1")
        }

        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        val isDataChanged = preferenceSummaryViewModel.isDataChanged()

        assertEquals(true, isDataChanged)
    }

    @Test
    fun `Is Data Changed With Different Gateway`() {
        val response = ProfilesItemModel(AddressModel(addressId = 1), ShipmentModel(serviceId = 1), 1, PaymentModel(gatewayCode = "1", metadata = "1"))

        preferenceSummaryViewModel.apply {
            setProfileAddressId(1)
            setProfileServiceId(1)
            setProfileGatewayCode("2")
            setProfilePaymentMetadata("1")
        }

        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        val isDataChanged = preferenceSummaryViewModel.isDataChanged()

        assertEquals(true, isDataChanged)
    }

    @Test
    fun `Is Data Changed With Different Metadata`() {
        val response = ProfilesItemModel(AddressModel(addressId = 1), ShipmentModel(serviceId = 1), 1, PaymentModel(gatewayCode = "1", metadata = "1"))

        preferenceSummaryViewModel.apply {
            setProfileAddressId(1)
            setProfileServiceId(1)
            setProfileGatewayCode("1")
            setProfilePaymentMetadata("2")
        }

        every {
            getPreferenceByIdUseCase.execute(any(), any(), any())
        } answers {
            assertEquals(OccState.Loading, preferenceSummaryViewModel.preference.value)
            (args[0] as ((ProfilesItemModel) -> Unit)).invoke(response)
        }

        preferenceSummaryViewModel.getPreferenceDetail(0, 0, 0, "", "", "", "")
        val isDataChanged = preferenceSummaryViewModel.isDataChanged()

        assertEquals(true, isDataChanged)
    }
}
