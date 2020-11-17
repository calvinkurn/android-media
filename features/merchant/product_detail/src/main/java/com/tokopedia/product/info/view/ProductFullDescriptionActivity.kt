package com.tokopedia.product.info.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.info.model.description.DescriptionData
import com.tokopedia.product.info.view.fragment.ProductFullDescriptionFragment
import javax.inject.Inject

class ProductFullDescriptionActivity : BaseSimpleActivity(), HasComponent<ProductDetailComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var descriptionData: DescriptionData? = null

    companion object {
        private const val PARAM_DESCRIPTION_DATA = "description_data"

        fun createIntent(context: Context, descriptionData: DescriptionData): Intent {
            return Intent(context, ProductFullDescriptionActivity::class.java).apply {
                putExtra(PARAM_DESCRIPTION_DATA, descriptionData)
            }
        }
    }

    override fun getNewFragment(): Fragment? = ProductFullDescriptionFragment.createInstance(descriptionData
            ?: DescriptionData())

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        intent?.run {
            descriptionData = getParcelableExtra(PARAM_DESCRIPTION_DATA)
        }
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default))
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
}