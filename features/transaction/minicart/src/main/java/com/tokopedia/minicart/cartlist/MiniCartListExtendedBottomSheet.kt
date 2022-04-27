package com.tokopedia.minicart.cartlist

import android.app.Activity
import android.content.Intent
import android.view.View
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster

class MiniCartListExtendedBottomSheet(private var onUpdateProduct: (oldBundleId: String) -> Unit): BottomSheetUnify() {

    companion object {
        private const val EXTRA_OLD_BUNDLE_ID = "old_bundle_id"
        private const val EXTRA_NEW_BUNDLE_ID = "new_bundle_id"
        private const val EXTRA_IS_VARIANT_CHANGED = "is_variant_changed"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MiniCartListBottomSheet.REQUEST_CODE_BUNDLING_SELECTION && resultCode == Activity.RESULT_OK) {
            val oldBundleId = data?.getStringExtra(EXTRA_OLD_BUNDLE_ID).orEmpty()
            val newBundleId = data?.getStringExtra(EXTRA_NEW_BUNDLE_ID).orEmpty()
            val isChangeVariant = data?.getBooleanExtra(EXTRA_IS_VARIANT_CHANGED, false).orFalse()
            if ((oldBundleId.isNotBlank() && newBundleId.isNotBlank() && oldBundleId != newBundleId) || isChangeVariant) {
                showToaster(
                    text = context?.getString(R.string.mini_cart_bundling_package_has_been_changed_desc).orEmpty(),
                    actionText = context?.getString(R.string.mini_cart_bundling_package_has_been_changed_action_text).orEmpty()
                )
                onUpdateProduct.invoke(oldBundleId)
            }
        }
    }

    private fun showToaster(text: String, actionText: String, clickListener: View.OnClickListener = View.OnClickListener {}) {
        Toaster.toasterCustomBottomHeight = context?.resources?.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_72).orZero()
        dialog?.window?.decorView?.apply {
            Toaster.build(
                view = this,
                text = text,
                duration = Toaster.TYPE_NORMAL,
                actionText = actionText,
                clickListener = clickListener
            ).show()
        }
    }
}