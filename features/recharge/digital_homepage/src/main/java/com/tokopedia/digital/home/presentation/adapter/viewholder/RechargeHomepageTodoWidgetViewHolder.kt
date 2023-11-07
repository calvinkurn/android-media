package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.ViewGroup
import com.tokopedia.digital.home.R
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeListTodoWidgetBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetAutopayBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetPostReminderBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageTodoWidgetAdapterTypeFactory
import com.tokopedia.digital.home.presentation.adapter.decoration.RechargeTodoWidgetSpaceDecorator
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class RechargeHomepageTodoWidgetViewHolder(
    val binding: ViewRechargeHomeListTodoWidgetBinding,
    val listener: RechargeHomepageItemListener,
    val todoWidgetListener: RechargeHomepageTodoWidgetListener,
) : AbstractViewHolder<RechargeHomepageTodoWidgetModel>(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_list_todo_widget
        private const val SIZE_MAX = 2
        const val SPACE_DP = 8
        private const val BAYAR_SEKALIGUS_TYPE = "BAYAR_SEKALIGUS"
        private const val POSTPAIDREMINDER_TYPE = "POSTPAID"
        private const val AUTOPAY_TYPE = "AUTOPAY"
    }

    private val factory = RechargeHomepageTodoWidgetAdapterTypeFactory(todoWidgetListener, object : RechargeHomepageTodoWidgetCloseProcess {
        override fun onCloseWidget(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
            removeItem(element)
        }
    })
    private val baseAdapter = BaseAdapter(
        factory
    )

    private val factory2 = RechargeHomepageTodoWidgetAdapterTypeFactory(todoWidgetListener, object : RechargeHomepageTodoWidgetCloseProcess {
        override fun onCloseWidget(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
            removeItem2(element)
        }
    })
    private val baseAdapter2 = BaseAdapter(
        factory2
    )

    override fun bind(element: RechargeHomepageTodoWidgetModel) {
        if (element.section.items.isNotEmpty()) {
            with(binding) {
                if (element.section.items.first().title.isEmpty()) {
                    titleTodoWidgetFirst.hide()
                } else {
                    titleTodoWidgetFirst.show()
                    titleTodoWidgetFirst.text = element.section.items.first().title
                }
                val stickyLayoutFirst = getStickyLayout(element.section.items.first().widgets)
                if (stickyLayoutFirst != null) {
                    rvTodoWidgetFirst.apply {
                        setMargin(
                            getDimens(com.tokopedia.digital.home.R.dimen.product_card_width_bayar_sekaligus_todo_widget),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        )
                    }
                    viewStickyLayoutFirst.root.show()
                    viewStickyLayoutFirst.tgTitleTodoWidget.text = stickyLayoutFirst.title
                    viewStickyLayoutFirst.tgSubTitleTodoWidget.text = stickyLayoutFirst.subtitle
                    viewStickyLayoutFirst.imgTodoWidgetBackground.loadImage(TokopediaImageUrl.RECHARGE_SUBHOME_TODO_WIDGET)
                    viewStickyLayoutFirst.root.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(stickyLayoutFirst.appLink)
                    }
                } else {
                    viewStickyLayoutFirst.root.hide()
                    rvTodoWidgetFirst.apply {
                        setMargin(
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                        )
                    }
                }

                with(binding.rvTodoWidgetFirst) {
                    show()
                    baseAdapter.setVisitables(mapperTodoList(element.section.items.first().widgets).toList())
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


                if (element.section.items.size >= SIZE_MAX) {
                    if (element.section.items.second().title.isEmpty()) {
                        titleTodoWidgetSecond.hide()
                    } else {
                        titleTodoWidgetSecond.show()
                        titleTodoWidgetSecond.text = element.section.items.second().title
                    }
                    val stickyLayoutSecond = getStickyLayout(element.section.items.second().widgets)
                    if (stickyLayoutSecond != null) {
                        rvTodoWidgetSecond.apply {
                            setMargin(
                                getDimens(com.tokopedia.digital.home.R.dimen.product_card_width_bayar_sekaligus_todo_widget),
                                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            )
                        }
                        viewStickyLayoutSecond.root.show()
                        viewStickyLayoutSecond.tgTitleTodoWidget.text = stickyLayoutSecond.title
                        viewStickyLayoutSecond.tgSubTitleTodoWidget.text =
                            stickyLayoutSecond.subtitle
                        viewStickyLayoutSecond.imgTodoWidgetBackground.loadImage(TokopediaImageUrl.RECHARGE_SUBHOME_TODO_WIDGET)
                        viewStickyLayoutSecond.root.setOnClickListener {
                            todoWidgetListener.onClickTodoWidget(stickyLayoutSecond.appLink)
                        }
                    } else {
                        viewStickyLayoutSecond.root.hide()
                        rvTodoWidgetSecond.apply {
                            setMargin(
                                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                                getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                            )
                        }
                    }
                    with(binding.rvTodoWidgetSecond) {
                        show()
                        baseAdapter2.setVisitables(mapperTodoList(element.section.items.second().widgets).toList())
                        adapter = baseAdapter2
                        layoutManager = LinearLayoutManager(
                            itemView.context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        val displayMetrics = itemView.context.resources.displayMetrics
                        removeAllItemDecoration()
                        addItemDecoration(
                            RechargeTodoWidgetSpaceDecorator(
                                SPACE_DP.dpToPx(displayMetrics),
                                (stickyLayoutSecond != null)
                            )
                        )
                    }
                } else {
                    viewStickyLayoutSecond.root.hide()
                    rvTodoWidgetSecond.hide()
                    titleTodoWidgetSecond.hide()
                }
            }
        } else {
            with(binding) {
                viewStickyLayoutSecond.root.hide()
                rvTodoWidgetSecond.hide()
                viewStickyLayoutFirst.root.hide()
                rvTodoWidgetFirst.hide()
                titleTodoWidgetFirst.hide()
                titleTodoWidgetSecond.hide()
            }
            listener.loadRechargeSectionData(element.section.id, element.section.name)
        }
    }

    fun removeItem(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
        baseAdapter.removeElement(element)
    }

    fun removeItem2(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>) {
        baseAdapter2.removeElement(element)
    }

    class RechargeHomepageTodoWidgetPostReminderViewHolder(
        val binding: ViewRechargeHomeTodoWidgetPostReminderBinding,
        val todoWidgetListener: RechargeHomepageTodoWidgetListener,
        val closeItemListener: RechargeHomepageTodoWidgetCloseProcess
    ) : AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel>(
        binding.root
    ) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget_post_reminder
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel) {
            renderCommonTodoLayout(binding, element)
        }

        fun renderCommonTodoLayout(
            binding: ViewRechargeHomeTodoWidgetPostReminderBinding,
            element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel
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
                        labelParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        labelParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                        btnMainTodoWidget.layoutParams = labelParams
                    }

                    root.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(widget.appLink)
                    }

                    setCardHeightMatchParent()
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
    }

    class RechargeHomepageTodoWidgetAutoPayViewHolder(
        val binding: ViewRechargeHomeTodoWidgetAutopayBinding,
        val todoWidgetListener: RechargeHomepageTodoWidgetListener,
        val closeItemListener: RechargeHomepageTodoWidgetCloseProcess
    ) : AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel>(
        binding.root
    ) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget_autopay
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel) {
            renderCommonTodoLayout(binding, element)
        }

        fun renderCommonTodoLayout(
            binding: ViewRechargeHomeTodoWidgetAutopayBinding,
            element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel
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
                        labelParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        labelParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                        btnMainTodoWidget.layoutParams = labelParams
                    }

                    root.setOnClickListener {
                        todoWidgetListener.onClickTodoWidget(widget.appLink)
                    }

                    setCardHeightMatchParent()
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
    }

    private fun getStickyLayout(widgets: List<RechargeHomepageSections.Widgets>): RechargeHomepageSections.Widgets? {
        return widgets.firstOrNull { it.isSticky && it.type == BAYAR_SEKALIGUS_TYPE }
    }

    private fun <T> List<T>.second(): T {
        if (isEmpty())
            throw NoSuchElementException("List is empty.")
        return this[SIZE_MAX - Int.ONE]
    }

    private fun mapperTodoList(widgets: List<RechargeHomepageSections.Widgets>): MutableList<Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>> {
        val itemList = mutableListOf<Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>>()
        widgets.filter {
            it.type != BAYAR_SEKALIGUS_TYPE
        }.forEach {
            val item = when {
                it.type.startsWith(AUTOPAY_TYPE) -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel(
                    it
                )

                it.type.startsWith(POSTPAIDREMINDER_TYPE)-> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel(
                    it
                )

                else -> RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel(
                    it
                )
            }

            itemList.add(item)
        }
        return itemList
    }

    interface RechargeHomepageTodoWidgetListener {
        fun onClickTodoWidget(applink: String)
        fun onClickThreeButton(optionButtons: List<RechargeHomepageSections.OptionButton>)
    }

    interface RechargeHomepageTodoWidgetCloseProcess {
        fun onCloseWidget(element: Visitable<RechargeHomepageTodoWidgetAdapterTypeFactory>)
    }
}
