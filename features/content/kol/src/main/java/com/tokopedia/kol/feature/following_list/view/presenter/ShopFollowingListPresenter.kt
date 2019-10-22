package com.tokopedia.kol.feature.following_list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kol.feature.following_list.domain.interactor.GetShopFollowingListUseCase
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList
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
        private val getUserShopFollowingListUseCase: GetShopFollowingListUseCase
) : BaseDaggerPresenter<KolFollowingList.View>(), KolFollowingList.Presenter, CoroutineScope {

    companion object {
        const val NAME = "ShopFollowingPresenter"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = SupervisorJob()

    override fun getKolFollowingList(userId: Int) {
        launch {
            val userFollow = getUserShopFollowingListUseCase.apply {
                clearRequest()
                addRequestWithParam(userId, 1)
            }.executeOnBackground()
            userFollow.toString()
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
}