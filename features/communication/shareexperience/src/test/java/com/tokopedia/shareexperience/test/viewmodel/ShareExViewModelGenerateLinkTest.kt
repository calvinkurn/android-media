package com.tokopedia.shareexperience.test.viewmodel

import app.cash.turbine.test
import com.tokopedia.shareexperience.base.ShareExViewModelTestFixture
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.ui.ShareExAction
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ShareExViewModelGenerateLinkTest : ShareExViewModelTestFixture() {

    @Test
    fun `generate link from default page, get default url`() {
        runTest {
            // Given
            val dummyChannel = ShareExChannelItemModel(
                channelEnum = ShareExChannelEnum.OTHERS,
                mimeType = ShareExMimeTypeEnum.TEXT,
                packageName = "dummyPackage"
            )
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = "",
                pageTypeEnum = ShareExPageTypeEnum.OTHERS,
                defaultUrl = "defaultUrl",
                trackerArg = ShareExTrackerArg(),
                throwable = dummyThrowable
            )

            coEvery {
                getSharePropertiesUseCase.getDefaultData()
            } returns ShareExBottomSheetModel(
                title = "testTitle",
                subtitle = "testSubtitle",
                bottomSheetPage = ShareExBottomSheetPageModel(
                    socialChannel = ShareExChannelModel(listChannel = listOf(dummyChannel))
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
                assertEquals(false, initialValue.isAffiliateError)

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals("defaultUrl", updatedValue.message)
                assertEquals("defaultUrl", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(ShareExImageTypeEnum.NO_IMAGE, updatedValue.imageType)
                assertEquals(false, updatedValue.isAffiliateError)

                cancelAndConsumeRemainingEvents()
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

                val initialValue = awaitItem()
                assertEquals(null, initialValue.intent)
                assertEquals("", initialValue.message)
                assertEquals("", initialValue.shortLink)
                assertEquals(null, initialValue.channelEnum)
                assertEquals(ShareExImageTypeEnum.NO_IMAGE, initialValue.imageType)
                assertEquals(false, initialValue.isAffiliateError)

                val updatedValue = awaitItem()
                assert(updatedValue.intent != null)
                assertEquals("", updatedValue.message)
                assertEquals("", updatedValue.shortLink)
                assertEquals(ShareExChannelEnum.OTHERS, updatedValue.channelEnum)
                assertEquals(ShareExImageTypeEnum.NO_IMAGE, updatedValue.imageType)
                assertEquals(false, updatedValue.isAffiliateError)

                cancelAndConsumeRemainingEvents()
            }
        }
    }
}
