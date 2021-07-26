package com.tokopedia.flight.passenger.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.passenger.view.adapter.FlightAmenityAdapterTypeFactory
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightBookingAmenityViewHolder
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

/**
 * Created by furqan on 06/10/21.
 */
class FlightBookingAmenityFragment : BaseListFragment<FlightBookingAmenityModel, FlightAmenityAdapterTypeFactory>(),
        FlightBookingAmenityViewHolder.ListenerCheckedLuggage {

    private var flightBookingAmenityViewModels: ArrayList<FlightBookingAmenityModel>? = null
    private var selectedAmenity: FlightBookingAmenityMetaModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            flightBookingAmenityViewModels = it.getParcelableArrayList(EXTRA_LIST_AMENITIES)
            selectedAmenity = it.getParcelable(EXTRA_SELECTED_AMENITIES)
        }

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {}
    override fun onItemClicked(flightBookingLuggageViewModel: FlightBookingAmenityModel) {
        val viewModels: MutableList<FlightBookingAmenityModel> = ArrayList()
        viewModels.add(flightBookingLuggageViewModel)
        selectedAmenity!!.amenities = viewModels
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_booking_luggage, container, false)
        val button = view.findViewById<View>(R.id.button_save) as UnifyButton
        button.setOnClickListener {
            activity?.let {
                val intent = Intent()
                intent.putExtra(EXTRA_SELECTED_AMENITIES, selectedAmenity)
                it.setResult(Activity.RESULT_OK, intent)
                it.finish()
            }
        }
        return view
    }

    override fun loadData(page: Int) {
        flightBookingAmenityViewModels?.let {
            renderList(it)
        }
    }

    override fun getAdapterTypeFactory(): FlightAmenityAdapterTypeFactory {
        return FlightAmenityAdapterTypeFactory(this)
    }

    override fun isItemChecked(selectedItem: FlightBookingAmenityModel?): Boolean {
        return selectedAmenity!!.amenities.contains(selectedItem)
    }

    override fun resetItemCheck() {
        selectedAmenity!!.amenities = ArrayList()
        adapter.notifyDataSetChanged()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    companion object {

        const val EXTRA_SELECTED_AMENITIES = "EXTRA_SELECTED_AMENITIES"
        const val EXTRA_LIST_AMENITIES = "EXTRA_LIST_AMENITIES"

        fun createInstance(flightBookingAmenityMetaViewModels: ArrayList<FlightBookingAmenityMetaModel?>?,
                           selectedAmenity: FlightBookingAmenityMetaModel?): FlightBookingAmenityFragment {
            val flightBookingAmenityFragment = FlightBookingAmenityFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_LIST_AMENITIES, flightBookingAmenityMetaViewModels)
            bundle.putParcelable(EXTRA_SELECTED_AMENITIES, selectedAmenity)
            flightBookingAmenityFragment.arguments = bundle
            return flightBookingAmenityFragment
        }

    }
}