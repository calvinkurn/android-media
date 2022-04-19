package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel
import java.util.*

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailAdapter constructor(typeFactory: InboxReputationDetailTypeFactory) :
    RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private val list: MutableList<Visitable<*>>
    private val emptyModel: EmptyModel
    private val loadingModel: LoadingModel
    private val typeFactory: InboxReputationDetailTypeFactory

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
        return (list[position] as? Visitable<InboxReputationDetailTypeFactory>)?.type(typeFactory) ?: 0
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addList(list: List<Visitable<*>>) {
        this.list.addAll(list)
    }

    fun showEmpty() {
        list.add(emptyModel)
    }

    fun removeEmpty() {
        list.remove(emptyModel)
    }

    fun showLoading() {
        list.add(loadingModel)
        notifyDataSetChanged()
    }

    fun removeLoading() {
        list.remove(loadingModel)
        notifyDataSetChanged()
    }

    val isLoading: Boolean
        get() {
            return list.contains(loadingModel)
        }

    fun addHeader(model: InboxReputationDetailHeaderUiModel) {
        list.add(model)
    }

    fun clearList() {
        list.clear()
    }

    val header: InboxReputationDetailHeaderUiModel?
        get() {
            return if (list.firstOrNull() is InboxReputationDetailHeaderUiModel) list.firstOrNull() as InboxReputationDetailHeaderUiModel? else null
        }

    fun getList(): List<Visitable<*>> {
        return list
    }

    init {
        list = ArrayList()
        this.typeFactory = typeFactory
        emptyModel = EmptyModel()
        loadingModel = LoadingModel()
    }
}