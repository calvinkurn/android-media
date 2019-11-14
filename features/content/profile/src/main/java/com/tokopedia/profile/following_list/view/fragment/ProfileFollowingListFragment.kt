package com.tokopedia.profile.following_list.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.profile.following_list.di.DaggerFollowingListComponent
import com.tokopedia.profile.following_list.view.listener.FollowingList
import com.tokopedia.profile.following_list.view.listener.FollowingListEmptyListener
import com.tokopedia.profile.following_list.view.viewmodel.ProfileFollowingResultViewModel
import com.tokopedia.profile.following_list.view.viewmodel.ProfileFollowingViewModel
import javax.inject.Inject

/**
 * Created by jegul on 2019-10-22
 */
class ProfileFollowingListFragment : BaseFollowListFragment<ProfileFollowingViewModel, ProfileFollowingResultViewModel>() {

    companion object {

        @JvmStatic
        fun createInstance(bundle: Bundle?): ProfileFollowingListFragment {
            return ProfileFollowingListFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    override lateinit var presenter: FollowingList.Presenter<ProfileFollowingViewModel, ProfileFollowingResultViewModel>

    override fun initInjector() {
        DaggerFollowingListComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    override fun onListItemClicked(item: ProfileFollowingViewModel) {
        if (RouteManager.isSupportApplink(context, item.profileApplink) && !item.profileApplink.contains("m.tokopedia.com")) {
            RouteManager.route(context, item.profileApplink)
        }
    }

    override fun onViewUpdated(viewModel: ProfileFollowingResultViewModel) {
        if (viewModel.followingViewModelList.isEmpty()) {
            emptyButton.text = viewModel.buttonText
            emptyApplink = viewModel.buttonApplink

            if (activity is FollowingListEmptyListener) {
                (activity as FollowingListEmptyListener).onFollowingEmpty()
            }
        } else {
            if (activity is FollowingListEmptyListener) {
                (activity as FollowingListEmptyListener).onFollowingNotEmpty()
            }
        }
    }

    override fun updateParams(viewModel: ProfileFollowingResultViewModel) {
        this.isCanLoadMore = viewModel.isCanLoadMore
        this.cursor = viewModel.lastCursor
    }
}