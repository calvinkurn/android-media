package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.view.adapter.ProductViewPagerAdapter
import kotlinx.android.synthetic.main.activity_full_desc_tab.*

class ProductFullDescriptionTabActivity : BaseSimpleActivity() {


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
    }
}