package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.adapter.decoration.RechargeTodoWidgetSpaceDecorator
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageTodoWidgetViewHolder
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.SPACE_DP
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show

class RechargeHomepageTodoWidgetAdapter(
    var items: List<RechargeHomepageSections.Item>,
    val todoWidgetListener: RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetListener,
): RecyclerView.Adapter<RechargeHomepageTodoWidgetAdapter.RechargeHomeTodoWidgetListViewHolder>(){

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RechargeHomeTodoWidgetListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RechargeHomeTodoWidgetListViewHolder {
        val view = ViewRechargeHomeTodoWidgetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RechargeHomeTodoWidgetListViewHolder(view)
    }

    private val factory = RechargeHomepageTodoWidgetAdapterTypeFactory(todoWidgetListener, object :
        RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetCloseProcess {
        override fun onCloseWidget(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
            removeItem(element)
        }
    })

    private val baseAdapter = BaseAdapter(
        factory
    )

    fun removeItem(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
        baseAdapter.removeElement(element)
    }

    inner class RechargeHomeTodoWidgetListViewHolder(val binding: ViewRechargeHomeTodoWidgetBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RechargeHomepageSections.Item) {
            with(binding) {
                if (item.title.isEmpty()) {
                    titleTodoWidget.hide()
                } else {
                    titleTodoWidget.show()
                    titleTodoWidget.text = item.title
                }
                val stickyLayoutFirst = getStickyLayout(item.widgets)
                if (stickyLayoutFirst != null) {
                    rvTodoWidget.apply {
                        setMargin(
                            getDimens(com.tokopedia.digital.home.R.dimen.product_card_width_bayar_sekaligus_todo_widget),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        )
                    }
                    viewStickyLayout.root.show()
                    viewStickyLayout.tgTitleTodoWidget.text = stickyLayoutFirst.title
                    viewStickyLayout.tgSubTitleTodoWidget.text = stickyLayoutFirst.subtitle
                    viewStickyLayout.imgTodoWidgetBackground.loadImage(TokopediaImageUrl.RECHARGE_SUBHOME_TODO_WIDGET)
                    viewStickyLayout.root.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(stickyLayoutFirst.appLink)
                    }
                } else {
                    viewStickyLayout.root.hide()
                    rvTodoWidget.apply {
                        setMargin(
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        )
                    }
                }

                with(binding.rvTodoWidget) {
                    show()
                    baseAdapter.setVisitables(mapperTodoList(item.widgets).toList())
                    adapter = baseAdapter
                    layoutManager =
                        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                    val displayMetrics = itemView.context.resources.displayMetrics
                    removeAllItemDecoration()
                    addItemDecoration(
                        RechargeTodoWidgetSpaceDecorator(
                            SPACE_DP.dpToPx(displayMetrics),
                            (stickyLayoutFirst != null)
                        )
                    )
                }
            }
        }
    }

    private fun getStickyLayout(widgets: List<RechargeHomepageSections.Widgets>): RechargeHomepageSections.Widgets? {
        return widgets.firstOrNull { it.isSticky && it.type == RechargeHomepageConst.BAYAR_SEKALIGUS_TYPE }
    }

    private fun mapperTodoList(widgets: List<RechargeHomepageSections.Widgets>): MutableList<Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>> {
        val itemList = mutableListOf<Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>>()
        widgets.filter {
            it.type != RechargeHomepageConst.BAYAR_SEKALIGUS_TYPE
        }.forEach {
            val item = when {
                it.type.startsWith(RechargeHomepageConst.AUTOPAY_TYPE) || it.type.startsWith(
                    RechargeHomepageConst.POSTPAIDREMINDER_TYPE
                ) -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel(
                    it
                )
                else -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel(
                    it
                )
            }

            itemList.add(item)
        }
        return itemList
    }

}
