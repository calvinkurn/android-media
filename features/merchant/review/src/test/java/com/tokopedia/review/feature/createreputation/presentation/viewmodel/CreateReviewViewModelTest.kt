package com.tokopedia.review.feature.createreputation.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.domain.RequestState
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevSubmitReviewUseCase
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewMediaUploadResult
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewTextAreaTextUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewToasterUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.PostSubmitUiState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewAnonymousInfoBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewAnonymousUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBadRatingCategoriesUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewIncentiveBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewProductCardUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewProgressBarUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewRatingUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewSubmitButtonUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaTitleUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTickerUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTopicsUiState
import com.tokopedia.review.utils.assertInstanceOf
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.Toaster
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class CreateReviewViewModelTest: CreateReviewViewModelTestFixture() {
    @Test
    fun `should trigger load reputation form when form not yet loaded`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        setInitialData()
        coVerify { getProductReputationForm.getReputationForm(any()) }
    }

    @Test
    fun `should not trigger load reputation form when form already loaded`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        setInitialData()
        coVerify(exactly = 1) { getProductReputationForm.getReputationForm(any()) }
        setInitialData(reputationID = SAMPLE_REPUTATION_ID.reversed())
        coVerify(exactly = 1) { getProductReputationForm.getReputationForm(any()) }
    }

    @Test
    fun `should not trigger load reputation form when reputationID is empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        setInitialData(reputationID = "")
        coVerify(inverse = true) { getProductReputationForm.getReputationForm(any()) }
    }

    @Test
    fun `should not trigger load reputation form when productID is empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        setInitialData(productID = "")
        coVerify(inverse = true) { getProductReputationForm.getReputationForm(any()) }
    }

    @Test
    fun `productCardUiState should equal to CreateReviewProductCardUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(CreateReviewProductCardUiState.Loading, viewModel.productCardUiState.value)
    }

    @Test
    fun `productCardUiState should equal to CreateReviewProductCardUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewProductCardUiState.Showing>(viewModel.productCardUiState.value)
    }

    @Test
    fun `ratingUiState should equal to CreateReviewRatingUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(CreateReviewRatingUiState.Loading, viewModel.ratingUiState.value)
    }

    @Test
    fun `ratingUiState should equal to CreateReviewProductCardUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewRatingUiState.Showing>(viewModel.ratingUiState.value)
    }

    @Test
    fun `tickerUiState should equal to CreateReviewTickerUiState#Hidden when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(CreateReviewTickerUiState.Hidden, viewModel.tickerUiState.value)
    }

    @Test
    fun `tickerUiState should equal to CreateReviewTickerUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTickerUiState.Showing>(viewModel.tickerUiState.value)
    }

    @Test
    fun `tickerUiState should equal to CreateReviewTickerUiState#Hidden when getIncentiveOvo return null`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo(null)
        setInitialData()
        Assert.assertEquals(CreateReviewTickerUiState.Hidden, viewModel.tickerUiState.value)
    }

    @Test
    fun `textAreaTitleUiState should equal to CreateReviewTextAreaTitleUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(CreateReviewTextAreaTitleUiState.Loading, viewModel.textAreaTitleUiState.value)
    }

    @Test
    fun `textAreaTitleUiState should equal to CreateReviewTextAreaTitleUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTextAreaTitleUiState.Showing>(viewModel.textAreaTitleUiState.value)
    }

    @Test
    fun `textAreaTitleUiState textRes id should equal to review_create_worst_title when rating is 1`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 1)
        val textAreaTitleUiState = viewModel.textAreaTitleUiState.value as CreateReviewTextAreaTitleUiState.Showing
        Assert.assertEquals(R.string.review_create_worst_title, textAreaTitleUiState.textRes.id)
    }

    @Test
    fun `textAreaTitleUiState textRes id should equal to review_form_bad_title when rating is 2`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 2)
        val textAreaTitleUiState = viewModel.textAreaTitleUiState.value as CreateReviewTextAreaTitleUiState.Showing
        Assert.assertEquals(R.string.review_form_bad_title, textAreaTitleUiState.textRes.id)
    }

    @Test
    fun `textAreaTitleUiState textRes id should equal to review_form_neutral_title when rating is 3`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 3)
        val textAreaTitleUiState = viewModel.textAreaTitleUiState.value as CreateReviewTextAreaTitleUiState.Showing
        Assert.assertEquals(R.string.review_form_neutral_title, textAreaTitleUiState.textRes.id)
    }

    @Test
    fun `textAreaTitleUiState textRes id should equal to review_create_best_title when rating is more than 3`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 4)
        val textAreaTitleUiState = viewModel.textAreaTitleUiState.value as CreateReviewTextAreaTitleUiState.Showing
        Assert.assertEquals(R.string.review_create_best_title, textAreaTitleUiState.textRes.id)
    }

    @Test
    fun `badRatingCategoriesUiState should equal to CreateReviewBadRatingCategoriesUiState#Hidden when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewBadRatingCategoriesUiState.Hidden>(viewModel.badRatingCategoriesUiState.value)
    }

    @Test
    fun `badRatingCategoriesUiState should equal to CreateReviewBadRatingCategoriesUiState#Hidden when isGoodRating is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewBadRatingCategoriesUiState.Hidden>(viewModel.badRatingCategoriesUiState.value)
    }

    @Test
    fun `badRatingCategoriesUiState should equal to CreateReviewBadRatingCategoriesUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 1)
        assertInstanceOf<CreateReviewBadRatingCategoriesUiState.Showing>(viewModel.badRatingCategoriesUiState.value)
    }

    @Test
    fun `textAreaUiState should equal to CreateReviewTextAreaUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTextAreaUiState.Loading>(viewModel.textAreaUiState.value)
    }

    @Test
    fun `textAreaUiState should equal to CreateReviewTextAreaUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTextAreaUiState.Showing>(viewModel.textAreaUiState.value)
    }

    @Test
    fun `textAreaUiState textAreaHint stringRes id should equal to review_form_bad_helper_must_fill when only bad rating other category selected`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 1)
        viewModel.updateBadRatingSelectedStatus(BAD_RATING_CATEGORY_ID_OTHER, true)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_form_bad_helper_must_fill, textAreaUiState.hint.id)
    }

    @Test
    fun `textAreaUiState textAreaHint stringRes id should equal to review_form_bad_helper when not only bad rating other category selected`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 2)
        viewModel.updateBadRatingSelectedStatus(BAD_RATING_CATEGORY_ID_OTHER, true)
        viewModel.updateBadRatingSelectedStatus(BAD_RATING_CATEGORY_ID_PRODUCT_PROBLEM, true)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_form_bad_helper, textAreaUiState.hint.id)
    }

    @Test
    fun `textAreaUiState textAreaHint stringRes id should equal to review_form_bad_helper when no bad rating other category selected`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 1)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_form_bad_helper, textAreaUiState.hint.id)
    }

    @Test
    fun `textAreaUiState textAreaHint stringRes id should equal to review_form_neutral_helper when selected rating is 3`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 3)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_form_neutral_helper, textAreaUiState.hint.id)
    }

    @Test
    fun `textAreaUiState textAreaHint stringRes id should equal to review_raw_string_format when selected rating is more than 3, placeholder from BE is not blank and review topics inspiration is enabled`() = runCollectingStateFlows {
        mockStringAb("review_inspiration", "experiment_variant", "experiment_variant")
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 4)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_raw_string_format, textAreaUiState.hint.id)
        Assert.assertEquals(listOf("Contoh: Kualitas materialnya bagus, bikin panas & matangnya rata. Ukuran ini pas buat masak sehari-hari. Wajib punya, deh \uD83D\uDC4D"), textAreaUiState.hint.params)
    }

    @Test
    fun `textAreaUiState textAreaHint stringRes id should equal to review_form_good_helper when selected rating is more than 3 and placeholder from BE is blank`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessValidWithEmptyPlaceholder)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 4)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_form_good_helper, textAreaUiState.hint.id)
    }

    @Test
    fun `textAreaUiState textAreaHint stringRes id should equal to review_form_good_helper when selected rating is more than 3 and review topics inspiration is disabled`() = runCollectingStateFlows {
        mockStringAb("review_inspiration", "experiment_variant", "control_variant")
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 4)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_form_good_helper, textAreaUiState.hint.id)
    }

    @Test
    fun `textAreaUiState textAreaHelper stringRes id should equal to review_create_bottom_sheet_text_area_partial_incentive when review text length is between 1 to 40`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateTextAreaHasFocus(true)
        viewModel.setReviewText("a", CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_create_bottom_sheet_text_area_partial_incentive, textAreaUiState.helper.id)
    }

    @Test
    fun `textAreaUiState textAreaHelper stringRes id should equal to review_create_bottom_sheet_text_area_eligible_for_incentive when review text length is more than or equal to 40`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo(getProductIncentiveOvoUseCaseResultSuccessChallenge)
        setInitialData()
        viewModel.updateTextAreaHasFocus(true)
        viewModel.setReviewText("a".repeat(40), CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_create_bottom_sheet_text_area_eligible_for_incentive, textAreaUiState.helper.id)
    }

    @Test
    fun `textAreaUiState textAreaHelper stringRes id should equal to review_create_bottom_sheet_text_area_empty_incentive when review text is empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateTextAreaHasFocus(true)
        val textAreaUiState = viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing
        Assert.assertEquals(R.string.review_create_bottom_sheet_text_area_empty_incentive, textAreaUiState.helper.id)
    }

    @Test
    fun `templateUiState should equal to CreateReviewTemplateUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTemplateUiState.Loading>(viewModel.templateUiState.value)
    }

    @Test
    fun `templateUiState should equal to CreateReviewTemplateUiState#Hidden when canRenderForm is true and template is empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTemplateUiState.Hidden>(viewModel.templateUiState.value)
    }

    @Test
    fun `templateUiState should equal to CreateReviewTemplateUiState#Showing when canRenderForm is true and template is not empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate(getReviewTemplateUseCaseResultSuccessNonEmpty)
        mockSuccessGetProductIncentiveOvo()
        assertInstanceOf<CreateReviewTemplateUiState.Loading>(viewModel.templateUiState.value)
        setInitialData()
        assertInstanceOf<CreateReviewTemplateUiState.Showing>(viewModel.templateUiState.value)
    }

    @Test
    fun `mediaPickerUiState should equal to CreateReviewMediaPickerUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewMediaPickerUiState.Loading>(viewModel.mediaPickerUiState.value)
    }

    @Test
    fun `mediaPickerUiState should equal to CreateReviewMediaPickerUiState#SuccessUpload when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewMediaPickerUiState.SuccessUpload>(viewModel.mediaPickerUiState.value)
    }

    @Test
    fun `mediaPickerUiState mediaItems should contain CreateReviewMediaUiModel#AddLarge when no media added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        val mediaPickerUiState = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        assertInstanceOf<CreateReviewMediaUiModel.AddLarge>(mediaPickerUiState.mediaItems.first())
    }

    @Test
    fun `mediaPickerUiState mediaItems should contain CreateReviewMediaUiModel#AddSmall when contain 1 video added through new media picker`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        val mediaPickerUiState = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        assertInstanceOf<CreateReviewMediaUiModel.AddSmall>(mediaPickerUiState.mediaItems.last())
    }

    @Test
    fun `mediaPickerUiState mediaItems should contain CreateReviewMediaUiModel#AddSmall when contain at least 1 image added through new media picker`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        val mediaPickerUiState = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        assertInstanceOf<CreateReviewMediaUiModel.AddSmall>(mediaPickerUiState.mediaItems.last())
    }

    @Test
    fun `mediaPickerUiState mediaItems should contain media with error state when upload image is failed`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        val mediaPickerUiState = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        val image = mediaPickerUiState.mediaItems.first() as CreateReviewMediaUiModel.Image
        Assert.assertEquals(CreateReviewMediaUiModel.State.UPLOAD_FAILED, image.state)
    }

    @Test
    fun `mediaPickerUiState mediaItems should contain media with error state when upload video is failed`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        val mediaPickerUiState = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        val video = mediaPickerUiState.mediaItems.first() as CreateReviewMediaUiModel.Video
        Assert.assertEquals(CreateReviewMediaUiModel.State.UPLOAD_FAILED, video.state)
    }

    @Test
    fun `mediaPickerUiState mediaItems should contain media with success state when upload image is success`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessUploadMedia()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        val mediaPickerUiState = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        val image = mediaPickerUiState.mediaItems.first() as CreateReviewMediaUiModel.Image
        Assert.assertEquals(CreateReviewMediaUiModel.State.UPLOADED, image.state)
    }

    @Test
    fun `mediaPickerUiState mediaItems should contain media with error state when upload video is success`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessUploadMedia()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        val mediaPickerUiState = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        val video = mediaPickerUiState.mediaItems.first() as CreateReviewMediaUiModel.Video
        Assert.assertEquals(CreateReviewMediaUiModel.State.UPLOADED, video.state)
    }

    @Test
    fun `when added new image then the already uploaded image should not be re-uploaded`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessUploadMedia()
        setInitialData()
        every { isVideoFormat(any()) } returns false
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.mediaPickerUiState.value
        viewModel.updateMediaPicker(listOf("image.jpg", "another-image.jpg"))
        viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        coVerify(exactly = 2) { uploaderUseCase(any()) }
    }

    @Test
    fun `when added new image then the already uploaded video should not be re-uploaded`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessUploadMedia()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        viewModel.mediaPickerUiState.value
        viewModel.updateMediaPicker(listOf("video.mp4", "image.jpg"))
        viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        coVerify(exactly = 2) { uploaderUseCase(any()) }
    }

    @Test
    fun `when added new image then the already failed to upload image should be re-uploaded`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia()
        setInitialData()
        every { isVideoFormat(any()) } returns false
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.mediaPickerUiState.value
        viewModel.updateMediaPicker(listOf("image.jpg", "another-image.jpg"))
        viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        coVerify(exactly = 3) { uploaderUseCase(any()) }
    }

    @Test
    fun `when added new image then the already failed to upload video should be re-uploaded`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        viewModel.mediaPickerUiState.value
        viewModel.updateMediaPicker(listOf("video.mp4", "image.jpg"))
        viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        coVerify(exactly = 3) { uploaderUseCase(any()) }
    }

    @Test
    fun `anonymousUiState should equal to CreateReviewAnonymousUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewAnonymousUiState.Loading>(viewModel.anonymousUiState.value)
    }

    @Test
    fun `anonymousUiState should equal to CreateReviewAnonymousUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewAnonymousUiState.Showing>(viewModel.anonymousUiState.value)
    }

    @Test
    fun `progressBarUiState should equal to CreateReviewProgressBarUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewProgressBarUiState.Loading>(viewModel.progressBarUiState.value)
    }

    @Test
    fun `progressBarUiState should equal to CreateReviewProgressBarUiState#Showing when canRenderForm is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewProgressBarUiState.Showing>(viewModel.progressBarUiState.value)
    }

    @Test
    fun `submitButtonUiState should equal to CreateReviewSubmitButtonUiState#Loading when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewSubmitButtonUiState.Loading>(viewModel.submitButtonUiState.value)
    }

    @Test
    fun `submitButtonUiState should equal to CreateReviewSubmitButtonUiState#Enabled when not sending review and have good rating`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewSubmitButtonUiState.Enabled>(viewModel.submitButtonUiState.value)
    }

    @Test
    fun `submitButtonUiState should equal to CreateReviewSubmitButtonUiState#Enabled when not sending review, have bad rating, only other bad rating category selected and review text is not empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 1)
        viewModel.setReviewText("a", CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        viewModel.updateBadRatingSelectedStatus(BAD_RATING_CATEGORY_ID_OTHER, true)
        assertInstanceOf<CreateReviewSubmitButtonUiState.Enabled>(viewModel.submitButtonUiState.value)
    }

    @Test
    fun `submitButtonUiState should equal to CreateReviewSubmitButtonUiState#Enabled when not sending review, have bad rating, any other bad rating category than other category selected and review text is not empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 1)
        viewModel.setReviewText("a", CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        viewModel.updateBadRatingSelectedStatus(BAD_RATING_CATEGORY_ID_PRODUCT_PROBLEM, true)
        assertInstanceOf<CreateReviewSubmitButtonUiState.Enabled>(viewModel.submitButtonUiState.value)
    }

    @Test
    fun `submitButtonUiState should equal to CreateReviewSubmitButtonUiState#Disabled when not sending review, have bad rating, only other bad rating category selected and review text is empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData(rating = 1)
        viewModel.updateBadRatingSelectedStatus(BAD_RATING_CATEGORY_ID_OTHER, true)
        assertInstanceOf<CreateReviewSubmitButtonUiState.Disabled>(viewModel.submitButtonUiState.value)
    }

    @Test
    fun `createReviewBottomSheetUiState should equal to CreateReviewBottomSheetUiState#Showing when reviewForm is success and valid`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewBottomSheetUiState.Showing>(viewModel.createReviewBottomSheetUiState.value)
    }

    @Test
    fun `createReviewBottomSheetUiState should equal to CreateReviewBottomSheetUiState#ShouldDismiss when reviewForm is error`() = runCollectingStateFlows {
        mockErrorGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        val createReviewBottomSheetUiState = viewModel.createReviewBottomSheetUiState.value
        assertInstanceOf<CreateReviewBottomSheetUiState.ShouldDismiss>(createReviewBottomSheetUiState)
        Assert.assertEquals(
            R.string.review_toaster_page_error,
            (createReviewBottomSheetUiState as CreateReviewBottomSheetUiState.ShouldDismiss).message.id
        )
        Assert.assertFalse(createReviewBottomSheetUiState.success)
        Assert.assertEquals("", createReviewBottomSheetUiState.feedbackId)
    }

    @Test
    fun `createReviewBottomSheetUiState should equal to CreateReviewBottomSheetUiState#ShouldDismiss when review is invalid`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        val createReviewBottomSheetUiState = viewModel.createReviewBottomSheetUiState.value
        assertInstanceOf<CreateReviewBottomSheetUiState.ShouldDismiss>(createReviewBottomSheetUiState)
        Assert.assertEquals(
            R.string.review_pending_invalid_to_review,
            (createReviewBottomSheetUiState as CreateReviewBottomSheetUiState.ShouldDismiss).message.id
        )
        Assert.assertFalse(createReviewBottomSheetUiState.success)
        Assert.assertEquals("", createReviewBottomSheetUiState.feedbackId)
    }

    @Test
    fun `createReviewBottomSheetUiState should equal to CreateReviewBottomSheetUiState#ShouldDismiss when product status is 0`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessProductDeleted)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        val createReviewBottomSheetUiState = viewModel.createReviewBottomSheetUiState.value
        assertInstanceOf<CreateReviewBottomSheetUiState.ShouldDismiss>(createReviewBottomSheetUiState)
        Assert.assertEquals(
            R.string.review_pending_deleted_product_error_toaster,
            (createReviewBottomSheetUiState as CreateReviewBottomSheetUiState.ShouldDismiss).message.id
        )
        Assert.assertFalse(createReviewBottomSheetUiState.success)
        Assert.assertEquals("", createReviewBottomSheetUiState.feedbackId)
    }

    @Test
    fun `incentiveBottomSheetUiState should equal to CreateReviewIncentiveBottomSheetUiState#Hidden when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewIncentiveBottomSheetUiState.Hidden>(viewModel.incentiveBottomSheetUiState.value)
    }

    @Test
    fun `incentiveBottomSheetUiState should equal to CreateReviewIncentiveBottomSheetUiState#Hidden when canRenderForm is true and incentive ovo result is null`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo(null)
        setInitialData()
        viewModel.showIncentiveBottomSheet()
        assertInstanceOf<CreateReviewIncentiveBottomSheetUiState.Hidden>(viewModel.incentiveBottomSheetUiState.value)
    }

    @Test
    fun `incentiveBottomSheetUiState should equal to CreateReviewIncentiveBottomSheetUiState#Hidden when canRenderForm is true and shouldShowIncentiveBottomSheet is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewIncentiveBottomSheetUiState.Hidden>(viewModel.incentiveBottomSheetUiState.value)
    }

    @Test
    fun `incentiveBottomSheetUiState should equal to CreateReviewIncentiveBottomSheetUiState#Showing when canRenderForm is true and incentive ovo result is not null`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.showIncentiveBottomSheet()
        assertInstanceOf<CreateReviewIncentiveBottomSheetUiState.Showing>(viewModel.incentiveBottomSheetUiState.value)
    }

    @Test
    fun `dismissIncentiveBottomSheet should change incentiveBottomSheetUiState equal to CreateReviewIncentiveBottomSheetUiState#Hidden`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.showIncentiveBottomSheet()
        assertInstanceOf<CreateReviewIncentiveBottomSheetUiState.Showing>(viewModel.incentiveBottomSheetUiState.value)
        viewModel.dismissIncentiveBottomSheet()
        assertInstanceOf<CreateReviewIncentiveBottomSheetUiState.Hidden>(viewModel.incentiveBottomSheetUiState.value)
    }

    @Test
    fun `textAreaBottomSheetUiState should equal to CreateReviewTextAreaBottomSheetUiState#Hidden when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTextAreaBottomSheetUiState.Hidden>(viewModel.textAreaBottomSheetUiState.value)
    }

    @Test
    fun `textAreaBottomSheetUiState should equal to CreateReviewTextAreaBottomSheetUiState#Hidden when canRenderForm is true and shouldShowTextAreaBottomSheet is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTextAreaBottomSheetUiState.Hidden>(viewModel.textAreaBottomSheetUiState.value)
    }

    @Test
    fun `textAreaBottomSheetUiState should equal to CreateReviewTextAreaBottomSheetUiState#Showing when canRenderForm is true and shouldShowTextAreaBottomSheet is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.showTextAreaBottomSheet()
        assertInstanceOf<CreateReviewTextAreaBottomSheetUiState.Showing>(viewModel.textAreaBottomSheetUiState.value)
    }

    @Test
    fun `dismissTextAreaBottomSheet should change textAreaBottomSheetUiState equal to CreateReviewTextAreaBottomSheetUiState#Hidden`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.showTextAreaBottomSheet()
        assertInstanceOf<CreateReviewTextAreaBottomSheetUiState.Showing>(viewModel.textAreaBottomSheetUiState.value)
        viewModel.dismissTextAreaBottomSheet()
        assertInstanceOf<CreateReviewTextAreaBottomSheetUiState.Hidden>(viewModel.textAreaBottomSheetUiState.value)
    }

    @Test
    fun `postSubmitBottomSheetUiState should equal to PostSubmitUiState#ShowThankYouToaster with null data when postSubmitReviewResult is not success`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessSubmitReview()
        mockErrorPostSubmitBottomSheet()
        setInitialData()
        viewModel.submitReview()
        assertInstanceOf<PostSubmitUiState.ShowThankYouToaster>(viewModel.postSubmitBottomSheetUiState.value)
        Assert.assertEquals(null, (viewModel.postSubmitBottomSheetUiState.value as PostSubmitUiState.ShowThankYouToaster).data)
    }

    @Test
    fun `postSubmitBottomSheetUiState should equal to PostSubmitUiState#ShowThankYouBottomSheet when postSubmitReviewResult is success`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessSubmitReview()
        mockSuccessPostSubmitBottomSheet()
        setInitialData()
        viewModel.submitReview()
        assertInstanceOf<PostSubmitUiState.ShowThankYouBottomSheet>(viewModel.postSubmitBottomSheetUiState.value)
    }

    @Test
    fun `postSubmitBottomSheetUiState should equal to PostSubmitUiState#ShowThankYouToaster when postSubmitReviewResult is success`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessSubmitReview()
        mockSuccessPostSubmitBottomSheet(getPostSubmitBottomSheetResultSuccessShowToaster)
        setInitialData()
        viewModel.submitReview()
        assertInstanceOf<PostSubmitUiState.ShowThankYouToaster>(viewModel.postSubmitBottomSheetUiState.value)
    }

    @Test
    fun `anonymousInfoBottomSheetUiState should equal to CreateReviewAnonymousInfoBottomSheetUiState#Hidden when canRenderForm is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessInvalid)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewAnonymousInfoBottomSheetUiState.Hidden>(viewModel.anonymousInfoBottomSheetUiState.value)
    }

    @Test
    fun `anonymousInfoBottomSheetUiState should equal to CreateReviewAnonymousInfoBottomSheetUiState#Hidden when canRenderForm is true and shouldShowAnonymousInfoBottomSheet is false`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewAnonymousInfoBottomSheetUiState.Hidden>(viewModel.anonymousInfoBottomSheetUiState.value)
    }

    @Test
    fun `anonymousInfoBottomSheetUiState should equal to CreateReviewAnonymousInfoBottomSheetUiState#Showing when canRenderForm is true and shouldShowAnonymousInfoBottomSheet is true`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.showAnonymousInfoBottomSheet()
        assertInstanceOf<CreateReviewAnonymousInfoBottomSheetUiState.Showing>(viewModel.anonymousInfoBottomSheetUiState.value)
    }

    @Test
    fun `dismissAnonymousInfoBottomSheet should change anonymousInfoBottomSheetUiState equal to CreateReviewAnonymousInfoBottomSheetUiState#Hidden`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.showAnonymousInfoBottomSheet()
        assertInstanceOf<CreateReviewAnonymousInfoBottomSheetUiState.Showing>(viewModel.anonymousInfoBottomSheetUiState.value)
        viewModel.dismissAnonymousInfoBottomSheet()
        assertInstanceOf<CreateReviewAnonymousInfoBottomSheetUiState.Hidden>(viewModel.anonymousInfoBottomSheetUiState.value)
    }

    @Test
    fun `topicsUiState should equal to CreateReviewTopicsUiState#Hidden when canRenderForm is false and review topics inspiration is enabled`() = runCollectingStateFlows {
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessProductDeleted)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTopicsUiState.Hidden>(viewModel.topicsUiState.value)
    }

    @Test
    fun `topicsUiState should equal to CreateReviewTopicsUiState#Hidden when postSubmitReviewResult is not success and review topics inspiration is enabled`() = runCollectingStateFlows {
        mockStringAb("review_inspiration", "experiment_variant", "experiment_variant")
        mockErrorGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTopicsUiState.Hidden>(viewModel.topicsUiState.value)
    }

    @Test
    fun `topicsUiState should equal to CreateReviewTopicsUiState#Hidden when keywords is empty and review topics inspiration is enabled`() = runCollectingStateFlows {
        mockStringAb("review_inspiration", "experiment_variant", "experiment_variant")
        mockSuccessGetReputationForm(getReputationFormUseCaseResultSuccessValidWithEmptyKeywords)
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTopicsUiState.Hidden>(viewModel.topicsUiState.value)
    }

    @Test
    fun `topicsUiState should equal to CreateReviewTopicsUiState#Hidden when keywords is not empty and review topics inspiration is disabled`() = runCollectingStateFlows {
        mockStringAb("review_inspiration", "experiment_variant", "control_variant")
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTopicsUiState.Hidden>(viewModel.topicsUiState.value)
    }

    @Test
    fun `topicsUiState should equal to CreateReviewTopicsUiState#Showing when keywords is not empty and review topics inspiration is enabled`() = runCollectingStateFlows {
        mockStringAb("review_inspiration", "experiment_variant", "experiment_variant")
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        assertInstanceOf<CreateReviewTopicsUiState.Showing>(viewModel.topicsUiState.value)
    }

    @Test
    fun `should enqueue toaster error when submit review returns error`() = runCollectingStateFlows { toasterQueue ->
        val expectedToasterQueue = CreateReviewToasterUiModel(
            message = StringRes(R.string.review_create_fail_toaster, listOf(ERROR_CODE)),
            actionText = StringRes(R.string.review_oke),
            duration = Toaster.LENGTH_SHORT,
            type = Toaster.TYPE_ERROR,
            payload = Unit
        )
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorSubmitReview()
        mockErrorHandler()
        setInitialData()
        viewModel.submitReview()
        Assert.assertEquals(expectedToasterQueue, toasterQueue.last())
    }

    @Test
    fun `should enqueue toaster error when retry upload media returns error`() = runCollectingStateFlows { toasterQueue ->
        val expectedToasterQueue = CreateReviewToasterUiModel(
            message = StringRes(R.string.review_form_media_picker_toaster_failed_upload_message, listOf(ERROR_CODE)),
            actionText = StringRes(Int.ZERO),
            duration = Toaster.LENGTH_SHORT,
            type = Toaster.TYPE_ERROR,
            payload = Unit
        )
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia(withException = false)
        mockErrorHandler()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.retryUploadMedia()
        Assert.assertEquals(expectedToasterQueue, toasterQueue.last())
    }

    @Test
    fun `should enqueue toaster error when try to submit review and there's at least 1 failed to upload media`() = runCollectingStateFlows { toasterQueue ->
        val expectedToasterQueue = CreateReviewToasterUiModel(
            message = StringRes(R.string.review_form_media_picker_toaster_failed_upload_message, listOf(ERROR_CODE)),
            actionText = StringRes(Int.ZERO),
            duration = Toaster.LENGTH_SHORT,
            type = Toaster.TYPE_ERROR,
            payload = Unit
        )
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia()
        mockErrorHandler()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.submitReview()
        Assert.assertEquals(expectedToasterQueue, toasterQueue.last())
    }

    @Test
    fun `should enqueue toaster when try to submit review and there's at least 1 uploading image`() = runCollectingStateFlows { toasterQueue ->
        val expectedToasterQueue = CreateReviewToasterUiModel(
            message = StringRes(R.string.review_form_media_picker_toaster_wait_for_upload_message),
            actionText = StringRes(Int.ZERO),
            duration = Toaster.LENGTH_SHORT,
            type = Toaster.TYPE_NORMAL,
            payload = Unit
        )
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia(delayTime = 10000L)
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.submitReview()
        Assert.assertEquals(expectedToasterQueue, toasterQueue.last())
    }

    @Test
    fun `should enqueue toaster when try to submit review and there's at least 1 uploading video`() = runCollectingStateFlows { toasterQueue ->
        val expectedToasterQueue = CreateReviewToasterUiModel(
            message = StringRes(R.string.review_form_media_picker_toaster_wait_for_upload_message),
            actionText = StringRes(Int.ZERO),
            duration = Toaster.LENGTH_SHORT,
            type = Toaster.TYPE_NORMAL,
            payload = Unit
        )
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockErrorUploadMedia(delayTime = 10000L)
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        viewModel.submitReview()
        Assert.assertEquals(expectedToasterQueue, toasterQueue.last())
    }

    @Test
    fun `selectTemplate should add template text to review text`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate(getReviewTemplateUseCaseResultSuccessNonEmpty)
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        val templateToSelect = viewModel.templateUiState.value.templates.first().data
        viewModel.selectTemplate(templateToSelect)
        Assert.assertEquals(templateToSelect.text, (viewModel.textAreaUiState.value as CreateReviewTextAreaUiState.Showing).reviewTextAreaTextUiModel.text)
    }

    @Test
    fun `getProductId should return current product ID`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(SAMPLE_PRODUCT_ID, viewModel.getProductId())
    }

    @Test
    fun `getOrderId should return current order ID`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(SAMPLE_ORDER_ID, viewModel.getOrderId())
    }

    @Test
    fun `getReputationId should return current reputation ID`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(SAMPLE_REPUTATION_ID, viewModel.getReputationId())
    }

    @Test
    fun `getUtmSource should return current UTM source ID`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(SAMPLE_UTM_SOURCE, viewModel.getUtmSource())
    }

    @Test
    fun `hasIncentive should return current has incentive value`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(true, viewModel.hasIncentive())
    }

    @Test
    fun `hasOngoingChallenge should return current has ongoing challenge value`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo(getProductIncentiveOvoUseCaseResultSuccessChallenge)
        setInitialData()
        Assert.assertEquals(true, viewModel.hasOngoingChallenge())
    }

    @Test
    fun `isGoodRating should return current good rating value`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(true, viewModel.isGoodRating())
    }

    @Test
    fun `isReviewTextEmpty should return true when review text empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(true, viewModel.isReviewTextEmpty())
    }

    @Test
    fun `isMediaEmpty should return true when data not yet loaded`() = runCollectingStateFlows {
        Assert.assertEquals(true, viewModel.isMediaEmpty())
    }

    @Test
    fun `isMediaEmpty should return true when data loaded and no media added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(true, viewModel.isMediaEmpty())
    }

    @Test
    fun `isMediaEmpty should return false when data loaded and 1 image added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        Assert.assertEquals(false, viewModel.isMediaEmpty())
    }

    @Test
    fun `isMediaEmpty should return false when data loaded and 1 video added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        Assert.assertEquals(false, viewModel.isMediaEmpty())
    }

    @Test
    fun `getUserId should return current user ID`() = runCollectingStateFlows {
        Assert.assertEquals(SAMPLE_USER_ID, viewModel.getUserId())
    }

    @Test
    fun `getShopId should return current shop ID`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(SAMPLE_SHOP_ID, viewModel.getShopId())
    }

    @Test
    fun `getSelectedMediasUrl should return empty when no media added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertTrue(viewModel.getSelectedMediasUrl().isEmpty())
    }

    @Test
    fun `getSelectedMediasUrl should return non empty when 1 image added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        Assert.assertTrue(viewModel.getSelectedMediasUrl().isNotEmpty())
    }

    @Test
    fun `getSelectedMediasUrl should return non empty when 1 video added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        Assert.assertTrue(viewModel.getSelectedMediasUrl().isNotEmpty())
    }

    @Test
    fun `getSelectedMediaFiles should return empty when no media added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertTrue(viewModel.getSelectedMediaFiles().isEmpty())
    }

    @Test
    fun `getSelectedMediaFiles should return non empty when 1 image added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        Assert.assertTrue(viewModel.getSelectedMediaFiles().isNotEmpty())
    }

    @Test
    fun `getSelectedMediaFiles should return non empty when 1 video added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        Assert.assertTrue(viewModel.getSelectedMediaFiles().isNotEmpty())
    }

    @Test
    fun `getRating should return current rating`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(5, viewModel.getRating())
    }

    @Test
    fun `getReviewMessageLength should return review text length`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.setReviewText("a".repeat(40), CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        Assert.assertEquals(40, viewModel.getReviewMessageLength())
    }

    @Test
    fun `getNumberOfMedia should return empty when no media added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(0, viewModel.getNumberOfMedia())
    }

    @Test
    fun `getNumberOfMedia should return 1 when 1 image added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        Assert.assertEquals(1, viewModel.getNumberOfMedia())
    }

    @Test
    fun `getNumberOfMedia should return 1 when 1 video added`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        Assert.assertEquals(1, viewModel.getNumberOfMedia())
    }

    @Test
    fun `isAnonymous should return current true when user check anonymous checkbox`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.setAnonymous(true)
        Assert.assertTrue(viewModel.isAnonymous())
    }

    @Test
    fun `isAnonymous should return current true when user uncheck anonymous checkbox`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.setAnonymous(true)
        viewModel.setAnonymous(false)
        Assert.assertFalse(viewModel.isAnonymous())
    }

    @Test
    fun `isTemplateAvailable should return false when template is not loaded`() = runCollectingStateFlows {
        Assert.assertFalse(viewModel.isTemplateAvailable())
    }

    @Test
    fun `isTemplateAvailable should return false when template is loaded but empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertFalse(viewModel.isTemplateAvailable())
    }

    @Test
    fun `isTemplateAvailable should return true when template is loaded and not empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate(getReviewTemplateUseCaseResultSuccessNonEmpty)
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertTrue(viewModel.isTemplateAvailable())
    }

    @Test
    fun `getSelectedTemplateCount should return zero when template is not loaded`() = runCollectingStateFlows {
        Assert.assertEquals(0, viewModel.getSelectedTemplateCount())
    }

    @Test
    fun `getSelectedTemplateCount should return zero when template is loaded but empty`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(0, viewModel.getSelectedTemplateCount())
    }

    @Test
    fun `getSelectedTemplateCount should return zero when template is loaded, not empty and no template selected`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate(getReviewTemplateUseCaseResultSuccessNonEmpty)
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertEquals(0, viewModel.getSelectedTemplateCount())
    }

    @Test
    fun `getSelectedTemplateCount should return non zero when template is loaded, not empty and at least 1 template selected`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate(getReviewTemplateUseCaseResultSuccessNonEmpty)
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        val templateToSelect = viewModel.templateUiState.value.templates.last().data
        viewModel.selectTemplate(templateToSelect)
        Assert.assertEquals(1, viewModel.getSelectedTemplateCount())
    }

    @Test
    fun `isReviewComplete should return true when review data is complete on non incentive or challenge review`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo(getProductIncentiveOvoUseCaseResultSuccessNoIncentive)
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.setReviewText("a", CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        Assert.assertTrue(viewModel.isReviewComplete())
    }

    @Test
    fun `isReviewComplete should return true when review data is complete on incentive review`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo(getProductIncentiveOvoUseCaseResultSuccessIncentive)
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.setReviewText("a".repeat(40), CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        Assert.assertTrue(viewModel.isReviewComplete())
    }

    @Test
    fun `isReviewComplete should return true when review data is complete on challenge review`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo(getProductIncentiveOvoUseCaseResultSuccessChallenge)
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.setReviewText("a".repeat(40), CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
        Assert.assertTrue(viewModel.isReviewComplete())
    }

    @Test
    fun `isReviewComplete should return true when review data is not complete`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        Assert.assertFalse(viewModel.isReviewComplete())
    }

    @Test
    fun `getUserName should return current user name`() = runCollectingStateFlows {
        Assert.assertEquals(SAMPLE_USER_NAME, viewModel.getUserName())
    }

    @Test
    fun `getFeedbackId should return current review feedback ID`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessSubmitReview()
        mockSuccessPostSubmitBottomSheet()
        setInitialData()
        viewModel.submitReview()
        Assert.assertEquals(SAMPLE_FEEDBACK_ID, viewModel.getFeedbackId())
    }

    @Test
    fun `removeMedia should remove media from list of added media`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        val mediaPickerUiStateWith1Image = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        viewModel.removeMedia(mediaPickerUiStateWith1Image.mediaItems.first())
        val mediaPickerUiStateWith0Media = viewModel.mediaPickerUiState.value as CreateReviewMediaPickerUiState.HasMedia
        Assert.assertEquals(1, mediaPickerUiStateWith0Media.mediaItems.size)
        assertInstanceOf<CreateReviewMediaUiModel.AddLarge>(mediaPickerUiStateWith0Media.mediaItems.first())
    }

    @Test
    fun `enqueueDisabledAddMoreMediaToaster should enqueue new toaster`() = runCollectingStateFlows { toasterQueue ->
        val expectedToasterQueue = CreateReviewToasterUiModel(
            message = StringRes(R.string.review_form_cannot_add_more_media_while_uploading),
            actionText = StringRes(Int.ZERO),
            duration = Toaster.LENGTH_SHORT,
            type = Toaster.TYPE_NORMAL,
            payload = Unit
        )
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.enqueueDisabledAddMoreMediaToaster()
        Assert.assertEquals(expectedToasterQueue, toasterQueue.last())
    }

    @Test
    fun `submit review should include uploaded image attachment ID when there's any uploaded image`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessUploadMedia()
        mockSuccessSubmitReview()
        mockSuccessPostSubmitBottomSheet()
        setInitialData()
        viewModel.updateMediaPicker(listOf("image.jpg"))
        viewModel.submitReview()
        coVerify {
            submitReviewUseCase.setParams(
                ProductrevSubmitReviewUseCase.SubmitReviewRequestParams(
                    reputationId = SAMPLE_REPUTATION_ID,
                    productId = SAMPLE_PRODUCT_ID,
                    shopId = SAMPLE_SHOP_ID,
                    reputationScore = Int.ZERO,
                    rating = 5,
                    reviewText = "",
                    isAnonymous = false,
                    attachmentIds = listOf(SAMPLE_UPLOAD_ID),
                    videoAttachments = emptyList(),
                    utmSource = SAMPLE_UTM_SOURCE,
                    badRatingCategoryIds = emptyList(),
                )
            )
        }
    }

    @Test
    fun `submit review should include uploaded video attachment ID and URL when there's any uploaded video`() = runCollectingStateFlows {
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        mockSuccessUploadMedia()
        mockSuccessSubmitReview()
        mockSuccessPostSubmitBottomSheet()
        setInitialData()
        viewModel.updateMediaPicker(listOf("video.mp4"))
        viewModel.submitReview()
        coVerify {
            submitReviewUseCase.setParams(
                ProductrevSubmitReviewUseCase.SubmitReviewRequestParams(
                    reputationId = SAMPLE_REPUTATION_ID,
                    productId = SAMPLE_PRODUCT_ID,
                    shopId = SAMPLE_SHOP_ID,
                    reputationScore = Int.ZERO,
                    rating = 5,
                    reviewText = "",
                    isAnonymous = false,
                    attachmentIds = emptyList(),
                    videoAttachments = listOf(
                        ProductrevSubmitReviewUseCase.SubmitReviewRequestParams.VideoAttachment(
                            SAMPLE_UPLOAD_ID, SAMPLE_VIDEO_URL
                        )
                    ),
                    utmSource = SAMPLE_UTM_SOURCE,
                    badRatingCategoryIds = emptyList(),
                )
            )
        }
    }

    @Test
    fun `hasTemplate should return true when contain template`() = runCollectingStateFlows {
        `templateUiState should equal to CreateReviewTemplateUiState#Showing when canRenderForm is true and template is not empty`()
        Assert.assertTrue(viewModel.hasTemplate())
    }

    @Test
    fun `hasTemplate should return false when does not contain template`() = runCollectingStateFlows {
        `templateUiState should equal to CreateReviewTemplateUiState#Hidden when canRenderForm is true and template is empty`()
        Assert.assertFalse(viewModel.hasTemplate())
    }

    @Test
    fun `hasTemplate should return false when template not loaded`() = runCollectingStateFlows {
        Assert.assertFalse(viewModel.hasTemplate())
    }

    @Test
    fun `templateUsedCount should return 0 when contain template and no template used`() = runCollectingStateFlows {
        `templateUiState should equal to CreateReviewTemplateUiState#Showing when canRenderForm is true and template is not empty`()
        Assert.assertEquals(0, viewModel.templateUsedCount())
    }

    @Test
    fun `templateUsedCount should return 1 when contain template and 1 template used`() = runCollectingStateFlows {
        `selectTemplate should add template text to review text`()
        Assert.assertEquals(1, viewModel.templateUsedCount())
    }

    @Test
    fun `templateUsedCount should 0 false when does not contain template`() = runCollectingStateFlows {
        `templateUiState should equal to CreateReviewTemplateUiState#Hidden when canRenderForm is true and template is empty`()
        Assert.assertEquals(0, viewModel.templateUsedCount())
    }

    @Test
    fun `templateUsedCount should return 0 when template not loaded`() = runCollectingStateFlows {
        Assert.assertEquals(0, viewModel.templateUsedCount())
    }

    @Test
    fun `saveUiState should save current state`() = runCollectingStateFlows {
        mockk<Bundle>(relaxed = true) {
            viewModel.saveUiState(this)
            verify { putInt("savedStateRating", any()) }
            verify { putSerializable("savedStateReviewText", any()) }
            verify { putBoolean("savedStateAnonymous", any()) }
            verify { putStringArrayList("savedStateMediaUris", any()) }
            verify { putSerializable("savedStateUploadResults", any()) }
            verify { putBoolean("savedStateEditMode", any()) }
            verify { putString("savedStateProductId", any()) }
            verify { putString("savedStateShopId", any()) }
            verify { putString("savedStateOrderId", any()) }
            verify { putString("savedStateFeedbackId", any()) }
            verify { putString("savedStateReputationId", any()) }
            verify { putString("savedStateUtmSource", any()) }
            verify { putBoolean("savedStateShowIncentiveBottomSheet", any()) }
            verify { putBoolean("savedStateShowTextAreaBottomSheet", any()) }
            verify { putBoolean("savedStateShowAnonymousInfoBottomSheet", any()) }
            verify { putBoolean("savedStateSendingReview", any()) }
            verify { putSerializable("savedStateReviewForm", any()) }
            verify { putSerializable("savedStateIncentiveOvo", any()) }
            verify { putSerializable("savedStateReviewTemplate", any()) }
            verify { putSerializable("savedStateBadRatingCategories", any()) }
            verify { putSerializable("savedStateSubmitReviewResult", any()) }
            verify { putSerializable("savedStatePostSubmitReviewResult", any()) }
        }
    }

    @Test
    fun `restoreUiState should restore saved state`() = runCollectingStateFlows {
        mockk<Bundle>(relaxed = true) {
            every { this@mockk.get(any()) } answers {
                when (args.first()) {
                    "savedStateReviewForm" -> ReviewFormRequestSuccessState(getReputationFormUseCaseResultSuccessValidWithNonEmptyKeywords)
                    "savedStateIncentiveOvo" -> IncentiveOvoRequestSuccessState(getProductIncentiveOvoUseCaseResultSuccessIncentive)
                    "savedStateReviewTemplate" -> ReviewTemplateRequestSuccessState(emptyList())
                    "savedStateBadRatingCategories" -> BadRatingCategoriesRequestSuccessState(getBadRatingCategoryUseCaseResultSuccessNonEmpty.productrevGetBadRatingCategory.list)
                    "savedStateSubmitReviewResult" -> mockk<RequestState.Idle>(relaxed = true)
                    "savedStatePostSubmitReviewResult" -> mockk<RequestState.Idle>(relaxed = true)
                    "savedStateRating" -> 5
                    "savedStateAnonymous" -> false
                    "savedStateMediaUris" -> emptyList<String>()
                    "savedStateUploadResults" -> emptyMap<String, CreateReviewMediaUploadResult>()
                    "savedStateEditMode" -> false
                    "savedStateOrderId" -> ""
                    "savedStateFeedbackId" -> ""
                    "savedStateUtmSource" -> ""
                    "savedStateShowIncentiveBottomSheet" -> false
                    "savedStateShowTextAreaBottomSheet" -> false
                    "savedStateShowAnonymousInfoBottomSheet" -> false
                    "savedStateSendingReview" -> false
                    "savedStateReviewText" -> CreateReviewTextAreaTextUiModel()
                    "savedStateShopId" -> ""
                    "savedStateProductId" -> ""
                    "savedStateReputationId" -> ""
                    else -> mockk(relaxed = true)
                }
            }
            viewModel.restoreUiState(this)
            verify { this@mockk.get("savedStateReviewForm") }
            verify { this@mockk.get("savedStateIncentiveOvo") }
            verify { this@mockk.get("savedStateReviewTemplate") }
            verify { this@mockk.get("savedStateBadRatingCategories") }
            verify { this@mockk.get("savedStateSubmitReviewResult") }
            verify { this@mockk.get("savedStatePostSubmitReviewResult") }
            verify { this@mockk.get("savedStateRating") }
            verify { this@mockk.get("savedStateAnonymous") }
            verify { this@mockk.get("savedStateMediaUris") }
            verify { this@mockk.get("savedStateUploadResults") }
            verify { this@mockk.get("savedStateEditMode") }
            verify { this@mockk.get("savedStateOrderId") }
            verify { this@mockk.get("savedStateFeedbackId") }
            verify { this@mockk.get("savedStateUtmSource") }
            verify { this@mockk.get("savedStateShowIncentiveBottomSheet") }
            verify { this@mockk.get("savedStateShowTextAreaBottomSheet") }
            verify { this@mockk.get("savedStateShowAnonymousInfoBottomSheet") }
            verify { this@mockk.get("savedStateSendingReview") }
            verify { this@mockk.get("savedStateReviewText") }
            verify { this@mockk.get("savedStateShopId") }
            verify { this@mockk.get("savedStateProductId") }
            verify { this@mockk.get("savedStateReputationId") }
        }
    }

    @Test
    fun `setBottomSheetBottomInset should update createReviewBottomSheetUiState when createReviewBottomSheetUiState is CreateReviewBottomSheetUiState#Showing`() = runCollectingStateFlows {
        val expectedInset = 100
        mockSuccessGetReputationForm()
        mockSuccessGetReviewTemplate()
        mockSuccessGetProductIncentiveOvo()
        setInitialData()
        viewModel.setBottomSheetBottomInset(expectedInset)
        val createReviewBottomSheetUiState = viewModel.createReviewBottomSheetUiState.value
        Assert.assertEquals(expectedInset, (createReviewBottomSheetUiState as CreateReviewBottomSheetUiState.Showing).bottomInset)
    }
}
