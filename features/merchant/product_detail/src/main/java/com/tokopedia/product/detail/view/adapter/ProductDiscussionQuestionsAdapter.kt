package com.tokopedia.product.detail.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDiscussionQuestionViewHolder

class ProductDiscussionQuestionsAdapter(
        private val questions: List<Question>,
        private val dynamicProductDetailListener: DynamicProductDetailListener,
        private val type: String,
        private val name: String,
        private val adapterPositon: Int
): RecyclerView.Adapter<ProductDiscussionQuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDiscussionQuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dynamic_discussion_most_helpful_question_and_answer, parent, false)
        return ProductDiscussionQuestionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: ProductDiscussionQuestionViewHolder, position: Int) {
        holder.bind(questions[position], dynamicProductDetailListener, type, name, adapterPositon, itemCount)
    }
}