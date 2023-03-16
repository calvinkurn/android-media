package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddressError
import com.tokopedia.manageaddress.domain.mapper.SharedAddressMapper
import com.tokopedia.manageaddress.domain.response.shareaddress.DeleteShareAddressResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.GetSharedAddressListResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddrGetSharedAddressList
import com.tokopedia.manageaddress.domain.response.shareaddress.SaveShareAddressResponse
import com.tokopedia.manageaddress.domain.usecase.shareaddress.DeleteFromFriendAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.GetSharedAddressListUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SaveFromFriendAddressUseCase
import com.tokopedia.manageaddress.ui.uimodel.FromFriendAddressActionState
import com.tokopedia.manageaddress.ui.uimodel.FromFriendAddressListState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FromFriendViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getShareAddressUseCase = mockk<GetSharedAddressListUseCase>(relaxed = true)
    private val shareAddressMapper = mockk<SharedAddressMapper>(relaxed = true)
    private val saveShareAddressUseCase = mockk<SaveFromFriendAddressUseCase>(relaxed = true)
    private val deleteFromFriendAddressUseCase =
        mockk<DeleteFromFriendAddressUseCase>(relaxed = true)
    private val getShareAddressObserver =
        mockk<Observer<FromFriendAddressListState>>(relaxed = true)
    private val saveShareAddressObserver =
        mockk<Observer<FromFriendAddressActionState>>(relaxed = true)
    private val deleteShareAddressObserver =
        mockk<Observer<FromFriendAddressActionState>>(relaxed = true)

    lateinit var viewModel: FromFriendViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = FromFriendViewModel(
            getShareAddressUseCase,
            shareAddressMapper,
            saveShareAddressUseCase,
            deleteFromFriendAddressUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.getFromFriendAddressState.observeForever(getShareAddressObserver)
        viewModel.saveAddressState.observeForever(saveShareAddressObserver)
        viewModel.deleteAddressState.observeForever(deleteShareAddressObserver)
    }

    @Test
    fun `verify when get share address success`() {
        val source = "notification"
        val keroAddrGetSharedAddressList = spyk(KeroAddrGetSharedAddressList())
        val mockResponse = spyk(GetSharedAddressListResponse().apply {
            keroGetSharedAddressList = keroAddrGetSharedAddressList
        })

        coEvery {
            getShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.source = source
        viewModel.getFromFriendAddressList()

        Assert.assertEquals(viewModel.source, source)
        verify {
            getShareAddressObserver.onChanged(
                FromFriendAddressListState.Success(
                    keroAddrGetSharedAddressList
                )
            )
        }
    }

    @Test
    fun `verify when get share address error`() {
        coEvery {
            getShareAddressUseCase.invoke(any())
        } throws mockThrowable

        viewModel.getFromFriendAddressList()

        verify {
            getShareAddressObserver.onChanged(
                FromFriendAddressListState.Fail(
                    mockThrowable,
                    mockThrowable.message.orEmpty()
                )
            )
        }
    }

    @Test
    fun `verify when save share address is success`() {
        val message = "Success Save Address"
        val mockResponse = spyk(
            SaveShareAddressResponse(
                data = spyk(
                    SaveShareAddressResponse.KeroAddrSaveSharedAddress(
                        isSuccess = true,
                        message = message
                    )
                )
            )
        )

        coEvery {
            saveShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.saveAddress()

        verify {
            saveShareAddressObserver.onChanged(FromFriendAddressActionState.Success(message))
        }
    }

    @Test
    fun `verify when save share address not success`() {
        val fakeErrorMessage = "error message"
        val mockResponse = spyk(
            SaveShareAddressResponse(
                data = spyk(
                    SaveShareAddressResponse.KeroAddrSaveSharedAddress(
                        isSuccess = false,
                        error = spyk(
                            KeroAddressError(
                                message = fakeErrorMessage
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            saveShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.saveAddress()

        verify {
            saveShareAddressObserver.onChanged(
                FromFriendAddressActionState.Fail(
                    fakeErrorMessage
                )
            )
        }
    }

    @Test
    fun `verify when save share address error`() {
        coEvery {
            saveShareAddressUseCase.invoke(any())
        } throws mockThrowable

        viewModel.saveAddress()

        verify {
            saveShareAddressObserver.onChanged(
                FromFriendAddressActionState.Fail(
                    mockThrowable.message.orEmpty()
                )
            )
        }
    }

    @Test
    fun `verify when delete share address is success`() {
        val message = "Success Delete Address"
        val addressList = arrayListOf<RecipientAddressModel>(spyk(), spyk())
        viewModel.addressList.addAll(addressList)
        val mockResponse = spyk(
            DeleteShareAddressResponse(
                data = spyk(
                    DeleteShareAddressResponse.KeroAddrDeleteSharedAddress(
                        isSuccess = true,
                        message = message
                    )
                )
            )
        )

        coEvery {
            deleteFromFriendAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.onCheckedAddress(0, true)
        viewModel.deleteAddress()
        Thread.sleep(3100)

        Assert.assertEquals(viewModel.addressList.size, addressList.size - 1)
        verify {
            deleteShareAddressObserver.onChanged(FromFriendAddressActionState.Success(message))
        }
    }

    @Test
    fun `verify when delete share address not success`() {
        val addressList = arrayListOf<RecipientAddressModel>(spyk(), spyk())
        viewModel.addressList.addAll(addressList)
        val fakeErrorMessage = "error message"
        val mockResponse = spyk(
            DeleteShareAddressResponse(
                data = spyk(
                    DeleteShareAddressResponse.KeroAddrDeleteSharedAddress(
                        isSuccess = false,
                        error = spyk(
                            KeroAddressError(
                                detail = fakeErrorMessage
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            deleteFromFriendAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.deleteAddress()
        Thread.sleep(3100)

        Assert.assertEquals(viewModel.addressList.size, addressList.size)
    }

    @Test
    fun `verify when delete share address error`() {
        coEvery {
            deleteFromFriendAddressUseCase.invoke(any())
        } throws mockThrowable

        viewModel.deleteAddress()
        Thread.sleep(3100)

        verify {
            deleteShareAddressObserver.onChanged(
                FromFriendAddressActionState.Fail(
                    mockThrowable.message.orEmpty()
                )
            )
        }
    }

    @Test
    fun `verify when cancel delete share address`() {
        viewModel.deleteAddress()
        viewModel.onCancelDeleteAddress()
        Thread.sleep(1000)

        verify {
            deleteShareAddressObserver.onChanged(
                FromFriendAddressActionState.Loading(false)
            )
        }
    }

    @Test
    fun `verify when on checked address is correctly`() {
        viewModel.addressList.add(spyk())

        viewModel.onCheckedAddress(0, isChecked = true)

        Assert.assertTrue(viewModel.isHaveAddressList)
        Assert.assertTrue(viewModel.selectedAddressList.isNotEmpty())
    }

    @Test
    fun `verify when set all list selected is correctly`() {
        viewModel.addressList.add(spyk())

        viewModel.setAllListSelected(true)
        viewModel.isNeedUpdateAllList = true

        Assert.assertTrue(viewModel.isAllSelected)
        Assert.assertTrue(viewModel.isNeedUpdateAllList)
    }

    @Test
    fun `verify when set share address from notif is correctly`() {
        viewModel.isShareAddressFromNotif = true

        Assert.assertTrue(viewModel.isShareAddressFromNotif)
    }
}
