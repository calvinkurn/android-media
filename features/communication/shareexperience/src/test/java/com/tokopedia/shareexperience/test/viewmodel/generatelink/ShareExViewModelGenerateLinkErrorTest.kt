// package com.tokopedia.shareexperience.test.viewmodel.generatelink
//
// import android.net.Uri
// import app.cash.turbine.test
// import com.tokopedia.shareexperience.base.ShareExViewModelTestFixture
// import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
// import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
// import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
// import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
// import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
// import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
// import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
// import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
// import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
// import com.tokopedia.shareexperience.domain.model.property.ShareExLinkProperties
// import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
// import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
// import com.tokopedia.shareexperience.domain.util.ShareExResult
// import com.tokopedia.shareexperience.ui.ShareExAction
// import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
// import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
// import io.mockk.coEvery
// import io.mockk.mockk
// import kotlinx.coroutines.flow.flow
// import kotlinx.coroutines.test.runTest
// import org.junit.Test
// import kotlin.test.assertEquals
//
// class ShareExViewModelGenerateLinkErrorTest: ShareExViewModelTestFixture() {
//
//    @Test
//    fun `generate affiliate link, get short link`() {
//        runTest {
//            // Given
//            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
//                identifier = dummyIdentifier,
//                pageTypeEnum = ShareExPageTypeEnum.PDP,
//                defaultUrl = "",
//                trackerArg = ShareExTrackerArg(),
//                bottomSheetModel = ShareExBottomSheetModel(
//                    title = "testTitle",
//                    subtitle = "testSubtitle",
//                    bottomSheetPage = ShareExBottomSheetPageModel(
//                        listChip = listOf(),
//                        listShareProperty = listOf(
//                            ShareExPropertyModel(
//                                affiliate = ShareExAffiliateModel(
//                                    eligibility = ShareExAffiliateEligibilityModel(true)
//                                ),
//                                linkProperties = ShareExLinkProperties(
//                                    androidUrl = dummyUrl,
//                                    iosUrl = dummyUrl,
//                                    desktopUrl = dummyUrl
//                                ),
//                                imageGenerator = ShareExImageGeneratorPropertyModel(
//                                    sourceId = "testSourceId",
//                                    args = mapOf("test" to "test")
//                                )
//                            )
//                        )
//                    )
//                ),
//                throwable = null
//            )
//
//            mockUriBuilder()
//
//            coEvery {
//                getGeneratedImageUseCase.getData(any(), any())
//            } returns flow {
//                emit(ShareExResult.Loading)
//                emit(
//                    ShareExResult.Success(
//                        ShareExImageGeneratorModel(
//                            imageUrl = "testImageUrl",
//                            imageTypeEnum = ShareExImageTypeEnum.CONTEXTUAL_IMAGE
//                        )
//                    )
//                )
//            }
//
//            coEvery {
//                getShortLinkUseCase.getShortLink(any())
//            } returns flow {
//                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Loading))
//                emit(Pair(ShareExShortLinkFallbackPriorityEnum.AFFILIATE, ShareExResult.Success(dummyShortLink)))
//            }
//
//            val mockUri: Uri = mockk(relaxed = true)
//            coEvery {
//                getDownloadedImageUseCase.downloadImage(any())
//            } returns flow {
//                emit(ShareExResult.Loading)
//                emit(ShareExResult.Success(mockUri))
//            }
//
//            viewModel.channelIntentUiState.test {
//                // When
//                viewModel.setupViewModelObserver()
//                viewModel.processAction(ShareExAction.InitializePage)
//                viewModel.processAction(ShareExAction.GenerateLink(dummyChannel))
//
//                // Then
//                skipItems(1) // initial
//                assertLoadingImageGenerator(awaitItem())
//                assertLoadingLinkGenerator(awaitItem())
//
//                val updatedValue = awaitItem()
//                assert(updatedValue.intent != null)
//                assertEquals(dummyShortLink, updatedValue.message)
//                assertEquals(dummyShortLink, updatedValue.shortLink)
//                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
//                assertEquals(false, updatedValue.isLoading)
//                assert(updatedValue.error == null)
//                assertEquals(ShareExImageTypeEnum.CONTEXTUAL_IMAGE, updatedValue.imageType)
//                assertEquals(false, updatedValue.isAffiliateError)
//
//                expectNoEvents()
//            }
//        }
//    }
//
// }
