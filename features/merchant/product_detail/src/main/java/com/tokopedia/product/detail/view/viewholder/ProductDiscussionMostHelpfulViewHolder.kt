package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.adapter.ProductDiscussionQuestionsAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_discussion_most_helpful.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_local_load.view.*
import kotlinx.android.synthetic.main.partial_dynamic_discussion_most_helpful_empty_state.view.*

class ProductDiscussionMostHelpfulViewHolder(view: View,
                                             private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductDiscussionMostHelpfulDataModel>(view) {

    companion object {
        private const val EMPTY_TALK_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/talk_product_detail_empty.png"
        val LAYOUT = R.layout.item_dynamic_discussion_most_helpful
    }

    override fun bind(element: ProductDiscussionMostHelpfulDataModel) {
        with(element) {
            return when {
                questions == null && !isShimmering-> {
                    showLocalLoad()
                    hideEmptyState()
                    hideShimmer()
                    hideMultipleQuestion()
                    hideTitle()
                }
                isShimmering -> {
                    showShimmer()
                    hideEmptyState()
                    hideLocalLoad()
                    hideMultipleQuestion()
                    hideTitle()
                }
                (element.questions?.isEmpty() == true) -> {
                    showEmptyState(type, name)
                    hideLocalLoad()
                    hideShimmer()
                    hideTitle()
                    hideMultipleQuestion()
                }
                else -> {
                    showTitle()
                    showSeeAll(totalQuestion, type, name, questions?.size.toString())
                    showMultipleQuestions(questions, type, name)
                    hideEmptyState()
                    hideShimmer()
                    hideLocalLoad()
                }
            }
        }
    }

    private fun showEmptyState(type: String, name: String) {
        itemView.productDiscussionMostHelpfulEmptyLayout.apply {
            show()
            productDetailDiscussionEmptyButton.setOnClickListener {
                listener.onDiscussionSendQuestionClicked(ComponentTrackDataModel(type, name, adapterPosition + 1))
            }
            productDetailDiscussionEmptyImage.loadImage(EMPTY_TALK_IMAGE_URL)
        }
    }

    private fun showMultipleQuestions(questions: List<Question>?, type: String, name: String) {
        questions?.let {
            val questionsAdapter = ProductDiscussionQuestionsAdapter(it, listener, type, name, adapterPosition + 1)
            itemView.productDiscussionMostHelpfulQuestions.apply {
                adapter = questionsAdapter
                show()
            }
        }
    }

    private fun showTitle() {
        itemView.productDiscussionMostHelpfulTitle.show()
    }

    private fun showSeeAll(totalQuestion: Int, type: String, name: String, numberOfThreadsShown: String) {
        itemView.productDiscussionMostHelpfulSeeAll.apply {
            text = context.getString(R.string.product_detail_discussion_see_all, totalQuestion)
            setOnClickListener {
                listener.goToTalkReading(ComponentTrackDataModel(type, name, adapterPosition + 1), numberOfThreadsShown)
            }
            show()
        }
    }

    private fun showLocalLoad() {
        itemView.apply {
            productDiscussionLocalLoadLayout.show()
            productDetailDiscussionLocalLoad.apply {
                title?.text = getString(R.string.product_detail_discussion_local_load_title)
                description?.text = getString(R.string.product_detail_discussion_local_load_description)
                refreshBtn?.setOnClickListener {
                    listener.onDiscussionRefreshClicked()
                }
            }
        }
    }

    private fun hideMultipleQuestion() {
        itemView.productDiscussionMostHelpfulQuestions.hide()
    }

    private fun hideTitle() {
        itemView.apply {
            productDiscussionMostHelpfulTitle.hide()
            productDiscussionMostHelpfulSeeAll.hide()
        }
    }

    private fun showShimmer() {
        itemView.productDiscussionShimmerLayout.show()
    }

    private fun hideEmptyState() {
        itemView.productDiscussionMostHelpfulEmptyLayout.hide()
    }

    private fun hideShimmer() {
        itemView.productDiscussionShimmerLayout.hide()
    }

    private fun hideLocalLoad() {
        itemView.productDiscussionLocalLoadLayout.hide()
    }

}