package com.tokopedia.selleronboarding.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SlideUiModel
import kotlinx.android.synthetic.main.viewholder_sob_onboarding.view.*

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SlideAdapter : RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    private val items = mutableListOf<SlideUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.viewholder_sob_onboarding, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addSlideItem(item: SlideUiModel) {
        this.items.add(item)
    }

    fun clearSlideItems() {
        this.items.clear()
    }

    inner class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: SlideUiModel) = with(itemView) {
            tvHeaderText.text = item.headerText
            try {
                imgIllustrationSob.loadImageDrawable(item.drawableRes)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }
    }
}