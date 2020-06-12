package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.view.uimodel.ContactLoadMoreUiModel
import com.tokopedia.unifyprinciples.Typography

class ContactLoadMoreViewHolder(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<ContactLoadMoreUiModel>(itemView) {

    private var tittle: Typography? = itemView?.findViewById(R.id.txt_tittle)
    private var ctaDetail: Typography? = itemView?.findViewById(R.id.txt_action_detail)

    interface Listener {
        fun onClickContactLoadMore()
    }

    override fun bind(element: ContactLoadMoreUiModel) {
        bindTitle(element)
        bindCtaDetail(element)
    }

    @SuppressLint("SetTextI18n")
    private fun bindTitle(element: ContactLoadMoreUiModel) {
        tittle?.text = "Pengguna (${element.totalCount - 5}+)"
    }

    private fun bindCtaDetail(element: ContactLoadMoreUiModel) {
        if (element.hideCta) {
            ctaDetail?.hide()
        } else {
            ctaDetail?.show()
            ctaDetail?.setOnClickListener {
                listener?.onClickContactLoadMore()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chat_search_contact_load_more
    }
}