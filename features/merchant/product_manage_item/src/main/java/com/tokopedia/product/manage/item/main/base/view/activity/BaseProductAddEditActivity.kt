package com.tokopedia.product.manage.item.main.base.view.activity

import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.app.TkpdCoreRouter
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.utils.ProductEditModuleRouter

abstract class BaseProductAddEditActivity : BaseSimpleActivity(), HasComponent<ProductComponent> {

    override fun getComponent() = (application as ProductEditModuleRouter).productComponent

    protected abstract fun getCancelMessageRes(): Int

    protected abstract fun needDeleteCacheOnBack(): Boolean

    override fun onBackPressed() {
        if (showDialogSaveDraftOnBack()) {
            val alertDialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(getCancelMessageRes()))
                    .setPositiveButton(getString(R.string.label_exit)) { dialogInterface, i ->
                        deleteNotUsedTkpdCacheImage()
                        backPressedHandleTaskRoot()
                    }.setNegativeButton(getString(R.string.label_cancel)) { arg0, arg1 ->
                        // no op, just dismiss
                    }
            alertDialogBuilder.setNeutralButton(getString(R.string.product_draft_save_as_draft), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val doSave = saveProductToDraft()
                    UnifyTracking.eventClickAddProduct(this@BaseProductAddEditActivity, AppEventTracking.Category.ADD_PRODUCT,
                            AppEventTracking.EventLabel.SAVE_DRAFT)
                    if (!doSave) {
                        backPressedHandleTaskRoot()
                    }
                }
            })
            val dialog = alertDialogBuilder.create()
            dialog.show()
        } else {
            backPressedHandleTaskRoot()
        }
    }

    private fun showDialogSaveDraftOnBack(): Boolean {
        val fragment = getBaseProductAddFragment()
        return fragment != null && fragment.showDialogSaveDraftOnBack()
    }

    private fun saveProductToDraft(): Boolean {
        // save newly added product ToDraft
        val fragment = getFragment()
        if (fragment != null && fragment is BaseProductAddEditFragment<*, *>) {
            fragment.saveDraft(false)
            return true
        }
        return false
    }

    fun getBaseProductAddFragment(): BaseProductAddEditFragment<*, *>? {
        val fragment = getFragment()
        return fragment as? BaseProductAddEditFragment<*, *>
    }

    private fun backPressedHandleTaskRoot() {
        if (isTaskRoot) {
            val homeIntent = (application as TkpdCoreRouter).getHomeIntent(this)
            startActivity(homeIntent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun deleteNotUsedTkpdCacheImage() {
        if (needDeleteCacheOnBack()) {
            val fragment = getBaseProductAddFragment()
            fragment?.deleteNotUsedTkpdCacheImage()
        }
    }
}
