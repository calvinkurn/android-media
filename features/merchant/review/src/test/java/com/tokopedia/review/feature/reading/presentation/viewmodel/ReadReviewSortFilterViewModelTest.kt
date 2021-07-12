package com.tokopedia.review.feature.reading.presentation.viewmodel

import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Assert
import org.junit.Test

class ReadReviewSortFilterViewModelTest : ReadReviewSortFilterViewModelTestFixture() {

    @Test
    fun `when filter is selected should update buttonState accordingly`() {
        val expectedButtonState = true
        val filterData = arrayListOf(
                ListItemUnify("Kualitas Produk", ""),
                ListItemUnify("Pelayanan Toko", ""),
                ListItemUnify("Kemasan Produk", ""),
                ListItemUnify("Harga", ""),
                ListItemUnify("Pengiriman", "")
        )
        val isChecked = true
        val selectedFilter = ListItemUnify("Kualitas Produk", "")

        viewModel.setInitialValues(setOf(), "", filterData)
        viewModel.onFilterCheckChange(isChecked, selectedFilter)

        verifySelectedFilterEquals(setOf(selectedFilter.listTitleText))
        verifyFilterDataEquals(filterData)
        verifyOriginalFilterEquals(setOf())
        verifyButtonStateValueEquals(expectedButtonState)
    }

    @Test
    fun `when filter is selected then unselected should update buttonState accordingly`() {
        var expectedButtonState = true
        val filterData = arrayListOf(
                ListItemUnify("Kualitas Produk", ""),
                ListItemUnify("Pelayanan Toko", ""),
                ListItemUnify("Kemasan Produk", ""),
                ListItemUnify("Harga", ""),
                ListItemUnify("Pengiriman", "")
        )
        val selectedFilter = ListItemUnify("Kualitas Produk", "")

        viewModel.setInitialValues(setOf(), "", filterData)
        viewModel.onFilterCheckChange(true, selectedFilter)
        verifyButtonStateValueEquals(expectedButtonState)
        verifySelectedFilterEquals(setOf(selectedFilter.listTitleText))

        expectedButtonState = false
        viewModel.onFilterCheckChange(false, selectedFilter)
        verifyButtonStateValueEquals(expectedButtonState)
        verifySelectedFilterEquals(setOf())

        verifyFilterDataEquals(filterData)
        verifyOriginalFilterEquals(setOf())
    }

    @Test
    fun `when clearAllFilters should update buttonState accordingly`() {
        val expectedButtonState = true
        val filterData = arrayListOf(
                ListItemUnify("Kualitas Produk", ""),
                ListItemUnify("Pelayanan Toko", ""),
                ListItemUnify("Kemasan Produk", ""),
                ListItemUnify("Harga", ""),
                ListItemUnify("Pengiriman", "")
        )

        viewModel.setInitialValues(originalFilter, "", filterData)
        viewModel.clearAllFilters()

        verifyFilterDataEquals(filterData)
        verifySelectedFilterEquals(setOf())
        verifyOriginalFilterEquals(originalFilter)
        verifyButtonStateValueEquals(expectedButtonState)
    }

    @Test
    fun `when updateSelectedFilter should set selectedFilter accordingly`() {
        val selectedFilter = ListItemUnify("Kualitas Produk", "")

        viewModel.updateSelectedFilter(selectedFilter)

        verifySelectedFilterEquals(setOf(selectedFilter.listTitleText))
    }

    @Test
    fun `when onSortCheckChange should update buttonState accordingly`() {
        val expectedButtonState = true
        val expectedSelectedSort = "Rating Tertinggi"
        val sortData = arrayListOf(
                ListItemUnify("Paling Membantu", ""),
                ListItemUnify("Terbaru", ""),
                ListItemUnify("Rating Tertinggi", ""),
                ListItemUnify("Rating Terendah", ""),
        )

        viewModel.setInitialValues(setOf(), originalSort, sortData)
        viewModel.onSortCheckChange(true, ListItemUnify(expectedSelectedSort, ""))

        verifyFilterDataEquals(sortData)
        verifyOriginalSortEquals(originalSort)
        verifySelectedSortEquals(expectedSelectedSort)
        verifyButtonStateValueEquals(expectedButtonState)
    }

    @Test
    fun `when onSortCheckChange but isChecked = false should not update buttonState`() {
        val expectedButtonState = false
        val sortData = arrayListOf(
                ListItemUnify("Paling Membantu", ""),
                ListItemUnify("Terbaru", ""),
                ListItemUnify("Rating Tertinggi", ""),
                ListItemUnify("Rating Terendah", ""),
        )

        viewModel.setInitialValues(setOf(), originalSort, sortData)
        viewModel.onSortCheckChange(false, ListItemUnify("Rating Tertinggi", ""))

        verifyFilterDataEquals(sortData)
        verifyOriginalSortEquals(originalSort)
        verifySelectedSortEquals("")
        verifyButtonStateValueEquals(expectedButtonState)
    }

    private fun verifyFilterDataEquals(filterData: ArrayList<ListItemUnify>) {
        Assert.assertEquals(viewModel.getFilterData(), filterData)
    }

    private fun verifyOriginalFilterEquals(originalFilter: Set<String>) {
        Assert.assertEquals(viewModel.getOriginalFilters(), originalFilter)
    }

    private fun verifySelectedFilterEquals(selectedFilters: Set<String>) {
        if (selectedFilters.isEmpty()) {
            Assert.assertTrue(viewModel.getSelectedFilters().isEmpty())
            return
        }
        viewModel.getSelectedFilters().forEach {
            Assert.assertTrue(selectedFilters.contains(it.listTitleText))
        }
    }

    private fun verifyOriginalSortEquals(originalSort: String) {
        Assert.assertEquals(viewModel.getOriginalSort(), originalSort)
    }

    private fun verifySelectedSortEquals(selectedSort: String) {
        Assert.assertEquals(viewModel.getSelectedSort().listTitleText, selectedSort)
    }

    private fun verifyButtonStateValueEquals(expectedButtonState: Boolean) {
        viewModel.buttonState.verifyValueEquals(expectedButtonState)
    }
}