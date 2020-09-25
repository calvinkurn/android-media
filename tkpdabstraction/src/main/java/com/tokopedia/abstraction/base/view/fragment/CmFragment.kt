package com.tokopedia.abstraction.base.view.fragment

import androidx.annotation.StringDef
import com.tokopedia.abstraction.base.view.fragment.FragmentInflater.Companion.ACTIVITY
import com.tokopedia.abstraction.base.view.fragment.FragmentInflater.Companion.DEFAULT
import com.tokopedia.abstraction.base.view.fragment.FragmentInflater.Companion.VIEW_PAGER
import com.tokopedia.abstraction.base.view.listener.FragmentLifecycleObserver

abstract class CmFragment: TkpdBaseV4Fragment() {
    @FragmentInflater
    open var fragmentInflater: String = DEFAULT
    var fragmentName: String = javaClass.name
    var hasReadArguments = false

    override fun onStart() {
        super.onStart()
        if (!hasReadArguments) {
            fragmentInflater = arguments?.getString(BUNDLE_ARGS_INFLATER) ?: fragmentInflater
            if (!arguments?.getString(BUNDLE_ARGS_FRAGMENT_NAME).isNullOrEmpty())
                fragmentName = fragmentName + "_" + arguments?.getString(BUNDLE_ARGS_FRAGMENT_NAME) ?: fragmentName
            hasReadArguments = true
        }
    }

    //todo back aane pe onResume handle kara h - tell PO - maybe we can put this on onCreate(..)
    //todo Rahul - check - openscreenname
    override fun onResume() {
        super.onResume()

        view?.post {
            if (isVisible && fragmentInflater == ACTIVITY) {
                FragmentLifecycleObserver.onFragmentResumed(this)
            }
            else if (isVisible && fragmentInflater == VIEW_PAGER) {
                //To handle - when this fragment gets onResume event from backstack (when user comes back to this activity)
                //todo Rahul do not use this, instead call from container fragment
//                val arr = IntArray(2)
//                view?.getLocationOnScreen(arr)
//                if (arr[0] == 0) {
//                    FragmentLifecycleObserver.onFragmentResumed(this)
//                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        FragmentLifecycleObserver.onFragmentStop(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        FragmentLifecycleObserver.onFragmentDestroyed(this)
    }

    companion object {
        const val BUNDLE_ARGS_INFLATER = "cm_inflater"
        const val BUNDLE_ARGS_NAME = "cm_name"
        const val BUNDLE_ARGS_FRAGMENT_NAME = "cm_fragmentName"
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(VIEW_PAGER, ACTIVITY, DEFAULT)
annotation class FragmentInflater {
    companion object {
        const val VIEW_PAGER = "vp"
        const val ACTIVITY = "activity"
        const val DEFAULT = "default"
    }
}