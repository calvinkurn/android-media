package com.tokopedia.product.info.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.description.DescriptionData
import com.tokopedia.product.info.view.fragment.ProductFullDescriptionFragment

class ProductFullDescriptionActivity : BaseSimpleActivity(){

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
        intent?.run {
            descriptionData = getParcelableExtra(PARAM_DESCRIPTION_DATA)
        }
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.design.R.drawable.ic_close_default))
    }
}