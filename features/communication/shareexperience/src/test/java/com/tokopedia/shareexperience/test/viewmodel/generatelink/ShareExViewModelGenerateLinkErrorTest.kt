 
package com.tokopedia.shareexperience.test.viewmodel.generatelink

import android.net.Uri
import app.cash.turbine.test
import com.tokopedia.shareexperience.base.ShareExViewModelTestFixture
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExMessagePlaceholderEnum
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkMessagePropertiesModel
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkPropertiesModel
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.ui.ShareExAction
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetResultArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.shareexperience.ui.util.ShareExIntentErrorEnum
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShareExViewModelGenerateLinkErrorTest : ShareExViewModelTestFixture() {

    @Test
    fun `generate short link with image channel and error image generator, get short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "shareId",
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(true)
                                ),
                                linkProperties = ShareExLinkPropertiesModel(
                                    messageObject = ShareExLinkMessagePropertiesModel(
                                        message = ShareExMessagePlaceholderEnum.BRANCH_LINK.placeholder,
                                        replacementMap = mutableMapOf(ShareExMessagePlaceholderEnum.BRANCH_LINK to "")
                                    ),
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "testSourceId",
                                    args = mapOf("test" to "test")
                                )
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder()

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Error(dummyThrowable))
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Success(dummyShortLink)))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Success(mockUri))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannelWithImage))

                // Then
                skipItems(3) // loading image generator, loading short link

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals(dummyShortLink, updatedValue.message)
                assertEquals(dummyShortLink, updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error == null)
                assertEquals(ShareExImageTypeEnum.DEFAULT, updatedValue.imageType)
                assertEquals(0, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate short link with text channel and error image generator, get short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "shareId",
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(true)
                                ),
                                linkProperties = ShareExLinkPropertiesModel(
                                    messageObject = ShareExLinkMessagePropertiesModel(
                                        message = ShareExMessagePlaceholderEnum.BRANCH_LINK.placeholder,
                                        replacementMap = mutableMapOf(ShareExMessagePlaceholderEnum.BRANCH_LINK to "")
                                    ),
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "testSourceId",
                                    args = mapOf("test" to "test")
                                )
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder()

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Error(dummyThrowable))
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Success(dummyShortLink)))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(3) // loading image generator, loading short link

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals(dummyShortLink, updatedValue.message)
                assertEquals(dummyShortLink, updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error == null)
                assertEquals(ShareExImageTypeEnum.DEFAULT, updatedValue.imageType)
                assertEquals(0, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate short link and error generate image flow, get short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "shareId",
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(true)
                                ),
                                linkProperties = ShareExLinkPropertiesModel(
                                    messageObject = ShareExLinkMessagePropertiesModel(
                                        message = ShareExMessagePlaceholderEnum.BRANCH_LINK.placeholder,
                                        replacementMap = mutableMapOf(ShareExMessagePlaceholderEnum.BRANCH_LINK to "")
                                    ),
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "testSourceId",
                                    args = mapOf("test" to "test")
                                )
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder()

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } throws dummyThrowable

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Success(dummyShortLink)))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Success(mockUri))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(2) // loading short link

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals(dummyShortLink, updatedValue.message)
                assertEquals(dummyShortLink, updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error == null)
                assertEquals(ShareExImageTypeEnum.DEFAULT, updatedValue.imageType)
                assertEquals(0, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate short link and error affiliate, get affiliate flag`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "shareId",
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(true)
                                ),
                                linkProperties = ShareExLinkPropertiesModel(
                                    messageObject = ShareExLinkMessagePropertiesModel(
                                        message = ShareExMessagePlaceholderEnum.BRANCH_LINK.placeholder,
                                        replacementMap = mutableMapOf(ShareExMessagePlaceholderEnum.BRANCH_LINK to "")
                                    ),
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "testSourceId",
                                    args = mapOf("test" to "test")
                                )
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder()

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(
                            imageTypeEnum = ShareExImageTypeEnum.CONTEXTUAL_IMAGE
                        )
                    )
                )
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Error(dummyThrowable)))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Success(mockUri))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(3) // initial, loading image generator, loading short link

                val updatedValue = awaitItem()
                assert(updatedValue.intent == null)
                assertEquals("", updatedValue.message)
                assertEquals("", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error != null)
                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedValue.imageType)
                assertEquals(1, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate short link and error non affiliate, get affiliate flag`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "shareId",
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(false)
                                ),
                                linkProperties = ShareExLinkPropertiesModel(
                                    messageObject = ShareExLinkMessagePropertiesModel(
                                        message = ShareExMessagePlaceholderEnum.BRANCH_LINK.placeholder,
                                        replacementMap = mutableMapOf(ShareExMessagePlaceholderEnum.BRANCH_LINK to "")
                                    ),
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "testSourceId",
                                    args = mapOf("test" to "test")
                                )
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder()

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(
                            imageTypeEnum = ShareExImageTypeEnum.CONTEXTUAL_IMAGE
                        )
                    )
                )
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Error(dummyThrowable)))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Success(mockUri))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(3) // initial, loading image generator, loading short link

                val updatedValue = awaitItem()
                assert(updatedValue.intent == null)
                assertEquals("", updatedValue.message)
                assertEquals("", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error != null)
                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedValue.imageType)
                assertTrue(updatedValue.errorHistory.contains(ShareExIntentErrorEnum.BRANCH_ERROR))
                assertEquals(1, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate short link and error download image, still get intent and short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "shareId",
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(false)
                                ),
                                linkProperties = ShareExLinkPropertiesModel(
                                    messageObject = ShareExLinkMessagePropertiesModel(
                                        message = ShareExMessagePlaceholderEnum.BRANCH_LINK.placeholder,
                                        replacementMap = mutableMapOf(ShareExMessagePlaceholderEnum.BRANCH_LINK to "")
                                    ),
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "testSourceId",
                                    args = mapOf("test" to "test")
                                )
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder()

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(
                            imageTypeEnum = ShareExImageTypeEnum.CONTEXTUAL_IMAGE
                        )
                    )
                )
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Success(dummyShortLink)))
            }

            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Error(dummyThrowable))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannelWithImage))

                // Then
                skipItems(3) // initial, loading image generator, loading short link

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals(dummyShortLink, updatedValue.message)
                assertEquals(dummyShortLink, updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error != null)
                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedValue.imageType)
                assertTrue(updatedValue.errorHistory.contains(ShareExIntentErrorEnum.IMAGE_DOWNLOADER))
                assertEquals(1, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate short link and error generate branch link, still get intent and default link with utm`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = dummyIdentifier,
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "defaultUrl",
                trackerArg = ShareExTrackerArg("utmcampaign"),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "shareId",
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(false)
                                ),
                                linkProperties = ShareExLinkProperties(
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "testSourceId",
                                    args = mapOf("test" to "test")
                                )
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder()

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(
                            imageTypeEnum = ShareExImageTypeEnum.CONTEXTUAL_IMAGE
                        )
                    )
                )
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Error(dummyThrowable)))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.DEFAULT, ShareExResult.Success("defaultUrlWithUtm")))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(ShareExResult.Success(mockUri))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannelWithImage))

                // Then
                skipItems(3) // initial, loading image generator, loading short link

                val updatedValue = awaitItem()
                assert(updatedValue.intent == null)
                assertEquals("", updatedValue.message)
                assertEquals("", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error != null)
                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedValue.imageType)
                assertTrue(updatedValue.errorHistory.contains(ShareExIntentErrorEnum.BRANCH_ERROR))
                assertEquals(1, updatedValue.errorHistory.size)

                val updatedResultValue = awaitItem()
                assert(updatedResultValue.intent != null)
                assertEquals("defaultUrlWithUtm", updatedResultValue.message)
                assertEquals("defaultUrlWithUtm", updatedResultValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedResultValue.channelEnum)
                assertEquals(false, updatedResultValue.isLoading)
                assert(updatedResultValue.error == null)
                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedResultValue.imageType)
                assertTrue(updatedResultValue.errorHistory.contains(ShareExIntentErrorEnum.BRANCH_ERROR))
                assertEquals(1, updatedResultValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }
}
