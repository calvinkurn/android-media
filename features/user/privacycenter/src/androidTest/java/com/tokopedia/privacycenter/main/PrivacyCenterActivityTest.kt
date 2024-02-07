package com.tokopedia.privacycenter.main

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.privacycenter.di.ActivityComponentFactory
import com.tokopedia.privacycenter.main.di.ConsentWithdrawalState
import com.tokopedia.privacycenter.main.di.DaggerTestAppComponent
import com.tokopedia.privacycenter.main.di.FakeActivityComponentFactory
import com.tokopedia.privacycenter.main.di.FakeAppModule
import com.tokopedia.privacycenter.main.di.FakeGraphqlRepository
import com.tokopedia.privacycenter.main.di.RecommendationState
import com.tokopedia.privacycenter.ui.main.PrivacyCenterActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class PrivacyCenterActivityTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(PrivacyCenterActivity::class.java, false, false)

    private lateinit var ctx: Context

    private lateinit var repositoryStub: FakeGraphqlRepository

    @Before
    fun setUp() {
        ctx = ApplicationProvider.getApplicationContext()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        val component = DaggerTestAppComponent.builder().appModule(FakeAppModule(ctx)).build()
//        fakeGql = component.fakeGraphql() as FakeGraphqlUseCase
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        ActivityComponentFactory.instance = FakeActivityComponentFactory()
        repositoryStub = component.graphqlRepository() as FakeGraphqlRepository
    }

    @After
    fun tear() {
        InstrumentationAuthHelper.clearUserSession()
    }

    @Test
    fun basic_test() {
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot { } assert {
            shouldShowCorrectHeader(name = "Hai, Erick Samuel (test)")
        }

        privacyCenterRobot {
            scrollToBottom()
            clickRiwayatKebijakan()
        } assert {
            shouldDisplayPrivacyTestData()
        }
    }

    @Test
    fun check_activity_section() {
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot { } assert {
            shouldShowActivitySection()
        }
    }

    @Test
    fun check_recommendation_section_opt_in() {
        repositoryStub.setRecommendationResponse(RecommendationState.RECOMMENDATION_FRIEND_OPT_IN)
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot { } assert {
            shouldShowRecommendationSectionSuccess()
            shouldShowRecommendationShareFriend(isActivated = true)
        }
    }

    @Test
    fun check_recommendation_section_opt_out() {
        repositoryStub.setRecommendationResponse(RecommendationState.RECOMMENDATION_FRIEND_OPT_OUT)
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot { } assert {
            shouldShowRecommendationSectionSuccess()
            shouldShowRecommendationShareFriend(isActivated = false)
        }
    }

    @Test
    fun check_recommendation_section_failed() {
        repositoryStub.setRecommendationResponse(RecommendationState.RECOMMENDATION_FRIEND_FAILED)
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot { } assert {
            shouldShowRecommendationSectionFailed()
        }
    }

    @Test
    fun check_consent_withdrawal_section_success() {
        repositoryStub.setConsentWithdrawalResponse(ConsentWithdrawalState.CONSENT_WITHDRAWAL_SUCCESS)
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot {

        } assert {
            shouldShowConsentWithdrawalSuccess()
        }
    }

    @Test
    fun check_consent_withdrawal_section_failed() {
        repositoryStub.setConsentWithdrawalResponse(ConsentWithdrawalState.CONSENT_WITHDRAWAL_FAILED)
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot {

        } assert {
            shouldShowConsentWithdrawalFailed()
        }
    }

    @Test
    fun check_privacy_policy_section() {
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot {
            scrollToBottom()
        } assert {
            shouldShowPrivacyPolicy()
        }
    }

    @Test
    fun check_faq_section() {
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot {
            scrollToBottom()
        } assert {
            shouldShowFaq()
        }
    }

    @Test
    fun check_tokopedia_care_section() {
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot {
            scrollToFooterImage()
        } assert {
            shouldShowTokopediaCare()
        }
    }

    @Test
    fun check_image_footer_section() {
        activityTestRule.launchActivity(Intent())

        privacyCenterRobot {
            scrollToFooterImage()
        } assert {
            shouldShowFooterImage()
        }
    }
}
