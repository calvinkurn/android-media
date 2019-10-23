package com.tokopedia.kol.feature.following_list.view.fragment

import android.os.Bundle
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.following_list.di.DaggerKolFollowingListComponent
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList
import com.tokopedia.kol.feature.following_list.view.presenter.ShopFollowingListPresenter
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingResultViewModel
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingViewModel
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-10-22
 */
class ShopFollowingListFragment : BaseFollowListFragment<ShopFollowingViewModel, ShopFollowingResultViewModel>() {

    companion object {

        @JvmStatic
        fun createInstance(bundle: Bundle?): ShopFollowingListFragment {
            return ShopFollowingListFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    override lateinit var presenter: KolFollowingList.Presenter<ShopFollowingViewModel, ShopFollowingResultViewModel>

    override fun initInjector() {
        DaggerKolFollowingListComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
    }

    override fun updateParams(viewModel: ShopFollowingResultViewModel) {
        this.isCanLoadMore = viewModel.isCanLoadMore
        this.cursor = viewModel.currentPage.toString()
    }

    override fun onViewUpdated(viewModel: ShopFollowingResultViewModel) {

    }
}