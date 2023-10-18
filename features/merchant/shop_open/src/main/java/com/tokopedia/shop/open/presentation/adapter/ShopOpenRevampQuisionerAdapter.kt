package com.tokopedia.shop.open.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.data.model.Question
import com.tokopedia.shop.open.listener.SurveyListener

class ShopOpenRevampQuisionerAdapter(
    val surveyListener: SurveyListener
) : RecyclerView.Adapter<ShopOpenRevampQuisionerAdapter.ShopOpenRevampQuisionerViewHolder>() {

    private var questionListData: MutableList<Question> = mutableListOf()

    fun updateDataQuestionsList(questionList: List<Question>) {
        questionListData = questionList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopOpenRevampQuisionerViewHolder {
        return ShopOpenRevampQuisionerViewHolder(parent.inflateLayout(R.layout.shop_open_revamp_item_survey_container))
    }

    override fun getItemCount(): Int {
        return questionListData.size
    }

    override fun onBindViewHolder(holder: ShopOpenRevampQuisionerViewHolder, position: Int) {
        holder.bindData(questionListData[position])
    }

    inner class ShopOpenRevampQuisionerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var recyclerView: RecyclerView? = null
        private var layoutManager: LinearLayoutManager? = null
        private var adapter: ShopOpenRevampChoiceItemAdapter? = null
        val context: Context
        var txtQuestionTitle: TextView

        init {
            context = itemView.context
            txtQuestionTitle = itemView.findViewById(R.id.text_question_title_quisioner_page)
            recyclerView = itemView.findViewById(R.id.recycler_view_choice_list)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        fun bindData(question: Question) {
            if (question.type == 1) {
                setupChoicesView(question)
            }
        }

        private fun setupChoicesView(question: Question) {
            txtQuestionTitle.text = question.question
            recyclerView?.layoutManager = layoutManager
            adapter = ShopOpenRevampChoiceItemAdapter(question.choices, surveyListener, question.id)
            recyclerView?.adapter = adapter
        }
    }
}
