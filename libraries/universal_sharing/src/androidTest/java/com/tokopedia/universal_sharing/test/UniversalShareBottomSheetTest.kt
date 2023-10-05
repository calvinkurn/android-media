package com.tokopedia.universal_sharing.test

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.universal_sharing.stub.di.FakeActivityComponentFactory
import com.tokopedia.universal_sharing.stub.data.repository.FakeGraphqlRepository
import com.tokopedia.universal_sharing.stub.view.UniversalShareTestActivity
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.stub.common.ActivityScenarioTestRule
import com.tokopedia.universal_sharing.stub.common.MockTimber
import com.tokopedia.universal_sharing.stub.common.UserSessionStub
import com.tokopedia.universal_sharing.test.robot.universalSharingRobot
import com.tokopedia.universal_sharing.test.robot.validate
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber
import javax.inject.Inject

class UniversalShareBottomSheetTest {

    @get:Rule
    var activityTestRule = ActivityScenarioTestRule<UniversalShareTestActivity>()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var userSession: UserSessionStub

    @ApplicationContext
    @Inject
    lateinit var fakeGraphqlRepository: FakeGraphqlRepository

    private lateinit var timber: MockTimber

    @Before
    fun beforeTest() {
        Intents.init()
        setupTimber()
        setupDaggerComponent()
        setupEarlyEnv()
    }

    @After
    fun afterTest() {
        Intents.release()
    }

    private fun setupTimber() {
        timber = MockTimber()
        Timber.plant(timber)
    }

    private fun setupDaggerComponent() {
        val fakeComponent = FakeActivityComponentFactory()
        ActivityComponentFactory.instance = fakeComponent
        fakeComponent.universalSharingComponent.inject(this)
    }

    private fun setupEarlyEnv() {
        userSession.setUserLoginStatus(true)
    }

    @Test
    fun sharingPDP_NonLoginUser_ImageOptionsFromMedia() {
        userSession.setUserLoginStatus(false)
        universalSharingRobot {
            runTest(UniversalShareModel.getDefaultPDPBottomSheet())
            scrollHorizontalIOnImagesOptions(3)
            clickOnSpecificImageOption(2)
        } validate {
            shouldShowTitleBottomSheet()
            shouldShowTitleHeadingImageOptions()
            shouldShowImagesOptions(2)
            shouldShowThumbnailShare()
            shouldShowDefaultShareMediaList()
        }
    }

    @Test
    fun sharingPDP_LoginUser_6ImagesOptionsFromMedia_UserNotRegisterOnAffiliate_NotEligibleAffiliate_EligibleCommission() {
        fakeGraphqlRepository.mockParam = FakeGraphqlRepository.MockParam.NOT_REGISTERED

        universalSharingRobot {
            runTest(UniversalShareModel.getDefaultPDPBottomSheet().apply {
                enableAffiliateCommission(anyInput())
            })
            scrollHorizontalIOnImagesOptions(3)
            clickOnSpecificImageOption(2)
        } validate {
            shouldShowTitleBottomSheet()
            shouldShowTitleHeadingImageOptions()
            shouldShowImagesOptions(2)
            shouldShowThumbnailShare()
            shouldShowRegisterAffiliateTicker()
            shouldShowDefaultShareMediaList()
        }
    }

    @Test
    fun sharingPDP_LoginUser_2ImagesOptionsFromMedia_UserRegisteredOnAffiliate_EligibleAffiliateAndCommission() {
        fakeGraphqlRepository.mockParam = FakeGraphqlRepository.MockParam.ELIGIBLE_COMMISSION

        universalSharingRobot {
            runTest(UniversalShareModel.getPDPBottomSheetWith2ImagesOption().apply {
                enableAffiliateCommission(anyInput())
            })
            scrollHorizontalIOnImagesOptions(1)
            clickOnSpecificImageOption(1)
        } validate {
            shouldShowTitleBottomSheet()
            shouldShowTitleHeadingImageOptions()
            shouldShowImagesOptions(1)
            shouldShowThumbnailShare()
            shouldShowCommissionAffiliate()
            shouldShowDefaultShareMediaList()
        }
    }

    @Test
    fun sharingPDP_LoginUser_2ImagesOptionsFromMedia_UserRegisteredOnAffiliate_NotEligibleAffiliateAndCommission() {
        fakeGraphqlRepository.mockParam = FakeGraphqlRepository.MockParam.NOT_ELIGIBLE_COMMISSION

        universalSharingRobot {
            runTest(UniversalShareModel.getPDPBottomSheetWith2ImagesOption().apply {
                enableAffiliateCommission(anyInput())
            })
            scrollHorizontalIOnImagesOptions(1)
            clickOnSpecificImageOption(1)
        } validate {
            shouldShowTitleBottomSheet()
            shouldShowTitleHeadingImageOptions()
            shouldShowImagesOptions(1)
            shouldShowThumbnailShare()
            shouldHideCommissionAffiliate()
            shouldShowDefaultShareMediaList()
        }
    }

    @Test
    fun sharingShop_LoginUser_ShowShopChips() {
        var onTitleChipPropertiesSelected = ""

        universalSharingRobot {
            runTest(UniversalShareModel.getShopBottomSheet().apply {
                onChipChangedListener {
                    onTitleChipPropertiesSelected = it.title
                }
            })
            scrollHorizontalOnChips(4)
            clickOnSpecificChip(2)
        } validate {
            shouldShowTitleBottomSheet()
            shouldShowTitleChipOptions()
            shouldShowTabChips()
            shouldShowSelectionTabChips(2)
            hasTitleChipSharing(onTitleChipPropertiesSelected)
            shouldShowThumbnailShare()
            shouldShowShareMediaListIfImageOnlySharingOptions()
        }
    }

    private fun runTest(bottomSheet: UniversalShareBottomSheet) {
        activityTestRule.launchActivity(Intent(context, UniversalShareTestActivity::class.java))
        (activityTestRule.activity as UniversalShareTestActivity)
            .getShareFragment()
            .showUniversalBottomSheet(bottomSheet)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun anyInput(): AffiliateInput = AffiliateInput()
}
