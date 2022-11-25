package com.tokopedia.feed_shop.presenter

import android.content.Context
import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.feed_shop.shop.domain.DynamicFeedShopDomain
import com.tokopedia.feed_shop.shop.domain.usecase.GetFeedShopFirstUseCase
import com.tokopedia.feed_shop.shop.view.contract.FeedShopContract
import com.tokopedia.feed_shop.shop.view.presenter.FeedShopPresenter
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemShop
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.follow.FollowKol
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolData
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import rx.Subscriber

class FeedShopPresenterTest: KolPostLikeListener {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getDynamicFeedFirstUseCase: GetFeedShopFirstUseCase

    @RelaxedMockK
    lateinit var getDynamicFeedUseCase: GetDynamicFeedUseCase

    @RelaxedMockK
    lateinit var followKolPostGqlUseCase: FollowKolPostGqlUseCase

    @RelaxedMockK
    lateinit var likeKolPostUseCase: LikeKolPostUseCase

    @RelaxedMockK
    lateinit var deletePostUseCase: DeletePostUseCase

    @RelaxedMockK
    lateinit var trackAffiliateClickUseCase: TrackAffiliateClickUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var sendTopAdsUseCase: SendTopAdsUseCase

    lateinit var presenter: FeedShopContract.Presenter
    lateinit var view: FeedShopContract.View

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = FeedShopPresenter(
                getDynamicFeedFirstUseCase,
                getDynamicFeedUseCase,
                followKolPostGqlUseCase,
                likeKolPostUseCase,
                deletePostUseCase,
                trackAffiliateClickUseCase,
                atcUseCase,
                sendTopAdsUseCase
        )
        view = mockk(relaxed = true)
        presenter.attachView(view)
    }

    @Test
    fun `get feed first page should be successful`() {
        every {
            getDynamicFeedFirstUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedShopDomain>>().onCompleted()
            secondArg<Subscriber<DynamicFeedShopDomain>>().onNext(DynamicFeedShopDomain())
        }

        every {
            view.userSession.userId
        } returns "10000"

        presenter.getFeedFirstPage("123", false, false)

        verify {
            getDynamicFeedFirstUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get feed first page should be successful must not call onSuccessGetFeedFirstPage if DynamicFeedShopDomain is null`() {
        every {
            getDynamicFeedFirstUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedShopDomain>>().onCompleted()
            secondArg<Subscriber<DynamicFeedShopDomain>>().onNext(null)
        }

        every {
            view.userSession.userId
        } returns "10000"

        presenter.getFeedFirstPage("123", false, false)

        verify {
            getDynamicFeedFirstUseCase.execute(any(), any())
        }
        verify(exactly = 0) {
            view.onSuccessGetFeedFirstPage(any(), any(), any())
        }
    }

    @Test
    fun `get feed first page error should call showGetListError`() {
        every {
            getDynamicFeedFirstUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedShopDomain>>().onError(Throwable())
        }
        every {
            view.userSession.userId
        } returns "10000"
        presenter.getFeedFirstPage("123", false, false)
        verify {
            getDynamicFeedFirstUseCase.execute(any(), any())
            view.showGetListError(any())
        }
    }

    @Test
    fun `get feed first page error should call showGetListError when isAllowDebuggingTools is true and error is null`() {
        every {
            getDynamicFeedFirstUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedShopDomain>>().onError(null)
        }
        every {
            view.userSession.userId
        } returns "10000"
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.getFeedFirstPage("123", false, false)
        verify {
            getDynamicFeedFirstUseCase.execute(any(), any())
            view.showGetListError(any())
        }
    }

    @Test
    fun `get feed first page error should call showGetListError when isAllowDebuggingTools is true and error is not null`() {
        every {
            getDynamicFeedFirstUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedShopDomain>>().onError(Throwable())
        }
        every {
            view.userSession.userId
        } returns "10000"
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.getFeedFirstPage("123", false, false)
        verify {
            getDynamicFeedFirstUseCase.execute(any(), any())
            view.showGetListError(any())
        }
    }

    @Test
    fun `get feed first page error should call showGetListError when isAllowDebuggingTools is false`() {
        every {
            getDynamicFeedFirstUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedShopDomain>>().onError(Throwable())
        }
        every {
            view.userSession.userId
        } returns "10000"
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.getFeedFirstPage("123", false, false)
        verify {
            getDynamicFeedFirstUseCase.execute(any(), any())
            view.showGetListError(any())
        }
    }

    @Test
    fun `get feed first page, userId equals to zero and get feed should be successful`() {
        every {
            getDynamicFeedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedDomainModel>>().onCompleted()
            secondArg<Subscriber<DynamicFeedDomainModel>>().onError(Throwable())
            secondArg<Subscriber<DynamicFeedDomainModel>>().onNext(DynamicFeedDomainModel())
        }

        presenter.getFeedFirstPage("123", false, false)

        verify {
            getDynamicFeedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get feed should be successful`() {
        every {
            getDynamicFeedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedDomainModel>>().onCompleted()
            secondArg<Subscriber<DynamicFeedDomainModel>>().onError(Throwable())
            secondArg<Subscriber<DynamicFeedDomainModel>>().onNext(DynamicFeedDomainModel())
        }

        presenter.getFeed("1231")

        verify {
            getDynamicFeedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get feed should not call onSuccessGetFeed when DynamicFeedDomainModel is null`() {
        every {
            getDynamicFeedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedDomainModel>>().onNext(null)
        }

        presenter.getFeed("1231")

        verify {
            getDynamicFeedUseCase.execute(any(), any())
        }
        verify(exactly = 0) {
            view.onSuccessGetFeed(any(), any())
        }
    }

    @Test
    fun `get feed should not call showGetListError when view is null`() {
        presenter.detachView()
        every {
            getDynamicFeedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedDomainModel>>().onError(Throwable())
        }

        presenter.getFeed("1231")

        verify {
            getDynamicFeedUseCase.execute(any(), any())
        }
        verify(exactly = 0) {
            view.showGetListError(any())
        }
    }

    @Test
    fun `get feed should call showGetListError when view is null, isAllowDebuggingTools true and throwable is not null`() {
        every {
            getDynamicFeedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedDomainModel>>().onError(Throwable())
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.getFeed("1231")

        verify {
            getDynamicFeedUseCase.execute(any(), any())
        }
        verify {
            view.showGetListError(any())
        }
    }

    @Test
    fun `get feed should call showGetListError when view is null, isAllowDebuggingTools true and throwable is null`() {
        every {
            getDynamicFeedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedDomainModel>>().onError(null)
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.getFeed("1231")

        verify {
            getDynamicFeedUseCase.execute(any(), any())
        }
        verify {
            view.showGetListError(any())
        }
    }

    @Test
    fun `get feed should call showGetListError when view is null, isAllowDebuggingTools false and throwable is not null`() {
        every {
            getDynamicFeedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFeedDomainModel>>().onError(Throwable())
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.getFeed("1231")

        verify {
            getDynamicFeedUseCase.execute(any(), any())
        }
        verify {
            view.showGetListError(any())
        }
    }

    @Test
    fun `when followKol success should call onSuccessFollow`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(
                    mapOf(
                            FollowKolQuery::class.java to FollowKolQuery().apply {
                                data = FollowKol().apply {
                                    error = ""
                                    data = FollowKolData().apply {
                                        status = 1
                                    }
                                }
                            }
                    )
                    , any(), false))
        }

        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns true
        presenter.followKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onSuccessFollowKol()
        }
    }

    @Test
    fun `when followKol is not success should call onErrorFollowKol`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(
                    mapOf(
                            FollowKolQuery::class.java to FollowKolQuery().apply {
                                data = FollowKol().apply {
                                    data = FollowKolData().apply {
                                        status = 0
                                    }
                                }
                            }
                    ),
                    any(), false))
        }
        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns true
        presenter.followKol(1231)
        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when followKol data error should call onErrorFollowKol`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(
                    mapOf(
                            FollowKolQuery::class.java to FollowKolQuery().apply {
                                data = FollowKol().apply {
                                    data = FollowKolData().apply {
                                        status = 0
                                    }
                                    error = "error message"
                                }
                            }
                    ),
                    any(), false))
        }
        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns false
        presenter.followKol(1231)
        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when followKol response error should call onErrorFollowKol if view is not null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onError(Throwable())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.followKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.followKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when followKol response error should call onErrorFollowKol if view is not null and throwable is null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onError(null)
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.followKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.followKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when followKol response error should not call onErrorFollowKol if view null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onError(Throwable())
        }
        presenter.detachView()
        presenter.followKol(1231)
        verify {
            followKolPostGqlUseCase.execute(any())
        }
        verify(exactly = 0) {
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when followKol should call onErrorFollowKol if query is null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(any(), any(), false))
        }
        presenter.followKol(1231)
        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when unfollow kol success should call onSuccessFollowKol`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(mapOf(
                    FollowKolQuery::class.java to FollowKolQuery().apply {
                        data = FollowKol().apply {
                            data = FollowKolData().apply {
                                status = 1
                            }
                            error = "error message"
                        }
                    }
            ), any(), false))
        }
        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns true
        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onSuccessFollowKol()
        }
    }

    @Test
    fun `when unfollow kol is not success should call onErrorFollowKol`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(mapOf(
                    FollowKolQuery::class.java to FollowKolQuery().apply {
                        data = FollowKol().apply {
                            data = FollowKolData().apply {
                                status = 0
                            }
                            error = "error message"
                        }
                    }
            ), any(), false))
        }
        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns true
        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when unfollow kol data error should call onErrorFollowKol`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(mapOf(
                    FollowKolQuery::class.java to FollowKolQuery().apply {
                        data = FollowKol().apply {
                            data = FollowKolData().apply {
                                status = 0
                            }
                            error = "error message"
                        }
                    }
            ), any(), false))
        }
        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns false
        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when unfollow kol should call onErrorFollowKol if query is null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(any(), any(), false))
        }
        presenter.unfollowKol(1231)
        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when unfollow kol response error should call onErrorFollowKol if view is not null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onError(Throwable())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when unfollow kol response error should call onErrorFollowKol if view is not null and throwable is null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onError(null)
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `when unfollow kol response error should not call onErrorFollowKol if view null`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onError(Throwable())
        }
        presenter.detachView()
        presenter.unfollowKol(1231)
        verify {
            followKolPostGqlUseCase.execute(any())
        }
        verify(exactly = 0) {
            view.onErrorFollowKol(any())
        }
    }

    @Test
    fun `like kol post should be successful`() {
        every {
            likeKolPostUseCase.execute(any(), any())
        } answers {}

        presenter.likeKol(anyLong(), anyInt(), this)

        verify {
            likeKolPostUseCase.execute(any(), any())
        }

        presenter.attachView(null)
        presenter.likeKol(anyLong(), anyInt(), this)
    }

    @Test
    fun `unlike kol post should be successful`() {
        every {
            likeKolPostUseCase.execute(any(), any())
        } answers {}

        presenter.unlikeKol(anyLong(), anyInt(), this)

        verify {
            likeKolPostUseCase.execute(any(), any())
        }

        presenter.attachView(null)
        presenter.unlikeKol(anyLong(), anyInt(), this)
    }

    @Test
    fun `delete post success should call onSuccessDeletePost`() {
        every {
            deletePostUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onCompleted()
            secondArg<Subscriber<Boolean>>().onNext(true)
        }

        presenter.deletePost(anyString(), anyInt())

        verify {
            deletePostUseCase.execute(any(), any())
            view.onSuccessDeletePost(any())
        }
    }

    @Test
    fun `delete post success is false should not call onSuccessDeletePost`() {
        every {
            deletePostUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onCompleted()
            secondArg<Subscriber<Boolean>>().onNext(false)
        }

        presenter.deletePost(anyString(), anyInt())

        verify {
            deletePostUseCase.execute(any(), any())
        }
        verify(exactly = 0) {
            view.onSuccessDeletePost(any())
        }
    }

    @Test
    fun `delete post success is null should not call onSuccessDeletePost`() {
        every {
            deletePostUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onCompleted()
            secondArg<Subscriber<Boolean>>().onNext(null)
        }

        presenter.deletePost(anyString(), anyInt())

        verify {
            deletePostUseCase.execute(any(), any())
        }
        verify(exactly = 0) {
            view.onSuccessDeletePost(any())
        }
    }

    @Test
    fun `delete post response is error should call onErrorDeletePost`() {
        every {
            deletePostUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onError(Throwable())
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.deletePost(anyString(), anyInt())
        verify {
            deletePostUseCase.execute(any(), any())
            view.onErrorDeletePost(any(), any(), any())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.deletePost(anyString(), anyInt())
        verify {
            deletePostUseCase.execute(any(), any())
            view.onErrorDeletePost(any(), any(), any())
        }
    }

    @Test
    fun `delete post response is error should not call onErrorDeletePost if view is null`() {
        every {
            deletePostUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onError(Throwable())
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.detachView()
        presenter.deletePost(anyString(), anyInt())
        verify {
            deletePostUseCase.execute(any(), any())
        }
        verify(exactly = 0) {
            view.onErrorDeletePost(any(), any(), any())
        }
    }

    @Test
    fun `delete post response is error should call onErrorDeletePost if throwable is null`() {
        every {
            deletePostUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onError(null)
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.deletePost(anyString(), anyInt())
        verify {
            deletePostUseCase.execute(any(), any())
            view.onErrorDeletePost(any(), any(), any())
        }
    }

    @Test
    fun `track post click should be successful`() {
        every {
            trackAffiliateClickUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onCompleted()
            secondArg<Subscriber<Boolean>>().onError(Throwable())
            secondArg<Subscriber<Boolean>>().onNext(true)
        }

        every {
            view.userSession.isLoggedIn
        } returns true
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.trackPostClick(anyString(), anyString())

        verify {
            trackAffiliateClickUseCase.execute(any(), any())
        }

        every {
            view.userSession.isLoggedIn
        } returns false
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.trackPostClick(anyString(), anyString())

        verify {
            trackAffiliateClickUseCase.execute(any(), any())
        }

        every {
            trackAffiliateClickUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onError(null)
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.trackPostClick(anyString(), anyString())
    }

    @Test
    fun `track click post url should be successful`() {
        every {
            trackAffiliateClickUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onCompleted()
            secondArg<Subscriber<Boolean>>().onError(Throwable())
            secondArg<Subscriber<Boolean>>().onNext(true)
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.trackPostClickUrl(anyString())

        verify {
            trackAffiliateClickUseCase.execute(any(), any())
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.trackPostClickUrl(anyString())

        verify {
            trackAffiliateClickUseCase.execute(any(), any())
        }

        every {
            trackAffiliateClickUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onError(null)
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.trackPostClickUrl(anyString())
    }

    @Test
    fun `add post tag item success should call onAddToCartSuccess`() {
        val mockCartData = DataModel(success = 1, productId = 12345)
        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onCompleted()
            secondArg<Subscriber<AddToCartDataModel>>().onNext(AddToCartDataModel(
                    data = mockCartData
            ))
        }

        var postTagItem = PostTagItem().copy(shop = listOf(PostTagItemShop("12314"))).apply {
            id = "10000"
            price = "50000"
        }
        presenter.addPostTagItemToCart(postTagItem)

        verify {
            atcUseCase.execute(any(), any())
        }

        postTagItem = PostTagItem().copy(shop = listOf())
        postTagItem.applink = "www.tokopedia.com"
        presenter.addPostTagItemToCart(postTagItem)

        verify {
            view.onAddToCartSuccess(mockCartData.productId.toString())
        }
    }

    @Test
    fun `add post tag item not success should call onAddToCartFailed`() {
        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onCompleted()
            secondArg<Subscriber<AddToCartDataModel>>().onNext(AddToCartDataModel(
                    data = DataModel(success = 0)
            ))
        }

        var postTagItem = PostTagItem().copy(shop = listOf(PostTagItemShop("12314"))).apply {
            id = "10000"
            price = "50000"
        }
        presenter.addPostTagItemToCart(postTagItem)

        verify {
            atcUseCase.execute(any(), any())
        }

        postTagItem = PostTagItem().copy(shop = listOf())
        postTagItem.applink = "www.tokopedia.com"
        presenter.addPostTagItemToCart(postTagItem)

        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }

        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onNext(null)
        }
        postTagItem = PostTagItem().copy(shop = listOf(PostTagItemShop("12314"))).apply {
            id = "10000"
            price = "50000"
            applink = "www.tokopedia.com"
        }
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }

        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onNext(AddToCartDataModel())
        }
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }
    }

    @Test
    fun `add post tag item response error should call onAddToCartFailed`() {
        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onError(Throwable())
        }
        var postTagItem = PostTagItem().copy(shop = listOf(PostTagItemShop("12314"))).apply {
            id = "10000"
            price = "50000"
        }
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            atcUseCase.execute(any(), any())
        }
        postTagItem = PostTagItem().copy(shop = listOf())
        postTagItem.applink = "www.tokopedia.com"
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }
    }

    @Test
    fun `add post tag item response error should call onAddToCartFailed when exception is null`() {
        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onError(null)
        }
        var postTagItem = PostTagItem().copy(shop = listOf(PostTagItemShop("12314"))).apply {
            id = "10000"
            price = "50000"
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }
    }

    @Test
    fun `add post tag item response error should call onAddToCartFailed when exception is not null`() {
        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onError(Throwable())
        }
        var postTagItem = PostTagItem().copy(shop = listOf(PostTagItemShop("12314"))).apply {
            id = "10000"
            price = "50000"
        }
        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }

        mockkStatic(GlobalConfig::class)
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        presenter.addPostTagItemToCart(postTagItem)
        verify {
            view.onAddToCartFailed(postTagItem.applink)
        }
    }

    @Test
    fun `clear cache should clear dynamic feed first usecase`() {
        presenter.clearCache()

        verify {
            getDynamicFeedFirstUseCase.clearFeedFirstCache()
        }
    }

    @Test
    fun `hit click and hit impressions should be successful`() {
        presenter.doTopAdsTracker(anyString(), anyString(), anyString(), anyString(), true)

        verify {
            sendTopAdsUseCase.hitClick(anyString(), anyString(), anyString(), anyString())
        }

        presenter.doTopAdsTracker(anyString(), anyString(), anyString(), anyString(), false)

        verify {
            sendTopAdsUseCase.hitImpressions(anyString(), anyString(), anyString(), anyString())
        }
    }

    @Test
    fun `detach view should be successful`() {
        presenter.detachView()

        verify {
            getDynamicFeedFirstUseCase.unsubscribe()
            getDynamicFeedUseCase.unsubscribe()
            followKolPostGqlUseCase.unsubscribe()
            likeKolPostUseCase.unsubscribe()
            deletePostUseCase.unsubscribe()
            trackAffiliateClickUseCase.unsubscribe()
        }
    }

    override val androidContext: Context get() = mockk(relaxed = true)

    override fun onLikeKolSuccess(rowNumber: Int, action: LikeKolPostUseCase.LikeKolPostAction) {}

    override fun onLikeKolError(message: String) {}
}
