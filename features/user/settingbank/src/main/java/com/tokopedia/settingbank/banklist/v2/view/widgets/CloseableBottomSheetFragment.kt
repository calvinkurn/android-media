package com.tokopedia.settingbank.banklist.v2.view.widgets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.settingbank.R


class CloseableBottomSheetFragment private constructor(private val fragment: Fragment,
                                                       private val isCloseable: Boolean,
                                                       @CloseableBottomSheetState private var mState: Int,
                                                       private val title: String,
                                                       private val closableCallBack: ClosableCallback?) : BottomSheetDialogFragment() {

    private var bottomSheetInternal: FrameLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.sbank_closeable_bottom_sheet_fragment, container, false)
        val frameLayout = contentView.findViewById<FrameLayout>(R.id.container)
        childFragmentManager.beginTransaction().add(frameLayout.id, fragment).commit()
        val closeButton = contentView.findViewById<ImageView>(R.id.close_button_rounded)
        val headerTitle = contentView.findViewById<TextView>(R.id.title_closeable_rounded)
        val tray = contentView.findViewById<TextView>(R.id.tray_close)

        if (isCloseable) {
            closeButton.show()
            headerTitle.show()
            tray.hide()
            headerTitle.text = title
            closeButton.setOnClickListener {
                dismiss()
            }

        } else {
            closeButton.hide()
            headerTitle.hide()
            tray.show()
        }
        setDialogShowListener()
        return contentView
    }

    private fun setDialogShowListener() {
        dialog.setOnShowListener {
            bottomSheetInternal = view?.parent as FrameLayout
            context?.let { c ->
                bottomSheetInternal?.let { b ->
                    b.background = c.resources.getDrawable(com.tokopedia.design.R.drawable.bg_header_rounded_closeable_bs)
                    view?.let {
                        when (mState) {
                            STATE_FULL -> {
                                b.layoutParams.height = (b.parent as View).height
                                BottomSheetBehavior.from(b).peekHeight = (b.parent as View).height
                            }

                            STATE_FIT -> {
                                if (it.height < (b.parent as View).height) {
                                    BottomSheetBehavior.from(b).peekHeight = b.height
                                } else {
                                    b.layoutParams.height = (b.parent as View).height
                                    BottomSheetBehavior.from(b).peekHeight = (b.parent as View).height
                                }
                            }
                        }

                    }
                }
            }

        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        closableCallBack?.onCloseClick(this)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        closableCallBack?.onCloseClick(this)
    }

    companion object {
        @JvmOverloads
        fun newInstance(fragment: Fragment, isCloseable: Boolean,
                        title: String = "", closableCallBack: ClosableCallback? = null,
                        @CloseableBottomSheetState state: Int) = CloseableBottomSheetFragment(fragment, isCloseable, title = title,
                closableCallBack = closableCallBack, mState = state)

        const val STATE_FULL = 1
        const val STATE_PARTIAL = 2
        const val STATE_FIT = 3
    }

    @IntDef(STATE_FIT, STATE_FULL, STATE_PARTIAL)
    annotation class CloseableBottomSheetState

    interface ClosableCallback {
        fun onCloseClick(bottomSheetFragment: BottomSheetDialogFragment)
    }


}