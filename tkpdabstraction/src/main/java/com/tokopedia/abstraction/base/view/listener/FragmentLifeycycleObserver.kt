package com.tokopedia.abstraction.base.view.listener

import androidx.fragment.app.Fragment

object FragmentLifecycleObserver {

    private val callbacks = arrayListOf<FragmentLifecycleCallback>()

    fun onFragmentStart(fragment: Fragment){
        for(item in FragmentLifecycleObserver.callbacks){
            item.onFragmentStart(fragment)
        }
    }

    fun onFragmentResume(fragment: Fragment){
        for(item in FragmentLifecycleObserver.callbacks){
            item.onFragmentResume(fragment)
        }
    }

    fun onFragmentStop(fragment: Fragment){
        for(item in FragmentLifecycleObserver.callbacks){
            item.onFragmentStop(fragment)
        }
    }
    fun onFragmentSelected(fragment: Fragment){
        for(item in FragmentLifecycleObserver.callbacks){
            item.onFragmentSelected(fragment)
        }
    }

    fun onFragmentUnSelected(fragment: Fragment){
        for(item in FragmentLifecycleObserver.callbacks){
            item.onFragmentUnSelected(fragment)
        }
    }

    fun registerCallback(fragmentLifecycleCallback: FragmentLifecycleCallback){
        callbacks.add(fragmentLifecycleCallback)
    }

    fun unRegisterCallback(fragmentLifecycleCallback: FragmentLifecycleCallback){
        callbacks.remove(fragmentLifecycleCallback)
    }


    //==============USELESS CODE BELOW NEED TO REMOVE==========
//    var weakFragment: WeakReference<CmFragment>? = null
//
//    fun onFragmentSelected(fragment: CmFragment) {
//        checkNotifications(fragment)
//    }
//
//    fun onFragmentResumed(fragment: CmFragment) {
//        checkNotifications(fragment)
//    }
//
//    fun onFragmentStop(fragment: CmFragment) {
//
//    }
//
//    fun onFragmentDestroyed(fragment: CmFragment) {
//
//    }
//
//    private fun checkNotifications(fragment: CmFragment) {
//
//
//    }

//    fun createAlertDialog(context: Context, title: String) {
//
//    }
}