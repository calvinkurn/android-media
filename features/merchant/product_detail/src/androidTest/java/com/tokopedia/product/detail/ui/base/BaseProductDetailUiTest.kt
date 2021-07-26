package com.tokopedia.product.detail.ui.base

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.detail.ui.di.DaggerProductDetailTestComponent
import com.tokopedia.product.detail.ui.di.ProductDetailTestComponent
import com.tokopedia.user.session.UserSessionInterface
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Created by Yehezkiel on 08/04/21
 */
abstract class BaseProductDetailUiTest {

    @get:Rule
    var activityCommonRule: ActivityTestRule<ProductDetailActivityMock> = IntentsTestRule(ProductDetailActivityMock::class.java,
            false,
            false)

    @Inject
    lateinit var userSessionMock: UserSessionInterface

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    protected var productDetailTestComponent: ProductDetailTestComponent? = null

    abstract fun before()

    @Before
    fun doBeforeRun() {
        before()
        productDetailTestComponent = DaggerProductDetailTestComponent
                .builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()

        productDetailTestComponent?.inject(this)

        val intent = ProductDetailActivityMock.createIntent(context, "1060957410")
        activityCommonRule.launchActivity(intent)
    }

}