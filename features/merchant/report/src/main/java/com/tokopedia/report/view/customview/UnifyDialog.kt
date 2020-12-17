package com.tokopedia.report.view.customview

import android.app.Activity
import android.os.Build
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.report.R

class UnifyDialog(private val activity: Activity,
                  @ActionType private var actionType: Int,
                  @HeaderType private val headerType: Int) {

    var alertDialog: AlertDialog? = null
        private set

    private var btnPrimary: Button? = null
    private var btnSecondary: Button? = null
    private var btnSecondaryLong: Button? = null
    private var dialogImageHeader: CardView? = null
    private var imageHeader: ImageView? = null
    private var dialogContent: ConstraintLayout? = null
    private var dialogTitle: TextView? = null
    private var dialogDescription: TextView? = null

    fun layoutResId(): Int = R.layout.report_layout_unify_dialog

    init {
        val dialogView = activity.layoutInflater.inflate(layoutResId(), null)
        initView(dialogView)

        alertDialog = AlertDialog.Builder(activity).create()
        alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog?.setView(dialogView)

        initListener(alertDialog)
    }

    fun initView(dialogView: View) {
        btnPrimary = dialogView.findViewById(R.id.dialog_btn_primary)
        btnSecondary = dialogView.findViewById(R.id.dialog_btn_secondary)
        btnSecondaryLong = dialogView.findViewById(R.id.dialog_btn_secondary_long)
        dialogContent = dialogView.findViewById(R.id.dialog_content)
        dialogImageHeader = dialogView.findViewById(R.id.dialog_card_icon)
        dialogTitle = dialogView.findViewById(R.id.dialog_title)
        dialogDescription = dialogView.findViewById(R.id.dialog_description)
        imageHeader = dialogView.findViewById(R.id.dialog_icon)
        when(actionType){
            SINGLE_ACTION -> {
                btnSecondary?.visibility = View.GONE
                btnSecondaryLong?.visibility = View.GONE
            }
            HORIZONTAL_ACTION -> {
                btnSecondary?.visibility = View.VISIBLE
                btnSecondaryLong?.visibility = View.GONE
            }
            VERTICAL_ACTION -> {
                btnSecondary?.visibility = View.GONE
                btnSecondaryLong?.visibility = View.VISIBLE
            }
        }

        dialogImageHeader?.visibility = if (headerType == NO_HEADER) View.GONE else View.VISIBLE
        setupMarginTitleByHeader(headerType)
    }

    private fun setupMarginTitleByHeader(headerType: Int) {
        dialogContent?.let {
            val layoutParams = it.layoutParams as ConstraintLayout.LayoutParams
            val imageLp = imageHeader?.layoutParams
            val topMargin: Int = activity.resources.getDimensionPixelSize(when(headerType){
                ICON_HEADER -> {
                    dialogImageHeader?.radius = activity.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8).toFloat()
                    imageLp?.width = activity.resources.getDimensionPixelSize(R.dimen.dp_80)
                    imageLp?.height = activity.resources.getDimensionPixelSize(R.dimen.dp_80)
                    com.tokopedia.unifyprinciples.R.dimen.unify_space_16
                }
                IMAGE_HEADER -> {
                    dialogImageHeader?.radius = activity.resources.getDimensionPixelSize(R.dimen.dp_10).toFloat()
                    imageLp?.width = activity.resources.getDimensionPixelSize(R.dimen.dp_180)
                    imageLp?.height = activity.resources.getDimensionPixelSize(R.dimen.dp_180)
                    com.tokopedia.unifyprinciples.R.dimen.unify_space_0
                }
                else -> com.tokopedia.unifyprinciples.R.dimen.unify_space_24
            })
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMargins(layoutParams.marginStart, topMargin,
                        layoutParams.marginEnd, layoutParams.bottomMargin)
            } else {
                layoutParams.setMargins(layoutParams.leftMargin, topMargin,
                        layoutParams.rightMargin, layoutParams.bottomMargin)
            }
            it.layoutParams = layoutParams
            imageHeader?.layoutParams = imageLp
        }
    }

    fun initListener(dialog: AlertDialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    fun setTitle(title: CharSequence){
        dialogTitle?.text = title
    }

    fun setImageHeader(imgRes: Int){
        imageHeader?.setImageDrawable(AppCompatResources.getDrawable(activity, imgRes))
    }

    fun setDescription(description: CharSequence){
        dialogDescription?.text = description
    }

    fun setOk(cta: CharSequence){
        btnPrimary?.text = cta
    }

    fun setOkOnClickListner(listener: View.OnClickListener){
        btnPrimary?.setOnClickListener(listener)
    }

    fun setSecondary(cta: CharSequence){
        if (actionType != SINGLE_ACTION) {
            btnSecondary?.text = cta
            btnSecondaryLong?.text = cta
        }
    }

    fun setSecondaryOnClickListner(listener: View.OnClickListener){
        if (actionType != SINGLE_ACTION) {
            btnSecondary?.setOnClickListener(listener)
            btnSecondaryLong?.setOnClickListener(listener)
        }
    }

    fun show() {
        alertDialog?.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }

    fun setCancelable(cancelable: Boolean) {
        alertDialog?.setCancelable(cancelable)
    }

    companion object{
        const val SINGLE_ACTION = 1
        const val HORIZONTAL_ACTION = 2
        const val VERTICAL_ACTION = 3

        const val NO_HEADER = 1
        const val ICON_HEADER = 2
        const val IMAGE_HEADER = 3
    }

    @IntDef(SINGLE_ACTION, HORIZONTAL_ACTION, VERTICAL_ACTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ActionType

    @IntDef(NO_HEADER, ICON_HEADER, IMAGE_HEADER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class HeaderType
}