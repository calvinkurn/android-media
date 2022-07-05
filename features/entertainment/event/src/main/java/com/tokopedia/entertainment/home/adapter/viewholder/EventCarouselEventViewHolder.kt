package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.EventCarouselModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel.view.ent_btn_see_more
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel.view.ent_recycle_view_carousel
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.event_date
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.event_image
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.event_location
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.event_price
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.event_title
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.tg_event_home_start_from
import java.text.SimpleDateFormat
import java.util.Date
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens


/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselEventViewHolder(itemView: View,
                                   val carouselListener: TrackingListener,
                                   val clickCarouselListener: ClickCarouselListener
) : AbstractViewHolder<EventCarouselModel>(itemView) {

    var itemAdapter = InnerItemAdapter(carouselListener, clickCarouselListener)

    init {
        itemView.ent_recycle_view_carousel.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,
                    false)
            adapter = itemAdapter
        }
        itemView.ent_btn_see_more.setOnClickListener {
            carouselListener.clickSeeAllTopEventProduct()
        }
    }

    override fun bind(element: EventCarouselModel) {
        itemAdapter.items = element.items
        element.items.forEachIndexed { index, eventItemModel ->
            itemAdapter.productNames.add(index,
                    eventItemModel.title)
        }
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_carousel
        val TAG = EventCarouselEventViewHolder::class.java.simpleName

        const val EMPTY_DATE = "0"
        const val RESET_SPACE = 0
    }

    class InnerItemAdapter(val carouselListener: TrackingListener,
                           val clickCarouselListener: ClickCarouselListener
    )
        : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemModel>
        var productNames = mutableListOf<String>()
        var sdf = SimpleDateFormat("dd/MM/yy")
        var newsdf = SimpleDateFormat("dd\nMMM")
        var pos: Int = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_carousel_adapter_item, parent,
                            false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            var item = items.get(position)
            this.pos = position
            Glide.with(holder.view).load(item.imageUrl).into(holder.view.event_image)
            holder.view.event_location.text = item.location
            holder.view.event_title.text = item.title
            holder.view.event_date.apply {
                val dateFormated = formatedSchedule(item.date)
                if (dateFormated.isNullOrEmpty()){
                    gone()
                } else {
                    show()
                    text = dateFormated
                }
                typeface = Typography.getFontType(context, true, Typography.DISPLAY_2)
            }

            holder.view.event_price.apply {
                if (item.isFree){
                    text = resources.getString(R.string.ent_free_price)
                    holder.view.tg_event_home_start_from.gone()
                    setMargin(RESET_SPACE, RESET_SPACE, RESET_SPACE, RESET_SPACE)
                } else {
                    text = item.price
                    holder.view.tg_event_home_start_from.show()
                }
            }
            holder.view.setOnClickListener {
                carouselListener.clickTopEventProduct(item, productNames,
                        position + 1)
                clickCarouselListener.redirectCarouselToPDPEvent(item.seoURL)
            }
            holder.view.addOnImpressionListener(item, {
                carouselListener.impressionTopEventProduct(item, productNames,
                        position + 1)
            })

            holder.view.event_title.apply {
                val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                if (item.location.isEmpty()) {
                    labelParams.topToBottom = holder.view.event_image.id
                    setMargin(
                        getDimens(unifyDimens.spacing_lvl4),
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                } else {
                    labelParams.topToBottom = holder.view.event_location.id
                    setMargin(
                        getDimens(unifyDimens.spacing_lvl4),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                }
                layoutParams = labelParams
            }

            holder.view.tg_event_home_start_from.apply {
                if (item.location.isNotEmpty()) {
                    setMargin(
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.spacing_lvl2),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                } else {
                    setMargin(
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.spacing_lvl1),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                }
            }

            holder.view.event_price.apply {
                if (item.location.isNotEmpty()) {
                    setMargin(
                        getDimens(unifyDimens.spacing_lvl2),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                } else {
                    setMargin(
                        getDimens(unifyDimens.spacing_lvl2),
                        getDimens(unifyDimens.unify_space_12),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0)
                    )
                }
            }
        }

        private fun formatedSchedule(schedule: String): String? {
            return try {
                if(!schedule.equals(EMPTY_DATE)) {
                    val date = Date(schedule.toLong() * 1000)
                    newsdf.format(date).toUpperCase()
                } else ""
            } catch (e: Exception) {
                ""
            }
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface ClickCarouselListener{
        fun redirectCarouselToPDPEvent(applink: String)
    }
}