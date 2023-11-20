package com.tokopedia.carouselproductcard.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingProductCardView
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingSelectedGroupModel

class CarouselPagingProductCardActivityTest: AppCompatActivity(), Listener {

    private val adapter = Adapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carousel_product_card_activity_test_layout)

        adapter.data = data

        val recyclerView = findViewById<RecyclerView>(R.id.carouselProductCardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = adapter
    }

    override fun onGroupChanged(position: Int, selectedGroupModel: CarouselPagingSelectedGroupModel) {
        val currentData = adapter.data.toMutableList()
        val carouselPagingModel = currentData[position]
        val productCardGroupList = carouselPagingModel.productCardGroupList.toMutableList()

        val groupProductModel = productCardGroupList.find { it.group == selectedGroupModel.group }

        if (groupProductModel == null || groupProductModel.productItemList.isNotEmpty()) return

        val carouselGroupProductModelIndex = productCardGroupList.indexOf(groupProductModel)
        val newProductCardList = productCardList(position, 7)

        productCardGroupList.removeAt(carouselGroupProductModelIndex)
        productCardGroupList.add(
            carouselGroupProductModelIndex,
            CarouselPagingGroupProductModel(
                group = selectedGroupModel.group,
                productItemList = newProductCardList
            )
        )

        currentData.removeAt(position)
        currentData.add(
            position,
            carouselPagingModel.copy(
                productCardGroupList = productCardGroupList,
                currentPageInGroup = when (selectedGroupModel.direction) {
                    CarouselPagingGroupChangeDirection.PREVIOUS -> CarouselPagingModel.LAST_PAGE_IN_GROUP
                    CarouselPagingGroupChangeDirection.NEXT -> CarouselPagingModel.FIRST_PAGE_IN_GROUP
                    else -> carouselPagingModel.currentPageInGroup
                },
            )
        )
//        adapter.data = currentData
//        adapter.notifyItemChanged(position)
    }

    internal class Adapter(
        private val listener: Listener,
    ) : RecyclerView.Adapter<ViewHolder>() {

        var data: List<CarouselPagingModel> = listOf()

        private val recycledViewPool = RecyclerView.RecycledViewPool()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(ViewHolder.LAYOUT, parent, false)
            return ViewHolder(view, recycledViewPool, listener)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun onViewRecycled(holder: ViewHolder) {
            holder.recycle()
            super.onViewRecycled(holder)
        }
    }

    internal class ViewHolder(
        itemView: View,
        private val recycledViewPool: RecyclerView.RecycledViewPool,
        private val listener: Listener,
    ): RecyclerView.ViewHolder(itemView) {

        companion object {
            val LAYOUT = R.layout.carousel_paging_product_card_item_test_layout
        }

        private val item: CarouselPagingProductCardView? by lazy {
            itemView as? CarouselPagingProductCardView
        }

        fun bind(carouselPagingModel: CarouselPagingModel) {
            item?.setPagingModel(
                model = carouselPagingModel,
                recycledViewPool = recycledViewPool,
                listener = object : CarouselPagingProductCardView.CarouselPagingListener {
                    override fun onGroupChanged(selectedGroupModel: CarouselPagingSelectedGroupModel) {
                        val groupIndex =
                            carouselPagingModel
                                .productCardGroupList
                                .indexOfFirst { it.group == selectedGroupModel.group }

                        Toast.makeText(
                            itemView.context,
                            "Group: ${selectedGroupModel.group.title}, " +
                                "Direction: ${selectedGroupModel.direction}, " +
                                "Group Index: $groupIndex",
                            Toast.LENGTH_LONG
                        ).show()

                        listener.onGroupChanged(bindingAdapterPosition, selectedGroupModel)
                    }

                    override fun onItemImpress(
                        groupModel: CarouselPagingGroupModel,
                        itemPosition: Int
                    ) {

                    }

                    override fun onItemClick(
                        groupModel: CarouselPagingGroupModel,
                        itemPosition: Int
                    ) {

                    }
                }
            )
        }

        fun recycle() {
            item?.recycle()
        }
    }
}

internal interface Listener {
    fun onGroupChanged(position: Int, selectedGroupModel: CarouselPagingSelectedGroupModel)
}
