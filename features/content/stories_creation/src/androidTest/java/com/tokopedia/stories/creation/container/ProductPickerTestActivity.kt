package com.tokopedia.stories.creation.container

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.ProductSetupFragment
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.stories.creation.di.product.DaggerProductPickerTestComponent
import com.tokopedia.stories.creation.di.product.ProductPickerTestModule
import com.tokopedia.stories.creation.provider.ProductPickerTestActivityProvider
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
class ProductPickerTestActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .add(ProductSetupFragment::class.java, null, null)
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is ProductSetupFragment -> {
                fragment.setDataSource(object : ProductSetupFragment.DataSource {
                    override fun getProductSectionList(): List<ProductTagSectionUiModel> {
                        return ProductPickerTestActivityProvider.productTag
                    }

                    override fun isEligibleForPin(): Boolean = false

                    override fun getSelectedAccount(): ContentAccountUiModel {
                        return ProductPickerTestActivityProvider.selectedAccount
                    }

                    override fun creationId(): String {
                        return "123"
                    }

                    override fun maxProduct(): Int {
                        return ProductPickerTestActivityProvider.maxProduct
                    }

                    override fun isNumerationShown(): Boolean = false

                    override fun fetchCommissionProduct(): Boolean = false
                })

                fragment.setListener(object : ProductSetupFragment.Listener {
                    override fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>) {

                    }
                })
            }
        }
    }

    private fun inject() {
        DaggerProductPickerTestComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .productPickerTestModule(
                ProductPickerTestModule(
                    mockRepository = ProductPickerTestActivityProvider.mockRepository,
                    mockCommonRepository = ProductPickerTestActivityProvider.mockCommonRepository,
                )
            )
            .build()
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }
}
