package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntLayoutViewholderEventCarouselAdapterItemBinding
import com.tokopedia.entertainment.databinding.EntLayoutViewholderEventCarouselBinding
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.EventCarouselModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.text.SimpleDateFormat
import java.util.Date
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens


/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselEventViewHolder(itemView: View,
                                   private val carouselListener: TrackingListener,
                                   private val clickCarouselListener: ClickCarouselListener
) : AbstractViewHolder<EventCarouselModel>(itemView) {

    var itemAdapter = InnerItemAdapter(carouselListener, clickCarouselListener)
    private val binding: EntLayoutViewholderEventCarouselBinding? by viewBinding()

    init {
        binding?.entRecycleViewCarousel?.run {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,
                    false)
            adapter = itemAdapter
        }
        binding?.entBtnSeeMore?.setOnClickListener {
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

        private const val EMPTY_DATE = "0"
        private const val RESET_SPACE = 0
        private const val DATE_PATTERN = "dd\nMMM"
        private const val SECONDS = 1000

    }

    class InnerItemAdapter(val carouselListener: TrackingListener,
                           val clickCarouselListener: ClickCarouselListener
    )
        : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemModel>
        var productNames = mutableListOf<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val binding = EntLayoutViewholderEventCarouselAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return InnerViewHolder(binding, carouselListener, clickCarouselListener, productNames)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(val binding: EntLayoutViewholderEventCarouselAdapterItemBinding, private val carouselListener: TrackingListener,
                          private val clickCarouselListener: ClickCarouselListener, private val productNames: List<String>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventItemModel) {
            with(binding) {
                eventImage.loadImage(item.imageUrl)
                eventLocation.text = item.location
                eventTitle.text = item.title
                eventDate.run {
                    val dateFormated = formatedSchedule(item.date)
                    if (dateFormated.isNullOrEmpty()){
                        gone()
                    } else {
                        show()
                        text = dateFormated
                    }
                    typeface = Typography.getFontType(context, true, Typography.DISPLAY_2)
                }

                eventPrice.run {
                    if (item.isFree){
                        text = resources.getString(R.string.ent_free_price)
                        tgEventHomeStartFrom.gone()
                        setMargin(RESET_SPACE, RESET_SPACE, RESET_SPACE, RESET_SPACE)
                    } else {
                        text = item.price
                        tgEventHomeStartFrom.show()
                    }
                }
                root.setOnClickListener {
                    carouselListener.clickTopEventProduct(item, productNames,
                        position + 1)
                    clickCarouselListener.redirectCarouselToPDPEvent(item.seoURL)
                }
                root.addOnImpressionListener(item) {
                    carouselListener.impressionTopEventProduct(
                        item, productNames,
                        position + Int.ONE
                    )
                }

                eventTitle.run {
                    val labelParams = this.layoutParams as ConstraintLayout.LayoutParams
                    if (item.location.isEmpty()) {
                        labelParams.topToBottom = binding.eventImage.id
                        setMargin(
                            getDimens(unifyDimens.spacing_lvl4),
                            getDimens(unifyDimens.spacing_lvl3),
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.unify_space_0)
                        )
                    } else {
                        labelParams.topToBottom = binding.eventLocation.id
                        setMargin(
                            getDimens(unifyDimens.spacing_lvl4),
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.unify_space_0),
                            getDimens(unifyDimens.unify_space_0)
                        )
                    }
                    layoutParams = labelParams
                }

                tgEventHomeStartFrom.run {
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

                eventPrice.run {
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
        }

        private fun formatedSchedule(schedule: String): String? {
            return try {
                if(!schedule.equals(EMPTY_DATE)) {
                    val date = Date(schedule.toLong() * SECONDS)
                    SimpleDateFormat(DATE_PATTERN).format(date).toUpperCase()
                } else ""
            } catch (e: Exception) {
                ""
            }
        }
    }

    interface ClickCarouselListener{
        fun redirectCarouselToPDPEvent(applink: String)
    }
}
