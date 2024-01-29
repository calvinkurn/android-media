package com.tokopedia.shareexperience.base

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetShortLinkUseCase
import com.tokopedia.shareexperience.ui.ShareExViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito

abstract class ShareExViewModelTestFixture {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @RelaxedMockK
    lateinit var getSharePropertiesUseCase: ShareExGetSharePropertiesUseCase

    @RelaxedMockK
    lateinit var getGeneratedImageUseCase: ShareExGetGeneratedImageUseCase

    @RelaxedMockK
    lateinit var getShortLinkUseCase: ShareExGetShortLinkUseCase

    @RelaxedMockK
    lateinit var getDownloadedImageUseCase: ShareExGetDownloadedImageUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var mockLifecycleOwner: LifecycleOwner
    private lateinit var lifecycle: LifecycleRegistry
    protected lateinit var viewModel: ShareExViewModel

    protected val dummyThrowable = Throwable("Oops!")
    protected val dummyIdentifier = "testId"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(Uri::class)
        mockLifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
        lifecycle = LifecycleRegistry(mockLifecycleOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        viewModel = ShareExViewModel(
            getSharePropertiesUseCase,
            getGeneratedImageUseCase,
            getShortLinkUseCase,
            getDownloadedImageUseCase,
            userSession,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun mockUriBuilder() {
        val mockUri = mockk<Uri>(relaxed = true)

        // Mock behavior for Uri.parse
        every { Uri.parse(any()) } returns mockUri

        // Mock the scheme, authority, and path to return specific values
        every { mockUri.scheme } returns "http"
        every { mockUri.authority } returns "example.com"
        every { mockUri.path } returns "/path/to/resource"

        // Mock the Uri.Builder constructor
        mockkConstructor(Uri.Builder::class)
        every { anyConstructed<Uri.Builder>().scheme(any()) } returns mockk(relaxed = true)
        every { anyConstructed<Uri.Builder>().authority(any()) } returns mockk(relaxed = true)
        every { anyConstructed<Uri.Builder>().path(any()) } returns mockk(relaxed = true)
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
