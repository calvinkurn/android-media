package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel
import java.util.*

/**
 * @author by nisie on 8/11/17.
 */
class InboxReputationAdapter constructor(private val typeFactory: InboxReputationTypeFactory) :
    RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private val list: MutableList<Visitable<*>?>
    private val emptySearchModel: EmptySearchModel
    private val loadingModel: LoadingModel

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*> {
        val context: Context = parent.context
        val view: View = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        (holder as? AbstractViewHolder<Visitable<*>>)?.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return (list[position] as? Visitable<InboxReputationTypeFactory>)?.type(typeFactory) ?: 0
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<InboxReputationItemUiModel?>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setList(
        list: List<InboxReputationItemUiModel>,
        sellerMigrationReviewModel: SellerMigrationReviewModel?
    ) {
        this.list.clear()
        if (sellerMigrationReviewModel != null) {
            this.list.add(sellerMigrationReviewModel)
        }
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addList(list: List<InboxReputationItemUiModel>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun showEmpty(title: String, buttonText: String, onClickListener: View.OnClickListener) {
        emptySearchModel.title = title
        emptySearchModel.buttonText = buttonText
        emptySearchModel.buttonListener = onClickListener
        list.add(emptySearchModel)
    }

    fun removeEmpty() {
        list.remove(emptySearchModel)
    }

    fun showLoading() {
        list.add(loadingModel)
    }

    fun removeLoading() {
        list.remove(loadingModel)
    }

    val isLoading: Boolean
        get() {
            return list.contains(loadingModel)
        }

    fun showLoadingFull() {
        list.add(loadingModel)
    }

    fun removeLoadingFull() {
        list.remove(loadingModel)
    }

    fun clearList() {
        list.clear()
    }

    fun showEmpty(title: String) {
        emptySearchModel.title = title
        emptySearchModel.buttonText = ""
        emptySearchModel.buttonListener = null
        list.add(emptySearchModel)
    }

    fun getlist(): List<Visitable<*>?> {
        return list
    }

    val isEmpty: Boolean
        get() {
            return list.contains(emptySearchModel)
        }

    init {
        list = ArrayList()
        loadingModel = LoadingModel()
        emptySearchModel = EmptySearchModel()
    }
}