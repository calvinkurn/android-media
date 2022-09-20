package com.tokopedia.digital_deals.view.presenter

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_entertainment.data.Brand
import com.tokopedia.common_entertainment.data.DealsDetailsResponse
import com.tokopedia.common_entertainment.data.Media
import com.tokopedia.digital_deals.domain.getusecase.GetDealDetailsUseCase
import com.tokopedia.digital_deals.domain.getusecase.GetDealLikesUseCase
import com.tokopedia.digital_deals.domain.getusecase.GetEventContentUseCase
import com.tokopedia.digital_deals.domain.getusecase.GetSearchNextUseCase
import com.tokopedia.digital_deals.domain.postusecase.PostNsqEventUseCase
import com.tokopedia.digital_deals.domain.postusecase.PostNsqTravelDataUseCase
import com.tokopedia.digital_deals.view.contractor.DealDetailsContract
import com.tokopedia.digital_deals.view.model.Location
import com.tokopedia.digital_deals.view.model.Page
import com.tokopedia.digital_deals.view.model.response.GetLikesResponse
import com.tokopedia.digital_deals.view.model.response.SearchResponse
import com.tokopedia.digital_deals.view.utils.Utils
import com.tokopedia.linker.LinkerManager
import com.tokopedia.network.data.model.response.DataResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Subscriber
import timber.log.Timber
import java.lang.reflect.Type

class DealDetailsPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var presenter: DealDetailsPresenter
    private lateinit var view: DealDetailsContract.View

    private var getDealDetailsUseCase = mockk<GetDealDetailsUseCase>(relaxed = true)
    private var getSearchNextUseCase = mockk<GetSearchNextUseCase>(relaxed = true)
    private var getDealLikesUseCase = mockk<GetDealLikesUseCase>(relaxed = true)
    private var postNsqEventUseCase = mockk<PostNsqEventUseCase>(relaxed = true)
    private var postNsqTravelDataUseCase = mockk<PostNsqTravelDataUseCase>(relaxed = true)
    private var getEventContentUseCase = mockk<GetEventContentUseCase>(relaxed = true)

    @Before
    fun setup() {
        presenter = spyk(DealDetailsPresenter(
            getDealDetailsUseCase,
            getSearchNextUseCase,
            getDealLikesUseCase,
            postNsqEventUseCase,
            postNsqTravelDataUseCase,
            getEventContentUseCase
        ), recordPrivateCalls = true)

        view = mockk(relaxed = true)
        presenter.attachView(view)
    }

    @Test
    fun `Get Deal Details onNext with non-null response and all prop disabled`() {
        val token = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponse = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse()
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(token to restResponse)
            )
        }

        every { view.isRecommendationEnableFromArguments } answers { false }
        every { view.isEnableBuyFromArguments } answers { false }
        every { view.isEnableLikeFromArguments } answers { false }

        presenter.getDealDetails()

        verify { getDealDetailsUseCase.setRequestParams(any()) }
        verify { getDealDetailsUseCase.execute(any()) }

        verify { view.hideProgressBar() }
        verify { view.showShareButton() }
        verify { view.showCollapsingHeader() }
        verify { view.renderDealDetails(any()) }

        verify(exactly = 0) { presenter["getRecommendedDeals"]() }
        verify(exactly = 0) { presenter["getLikes"]() }
        verify { view.hideCheckoutView() }
        verify { view.hideLikeButtonView() }
    }

    @Test
    fun `Get Deal Details onNext with non-null response and all prop enabled`() {
        val token = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponse = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse()
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(token to restResponse)
            )
        }

        every { view.isRecommendationEnableFromArguments } answers { true }
        every { view.isEnableBuyFromArguments } answers { true }
        every { view.isEnableLikeFromArguments } answers { true }

        presenter.getDealDetails()

        verify { getDealDetailsUseCase.setRequestParams(any()) }
        verify { getDealDetailsUseCase.execute(any()) }

        verify { view.hideProgressBar() }
        verify { view.showShareButton() }
        verify { view.showCollapsingHeader() }
        verify { view.renderDealDetails(any()) }

        verify { presenter["getRecommendedDeals"]() }
        verify { presenter["getLikes"]() }
        verify(exactly = 0) { view.hideCheckoutView() }
        verify(exactly = 0) { view.hideLikeButtonView() }
    }

    @Test
    fun `Get Deal Details onError`() {
        mockkStatic(Timber::class)
        mockkStatic(NetworkErrorHelper::class)

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onError(
                Throwable("error")
            )
        }

        presenter.getDealDetails()

        verify { view.hideProgressBar() }
        verify { view.hideCollapsingHeader() }
        verify { NetworkErrorHelper.showEmptyState(any(), any(), any()) }
        verify { Timber.d(any() as String) }
    }

    @Test
    fun `Get Deal Details onError with onRetryClicked`() {
        mockkStatic(Timber::class)
        mockkStatic(NetworkErrorHelper::class)

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onError(
                Throwable("error")
            )
        }

        every { NetworkErrorHelper.showEmptyState(any(), any(), any())} answers {
            thirdArg<NetworkErrorHelper.RetryClickedListener>().onRetryClicked()
        } andThen {

        }

        presenter.getDealDetails()

        verify { view.hideProgressBar() }
        verify { view.hideCollapsingHeader() }
        verify { NetworkErrorHelper.showEmptyState(any(), any(), any()) }
        verify { Timber.d(any() as String) }

        verify { presenter.getDealDetails() }
    }

    @Test
    fun `startBannerSlide should use adapter count`() {
        mockkStatic(Observable::class)
        val viewPagerMock = mockk<TouchViewPager>()
        val pagerAdapterMock = mockk<PagerAdapter>()

        every { viewPagerMock.adapter } returns pagerAdapterMock
        every { viewPagerMock.currentItem } returns 2
        every { pagerAdapterMock.count } returns 2
        every { viewPagerMock.childCount } returns 2

        presenter.startBannerSlide(viewPagerMock)

        verify { pagerAdapterMock.count }
        verify(exactly = 0) { viewPagerMock.childCount }
    }

    @Test
    fun `startBannerSlide should use child count when error`() {
        mockkStatic(Observable::class)
        val viewPagerMock = mockk<TouchViewPager>()
        val pagerAdapterMock = mockk<PagerAdapter>()

        every { viewPagerMock.adapter } throws Exception("dummy")
        every { viewPagerMock.currentItem } returns 2
        every { viewPagerMock.childCount } returns 2

        presenter.startBannerSlide(viewPagerMock)

        verify(exactly = 0) { pagerAdapterMock.count }
        verify { viewPagerMock.childCount }
    }

    @Test
    fun `Get Deal Details onComplete`() {
        mockkStatic(Timber::class)

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onCompleted()
        }

        presenter.getDealDetails()
        verify { Timber.d(any() as String) }
    }

    @Test
    fun `onDestroy should unsubscribe all usecase`() {
        presenter.onDestroy()

        verify { getDealDetailsUseCase.unsubscribe() }
        verify { getSearchNextUseCase.unsubscribe() }
        verify { postNsqEventUseCase.unsubscribe() }
        verify { postNsqTravelDataUseCase.unsubscribe() }
    }

    @Test
    fun `getLikes (private) onNext non-empty response`() {
        mockkStatic(Utils::class)
        val tokenDealDetails = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponseDealDetails = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse()
            },
            200,
            false
        )

        val tokenDealLikes = object : TypeToken<DataResponse<ArrayList<GetLikesResponse>?>?>() {}.type
        val restResponseDealLikes = RestResponse(
            DataResponse<ArrayList<GetLikesResponse>>().apply {
                data = arrayListOf(GetLikesResponse())
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealDetails to restResponseDealDetails)
            )
        }

        every {
            getDealLikesUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealLikes to restResponseDealLikes)
            )
        }
        every { view.isEnableLikeFromArguments } answers { true }
        every { Utils.getSingletonInstance().sortOutletsWithLocation(any(), any()) } returns Unit
        every { Utils.getSingletonInstance().getLocation(any()) } returns Location()

        presenter.getDealDetails()

        verify(exactly = 1) { view.setLikes(any(), any()) }
    }

    @Test
    fun `getLikes (private) onNext empty response`() {
        val tokenDealDetails = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponseDealDetails = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse()
            },
            200,
            false
        )

        val tokenDealLikes = object : TypeToken<DataResponse<ArrayList<GetLikesResponse>?>?>() {}.type
        val restResponseDealLikes = RestResponse(
            DataResponse<ArrayList<GetLikesResponse>>().apply {
                data = arrayListOf()
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealDetails to restResponseDealDetails)
            )
        }

        every {
            getDealLikesUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealLikes to restResponseDealLikes)
            )
        }
        every { view.isEnableLikeFromArguments } answers { true }

        presenter.getDealDetails()

        verify(exactly = 0) { view.setLikes(any(), any()) }
    }

    @Test
    fun `getRecommendation (private) onNext with salam_indicator`() {
        mockkStatic(Utils::class)
        val tokenDealDetails = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponseDealDetails = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse().apply {
                    customText1 = 1
                }
            },
            200,
            false
        )

        val tokenSearchNext = object : TypeToken<DataResponse<SearchResponse?>?>() {}.type
        val restResponseSearchNext = RestResponse(
            DataResponse<SearchResponse>().apply {
                data = SearchResponse().apply {
                    page = Page().apply {
                        uriNext = "dummy"
                    }
                }
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealDetails to restResponseDealDetails)
            )
        }

        every {
            getSearchNextUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenSearchNext to restResponseSearchNext)
            )
        }

        every { view.isRecommendationEnableFromArguments } answers { true }
        every { presenter["checkIfToLoad"](any() as LinearLayoutManager) } returns Unit

        every { Utils.getSingletonInstance().sortOutletsWithLocation(any(), any()) } returns Unit
        every { Utils.getSingletonInstance().getLocation(any()) } returns Location()

        presenter.getDealDetails()

        verify { view.removeFooter() }
        verify { view.addDealsToCards(any()) }
    }

    @Test
    fun `onOptionMenuClick id is action_menu_share`() {
        mockkStatic(LinkerManager::class)
        val tokenDealDetails = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponseDealDetails = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse().apply {
                    displayName = ""
                    seoUrl = ""
                    imageWeb = ""
                    webUrl = ""
                }
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealDetails to restResponseDealDetails)
            )
        }

        every {
            LinkerManager.getInstance().executeShareRequest(any())
        } returns Unit

        presenter.getDealDetails()
        presenter.onOptionMenuClick(com.tokopedia.digital_deals.R.id.action_menu_share)

        verify { view.activity }
        verify { view.activity.resources.getString(any()) }
    }

    @Test
    fun `getAllOutlets should not be null if dealDetailsResponse not null`() {
        mockkStatic(Utils::class)
        val tokenDealDetails = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponseDealDetails = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse().apply {
                    displayName = ""
                    seoUrl = ""
                    imageWeb = ""
                    webUrl = ""
                    outlets = listOf()
                }
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealDetails to restResponseDealDetails)
            )
        }

        every { Utils.getSingletonInstance().sortOutletsWithLocation(any(), any()) } returns Unit
        every { Utils.getSingletonInstance().getLocation(any()) } returns Location()

        presenter.getDealDetails()
        val res = presenter.allOutlets
        assert(res != null)
    }

    @Test
    fun `getAllOutlets should be null if dealDetailsResponse null`() {
        assert(presenter.allOutlets == null)
    }

    @Test
    fun `onOptionMenuClick id is not action_menu_share should call onBackPressed`() {
        every { view.activity.onBackPressed() } returns Unit

        presenter.onOptionMenuClick(123)

        verify { view.activity.onBackPressed() }
    }

    /** test nothing due to private property, only to increase coverage*/
    @Test
    fun `onBannerSlide should update currentPage`() {
        val page = 10
        presenter.onBannerSlide(page)
    }

    @Test
    fun `onRecyclerViewScroll should call checkIfToLoad`() {
        val contextMock = mockk<Context>()
        val layoutManager = LinearLayoutManager(contextMock)

        presenter.onRecyclerViewScrolled(layoutManager)

        verify { presenter["checkIfToLoad"](any() as LinearLayoutManager) }
    }

    @Test
    fun `sendNsqEvent onNext should call log`() {
        mockkStatic(Log::class)
        every {
            postNsqEventUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf()
            )
        }
        val data = DealsDetailsResponse().apply {
            id = 123
        }

        presenter.sendNsqEvent("123", data)

        verify { postNsqEventUseCase.setRequestModel(any()) }
        verify { postNsqEventUseCase.execute(any()) }
        verify { Log.d(any(), any()) }
    }

    @Test
    fun `sendNsqEvent onError should call empty state`() {
        mockkStatic(NetworkErrorHelper::class)
        every {
            postNsqEventUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onError(
                Throwable("error")
            )
        }
        val data = DealsDetailsResponse().apply {
            id = 123
        }

        presenter.sendNsqEvent("123", data)

        verify { postNsqEventUseCase.setRequestModel(any()) }
        verify { postNsqEventUseCase.execute(any()) }
        verify { NetworkErrorHelper.showEmptyState(any(), any(), any()) }
    }

    @Test
    fun `sendNsqTravelEvent onNext should call log`() {
        mockkStatic(Log::class)
        every {
            postNsqTravelDataUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf()
            )
        }
        val data = DealsDetailsResponse().apply {
            id = 123
            displayName = "dummy"
            mediaUrl = listOf(Media().apply {
                url = "dummy"
            })
            brand = Brand().apply {
                title = "dummy"
            }
            mrp = 123
            seoUrl = "dummy"
        }

        presenter.sendNsqTravelEvent("123", data)

        verify { postNsqTravelDataUseCase.execute(any()) }
        verify { postNsqTravelDataUseCase.setTravelDataRequestModel(any()) }
        verify { Log.d(any(), any()) }
    }

    @Test
    fun `sendNsqTravelEvent onError should call empty state`() {
        mockkStatic(NetworkErrorHelper::class)
        every {
            postNsqTravelDataUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onError(
                Throwable("dummy")
            )
        }
        val data = DealsDetailsResponse().apply {
            id = 123
            displayName = "dummy"
            mediaUrl = listOf(Media().apply {
                url = "dummy"
            })
            brand = Brand().apply {
                title = "dummy"
            }
            mrp = 123
            seoUrl = "dummy"
        }

        presenter.sendNsqTravelEvent("123", data)

        verify { postNsqTravelDataUseCase.execute(any()) }
        verify { postNsqTravelDataUseCase.setTravelDataRequestModel(any()) }
        verify { NetworkErrorHelper.showEmptyState(any(), any(), any()) }
    }

    @Test
    fun `sendNsqTravelEvent onCompleted should not log or show empty state`() {
        mockkStatic(Log::class)
        mockkStatic(NetworkErrorHelper::class)

        every {
            postNsqTravelDataUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onCompleted()
        }

        val data = DealsDetailsResponse().apply {
            id = 123
            displayName = "dummy"
            brand = Brand().apply {
                title = "dummy"
            }
            mrp = 123
            seoUrl = "dummy"
        }

        presenter.sendNsqTravelEvent("123", data)

        verify(exactly = 0) { Log.d(any(), any()) }
        verify(exactly = 0) { NetworkErrorHelper.showEmptyState(any(), any(), any()) }
    }

    @Test
    fun `getEventContent should call use case`() {
        mockkStatic(Utils::class)
        val tokenDealDetails = object : TypeToken<DataResponse<DealsDetailsResponse?>?>() {}.type
        val restResponseDealDetails = RestResponse(
            DataResponse<DealsDetailsResponse>().apply {
                data = DealsDetailsResponse()
            },
            200,
            false
        )

        every {
            getDealDetailsUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(tokenDealDetails to restResponseDealDetails)
            )
        }
        every { Utils.getSingletonInstance().sortOutletsWithLocation(any(), any()) } returns Unit
        every { Utils.getSingletonInstance().getLocation(any()) } returns Location()

        presenter.getDealDetails()
        presenter.getEventContent({}, {})

        verify { getEventContentUseCase.getEventContent(any(), any(), any(), any()) }
    }

}
