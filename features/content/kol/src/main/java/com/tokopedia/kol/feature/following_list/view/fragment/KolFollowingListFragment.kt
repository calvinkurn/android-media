package com.tokopedia.kol.feature.following_list.view.fragment

import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.following_list.di.DaggerKolFollowingListComponent
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingListEmptyListener
import com.tokopedia.kol.feature.following_list.view.presenter.KolFollowingListPresenter
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-10-22
 */
class KolFollowingListFragment : BaseFollowListFragment<KolFollowingViewModel, KolFollowingResultViewModel>() {

    companion object {

        @JvmStatic
        fun createInstance(bundle: Bundle?): KolFollowingListFragment {
            return KolFollowingListFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    override lateinit var presenter: KolFollowingList.Presenter<KolFollowingViewModel, KolFollowingResultViewModel>

    override fun initInjector() {
        DaggerKolFollowingListComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
    }

    override fun onListItemClicked(item: KolFollowingViewModel) {
        if (RouteManager.isSupportApplink(context, item.profileApplink) && !item.profileApplink.contains("m.tokopedia.com")) {
            RouteManager.route(context, item.profileApplink)
        }
    }

    override fun onViewUpdated(viewModel: KolFollowingResultViewModel) {
        if (viewModel.followingViewModelList.isEmpty()) {
            emptyButton.text = viewModel.buttonText
            emptyApplink = viewModel.buttonApplink

            if (activity is KolFollowingListEmptyListener) {
                (activity as KolFollowingListEmptyListener).onFollowingEmpty()
            }
        } else {
            if (activity is KolFollowingListEmptyListener) {
                (activity as KolFollowingListEmptyListener).onFollowingNotEmpty()
            }
        }
    }

    override fun updateParams(viewModel: KolFollowingResultViewModel) {
        this.isCanLoadMore = viewModel.isCanLoadMore
        this.cursor = viewModel.lastCursor
    }
}