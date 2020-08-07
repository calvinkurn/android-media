package com.tokopedia.search.result.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.search.result.TestDispatcherProvider
import com.tokopedia.search.result.presentation.model.ChildViewVisibilityChangedModel
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.shouldBe
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

internal class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val searchViewModel = SearchViewModel(TestDispatcherProvider())

    @Test
    fun `handle show auto complete view`() {
        `When show auto complete view`()

        `Then validate show auto complete view event`()
    }

    private fun `When show auto complete view`() {
        searchViewModel.showAutoCompleteView()
    }

    private fun `Then validate show auto complete view event`() {
        val autoCompleteEvent = searchViewModel.getShowAutoCompleteViewEventLiveData().value

        autoCompleteEvent?.getContentIfNotHandled() shouldBe true
    }

    @Test
    fun `handle hide search page loading`() {
        `When hide search page loading`()

        `Then validate hide search page loading event`()
    }

    private fun `When hide search page loading`() {
        searchViewModel.hideSearchPageLoading()
    }

    private fun `Then validate hide search page loading event`() {
        val hideLoadingEvent = searchViewModel.getHideLoadingEventLiveData().value

        hideLoadingEvent?.getContentIfNotHandled() shouldBe true
    }

    @Test
    fun `handle shop view visibilty changed to visible`() {
        val productSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
        val shopSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()

        val productListVisibilityModel = ChildViewVisibilityChangedModel(
                isChildViewVisibleToUser = false,
                isChildViewReady = true,
                isFilterEnabled = true,
                isSortEnabled = true,
                searchNavigationOnClickListener = productSearchNavigationOnClick
        )
        val shopListVisibilityModel = ChildViewVisibilityChangedModel(
                isChildViewVisibleToUser = true,
                isChildViewReady = true,
                isFilterEnabled = true,
                isSortEnabled = false,
                searchNavigationOnClickListener = shopSearchNavigationOnClick
        )

        `When handle child views visibility changed`(
                productListVisibilityModel,
                shopListVisibilityModel
        )

        `Then verify event for visible child view`(shopListVisibilityModel)
    }

    private fun `When handle child views visibility changed`(
            productListVisibilityModel: ChildViewVisibilityChangedModel,
            shopListVisibilityModel: ChildViewVisibilityChangedModel
    ) {
        searchViewModel.onChildViewVisibilityChanged(productListVisibilityModel)
        searchViewModel.onChildViewVisibilityChanged(shopListVisibilityModel)
    }

    private fun `Then verify event for visible child view`(childViewVisibilityChangedModel: ChildViewVisibilityChangedModel?) {
        val childViewVisibilityEvent = searchViewModel.getChildViewVisibleEventLiveData().value

        childViewVisibilityEvent?.getContentIfNotHandled() shouldBe childViewVisibilityChangedModel
    }

    @Test
    fun `handle no child is visible or ready`() {
        val productSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
        val shopSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()

        val productListVisibilityModel = ChildViewVisibilityChangedModel(
                isChildViewVisibleToUser = false,
                isChildViewReady = false,
                isFilterEnabled = true,
                isSortEnabled = true,
                searchNavigationOnClickListener = productSearchNavigationOnClick
        )
        val shopListVisibilityModel = ChildViewVisibilityChangedModel(
                isChildViewVisibleToUser = false,
                isChildViewReady = false,
                isFilterEnabled = true,
                isSortEnabled = false,
                searchNavigationOnClickListener = shopSearchNavigationOnClick
        )

        `When handle child views visibility changed`(
                productListVisibilityModel, shopListVisibilityModel
        )

        `Then verify event for visible child view`(null)
    }

    @Test
    fun `Change bottom navigation visibility to visible`() {
        `When change bottom navigation visibility`(true)
        `Then verify bottom navigation visibility live data`(true)
    }

    private fun `When change bottom navigation visibility`(isVisible: Boolean) {
        searchViewModel.changeBottomNavigationVisibility(isVisible)
    }

    private fun `Then verify bottom navigation visibility live data`(expectedIsBottomNavigationVisible: Boolean) {
        val bottomNavigationVisibilityLiveData = searchViewModel.getBottomNavigationVisibilityLiveData()
        bottomNavigationVisibilityLiveData.value shouldBe expectedIsBottomNavigationVisible
    }

    @Test
    fun `change bottom navigation visibility to hidden`() {
        `When change bottom navigation visibility`(false)
        `Then verify bottom navigation visibility live data`(false)
    }
}