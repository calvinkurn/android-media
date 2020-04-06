package com.tokopedia.product.addedit.detail.presentation.activity

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailModule
import com.tokopedia.product.addedit.detail.di.DaggerAddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment

class AddEditProductDetailActivity : BaseSimpleActivity(), HasComponent<AddEditProductDetailComponent> {

    override fun getNewFragment(): Fragment? {
        val initialSelectedImagePathList = intent?.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
        return AddEditProductDetailFragment.createInstance(initialSelectedImagePathList)
    }

    override fun getComponent(): AddEditProductDetailComponent {
        return DaggerAddEditProductDetailComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(application))
                .addEditProductDetailModule(AddEditProductDetailModule())
                .build()
    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        val dialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setMessage(R.string.message_alert_dialog_before_exit)
                .setNegativeButton(R.string.label_negative_button_on_alert_dialog) { _, _ -> }
                .setPositiveButton(R.string.label_positive_button_on_alert_dialog) { _, _ ->
                    backHome()
                }
                .setNeutralButton(R.string.label_neutral_button_on_alert_dialog) { _, _ ->
                    saveProductToDraft()
                    backHome()
                }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun backHome() {
        val intentHome = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.SELLER_HOME)
        startActivity(intentHome)
        finish()
    }

    private fun saveProductToDraft() {
        val f = fragment
        if (f != null && f is AddEditProductDetailFragment) {
            f.insertProductDraft(false)
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductDetailFragment) {
            f.onBackPressed()
        }
    }
}