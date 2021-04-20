package com.tokopedia.flight.cancellationdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationRefundBottomAdapter
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationRefundDetailMiddleAdapter
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationDetailActivity.Companion.EXTRA_SAVED_CANCELLATION_DETAIL
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationDetailActivity.Companion.EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailJourneyAdapterJourneyTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailJourneyTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailPassengerAdapterTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailPassengerTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder.FlightOrderCancellationDetailJourneyViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailPassengerModel
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.detail.view.adapter.FlightSimpleAdapter
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.orderdetail.data.OrderDetailCancellation
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import kotlinx.android.synthetic.main.fragment_flight_cancellation_detail.*
import java.util.*

/**
 * @author by furqan on 06/01/2021
 */
class FlightOrderCancellationDetailFragment : BaseDaggerFragment(), FlightOrderCancellationDetailJourneyViewHolder.Listener {

    private lateinit var savedInstanceCacheManagerId: String
    private lateinit var cancellationDetail: FlightOrderCancellationListModel

    private var isPassengerInfoShowed = true

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

        // journey detail
        val journeyTypeFactory: FlightOrderCancellationDetailJourneyTypeFactory = FlightOrderCancellationDetailJourneyAdapterJourneyTypeFactory(this, JOURNEY_TITLE_FONT_SIZE)
        val journeyAdapter = BaseListAdapter<FlightOrderDetailJourneyModel, FlightOrderCancellationDetailJourneyTypeFactory>(journeyTypeFactory)
        recycler_view_flight.adapter = journeyAdapter

        journeyAdapter.addElement(cancellationDetail.cancellationDetail.journeys)
        journeyAdapter.notifyDataSetChanged()

        // passenger detail
        val passengerTypeFactory = FlightOrderCancellationDetailPassengerAdapterTypeFactory()
        val passengerAdapter = BaseListAdapter<FlightOrderCancellationDetailPassengerModel, FlightOrderCancellationDetailPassengerTypeFactory>(passengerTypeFactory)
        recycler_view_data_passenger.adapter = passengerAdapter

        passengerAdapter.addElement(cancellationDetail.cancellationDetail.passengers)
        passengerAdapter.notifyDataSetChanged()

        layout_expendable_passenger.setOnClickListener {
            image_expendable_passenger.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flight_rotate_reverse))
            togglePassengerInfo()
        }

        renderBottomInfo()
    }

    private fun renderBottomInfo() {
        if (cancellationDetail.cancellationDetail.refundDetail.topInfo.isNotEmpty() ||
                cancellationDetail.cancellationDetail.refundDetail.middleInfo.isNotEmpty() ||
                cancellationDetail.cancellationDetail.refundDetail.bottomInfo.isNotEmpty() ||
                cancellationDetail.cancellationDetail.refundDetail.notes.isNotEmpty()) {

            // top info
            if (cancellationDetail.cancellationDetail.refundDetail.topInfo.isNotEmpty()) {
                val refundTopAdapter = FlightCancellationRefundBottomAdapter(FlightCancellationRefundBottomAdapter.TYPE_NORMAL)
                refundTopAdapter.addData(generateSimpleViewModel(cancellationDetail.cancellationDetail.refundDetail.topInfo))
                rv_bottom_top_info.layoutManager = LinearLayoutManager(context)
                rv_bottom_top_info.adapter = refundTopAdapter
            } else {
                rv_bottom_top_info.visibility = View.GONE
                bottom_first_separator.visibility = View.GONE
            }

            // middle info
            if (cancellationDetail.cancellationDetail.refundDetail.middleInfo.isNotEmpty()) {
                val refundMiddleAdapter = FlightCancellationRefundDetailMiddleAdapter(cancellationDetail.cancellationDetail.refundDetail.middleInfo)
                rv_bottom_middle_info.layoutManager = LinearLayoutManager(context)
                rv_bottom_middle_info.adapter = refundMiddleAdapter
            } else {
                rv_bottom_middle_info.visibility = View.GONE
                bottom_second_separator.visibility = View.GONE
            }

            // bottom info
            if (cancellationDetail.cancellationDetail.refundDetail.bottomInfo.isNotEmpty()) {
                val refundBottomAdapter = FlightCancellationRefundBottomAdapter(FlightCancellationRefundBottomAdapter.TYPE_RED)
                refundBottomAdapter.addData(generateSimpleViewModel(cancellationDetail.cancellationDetail.refundDetail.bottomInfo))
                rv_bottom_bottom_info.layoutManager = LinearLayoutManager(context)
                rv_bottom_bottom_info.adapter = refundBottomAdapter
            } else {
                rv_bottom_bottom_info.visibility = View.GONE
            }

            // notes
            if (cancellationDetail.cancellationDetail.refundDetail.notes.isNotEmpty()) {
                val refundNotesAdapter = FlightSimpleAdapter()
                refundNotesAdapter.setArrowVisible(false)
                refundNotesAdapter.setClickable(false)
                refundNotesAdapter.setTitleBold(false)
                refundNotesAdapter.setTitleOnly(true)
                refundNotesAdapter.setTitleMaxLines(NOTES_MAX_LINES)
                refundNotesAdapter.setViewModels(generateSimpleViewModel(cancellationDetail.cancellationDetail.refundDetail.notes))
                rv_bottom_notes.layoutManager = LinearLayoutManager(context)
                rv_bottom_notes.adapter = refundNotesAdapter
            } else {
                rv_bottom_notes.visibility = View.GONE
            }

        } else {
            container_bottom_info.visibility = View.GONE
        }
    }

    private fun generateSimpleViewModel(items: List<OrderDetailCancellation.OrderDetailRefundKeyValue>): List<SimpleModel> {
        val datas: MutableList<SimpleModel> = ArrayList()
        for (item in items) {
            datas.add(SimpleModel(item.key, item.value))
        }
        return datas
    }

    private fun togglePassengerInfo() {
        if (isPassengerInfoShowed) {
            hidePassengerInfo()
        } else {
            showPassengerInfo()
        }
    }

    private fun hidePassengerInfo() {
        isPassengerInfoShowed = false
        recycler_view_data_passenger.visibility = View.GONE
        image_expendable_passenger.rotation = 180f
    }

    private fun showPassengerInfo() {
        isPassengerInfoShowed = true
        recycler_view_data_passenger.visibility = View.VISIBLE
        image_expendable_passenger.rotation = 0f
    }

    companion object {

        private const val JOURNEY_TITLE_FONT_SIZE = 16F
        private const val NOTES_MAX_LINES = 5

        fun createInstance(savedInstanceCacheManagerId: String): FlightOrderCancellationDetailFragment =
                FlightOrderCancellationDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID, savedInstanceCacheManagerId)
                    }
                }
    }
}