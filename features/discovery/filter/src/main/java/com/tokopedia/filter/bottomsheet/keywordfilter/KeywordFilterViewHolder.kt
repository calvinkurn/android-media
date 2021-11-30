package com.tokopedia.filter.bottomsheet.keywordfilter

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor
import com.tokopedia.filter.R
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.ExistsAsNegative
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.ForbiddenCharacter
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.IsOriginalKeyword
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView.KeywordFilterError.MaxFiveNegative
import com.tokopedia.filter.common.helper.ChipSpacingItemDecoration
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.iconunify.IconUnify.Companion.ADD_CIRCLE
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography

internal class KeywordFilterViewHolder(
    itemView: View,
    private val listener: KeywordFilterListener,
): AbstractViewHolder<KeywordFilterDataView>(itemView), KeywordFilterItemListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.sort_filter_bottom_sheet_keyword_filter_view_holder
    }

    private val layoutManager = ChipsLayoutManager
        .newBuilder(itemView.context)
        .setOrientation(ChipsLayoutManager.HORIZONTAL)
        .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
        .build()

    private val spacingItemDecoration = ChipSpacingItemDecoration(
        itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
        itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    )

    private val keywordFilterTitle by lazy {
        itemView.findViewById<Typography?>(R.id.keywordFilterTitle)
    }

    private val keywordFilterSubTitle by lazy {
        itemView.findViewById<Typography?>(R.id.keywordFilterSubTitle)
    }

    private val keywordFilterNewNotification by lazy {
        itemView.findViewById<NotificationUnify?>(R.id.keywordFilterNewNotification)
    }

    private val keywordFilterTextField by lazy {
        itemView.findViewById<TextFieldUnify2?>(R.id.keywordFilterTextField)
    }

    private val keywordFilterRecyclerView by lazy {
        itemView.findViewById<RecyclerView?>(R.id.keywordFilterRecyclerView)
    }

    private val keywordFilterItemAdapter = KeywordFilterItemAdapter(this)
    private var keywordFilterDataView: KeywordFilterDataView? = null

    init {
        configureRecyclerView()
        configureTextField()
    }

    private fun configureRecyclerView() {
        keywordFilterRecyclerView?.let {
            it.layoutManager = layoutManager
            it.isNestedScrollingEnabled = false
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    private fun configureTextField() {
        keywordFilterTextField?.addOnFocusChangeListener = { _, hasFocus ->
            onKeywordTextFieldFocusChanged(hasFocus)
        }

        keywordFilterTextField?.editText?.setOnEditorActionListener {
            _: TextView?, actionId: Int, event: KeyEvent? ->
            if (isEnterPressed(actionId, event)) {
                onAddKeywordClicked()
                true
            } else false
        }

        keywordFilterTextField?.icon1?.apply {
            visible()
            setAddIconDrawable()
            setOnClickListener {
                onAddKeywordClicked()
            }
        }
    }

    private fun onKeywordTextFieldFocusChanged(hasFocus: Boolean) {
        if (hasFocus)
            listener.scrollToPosition(adapterPosition)
    }

    private fun isEnterPressed(actionId: Int, event: KeyEvent?): Boolean {
        val action = event?.action ?: return false

        return actionId == EditorInfo.IME_NULL && action == KeyEvent.ACTION_DOWN
    }

    private fun onAddKeywordClicked() {
        val keyword = keywordFilterTextField?.getEditableValue()?.toString() ?: ""

        keywordFilterDataView?.addKeyword(
            keyword = keyword,
            onSuccess = ::onSuccessAddKeyword,
            onError = ::onErrorAddKeyword,
        )
    }

    private fun onSuccessAddKeyword() {
        val keywordFilterDataView = keywordFilterDataView ?: return

        keywordFilterTextField?.run {
            isInputError = false
            setMessage(getString(R.string.keyword_filter_use_add_icon_to_add))

            editText?.text?.clear()
        }

        keywordFilterItemAdapter.setItemList(keywordFilterDataView.itemList)

        listener.onChangeKeywordFilter(keywordFilterDataView)
    }

    private fun onErrorAddKeyword(error: KeywordFilterError) {
        val errorMessage = getErrorMessage(error)

        keywordFilterTextField?.run {
            isInputError = true
            setMessage(errorMessage)
        }
    }

    private fun getErrorMessage(error: KeywordFilterError) =
        when (error) {
            is MaxFiveNegative -> getString(R.string.keyword_filter_max_count)
            is ExistsAsNegative -> getString(R.string.keyword_filter_enter_different_keyword)
            is IsOriginalKeyword -> getString(R.string.keyword_filter_cannot_same_as_keyword)
            is ForbiddenCharacter -> getString(R.string.keyword_filter_contains_forbidden_char)
        }

    private fun ImageView.setAddIconDrawable() {
        val context = context ?: return

        val iconPlusDrawable = getIconUnifyDrawable(
            context, ADD_CIRCLE, getColor(context, com.tokopedia.abstraction.R.color.black)
        )

        setImageDrawable(iconPlusDrawable)
    }

    override fun bind(element: KeywordFilterDataView) {
        keywordFilterDataView = element

        bindTitleSection(element)
        bindKeywordChips(element)
    }

    private fun bindTitleSection(element: KeywordFilterDataView) {
        keywordFilterTitle?.text = element.filter.title
        keywordFilterSubTitle?.text = element.filter.filterAttributeDetail
        keywordFilterNewNotification?.showWithCondition(element.filter.isNew)
    }

    private fun bindKeywordChips(element: KeywordFilterDataView) {
        keywordFilterRecyclerView?.swapAdapter(
            keywordFilterItemAdapter,
            false,
        )

        keywordFilterItemAdapter.setItemList(element.itemList)
    }

    private class KeywordFilterItemAdapter(
        private val listener: KeywordFilterItemListener,
    ): ListAdapter<KeywordFilterItemDataView, KeywordFilterItemViewHolder>(
        KeywordFilterDiffUtilItemCallback(),
    ) {

        fun setItemList(list: List<KeywordFilterItemDataView>) {
            submitList(ArrayList(list))
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): KeywordFilterItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                KeywordFilterItemViewHolder.LAYOUT,
                parent,
                false,
            )

            return KeywordFilterItemViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: KeywordFilterItemViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    private class KeywordFilterDiffUtilItemCallback
        : DiffUtil.ItemCallback<KeywordFilterItemDataView>() {

        override fun areItemsTheSame(
            oldItem: KeywordFilterItemDataView,
            newItem: KeywordFilterItemDataView
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: KeywordFilterItemDataView,
            newItem: KeywordFilterItemDataView,
        ) = true
    }

    private class KeywordFilterItemViewHolder(
        itemView: View,
        private val keywordFilterItemListener: KeywordFilterItemListener,
    ): RecyclerView.ViewHolder(itemView) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.sort_filter_bottom_sheet_chips_layout
        }

        private val sortFilterChipsUnify by lazy {
            itemView.findViewById<ChipsUnify?>(R.id.sortFilterChipsUnify)
        }

        fun bind(keywordFilterItemDataView: KeywordFilterItemDataView) {
            sortFilterChipsUnify?.apply {
                chipText = keywordFilterItemDataView.negativeKeyword
                chipSize = ChipsUnify.SIZE_MEDIUM
                chipType = ChipsUnify.TYPE_NORMAL
                setOnRemoveListener {
                    keywordFilterItemListener.onRemoveItemClicked(keywordFilterItemDataView)
                }
            }
        }
    }

    override fun onRemoveItemClicked(keywordFilterItemDataView: KeywordFilterItemDataView) {
        val keywordFilterDataView = keywordFilterDataView ?: return

        keywordFilterDataView.removeKeyword(keywordFilterItemDataView.negativeKeyword)

        keywordFilterItemAdapter.setItemList(keywordFilterDataView.itemList)

        listener.onChangeKeywordFilter(keywordFilterDataView)
    }
}