package com.tokopedia.promousage.view.custom

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.promousage.databinding.BottomsheetPromoBinding
import com.tokopedia.unifycomponents.R
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable

open class PromoBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): PromoBottomSheet {
            val args = Bundle()
            val fragment = PromoBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }


    private var binding by autoClearedNullable<BottomsheetPromoBinding>()
    private lateinit var frameDialogView: View
    private var bottomSheet = BottomSheetBehavior<View>()
    var isDragable: Boolean = false
    var isHideable: Boolean = false
    var isFullpage: Boolean = false
    var customPeekHeight: Int = 200
    var showKnob: Boolean = true
    var showHeader: Boolean = true
    var showCloseIcon: Boolean = true
    var overlayClickDismiss: Boolean = true
    var clearContentPadding: Boolean = false
    var isKeyboardOverlap: Boolean = true
    var isSkipCollapseState: Boolean = false
    private var displayMetrix = DisplayMetrics()
    private var whiteContainerBackground: Drawable? = null
    var bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_COLLAPSED
    private var child: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.UnifyBottomSheetOverlapStyle)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetPromoBinding.inflate(inflater, container, false)
        binding?.bottomSheetTitle?.text = "Promo"

        if (child != null) {
            (binding?.root as ViewGroup).addView(child)
        }

        dialog?.setOnShowListener {
            showListener(it)
        }



        binding?.bottomSheetClose?.visibility = if (showCloseIcon) View.VISIBLE else View.GONE

        (requireContext() as Activity).windowManager.defaultDisplay.getMetrics(displayMetrix)

        return binding?.root
    }

    private fun showListener(dialogInterface: DialogInterface?) {
        //dialog?.setCanceledOnTouchOutside(overlayClickDismiss)

        //bottomSheetClose.setOnClickListener(closeListener)

        //BottomSheetUnify.actionLayout(bottomSheetAction, actionText, actionIcon, actionListener)

        val frameDialogView = binding?.bottomSheetWrapper?.parent as View
        frameDialogView.setBackgroundColor(Color.TRANSPARENT)

        frameDialogView.bringToFront()

        bottomSheet = BottomSheetBehavior.from(frameDialogView)

        /**
         * set peekheight so user cant drag down the bottomsheet
         */
        if (isFullpage && !isDragable) {    // full page & not dragable
            frameDialogView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

            bottomSheet.peekHeight = displayMetrix.heightPixels
        } else if (!isFullpage && !isDragable) { // not full page & not dragable
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {

                }

                override fun onStateChanged(p0: View, p1: Int) {
                    /**
                     * Set peekheight here because need to get view height, view height can be get after rendered
                     * peek height obtained from wrapper parent height because parent background have another padding from 9patch
                     */
                    bottomSheet.peekHeight = (binding?.bottomSheetWrapper?.parent as View).height

                    if (isHideable && p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        } else { // dragable
            var mPeekHeight = customPeekHeight.toPx()
            if (isSkipCollapseState) {
                if (isFullpage) {
                    mPeekHeight = displayMetrix.heightPixels
                } else {
                    mPeekHeight = (binding?.bottomSheetWrapper?.parent as View).height
                }
            }
            bottomSheet.peekHeight = mPeekHeight
            bottomSheet.state = bottomSheetBehaviorDefaultState

            if (isFullpage) {
                frameDialogView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }

            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {

                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }

        bottomSheet.isHideable = isHideable
        bottomSheet.state = if (!isDragable || isFullpage) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_COLLAPSED
        }

        // ======================================

        //showDialogListener()
    }

    fun setChild(childParam: View?) {
        child = childParam
    }
}
