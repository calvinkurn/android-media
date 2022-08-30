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
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet

class TokoNowRecipeFilterFragment : Fragment() {

    companion object {

        fun newInstance(): TokoNowRecipeFilterFragment {
            return TokoNowRecipeFilterFragment()
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
        showSortFilterBottomSheet()
    }

    private fun showSortFilterBottomSheet() {
        val title = getString(R.string.tokopedianow_filter)
        // Temporary Hardcode
        val items = listOf(
            TokoNowSectionHeaderUiModel(
                id = "1",
                title = "Urutkan",
                seeAllAppLink = ""
            ),
            TokoNowChipListUiModel(
                items = listOf(
                    TokoNowChipUiModel(
                        id = "1",
                        parentId = "1",
                        text = "Terbaru"
                    ),
                    TokoNowChipUiModel(
                        id = "2",
                        parentId = "1",
                        text = "Terlama"
                    ),
                    TokoNowChipUiModel(
                        id = "3",
                        parentId = "1",
                        text = "Porsi Terbanyak"
                    ),
                    TokoNowChipUiModel(
                        id = "4",
                        parentId = "1",
                        text = "Porsi Paling Sedikit"
                    ),
                    TokoNowChipUiModel(
                        id = "5",
                        parentId = "1",
                        text = "Waktu Tercepat"
                    ),
                    TokoNowChipUiModel(
                        id = "6",
                        parentId = "1",
                        text = "Waktu Terlama"
                    )
                )
            ),
            TokoNowSectionHeaderUiModel(
                id = "2",
                title = "Bahan",
                seeAllAppLink = "tokopedia://now"
            ),
            TokoNowChipListUiModel(
                items = listOf(
                    TokoNowChipUiModel(
                        id = "1",
                        parentId = "2",
                        text = "Daging Sapi",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg"
                    ),
                    TokoNowChipUiModel(
                        id = "2",
                        parentId = "2",
                        text = "Daging Ayam",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg"
                    ),
                    TokoNowChipUiModel(
                        id = "3",
                        parentId = "2",
                        text = "Kubis",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg"
                    ),
                    TokoNowChipUiModel(
                        id = "4",
                        parentId = "2",
                        text = "Kimchi",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg"
                    ),
                    TokoNowChipUiModel(
                        id = "5",
                        parentId = "2",
                        text = "Wortel",
                        imageUrl = "https://images.tokopedia.net/img/now/Tokopedia%20NOW!%20Badge.jpg"
                    )
                )
            )
        )

        val bottomSheet = TokoNowSortFilterBottomSheet.newInstance().apply {
            setTitle(title)
            sortFilterItems = items
        }.apply {

        }

        bottomSheet.show(childFragmentManager)
    }
}