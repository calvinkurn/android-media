package com.tokopedia.kol.feature.following_list.view.fragment

import android.os.Bundle
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.following_list.di.DaggerKolFollowingListComponent

/**
 * Created by jegul on 2019-10-22
 */
class KolFollowingListFragment : BaseFollowListFragment() {

    companion object {

        @JvmStatic
        fun createInstance(bundle: Bundle?): KolFollowingListFragment {
            return KolFollowingListFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun initInjector() {
        DaggerKolFollowingListComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
    }
}