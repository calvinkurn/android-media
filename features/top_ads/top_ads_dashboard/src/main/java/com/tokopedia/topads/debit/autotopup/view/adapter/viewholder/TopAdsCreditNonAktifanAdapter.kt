package com.tokopedia.topads.debit.autotopup.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.RvItemTopadsCreditOtomatisBinding

class TopAdsCreditNonAktifanAdapter :
    RecyclerView.Adapter<TopAdsCreditNonAktifanAdapter.ViewHolder>() {

    private val list = getList()

    class ViewHolder(private val binding: RvItemTopadsCreditOtomatisBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pair: Pair<Int, Int>) {
            binding.apply {
                txtTitle.text = txtTitle.resources.getString(pair.first)
                txtDescription.text = txtTitle.resources.getString(pair.second)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemTopadsCreditOtomatisBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getList() = listOf<Pair<Int, Int>>(
        Pair(R.string.topads_credit_otomatis_title_1, R.string.topads_credit_otomatis_desc_1),
        Pair(R.string.topads_credit_otomatis_title_2, R.string.topads_credit_otomatis_desc_2),
        Pair(R.string.topads_credit_otomatis_title_3, R.string.topads_credit_otomatis_desc_3)
    )
}