package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDiscussionMostHelpfulDataModel
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.databinding.ItemDynamicDiscussionMostHelpfulBinding
import com.tokopedia.product.detail.view.adapter.ProductDiscussionQuestionsAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductDiscussionMostHelpfulViewHolder(private val view: View,
                                             private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductDiscussionMostHelpfulDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_discussion_most_helpful
    }

    private val binding = ItemDynamicDiscussionMostHelpfulBinding.bind(view)

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
            }.also {
                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
            }
        }
    }

    private fun showEmptyState(type: String, name: String) {
        binding.productDiscussionMostHelpfulEmptyLayout.apply {
            root.show()
            productDetailDiscussionEmptyButton.setOnClickListener {
                listener.onDiscussionSendQuestionClicked(ComponentTrackDataModel(type, name, adapterPosition + 1))
            }
        }
    }

    private fun showMultipleQuestions(questions: List<Question>?, type: String, name: String) {
        questions?.let {
            val questionsAdapter = ProductDiscussionQuestionsAdapter(it, listener, type, name, adapterPosition + 1)
            binding.productDiscussionMostHelpfulQuestions.apply {
                adapter = questionsAdapter
                show()
            }
        }
    }

    private fun showTitle() {
        binding.productDiscussionMostHelpfulTitle.show()
    }

    private fun showSeeAll(totalQuestion: Int, type: String, name: String, numberOfThreadsShown: String) {
        binding.productDiscussionMostHelpfulSeeAll.apply {
            text = context.getString(R.string.product_detail_discussion_see_all, totalQuestion)
            setOnClickListener {
                listener.goToTalkReading(ComponentTrackDataModel(type, name, adapterPosition + 1), numberOfThreadsShown)
            }
            show()
        }
    }

    private fun showLocalLoad() {
        binding.productDiscussionLocalLoadLayout.apply {
            root.show()
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
        binding.productDiscussionMostHelpfulQuestions.hide()
    }

    private fun hideTitle() {
        binding.apply {
            productDiscussionMostHelpfulTitle.hide()
            productDiscussionMostHelpfulSeeAll.hide()
        }
    }

    private fun showShimmer() {
        binding.productDiscussionShimmerLayout.root.show()
    }

    private fun hideEmptyState() {
        binding.productDiscussionMostHelpfulEmptyLayout.root.hide()
    }

    private fun hideShimmer() {
        binding.productDiscussionShimmerLayout.root.hide()
    }

    private fun hideLocalLoad() {
        binding.productDiscussionLocalLoadLayout.root.hide()
    }

    private fun getComponentTrackData(
        element: ProductDiscussionMostHelpfulDataModel
    ) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

}
