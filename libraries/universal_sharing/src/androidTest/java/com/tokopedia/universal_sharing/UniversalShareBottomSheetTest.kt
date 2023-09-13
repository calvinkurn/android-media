package com.tokopedia.universal_sharing

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common.stub.FakeActivityComponentFactory
import com.tokopedia.common.stub.FakeGraphqlRepository
import com.tokopedia.common.view.UniversalShareTestActivity
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class UniversalShareBottomSheetTest {

    private lateinit var fakeGraphqlRepository: FakeGraphqlRepository
    private lateinit var baseAppComponent: BaseAppComponent
    private val testComponent = FakeActivityComponentFactory()
    private lateinit var timber: MockTimber

    @get:Rule
    var activityTestRule = IntentsTestRule(
        UniversalShareTestActivity::class.java,
        false,
        false
    )

    @Before
    fun setup() {
        timber = MockTimber()
        Timber.plant(timber)
        baseAppComponent = stubAppGraphqlRepo()

        //we will use this to manipulate on extract branch link
        ActivityComponentFactory.instance = testComponent
        fakeGraphqlRepository = baseAppComponent.graphqlRepository() as FakeGraphqlRepository
    }

    @Test
    fun sharingPDP_NonLoginUser_ImageOptionsFromMedia() {
        testComponent.isLogin = false

        universalSharingRobot {
            runTest(UniversalShareModel.getDefaultPDPBottomSheet())
            atScrollImagesOptions(3)
            atClickOneImageOptions(2)
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
        testComponent.isLogin = true
        fakeGraphqlRepository.mockParam = FakeGraphqlRepository.MockParam.NOT_REGISTERED

        universalSharingRobot {
            runTest(UniversalShareModel.getDefaultPDPBottomSheet().apply {
                enableAffiliateCommission(anyInput())
            })
            atScrollImagesOptions(3)
            atClickOneImageOptions(2)
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
        testComponent.isLogin = true
        fakeGraphqlRepository.mockParam = FakeGraphqlRepository.MockParam.ELIGIBLE_COMMISSION

        universalSharingRobot {
            runTest(UniversalShareModel.getPDPBottomSheetWith2ImagesOption().apply {
                enableAffiliateCommission(anyInput())
            })
            atScrollImagesOptions(1)
            atClickOneImageOptions(1)
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
        testComponent.isLogin = true
        fakeGraphqlRepository.mockParam = FakeGraphqlRepository.MockParam.NOT_ELIGIBLE_COMMISSION

        universalSharingRobot {
            runTest(UniversalShareModel.getPDPBottomSheetWith2ImagesOption().apply {
                enableAffiliateCommission(anyInput())
            })
            atScrollImagesOptions(1)
            atClickOneImageOptions(1)
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
        testComponent.isLogin = true
        var onTitleChipPropertiesSelected = ""

        universalSharingRobot {
            runTest(UniversalShareModel.getShopBottomSheet().apply {
                onChipChangedListener {
                    onTitleChipPropertiesSelected = it.title
                }
            })
            atScrollChips(4)
            atClickChips(2)
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
        activityTestRule.launchActivity(Intent())
        activityTestRule.activity.getShareFragment().showUniversalBottomSheet(bottomSheet)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun anyInput(): AffiliateInput = AffiliateInput()
}
