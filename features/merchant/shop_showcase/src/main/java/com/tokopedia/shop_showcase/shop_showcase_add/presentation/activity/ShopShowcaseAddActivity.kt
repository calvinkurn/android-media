package com.tokopedia.shop_showcase.shop_showcase_add.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ShopShowcaseEditParam
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment.ShopShowcaseAddFragment

/**
 * @author by Rafli Syam on 2020-03-04
 *
 * ApplinkConsInternalMerchant.MERCHANT_SHOP_SHOWCASE_ADD
 */

class ShopShowcaseAddActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, isActionEdit: Boolean, showcaseId: String, showcaseName: String): Intent {
            val intent = Intent(context, ShopShowcaseAddActivity::class.java)
            intent.putExtra(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT, isActionEdit)
            intent.putExtra(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID, showcaseId)
            intent.putExtra(ShopShowcaseEditParam.EXTRA_SHOWCASE_NAME, showcaseName)
            return intent
        }
        const val DEFAULT_SHOWCASE_ID = "0"
    }

    private var isActionEdit: Boolean = false
    private var showcaseId: String? = DEFAULT_SHOWCASE_ID
    private var showcaseName: String? = ""

    override fun getNewFragment(): Fragment? {
        intent?.extras?.let {
            isActionEdit = it.getBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT)
            showcaseId = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID)
            showcaseName = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_NAME)
        }
        return ShopShowcaseAddFragment.createInstance(isActionEdit, showcaseId, showcaseName)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_showcase_add
    }

    override fun setupStatusBar(){
        val window: Window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onBackPressed() {
        if(isActionEdit) {
            onBackPressedConfirm()
        } else {
            finish()
        }
    }

    private fun onBackPressedConfirm() {
        val confirmDialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        confirmDialog.apply {
            setTitle(getString(R.string.text_exit_confirm_dialog_title))
            setDescription(getString(R.string.text_exit_confirm_dialog_description))
            setPrimaryCTAText(getString(R.string.text_cancel_button))
            setPrimaryCTAClickListener {
                this.dismiss()
            }
            setSecondaryCTAText(getString(R.string.text_exit_button))
            setSecondaryCTAClickListener {
                this.dismiss()
                finish()
            }
            show()
        }
    }
}