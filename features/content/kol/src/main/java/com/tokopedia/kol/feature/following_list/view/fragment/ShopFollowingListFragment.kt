package com.tokopedia.kol.feature.following_list.view.fragment

import android.os.Bundle
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.following_list.di.DaggerKolFollowingListComponent
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList
import com.tokopedia.kol.feature.following_list.view.presenter.ShopFollowingListPresenter
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-10-22
 */
class ShopFollowingListFragment : BaseFollowListFragment() {

    companion object {

        @JvmStatic
        fun createInstance(bundle: Bundle?): ShopFollowingListFragment {
            return ShopFollowingListFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    @field:[Named(ShopFollowingListPresenter.NAME)]
    override lateinit var presenter: KolFollowingList.Presenter

    override fun initInjector() {
        DaggerKolFollowingListComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
    }


}