package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.unifycomponents.UnifyButton

class LoadMoreViewHolder constructor(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<LoadMoreUiModel>(itemView) {

    private val btnLoading: UnifyButton? = itemView?.findViewById(R.id.btn_loading)

    interface Listener {
        fun loadMoreEarlier(lastKnownPosition: Int, element: LoadMoreUiModel)
        fun loadMoreNew(lastKnownPosition: Int, element: LoadMoreUiModel)
    }

    override fun bind(element: LoadMoreUiModel, payloads: MutableList<Any>) {
        val payload = payloads.getOrNull(0) ?: return
        when (payload) {
            PAYLOAD_UPDATE_STATE -> bindButtonState(element)
        }
    }

    override fun bind(element: LoadMoreUiModel) {
        bindButtonState(element)
        bindClick(element)
    }

    private fun bindButtonState(element: LoadMoreUiModel) {
        btnLoading?.isLoading = element.loading
        btnLoading?.isEnabled = !element.loading
    }

    private fun bindClick(element: LoadMoreUiModel) {
        btnLoading?.setOnClickListener {
            when (element.type) {
                LoadMoreUiModel.LoadMoreType.NEW -> {
                    listener?.loadMoreNew(adapterPosition, element)
                }
                LoadMoreUiModel.LoadMoreType.EARLIER -> {
                    listener?.loadMoreEarlier(adapterPosition, element)
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_load_more

        const val PAYLOAD_UPDATE_STATE = "payload_update_state"
    }
}