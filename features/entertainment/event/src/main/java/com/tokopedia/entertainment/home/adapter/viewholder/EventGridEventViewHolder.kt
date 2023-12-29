package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntLayoutViewholderEventGridAdapterItemBinding
import com.tokopedia.entertainment.databinding.EntLayoutViewholderEventGridBinding
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.EventGridModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridEventViewHolder(
    itemView: View,
    val gridlistener: TrackingListener,
    val clickGridListener: ClickGridListener
) : AbstractViewHolder<EventGridModel>(itemView) {

    var itemAdapter = InnerItemAdapter(gridlistener, clickGridListener)
    private val binding: EntLayoutViewholderEventGridBinding? by viewBinding()

    init {
        binding?.entRecycleViewGrid?.run {
            layoutManager =
                GridLayoutManager(itemView.context, SPAN, LinearLayoutManager.VERTICAL, false)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventGridModel) {
        binding?.run {
            entTitleCard.show()
            btnSeeAll.show()
            entRecycleViewGrid.show()
            entTitleCard.text = element.title
            itemAdapter.titleGrid = element.title
            itemAdapter.setList(element.items)
            btnSeeAll.setOnClickListener {
                gridlistener.clickSeeAllCuratedEventProduct(
                    element.title,
                    adapterPosition + Int.ONE
                )
                RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalEntertainment.EVENT_CATEGORY,
                    element.id,
                    "",
                    ""
                )
            }
            if (element.items.isEmpty()) {
                entTitleCard.gone()
                btnSeeAll.gone()
                entRecycleViewGrid.gone()
            }
        }
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_grid
        private const val SPAN = 2
    }

    class InnerItemAdapter(
        private val gridListener: TrackingListener,
        private val clickGridListener: ClickGridListener
    ) : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemModel>
        var titleGrid = ""

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val binding = EntLayoutViewholderEventGridAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return InnerViewHolder(binding, gridListener, clickGridListener, items, titleGrid)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            holder.bind(items[position])
        }

        fun setList(list: MutableList<EventItemModel>) {
            items = list
            notifyDataSetChanged()
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(
        private val binding: EntLayoutViewholderEventGridAdapterItemBinding,
        private val gridListener: TrackingListener,
        private val clickGridListener: ClickGridListener,
        private val items: List<EventItemModel>,
        private val titleGrid: String
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventItemModel) {
            with(binding) {
                image.loadImage(item.imageUrl)
                txtLocation.text = item.location
                txtTitle.text = item.title
                txtPrice.run {
                    if (item.isFree) {
                        text = resources.getString(R.string.ent_free_price)
                        binding.txtStartTitle.gone()
                    } else {
                        text = item.price
                        binding.txtStartTitle.show()
                    }
                }
                root.setOnClickListener {
                    gridListener.clickSectionEventProduct(
                        item, items, titleGrid,
                        position + Int.ONE
                    )
                    clickGridListener.redirectToPDPEvent(item.seoURL)
                }
                root.addOnImpressionListener(item) {
                    gridListener.impressionSectionEventProduct(
                        item, items, titleGrid,
                        position + Int.ONE
                    )
                }

                txtTitle.run {
                    val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                    if (item.location.isEmpty()) {
                        labelParams.topToBottom = binding.image.id
                    } else {
                        labelParams.topToBottom = binding.txtLocation.id
                    }
                    layoutParams = labelParams
                }

                txtStartTitle.run {
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
        }
    }

    interface ClickGridListener {
        fun redirectToPDPEvent(applink: String)
    }

}
