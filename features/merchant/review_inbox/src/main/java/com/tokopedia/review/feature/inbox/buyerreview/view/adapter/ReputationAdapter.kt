package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SmileyModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * @author by nisie on 8/28/17.
 */
class ReputationAdapter private constructor(
    private val context: Context,
    private val listener: ReputationListener
) :
    RecyclerView.Adapter<ReputationAdapter.ViewHolder>() {

    companion object {
        const val SMILEY_BAD: String = "-1"
        const val SMILEY_NEUTRAL: String = "1"
        const val SMILEY_GOOD: String = "2"
        fun createInstance(context: Context, listener: ReputationListener): ReputationAdapter {
            return ReputationAdapter(context, listener)
        }
    }

    private var canGiveReputation: Boolean = true
    var list: ArrayList<SmileyModel> = ArrayList()

    interface ReputationListener {
        fun onReputationSmileyClicked(name: String, value: String)
        fun onGoToShopDetail(shopId: Long)
        fun onGoToPeopleProfile(userId: Long)
    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val smiley: ImageView = itemView.findViewById<View>(R.id.smiley) as ImageView
        val smileyText: Typography = itemView.findViewById<View>(R.id.smiley_name) as Typography
        val main: View = itemView.findViewById(R.id.main)

        init {
            main.setOnClickListener {
                if (canGiveReputation) listener.onReputationSmileyClicked(
                    list[adapterPosition]
                        .name, list[adapterPosition].score
                )
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.listview_smiley, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list.getOrNull(position)?.resId?.let {
            ImageHandler.loadImageWithIdWithoutPlaceholder(
                holder.smiley,
                it
            )
        }
        if (list.getOrNull(position)?.name?.isEmpty() == true
        ) holder.smileyText.visibility =
            View.GONE else holder.smileyText.text = list.getOrNull(position)?.name
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun showAllSmiley() {
        list.clear()
        list.add(
            SmileyModel(
                R.drawable.ic_smiley_bad_empty,
                context.getString(R.string.smiley_bad),
                SMILEY_BAD
            )
        )
        list.add(
            SmileyModel(
                R.drawable.ic_smiley_netral_empty,
                context.getString(R.string.smiley_netral),
                SMILEY_NEUTRAL
            )
        )
        list.add(
            SmileyModel(
                R.drawable.ic_smiley_good_empty,
                context.getString(R.string.smiley_good),
                SMILEY_GOOD
            )
        )
        notifyDataSetChanged()
    }

    fun showSmileyBad() {
        list.clear()
        list.add(
            SmileyModel(
                R.drawable.review_ic_smiley_bad,
                context.getString(R.string.smiley_bad),
                SMILEY_BAD
            )
        )
        canGiveReputation = false
        notifyDataSetChanged()
    }

    fun showSmileyNeutral() {
        list.clear()
        list.add(
            SmileyModel(
                R.drawable.review_ic_smiley_neutral,
                context.getString(R.string.smiley_netral),
                SMILEY_NEUTRAL
            )
        )
        canGiveReputation = false
        notifyDataSetChanged()
    }

    fun showSmileyGood() {
        list.clear()
        list.add(
            SmileyModel(
                R.drawable.review_ic_smiley_good,
                context.getString(R.string.smiley_good),
                SMILEY_GOOD
            )
        )
        canGiveReputation = false
        notifyDataSetChanged()
    }

    fun showLockedSmiley() {
        list.clear()
        list.add(
            SmileyModel(
                R.drawable.ic_locked2,
                "",
                ""
            )
        )
        canGiveReputation = false
    }

    fun showChangeSmiley(reviewerScore: Int) {
        canGiveReputation = true
        if (reviewerScore == InboxReputationDetailHeaderViewHolder.SMILEY_BAD) {
            list.clear()
            list.add(
                SmileyModel(
                    R.drawable.review_ic_smiley_bad,
                    context.getString(R.string.smiley_bad),
                    SMILEY_BAD,
                    true
                )
            )
            list.add(
                SmileyModel(
                    R.drawable.ic_smiley_netral_empty,
                    context.getString(R.string.smiley_netral),
                    SMILEY_NEUTRAL,
                    true
                )
            )
            list.add(
                SmileyModel(
                    R.drawable.ic_smiley_good_empty,
                    context.getString(R.string.smiley_good),
                    SMILEY_GOOD,
                    true
                )
            )
        } else if (reviewerScore == InboxReputationDetailHeaderViewHolder.SMILEY_NEUTRAL) {
            list.clear()
            list.add(
                SmileyModel(
                    R.drawable.ic_smiley_bad_empty,
                    context.getString(R.string.smiley_bad),
                    SMILEY_BAD,
                    true
                )
            )
            list.add(
                SmileyModel(
                    R.drawable.review_ic_smiley_neutral,
                    context.getString(R.string.smiley_netral),
                    SMILEY_NEUTRAL,
                    true
                )
            )
            list.add(
                SmileyModel(
                    R.drawable.ic_smiley_good_empty,
                    context.getString(R.string.smiley_good),
                    SMILEY_GOOD,
                    true
                )
            )
        }
        notifyDataSetChanged()
    }
}