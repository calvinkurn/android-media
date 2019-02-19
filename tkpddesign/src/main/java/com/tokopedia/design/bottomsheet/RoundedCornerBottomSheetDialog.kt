package com.tokopedia.design.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.tokopedia.design.R

/**
 * @author by nisie on 14/02/19.
 */
open class RoundedCornerBottomSheetDialog : BottomSheetDialog {

    internal lateinit var context: Context
    private var closeListener: CloseClickedListener? = null

    interface CloseClickedListener {
        fun onCloseDialog()
    }

    private constructor(context: Context) : super(context) {
        init(context)
    }

    private constructor(context: Context, theme: Int) : super(context, theme) {
        init(context)
    }

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {
        init(context)
    }

    private fun setListener(closeListener: CloseClickedListener) {
        this.closeListener = closeListener
    }

    private fun init(context: Context) {
        this.context = context
    }

    fun initView(bottomSheetWebviewFragment: Fragment) {
        val contentView = (context as FragmentActivity).layoutInflater.inflate(R.layout
                .bottom_sheet_rounded_header, null)

        val closeButton = contentView.findViewById<ImageView>(R.id.close_button)
        closeButton.setOnClickListener {
            dismiss()
            closeListener?.run{
                onCloseDialog()
            }
        }

        (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_sheet_fragment_container,
                        bottomSheetWebviewFragment,
                        bottomSheetWebviewFragment.javaClass.simpleName)
                .commit()

        super.setContentView(contentView)
    }

    /**
     *
     */
    fun setCustomFragment(activity : FragmentActivity, fragment : Fragment){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = (context as Activity).layoutInflater.inflate(R.layout
                .bottom_sheet_rounded_header, null)

        val closeButton = contentView.findViewById<ImageView>(R.id.close_button)
        closeButton.setOnClickListener {
            dismiss()
            closeListener!!.onCloseDialog()
        }

        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, fragment, fragment.javaClass.simpleName)
                .commit()

        super.setContentView(contentView)
    }

    /**
     * Use this function instead to override custom view.
     */
    fun setCustomContentView(layoutResId : Int){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = (context as Activity).layoutInflater.inflate(R.layout
                .bottom_sheet_rounded_header, null)

        val closeButton = contentView.findViewById<ImageView>(R.id.close_button)
        closeButton.setOnClickListener {
            dismiss()
            closeListener!!.onCloseDialog()
        }

        val frameLayout = contentView.findViewById<FrameLayout>(R.id.bottom_sheet_fragment_container)
        val view = inflater.inflate(layoutResId, null)
        frameLayout.addView(view)
        super.setContentView(contentView)
    }

    companion object {

        fun createInstance(context: Context): RoundedCornerBottomSheetDialog {
            val closeableBottomSheetDialog = RoundedCornerBottomSheetDialog(context)
            closeableBottomSheetDialog.setListener(object : CloseClickedListener {
                override fun onCloseDialog() {

                }
            })
            return closeableBottomSheetDialog
        }

        fun createInstance(context: Context,
                           closeListener: CloseClickedListener): RoundedCornerBottomSheetDialog {
            val closeableBottomSheetDialog = RoundedCornerBottomSheetDialog(context)
            closeableBottomSheetDialog.setListener(closeListener)
            return closeableBottomSheetDialog
        }
    }
}