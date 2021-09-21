package com.tokopedia.sellerfeedback.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerfeedback.R
import com.tokopedia.unifyprinciples.Typography

class FeedbackPageAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<FeedbackPageAdapter.ViewHolder>() {

    private val pagesTitle = mutableListOf<String>()
    private var selectedTitle = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pagesTitle[position]
        holder.title.text = item
        holder.radio.isChecked = item == selectedTitle

        holder.bind()
    }

    override fun getItemCount() = pagesTitle.size

    fun updateList(pagesTitle: List<String>) {
        this.pagesTitle.clear()
        this.pagesTitle.addAll(pagesTitle)
        notifyDataSetChanged()
    }

    fun setSelected(selectedTitle: String) {
        this.selectedTitle = selectedTitle
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: Typography = view.findViewById(R.id.title)
        val radio: RadioButton = view.findViewById(R.id.radio_button)

        fun bind() {
            radio.setOnClickListener { onItemClick() }
            view.setOnClickListener { onItemClick() }
        }

        private fun onItemClick() {
            listener.onItemClick(title.text.toString())
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(title: String)
    }

}