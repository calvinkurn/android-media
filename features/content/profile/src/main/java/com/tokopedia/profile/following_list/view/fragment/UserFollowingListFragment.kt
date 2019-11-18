package com.tokopedia.profile.following_list.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.DeepLinkChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.profile.following_list.di.DaggerFollowingListComponent
import com.tokopedia.profile.following_list.view.listener.FollowingList
import com.tokopedia.profile.following_list.view.listener.FollowingListEmptyListener
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingResultViewModel
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingViewModel
import javax.inject.Inject

/**
 * Created by jegul on 2019-10-22
 */
class UserFollowingListFragment : BaseFollowListFragment<UserFollowingViewModel, UserFollowingResultViewModel>() {

    companion object {

        @JvmStatic
        fun createInstance(bundle: Bundle?): UserFollowingListFragment {
            return UserFollowingListFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    override lateinit var presenter: FollowingList.Presenter<UserFollowingViewModel, UserFollowingResultViewModel>

    override fun initInjector() {
        DaggerFollowingListComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    override fun onListItemClicked(item: UserFollowingViewModel) {
        if (RouteManager.isSupportApplink(context, item.profileApplink) && !item.profileApplink.contains(DeepLinkChecker.MOBILE_HOST)) {
            RouteManager.route(context, item.profileApplink)
        }
    }

    override fun onViewUpdated(viewModel: UserFollowingResultViewModel) {
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

    override fun updateParams(viewModel: UserFollowingResultViewModel) {
        this.isCanLoadMore = viewModel.isCanLoadMore
        this.cursor = viewModel.lastCursor
    }
}