package com.tokopedia.play.widget.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.play.widget.pref.PlayWidgetPreference
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 09/11/20
 */
@Module
class PlayWidgetModule {

    @Provides
    fun providePlayWidgetUpdateChannelUseCase(graphqlRepository: GraphqlRepository): PlayWidgetUpdateChannelUseCase {
        return PlayWidgetUpdateChannelUseCase(graphqlRepository)
    }

    @Provides
    fun providePlayWidgetPreference(@ApplicationContext context: Context): PlayWidgetPreference {
        return PlayWidgetPreference(context)
    }
}
