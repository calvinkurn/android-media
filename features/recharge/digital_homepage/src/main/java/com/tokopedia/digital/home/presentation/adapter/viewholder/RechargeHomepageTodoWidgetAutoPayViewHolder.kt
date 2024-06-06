package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetAutopayBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetCloseProcessListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetListener
import com.tokopedia.digital.home.presentation.listener.TodoWidgetItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.CATEGORY_TYPE
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.POSTPAIDREMINDER_TYPE
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.PREPAIDREMINDER_TYPE
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loaderfresco.loadImageFresco
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.digital.home.R as digitalhomeR

class RechargeHomepageTodoWidgetAutoPayViewHolder(
    val binding: ViewRechargeHomeTodoWidgetAutopayBinding,
    private val todoWidgetListener: RechargeHomepageTodoWidgetListener,
    private val closeItemListener: RechargeHomepageTodoWidgetCloseProcessListener,
    private val todoWidgetItemListener: TodoWidgetItemListener,
    private val todoWidgetSectionId: String,
    private val todoWidgetSectionName: String,
    private val todoWidgetSectionTemplate: String
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
                renderImgIcon(widget, binding)
                renderCloseButton(element, binding)
                renderText(widget, binding)
                renderFavoriteNumber(widget, binding)
                renderButton(widget, binding)
                renderReason(widget, binding)

                if (widget.button.isEmpty()) {
                    root.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(
                            widget, false, todoWidgetSectionId,
                            todoWidgetSectionName, todoWidgetSectionTemplate
                        )
                    }
                }


                setCardHeightMatchParent()
                setCardWidth(smallCardType(widget) && (todoWidgetItemListener.getListSize() > Int.ONE))
            }
        }
    }

    private fun smallCardType(widget: RechargeHomepageSections.Widgets): Boolean {
        return (widget.type.startsWith(POSTPAIDREMINDER_TYPE) || widget.type.startsWith(
            PREPAIDREMINDER_TYPE) || widget.type.startsWith(CATEGORY_TYPE))
    }

    private fun renderImgIcon(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            imgIconTodoWidget.loadImageFresco(widget.iconUrl)
        }
    }

    private fun renderCloseButton(
        element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            if (element.widget.isCloseButton) {
                imgCloseTodoWidget.show()
                imgCloseTodoWidget.setOnClickListener {
                    closeItemListener.onCloseWidget(element)
                }
            } else {
                imgCloseTodoWidget.hide()
            }
        }
    }

    private fun renderText(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            tgCategoryNameTodoWidget.text = widget.title
            tgProductNameTodoWidget.text = widget.subtitle
            tgPriceTodoWidget.text = widget.price
            tgPercentageTodoWidget.text = widget.discount
            tgSlashPriceTodoWidget.run {
                    text = widget.slashedPrice
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun renderFavoriteNumber(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            tgFavoriteNumberTodoWidget.text = widget.label
            if (widget.type.startsWith(CATEGORY_TYPE)) {
                tgFavoriteNumberTodoWidget.maxLines = MAX_LINE_CATEGORY
            } else {
                tgFavoriteNumberTodoWidget.maxLines = MAX_LINE_NON_CATEGORY
            }

            if (widget.price.isEmpty()) {
                renderPriceEmpty(widget, binding)
            } else {
                renderPriceNotEmpty(binding)
            }
        }
    }

    private fun renderPriceEmpty(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            val labelParams =
                iconThreeButtonTodoWidget.layoutParams as ConstraintLayout.LayoutParams
            labelParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            labelParams.topToTop = tgFavoriteNumberTodoWidget.id
            labelParams.bottomToBottom = tgFavoriteNumberTodoWidget.id
            iconThreeButtonTodoWidget.layoutParams = labelParams

            if (!smallCardType(widget)) {
                tgFavoriteNumberTodoWidget.apply {
                    setMargin(
                        getDimens(unifyprinciplesR.dimen.unify_space_0),
                        getDimens(unifyprinciplesR.dimen.unify_space_32),
                        getDimens(unifyprinciplesR.dimen.unify_space_0),
                        getDimens(unifyprinciplesR.dimen.unify_space_0)
                    )
                }
            } else {
                tgFavoriteNumberTodoWidget.apply {
                    setMargin(
                        getDimens(unifyprinciplesR.dimen.unify_space_0),
                        getDimens(unifyprinciplesR.dimen.unify_space_8),
                        getDimens(unifyprinciplesR.dimen.unify_space_0),
                        getDimens(unifyprinciplesR.dimen.unify_space_0)
                    )
                }
            }
        }
    }

    private fun renderPriceNotEmpty(
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            val labelParams =
                iconThreeButtonTodoWidget.layoutParams as ConstraintLayout.LayoutParams
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
    }

    private fun renderButton(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        if (widget.button.isNotEmpty()) {
            renderButtonNotEmpty(widget, binding)
        } else {
            renderButtonEmpty(binding)
        }

        if (widget.optionButtons.isNotEmpty()) {
            renderOptionButtonNotEmpty(widget, binding)
        } else {
            renderOptionButtonEmpty(binding)
        }
    }

    private fun renderButtonNotEmpty(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            btnMainTodoWidget.show()
            btnMainTodoWidget.text = widget.button
            btnMainTodoWidget.setOnClickListener {
                todoWidgetListener.onClickTodoWidget(
                    widget, true, todoWidgetSectionId,
                    todoWidgetSectionName, todoWidgetSectionTemplate
                )
            }
        }
    }

    private fun renderButtonEmpty(
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            btnMainTodoWidget.hide()
        }
    }

    private fun renderOptionButtonNotEmpty(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            iconThreeButtonTodoWidget.addOnImpressionListener(
                widget
            ){
               todoWidgetListener.onImpressThreeButton(widget)
            }
            iconThreeButtonTodoWidget.show()
            iconThreeButtonTodoWidget.setOnClickListener {
                todoWidgetListener.onClickThreeButton(widget)
            }

            btnMainTodoWidget.apply {
                setMargin(
                    getDimens(unifyprinciplesR.dimen.unify_space_0),
                    getDimens(unifyprinciplesR.dimen.unify_space_0),
                    getDimens(unifyprinciplesR.dimen.unify_space_8),
                    getDimens(unifyprinciplesR.dimen.unify_space_0)
                )
            }
        }
    }

    private fun renderOptionButtonEmpty(
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
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
    }

    private fun renderReason(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        if (widget.reason.isNotEmpty()) {
            renderReasonNotEmpty(widget, binding)
        } else {
            renderReasonEmpty(widget, binding)
        }
    }

    private fun renderReasonNotEmpty(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
            tgInfoTodoWidget.show()
            viewInfoTodoWidget.show()
            tgInfoTodoWidget.text = widget.reason

            val labelParams = btnMainTodoWidget.layoutParams as ConstraintLayout.LayoutParams
            labelParams.endToStart = iconThreeButtonTodoWidget.id
            labelParams.endToEnd = ConstraintLayout.LayoutParams.UNSET
            labelParams.bottomToBottom = iconThreeButtonTodoWidget.id
            labelParams.topToTop = iconThreeButtonTodoWidget.id
            btnMainTodoWidget.layoutParams = labelParams
        }
    }

    private fun renderReasonEmpty(
        widget: RechargeHomepageSections.Widgets,
        binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ) {
        with(binding) {
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
