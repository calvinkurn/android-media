package com.tokopedia.shareexperience.test.viewmodel.generatelink

import android.net.Uri
import app.cash.turbine.test
import com.tokopedia.shareexperience.base.ShareExViewModelTestFixture
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkProperties
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.ui.ShareExAction
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.shareexperience.ui.uistate.ShareExChannelIntentUiState
import com.tokopedia.shareexperience.ui.util.ShareExIntentErrorEnum
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShareExViewModelGenerateLinkTest : ShareExViewModelTestFixture() {

    @Test
    fun `generate affiliate link, get short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = dummyIdentifier,
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(""),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(true)
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

            every {
                userSession.isLoggedIn
            } returns true

            every {
                userSession.userId
            } returns "123"

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(
                            imageUrl = "testImageUrl",
                            imageTypeEnum = ShareExImageTypeEnum.CONTEXTUAL_IMAGE
                        )
                    )
                )
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Loading))
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Success(dummyShortLink)))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                emit(ShareExResult.Success(mockUri))
            }

            val dummyChannel = ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.OTHERS,
                mimeType = ShareExMimeTypeEnum.ALL,
                packageName = "dummyPackage"
            )

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(1) // initial
                assertLoadingImageGenerator(awaitItem())
                assertLoadingLinkGenerator(awaitItem())

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals(dummyShortLink, updatedValue.message)
                assertEquals(dummyShortLink, updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error == null)
                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedValue.imageType)
                assertEquals(0, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate branch link, get short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = dummyIdentifier,
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(""),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
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
                                    args = mapOf()
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
                            imageUrl = "testImageUrl",
                            imageTypeEnum = ShareExImageTypeEnum.CONTEXTUAL_IMAGE
                        )
                    )
                )
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Loading))
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Success(dummyShortLink)))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                emit(ShareExResult.Success(mockUri))
            }

            val dummyChannel = ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.OTHERS,
                mimeType = ShareExMimeTypeEnum.IMAGE,
                packageName = "dummyPackage"
            )

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(1) // initial
                assertLoadingImageGenerator(awaitItem())
                assertLoadingLinkGenerator(awaitItem())

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals(dummyShortLink, updatedValue.message)
                assertEquals(dummyShortLink, updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error == null)
                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedValue.imageType)
                assertEquals(0, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate affiliate link without image generator, get short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = dummyIdentifier,
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(""),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                affiliate = ShareExAffiliateModel(
                                    eligibility = ShareExAffiliateEligibilityModel(true)
                                ),
                                linkProperties = ShareExLinkProperties(
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = null
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder(query = "testQuery")

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(
                            imageUrl = "testImageUrl",
                            imageTypeEnum = ShareExImageTypeEnum.DEFAULT
                        )
                    )
                )
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
                emit(ShareExResult.Success(mockUri))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(1) // initial
                assertLoadingImageGenerator(awaitItem())
                assertLoadingLinkGenerator(awaitItem())

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
    fun `generate link without image generator, get short link`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = dummyIdentifier,
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(""),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf(),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                linkProperties = ShareExLinkProperties(
                                    androidUrl = dummyUrl,
                                    iosUrl = dummyUrl,
                                    desktopUrl = dummyUrl
                                ),
                                imageGenerator = null
                            )
                        )
                    )
                ),
                throwable = null
            )

            mockUriBuilder(mockUriDetails = false, query = "")

            coEvery {
                getGeneratedImageUseCase.getData(any())
            } returns flow {
                emit(ShareExResult.Loading)
                delay(10)
                emit(
                    ShareExResult.Success(
                        ShareExImageGeneratorModel(
                            imageUrl = "testImageUrl",
                            imageTypeEnum = ShareExImageTypeEnum.DEFAULT
                        )
                    )
                )
            }

            coEvery {
                getShortLinkUseCase.getShortLink(any())
            } returns flow {
                // Not affiliate
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Loading))
                delay(10)
                emit(Pair(ShareExShortLinkFallbackPriorityEnum.BRANCH, ShareExResult.Success(dummyShortLink)))
            }

            val mockUri: Uri = mockk(relaxed = true)
            coEvery {
                getDownloadedImageUseCase.downloadImageThumbnail(any())
            } returns flow {
                emit(ShareExResult.Loading)
                emit(ShareExResult.Success(mockUri))
            }

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(1) // initial
                assertLoadingImageGenerator(awaitItem())
                assertLoadingLinkGenerator(awaitItem())

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
    fun `generate link without bottom sheet property and default url, get empty short link or message`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = dummyIdentifier,
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(""),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel()
                ),
                throwable = null
            )

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                // Then
                skipItems(1) // initial

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals("", updatedValue.message)
                assertEquals("", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(false, updatedValue.isLoading)
                assert(updatedValue.error != null)
                assertEquals(ShareExImageTypeEnum.NO_IMAGE, updatedValue.imageType)
                assertTrue(updatedValue.errorHistory.contains(ShareExIntentErrorEnum.DEFAULT_URL_ERROR))
                assertEquals(1, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate link from default page, get default url`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = "",
                pageTypeEnum = ShareExPageTypeEnum.OTHERS,
                defaultUrl = "defaultUrl",
                trackerArg = ShareExTrackerArg(""),
                throwable = dummyThrowable
            )

            coEvery {
                getSharePropertiesUseCase.getDefaultData()
            } returns ShareExBottomSheetModel(
                title = "testTitle",
                subtitle = "testSubtitle",
                bottomSheetPage = ShareExBottomSheetPageModel(
                    listShareProperty = listOf(
                        ShareExPropertyModel(
                            socialChannel = ShareExChannelModel(listChannel = listOf(dummyChannel))
                        )
                    )
                )
            )

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                val initialValue = awaitItem()
                assertEquals(null, initialValue.intent)
                assertEquals("", initialValue.message)
                assertEquals("", initialValue.shortLink)
                assertEquals(null, initialValue.channelEnum)
                assertEquals(ShareExImageTypeEnum.NO_IMAGE, initialValue.imageType)
                assertEquals(false, initialValue.isLoading)
                assertEquals(0, initialValue.errorHistory.size)

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals("defaultUrl", updatedValue.message)
                assertEquals("defaultUrl", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(ShareExImageTypeEnum.NO_IMAGE, updatedValue.imageType)
                assertTrue(updatedValue.errorHistory.contains(ShareExIntentErrorEnum.DEFAULT_URL_ERROR))
                assertEquals(1, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `generate link from page without bottom sheet arg, get empty string`() {
        runTest {
            // Given
            val dummyChannel = ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.OTHERS,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = ""
            )
            viewModel.bottomSheetArgs = null

            viewModel.channelIntentUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))

                skipItems(1) // initial

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals("", updatedValue.message)
                assertEquals("", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(ShareExImageTypeEnum.NO_IMAGE, updatedValue.imageType)
                assertTrue(updatedValue.errorHistory.contains(ShareExIntentErrorEnum.DEFAULT_URL_ERROR))
                assertEquals(1, updatedValue.errorHistory.size)

                expectNoEvents()
            }
        }
    }

    private fun assertLoadingImageGenerator(loadingImageGenerator: ShareExChannelIntentUiState) {
        assert(loadingImageGenerator.intent == null)
        assertEquals("", loadingImageGenerator.message)
        assertEquals("", loadingImageGenerator.shortLink)
        assertEquals(null, loadingImageGenerator.channelEnum)
        assertEquals(true, loadingImageGenerator.isLoading)
        assert(loadingImageGenerator.error == null)
        assertEquals(ShareExImageTypeEnum.NO_IMAGE, loadingImageGenerator.imageType)
        assertEquals(0, loadingImageGenerator.errorHistory.size)
    }

    private fun assertLoadingLinkGenerator(loadingLinkGenerator: ShareExChannelIntentUiState) {
        assert(loadingLinkGenerator.intent == null)
        assertEquals("", loadingLinkGenerator.message)
        assertEquals("", loadingLinkGenerator.shortLink)
        assertEquals(ShareExChannelEnum.OTHERS, loadingLinkGenerator.channelEnum)
        assertEquals(true, loadingLinkGenerator.isLoading)
        assert(loadingLinkGenerator.error == null)
        assertEquals(ShareExImageTypeEnum.NO_IMAGE, loadingLinkGenerator.imageType)
        assertEquals(0, loadingLinkGenerator.errorHistory.size)
    }
}
