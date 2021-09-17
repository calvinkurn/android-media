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
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationDetailActivity.Companion.EXTRA_SAVED_CANCELLATION_DETAIL
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationDetailActivity.Companion.EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID
import com.tokopedia.flight.cancellationdetail.presentation.adapter.*
import com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder.FlightOrderCancellationDetailJourneyViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailPassengerModel
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.databinding.FragmentFlightCancellationDetailBinding
import com.tokopedia.flight.detail.view.adapter.FlightSimpleAdapter
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.orderdetail.data.OrderDetailCancellation
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*

/**
 * @author by furqan on 06/01/2021
 */
class FlightOrderCancellationDetailFragment : BaseDaggerFragment(), FlightOrderCancellationDetailJourneyViewHolder.Listener {

    private lateinit var savedInstanceCacheManagerId: String
    private lateinit var cancellationDetail: FlightOrderCancellationListModel

    private var isPassengerInfoShowed = true

    private var binding by autoClearedNullable<FragmentFlightCancellationDetailBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFlightCancellationDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(::cancellationDetail.isInitialized){
            renderView()
        }
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
        binding?.cancellationStatus?.requestFocus()
        binding?.cancellationStatus?.text = cancellationDetail.cancellationDetail.statusStr
        binding?.cancellationDate?.text = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                DateUtil.DEFAULT_VIEW_FORMAT,
                cancellationDetail.cancellationDetail.createTime)

        // journey detail
        val journeyTypeFactory: FlightOrderCancellationDetailJourneyTypeFactory = FlightOrderCancellationDetailJourneyAdapterJourneyTypeFactory(this, JOURNEY_TITLE_FONT_SIZE)
        val journeyAdapter = BaseListAdapter<FlightOrderDetailJourneyModel, FlightOrderCancellationDetailJourneyTypeFactory>(journeyTypeFactory)
        binding?.recyclerViewFlight?.adapter = journeyAdapter

        journeyAdapter.addElement(cancellationDetail.cancellationDetail.journeys)
        journeyAdapter.notifyDataSetChanged()

        // passenger detail
        val passengerTypeFactory = FlightOrderCancellationDetailPassengerAdapterTypeFactory()
        val passengerAdapter = BaseListAdapter<FlightOrderCancellationDetailPassengerModel, FlightOrderCancellationDetailPassengerTypeFactory>(passengerTypeFactory)
        binding?.recyclerViewDataPassenger?.adapter = passengerAdapter

        passengerAdapter.addElement(cancellationDetail.cancellationDetail.passengers)
        passengerAdapter.notifyDataSetChanged()

        binding?.layoutExpendablePassenger?.setOnClickListener {
            binding?.imageExpendablePassenger?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flight_rotate_reverse))
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
                binding?.rvBottomTopInfo?.layoutManager = LinearLayoutManager(context)
                binding?.rvBottomTopInfo?.adapter = refundTopAdapter
            } else {
                binding?.rvBottomTopInfo?.visibility = View.GONE
                binding?.bottomFirstSeparator?.visibility = View.GONE
            }

            // middle info
            if (cancellationDetail.cancellationDetail.refundDetail.middleInfo.isNotEmpty()) {
                val refundMiddleAdapter = FlightCancellationRefundDetailMiddleAdapter(cancellationDetail.cancellationDetail.refundDetail.middleInfo)
                binding?.rvBottomMiddleInfo?.layoutManager = LinearLayoutManager(context)
                binding?.rvBottomMiddleInfo?.adapter = refundMiddleAdapter
            } else {
                binding?.rvBottomMiddleInfo?.visibility = View.GONE
                binding?.bottomSecondSeparator?.visibility = View.GONE
            }

            // bottom info
            if (cancellationDetail.cancellationDetail.refundDetail.bottomInfo.isNotEmpty()) {
                val refundBottomAdapter = FlightCancellationRefundBottomAdapter(FlightCancellationRefundBottomAdapter.TYPE_RED)
                refundBottomAdapter.addData(generateSimpleViewModel(cancellationDetail.cancellationDetail.refundDetail.bottomInfo))
                binding?.rvBottomBottomInfo?.layoutManager = LinearLayoutManager(context)
                binding?.rvBottomBottomInfo?.adapter = refundBottomAdapter
            } else {
                binding?.rvBottomBottomInfo?.visibility = View.GONE
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
                binding?.rvBottomNotes?.layoutManager = LinearLayoutManager(context)
                binding?.rvBottomNotes?.adapter = refundNotesAdapter
            } else {
                binding?.rvBottomNotes?.visibility = View.GONE
            }

        } else {
            binding?.containerBottomInfo?.visibility = View.GONE
        }
    }

    private fun generateSimpleViewModel(items: List<OrderDetailCancellation.OrderDetailRefundKeyValue>): MutableList<SimpleModel> {
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
        binding?.recyclerViewDataPassenger?.visibility = View.GONE
        binding?.imageExpendablePassenger?.rotation = IMAGE_ROTATION
    }

    private fun showPassengerInfo() {
        isPassengerInfoShowed = true
        binding?.recyclerViewDataPassenger?.visibility = View.VISIBLE
        binding?.imageExpendablePassenger?.rotation = 0f
    }

    companion object {

        private const val JOURNEY_TITLE_FONT_SIZE = 16F
        private const val NOTES_MAX_LINES = 5
        private const val IMAGE_ROTATION = 180f

        fun createInstance(savedInstanceCacheManagerId: String): FlightOrderCancellationDetailFragment =
                FlightOrderCancellationDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SAVED_INSTANCE_CACHE_MANAGER_ID, savedInstanceCacheManagerId)
                    }
                }
    }
}