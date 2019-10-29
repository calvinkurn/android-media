package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.ProductViewPagerAdapter
import com.tokopedia.product.detail.view.fragment.ProductFullDescriptionFragment
import com.tokopedia.product.detail.view.fragment.ProductSpecificationFragment
import kotlinx.android.synthetic.main.activity_full_desc_tab.*
import javax.inject.Inject

class ProductFullDescriptionTabActivity : BaseSimpleActivity(), HasComponent<ProductDetailComponent> {

    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    private var titleList = listOf<String>()
    lateinit var descriptionData: DescriptionData
    lateinit var listOfSpecification: ArrayList<Specification>
    private val productViewPagerAdapter: ProductViewPagerAdapter by lazy {
        ProductViewPagerAdapter(supportFragmentManager, titleList, descriptionData, listOfSpecification)
    }

    companion object {
        private const val PARAM_DESCRIPTION_DATA = "description_data"
        private const val PARAM_CATALOG_DATA = "catalog_data"

        fun createIntent(context: Context, descriptionData: DescriptionData, listOfCatalog: ArrayList<Specification>): Intent {
            return Intent(context, ProductFullDescriptionTabActivity::class.java).apply {
                putExtra(PARAM_DESCRIPTION_DATA, descriptionData)
                putParcelableArrayListExtra(PARAM_CATALOG_DATA, listOfCatalog)
            }
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_full_desc_tab

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        intent?.run {
            descriptionData = getParcelableExtra(PARAM_DESCRIPTION_DATA)
            listOfSpecification = getParcelableArrayListExtra(PARAM_CATALOG_DATA) ?: arrayListOf()
        }

        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default))
        titleList = listOf(getString(R.string.label_desc), getString(R.string.label_spec))
        viewPagerDesc.adapter = productViewPagerAdapter

        tabLayoutDesc.run {
            visibility = if (listOfSpecification.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            setupWithViewPager(viewPagerDesc)
        }

        tabLayoutDesc.onTabSelected {
            val position = it.position
            when (productViewPagerAdapter.getItem(position)) {
                is ProductFullDescriptionFragment -> {
                    productDetailTracking.eventClickDescriptionTabOnProductDescription(
                            descriptionData.basicId
                    )
                }
                is ProductSpecificationFragment -> {
                    productDetailTracking.eventClickSpecificationTabOnProductDescription(
                            descriptionData.basicId
                    )
                }
                else -> {
                }
            }
        }
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
}