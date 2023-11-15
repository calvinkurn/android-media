package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetAutopayBinding
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetCloseProcessListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetListener
import com.tokopedia.digital.home.presentation.listener.TodoWidgetItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.CATEGORY_TYPE
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.POSTPAIDREMINDER_TYPE
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.digital.home.R as digitalhomeR

class RechargeHomepageTodoWidgetAutoPayViewHolder(
    val binding: ViewRechargeHomeTodoWidgetAutopayBinding,
    private val todoWidgetListener: RechargeHomepageTodoWidgetListener,
    private val closeItemListener: RechargeHomepageTodoWidgetCloseProcessListener,
    private val todoWidgetItemListener: TodoWidgetItemListener,
    private val todoWidgetSectionId: String,
    private val todoWidgetSectionName: String,
) : AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel>(
    binding.root
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_todo_widget_autopay
        private const val MAX_LINE_NON_CATEGORY = 1
        private const val MAX_LINE_CATEGORY = 2
    }

    override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel) {
        renderTodoLayout(binding, element)
    }

    private fun renderTodoLayout(
        binding: ViewRechargeHomeTodoWidgetAutopayBinding,
        element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel
    ) {
        with(binding) {
            element.widget.let { widget ->
                imgIconTodoWidget.loadImage(widget.iconUrl)

                if (widget.isCloseButton) {
                    imgCloseTodoWidget.show()
                    imgCloseTodoWidget.setOnClickListener {
                        closeItemListener.onCloseWidget(element)
                    }
                } else {
                    imgCloseTodoWidget.hide()
                }

                tgCategoryNameTodoWidget.text = widget.title
                tgProductNameTodoWidget.text = widget.subtitle
                tgFavoriteNumberTodoWidget.text = widget.label
                tgPriceTodoWidget.text = widget.price
                tgSlashPriceTodoWidget.text = widget.slashedPrice
                tgPercentageTodoWidget.text = widget.discount

                if (widget.type.startsWith(CATEGORY_TYPE)) {
                    tgFavoriteNumberTodoWidget.maxLines = MAX_LINE_CATEGORY
                } else {
                    tgFavoriteNumberTodoWidget.maxLines = MAX_LINE_NON_CATEGORY
                }

                if (widget.price.isEmpty()) {
                    val labelParams = iconThreeButtonTodoWidget.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    labelParams.topToTop = tgFavoriteNumberTodoWidget.id
                    labelParams.bottomToBottom = tgFavoriteNumberTodoWidget.id
                    iconThreeButtonTodoWidget.layoutParams = labelParams

                    tgFavoriteNumberTodoWidget.apply {
                        setMargin(
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_32),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )
                    }
                } else {
                    val labelParams = iconThreeButtonTodoWidget.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    labelParams.bottomToBottom = tgPriceTodoWidget.id
                    labelParams.topToTop = tgPriceTodoWidget.id
                    iconThreeButtonTodoWidget.layoutParams = labelParams

                    tgFavoriteNumberTodoWidget.apply {
                        setMargin(
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_12),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )
                    }
                }

                if (widget.button.isNotEmpty()) {
                    btnMainTodoWidget.show()
                    btnMainTodoWidget.text = widget.button
                    btnMainTodoWidget.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(widget, true, todoWidgetSectionId, todoWidgetSectionName)
                    }
                } else {
                    btnMainTodoWidget.hide()
                }

                if (widget.optionButtons.isNotEmpty()) {
                    iconThreeButtonTodoWidget.show()
                    iconThreeButtonTodoWidget.setOnClickListener {
                        todoWidgetListener.onClickThreeButton(widget.optionButtons)
                    }

                    btnMainTodoWidget.apply {
                        setMargin(
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_8),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )
                    }
                } else {
                    iconThreeButtonTodoWidget.hide()

                    btnMainTodoWidget.apply {
                        setMargin(
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )
                    }
                }

                if (widget.reason.isNotEmpty()) {
                    tgInfoTodoWidget.show()
                    viewInfoTodoWidget.show()
                    tgInfoTodoWidget.text = widget.reason

                    val labelParams = btnMainTodoWidget.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.endToStart = iconThreeButtonTodoWidget.id
                    labelParams.endToEnd = ConstraintLayout.LayoutParams.UNSET
                    labelParams.bottomToBottom = iconThreeButtonTodoWidget.id
                    labelParams.topToTop = iconThreeButtonTodoWidget.id
                    btnMainTodoWidget.layoutParams = labelParams
                } else {
                    tgInfoTodoWidget.hide()
                    viewInfoTodoWidget.hide()

                    val labelParams = btnMainTodoWidget.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.endToStart = ConstraintLayout.LayoutParams.UNSET
                    labelParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    if (widget.price.isNotEmpty()) {
                        labelParams.bottomToBottom = tgPriceTodoWidget.id
                        labelParams.topToTop = tgPriceTodoWidget.id
                    } else {
                        labelParams.bottomToBottom = tgFavoriteNumberTodoWidget.id
                        labelParams.topToTop = tgFavoriteNumberTodoWidget.id
                    }

                    btnMainTodoWidget.layoutParams = labelParams
                }

                if (widget.button.isEmpty()) {
                    root.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(widget, false, todoWidgetSectionId, todoWidgetSectionName)
                    }
                }

                setCardHeightMatchParent()
                setCardWidth(
                    (widget.type.startsWith(POSTPAIDREMINDER_TYPE) || widget.type.startsWith(CATEGORY_TYPE))
                        && (todoWidgetItemListener.getListSize() > Int.ONE)
                )
            }
        }
    }

    private fun setCardHeightMatchParent() {
        with(binding) {
            val layoutParams = todoWidgetMainCard.layoutParams
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            todoWidgetMainCard.layoutParams = layoutParams
        }
    }

    private fun setCardWidth(isSmallWidth: Boolean) {
        with(binding) {
            val layoutParams = todoWidgetMainCard.layoutParams
            layoutParams?.width = if (isSmallWidth) {
                todoWidgetMainCard.getDimens(digitalhomeR.dimen.todo_widget_autopay)
            } else {
                ViewGroup.LayoutParams.MATCH_PARENT
            }
            todoWidgetMainCard.layoutParams = layoutParams
        }
    }
}
