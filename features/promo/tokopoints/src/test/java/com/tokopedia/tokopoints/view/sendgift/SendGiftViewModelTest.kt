package com.tokopedia.tokopoints.view.sendgift

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity
import com.tokopedia.tokopoints.view.model.ValidateCouponEntity
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import java.lang.NullPointerException
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
class SendGiftViewModelTest {


    lateinit var viewModel: SendGiftViewModel
    val respository = mockk<SendGiftRespository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = SendGiftViewModel(respository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendGift success case`() {
        val observer = mockk<Observer<Resources<SendGiftData<out Any, out Any>>>>() {
            every { onChanged(any()) } just Runs
        }
        coEvery { respository.sendGift(null, "email", "notes") } returns mockk {
            every { hachikoRedeem } returns mockk()
        }
        viewModel.sendGiftLiveData.observeForever(observer)
        viewModel.sendGift(null, "email", "notes")

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<SendGiftData<out Any, out Any>>>))
            observer.onChanged(ofType(Success::class as KClass<Success<SendGiftData<out Any, out Any>>>))
        }
    }

    @Test
    fun `sendGift error from server`() {
        val observer = mockk<Observer<Resources<SendGiftData<out Any, out Any>>>>() {
            every { onChanged(any()) } just Runs
        }
        coEvery { respository.sendGift(null, "email", "notes") } throws mockk<MessageErrorException> {
            every { message } returns "title|message|,"
        }
        viewModel.sendGiftLiveData.observeForever(observer)
        viewModel.sendGift(null, "email", "notes")

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<SendGiftData<out Any, out Any>>>))
            observer.onChanged(ofType(Success::class as KClass<Success<SendGiftData<out Any, out Any>>>))
        }
        val result = viewModel.sendGiftLiveData.value as Success
        assert(result.data.title == "title")
        assert(result.data.messsage == "message")
    }

    @Test
    fun `sendGift error from internal`() {
        val observer = mockk<Observer<Resources<SendGiftData<out Any, out Any>>>>() {
            every { onChanged(any()) } just Runs
        }
        coEvery { respository.sendGift(null, "email", "notes") } throws mockk<NullPointerException> {}
        viewModel.sendGiftLiveData.observeForever(observer)
        viewModel.sendGift(null, "email", "notes")

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<SendGiftData<out Any, out Any>>>))
            observer.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<SendGiftData<out Any, out Any>>>))
        }
    }


    @Test
    fun `preValidateGift Success`() {
        val observer = mockk<Observer<Resources<Nothing?>>>() {
            every { onChanged(any()) } just Runs
        }
        coEvery { respository.preValidateGift(null, "email") } returns mockk {
            every { validateCoupon } returns mockk {
                every { isValid } returns 1
            }
        }
        viewModel.prevalidateLiveData.observeForever(observer)
        viewModel.preValidateGift(null, "email")

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<Nothing?>>))
            observer.onChanged(ofType(Success::class as KClass<Success<Nothing?>>))
        }

        assert(null == (viewModel.prevalidateLiveData.value as Success).data)
    }

    @Test
    fun `preValidateGift error`() {
        val observer = mockk<Observer<Resources<Nothing?>>>() {
            every { onChanged(any()) } just Runs
        }
        coEvery { respository.preValidateGift(null, "email") } throws mockk<MessageErrorException> {
            every { message } returns "message"
        }
        viewModel.prevalidateLiveData.observeForever(observer)
        viewModel.preValidateGift(null, "email")

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<Nothing?>>))
            //    observer.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<Nothing?>>))
        }

        /*   val result = viewModel.prevalidateLiveData.value as ErrorMessage
           assert(result.data == "message")*/
    }

    @Test
    fun `given error message size equals 2 when sendGift throws error should set first message as SendGiftData message`() {
        val message = "first err message|second err message"
        val error = MessageErrorException(message)

        val id = 1
        val email = "test@mail.com"
        val notes = "this is just a test"

        coEvery { respository.sendGift(id, email, notes) } throws error

        viewModel.sendGift(id, email, notes)

        val sendGiftLiveData = viewModel.sendGiftLiveData.value

        val expectedMessage = "first err message"
        val actualMessage = (sendGiftLiveData as Success<SendGiftData<out Any, out Any>>).data.messsage

        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun `given error message null when sendGift throws error should set first message as SendGiftData message`() {
        val message = null
        val error = MessageErrorException(message)

        val id = 1
        val email = "test@mail.com"
        val notes = "this is just a test"

        coEvery { respository.sendGift(id, email, notes) } throws error

        viewModel.sendGift(id, email, notes)

        val sendGiftLiveData = viewModel.sendGiftLiveData.value

        val actualMessage = (sendGiftLiveData as? Success<SendGiftData<out Any, out Any>>)?.data?.messsage

        assertTrue(actualMessage != null)
    }

    @Test
    fun `given isValid response NOT equals 1 when preValidateGift should update prevalidateLiveData with ErrorMessage`() {
        val isValid = 2
        val validateCouponResponse = ValidateCouponBaseEntity(
            validateCoupon = ValidateCouponEntity(isValid = isValid)
        )

        val id = 1
        val email = "tokopoints@mail.com"

        coEvery { respository.preValidateGift(id, email) } returns validateCouponResponse

        viewModel.preValidateGift(id, email)

        viewModel.prevalidateLiveData
            .verifyValueEquals(ErrorMessage<Nothing>(""))
    }

    @Test
    fun `given error message when preValidateGift should update prevalidateLiveData with ErrorMessage from exception message`() {
        val errorMessage = "[so]complicated|way|to|get|error|message]"
        val exception = MessageErrorException(errorMessage)

        val id = 1
        val email = "tokopoints@mail.com"

        coEvery { respository.preValidateGift(id, email) } throws exception

        viewModel.preValidateGift(id, email)

        viewModel.prevalidateLiveData
            .verifyValueEquals(ErrorMessage<Nothing>("complicated"))
    }
}
