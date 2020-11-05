package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.widget.CardProductNotificationCardUnify
import com.tokopedia.notifcenter.widget.CarouselProductRecyclerView

class CarouselProductNotificationViewHolder constructor(
        itemView: View?,
        private val notificationItemListener: NotificationItemListener?,
        private val carouselListener: Listener?,
        private val adapterListener: NotificationAdapterListener?
) : BaseNotificationViewHolder(itemView, notificationItemListener) {

    interface Listener {
        fun saveProductCarouselState(position: Int, state: Parcelable?)
        fun getSavedCarouselState(position: Int): Parcelable?
    }

    private val rv: CarouselProductRecyclerView? = itemView?.findViewById(R.id.rv_carousel_product)
    private val rvAdapter = CarouselProductAdapter(notificationItemListener)

    init {
        rv?.apply {
            setHasFixedSize(true)
            setRecycledViewPool(adapterListener?.getProductCarouselViewPool())
            adapter = rvAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        saveProductCarouselState(adapterPosition, carouselListener)
                    }
                }
            })
        }
    }

    override fun bind(element: NotificationUiModel) {
        super.bind(element)
        bindCarouselProduct(element)
        bindScrollState(element)
        bindClickItem(element)
    }

    private fun bindCarouselProduct(element: NotificationUiModel) {
        rvAdapter.notification = element
    }

    private fun bindScrollState(element: NotificationUiModel) {
        rv?.restoreSavedCarouselState(adapterPosition, carouselListener)
    }

    private fun bindClickItem(element: NotificationUiModel) {
        container?.setOnClickListener {
            notificationItemListener?.showProductBottomSheet(element)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_carousel_product_notification
    }

    /**
     * Recyclerview adapter used for this particular ViewHolder only
     */
    class CarouselProductAdapter constructor(
            private val listener: NotificationItemListener?
    ) : RecyclerView.Adapter<NestedSingleProductNotificationViewHolder>() {

        var notification: NotificationUiModel? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItemCount(): Int = notification?.productData?.size ?: 0

        override fun onCreateViewHolder(
                parent: ViewGroup, viewType: Int
        ): NestedSingleProductNotificationViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(
                            NestedSingleProductNotificationViewHolder.LAYOUT,
                            parent,
                            false
                    )
            return NestedSingleProductNotificationViewHolder(view, listener)
        }

        override fun onBindViewHolder(
                holder: NestedSingleProductNotificationViewHolder, position: Int
        ) {
            notification?.productData?.getOrNull(position)?.let {
                holder.bind(it)
            }
        }
    }

    /**
     * Recyclerview ViewHolder used for this particular
     * ViewHolder [CarouselProductNotificationViewHolder] only
     */
    class NestedSingleProductNotificationViewHolder constructor(
            itemView: View,
            private val listener: NotificationItemListener?
    ) : RecyclerView.ViewHolder(itemView) {

        private val productContainer: CardProductNotificationCardUnify? = itemView.findViewById(
                R.id.pc_single
        )

        fun bind(product: ProductData) {
            bindProductData(product)
        }

        private fun bindProductData(product: ProductData) {
            productContainer?.bindProductData(product)
        }

        companion object {
            val LAYOUT = R.layout.item_notifcenter_nested_single_product_notification
        }
    }
}