package com.tokopedia.catalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactory
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogForYouModel
import com.tokopedia.catalog.viewholder.components.CatalogForYouViewHolder
import com.tokopedia.catalog.viewholder.containers.CatalogProductsContainerViewHolder
import com.tokopedia.catalog.viewholder.products.CatalogForYouContainerViewHolder

class CatalogDetailAdapter (val context : FragmentActivity, val catalogDetailListener: CatalogDetailListener, val catalogId: String ,asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel>,
                            private val catalogAdapterTypeFactory: CatalogDetailAdapterFactory)
    :ListAdapter<BaseCatalogDataModel, AbstractViewHolder<*>>(asyncDifferConfig){

    private val catalogForYouImpressionSet = HashSet<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        if(viewType == CatalogProductsContainerViewHolder.LAYOUT){
            view.findViewById<ConstraintLayout>(R.id.root_container)?.let { rootLayout ->
                val layoutParams = rootLayout.layoutParams
                layoutParams.height = catalogDetailListener.getWindowHeight() - context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                rootLayout.layoutParams = layoutParams
            }
        }
        return catalogAdapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseCatalogDataModel>, getItem(position))
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    private fun bind(holder: AbstractViewHolder<BaseCatalogDataModel>, item: BaseCatalogDataModel) {
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else currentList[position]?.type(catalogAdapterTypeFactory) ?: HideViewHolder.LAYOUT
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<*>) {
        catalogDetailListener.setLastAttachItemPosition(holder.adapterPosition)
        if(holder is CatalogProductsContainerViewHolder){
            catalogDetailListener.hideFloatingLayout()
        }
        if (holder is CatalogForYouViewHolder) {
            val position = holder.adapterPosition
            if (!catalogForYouImpressionSet.add(position)) {
                val item = currentList[position] as? CatalogForYouModel
                item?.let { catalogForYouModel ->
                    catalogDetailListener.onCatalogForYouImpressed(catalogForYouModel,holder.adapterPosition)
                }
            }
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<*>) {
        catalogDetailListener.setLastDetachedItemPosition(holder.adapterPosition)
        if(holder is CatalogProductsContainerViewHolder){
            catalogDetailListener.showFloatingLayout()
        }else if(holder is CatalogForYouContainerViewHolder){
            holder.removeObservers()
        }
        super.onViewDetachedFromWindow(holder)
    }
}