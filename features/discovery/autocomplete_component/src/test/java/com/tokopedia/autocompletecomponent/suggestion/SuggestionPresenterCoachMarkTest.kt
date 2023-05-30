package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.searchbar.SearchBarKeyword
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.internal.schedulers.TrampolineScheduler
import rx.plugins.RxJavaPlugins
import rx.plugins.RxJavaSchedulersHook

private const val suggestionCommonResponse =
    "autocomplete/suggestion/suggestion-common-response.json"

internal class SuggestionPresenterCoachMarkTest : SuggestionPresenterTestFixtures() {

    private val keyword: String = "asus"

    init {
        setUpTestSchedulers()
        setUpAndroidTestSchedulers()
    }

    private fun setUpAndroidTestSchedulers() {
        RxAndroidPlugins.getInstance().reset()
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return TrampolineScheduler.INSTANCE
            }
        })
    }

    private fun setUpTestSchedulers() {
        RxJavaPlugins.getInstance().reset()
        RxJavaPlugins.getInstance().registerSchedulersHook(object : RxJavaSchedulersHook() {
            override fun getComputationScheduler(): Scheduler {
                return TrampolineScheduler.INSTANCE
            }

            override fun getIOScheduler(): Scheduler {
                return TrampolineScheduler.INSTANCE
            }

            override fun getNewThreadScheduler(): Scheduler {
                return TrampolineScheduler.INSTANCE
            }
        })
    }

    @Test
    fun `show suggestion CoachMark if local cache return true`() {
        `Given coachMarkLocalCache shouldShowSuggestionCoachMark return true`()
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse)

        `When presenter get suggestion data`()

        `Then verify coachMarkLocalCache shouldShowSuggestionCoachMark called`()
        `Then verify showSuggestionCoachMark is called`()
    }

    @Test
    fun `should not show suggestion CoachMark if local cache return true`() {
        `Given coachMarkLocalCache shouldShowSuggestionCoachMark return false`()
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()
        `Given Suggestion API will return SuggestionUniverse`(suggestionUniverse)

        `When presenter get suggestion data`()

        `Then verify coachMarkLocalCache shouldShowSuggestionCoachMark called`()
        `Then verify showSuggestionCoachMark is not called`()
    }

    private fun `Given coachMarkLocalCache shouldShowSuggestionCoachMark return true`() {
        every { coachMarkLocalCache.shouldShowSuggestionCoachMark() } returns true
    }

    private fun `Given coachMarkLocalCache shouldShowSuggestionCoachMark return false`() {
        every { coachMarkLocalCache.shouldShowSuggestionCoachMark() } returns false
    }

    private fun `When presenter get suggestion data`(
        searchParameter: Map<String, String> = mapOf(
            SearchApiConst.Q to keyword,
        ),
        activeKeyword: SearchBarKeyword = SearchBarKeyword(
            keyword = keyword
        ),
    ) {
        suggestionPresenter.getSuggestion(searchParameter, activeKeyword)
    }

    private fun `Then verify coachMarkLocalCache shouldShowSuggestionCoachMark called`() {
        verify {
            coachMarkLocalCache.shouldShowSuggestionCoachMark()
        }
    }

    private fun `Then verify showSuggestionCoachMark is called`() {
        verify {
            suggestionView.showSuggestionCoachMark()
        }
    }

    private fun `Then verify showSuggestionCoachMark is not called`() {
        verify(exactly = 0) {
            suggestionView.showSuggestionCoachMark()
        }
    }

    @Test
    fun `markSuggestionCoachMark success`() {
        `When markSuggestionCoachMark called`()

        `Then verify markShowSuggestionCoachMark called`()
    }

    private fun `When markSuggestionCoachMark called`() {
        suggestionPresenter.markSuggestionCoachMark()
    }

    private fun `Then verify markShowSuggestionCoachMark called`() {
        verify {
            coachMarkLocalCache.markShowSuggestionCoachMark()
        }
    }
}
