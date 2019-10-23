package com.tokopedia.kol.feature.following_list.view.presenter

import android.content.Context
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.following_list.data.pojo.usershopfollow.GetShopFollowingData
import com.tokopedia.kol.feature.following_list.data.pojo.usershopfollow.UserShopFollowDetail
import com.tokopedia.kol.feature.following_list.domain.interactor.GetShopFollowingListUseCase
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingResultViewModel
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 2019-10-22
 */
class ShopFollowingListPresenter @Inject constructor(
        @ApplicationContext private val context: Context,
        private val getUserShopFollowingListUseCase: GetShopFollowingListUseCase
) : BaseDaggerPresenter<KolFollowingList.View<ShopFollowingViewModel, ShopFollowingResultViewModel>>(), KolFollowingList.Presenter<ShopFollowingViewModel, ShopFollowingResultViewModel>, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = SupervisorJob()

    override fun getKolFollowingList(userId: Int) {
        launch {
            val userFollow = getUserShopFollowingListUseCase.apply {
                clearRequest()
                addRequestWithParam(userId, 1)
            }.executeOnBackground()
            view.onSuccessGetKolFollowingList(userFollow.convertToViewModel())
        }
    }

    override fun getKolLoadMore(userId: Int, cursor: String?) {

    }

    override fun getFollowersList(userId: Int) {

    }

    override fun getFollowersLoadMore(userId: Int, cursor: String?) {

    }

    override fun detachView() {
        super.detachView()
        job.cancel()
    }

    private fun GetShopFollowingData.convertToViewModel(): ShopFollowingResultViewModel = userShopFollow.result.let { result ->
        ShopFollowingResultViewModel(
                isCanLoadMore = result.haveNext,
                totalCount = result.totalCount.toInt(),
                followingViewModelList = result.userShopFollowDetail.map { it.convertToViewModel() }
        )
    }

    private fun UserShopFollowDetail.convertToViewModel(): ShopFollowingViewModel = ShopFollowingViewModel(
            id = shopID.toInt(),
            name = shopName,
            avatarUrl = logo,
            isLoadingItem = false,
            etalase = context.getString(R.string.shop_following_etalase_count, stats.totalShowcase),
            product = context.getString(R.string.shop_following_product_count, stats.productSold)
    )
}