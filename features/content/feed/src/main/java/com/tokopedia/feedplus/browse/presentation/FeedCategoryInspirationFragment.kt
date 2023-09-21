package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.feedplus.browse.presentation.adapter.FeedCategoryInspirationAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationModel
import com.tokopedia.feedplus.databinding.FragmentFeedBrowseInspirationBinding
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
class FeedCategoryInspirationFragment @Inject constructor() : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedBrowseInspirationBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { FeedCategoryInspirationAdapter() }

    override fun getScreenName(): String {
        return "Feed Browse Inspiration"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBrowseInspirationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

        setupMock()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.root.layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = adapter.getSpanSizeLookup()
        }
        binding.root.adapter = adapter
    }

    private fun setupMock() {
        adapter.submitList(
            buildList {
                add(
                    FeedCategoryInspirationModel.Chips(
                        id = "chips_mock",
                        chipList = List (6) {
                            FeedBrowseChipUiModel(
                                id = it.toString(),
                                label = "Chips $it",
                                isSelected = false,
                            )
                        }
                    )
                )

                addAll(
                    List(7) {
                        FeedCategoryInspirationModel.Card(
                            id = it.toString(),
                            imageUrl = "",
                            partnerName = "Partner $it",
                            avatarUrl = "",
                            badgeUrl = "",
                            title = "Card $it",
                        )
                    }
                )
            }
        )
    }
}
