package com.tokopedia.tokopedianow.recipehome.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowChipListUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSectionHeaderUiModel
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeSortBy
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet
import java.util.*

class TokoNowRecipeFilterFragment : Fragment() {

    companion object {

        const val EXTRA_SELECTED_FILTER_IDS = "extra_selected_filter_ids"

        fun newInstance(selectedFilterIds: List<String>): TokoNowRecipeFilterFragment {
            return TokoNowRecipeFilterFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(EXTRA_SELECTED_FILTER_IDS, ArrayList(selectedFilterIds))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedFilterIds = arguments
            ?.getStringArrayList(EXTRA_SELECTED_FILTER_IDS).orEmpty()
        showSortFilterBottomSheet(selectedFilterIds)
    }

    private fun showSortFilterBottomSheet(selectedFilterIds: List<String>) {
        val title = getString(R.string.tokopedianow_filter)
        // Temporary Hardcode
        val items = listOf(
            TokoNowSectionHeaderUiModel(
                id = "1",
                title = "Urutkan",
                seeAllAppLink = ""
            ),
            TokoNowChipListUiModel(
                parentId = "1",
                items = listOf(
                    TokoNowChipUiModel(
                        id = RecipeSortBy.Newest.name,
                        parentId = "1",
                        text = "Terbaru",
                        selected = selectedFilterIds.contains(RecipeSortBy.Newest.name)
                    ),
                    TokoNowChipUiModel(
                        id = RecipeSortBy.Oldest.name,
                        parentId = "1",
                        text = "Terlama",
                        selected = selectedFilterIds.contains(RecipeSortBy.Oldest.name)
                    ),
                    TokoNowChipUiModel(
                        id = RecipeSortBy.MostPortion.name,
                        parentId = "1",
                        text = "Porsi Terbanyak",
                        selected = selectedFilterIds.contains(RecipeSortBy.MostPortion.name)
                    ),
                    TokoNowChipUiModel(
                        id = RecipeSortBy.LeastPortion.name,
                        parentId = "1",
                        text = "Porsi Paling Sedikit",
                        selected = selectedFilterIds.contains(RecipeSortBy.LeastPortion.name)
                    ),
                    TokoNowChipUiModel(
                        id = RecipeSortBy.ShortestDuration.name,
                        parentId = "1",
                        text = "Waktu Tercepat",
                        selected = selectedFilterIds.contains(RecipeSortBy.ShortestDuration.name)
                    ),
                    TokoNowChipUiModel(
                        id = RecipeSortBy.LongestDuration.name,
                        parentId = "1",
                        text = "Waktu Terlama",
                        selected = selectedFilterIds.contains(RecipeSortBy.LongestDuration.name)
                    )
                ),
                isMultiSelect = false
            ),
            TokoNowSectionHeaderUiModel(
                id = "2",
                title = "Bahan",
                seeAllAppLink = "tokopedia://now"
            ),
            TokoNowChipListUiModel(
                parentId = "2",
                items = listOf(
                    TokoNowChipUiModel(
                        id = "1",
                        parentId = "2",
                        text = "Daging Sapi",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg",
                        selected = selectedFilterIds.contains("1")
                    ),
                    TokoNowChipUiModel(
                        id = "2",
                        parentId = "2",
                        text = "Daging Ayam",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg",
                        selected = selectedFilterIds.contains("2")
                    ),
                    TokoNowChipUiModel(
                        id = "3",
                        parentId = "2",
                        text = "Kubis",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg",
                        selected = selectedFilterIds.contains("3")
                    ),
                    TokoNowChipUiModel(
                        id = "4",
                        parentId = "2",
                        text = "Kimchi",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg",
                        selected = selectedFilterIds.contains("4")
                    ),
                    TokoNowChipUiModel(
                        id = "5",
                        parentId = "2",
                        text = "Wortel",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg",
                        selected = selectedFilterIds.contains("5")
                    )
                )
            )
        )

        val bottomSheet = TokoNowSortFilterBottomSheet.newInstance().apply {
            setTitle(title)
            sortFilterItems = items
        }

        bottomSheet.show(childFragmentManager)
    }
}