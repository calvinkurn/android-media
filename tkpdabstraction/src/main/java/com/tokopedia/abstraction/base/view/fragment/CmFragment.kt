package com.tokopedia.abstraction.base.view.fragment

import androidx.annotation.StringDef
import com.tokopedia.abstraction.base.view.fragment.FragmentInflater.Companion.ACTIVITY
import com.tokopedia.abstraction.base.view.fragment.FragmentInflater.Companion.DEFAULT
import com.tokopedia.abstraction.base.view.fragment.FragmentInflater.Companion.FRAGMENT
import com.tokopedia.abstraction.base.view.fragment.FragmentInflater.Companion.VIEW_PAGER
import com.tokopedia.fragmentLifecycle.FragmentLifecycleObserver

abstract class CmFragment: TkpdBaseV4Fragment() {
    @FragmentInflater
    open var fragmentInflater: String = DEFAULT
    var fragmentName: String = javaClass.name

    override fun onResume() {
        super.onResume()

        view?.post {
            if (isVisible && fragmentInflater == ACTIVITY) {
                FragmentLifecycleObserver.onFragmentResume(this)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        FragmentLifecycleObserver.onFragmentStop(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        FragmentLifecycleObserver.onFragmentStop(this)
    }

    companion object {
        const val BUNDLE_ARGS_INFLATER = "cm_inflater"
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(VIEW_PAGER, ACTIVITY, FRAGMENT, DEFAULT)
annotation class FragmentInflater {
    companion object {
        const val VIEW_PAGER = "vp"
        const val ACTIVITY = "activity"
        const val FRAGMENT = "fragment"
        const val DEFAULT = "default"
    }
}