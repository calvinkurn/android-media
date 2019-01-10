package com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll

import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel
import kotlinx.android.synthetic.main.item_post_poll.view.*

/**
 * @author by milhamj on 10/12/18.
 */
class PollViewHolder(private val pollOptionListener: PollAdapter.PollOptionListener)
    : BasePostViewHolder<PollContentViewModel>() {

    override var layoutRes = R.layout.item_post_poll

    override fun bind(element: PollContentViewModel) {
        val adapter = PollAdapter(pagerPosition, element, pollOptionListener)
        adapter.setList(element.optionList)
        itemView.optionRv.adapter = adapter

        itemView.totalVoter.text = element.totalVoter
    }
}