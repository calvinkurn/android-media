package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.PlayEtalaseAdapter
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.adapter.SearchSuggestionsAdapter
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment() {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var container: ViewGroup
    private lateinit var tvInfo: TextView
    private lateinit var psbSearch: PlaySearchBar
    private lateinit var rvEtalase: RecyclerView
    private lateinit var rvSuggestions: RecyclerView

    private val etalaseAdapter = PlayEtalaseAdapter(object : PlayEtalaseViewHolder.Listener {
        override fun onEtalaseClicked(etalaseId: Long) {
            broadcastCoordinator.navigateToFragment(
                    PlayEtalaseDetailFragment::class.java,
                    Bundle().apply {
                        putLong(PlayEtalaseDetailFragment.EXTRA_ETALASE_ID, etalaseId)
                    }
            )
        }

        override fun onEtalaseBound(etalaseId: Long) {
            viewModel.loadEtalaseProductPreview(etalaseId)
        }
    }, object : ProductSelectableViewHolder.Listener {
        override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
            viewModel.selectProduct(productId, isSelected)
        }

        override fun onProductSelectError(reason: Throwable) {
        }
    })

    private val searchSuggestionsAdapter = SearchSuggestionsAdapter(object : SearchSuggestionViewHolder.Listener {
        override fun onSuggestionClicked(suggestionText: String) {
            psbSearch.text = suggestionText
            psbSearch.forceSearch()
        }
    })

    override fun getTitle(): String = "Select Products or Collection"

    override fun isRootFragment(): Boolean = true

    override fun refresh() {
        etalaseAdapter.notifyDataSetChanged()
    }

    override fun getScreenName(): String = "Play Etalase Picker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        broadcastCoordinator.showBottomAction(false)
        return inflater.inflate(R.layout.fragment_play_etalase_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeEtalase()
        observeSearchSuggestions()
    }

    private fun initView(view: View) {
        with(view) {
            container = this as ViewGroup
            tvInfo = findViewById(R.id.tv_info)
            psbSearch = findViewById(R.id.psb_search)
            rvEtalase = findViewById(R.id.rv_etalase)
            rvSuggestions = findViewById(R.id.rv_suggestions)
        }
    }

    private fun setupView(view: View) {
        psbSearch.setListener(object : PlaySearchBar.Listener {

            override fun onEditStateChanged(view: PlaySearchBar, isEditing: Boolean) {
                if (isEditing) enterSearchMode()
                else exitSearchMode()
            }

            override fun onCanceled(view: PlaySearchBar) {
                exitSearchMode()
                viewModel.loadEtalaseList()
            }

            override fun onNewKeyword(view: PlaySearchBar, keyword: String) {
                viewModel.loadSuggestionsFromKeyword(keyword)
            }

            override fun onSearchButtonClicked(view: PlaySearchBar, keyword: String) {
                shouldSearchProductWithKeyword(keyword)
            }
        })

        rvEtalase.adapter = etalaseAdapter
        rvEtalase.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))

        rvSuggestions.adapter = searchSuggestionsAdapter
    }

    private fun enterSearchMode() {
        tvInfo.gone()
        rvEtalase.gone()

        rvSuggestions.visible()

        broadcastCoordinator.showBottomAction(false)
    }

    private fun exitSearchMode() {
        tvInfo.visible()
        rvEtalase.visible()

        rvSuggestions.gone()

        broadcastCoordinator.showBottomAction(psbSearch.text.isNotEmpty())
    }

    private fun shouldSearchProductWithKeyword(keyword: String) {
        viewModel.searchProductsByKeyword(keyword)
        exitSearchMode()
    }

    //region observe
    /**
     * Observe
     */
    private fun observeEtalase() {
        viewModel.observableEtalaseAndSearch.observe(viewLifecycleOwner, Observer {
            etalaseAdapter.setItemsAndAnimateChanges(it)
        })
    }

    private fun observeSearchSuggestions() {
        viewModel.observableSuggestionList.observe(viewLifecycleOwner, Observer {
            searchSuggestionsAdapter.setItemsAndAnimateChanges(it)
        })
    }
    //endregion
}