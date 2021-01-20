package com.tokopedia.sellerreview.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerhome.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By @ilhamsuaib on 20/01/21
 */

class RatingBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "RatingBottomSheet"
        fun createInstance(): RatingBottomSheet {
            return RatingBottomSheet().apply {
                showCloseIcon = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun dismiss() {
        view?.post {
            if (isVisible) {
                super.dismiss()
            }
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setChild(inflater: LayoutInflater, container: ViewGroup?) {
        val child = inflater.inflate(R.layout.sir_rating_bottom_sheet, container, false)
        setupView(child)
        setChild(child)
    }

    private fun setupView(child: View) {

    }
}