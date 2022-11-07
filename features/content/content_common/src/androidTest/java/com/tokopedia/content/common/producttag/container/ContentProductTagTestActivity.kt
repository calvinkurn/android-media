package com.tokopedia.content.common.producttag.container

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentFactory
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.di.ContentProductTagTestInjector
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.test.R
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

    @Inject
    lateinit var analytic: ContentProductTagAnalytic

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

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when(fragment) {
            is ProductTagParentFragment -> {
                fragment.setListener(object : ProductTagParentFragment.Listener {
                    override fun onCloseProductTag() {
                        closeProductTag()
                    }

                    override fun onFinishProductTag(products: List<SelectedProductUiModel>) {
                        findViewById<TextView>(R.id.tv_selected_product).text = products.toString()
                    }

                    override fun onMaxSelectedProductReached() {

                    }
                })

                fragment.setAnalytic(analytic)
            }
        }
    }

    override fun onBackPressed() {
        closeProductTag()
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

    private fun closeProductTag() {
        supportFragmentManager
            .beginTransaction()
            .remove(ProductTagParentFragment.getFragment(
                supportFragmentManager,
                classLoader,
                getArgumentBuilder()
            ))
            .commitNow()
    }

    companion object {
        const val EXTRA_ARGUMENT = "EXTRA_ARGUMENT"
    }
}