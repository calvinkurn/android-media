package com.tokopedia.profile.following_list.view.fragment

import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.profile.following_list.di.DaggerFollowingListComponent
import com.tokopedia.profile.following_list.view.activity.FollowingListActivity
import com.tokopedia.profile.following_list.view.listener.FollowingList
import com.tokopedia.profile.following_list.view.viewmodel.FollowingViewModel
import com.tokopedia.profile.following_list.view.viewmodel.ShopFollowingResultViewModel
import com.tokopedia.profile.following_list.view.viewmodel.ShopFollowingViewModel
import com.tokopedia.kotlin.extensions.view.hideLoadingTransparent
import com.tokopedia.kotlin.extensions.view.showLoadingTransparent
import com.tokopedia.profile.R
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

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

        @JvmStatic
        fun createInstance(userId: String): ShopFollowingListFragment {
            return ShopFollowingListFragment().apply {
                arguments = Bundle().apply {
                    putInt(FollowingListActivity.ARGS_USER_ID, userId.toInt())
                }
            }
        }
    }

    private val dialogDelete by lazy {
        DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setDescription(getString(R.string.shop_following_delete_description))
            setPrimaryCTAText(getString(R.string.shop_following_delete_yes))
            setSecondaryCTAText(getString(R.string.shop_following_delete_no))
            setSecondaryCTAClickListener {
                dismiss()
            }
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    @Inject
    override lateinit var presenter: FollowingList.Presenter<ShopFollowingViewModel, ShopFollowingResultViewModel>

    override fun initInjector() {
        DaggerFollowingListComponent.builder()
                .build()
                .inject(this)
    }

    override fun updateParams(viewModel: ShopFollowingResultViewModel) {
        this.isCanLoadMore = viewModel.isCanLoadMore
        this.cursor = viewModel.currentPage.toString()
    }

    override fun onViewUpdated(viewModel: ShopFollowingResultViewModel) {

    }

    override fun onListItemClicked(item: ShopFollowingViewModel) {
        RouteManager.route(
                context,
                ApplinkConst.SHOP_HOME,
                item.id.toString()
        )
    }

    override fun onUnfollowShopButtonClicked(model: FollowingViewModel) {
        dialogDelete.apply {
            setTitle(getString(R.string.shop_following_delete_title, model.name))
            setPrimaryCTAClickListener {
                presenter.unfollowShop(model)
                showLoadingTransparent()
                dialogDelete.dismiss()
            }
        }.show()
    }

    override fun onSuccessUnfollowShop(model: FollowingViewModel) {
        hideLoadingTransparent()
        adapter.run {
            val pos = itemList.indexOf(model)
            itemList.remove(model)
            notifyItemRemoved(pos)
        }
    }

    override fun onErrorUnfollowShop(errorMessage: String) {
        hideLoadingTransparent()
        view?.run {
            Toaster.showError(this, errorMessage, 2000)
        }
    }

    private fun showLoadingTransparent() {
        activity?.window?.decorView?.showLoadingTransparent()
    }

    private fun hideLoadingTransparent() {
        activity?.window?.decorView?.hideLoadingTransparent()
    }
}