package com.tokopedia.product.addedit.description.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment

class AddEditProductDescriptionActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = AddEditProductDescriptionFragment()

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductDescriptionActivity::class.java)
    }

}
