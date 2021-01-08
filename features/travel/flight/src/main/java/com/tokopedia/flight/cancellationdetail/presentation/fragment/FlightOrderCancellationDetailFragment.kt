package com.tokopedia.flight.cancellationdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationDetailActivity.Companion.EXTRA_SAVED_CANCELLATION_DETAIL
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationDetailActivity.Companion.EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailJourneyAdapterJourneyTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailJourneyTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder.FlightOrderCancellationDetailJourneyViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import kotlinx.android.synthetic.main.fragment_flight_cancellation_detail.*

/**
 * @author by furqan on 06/01/2021
 */
class FlightOrderCancellationDetailFragment : BaseDaggerFragment(), FlightOrderCancellationDetailJourneyViewHolder.Listener {

    private lateinit var savedInstanceCacheManagerId: String
    private lateinit var cancellationDetail: FlightOrderCancellationListModel

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_cancellation_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID)) {
                savedInstanceCacheManagerId = it.getString(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID)
                        ?: ""
            }
        }

        savedInstanceState?.let {
            if (it.containsKey(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID)) {
                savedInstanceCacheManagerId = it.getString(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID)
                        ?: ""
            }
        }

        if (savedInstanceCacheManagerId.isEmpty()) {
            activity?.finish()
        } else {
            SaveInstanceCacheManager(requireContext(), savedInstanceCacheManagerId).let { cache ->
                val data: FlightOrderCancellationListModel? = cache.get(EXTRA_SAVED_CANCELLATION_DETAIL, FlightOrderCancellationListModel::class.java)
                data?.let { nonNullData ->
                    cancellationDetail = nonNullData
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, savedInstanceCacheManagerId).also {
                it.put(EXTRA_SAVED_CANCELLATION_DETAIL, cancellationDetail)
            }
            outState.putString(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID, manager.id)
        }
    }

    override fun onCloseExpand(position: Int) {
        // TODO("Not yet implemented")
    }

    private fun renderView() {
        cancellation_status.requestFocus()
        cancellation_status.text = cancellationDetail.cancellationDetail.statusStr
        cancellation_date.text = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                cancellationDetail.cancellationDetail.createTime)

        val journeyTypeFactory: FlightOrderCancellationDetailJourneyTypeFactory = FlightOrderCancellationDetailJourneyAdapterJourneyTypeFactory(this, JOURNEY_TITLE_FONT_SIZE)
        val journeyAdapter = BaseListAdapter<FlightOrderDetailJourneyModel, FlightOrderCancellationDetailJourneyTypeFactory>(journeyTypeFactory)
        recycler_view_flight.adapter = journeyAdapter

        journeyAdapter.addElement(cancellationDetail.cancellationDetail.journeys)
        journeyAdapter.notifyDataSetChanged()

    }

    companion object {

        private const val JOURNEY_TITLE_FONT_SIZE = 16F

        fun createInstance(savedInstanceCacheManagerId: String): FlightOrderCancellationDetailFragment =
                FlightOrderCancellationDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID, savedInstanceCacheManagerId)
                    }
                }
    }
}