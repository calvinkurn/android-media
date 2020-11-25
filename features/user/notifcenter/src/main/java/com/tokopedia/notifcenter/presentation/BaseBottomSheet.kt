package com.tokopedia.notifcenter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class BaseBottomSheet: BottomSheetUnify() {

    abstract fun renderView()

    abstract fun contentView(): Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(
                requireContext(),
                contentView(),
                null
        )
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()
    }

}