package com.tokopedia.digital.home.presentation.adapter.viewholder

import com.tokopedia.digital.home.R
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeListTodoWidgetBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetAutopayBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetBayarSekaligusBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetPostReminderBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageTodoWidgetAdapterTypeFactory
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class RechargeHomepageTodoWidgetViewHolder (
    val binding: ViewRechargeHomeListTodoWidgetBinding,
    val listener: RechargeHomepageItemListener
): AbstractViewHolder<RechargeHomepageTodoWidgetModel>(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_list_todo_widget
        private const val SIZE_MAX = 2
        private const val BAYAR_SEKALIGUS_TYPE = "BAYAR_SEKALIGUS"
        private const val POSTPAIDREMINDER_TYPE = "POSTPAIDREMINDER"
        private const val AUTOPAY_TYPE = "AUTOPAY"
    }

    override fun bind(element: RechargeHomepageTodoWidgetModel) {
        if (element.section.items.isNotEmpty()) {
            with(binding) {
                titleTodoWidgetFirst.text = element.section.items.first().title

                with(binding.rvTodoWidgetFirst) {
                   val list = mapperTodoList(element.section.items.first().widgets)
                   adapter = BaseAdapter(
                       RechargeHomepageTodoWidgetAdapterTypeFactory(),
                       list
                   )
                   layoutManager= LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                }


                if (element.section.items.size >= SIZE_MAX) {
                    titleTodoWidgetSecond.text = element.section.items.second().title
                    with(binding.rvTodoWidgetSecond) {
                        adapter = BaseAdapter(
                            RechargeHomepageTodoWidgetAdapterTypeFactory(),
                            mapperTodoList(element.section.items.second().widgets)
                        )
                        layoutManager= LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
        } else {
            listener.loadRechargeSectionData(element.section.id, element.section.name)
        }
    }

    class RechargeHomepageTodoWidgetPostReminderViewHolder(
        val binding: ViewRechargeHomeTodoWidgetPostReminderBinding
    ): AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel>(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget_post_reminder
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel) {
           renderCommonTodoLayout(binding, element.widget)
        }

        fun renderCommonTodoLayout(binding: ViewRechargeHomeTodoWidgetPostReminderBinding, widget: RechargeHomepageSections.Widgets) {
            with(binding) {
                imgIconTodoWidget.loadImage(widget.iconUrl)

                if (widget.isCloseButton) {
                    imgCloseTodoWidget.show()
                } else {
                    imgCloseTodoWidget.hide()
                }

                tgCategoryNameTodoWidget.text= widget.title
                tgProductNameTodoWidget.text = widget.subtitle
                tgFavoriteNumberTodoWidget.text = widget.label
                tgPriceTodoWidget.text = widget.price

                if (widget.button.isNotEmpty()) {
                    btnMainTodoWidget.show()
                    btnMainTodoWidget.text = widget.button
                } else {
                    btnMainTodoWidget.hide()
                }

                if (widget.optionButtons.isNotEmpty()) {
                    iconThreeButtonTodoWidget.show()
                } else {
                    iconThreeButtonTodoWidget.hide()
                }

                if (widget.reason.isNotEmpty()) {
                    tgInfoTodoWidget.show()
                    viewInfoTodoWidget.show()
                    tgInfoTodoWidget.text = widget.reason
                } else {
                    tgInfoTodoWidget.hide()
                    viewInfoTodoWidget.hide()
                }
            }
        }
    }

    class RechargeHomepageTodoWidgetAutoPayViewHolder(
        val binding: ViewRechargeHomeTodoWidgetAutopayBinding
    ): AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel>(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget_autopay
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel) {
            renderCommonTodoLayout(binding, element.widget)
        }

        fun renderCommonTodoLayout(binding: ViewRechargeHomeTodoWidgetAutopayBinding, widget: RechargeHomepageSections.Widgets) {
            with(binding) {
                imgIconTodoWidget.loadImage(widget.iconUrl)

                if (widget.isCloseButton) {
                    imgCloseTodoWidget.show()
                } else {
                    imgCloseTodoWidget.hide()
                }

                tgCategoryNameTodoWidget.text= widget.title
                tgProductNameTodoWidget.text = widget.subtitle
                tgFavoriteNumberTodoWidget.text = widget.label
                tgPriceTodoWidget.text = widget.price

                if (widget.button.isNotEmpty()) {
                    btnMainTodoWidget.show()
                    btnMainTodoWidget.text = widget.button
                } else {
                    btnMainTodoWidget.hide()
                }

                if (widget.optionButtons.isNotEmpty()) {
                    iconThreeButtonTodoWidget.show()
                } else {
                    iconThreeButtonTodoWidget.hide()
                }

                if (widget.reason.isNotEmpty()) {
                    tgInfoTodoWidget.show()
                    viewInfoTodoWidget.show()
                    tgInfoTodoWidget.text = widget.reason
                } else {
                    tgInfoTodoWidget.hide()
                    viewInfoTodoWidget.hide()
                }
            }
        }
    }

    class RechargeHomepageTodoWidgetBayarSekaligusViewHolder(
        val binding: ViewRechargeHomeTodoWidgetBayarSekaligusBinding
    ): AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetBayarSekaligusItemModel>(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget_bayar_sekaligus
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetBayarSekaligusItemModel) {
            with(binding) {
                imgTodoBackground.loadImage(element.widget.iconUrl)
            }
        }
    }

    private fun <T> List<T>.second(): T {
        if (isEmpty())
            throw NoSuchElementException("List is empty.")
        return this[SIZE_MAX - Int.ONE]
    }

    private fun mapperTodoList(widgets: List<RechargeHomepageSections.Widgets>): List<Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>> {
        val itemList = mutableListOf<Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>>()
        widgets.forEach {
            val item = when (it.type) {
                AUTOPAY_TYPE -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel(it)
                POSTPAIDREMINDER_TYPE -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel(it)
                BAYAR_SEKALIGUS_TYPE -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetBayarSekaligusItemModel(it)
                else -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel(it)
            }

            itemList.add(item)
        }

        return itemList.toList()
    }
}
