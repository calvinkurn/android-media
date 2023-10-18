package com.tokopedia.shop.open.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.data.model.Choice
import com.tokopedia.shop.open.listener.SurveyListener

class ShopOpenRevampChoiceItemAdapter(
    private val choiceListData: List<Choice>,
    val listener: SurveyListener,
    private val questionId: Int
) : RecyclerView.Adapter<ShopOpenRevampChoiceItemAdapter.ShopOpenRevampChoiceItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopOpenRevampChoiceItemViewHolder {
        return ShopOpenRevampChoiceItemViewHolder(parent.inflateLayout(R.layout.shop_open_revamp_item_choice))
    }

    override fun getItemCount(): Int {
        return choiceListData.size
    }

    override fun onBindViewHolder(holder: ShopOpenRevampChoiceItemViewHolder, position: Int) {
        holder.bindData(
            choiceListData[position],
            choiceListData[position].id
        )
    }

    inner class ShopOpenRevampChoiceItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context
        val txtChoice: TextView
        val checkBox: CheckBox
        var isOnChecked = false

        init {
            context = itemView.context
            txtChoice = itemView.findViewById(R.id.text_choice)
            checkBox = itemView.findViewById(R.id.checkbox_choice)
        }

        fun bindData(choice: Choice, id: Int) {
            txtChoice.text = choice.choice
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    isOnChecked = true
                    checkedCheckbox(id)
                } else {
                    isOnChecked = false
                    uncheckedCheckbox(id)
                }
            }
            txtChoice.setOnClickListener {
                if (isOnChecked) {
                    isOnChecked = false
                    checkBox.isChecked = false
                    uncheckedCheckbox(id)
                } else {
                    isOnChecked = true
                    checkBox.isChecked = true
                    checkedCheckbox(id)
                }
            }
        }

        private fun checkedCheckbox(id: Int) {
            listener.onCheckedCheckbox(questionId, id)
        }

        private fun uncheckedCheckbox(id: Int) {
            listener.onUncheckedCheckbox(questionId, id)
        }
    }
}
