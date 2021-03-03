package com.tokopedia.shop.score.common.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class BaseBottomSheetShopScore: BottomSheetUnify() {

    abstract fun getLayoutResId(): Int

    abstract fun show(fragmentManager: FragmentManager?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(getLayoutResId(), container, false)
        setChild(view)
    }
}