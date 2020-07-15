package com.tokopedia.product.addedit.description.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment

class AddEditProductDescriptionActivity : BaseSimpleActivity() {

    companion object {
        fun createInstance(context: Context?, cacheManagerId: String?): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
    }

    override fun getNewFragment(): Fragment {
        val cacheManagerId = intent?.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID) ?: ""
        return AddEditProductDescriptionFragment.createInstance(cacheManagerId)
    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        sendDataBack()
    }

    private fun sendDataBack() {
        val f = fragment
        if (f != null && f is AddEditProductDescriptionFragment) {
            f.sendDataBack()
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductDescriptionFragment) {
            f.onBackPressed()
        }
    }
}