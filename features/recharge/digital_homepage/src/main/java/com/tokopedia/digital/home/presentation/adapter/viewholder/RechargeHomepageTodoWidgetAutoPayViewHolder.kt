package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetAutopayBinding
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.CATEGORY_TYPE
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.POSTPAIDREMINDER_TYPE
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class RechargeHomepageTodoWidgetAutoPayViewHolder(
    val binding: ViewRechargeHomeTodoWidgetAutopayBinding,
    val todoWidgetListener: RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetListener,
    val closeItemListener: RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetCloseProcess
) : AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel>(
    binding.root
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_todo_widget_autopay
    }

    override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel) {
        renderCommonTodoLayout(binding, element)
    }

    fun renderCommonTodoLayout(
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

                if (widget.type.startsWith(CATEGORY_TYPE)) {
                    tgFavoriteNumberTodoWidget.maxLines = 2
                } else {
                    tgFavoriteNumberTodoWidget.maxLines = 1
                }

                if (widget.price.isEmpty()) {
                    val labelParams = iconThreeButtonTodoWidget.layoutParams as ConstraintLayout.LayoutParams
                    labelParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    labelParams.topToTop = tgFavoriteNumberTodoWidget.id
                    labelParams.bottomToBottom = tgFavoriteNumberTodoWidget.id
                    iconThreeButtonTodoWidget.layoutParams = labelParams

                    tgFavoriteNumberTodoWidget.apply {
                        setMargin(
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_32),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
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
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        )
                    }
                }

                if (widget.button.isNotEmpty()) {
                    btnMainTodoWidget.show()
                    btnMainTodoWidget.text = widget.button
                    btnMainTodoWidget.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(widget.buttonAppLink)
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
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        )
                    }
                } else {
                    iconThreeButtonTodoWidget.hide()

                    btnMainTodoWidget.apply {
                        setMargin(
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
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

                root.setOnClickListener {
                    todoWidgetListener.onClickTodoWidget(widget.appLink)
                }

                setCardHeightMatchParent()
                setCardWidth(widget.type.startsWith(POSTPAIDREMINDER_TYPE) ||
                    widget.type.startsWith(CATEGORY_TYPE))
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

    private fun setCardWidth(isPostPaid: Boolean) {
        with(binding) {
            val layoutParams = todoWidgetMainCard.layoutParams
            layoutParams?.width = if (isPostPaid)  todoWidgetMainCard.getDimens(com.tokopedia.digital.home.R.dimen.todo_widget_autopay) else ViewGroup.LayoutParams.MATCH_PARENT
            todoWidgetMainCard.layoutParams = layoutParams
        }
    }
}
