package com.tokopedia.notifcenter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.state.Status
import com.tokopedia.notifcenter.presentation.adapter.NotificationFilterAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationFilterTypeFactoryImpl

class NotificationFilterView : LinearLayout {

    private var rvFilter: RecyclerView? = null
    private var settingBtn: ImageView? = null
    private var rvFilterAdapter: NotificationFilterAdapter? = null

    interface FilterListener {
        fun onFilterChanged(filterType: Int)
    }

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context?) {
        initViewInflation(context)
    }

    fun updateFilterState(dataState: Resource<NotifcenterFilterResponse>) {
        when (dataState.status) {
            Status.LOADING -> rvFilterAdapter?.showLoading(dataState.data)
            Status.SUCCESS -> rvFilterAdapter?.successLoading(dataState.data, dataState.needUpdate)
        }
    }

    fun setFilterListener(filterListener: FilterListener) {
        rvFilterAdapter?.filterListener = filterListener
    }

    fun reset() {
        rvFilterAdapter?.reset()
    }

    private fun initViewInflation(context: Context?) {
        View.inflate(context, R.layout.item_notifcenter_filter, this)?.apply {
            bindView(this)
            bindClickNavigation()
            initRecyclerView()
        }
    }

    private fun bindClickNavigation() {
        settingBtn?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING)
        }
    }

    private fun initRecyclerView() {
        val typeFactory = NotificationFilterTypeFactoryImpl()
        rvFilterAdapter = NotificationFilterAdapter(typeFactory)
        rvFilter?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = rvFilterAdapter
        }
    }

    private fun bindView(view: View) {
        settingBtn = view.findViewById(R.id.iv_setting_notif)
        rvFilter = view.findViewById(R.id.rv_filter)
    }

}