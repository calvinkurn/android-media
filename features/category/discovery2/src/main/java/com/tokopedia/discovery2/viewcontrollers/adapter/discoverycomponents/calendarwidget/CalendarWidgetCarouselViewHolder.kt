package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Constant.Calendar.CAROUSEL
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible

class CalendarWidgetCarouselViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var calendarWidgetCarouselViewModel: CalendarWidgetCarouselViewModel
    private var calendarCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.calendar_rv)
    private var linearLayoutManager: LinearLayoutManager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private val carouselRecyclerViewDecorator =
        CarouselProductCardItemDecorator(itemView.context?.resources?.getDimensionPixelSize(R.dimen.dp_8))
    init {
        calendarCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(false)
        calendarCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        calendarWidgetCarouselViewModel = discoveryBaseViewModel as CalendarWidgetCarouselViewModel
        getSubComponent().inject(calendarWidgetCarouselViewModel)
        addDecorator()
        if (mDiscoveryRecycleAdapter.itemCount == 0 || calendarWidgetCarouselViewModel.getCalendarList().isNullOrEmpty()) {
            addShimmer()
            mDiscoveryRecycleAdapter.notifyDataSetChanged()
        }
        handleCarouselPagination()
    }

    private fun addDecorator() {
        calendarWidgetCarouselViewModel.components.properties?.let {
            if (it.calendarType == Constant.Calendar.DYNAMIC || it.calendarLayout == CAROUSEL) {
                if (calendarCarouselRecyclerView.itemDecorationCount > 0)
                    calendarCarouselRecyclerView.removeItemDecorationAt(0)
                calendarCarouselRecyclerView.addItemDecoration(carouselRecyclerViewDecorator)
            } else {
                calendarCarouselRecyclerView.setMargin(
                    itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_8),
                    itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_8)
                )
            }
        }
    }

    private fun handleCarouselPagination() {
        calendarCarouselRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                val firstVisibleItemPosition: Int =
                    linearLayoutManager.findFirstVisibleItemPosition()
                if (!calendarWidgetCarouselViewModel.isLoadingData() && !calendarWidgetCarouselViewModel.isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0) {
                        calendarWidgetCarouselViewModel.fetchCarouselPaginatedCalendars()
                    }
                }
            }
        })
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerCalendarWidget.componentName, properties = calendarWidgetCarouselViewModel.components.properties))
        list.add(ComponentsItem(name = ComponentNames.ShimmerCalendarWidget.componentName, properties = calendarWidgetCarouselViewModel.components.properties))
        list.add(ComponentsItem(name = ComponentNames.ShimmerCalendarWidget.componentName, properties = calendarWidgetCarouselViewModel.components.properties))
        list.add(ComponentsItem(name = ComponentNames.ShimmerCalendarWidget.componentName, properties = calendarWidgetCarouselViewModel.components.properties))
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            calendarWidgetCarouselViewModel.getCalendarCarouselItemsListData().observe(it, { item ->
                mDiscoveryRecycleAdapter.setDataList(item)
            })
            calendarWidgetCarouselViewModel.syncData.observe(it, { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            })
            calendarWidgetCarouselViewModel.getCalendarLoadState().observe(it, { errorState ->
                if (errorState)
                    handleErrorState()
            })
        }
    }

    private fun handleErrorState() {
        addShimmer()
        mDiscoveryRecycleAdapter.notifyDataSetChanged()

    }

    private fun reloadComponent() {
        calendarCarouselRecyclerView.visible()
        calendarWidgetCarouselViewModel.resetComponent()
        calendarWidgetCarouselViewModel.fetchProductCarouselData()
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            calendarWidgetCarouselViewModel.getCalendarCarouselItemsListData().removeObservers(it)
            calendarWidgetCarouselViewModel.getCalendarLoadState().removeObservers(it)
        }
    }
}
