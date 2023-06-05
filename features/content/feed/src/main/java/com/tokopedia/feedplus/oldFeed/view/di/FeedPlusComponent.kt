package com.tokopedia.feedplus.oldFeed.view.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.di.FeedComponentModule
import com.tokopedia.feedcomponent.di.FeedComponentViewModelModule
import com.tokopedia.feedcomponent.di.FeedFloatingButtonManagerModule
import com.tokopedia.feedcomponent.people.di.PeopleModule
import com.tokopedia.feedcomponent.shoprecom.di.ShopRecomModule
import com.tokopedia.feedplus.oldFeed.view.fragment.FeedPlusDetailFragment
import com.tokopedia.feedplus.oldFeed.view.fragment.FeedPlusFragment
import com.tokopedia.feedplus.oldFeed.view.fragment.PlayFeedSeeMoreFragment
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.di.PlayVideoTabRepositoryModule
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by nisie on 5/15/17.
 */
@FeedPlusScope
@Component(
    modules = [
        FeedPlusModule::class,
        FeedComponentModule::class,
        ViewModelModule::class,
        PlayWidgetModule::class,
        FeedFloatingButtonManagerModule::class,
        ShopRecomModule::class,
        PeopleModule::class,
        FeedComponentViewModelModule::class,
        PlayVideoTabRepositoryModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedPlusComponent {
    @ApplicationContext
    fun context(): Context

    fun retrofitBuilder(): Retrofit.Builder
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    fun userSessionUserSessionInterface(): UserSessionInterface
    fun inject(feedPlusFragment: FeedPlusFragment)
    fun inject(feedPlusDetailFragment: FeedPlusDetailFragment)
    fun inject(feedSeeMoreFragment: PlayFeedSeeMoreFragment)
}
