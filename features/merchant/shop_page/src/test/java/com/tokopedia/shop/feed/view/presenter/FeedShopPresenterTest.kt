package com.tokopedia.shop.feed.view.presenter

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemShop
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.shop.feed.domain.DynamicFeedShopDomain
import com.tokopedia.shop.feed.domain.usecase.GetFeedShopFirstUseCase
import com.tokopedia.shop.feed.view.contract.FeedShopContract
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
            secondArg<Subscriber<DynamicFeedShopDomain>>().onError(Throwable())
            secondArg<Subscriber<DynamicFeedShopDomain>>().onNext(DynamicFeedShopDomain())
        }

        every {
            view.userSession.userId
        } returns "10000"

        presenter.getFeedFirstPage("123", false)

        verify {
            getDynamicFeedFirstUseCase.execute(any(), any())
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

        presenter.getFeedFirstPage("123", false)

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
    fun `follow kol should be successful`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onError(Throwable())
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(any(), any(), false))
        }

        presenter.followKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
        }
    }

    @Test
    fun `unfollow kol should be successful`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {
            firstArg<Subscriber<GraphqlResponse>>().onCompleted()
            firstArg<Subscriber<GraphqlResponse>>().onError(Throwable())
            firstArg<Subscriber<GraphqlResponse>>().onNext(GraphqlResponse(any(), any(), false))
        }

        presenter.unfollowKol(1231)

        verify {
            followKolPostGqlUseCase.execute(any())
        }
    }

    @Test
    fun `like kol post should be successful`() {
        every {
            likeKolPostUseCase.execute(any(), any())
        } answers {}

        presenter.likeKol(anyInt(), anyInt(), this)

        verify {
            likeKolPostUseCase.execute(any(), any())
        }

        presenter.attachView(null)
        presenter.likeKol(anyInt(), anyInt(), this)
    }

    @Test
    fun `unlike kol post should be successful`() {
        every {
            likeKolPostUseCase.execute(any(), any())
        } answers {}

        presenter.unlikeKol(anyInt(), anyInt(), this)

        verify {
            likeKolPostUseCase.execute(any(), any())
        }

        presenter.attachView(null)
        presenter.unlikeKol(anyInt(), anyInt(), this)
    }

    @Test
    fun `delete post should be successful`() {
        every {
            deletePostUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onCompleted()
            secondArg<Subscriber<Boolean>>().onError(Throwable())
            secondArg<Subscriber<Boolean>>().onNext(true)
        }

        presenter.deletePost(anyInt(), anyInt())

        verify {
            deletePostUseCase.execute(any(), any())
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

        presenter.trackPostClick(anyString(), anyString())

        verify {
            trackAffiliateClickUseCase.execute(any(), any())
        }

        every {
            view.userSession.isLoggedIn
        } returns false

        presenter.trackPostClick(anyString(), anyString())

        verify {
            trackAffiliateClickUseCase.execute(any(), any())
        }
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

        presenter.trackPostClickUrl(anyString())

        verify {
            trackAffiliateClickUseCase.execute(any(), any())
        }
    }

    @Test
    fun `add post tag item to cart should be successful`() {
        every {
            atcUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<AddToCartDataModel>>().onCompleted()
            secondArg<Subscriber<AddToCartDataModel>>().onError(Throwable())
            secondArg<Subscriber<AddToCartDataModel>>().onNext(AddToCartDataModel())
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