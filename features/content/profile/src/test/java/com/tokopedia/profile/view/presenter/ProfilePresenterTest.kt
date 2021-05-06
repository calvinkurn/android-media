package com.tokopedia.profile.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemShop
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.model.commission.AffiliatedProductByProductIDs
import com.tokopedia.feedcomponent.domain.model.statistic.FeedGetStatsPosts
import com.tokopedia.feedcomponent.domain.usecase.GetPostStatisticCommissionUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetRelatedPostUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.follow.FollowKol
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolData
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileFirstUseCase
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileUseCase
import com.tokopedia.profile.domain.usecase.ShouldChangeUsernameUseCase
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.subscriber.FollowSubscriber
import com.tokopedia.profile.view.viewmodel.DynamicFeedProfileViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Subscriber
import rx.observers.TestSubscriber
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProfilePresenterTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getDynamicFeedProfileFirstUseCase: GetDynamicFeedProfileFirstUseCase

    @RelaxedMockK
    lateinit var getDynamicFeedProfileUseCase: GetDynamicFeedProfileUseCase

    @RelaxedMockK
    lateinit var likeKolPostUseCase: LikeKolPostUseCase

    @RelaxedMockK
    lateinit var followKolPostGqlUseCase: FollowKolPostGqlUseCase

    @RelaxedMockK
    lateinit var deletePostUseCase: DeletePostUseCase

    @RelaxedMockK
    lateinit var trackAffiliateClickUseCase: TrackAffiliateClickUseCase

    @RelaxedMockK
    lateinit var shouldChangeUsernameUseCase: ShouldChangeUsernameUseCase

    @RelaxedMockK
    lateinit var getRelatedPostUseCase: GetRelatedPostUseCase

    @RelaxedMockK
    lateinit var atcUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var getPostStatisticCommissionUseCase: GetPostStatisticCommissionUseCase

    @RelaxedMockK
    lateinit var sendTopAdsUseCase: SendTopAdsUseCase

    @RelaxedMockK
    lateinit var view: ProfileContract.View

    @RelaxedMockK
    lateinit var likeListener: KolPostLikeListener

    private val presenter: ProfilePresenter by lazy {
        ProfilePresenter(getDynamicFeedProfileFirstUseCase, getDynamicFeedProfileUseCase,
                likeKolPostUseCase, followKolPostGqlUseCase, deletePostUseCase,
                trackAffiliateClickUseCase, shouldChangeUsernameUseCase, getRelatedPostUseCase,
                atcUseCase, getPostStatisticCommissionUseCase, sendTopAdsUseCase)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `test detachView method`() {
        presenter.detachView()
        verify { getDynamicFeedProfileFirstUseCase.unsubscribe() }
        verify { getDynamicFeedProfileUseCase.unsubscribe() }
        verify { likeKolPostUseCase.unsubscribe() }
        verify { followKolPostGqlUseCase.unsubscribe() }
        verify { deletePostUseCase.unsubscribe() }
        verify { trackAffiliateClickUseCase.unsubscribe() }
        verify { shouldChangeUsernameUseCase.unsubscribe() }
        verify { getRelatedPostUseCase.unsubscribe() }
        verify { atcUseCase.unsubscribe() }
    }

    @Test
    fun `test getProfileFirstPage method for exception`() {
        val slot = slot<Subscriber<DynamicFeedProfileViewModel>>()
        every {
            getDynamicFeedProfileFirstUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.getProfileFirstPage(0, false)
        verify { view.showGetListError(any()) }
        verify { view.hideHeader() }
    }

    @Test
    fun `test getProfileFirstPage method for success`() {
        val dummyResponseModel = DynamicFeedProfileViewModel()
        val slot = slot<Subscriber<DynamicFeedProfileViewModel>>()
        every {
            getDynamicFeedProfileFirstUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponseModel)
        }
        presenter.getProfileFirstPage(0, false)
        verify { view.onSuccessGetProfileFirstPage(any(), any()) }
    }

    @Test
    fun `test getProfilePost method for exception`() {
        val slot = slot<Subscriber<DynamicFeedDomainModel>>()
        every {
            getDynamicFeedProfileUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.getProfilePost(0)
        verify { view.showGetListError(any()) }
    }

    @Test
    fun `test getProfilePost method for success with null data`() {
        val dummyNullResponse: DynamicFeedDomainModel? = null
        val slot = slot<Subscriber<DynamicFeedDomainModel>>()
        every {
            getDynamicFeedProfileUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyNullResponse)
        }
        presenter.getProfilePost(0)
        verify { view.showGetListError(any()) }
    }

    @Test
    fun `test getProfilePost method for success with data`() {
        val dummyResponse = DynamicFeedDomainModel()
        val slot = slot<Subscriber<DynamicFeedDomainModel>>()
        every {
            getDynamicFeedProfileUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.getProfilePost(0)
        verify { view.onSuccessGetProfilePost(any(), any()) }
    }

    @Test
    fun `test followKol method for exception`() {
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            followKolPostGqlUseCase.execute(capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.followKol(0, true)
        verify { view.onErrorFollowKol(any()) }
    }

    @Test
    fun `test followKol method for success with null data`() {
        val dummyNullResponse: FollowKolQuery? = null
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = FollowKolQuery::class.java
        result[objectType] = dummyNullResponse
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            followKolPostGqlUseCase.execute(capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.followKol(0, true)
        verify { view.onErrorFollowKol(any()) }
    }

    @Test
    fun `test followKol method for success with error in data`() {
        val dummyResponse = FollowKolQuery().apply {
            data = FollowKol().apply {
                error = "dummyError"
            }
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = FollowKolQuery::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            followKolPostGqlUseCase.execute(capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.followKol(0, true)
        verify { view.onErrorFollowKol(any()) }
    }

    @Test
    fun `test followKol method for success with success in response`() {
        val dummyResponse = FollowKolQuery().apply {
            data = FollowKol().apply {
                data = FollowKolData().apply {
                    status = FollowSubscriber.FOLLOW_SUCCESS
                }
                error = ""
            }
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = FollowKolQuery::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            followKolPostGqlUseCase.execute(capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.followKol(0, true)
        verify { view.onSuccessFollowKol() }
    }

    @Test
    fun `test followKol method for success with failure in response`() {
        val dummyResponse = FollowKolQuery().apply {
            data = FollowKol().apply {
                data = FollowKolData()
                error = ""
            }
        }
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = FollowKolQuery::class.java
        result[objectType] = dummyResponse
        val slot = slot<Subscriber<GraphqlResponse>>()
        every {
            followKolPostGqlUseCase.execute(capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(GraphqlResponse(result, errors, false))
        }
        presenter.followKol(0, true)
        verify { view.onErrorFollowKol(any()) }
    }

    @Test
    fun `test likeKol method for exception`() {
        val slot = slot<Subscriber<Boolean>>()
        every {
            likeKolPostUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.likeKol(0, 0, likeListener, false)
        verify { likeListener.onLikeKolError(any()) }
    }

    @Test
    fun `test likeKol method for success with failure`() {
        val dummyResponse = false
        val slot = slot<Subscriber<Boolean>>()
        every {
            likeKolPostUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.likeKol(0, 0, likeListener, false)
        verify { likeListener.onLikeKolError(any()) }
    }

    @Test
    fun `test likeKol method for success`() {
        val dummyResponse = true
        val slot = slot<Subscriber<Boolean>>()
        every {
            likeKolPostUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.likeKol(0, 0, likeListener, false)
        verify { likeListener.onLikeKolSuccess(any(), any()) }
    }

    @Test
    fun `test addPostTagItemToCart method for exception`() {
        val postTagItem = PostTagItem(shop = listOf(PostTagItemShop(shopId = "0")), id = "0")
        val slot = slot<Subscriber<AddToCartDataModel>>()
        every {
            atcUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.addPostTagItemToCart(postTagItem)
        verify { view.onAddToCartFailed(any()) }

        val emptyPostTagItem = PostTagItem(shop = listOf())
        val slot2 = slot<Subscriber<AddToCartDataModel>>()
        every {
            atcUseCase.execute(any(), capture(slot2))
        } answers {
            val ans = slot2.captured
            ans.onError(Throwable())
        }
        presenter.addPostTagItemToCart(emptyPostTagItem)
        verify { view.onAddToCartFailed(any()) }
    }

    @Test
    fun `test addPostTagItemToCart method for success with failure`() {
        val postTagItem = PostTagItem(shop = listOf(PostTagItemShop(shopId = "0")), id = "0")
        val dummyResponse = AddToCartDataModel().apply {
            data = DataModel()
        }
        val slot = slot<Subscriber<AddToCartDataModel>>()
        every {
            atcUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.addPostTagItemToCart(postTagItem)
        verify { view.onAddToCartFailed(any()) }
    }

    @Test
    fun `test addPostTagItemToCart method for success`() {
        val postTagItem = PostTagItem(shop = listOf(PostTagItemShop(shopId = "0")), id = "0")
        val dummyResponse = AddToCartDataModel().apply {
            data = DataModel(success = 1)
        }
        val slot = slot<Subscriber<AddToCartDataModel>>()
        every {
            atcUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.addPostTagItemToCart(postTagItem)
        verify { view.onAddToCartSuccess() }
    }


    @Test
    fun `test deletePost method for exception`() {
        val dummyId = 0
        val dummyRowNumber = 0
        val slot = slot<Subscriber<Boolean>>()
        every {
            deletePostUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.deletePost(dummyId, dummyRowNumber)
        verify { view.onErrorDeletePost(any(), dummyId, dummyRowNumber) }
    }

    @Test
    fun `test deletePost method for success with failure`() {
        val dummyId = 0
        val dummyRowNumber = 0
        val dummyResponse = false
        val slot = slot<Subscriber<Boolean>>()
        every {
            deletePostUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.deletePost(0, 0)
        verify { view.onErrorDeletePost(any(), dummyId, dummyRowNumber) }
    }

    @Test
    fun `test deletePost method for success`() {
        val dummyResponse = true
        val slot = slot<Subscriber<Boolean>>()
        every {
            deletePostUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.deletePost(0, 0)
        verify { view.onSuccessDeletePost(0) }
    }

    @Test
    fun `test trackPostClick method for exception`() {
        val dummyException = Exception("dummyException")
        val slot = slot<Subscriber<Boolean>>()
        every {
            trackAffiliateClickUseCase.execute(any())
        } answers {
            val ans = slot.captured
            ans.onError(dummyException)
        }
        presenter.trackPostClick("", "")
        if (GlobalConfig.isAllowDebuggingTools()) {
            verify { dummyException.printStackTrace() }
        }
    }

    @Test
    fun `test trackPostClickUrl method for exception`() {
        val dummyException = Exception("dummyException")
        val slot = slot<Subscriber<Boolean>>()
        every {
            trackAffiliateClickUseCase.execute(any())
        } answers {
            val ans = slot.captured
            ans.onError(dummyException)
        }
        presenter.trackPostClickUrl("")
        if (GlobalConfig.isAllowDebuggingTools()) {
            verify { dummyException.printStackTrace() }
        }
    }

    @Test
    fun `test getRelatedProfile method for exception`() {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<FeedPostRelated> = TestSubscriber()
        val onErrorGetRelatedProfile: ((throwable: Throwable?) -> Unit)? = {}
        val onSuccessGetRelatedProfile: ((feedPostRelated: FeedPostRelated?) -> Unit)? = {}

        every {
            getRelatedPostUseCase.execute(any(), any())
        } answers {
            testSubscriber.onError(expectedReturn)
            testSubscriber.onCompleted()
        }

        presenter.getRelatedProfile(onErrorGetRelatedProfile, onSuccessGetRelatedProfile)
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `test getRelatedProfile method for success`() {
        val expectedReturn = mockk<FeedPostRelated>(relaxed = true)
        val testSubscriber: TestSubscriber<FeedPostRelated> = TestSubscriber()
        val onErrorGetRelatedProfile: ((throwable: Throwable?) -> Unit)? = {}
        val onSuccessGetRelatedProfile: ((feedPostRelated: FeedPostRelated?) -> Unit)? = {}

        every {
            getRelatedPostUseCase.execute(any(), any())
        } answers {
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.getRelatedProfile(onErrorGetRelatedProfile, onSuccessGetRelatedProfile)
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `test shouldChangeUsername method for exception`() {
        val slot = slot<Subscriber<Boolean>>()
        every {
            shouldChangeUsernameUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onError(Throwable())
        }
        presenter.shouldChangeUsername(0, "")
        verify { view.onErrorShouldChangeUsername(any(), "") }
    }

    @Test
    fun `test shouldChangeUsername method for success`() {
        val dummyResponse = false
        val slot = slot<Subscriber<Boolean>>()
        every {
            shouldChangeUsernameUseCase.execute(any(), capture(slot))
        } answers {
            val ans = slot.captured
            ans.onNext(dummyResponse)
        }
        presenter.shouldChangeUsername(0, "")
        verify { view.onSuccessShouldChangeUsername(dummyResponse, "") }
    }

    @Test
    fun `test getPostStatistic method for exception`() {
        val dummyException = Exception()
        val dummyActivityId = ""
        val dummyLikeCount = 0
        val dummyCommentCount = 0
        val dummyProductIDs = emptyList<String>()
        every {
            getPostStatisticCommissionUseCase.execute(any(), captureLambda())
        } answers {
            val onError = lambda<(Exception) -> Unit>()
            onError.invoke(dummyException)
        }
        presenter.getPostStatistic(dummyActivityId, dummyProductIDs, dummyLikeCount, dummyCommentCount)
        verify { view.onErrorGetPostStatistic(dummyException, dummyActivityId, dummyProductIDs) }
    }

    @Test
    fun `test getPostStatistic method for success`() {
        val dummyActivityId = ""
        val dummyLikeCount = 0
        val dummyCommentCount = 0
        val dummyProductIDs = emptyList<String>()
        val dummyResponse = Pair(FeedGetStatsPosts(), AffiliatedProductByProductIDs())
        every {
            getPostStatisticCommissionUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(Pair<FeedGetStatsPosts, AffiliatedProductByProductIDs>) -> Unit>()
            onSuccess.invoke(dummyResponse)
        }
        presenter.getPostStatistic(dummyActivityId, dummyProductIDs, dummyLikeCount, dummyCommentCount)
        verify { view.onSuccessGetPostStatistic(any()) }
    }

    @Test
    fun `test doTopAdsTracker method`() {
        presenter.doTopAdsTracker("", "", "", "", true)
        verify { sendTopAdsUseCase.hitClick("", "", "", "") }
        presenter.doTopAdsTracker("", "", "", "", false)
        verify { sendTopAdsUseCase.hitImpressions("", "", "", "") }
    }

}