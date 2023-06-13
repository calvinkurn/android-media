package com.tokopedia.loginfingerprint.view.dialog

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.loginfingerprint.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

object FingerprintDialogHelper {

    const val BIOMETRIC_OFFERING_MAIN_IMG = TokopediaImageUrl.BIOMETRIC_OFFERING_MAIN_IMG
    const val BIOMETRIC_OFFERING_SUCCESS_IMG = TokopediaImageUrl.BIOMETRIC_OFFERING_SUCCESS_IMG

    fun showFingerprintLockoutDialog(context: Context?, onPositiveButtonClick: () -> Unit? = {}, onDismiss: () -> Unit? = {}) {
        context?.run {
            val dialog = DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.error_fingerprint_invalid_title))
            dialog.setDescription(getString(R.string.error_fingerprint_lockout_description))
            dialog.setPrimaryCTAText(getString(R.string.error_fingerprint_ok))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                onPositiveButtonClick()
            }
            dialog.setOnDismissListener { onDismiss() }
            dialog.show()
        }
    }

    fun showNotRegisteredFingerprintDialog(context: Context?, onPositiveButtonClick: () -> Unit? = {}, onDismiss: () -> Unit? = {}) {
        context?.run {
            val dialog = DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.error_fingerprint_not_registered))
                setDescription(getString(R.string.error_fingerprint_not_registered_description))
                setPrimaryCTAText(getString(R.string.button_close_fingerprint))
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setPrimaryCTAClickListener {
                    dismiss()
                    onPositiveButtonClick()
                }
                setOnDismissListener { onDismiss() }
            }
            dialog.show()
        }
    }

    fun createBiometricOfferingDialog(activity: FragmentActivity, onPrimaryBtnClicked: () -> Unit?, onSecondaryBtnClicked: () -> Unit?, onCloseBtnClicked: () -> Unit?): BottomSheetUnify {
        return BottomSheetUnify().apply {

            val view = View.inflate(activity, R.layout.bottom_sheet_register_fingerprint_layout, null)
            val primaryBtn = view?.findViewById<UnifyButton>(R.id.bottom_sheet_reg_biom_primary_btn)
            val secondaryBtn = view?.findViewById<UnifyButton>(R.id.bottom_sheet_reg_biom_secondary_btn)

            val mainImgView = view?.findViewById<ImageUnify>(R.id.bottom_sheet_reg_biom_main_img)

            mainImgView?.run {
                Glide.with(activity)
                    .load(BIOMETRIC_OFFERING_MAIN_IMG)
                    .into(this)
            }

            primaryBtn?.setOnClickListener {
                onPrimaryBtnClicked()
            }
            secondaryBtn?.setOnClickListener {
                onSecondaryBtnClicked()
            }

            setCloseClickListener {
                onCloseBtnClicked()
            }
            setChild(view)
            activity.supportFragmentManager.run {
                show(this, "")
            }
        }
    }

    fun createBiometricOfferingSuccessDialog(activity: FragmentActivity, onPrimaryBtnClicked: () -> Unit?) {
        BottomSheetUnify().apply {
            val view = View.inflate(activity, R.layout.bottom_sheet_register_fingerprint_success, null)
            val primaryBtn = view?.findViewById<UnifyButton>(R.id.bottom_sheet_success_biom_primary_btn)

            val mainImgView = view?.findViewById<ImageUnify>(R.id.bottom_sheet_success_biom_main_img)

            mainImgView?.run {
                Glide.with(activity)
                    .load(BIOMETRIC_OFFERING_SUCCESS_IMG)
                    .into(this)
            }

            primaryBtn?.setOnClickListener {
                onPrimaryBtnClicked()
                dismiss()
            }
            setChild(view)
            activity.supportFragmentManager.run {
                show(this, "reg biom success bottomsheet")
            }
        }
    }

}