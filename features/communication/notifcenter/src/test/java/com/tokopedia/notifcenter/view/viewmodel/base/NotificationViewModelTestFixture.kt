package com.tokopedia.notifcenter.view.viewmodel.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.notifcenter.data.entity.bumpreminder.BumpReminderResponse
import com.tokopedia.notifcenter.data.entity.clearnotif.ClearNotifCounterResponse
import com.tokopedia.notifcenter.data.entity.deletereminder.DeleteReminderResponse
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.domain.AffiliateEducationArticleUseCase
import com.tokopedia.notifcenter.domain.ClearNotifCounterUseCase
import com.tokopedia.notifcenter.domain.GetNotificationCounterUseCase
import com.tokopedia.notifcenter.domain.MarkNotificationAsReadUseCase
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDeleteReminderBumpUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.domain.NotifcenterFilterV2UseCase
import com.tokopedia.notifcenter.domain.NotifcenterSetReminderBumpUseCase
import com.tokopedia.notifcenter.view.NotificationViewModel
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class NotificationViewModelTestFixture {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    protected lateinit var notifcenterDetailUseCase: NotifcenterDetailUseCase

    @RelaxedMockK
    protected lateinit var notifcenterFilterUseCase: NotifcenterFilterV2UseCase

    @RelaxedMockK
    protected lateinit var clearNotifUseCase: ClearNotifCounterUseCase

    @RelaxedMockK
    protected lateinit var bumpReminderUseCase: NotifcenterSetReminderBumpUseCase

    @RelaxedMockK
    protected lateinit var deleteReminderUseCase: NotifcenterDeleteReminderBumpUseCase

    @RelaxedMockK
    protected lateinit var markAsReadUseCase: MarkNotificationAsReadUseCase

    @RelaxedMockK
    protected lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @RelaxedMockK
    protected lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    protected lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    protected lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    protected lateinit var notifOrderListUseCase: NotifOrderListUseCase

    @RelaxedMockK
    protected lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    protected lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    protected lateinit var affiliateEducationArticleUseCase: AffiliateEducationArticleUseCase

    @RelaxedMockK
    protected lateinit var getNotificationCounterUseCase: GetNotificationCounterUseCase

    private val dispatcher = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: NotificationViewModel

    @Before
    open fun setup() {
        MockKAnnotations.init(this)
        viewModel = NotificationViewModel(
            notifcenterDetailUseCase,
            notifcenterFilterUseCase,
            bumpReminderUseCase,
            deleteReminderUseCase,
            clearNotifUseCase,
            markAsReadUseCase,
            topAdsImageViewUseCase,
            getRecommendationUseCase,
            addToWishlistV2UseCase,
            deleteWishlistV2UseCase,
            userSessionInterface,
            addToCartUseCase,
            notifOrderListUseCase,
            affiliateEducationArticleUseCase,
            getNotificationCounterUseCase,
            dispatcher
        )
    }

    @After
    fun finish() {
        unmockkAll()
        Dispatchers.resetMain()
        viewModel.cancelAllUseCase()
    }

    companion object {
        val notifCenterDetailResponse: NotifcenterDetailResponse = FileUtil.parse(
            "/success_notifcenter_detail_v3.json",
            NotifcenterDetailResponse::class.java
        )

        val notifCenterFilterResponse: NotifcenterFilterResponse = FileUtil.parse(
            "/success_notifcenter_filter_as_buyer.json",
            NotifcenterFilterResponse::class.java
        )

        val productRecommResponse: RecommendationEntity = FileUtil.parse(
            "/success_notifcenter_recomm_inbox.json",
            RecommendationEntity::class.java
        )

        val bumpReminderResponse: BumpReminderResponse = FileUtil.parse(
            "/success_notifcenter_bump_reminder.json",
            BumpReminderResponse::class.java
        )

        val deleteReminderResponse: DeleteReminderResponse = FileUtil.parse(
            "/success_notifcenter_delete_reminder.json",
            DeleteReminderResponse::class.java
        )

        val notifOrderListResponse = NotifOrderListResponse()
        val clearNotifCounterResponse = ClearNotifCounterResponse()
    }
}
