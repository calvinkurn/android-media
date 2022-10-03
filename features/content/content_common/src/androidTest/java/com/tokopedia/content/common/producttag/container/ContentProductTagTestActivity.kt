package com.tokopedia.content.common.producttag.container

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.producttag.di.ContentCreationProductTagTestModule
import com.tokopedia.content.common.producttag.di.ContentProductTagTestInjector
import com.tokopedia.content.common.producttag.di.ContentProductTagTestModule
import com.tokopedia.content.common.producttag.di.DaggerContentProductTagTestComponent
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.test.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
class ContentProductTagTestActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_product_tag_test)

        supportFragmentManager.beginTransaction()
            .replace(
                findViewById<FragmentContainerView>(R.id.fragment_container).id,
                ProductTagParentFragment.getFragment(
                    supportFragmentManager,
                    classLoader,
                    getArgumentBuilder()
                ),
                ProductTagParentFragment.TAG,
            )
            .commit()
    }

    private fun getArgumentBuilder(): ContentProductTagArgument.Builder {
        val argument = ContentProductTagArgument.mapFromString(intent.getStringExtra(EXTRA_ARGUMENT).orEmpty())

        return ContentProductTagArgument.Builder()
            .setShopBadge(argument.shopBadge)
            .setAuthorId(argument.authorId)
            .setAuthorType(argument.authorType)
            .setProductTagSource(argument.productTagSource)
            .setMultipleSelectionProduct(argument.isMultipleSelectionProduct, argument.maxSelectedProduct)
            .setFullPageAutocomplete(argument.isFullPageAutocomplete, argument.appLinkAfterAutocomplete)
            .setBackButton(argument.backButton)
            .setIsShowActionBarDivider(argument.isShowActionBarDivider)
            .setIsAutoHandleBackPressed(argument.isAutoHandleBackPressed)
    }

    private fun inject() {
        ContentProductTagTestInjector.get()?.inject(this)
    }

    companion object {
        const val EXTRA_ARGUMENT = "EXTRA_ARGUMENT"
    }
}