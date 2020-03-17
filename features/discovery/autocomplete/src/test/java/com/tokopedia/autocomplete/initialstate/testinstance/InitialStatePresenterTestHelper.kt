package com.tokopedia.autocomplete.initialstate.testinstance

import com.tokopedia.autocomplete.initialstate.InitialStateContract
import com.tokopedia.autocomplete.initialstate.recentsearch.DeleteRecentSearchUseCase
import com.tokopedia.autocomplete.initialstate.InitialStateUseCase
import com.tokopedia.autocomplete.initialstate.popularsearch.RefreshPopularSearchUseCase
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.spekframework.spek2.style.gherkin.FeatureBody

internal fun FeatureBody.createTestInstance() {
    val getInitialStateUseCase by memoized {
        mockk<InitialStateUseCase>(relaxed = true)
    }

    val deleteRecentSearchUseCase by memoized {
        mockk<DeleteRecentSearchUseCase>(relaxed = true)
    }

    val popularSearchUseCase by memoized {
        mockk<RefreshPopularSearchUseCase>(relaxed = true)
    }

    val userSession by memoized {
        mockk<UserSessionInterface>(relaxed = true)
    }

    val searchParameter by memoized{
        mockk<SearchParameter>(relaxed = true)
    }

    val initialStateView by memoized {
        mockk<InitialStateContract.View>(relaxed = true)
    }
}