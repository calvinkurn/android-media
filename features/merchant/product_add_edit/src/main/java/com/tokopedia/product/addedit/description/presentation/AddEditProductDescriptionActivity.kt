package com.tokopedia.product.addedit.description.presentation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class AddEditProductDescriptionActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = AddEditProductDescriptionFragment()

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductDescriptionActivity::class.java)
    }

}
