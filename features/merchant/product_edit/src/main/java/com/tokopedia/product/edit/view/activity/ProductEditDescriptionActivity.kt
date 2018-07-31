package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.ProductEditDescriptionFragment

class ProductEditDescriptionActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context : Context?, description : String) : Intent{
            val intent = Intent(context, ProductEditDescriptionActivity::class.java)
            val bundle  = Bundle()
            bundle.putString("description", description)
            intent.putExtras(bundle)
            return  intent
        }
    }

    override fun getNewFragment(): Fragment{
        return ProductEditDescriptionFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
