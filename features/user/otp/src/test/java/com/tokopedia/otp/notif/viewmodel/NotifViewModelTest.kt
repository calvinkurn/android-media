package com.tokopedia.otp.notif.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.otp.notif.domain.pojo.*
import com.tokopedia.otp.notif.domain.usecase.ChangeStatusPushNotifUseCase
import com.tokopedia.otp.notif.domain.usecase.DeviceStatusPushNotifUseCase
import com.tokopedia.otp.notif.domain.usecase.VerifyPushNotifExpUseCase
import com.tokopedia.otp.notif.domain.usecase.VerifyPushNotifUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class NotifViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var changeStatusPushNotifUseCase: ChangeStatusPushNotifUseCase
    @RelaxedMockK
    lateinit var deviceStatusPushNotifUseCase: DeviceStatusPushNotifUseCase
    @RelaxedMockK
    lateinit var verifyPushNotifUseCase: VerifyPushNotifUseCase
    @RelaxedMockK
    lateinit var verifyPushNotifExpUseCase: VerifyPushNotifExpUseCase
    @RelaxedMockK
    lateinit var changeStatusPushNotifResultObserver: Observer<Result<ChangeStatusPushNotifData>>
    @RelaxedMockK
    lateinit var deviceStatusPushNotifResultObserver: Observer<Result<DeviceStatusPushNotifData>>
    @RelaxedMockK
    lateinit var verifyPushNotifResultObserver: Observer<Result<VerifyPushNotifData>>
    @RelaxedMockK
    lateinit var verifyPushNotifExpResultObserver: Observer<Result<VerifyPushNotifExpData>>

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private lateinit var viewmodel: NotifViewModel

    private val data = ChangeStatusPushNotifData(success = true)
    private val successChangeStatusPushNotifResponse = ChangeStatusPushNotifPojo(data)

    private val deviceStatusData = DeviceStatusPushNotifData(success = true)
    private val successDeviceStatusPushNotifResponse = DeviceStatusPushNotifPojo(deviceStatusData)

    private val verifyPushNotifData = VerifyPushNotifData(success = true, imglink = "imgLink", messageTitle = "title", messageBody = "body", ctaType = "cta type")
    private val successVerifyPushNotifResponse = VerifyPushNotifPojo(verifyPushNotifData)

    private val verifyPushNotifExpData = VerifyPushNotifExpData(success = true)
    private val successVerifyPushNotifExpResponse = VerifyPushNotifExpPojo(verifyPushNotifExpData)

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewmodel = NotifViewModel(
                changeStatusPushNotifUseCase,
                deviceStatusPushNotifUseCase,
                verifyPushNotifUseCase,
                verifyPushNotifExpUseCase,
                dispatcherProviderTest
        )
    }

    @Test
    fun `Success change status push notif`() {
        viewmodel.changeStatusPushNotifResult.observeForever(changeStatusPushNotifResultObserver)
        coEvery { changeStatusPushNotifUseCase.getData(any()) } returns successChangeStatusPushNotifResponse

        viewmodel.changeStatusPushNotif(true)

        verify { changeStatusPushNotifResultObserver.onChanged(any<Success<ChangeStatusPushNotifData>>()) }
        assert(viewmodel.changeStatusPushNotifResult.value is Success)

        val result = viewmodel.changeStatusPushNotifResult.value as Success<ChangeStatusPushNotifData>
        assert(result.data == successChangeStatusPushNotifResponse.data)
    }

    @Test
    fun `Failed change status push notif error message not empty`() {
        successChangeStatusPushNotifResponse.data.success = false
        successChangeStatusPushNotifResponse.data.errorMessage = error

        viewmodel.changeStatusPushNotifResult.observeForever(changeStatusPushNotifResultObserver)
        coEvery { changeStatusPushNotifUseCase.getData(any()) } returns successChangeStatusPushNotifResponse

        viewmodel.changeStatusPushNotif(true)

        verify { changeStatusPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.changeStatusPushNotifResult.value is Fail)

        val result = viewmodel.changeStatusPushNotifResult.value as Fail
        assert(result.throwable.message == error)
    }

    @Test
    fun `Failed change status push notif error message empty & success == false`() {
        successChangeStatusPushNotifResponse.data.success = false
        successChangeStatusPushNotifResponse.data.errorMessage = ""

        viewmodel.changeStatusPushNotifResult.observeForever(changeStatusPushNotifResultObserver)
        coEvery { changeStatusPushNotifUseCase.getData(any()) } returns successChangeStatusPushNotifResponse

        viewmodel.changeStatusPushNotif(true)

        verify { changeStatusPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.changeStatusPushNotifResult.value is Fail)
    }

    @Test
    fun `Failed change status push notif`() {
        viewmodel.changeStatusPushNotifResult.observeForever(changeStatusPushNotifResultObserver)
        coEvery { changeStatusPushNotifUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.changeStatusPushNotif(true)

        verify { changeStatusPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.changeStatusPushNotifResult.value is Fail)

        val result = viewmodel.changeStatusPushNotifResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get device status push notif`() {
        viewmodel.deviceStatusPushNotifResult.observeForever(deviceStatusPushNotifResultObserver)
        coEvery { deviceStatusPushNotifUseCase.getData(any()) } returns successDeviceStatusPushNotifResponse

        viewmodel.deviceStatusPushNotif()

        verify { deviceStatusPushNotifResultObserver.onChanged(any<Success<DeviceStatusPushNotifData>>()) }
        assert(viewmodel.deviceStatusPushNotifResult.value is Success)

        val result = viewmodel.deviceStatusPushNotifResult.value as Success<DeviceStatusPushNotifData>
        assert(result.data == successDeviceStatusPushNotifResponse.data)
    }

    @Test
    fun `Failed get device status push notif error msg not empty `() {
        successDeviceStatusPushNotifResponse.data.errorMessage = error
        successDeviceStatusPushNotifResponse.data.success = false

        viewmodel.deviceStatusPushNotifResult.observeForever(deviceStatusPushNotifResultObserver)
        coEvery { deviceStatusPushNotifUseCase.getData(any()) } returns successDeviceStatusPushNotifResponse

        viewmodel.deviceStatusPushNotif()

        verify { deviceStatusPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.deviceStatusPushNotifResult.value is Fail)

        val result = viewmodel.deviceStatusPushNotifResult.value as Fail
        assert(result.throwable.message == error)
    }

    @Test
    fun `Failed get device status push notif other errors `() {
        successDeviceStatusPushNotifResponse.data.errorMessage = ""
        successDeviceStatusPushNotifResponse.data.success = false

        viewmodel.deviceStatusPushNotifResult.observeForever(deviceStatusPushNotifResultObserver)
        coEvery { deviceStatusPushNotifUseCase.getData(any()) } returns successDeviceStatusPushNotifResponse

        viewmodel.deviceStatusPushNotif()

        verify { deviceStatusPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.deviceStatusPushNotifResult.value is Fail)
    }

    @Test
    fun `Failed get device status push notif`() {
        viewmodel.deviceStatusPushNotifResult.observeForever(deviceStatusPushNotifResultObserver)
        coEvery { deviceStatusPushNotifUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.deviceStatusPushNotif()

        verify { deviceStatusPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.deviceStatusPushNotifResult.value is Fail)

        val result = viewmodel.deviceStatusPushNotifResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success verify push notif`() {
        viewmodel.verifyPushNotifResult.observeForever(verifyPushNotifResultObserver)
        coEvery { verifyPushNotifUseCase.getData(any()) } returns successVerifyPushNotifResponse

        viewmodel.verifyPushNotif("", "", "")

        verify { verifyPushNotifResultObserver.onChanged(any<Success<VerifyPushNotifData>>()) }
        assert(viewmodel.verifyPushNotifResult.value is Success)

        val result = viewmodel.verifyPushNotifResult.value as Success<VerifyPushNotifData>
        assert(result.data == successVerifyPushNotifResponse.data)
    }

    @Test
    fun `Failed verify push notif error msg not empty`() {
        successVerifyPushNotifResponse.data.imglink = ""
        successVerifyPushNotifResponse.data.errorMessage = error

        viewmodel.verifyPushNotifResult.observeForever(verifyPushNotifResultObserver)
        coEvery { verifyPushNotifUseCase.getData(any()) } returns successVerifyPushNotifResponse

        viewmodel.verifyPushNotif("", "", "")

        verify { verifyPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyPushNotifResult.value is Fail)

        val result = viewmodel.verifyPushNotifResult.value as Fail
        assert(result.throwable.message == error)
    }

    @Test
    fun `Failed verify push notif msg not empty`() {
        successVerifyPushNotifResponse.data.imglink = ""
        successVerifyPushNotifResponse.data.errorMessage = ""
        successVerifyPushNotifResponse.data.message = error

        viewmodel.verifyPushNotifResult.observeForever(verifyPushNotifResultObserver)
        coEvery { verifyPushNotifUseCase.getData(any()) } returns successVerifyPushNotifResponse

        viewmodel.verifyPushNotif("", "", "")

        verify { verifyPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyPushNotifResult.value is Fail)
    }

    @Test
    fun `Failed verify push notif other errors`() {
        successVerifyPushNotifResponse.data.imglink = ""
        successVerifyPushNotifResponse.data.errorMessage = ""
        successVerifyPushNotifResponse.data.message = ""

        viewmodel.verifyPushNotifResult.observeForever(verifyPushNotifResultObserver)
        coEvery { verifyPushNotifUseCase.getData(any()) } returns successVerifyPushNotifResponse

        viewmodel.verifyPushNotif("", "", "")

        verify { verifyPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyPushNotifResult.value is Fail)
    }

    @Test
    fun `Failed verify push notif`() {
        viewmodel.verifyPushNotifResult.observeForever(verifyPushNotifResultObserver)
        coEvery { verifyPushNotifUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.verifyPushNotif("", "", "")

        verify { verifyPushNotifResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyPushNotifResult.value is Fail)

        val result = viewmodel.verifyPushNotifResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success verify push notif expiration`() {
        viewmodel.verifyPushNotifExpResult.observeForever(verifyPushNotifExpResultObserver)
        coEvery { verifyPushNotifExpUseCase.getData(any()) } returns successVerifyPushNotifExpResponse

        viewmodel.verifyPushNotifExp("", "", "")

        verify { verifyPushNotifExpResultObserver.onChanged(any<Success<VerifyPushNotifExpData>>()) }
        assert(viewmodel.verifyPushNotifExpResult.value is Success)

        val result = viewmodel.verifyPushNotifExpResult.value as Success<VerifyPushNotifExpData>
        assert(result.data == successVerifyPushNotifExpResponse.data)
    }

    @Test
    fun `Failed verify push notif expiration error msg not empty`() {
        successVerifyPushNotifExpResponse.data.errorMessage = error

        viewmodel.verifyPushNotifExpResult.observeForever(verifyPushNotifExpResultObserver)
        coEvery { verifyPushNotifExpUseCase.getData(any()) } returns successVerifyPushNotifExpResponse

        viewmodel.verifyPushNotifExp("", "", "")

        verify { verifyPushNotifExpResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyPushNotifExpResult.value is Fail)

        val result = viewmodel.verifyPushNotifExpResult.value as Fail
        assert(result.throwable.message == error)
    }

    @Test
    fun `Failed verify push notif expiration`() {
        viewmodel.verifyPushNotifExpResult.observeForever(verifyPushNotifExpResultObserver)
        coEvery { verifyPushNotifExpUseCase.getData(any()) } coAnswers { throw throwable }

        viewmodel.verifyPushNotifExp("", "", "")

        verify { verifyPushNotifExpResultObserver.onChanged(any<Fail>()) }
        assert(viewmodel.verifyPushNotifExpResult.value is Fail)

        val result = viewmodel.verifyPushNotifExpResult.value as Fail
        assertEquals(throwable, result.throwable)
    }

    companion object {
        private val throwable = Throwable()
        private const val error = "error message"
    }
}