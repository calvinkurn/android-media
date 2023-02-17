package com.tokopedia.autocompletecomponent.initialstate.mps

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipDataView
import com.tokopedia.autocompletecomponent.chipwidget.ChipSpacingItemDecoration
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteMpsBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.util.addItemDecorationIfNotExists
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as RPrinciples

class MpsViewHolder(
    itemView: View,
    private val listener: MpsInitialStateListener,
): AbstractViewHolder<MpsDataView>(itemView) {
    private var binding: LayoutAutocompleteMpsBinding? by viewBinding()
    private val context: Context
        get() = itemView.context

    private val spacingItemDecoration = ChipSpacingItemDecoration(
        context.resources.getDimensionPixelSize(RPrinciples.dimen.unify_space_8),
        context.resources.getDimensionPixelSize(RPrinciples.dimen.unify_space_8)
    )

    private var adapter: MpsChipsAdapter? = null

    init {
        binding?.rvChips?.let {
            it.layoutManager = createLayoutManager()
            it.isNestedScrollingEnabled = false
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    override fun bind(element: MpsDataView?) {
        val element = element ?: return
        val binding = binding ?: return
        binding.tgHeader.text = element.header
        adapter = MpsChipsAdapter(object : MpsChipCallback {
            override fun onItemClicked(position: Int) {
                val chip = element.list.getOrNull(position) ?: return
                listener.onMpsChipClicked(chip)
            }
        })
        adapter?.submitList(element.list.map { toAutocompleteChipDataView(it) })
        binding.rvChips.adapter = adapter
    }

    override fun onViewRecycled() {
        adapter?.submitList(emptyList())
        adapter = null
        super.onViewRecycled()
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return ChipsLayoutManager.newBuilder(context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()
    }

    private fun toAutocompleteChipDataView(
        baseItemInitialStateSearch: BaseItemInitialStateSearch
    ): AutocompleteChipDataView = AutocompleteChipDataView(
        template = baseItemInitialStateSearch.template,
        type = baseItemInitialStateSearch.type,
        applink = baseItemInitialStateSearch.applink,
        url = baseItemInitialStateSearch.url,
        title = baseItemInitialStateSearch.title,
        dimension90 = baseItemInitialStateSearch.dimension90,
        position = baseItemInitialStateSearch.position,
        featureId = baseItemInitialStateSearch.featureId
    )

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_mps
    }
}
