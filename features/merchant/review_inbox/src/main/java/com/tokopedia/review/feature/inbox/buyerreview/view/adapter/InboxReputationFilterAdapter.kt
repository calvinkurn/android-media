package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.HeaderOptionUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.OptionUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * @author by nisie on 8/21/17.
 */
class InboxReputationFilterAdapter private constructor(
    private val listener: FilterListener,
    private val listOption: ArrayList<OptionUiModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface FilterListener {
        fun onFilterSelected(optionUiModel: OptionUiModel?)
        fun onFilterUnselected(optionUiModel: OptionUiModel?)
    }

    inner class HeaderViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: Typography? = itemView.findViewById(R.id.title)

    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filter: Typography? = itemView.findViewById(R.id.filter)
        val check: ImageView? = itemView.findViewById(R.id.check)
        private val mainView: View? = itemView.findViewById(R.id.main_view)

        init {
            mainView?.setOnClickListener {
                listOption.forEach {
                    if (it === listOption.getOrNull(adapterPosition)) it.isSelected = !it.isSelected
                    else if (it.key === listOption.getOrNull(adapterPosition)?.key)
                    it.isSelected = false
                }
                if (listOption.getOrNull(adapterPosition)?.isSelected == true) {
                    listener.onFilterSelected(listOption.getOrNull(adapterPosition))
                } else {
                    listener.onFilterUnselected(listOption.getOrNull(adapterPosition))
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_HEADER -> return HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.listview_filter_header, parent, false)
            )
            else -> return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.listview_filter, parent, false)
            )
        }
    }

    override fun onBindViewHolder(
        parent: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (getItemViewType(position) == VIEW_HEADER) {
            val holder: HeaderViewHolder = parent as HeaderViewHolder
            holder.title?.text = listOption.getOrNull(position)?.name
        } else {
            val holder: ViewHolder = parent as ViewHolder
            holder.filter?.text = listOption.getOrNull(position)?.name
            if (listOption.getOrNull(position)?.isSelected == true) {
                holder.filter?.setTextColor(
                    MethodChecker.getColor(
                        holder.itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400
                    )
                )
                holder.check?.show()
            } else {
                holder.filter?.setTextColor(
                    MethodChecker.getColor(
                        holder.itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44
                    )
                )
                holder.check?.hide()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listOption.getOrNull(position) is HeaderOptionUiModel) VIEW_HEADER else super.getItemViewType(
            position
        )
    }

    override fun getItemCount(): Int {
        return listOption.size
    }

    fun resetFilter() {
        for (optionUiModel: OptionUiModel in listOption) {
            optionUiModel.isSelected = false
        }
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_HEADER: Int = 101

        fun createInstance(
            listener: FilterListener,
            listOption: ArrayList<OptionUiModel>
        ): InboxReputationFilterAdapter {
            return InboxReputationFilterAdapter(listener, listOption)
        }
    }
}