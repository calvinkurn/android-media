package com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentModel

/**
 * @author by milhamj on 10/12/18.
 */
class PollViewHolder(private val pollOptionListener: PollAdapter.PollOptionListener)
    : BasePostViewHolder<PollContentModel>() {

    private val optionRv: RecyclerView = itemView.findViewById(R.id.optionRv)
    private val totalVoter: TextView = itemView.findViewById(R.id.totalVoter)

    companion object {
        private const val TOTAL_VOTER = "\${totalVoter}"
    }

    override var layoutRes = R.layout.item_post_poll

    override fun bind(element: PollContentModel) {
        val adapter = PollAdapter(pagerPosition, element, pollOptionListener)
        adapter.setList(element.optionList)
        optionRv.adapter = adapter

        val totalVoterText = element.totalVoter.replace(
                TOTAL_VOTER, element.totalVoterNumber.toString()
        )
        totalVoter.text = totalVoterText
    }
}
