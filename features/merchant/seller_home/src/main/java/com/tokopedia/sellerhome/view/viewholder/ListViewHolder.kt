package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import android.widget.Toast
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.adapter.ListAdapterTypeFactory
import com.tokopedia.sellerhome.view.model.ListItemUiModel
import com.tokopedia.sellerhome.view.model.ListUiModel
import kotlinx.android.synthetic.main.sah_partial_error_load_data.view.*
import kotlinx.android.synthetic.main.sah_partial_list_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_list_widget.view.*

class ListViewHolder(view: View?) : AbstractViewHolder<ListUiModel>(view), BaseListAdapter.OnAdapterInteractionListener<ListItemUiModel> {
    companion object {
        val RES_LAYOUT = R.layout.sah_list_card_widget
    }

    private var state = State.SUCCESS

    override fun bind(element: ListUiModel) {
        showLoadingState()
        itemView.postDelayed({
            showErrorState(element)
        }, 5000)
        itemView.postDelayed({
            showSuccessState(element)
        }, 10000)
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideListLayout()
        showShimmeringLayout()
    }

    private fun showSuccessState(element: ListUiModel) {
        element.data?.run {
            hideErrorLayout()
            hideShimmeringLayout()
            itemView.tv_card_title.text = element.title
            setupListInfoSeller()

            (itemView.rv_item_list.adapter as BaseListAdapter<ListItemUiModel, *>).run {
                data.addAll(items)
                notifyDataSetChanged()
            }

            showListLayout()
        }
    }

    private fun showErrorState(element: ListUiModel) {
        hideListLayout()
        hideShimmeringLayout()
        itemView.tv_error_card_title.text = element.title
        showErrorLayout()
    }

    private fun setupListInfoSeller() {
        itemView.rv_item_list.apply {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = BaseListAdapter<ListItemUiModel, ListAdapterTypeFactory>(ListAdapterTypeFactory(), this@ListViewHolder)
        }
    }

    override fun onItemClicked(t: ListItemUiModel?) {
        Toast.makeText(itemView.context, "Hi Bambang!", Toast.LENGTH_SHORT).show()
    }

    private fun showShimmeringLayout() {
        itemView.sah_list_layout_shimmering.visible()
    }

    private fun hideShimmeringLayout() {
        itemView.sah_list_layout_shimmering.gone()
    }

    private fun showListLayout() {
        itemView.sah_list_layout.visible()
    }

    private fun hideListLayout() {
        itemView.sah_list_layout.gone()
    }

    private fun showErrorLayout() {
        itemView.sah_error_layout.visible()
    }

    private fun hideErrorLayout() {
        itemView.sah_error_layout.gone()
    }

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }
}