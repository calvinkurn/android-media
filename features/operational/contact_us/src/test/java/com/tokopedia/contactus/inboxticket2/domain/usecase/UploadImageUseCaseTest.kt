package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.UploadImageResponse
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UploadImageUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val context: Context = mockk(relaxed = true)
    private val uploadImageUseCase = mockk<UploadImageUseCase<UploadImageResponse>>(relaxed = true)
    private var contactUsUploadImageUseCase = spyk(ContactUsUploadImageUseCase(context, uploadImageUseCase))
    private lateinit var userSession: UserSessionInterface
    private val list = ArrayList<ImageUpload>()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        userSession = UserSession(context)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check invocation of uploadFile with parameter list as null`() {
        runBlockingTest {
            val list = contactUsUploadImageUseCase.uploadFile("", mockk(relaxed = true), mockk(relaxed = true))
            assertEquals(list.size, 0)
        }
    }

    @Test
    fun `check picObj value on invocation of upload file`() {

        list.add(ImageUpload("", "", "", ""))
        val listfile = listOf<String>("file1")
        val response = mockk<UploadImageResponse>(relaxed = true)


        every { uploadImageUseCase.createObservable(any()).toBlocking().first().dataResultImageUpload } returns response
        every { response.data.picObj } returns "picObj"

        contactUsUploadImageUseCase.uploadFile("", list, listfile)

        assertEquals(list[0].picObj, "picObj")


    }

    @Test
    fun `check path of compressed file on invocation of getFile`() {
        list.add(ImageUpload("", "", "", ""))
        val path = "absolute_path"

        mockkStatic(ImageProcessingUtil::class)
        every { ImageProcessingUtil.compressImageFile(any(), any()).absolutePath } returns path

        val result = contactUsUploadImageUseCase.getFile(list)

        assertEquals(result[0], path)
    }

    @Test()
    fun `check check path of compressed file on invocation of getFile with exception`() {
        list.add(ImageUpload("", "", "", ""))
        val exception = IOException("io")

        mockkStatic(ImageProcessingUtil::class)
        every { ImageProcessingUtil.compressImageFile(any(), any()).absolutePath } throws exception
        every { context.getString(any()) } returns ""

        var result: List<String> = arrayListOf()
        try {
            result = contactUsUploadImageUseCase.getFile(list)
        } catch (e: IOException) {
            assertEquals(result.size, 0)
        }
    }
}