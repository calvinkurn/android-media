package com.tokopedia.autocompletecomponent.searchbar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
import com.tokopedia.discovery.common.utils.MpsLocalCache
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Rule

internal open class SearchBarViewModelTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val coachMarkLocalCache: CoachMarkLocalCache = mockk()
    protected val mpsLocalCache: MpsLocalCache = mockk()
    protected lateinit var viewModel: SearchBarViewModel

    @Before
    open fun setUp() {
        viewModel = createSearchBarViewModel()
    }

    protected open fun createSearchBarViewModel(): SearchBarViewModel {
        return SearchBarViewModel(
            coachMarkLocalCache,
            mpsLocalCache,
            CoroutineTestDispatchersProvider,
        )
    }

    protected fun `Given search bar keyword list already populated`(
        keywords: List<SearchBarKeyword>
    ) {
        keywords.forEach {
            viewModel.onQueryUpdated(it.keyword)
            viewModel.onKeywordAdded(it.keyword)
        }
    }

    protected fun `Given mps is enabled`() {
        viewModel.showMps()
    }

    protected fun `Given mps enabled and no coach mark should be displayed`() {
        `Given no coach mark should be displayed`()
        `Given should animate icon plus`()
        `Given coach mark local cache mark displayed will just run`()
        `Given mps is enabled`()
    }

    protected fun `Given no coach mark should be displayed`() {
        `Given should not show added keyword coach mark`()
        `Given should not show icon plus coach mark`()
    }

    protected fun `Given coach mark local cache mark displayed will just run`() {
        every { coachMarkLocalCache.markShowPlusIconCoachMark() } just runs
        every { coachMarkLocalCache.markShowAddedKeywordCoachMark() } just runs
    }

    protected fun `Given should not show added keyword coach mark`() {
        every { coachMarkLocalCache.shouldShowAddedKeywordCoachMark() } returns false
    }

    protected fun `Given should show added keyword coach mark`() {
        every { coachMarkLocalCache.shouldShowAddedKeywordCoachMark() } returns true
    }

    protected fun `Given should not show icon plus coach mark`() {
        every { coachMarkLocalCache.shouldShowPlusIconCoachMark() } returns false
    }

    protected fun `Given should show icon plus coach mark`() {
        every { coachMarkLocalCache.shouldShowPlusIconCoachMark() } returns true
    }

    protected fun `Given should animate icon plus`() {
        every { mpsLocalCache.shouldAnimatePlusIcon() } returns true
    }

    protected fun `Given should not animate icon plus`() {
        every { mpsLocalCache.shouldAnimatePlusIcon() } returns false
    }

    protected fun `Given active keyword updated`(query: String) {
        viewModel.onQueryUpdated(query)
    }

    protected fun `Then verify SearchBarKeyword list`(expectedKeywords: List<SearchBarKeyword>) {
        viewModel.searchBarKeywords.value shouldBe expectedKeywords
    }

    protected fun `Then verify active SearchBarKeyword`(expectedKeyword: SearchBarKeyword) {
        viewModel.activeKeyword shouldBe expectedKeyword
    }

    protected fun `Then verify coach mark local cache is called`() {
        verify {
            coachMarkLocalCache.shouldShowPlusIconCoachMark()
        }
    }

    protected fun `Then verify coach mark local cache is not called`() {
        verify(exactly = 0) {
            coachMarkLocalCache.shouldShowAddedKeywordCoachMark()
            coachMarkLocalCache.shouldShowPlusIconCoachMark()
        }
    }

    protected fun `Then verify mps state`(mpsState: SearchBarState) {
        viewModel.searchBarStateLiveData.value shouldBe mpsState
    }

    protected fun `Then verify searchBarKeywordError`(expected: SearchBarKeywordError?) {
        viewModel.searchBarKeywordErrorEvent.value shouldBe expected
    }

    protected fun `Then verify search parameter`(expectedParams: Map<String, String>) {
        viewModel.searchParameterLiveData.value shouldBe expectedParams
    }

}
