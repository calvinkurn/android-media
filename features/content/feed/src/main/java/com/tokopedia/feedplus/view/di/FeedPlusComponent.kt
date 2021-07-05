package com.tokopedia.feedplus.view.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.di.FeedComponentModule
import com.tokopedia.feedplus.view.fragment.DynamicFeedFragment
import com.tokopedia.feedplus.view.fragment.FeedOnboardingFragment
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment
import com.tokopedia.interest_pick_common.di.InterestPickCommonModule
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by nisie on 5/15/17.
 */
@FeedPlusScope
@Component(modules = [FeedPlusModule::class, FeedComponentModule::class,
    ViewModelModule::class, InterestPickCommonModule::class, PlayWidgetModule::class],
        dependencies = [BaseAppComponent::class])
interface FeedPlusComponent {
    @ApplicationContext
    fun context(): Context

    fun retrofitBuilder(): Retrofit.Builder
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    fun userSessionUserSessionInterface(): UserSessionInterface
    fun inject(feedPlusFragment: FeedPlusFragment)
    fun inject(feedPlusDetailFragment: FeedPlusDetailFragment)
    fun inject(dynamicFeedFragment: DynamicFeedFragment)
    fun inject(feedOnboardingFragment: FeedOnboardingFragment)
}