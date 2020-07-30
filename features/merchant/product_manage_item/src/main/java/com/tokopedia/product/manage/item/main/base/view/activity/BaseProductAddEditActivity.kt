package com.tokopedia.product.manage.item.main.base.view.activity

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.nishikino.model.EventTracking
import com.tokopedia.core.app.TkpdCoreRouter
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.main.base.view.listener.ListenerOnAddEditFragmentCreated
import com.tokopedia.product.manage.item.utils.ProductEditItemComponentInstance
import com.tokopedia.product.manage.item.utils.constant.AddProductTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

abstract class BaseProductAddEditActivity : BaseSimpleActivity(), HasComponent<ProductComponent>, ListenerOnAddEditFragmentCreated {

    protected var userSessionInterface: UserSessionInterface? = null

    override fun getComponent() = ProductEditItemComponentInstance.getComponent(application)

    protected abstract fun getCancelMessageRes(): Int

    protected abstract fun needDeleteCacheOnBack(): Boolean

    protected abstract var addEditPageType: AddEditPageType

    override fun setUserSession(userSessionInterface: UserSessionInterface) {
        this.userSessionInterface = userSessionInterface
    }

    override fun onBackPressed() {
        if (showDialogSaveDraftOnBack()) {
            val alertDialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(getCancelMessageRes()))
                    .setPositiveButton(getString(R.string.label_exit)) { dialogInterface, i ->
                        deleteNotUsedTkpdCacheImage()
                        backPressedHandleTaskRoot()
                        if(addEditPageType == AddEditPageType.ADD){
                            userSessionInterface?.let {
                                eventCancelAddProduct(it.shopId)
                            }
                        }
                    }.setNegativeButton(getString(R.string.label_cancel)) { arg0, arg1 ->
                        // no op, just dismiss
                    }
            alertDialogBuilder.setNeutralButton(getString(R.string.product_draft_save_as_draft), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val doSave = saveProductToDraft()
                    eventClickAddProduct(AppEventTracking.Category.ADD_PRODUCT,
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

    fun eventClickAddProduct(eventCategory: String, eventLabel: String) {
        TrackApp.getInstance()!!.gtm.sendGeneralEvent(EventTracking(
            AppEventTracking.Event.CLICK_ADD_PRODUCT,
            eventCategory,
            AppEventTracking.Action.CLICK,
            eventLabel
        ).event)
    }

    private fun eventCancelAddProduct(shopId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                AddProductTrackingConstant.Event.CLICK_ADD_PRODUCT,
                AddProductTrackingConstant.Category.ADD_PRODUCT_PAGE,
                AddProductTrackingConstant.Action.CLICK_BATAL,
                ""
        )
        mapEvent[AddProductTrackingConstant.Key.SHOP_ID] = shopId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
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
