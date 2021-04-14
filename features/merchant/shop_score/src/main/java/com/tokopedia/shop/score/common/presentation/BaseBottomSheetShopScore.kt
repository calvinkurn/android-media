package com.tokopedia.shop.score.common.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class BaseBottomSheetShopScore: BottomSheetUnify() {

    abstract fun getLayoutResId(): Int

    abstract fun getTitleBottomSheet(): String

    abstract fun show(fragmentManager: FragmentManager?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    protected open fun initInjector() {}

    @SuppressWarnings("unchecked")
    open fun <C> getComponent(componentType: Class<C>): C? {
        return componentType.cast((activity as? HasComponent<C>)?.component)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        setTitle(getTitleBottomSheet())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPause() {
        childFragmentManager.fragments.forEach {
            if (it is BottomSheetUnify) it.dismiss()
        }
        super.onPause()
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(getLayoutResId(), container, false)
        setChild(view)
    }
}