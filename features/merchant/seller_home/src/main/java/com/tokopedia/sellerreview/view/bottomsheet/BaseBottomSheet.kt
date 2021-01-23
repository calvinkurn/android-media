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
 * Created By @ilhamsuaib on 22/01/21
 */

abstract class BaseBottomSheet : BottomSheetUnify() {

    protected var childView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.UnifyBottomSheetNotOverlapStyle)
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

    abstract fun show(fm: FragmentManager)

    protected abstract fun getResLayout(): Int

    protected abstract fun setupView(): Unit?

    private fun setChild(inflater: LayoutInflater, container: ViewGroup?) {
        val child = inflater.inflate(getResLayout(), container, false)
        childView = child
        setChild(child)
        setupView()
    }
}