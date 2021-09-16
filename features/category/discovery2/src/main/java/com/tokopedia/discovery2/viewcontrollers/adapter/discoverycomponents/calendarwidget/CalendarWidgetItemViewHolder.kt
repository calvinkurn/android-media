package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class CalendarWidgetItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private lateinit var calendarWidgetItemViewModel: CalendarWidgetItemViewModel
    private var calendarParent: ConstraintLayout = itemView.findViewById(R.id.calendar_parent)
    private var calendarDateAlpha: View = itemView.findViewById(R.id.calendar_date_alpha)
    private var calendarDate: Typography = itemView.findViewById(R.id.calendar_date)
    private var calendarTitleImage: ImageUnify = itemView.findViewById(R.id.calendar_title_image)
    private var calendarTitle: Typography = itemView.findViewById(R.id.calendar_title)
    private var calendarDesc: Typography = itemView.findViewById(R.id.calendar_desc)
    private var calendarImage: ImageUnify = itemView.findViewById(R.id.calendar_image)
    private var calendarButton: UnifyButton = itemView.findViewById(R.id.calendar_button)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        calendarWidgetItemViewModel = discoveryBaseViewModel as CalendarWidgetItemViewModel
        getSubComponent().inject(calendarWidgetItemViewModel)

        calendarWidgetItemViewModel.components.apply {

        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            calendarWidgetItemViewModel.getSyncPageLiveData().observe(it, { needResync->
                if (needResync) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            calendarWidgetItemViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }
}