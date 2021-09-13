package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.HeaderOptionUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.OptionUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * @author by nisie on 8/21/17.
 */
class InboxReputationFilterAdapter private constructor(
    private val context: Context?, private val listener: FilterListener,
    private val listOption: ArrayList<OptionUiModel>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    open interface FilterListener {
        fun onFilterSelected(optionUiModel: OptionUiModel)
        fun onFilterUnselected(optionUiModel: OptionUiModel)
    }

    inner class HeaderViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: Typography

        init {
            title = itemView.findViewById<View>(R.id.title) as Typography
        }
    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var filter: Typography
        var check: ImageView
        var mainView: View

        init {
            filter = itemView.findViewById<View>(R.id.filter) as Typography
            check = itemView.findViewById<View>(R.id.check) as ImageView
            mainView = itemView.findViewById(R.id.main_view)
            mainView.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    for (viewModel: OptionUiModel in listOption!!) {
                        if (viewModel === listOption.get(getAdapterPosition())) viewModel.setSelected(
                            !viewModel.isSelected()
                        ) else if (viewModel.getKey() === listOption.get(getAdapterPosition())
                                .getKey()
                        ) viewModel.setSelected(false)
                    }
                    if (listOption.get(getAdapterPosition()).isSelected()) {
                        listener.onFilterSelected(listOption.get(getAdapterPosition()))
                    } else {
                        listener.onFilterUnselected(listOption.get(getAdapterPosition()))
                    }
                    notifyDataSetChanged()
                }
            })
        }
    }

    public override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_HEADER -> return HeaderViewHolder(
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listview_filter_header, parent, false)
            )
            else -> return ViewHolder(
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listview_filter, parent, false)
            )
        }
    }

    public override fun onBindViewHolder(
        parent: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (getItemViewType(position) == VIEW_HEADER) {
            val holder: HeaderViewHolder = parent as HeaderViewHolder
            holder.title.setText(listOption!!.get(position).getName())
        } else {
            val holder: ViewHolder = parent as ViewHolder
            holder.filter.setText(listOption!!.get(position).getName())
            if (listOption!!.get(position).isSelected()) {
                holder.filter.setTextColor(
                    MethodChecker.getColor(
                        context, com.tokopedia.unifyprinciples.R.color.Unify_G400
                    )
                )
                holder.check.setVisibility(View.VISIBLE)
            } else {
                holder.filter.setTextColor(
                    MethodChecker.getColor(
                        context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44
                    )
                )
                holder.check.setVisibility(View.GONE)
            }
        }
    }

    public override fun getItemViewType(position: Int): Int {
        if (listOption!!.get(position) is HeaderOptionUiModel) return VIEW_HEADER else return super.getItemViewType(
            position
        )
    }

    public override fun getItemCount(): Int {
        return listOption!!.size
    }

    fun resetFilter() {
        for (optionUiModel: OptionUiModel in listOption!!) {
            optionUiModel.setSelected(false)
        }
        notifyDataSetChanged()
    }

    companion object {
        private val VIEW_HEADER: Int = 101
        fun createInstance(
            context: Context?, listener: FilterListener,
            listOption: ArrayList<OptionUiModel>?
        ): InboxReputationFilterAdapter {
            return InboxReputationFilterAdapter(context, listener, listOption)
        }
    }
}