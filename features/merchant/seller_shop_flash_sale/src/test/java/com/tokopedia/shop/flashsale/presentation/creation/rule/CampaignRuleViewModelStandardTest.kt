package com.tokopedia.shop.flashsale.presentation.creation.rule

import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.util.CampaignDataGenerator
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.StandardTestRule
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

class CampaignRuleViewModelStandardTest: CampaignRuleViewModelTestFixture() {

    override fun provideTestRule() = StandardTestRule()

    @Test
    fun `check isInputChanged are false and then restart the TNC Confirmation Status is not running`() {
        runTest {
            viewModel.setPrivateProperty("initialPaymentType", PaymentType.INSTANT)
            viewModel.setPrivateProperty("initialUniqueBuyer", true)
            viewModel.setPrivateProperty("initialCampaignRelation", true)
            viewModel.setPrivateProperty(
                "initialCampaignRelations",
                CampaignDataGenerator.generateRelatedCampaigns()
            )

            viewModel.onInstantPaymentMethodSelected()
            viewModel.onNotRequireUniqueAccountSelected()
            viewModel.onAllowCampaignRelation()
            viewModel.onRelatedCampaignsChanged(CampaignDataGenerator.generateRelatedCampaigns())
            advanceUntilIdle()
            val method =
                viewModel.javaClass.getDeclaredMethod("resetTNCConfirmationStatusIfDataChanged")

            method.isAccessible = true
            method.invoke(viewModel)
            verify(exactly = 0) {
                viewModel.onTNCCheckboxUnchecked()
            }
        }
    }

    private inline fun <reified T> T.setPrivateProperty(name: String, value: Any?) {
        T::class.java.getDeclaredField(name)
            .apply { isAccessible = true }
            .set(this, value)
    }

    inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? =
        T::class.java.getDeclaredField(name)
            .apply { isAccessible = true }
            .get(this) as R

}
