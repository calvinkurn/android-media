package com.tokopedia.onboarding.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.onboarding.view.listener.UsernameInputContract
import com.tokopedia.profile.R
import java.util.*

/**
 * @author by milhamj on 10/4/18.
 */
class SuggestionAdapter(private val listener: UsernameInputContract.View)
    : RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {
    private val list: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_af_username, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position]
        holder.itemView.setOnClickListener { v -> listener.onSuggestionClicked(list[position]) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(suggestions: List<String>) {
        this.list.clear()
        this.list.addAll(suggestions)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
    }
}
