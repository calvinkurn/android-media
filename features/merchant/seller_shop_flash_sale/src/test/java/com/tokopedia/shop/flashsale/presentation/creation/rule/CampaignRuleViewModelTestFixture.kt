package com.tokopedia.shop.flashsale.presentation.creation.rule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.campaign.usecase.RolloutFeatureVariantsUseCase
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC.TncRequest
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.ValidateCampaignCreationEligibilityUseCase
import com.tokopedia.unit.test.rule.AbstractTestRule
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Result
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import java.util.*

open class CampaignRuleViewModelTestFixture {

    @RelaxedMockK
    lateinit var getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase

    @RelaxedMockK
    lateinit var doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase

    @RelaxedMockK
    lateinit var validateCampaignCreationEligibilityUseCase: ValidateCampaignCreationEligibilityUseCase

    @RelaxedMockK
    lateinit var tracker: ShopFlashSaleTracker

    @RelaxedMockK
    lateinit var campaignObserver: Observer<in Result<CampaignUiModel>>

    @RelaxedMockK
    lateinit var selectedPaymentTypeObserver: Observer<in PaymentType?>

    @RelaxedMockK
    lateinit var isUniqueBuyerObserver: Observer<in Boolean?>

    @RelaxedMockK
    lateinit var isCampaignRelationObserver: Observer<in Boolean?>

    @RelaxedMockK
    lateinit var relatedCampaignsObserver: Observer<in List<RelatedCampaign>?>

    @RelaxedMockK
    lateinit var isRelatedCampaignsVisibleObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isRelatedCampaignButtonActiveObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isRelatedCampaignRemovedObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isAllInputValidObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var tncClickEventObserver: Observer<in TncRequest>

    @RelaxedMockK
    lateinit var isTNCConfirmedObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var isCampaignCreationAllowedObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var saveDraftActionStateObserver: Observer<in CampaignRuleActionResult>

    @RelaxedMockK
    lateinit var createCampaignActionStateObserver: Observer<in CampaignRuleActionResult>

    @RelaxedMockK
    lateinit var addRelatedCampaignButtonEventObserver: Observer<in AddRelatedCampaignRequest>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testRule = provideTestRule()

    lateinit var viewModel: CampaignRuleViewModel

    open fun provideTestRule(): TestRule = CoroutineTestRule()

    fun dispatchers() = (testRule as AbstractTestRule).dispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = CampaignRuleViewModel(
            getSellerCampaignDetailUseCase,
            doSellerCampaignCreationUseCase,
            validateCampaignCreationEligibilityUseCase,
            tracker,
            dispatchers()
        )

        with(viewModel) {
            campaign.observeForever(campaignObserver)
            selectedPaymentType.observeForever(selectedPaymentTypeObserver)
            isUniqueBuyer.observeForever(isUniqueBuyerObserver)
            isCampaignRelation.observeForever(isCampaignRelationObserver)
            relatedCampaigns.observeForever(relatedCampaignsObserver)
            isRelatedCampaignsVisible.observeForever(isRelatedCampaignsVisibleObserver)
            isRelatedCampaignButtonActive.observeForever(isRelatedCampaignButtonActiveObserver)
            isRelatedCampaignRemoved.observeForever(isRelatedCampaignRemovedObserver)
            isAllInputValid.observeForever(isAllInputValidObserver)
            tncClickEvent.observeForever(tncClickEventObserver)
            isTNCConfirmed.observeForever(isTNCConfirmedObserver)
            isCampaignCreationAllowed.observeForever(isCampaignCreationAllowedObserver)
            saveDraftActionState.observeForever(saveDraftActionStateObserver)
            createCampaignActionState.observeForever(createCampaignActionStateObserver)
            addRelatedCampaignButtonEvent.observeForever(addRelatedCampaignButtonEventObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            campaign.removeObserver(campaignObserver)
            selectedPaymentType.removeObserver(selectedPaymentTypeObserver)
            isUniqueBuyer.removeObserver(isUniqueBuyerObserver)
            isCampaignRelation.removeObserver(isCampaignRelationObserver)
            relatedCampaigns.removeObserver(relatedCampaignsObserver)
            isRelatedCampaignsVisible.removeObserver(isRelatedCampaignsVisibleObserver)
            isRelatedCampaignButtonActive.removeObserver(isRelatedCampaignButtonActiveObserver)
            isRelatedCampaignRemoved.removeObserver(isRelatedCampaignRemovedObserver)
            isAllInputValid.removeObserver(isAllInputValidObserver)
            tncClickEvent.removeObserver(tncClickEventObserver)
            isTNCConfirmed.removeObserver(isTNCConfirmedObserver)
            isCampaignCreationAllowed.removeObserver(isCampaignCreationAllowedObserver)
            saveDraftActionState.removeObserver(saveDraftActionStateObserver)
            createCampaignActionState.removeObserver(createCampaignActionStateObserver)
            addRelatedCampaignButtonEvent.removeObserver(addRelatedCampaignButtonEventObserver)
        }
    }
}
