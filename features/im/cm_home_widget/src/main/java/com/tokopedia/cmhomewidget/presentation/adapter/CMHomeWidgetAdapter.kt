package com.tokopedia.cmhomewidget.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.presentation.adapter.factory.CMHomeWidgetViewHolderTypeFactoryImpl
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable
import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidgetAdapter @Inject constructor() :
    RecyclerView.Adapter<AbstractViewHolder<CMHomeWidgetVisitable>>() {

    @Inject
    lateinit var viewHolderTypeFactory: dagger.Lazy<CMHomeWidgetViewHolderTypeFactoryImpl>

    private val dataList = mutableListOf<CMHomeWidgetVisitable>()

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type(viewHolderTypeFactory.get())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<CMHomeWidgetVisitable> {
        return viewHolderTypeFactory.get().createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<CMHomeWidgetVisitable>,
        position: Int
    ) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    fun loadData(dataList: List<CMHomeWidgetVisitable>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }
}