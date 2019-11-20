package com.tokopedia.profile.following_list.view.presenter

import android.content.Context
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.profile.following_list.data.pojo.usershopfollow.GetShopFollowingData
import com.tokopedia.profile.following_list.data.pojo.usershopfollow.UserShopFollowDetail
import com.tokopedia.profile.following_list.domain.interactor.GetShopFollowingListUseCase
import com.tokopedia.profile.following_list.view.listener.FollowingList
import com.tokopedia.profile.following_list.view.viewmodel.FollowingViewModel
import com.tokopedia.profile.following_list.view.viewmodel.ShopFollowingResultViewModel
import com.tokopedia.profile.following_list.view.viewmodel.ShopFollowingViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profile.R
import com.tokopedia.profile.following_list.view.fragment.BaseFollowListFragment
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import rx.Subscriber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 2019-10-22
 */
class ShopFollowingListPresenter @Inject constructor(
        @ApplicationContext private val context: Context,
        private val getUserShopFollowingListUseCase: GetShopFollowingListUseCase,
        private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase
) : BaseDaggerPresenter<FollowingList.View<ShopFollowingViewModel, ShopFollowingResultViewModel>>(), FollowingList.Presenter<ShopFollowingViewModel, ShopFollowingResultViewModel>, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = SupervisorJob()

    override fun getFollowList(userId: Int, type: BaseFollowListFragment.FollowListType) {
        val page = 1
        launch {
            val userFollow = getUserShopFollowingListUseCase.apply {
                clearRequest()
                addRequestWithParam(userId, page)
            }.executeOnBackground()
            view.run {
                onSuccessGetKolFollowingList(userFollow.convertToUiModel(page))
                hideLoading()
            }
        }
    }

    override fun getFollowListLoadMore(userId: Int, cursor: String?, type: BaseFollowListFragment.FollowListType) {
        val page = try { cursor?.toInt()?.plus(1) } catch (e: Exception) { null } ?: return
        launch {
            val userFollow = getUserShopFollowingListUseCase.apply {
                clearRequest()
                addRequestWithParam(userId, page)
            }.executeOnBackground()
            view.run {
                onSuccessLoadMoreKolFollowingList(userFollow.convertToUiModel(page))
                hideLoading()
            }
        }
    }

    override fun unfollowShop(model: FollowingViewModel) {
        toggleFavouriteShopUseCase.execute(
                ToggleFavouriteShopUseCase.createRequestParam(model.id.toString(), Action.UNFOLLOW),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean) {
                        view.onSuccessUnfollowShop(model)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        view.onErrorUnfollowShop(ErrorHandler.getErrorMessage(context, e))
                    }
                }
        )
    }

    override fun detachView() {
        super.detachView()
        toggleFavouriteShopUseCase.unsubscribe()
        job.cancel()
    }

    private fun GetShopFollowingData.convertToUiModel(currentPage: Int): ShopFollowingResultViewModel = userShopFollow.result.let { result ->
        ShopFollowingResultViewModel(
                isCanLoadMore = result.haveNext,
                totalCount = result.totalCount.toInt(),
                followingViewModelList = result.userShopFollowDetail.map { it.convertToUiModel() },
                currentPage = currentPage
        )
    }

    private fun UserShopFollowDetail.convertToUiModel(): ShopFollowingViewModel = ShopFollowingViewModel(
            id = shopID.toInt(),
            name = shopName,
            avatarUrl = logo,
            isLoadingItem = false,
            etalase = context.getString(R.string.shop_following_etalase_count, stats.totalShowcase),
            product = context.getString(R.string.shop_following_product_count, stats.productSold)
    )
}