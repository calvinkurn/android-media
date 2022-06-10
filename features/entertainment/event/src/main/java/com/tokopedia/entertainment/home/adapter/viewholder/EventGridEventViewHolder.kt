package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.EventGridModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid.view.btn_see_all
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid.view.ent_recycle_view_grid
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid.view.ent_title_card
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid_adapter_item.view.image
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid_adapter_item.view.txt_location
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid_adapter_item.view.txt_price
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid_adapter_item.view.txt_start_title
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid_adapter_item.view.txt_title
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridEventViewHolder(itemView: View,
                               val gridlistener: TrackingListener,
                               val clickGridListener: ClickGridListener
)
    : AbstractViewHolder<EventGridModel>(itemView) {

    var itemAdapter = InnerItemAdapter(gridlistener, clickGridListener)

    init {
        itemView.ent_recycle_view_grid.apply {
            layoutManager = GridLayoutManager(itemView.context, 2, LinearLayoutManager.VERTICAL, false)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventGridModel) {
        itemView.ent_title_card.show()
        itemView.btn_see_all.show()
        itemView.ent_recycle_view_grid.show()
        itemView.ent_title_card.text = element.title
        itemAdapter.titleGrid = element.title
        itemAdapter.setList(element.items)
        itemView.btn_see_all.setOnClickListener {
            gridlistener.clickSeeAllCuratedEventProduct(element.title,
                    adapterPosition + 1)
            RouteManager.route(itemView.context, ApplinkConstInternalEntertainment.EVENT_CATEGORY
                    , element.id, "", "")
        }
        if(element.items.isEmpty()){
            itemView.ent_title_card.gone()
            itemView.btn_see_all.gone()
            itemView.ent_recycle_view_grid.gone()
        }
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_grid
        val TAG = EventGridEventViewHolder::class.java.simpleName
    }

    class InnerItemAdapter(val gridlistener: TrackingListener,
                           val clickGridListener: ClickGridListener
    )
        : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemModel>
        var titleGrid = ""
        var pos: Int = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_grid_adapter_item, parent,
                            false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            var item = items.get(position)
            this.pos = position
            Glide.with(holder.view).load(item.imageUrl).into(holder.view.image)
            holder.view.txt_location.text = item.location
            holder.view.txt_title.text = item.title
            holder.view.txt_price.apply {
                if (item.isFree){
                    text = resources.getString(R.string.ent_free_price)
                    holder.view.txt_start_title.gone()
                } else {
                    text = item.price
                    holder.view.txt_start_title.show()
                }
            }
            holder.view.setOnClickListener {
                gridlistener.clickSectionEventProduct(item, items, titleGrid,
                        position + 1)
                clickGridListener.redirectToPDPEvent(item.seoURL)
            }
            holder.view.addOnImpressionListener(item, {
                gridlistener.impressionSectionEventProduct(item, items, titleGrid,
                        position + 1)
            })

            holder.view.txt_title.apply {
                val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                if (item.location.isEmpty()) {
                    labelParams.topToBottom = holder.view.image.id
                } else {
                    labelParams.topToBottom = holder.view.txt_location.id
                }
                layoutParams = labelParams
            }

            holder.view.txt_start_title.apply {
                if (item.location.isNotEmpty()) {
                    setMargin(
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.unify_space_0)
                    )
                } else {
                    setMargin(
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.spacing_lvl6),
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.unify_space_0)
                    )
                }
            }

        }

        fun setList(list: MutableList<EventItemModel>) {
            items = list
            notifyDataSetChanged()
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface ClickGridListener{
        fun redirectToPDPEvent(applink: String)
    }

}