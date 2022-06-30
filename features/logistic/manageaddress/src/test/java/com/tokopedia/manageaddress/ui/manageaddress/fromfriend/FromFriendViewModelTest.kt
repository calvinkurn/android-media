package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.manageaddress.domain.usecase.DeleteFromFriendAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.GetAddressSharedListUseCase
import com.tokopedia.manageaddress.domain.usecase.SaveFromFriendAddressUseCase
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressActionState
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressListState
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

    private val getShareAddressUseCase = mockk<GetAddressSharedListUseCase>(relaxed = true)
    private val saveShareAddressUseCase = mockk<SaveFromFriendAddressUseCase>(relaxed = true)
    private val deleteFromFriendAddressUseCase =
        mockk<DeleteFromFriendAddressUseCase>(relaxed = true)
    private val getShareAddressObserver =
        mockk<Observer<FromFriendAddressListState<AddressListModel>>>(relaxed = true)
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
        val mockResponse = AddressListModel()

        coEvery {
            getShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.onSearchAdrress("")

        verify {
            getShareAddressObserver.onChanged(FromFriendAddressListState.Success(mockResponse))
        }
    }

    @Test
    fun `verify when get share address error`() {
        coEvery {
            getShareAddressUseCase.invoke(any())
        } throws mockThrowable

        viewModel.onSearchAdrress("")

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
        val mockResponse = spyk(ShareAddressResponse().apply {
            shareAddressResponse = spyk(
                ShareAddressResponse.ShareAddressResponse(
                    isSuccess = true
                )
            )
        })

        coEvery {
            saveShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.saveAddress()

        verify {
            saveShareAddressObserver.onChanged(FromFriendAddressActionState.Success)
        }
    }

    @Test
    fun `verify when save share address not success`() {
        val fakeErrorMessage = "error message"
        val mockResponse = spyk(ShareAddressResponse().apply {
            shareAddressResponse = spyk(
                ShareAddressResponse.ShareAddressResponse(
                    isSuccess = false,
                    error = fakeErrorMessage
                )
            )
        })

        coEvery {
            saveShareAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.saveAddress()

        verify {
            saveShareAddressObserver.onChanged(
                FromFriendAddressActionState.Fail(
                    null,
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
                    mockThrowable,
                    mockThrowable.message.orEmpty()
                )
            )
        }
    }

    @Test
    fun `verify when delete share address is success`() {
        val addressList = arrayListOf<RecipientAddressModel>(spyk(), spyk())
        viewModel.chosenAddrId = 0L
        viewModel.addressList.addAll(addressList)
        val mockResponse = spyk(ShareAddressResponse().apply {
            shareAddressResponse = spyk(
                ShareAddressResponse.ShareAddressResponse(
                    isSuccess = true
                )
            )
        })

        coEvery {
            deleteFromFriendAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.onCheckedAddress(0, true)
        viewModel.deleteAddress()
        Thread.sleep(3100)

        Assert.assertEquals(viewModel.addressList.size, addressList.size - 1)
        verify {
            deleteShareAddressObserver.onChanged(FromFriendAddressActionState.Success)
        }
    }

    @Test
    fun `verify when delete share address not success`() {
        val fakeErrorMessage = "error message"
        val mockResponse = spyk(ShareAddressResponse().apply {
            shareAddressResponse = spyk(
                ShareAddressResponse.ShareAddressResponse(
                    isSuccess = false,
                    error = fakeErrorMessage
                )
            )
        })

        coEvery {
            deleteFromFriendAddressUseCase.invoke(any())
        } returns mockResponse

        viewModel.deleteAddress()
        Thread.sleep(3100)

        verify {
            deleteShareAddressObserver.onChanged(
                FromFriendAddressActionState.Fail(
                    null,
                    fakeErrorMessage
                )
            )
        }
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
                    mockThrowable,
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
        Assert.assertTrue(viewModel.getSelectedAddressList().isNotEmpty())
    }

    @Test
    fun `verify when set all list selected is correctly`() {
        viewModel.addressList.add(spyk())

        viewModel.setAllListSelected(true)
        viewModel.isNeedUpdateAllList = true

        Assert.assertTrue(viewModel.isAllSelected)
        Assert.assertTrue(viewModel.isNeedUpdateAllList)
    }
}