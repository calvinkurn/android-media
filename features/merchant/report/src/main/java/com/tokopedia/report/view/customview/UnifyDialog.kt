package com.tokopedia.report.view.customview

import android.app.Activity
import android.os.Build
import android.support.annotation.IntDef
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.CardView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.design.base.BaseDialog
import com.tokopedia.report.R

class UnifyDialog(context: Activity,
                  @ActionType private val actionType: Int,
                  @HeaderType private val headerType: Int): BaseDialog(context) {

    private var btnPrimary: Button? = null
    private var btnSecondary: Button? = null
    private var btnSecondaryLong: Button? = null
    private var dialogImageHeader: CardView? = null
    private var imageHeader: ImageView? = null
    private var dialogContent: ConstraintLayout? = null
    private var dialogTitle: TextView? = null
    private var dialogDescription: TextView? = null

    override fun layoutResId(): Int = R.layout.layout_unify_dialog

    override fun initView(dialogView: View?) {
        btnPrimary = dialogView?.findViewById(R.id.dialog_btn_primary)
        btnSecondary = dialogView?.findViewById(R.id.dialog_btn_secondary)
        btnSecondaryLong = dialogView?.findViewById(R.id.dialog_btn_secondary_long)
        dialogContent = dialogView?.findViewById(R.id.dialog_content)
        dialogImageHeader = dialogView?.findViewById(R.id.dialog_card_icon)
        dialogTitle = dialogView?.findViewById(R.id.dialog_title)
        imageHeader = dialogView?.findViewById(R.id.dialog_icon)
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
            val topMargin = context.resources.getDimensionPixelSize(when(headerType){
                ICON_HEADER -> {
                    dialogImageHeader?.radius = context.resources.getDimensionPixelSize(R.dimen.dp_8).toFloat()
                    R.dimen.dp_16
                }
                IMAGE_HEADER -> {
                    dialogImageHeader?.radius = context.resources.getDimensionPixelSize(R.dimen.dp_10).toFloat()
                    R.dimen.dp_0
                }
                else -> R.dimen.dp_24
            })
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMargins(layoutParams.marginStart, topMargin,
                        layoutParams.marginEnd, layoutParams.bottomMargin)
            } else {
                layoutParams.setMargins(layoutParams.leftMargin, topMargin,
                        layoutParams.rightMargin, layoutParams.bottomMargin)
            }
        }
    }

    override fun initListener(dialog: AlertDialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    fun setTitle(title: CharSequence){
        dialogTitle?.text = title
    }

    fun setImageHeader(imgRes: Int){
        imageHeader?.setImageDrawable(AppCompatResources.getDrawable(context, imgRes))
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