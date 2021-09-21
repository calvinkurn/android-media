package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireOptionUiModel
import kotlinx.android.synthetic.main.item_pm_questionnaire_option_item.view.*

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireOptionAdapter(
        private val items: List<QuestionnaireOptionUiModel>
) : RecyclerView.Adapter<QuestionnaireOptionAdapter.QuestionnaireOptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireOptionViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pm_questionnaire_option_item, parent, false)
        return QuestionnaireOptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionnaireOptionViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class QuestionnaireOptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: QuestionnaireOptionUiModel) {
            with(itemView) {
                tvPmQuestionnaireItem.text = item.text
                cbPmQuestionnaireOption.isChecked = item.isChecked

                cbPmQuestionnaireOption.setOnCheckedChangeListener { _, isChecked ->
                    item.isChecked = isChecked
                }

                setOnClickListener {
                    cbPmQuestionnaireOption.isChecked = !cbPmQuestionnaireOption.isChecked
                }
            }
        }
    }
}