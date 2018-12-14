package com.tokopedia.kolcomponent.view.adapter.viewholder.post.poll

import com.tokopedia.kolcomponent.R
import com.tokopedia.kolcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.kolcomponent.view.adapter.viewitemView.post.poll.PollAdapter
import com.tokopedia.kolcomponent.view.viewmodel.post.poll.PollViewModel
import kotlinx.android.synthetic.main.item_post_poll.view.*

/**
 * @author by milhamj on 10/12/18.
 */
class PollViewHolder : BasePostViewHolder<PollViewModel>() {
    override var layoutRes = R.layout.item_post_poll

    override fun bind(element: PollViewModel) {
        val adapter = PollAdapter()
        adapter.setList(element.optionList)
        itemView.optionRv.adapter = adapter

        itemView.totalVoter.text = element.totalVoter
    }
}