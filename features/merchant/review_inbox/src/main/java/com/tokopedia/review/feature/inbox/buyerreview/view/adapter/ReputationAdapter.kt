package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
class ReputationAdapter private constructor(context: Context, listener: ReputationListener) :
    RecyclerView.Adapter<ReputationAdapter.ViewHolder>() {
    private var canGiveReputation: Boolean

    open interface ReputationListener {
        fun onReputationSmileyClicked(name: String?, value: String?)
        fun onGoToShopDetail(shopId: Long)
        fun onGoToPeopleProfile(userId: Long)
    }

    var list: ArrayList<SmileyModel>
    var listener: ReputationListener?
    var context: Context

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var smiley: ImageView
        var smileyText: Typography
        var main: View

        init {
            smiley = itemView.findViewById<View>(R.id.smiley) as ImageView
            smileyText = itemView.findViewById<View>(R.id.smiley_name) as Typography
            main = itemView.findViewById(R.id.main)
            main.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    if (listener != null && canGiveReputation) listener!!.onReputationSmileyClicked(
                        list.get(getAdapterPosition())
                            .getName(), list.get(getAdapterPosition()).getScore()
                    )
                }
            })
        }
    }

    public override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_smiley, viewGroup, false)
        )
    }

    public override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(
            holder.smiley,
            list.get(position).getResId()
        )
        if (list.get(position).getName()
                .isEmpty()
        ) holder.smileyText.setVisibility(View.GONE) else holder.smileyText.setText(
            list.get(
                position
            ).getName()
        )
    }

    public override fun getItemCount(): Int {
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
        if (reviewerScore == InboxReputationDetailHeaderViewHolder.Companion.SMILEY_BAD) {
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
        } else if (reviewerScore == InboxReputationDetailHeaderViewHolder.Companion.SMILEY_NEUTRAL) {
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

    companion object {
        val SMILEY_BAD: String = "-1"
        val SMILEY_NEUTRAL: String = "1"
        val SMILEY_GOOD: String = "2"
        fun createInstance(context: Context, listener: ReputationListener): ReputationAdapter {
            return ReputationAdapter(context, listener)
        }
    }

    init {
        list = ArrayList()
        this.listener = listener
        canGiveReputation = true
        this.context = context
    }
}