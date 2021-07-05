package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter

import android.content.Context
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.brandlist.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import java.util.concurrent.TimeUnit


class BrandlistAlphabetHeaderAdapter(val listener: BrandlistHeaderBrandInterface):
        RecyclerView.Adapter<BrandlistAlphabetHeaderAdapter.BrandlistAlphabetHeaderViewHolder>() {

    private val DEFAULT_SELECTED_POSITION = 1
    var headerList: MutableList<String> = mutableListOf()
    var selectedPosition = DEFAULT_SELECTED_POSITION
    var recyclerViewState: Parcelable? = null
    var lastTimeChipsClicked: Long = 0L
    private val startPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandlistAlphabetHeaderViewHolder {
        return BrandlistAlphabetHeaderViewHolder(parent.inflateLayout(R.layout.brandlist_item_alphabet_header_chip))
    }

    override fun getItemCount(): Int {
        return headerList.size
    }

    override fun onBindViewHolder(holder: BrandlistAlphabetHeaderViewHolder, position: Int) {
        holder.bindData(headerList[position], position)
    }

    inner class BrandlistAlphabetHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val context: Context
        var chipTextView: TextView
        var chipContainer: LinearLayout

        init {
            context = itemView.context
            chipContainer = itemView.findViewById(R.id.chip_alphabet_header)
            chipTextView = itemView.findViewById(R.id.chip_textview)
        }

        fun bindData(headerItem: String, position: Int) {
            chipTextView.text = headerItem

            if (position == startPosition){
                setDefaultText()
            }

            if (selectedPosition == position && position != startPosition) {
                isSelectedChips(true)
            } else if (selectedPosition != position && position != startPosition) {
                isSelectedChips(false)
            }

            chipContainer.setOnClickListener {
                if (position != selectedPosition && position != startPosition) {
                    val current: Long = System.currentTimeMillis()
                    val isMoreThanTwoSeconds: Boolean = current >= lastTimeChipsClicked + TimeUnit.SECONDS.toMillis(2) // Prevent spam click
                    if (isMoreThanTwoSeconds) {
                        lastTimeChipsClicked = current
                        selectedPosition = position
                        notifyDataSetChanged()
                        listener.onClickedChip(
                                position,
                                headerItem,
                                current,
                                recyclerViewState)
                    }
                }
            }
        }

        private fun setDefaultText() {
            chipContainer.background = ContextCompat.getDrawable(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            chipTextView.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200))
        }

        private fun isSelectedChips(isSelected: Boolean) {
            if (isSelected) {
                chipContainer.background = ContextCompat.getDrawable(context, R.drawable.chip_selected_small)
                chipTextView.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
            } else {
                chipContainer.background = ContextCompat.getDrawable(context, R.drawable.chip_normal_small)
                chipTextView.setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N200))
            }
        }
    }

}