package com.tokopedia.universal_sharing.test.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseProductModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseShopModel
import com.tokopedia.universal_sharing.stub.common.ActivityScenarioTestRule
import com.tokopedia.universal_sharing.stub.common.NetworkUtilStub
import com.tokopedia.universal_sharing.stub.data.response.GqlResponseStub
import com.tokopedia.universal_sharing.stub.di.FakeActivityComponentFactory
import com.tokopedia.universal_sharing.util.NetworkUtil
import com.tokopedia.universal_sharing.view.activity.UniversalSharingPostPurchaseSharingActivity
import com.tokopedia.user.session.UserSessionInterface
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseUniversalSharingPostPurchaseBottomSheetTest {
    @get:Rule
    var activityScenarioRule =
        ActivityScenarioTestRule<UniversalSharingPostPurchaseSharingActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var networkUtil: NetworkUtil

    @Before
    open fun beforeTest() {
        Intents.init()
        GqlResponseStub.reset()
        setupDaggerComponent()
        (networkUtil as NetworkUtilStub).isConnectedToNetwork = true
    }

    private fun setupDaggerComponent() {
        val fakeComponent = FakeActivityComponentFactory()
        ActivityComponentFactory.instance = fakeComponent
        fakeComponent.universalSharingComponent.inject(this)
    }

    @After
    open fun afterTest() {
        Intents.release()
    }

    protected fun launchActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent(context, UniversalSharingPostPurchaseSharingActivity::class.java)
        val model = UniversalSharingPostPurchaseModel(
            shopList = listOf(
                UniversalSharingPostPurchaseShopModel(
                    shopName = "Samsung Official Store",
                    shopType = "official_store",
                    productList = listOf(
                        UniversalSharingPostPurchaseProductModel(
                            orderId = "12341234",
                            productId = "123123123",
                            productName = "Samsung Galaxy A54 5G 8/256GB - Awesome Graphite",
                            productPrice = "1.000.000",
                            imageUrl = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png"
                        )
                    )
                )
            )
        )
        intent.putExtra(ApplinkConstInternalCommunication.PRODUCT_LIST_DATA, model)
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
    }
}
