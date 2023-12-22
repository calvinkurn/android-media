package com.tokopedia.gamification.giftbox.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.gamification.giftbox.presentation.LidImagesDownloaderUseCase
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxImageDownloadViewModel
import com.tokopedia.gamification.giftbox.util.TestUtil.verifyAssertEquals
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GiftBoxImageDownloadViewModelTest {
    @RelaxedMockK
    lateinit var lidImagesDownloaderUseCase: LidImagesDownloaderUseCase

    @RelaxedMockK
    lateinit var application: Application

    @RelaxedMockK
    lateinit var bitmap: Bitmap

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GiftBoxImageDownloadViewModel

    private val url = "https://tokopedia.com/download/image"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = GiftBoxImageDownloadViewModel(
            lidImagesDownloaderUseCase = lidImagesDownloaderUseCase,
            application = application,
            dispatchers = coroutineTestRule.dispatchers
        )
    }

    private fun stubDownloadBgImage(bitmap: Bitmap?) {
        coEvery {
            lidImagesDownloaderUseCase.downloadBgImage(application, url)
        } returns bitmap
    }

    private fun stubDownloadImages(bitmapList: List<Bitmap>, urlList: List<String>) {
        coEvery {
            lidImagesDownloaderUseCase.downloadImages(application, urlList)
        } returns bitmapList
    }

    private fun stubDownloadImages(error: Throwable, urlList: List<String>) {
        coEvery {
            lidImagesDownloaderUseCase.downloadImages(application, urlList)
        } throws error
    }

    private fun stubDownloadBgImage(error: Throwable) {
        coEvery {
            lidImagesDownloaderUseCase.downloadBgImage(application, url)
        } throws error
    }

    private fun verifyDownloadBgImageCalled() {
        coVerify {
            lidImagesDownloaderUseCase.downloadBgImage(application, url)
        }
    }

    private fun verifyDownloadImagesCalled(urlList: List<String>) {
        coVerify {
            lidImagesDownloaderUseCase.downloadImages(application, urlList)
        }
    }

    @Test
    fun `after successfully downloading an image, livedata should provide the expected bitmap as a result`() {
        val imageLiveData = MutableLiveData<LiveDataResult<Bitmap?>>()
        val result = bitmap

        stubDownloadBgImage(result)

        viewModel.downloadImage(
            url = url,
            imageLiveData = imageLiveData
        )

        verifyDownloadBgImageCalled()

        imageLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully downloading an image, livedata should provide null as a result`() {
        val imageLiveData = MutableLiveData<LiveDataResult<Bitmap?>>()
        val result: Bitmap? = null

        stubDownloadBgImage(result)

        viewModel.downloadImage(
            url = url,
            imageLiveData = imageLiveData
        )

        verifyDownloadBgImageCalled()

        imageLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `when downloading an image but there is an error, livedata should provide null as a result`() {
        val imageLiveData = MutableLiveData<LiveDataResult<Bitmap?>>()
        val result: Bitmap? = null

        stubDownloadBgImage(Throwable())

        viewModel.downloadImage(
            url = url,
            imageLiveData = imageLiveData
        )

        verifyDownloadBgImageCalled()

        imageLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `after successfully downloading multiple images, livedata should provide the expected bitmap list as a result`() {
        val imageListLiveData = MutableLiveData<LiveDataResult<List<Bitmap>?>>()
        val result: List<Bitmap> = listOf(bitmap, bitmap)
        val urlList: List<String> = listOf(url, url)

        stubDownloadImages(
            bitmapList = result,
            urlList = urlList
        )

        viewModel.downloadImages(
            urlList = urlList,
            imageListLiveData = imageListLiveData
        )

        verifyDownloadImagesCalled(urlList)

        imageListLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully downloading multiple images, but the size of result not the same as url so livedata should provide null as a result`() {
        val imageListLiveData = MutableLiveData<LiveDataResult<List<Bitmap>?>>()
        val result: List<Bitmap>? = null
        val urlList: List<String> = listOf(url)

        stubDownloadImages(
            bitmapList = listOf(bitmap, bitmap),
            urlList = urlList
        )

        viewModel.downloadImages(
            urlList = urlList,
            imageListLiveData = imageListLiveData
        )

        verifyDownloadImagesCalled(urlList)

        imageListLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `when downloading multiple images but there is an error, livedata should provide null as a result`() {
        val imageListLiveData = MutableLiveData<LiveDataResult<List<Bitmap>?>>()
        val result: List<Bitmap>? = null
        val urlList: List<String> = listOf(url)

        stubDownloadImages(
            error = Throwable(),
            urlList = urlList
        )

        viewModel.downloadImages(
            urlList = urlList,
            imageListLiveData = imageListLiveData
        )

        verifyDownloadImagesCalled(urlList)

        imageListLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.ERROR
            )
    }
}
