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
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetCloseProcessListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetListener
import com.tokopedia.digital.home.presentation.listener.TodoWidgetItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst
import com.tokopedia.digital.home.presentation.util.RechargeHomepageConst.SPACE_DP
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.digital.home.R as digitalhomeR

class RechargeHomepageTodoWidgetAdapter(
    var items: List<RechargeHomepageSections.Item>,
    val todoWidgetListener: RechargeHomepageTodoWidgetListener,
    val todoWidgetSectionId: String,
    val todoWidgetSectionName: String,
    val todoWidgetSectionTemplate: String
) : RecyclerView.Adapter<RechargeHomepageTodoWidgetAdapter.RechargeHomeTodoWidgetListViewHolder>() {

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

    inner class RechargeHomeTodoWidgetListViewHolder(val binding: ViewRechargeHomeTodoWidgetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val factory = RechargeHomepageTodoWidgetAdapterTypeFactory(
            todoWidgetListener,
            object : RechargeHomepageTodoWidgetCloseProcessListener {
                override fun onCloseWidget(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
                    removeItem(element)
                    todoWidgetListener.onCloseItem(
                        (element as RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel).widget,
                    )
                }
            },
            object : TodoWidgetItemListener {
                override fun getListSize(): Int {
                    return getListCount()
                }
            },
            todoWidgetSectionId, todoWidgetSectionName, todoWidgetSectionTemplate
        )

        private val baseAdapter = BaseAdapter(
            factory
        )

        fun removeItem(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
            try {
                val mutableListTodoWidget = baseAdapter.list.toMutableList()
                mutableListTodoWidget.remove(element)
                baseAdapter.setVisitables(mutableListTodoWidget.toList())
                if (baseAdapter.itemCount < Int.ONE) {
                    hideTitle()
                }
            } catch (e: UnsupportedOperationException) {
                Timber.d(e)
                showToasterRemoveItemError()
            }
        }

        fun getListCount(): Int {
            return baseAdapter.list.size
        }

        private fun hideTitle() {
            with(binding) {
                titleTodoWidget.hide()
            }
        }

        private fun showToasterRemoveItemError() {
            binding.root.let {
                Toaster.build(
                    it,
                    it.context.getString(digitalhomeR.string.recharge_home_remove_item_failed),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }

        fun bind(item: RechargeHomepageSections.Item) {
            renderTitle(item)
            renderSubTitle(item)
            renderStickyLayout(item)
            renderRecycleView(item)
        }

        private fun renderTitle(item: RechargeHomepageSections.Item) {
            with(binding) {
                if (item.title.isEmpty()) {
                    titleTodoWidget.hide()
                } else {
                    titleTodoWidget.show()
                    titleTodoWidget.text = item.title
                }
            }
        }

        private fun renderSubTitle(item: RechargeHomepageSections.Item) {
            with(binding) {
                if (item.subtitle.isEmpty()) {
                    subTitleTodoWidget.hide()
                } else {
                    subTitleTodoWidget.show()
                    subTitleTodoWidget.text = item.subtitle
                }
            }
        }

        private fun renderStickyLayout(item: RechargeHomepageSections.Item) {
            with(binding) {
                val stickyLayout = getStickyLayout(item.widgets)
                if (stickyLayout != null) {
                    rvTodoWidget.apply {
                        setMargin(
                            getDimens(digitalhomeR.dimen.product_card_width_bayar_sekaligus_todo_widget),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )
                        setPadding(
                            getDimens(unifyprinciplesR.dimen.unify_space_8),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_24),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )
                    }
                    viewStickyLayout.root.show()
                    viewStickyLayout.tgTitleTodoWidget.text = stickyLayout.title
                    viewStickyLayout.tgSubTitleTodoWidget.text = stickyLayout.subtitle
                    viewStickyLayout.imgTodoWidgetBackground.loadImage(TokopediaImageUrl.RECHARGE_SUBHOME_TODO_WIDGET)
                    viewStickyLayout.root.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(
                            stickyLayout, false, todoWidgetSectionId,
                            todoWidgetSectionName, todoWidgetSectionTemplate
                        )
                    }
                    setStickyHeightMatchParent(binding)
                } else {
                    viewStickyLayout.root.hide()
                    rvTodoWidget.apply {
                        setMargin(
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )

                        setPadding(
                            getDimens(unifyprinciplesR.dimen.unify_space_16),
                            getDimens(unifyprinciplesR.dimen.unify_space_0),
                            getDimens(unifyprinciplesR.dimen.unify_space_24),
                            getDimens(unifyprinciplesR.dimen.unify_space_0)
                        )
                    }
                }
            }
        }

        private fun renderRecycleView(item: RechargeHomepageSections.Item) {
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
                        SPACE_DP.dpToPx(displayMetrics)
                    )
                )
            }
        }
    }

    private fun setStickyHeightMatchParent(binding: ViewRechargeHomeTodoWidgetBinding) {
        with(binding) {
            val layoutParams = viewStickyLayout.root.layoutParams
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            viewStickyLayout.root.layoutParams = layoutParams
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
            itemList.add(
                RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel(
                    it
                )
            )
        }
        return itemList
    }

}
