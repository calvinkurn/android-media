package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.orderquery.data.Data
import com.tokopedia.contactus.orderquery.data.ImageUpload
import com.tokopedia.contactus.orderquery.data.ImageUploadResult
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils
import com.tokopedia.core.util.ImageUploadHandler
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.IOException

@ExperimentalCoroutinesApi
class UploadImageUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val context: Context = mockk()

    private var uploadImageUseCase = spyk(UploadImageUseCase(context))
    private lateinit var userSession:UserSessionInterface
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
            val list = uploadImageUseCase.uploadFile(null, mockk(), mockk(), true)
            assertEquals(list.size, 0)
        }
    }

    @Test
    fun `check picSrc and pocObj value on invocation of upload file  when upload result data is not null`() {

        list.add(ImageUpload("", "", "", ""))
        val imageUploadResult = mockk<ImageUploadResult>()
        val listnetcal = mockk<List<NetworkCalculator>>()
        val listfile = mockk<List<File>>()
        val data = mockk<Data>()
        runBlockingTest {

            every { listnetcal[any()] } returns mockk()

            every { listfile[any()] } returns mockk()

            coEvery { uploadImageUseCase.getImageUploadresult(any(), any(), any()) } returns imageUploadResult

            every { imageUploadResult.data } returns data

            every { data.picSrc } returns "picSrc"
            every { data.picObj } returns "picObj"

            uploadImageUseCase.uploadFile(list, listnetcal, listfile, true)

            assertEquals(list[0].picSrc, "picSrc")
            assertEquals(list[0].picObj, "picObj")
        }

    }

    @Test
    fun `check picobj is not null on invokation of upload file  when upload result data is not null`() {

        list.add(ImageUpload("", "", "", ""))
        val imageUploadResult = mockk<ImageUploadResult>()
        val listnetcal = mockk<List<NetworkCalculator>>()
        val listfile = mockk<List<File>>()
        runBlockingTest {

            coEvery {
                listnetcal[any()]
            } returns mockk()

            coEvery {
                listfile[any()]
            } returns mockk()

            coEvery {
                uploadImageUseCase.getImageUploadresult(any(), any(), any())
            } returns imageUploadResult

            coEvery {
                imageUploadResult.data
            } returns mockk(relaxed = true)

            uploadImageUseCase.uploadFile(list, listnetcal, listfile, true)

            assertNotNull(list[0].picObj)
        }

    }

    @Test(expected = RuntimeException::class)
    fun `throwing exception  on invokation of upload file  when upload result data is null`() {

        val exception = RuntimeException("run time")

        list.add(ImageUpload("", "", "", ""))
        val imageUploadResult = mockk<ImageUploadResult>(relaxed = true)
        val listnetcal = mockk<List<NetworkCalculator>>()
        val listfile = mockk<List<File>>()
        runBlockingTest {

            coEvery {
                listnetcal[any()]
            } returns mockk()

            coEvery {
                listfile[any()]
            } returns mockk()

            coEvery {
                uploadImageUseCase.getImageUploadresult(any(), any(), any())
            } returns imageUploadResult

            coEvery {
                imageUploadResult.data
            } returns null

            coEvery {
                imageUploadResult.messageError
            } returns ""

            uploadImageUseCase.uploadFile(list, listnetcal, listfile, true)

        }

    }


    @Test
    fun `check invocation of getFile`() {
        list.add(ImageUpload("", "", "", ""))
        val compressedImage = byteArrayOf()

        runBlockingTest {
            mockkStatic(ImageUploadHandler::class)
            coEvery {
                ImageUploadHandler.writeImageToTkpdPath(byteArrayOf())
            } returns mockk()

            coEvery {
                ImageUploadHandler.compressImage(any())
            } returns compressedImage

            uploadImageUseCase.getFile(list)

            assertEquals(list.size, 1)
        }
    }

    @Test(expected = IOException::class)
    fun `check invocation of getFile with exception`() {
        list.add(ImageUpload("", "", "", ""))
        val compressedImage = byteArrayOf()

        runBlockingTest {
            mockkStatic(ImageUploadHandler::class)
            coEvery {
                ImageUploadHandler.writeImageToTkpdPath(byteArrayOf())
            } throws IOException("nbXHJ")

            coEvery {
                ImageUploadHandler.compressImage(any())
            } returns compressedImage

            coEvery {
                context.getString(any())
            } returns ""

            uploadImageUseCase.getFile(list)

            assertEquals(list.size, 0)


        }
    }


    @Test
    fun `check invocation of uploadImage on invocation of getImageUploadResult when loged in`() {

        val netcal = mockk<NetworkCalculator>()
        val file = mockk<File>()

        runBlockingTest {

            coEvery {
                netcal.content
            } returns HashMap()

            coEvery {
                netcal.header
            } returns HashMap()

            mockkStatic(RetrofitUtils::class)
            coEvery {
                RetrofitUtils.createRetrofit(IMAGE_UPLOAD_URL)
                        .create(UploadImageContactUs::class.java)
                        .uploadImage(
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any()
                        )
            } returns mockk()


            uploadImageUseCase.getImageUploadresult(netcal, file, true)

            coVerify {
                RetrofitUtils.createRetrofit(IMAGE_UPLOAD_URL)
                        .create(UploadImageContactUs::class.java)
                        .uploadImage(
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any()
                        )
            }


        }

    }

    @Test
    fun `check invocation of uploadImagePublic on invocation of getImageUploadResult when not loged in`() {

        val netcal = mockk<NetworkCalculator>()
        val file = mockk<File>()

        runBlockingTest {

            coEvery {
                netcal.content
            } returns HashMap()

            coEvery {
                netcal.header
            } returns HashMap()

            mockkStatic(RetrofitUtils::class)
            coEvery {
                RetrofitUtils.createRetrofit(IMAGE_UPLOAD_URL)
                        .create(UploadImageContactUs::class.java)
                        .uploadImagePublic(
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any()
                        )
            } returns mockk()


            uploadImageUseCase.getImageUploadresult(netcal, file, false)

            coVerify {
                RetrofitUtils.createRetrofit(IMAGE_UPLOAD_URL)
                        .create(UploadImageContactUs::class.java)
                        .uploadImagePublic(
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any(),
                                any()
                        )
            }

        }

    }

    @Test
    fun `check list size on invocation of getNetworkCalculator`() {
        list.add(ImageUpload("", "", "", ""))

        every { uploadImageUseCase.getNetworkCalculator() } returns mockk(relaxed = true)

        uploadImageUseCase.getNetworkCalculatorList(list)

        assertEquals(list.size, 1)

    }
}