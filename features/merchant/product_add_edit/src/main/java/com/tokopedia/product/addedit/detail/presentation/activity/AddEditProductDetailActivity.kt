package com.tokopedia.product.addedit.detail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
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
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.label_title_on_dialog))
            setDescription(getString(R.string.label_description_on_dialog))
            setPrimaryCTAText(getString(R.string.label_cta_primary_button_on_dialog))
            setSecondaryCTAText(getString(R.string.label_cta_secondary_button_on_dialog))
            setSecondaryCTAClickListener {
                saveProductToDraft()
                moveToManageProduct()
                onCtaYesPressedHitTracking()
            }
            setPrimaryCTAClickListener {
                super.onBackPressed()
                onCtaNoPressedHitTracking()
            }
        }.show()
    }

    private fun moveToManageProduct() {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intent)
        finish()
    }

    private fun saveProductToDraft() {
        val f = fragment
        if (f != null && f is AddEditProductDetailFragment) {
            f.saveProductDraft(false)
        }
    }

    private fun onCtaYesPressedHitTracking() {
        val f = fragment
        if (f != null && f is AddEditProductDetailFragment) {
            f.onCtaYesPressed()
        }
    }

    private fun onCtaNoPressedHitTracking() {
        val f = fragment
        if (f != null && f is AddEditProductDetailFragment) {
            f.onCtaNoPressed()
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductDetailFragment) {
            f.onBackPressed()
        }
    }
}