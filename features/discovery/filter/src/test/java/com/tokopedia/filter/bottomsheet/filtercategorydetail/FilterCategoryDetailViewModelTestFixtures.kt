package com.tokopedia.filter.bottomsheet.filtercategorydetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.filter.bottomsheet.getCategoryFilter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.LevelTwoCategory
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.After
import org.junit.Before
import org.junit.Rule

internal open class FilterCategoryDetailViewModelTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val filterCategoryDetailViewModel = FilterCategoryDetailViewModel()

    protected var headerViewModelList: List<FilterCategoryLevelOneViewModel>? = null
    private val headerViewModelListObserver = Observer<List<FilterCategoryLevelOneViewModel>> {
        headerViewModelList = it
    }

    protected val updatedHeaderPosition = mutableListOf<Int>()
    private val updateHeaderInPositionObserver = EventObserver<Int> {
        updatedHeaderPosition.add(it)
    }

    protected var contentViewModelList: List<FilterCategoryLevelTwoViewModel>? = null
    private val contentViewModelListObserver = Observer<List<FilterCategoryLevelTwoViewModel>> {
        contentViewModelList = it
    }

    protected var updateContentInPosition = mutableListOf<Int>()
    private val updateContentInPositionObserver = EventObserver<Int> {
        updateContentInPosition.add(it)
    }

    @Before
    fun setUp() {
        filterCategoryDetailViewModel.headerViewModelListLiveData.observeForever(headerViewModelListObserver)
        filterCategoryDetailViewModel.updateHeaderInPositionEventLiveData.observeForever(updateHeaderInPositionObserver)
        filterCategoryDetailViewModel.contentViewModelListLiveData.observeForever(contentViewModelListObserver)
        filterCategoryDetailViewModel.updateContentInPositionEventLiveData.observeForever(updateContentInPositionObserver)
    }

    @After
    fun tearDown() {
        filterCategoryDetailViewModel.headerViewModelListLiveData.removeObserver(headerViewModelListObserver)
        filterCategoryDetailViewModel.updateHeaderInPositionEventLiveData.removeObserver(updateHeaderInPositionObserver)
        filterCategoryDetailViewModel.contentViewModelListLiveData.removeObserver(contentViewModelListObserver)
        filterCategoryDetailViewModel.updateContentInPositionEventLiveData.removeObserver(updateContentInPositionObserver)
    }

    protected fun getCategoryFilter() =
            "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>().getCategoryFilter()

    protected fun `Given initialized view model`(categoryFilter: Filter?, selectedCategoryFilterValue: String = "") {
        filterCategoryDetailViewModel.init(categoryFilter, selectedCategoryFilterValue)
    }

    protected fun `Given view model initialized and view created`(filter: Filter, selectedCategoryFilterValue: String = "") {
        filterCategoryDetailViewModel.init(filter, selectedCategoryFilterValue)
        filterCategoryDetailViewModel.onViewCreated()
    }

    protected fun `Then assert filter category header view model is selected`(selectedCategoryFilterPosition: Int) {
        headerViewModelList!!.forEachIndexed { index, headerViewModel ->
            val expectedIsSelected = index == selectedCategoryFilterPosition
            val actualIsSelected = headerViewModel.isSelected
            assert(actualIsSelected == expectedIsSelected) {
                "Header view model index $index isSelected should be $expectedIsSelected."
            }
        }
    }

    protected fun `Then assert content view model list`(levelOneFilterOption: Option) {
        val contentViewModelList = this.contentViewModelList ?: throw AssertionError("Content view model should not be null")

        val levelTwoCategoryList = levelOneFilterOption.levelTwoCategoryList

        val expectedContentViewModelSize = levelTwoCategoryList.size
        contentViewModelList.assertContentViewModelListSize(expectedContentViewModelSize)

        contentViewModelList.forEachIndexed { index, filterCategoryLevelTwoViewModel ->
            val expectedLevelTwoCategory = levelTwoCategoryList[index]
            val actualLevelTwoCategory = filterCategoryLevelTwoViewModel.levelTwoCategory

            assert(actualLevelTwoCategory == expectedLevelTwoCategory) {
                "Level Two view model index $index level two category is \"${actualLevelTwoCategory.name}\", " +
                        "expected is \"${expectedLevelTwoCategory.name}\"."
            }

            filterCategoryLevelTwoViewModel.assertLevelThreeCategoryViewModelList(expectedLevelTwoCategory)
        }
    }

    protected fun `Then assert button reset visibility`(expectedIsVisible: Boolean) {
        assert(filterCategoryDetailViewModel.isButtonResetVisibleLiveData.value == expectedIsVisible) {
            "Button Reset should be visible"
        }
    }

    private fun List<FilterCategoryLevelTwoViewModel>.assertContentViewModelListSize(expectedContentViewModelSize: Int) {
        val actualContentViewModelSize = size
        assert(actualContentViewModelSize == expectedContentViewModelSize) {
            "Content View Model List size is $actualContentViewModelSize, expected is $expectedContentViewModelSize."
        }
    }

    private fun FilterCategoryLevelTwoViewModel.assertLevelThreeCategoryViewModelList(expectedLevelTwoCategory: LevelTwoCategory) {
        val levelThreeCategoryList = expectedLevelTwoCategory.levelThreeCategoryList

        val expectedLevelThreeViewModelSize = levelThreeCategoryList.size
        assertLevelThreeCategoryViewModelListSize(expectedLevelThreeViewModelSize)

        levelThreeCategoryViewModelList.forEachIndexed { index, filterCategoryLevelThreeViewModel ->
            val expectedLevelThreeCategory = levelThreeCategoryList[index]
            val actualLevelThreeCategory = filterCategoryLevelThreeViewModel.levelThreeCategory

            assert(actualLevelThreeCategory == expectedLevelThreeCategory) {
                "Level Three view model index $index level three category is \"${actualLevelThreeCategory.name}\", " +
                        "expected is \"${expectedLevelThreeCategory.name}\"."
            }
        }
    }

    private fun FilterCategoryLevelTwoViewModel.assertLevelThreeCategoryViewModelListSize(expectedLevelThreeViewModelSize: Int) {
        val actualLevelThreeViewModelSize = levelThreeCategoryViewModelList.size
        assert(actualLevelThreeViewModelSize == expectedLevelThreeViewModelSize) {
            "Level Three View Model List size is $actualLevelThreeViewModelSize, expected is $expectedLevelThreeViewModelSize."
        }
    }
}