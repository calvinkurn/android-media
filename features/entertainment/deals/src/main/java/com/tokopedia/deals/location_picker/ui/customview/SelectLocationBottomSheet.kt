package com.tokopedia.deals.location_picker.ui.customview

import android.os.Bundle
import android.view.View
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.CurrentLocationCallback
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.ui.fragment.DealsSelectLocationFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.layout_deals_search_bar.*

class SelectLocationBottomSheet (private val currentLocation: Location?, private val isLandmarkPage: Boolean, private val callback: CurrentLocationCallback)
    : BottomSheetUnify() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initBottomSheet() {
        isFullpage = true
        setTitle(getString(R.string.deals_location_bottomsheet_title))
        setChild(View.inflate(requireContext(), R.layout.layout_deals_change_location, null))
    }
    private fun initView() {
        setLayoutMargin()
        val fragment = DealsSelectLocationFragment.createInstance(tv_location?.text.toString(), currentLocation, isLandmarkPage, callback)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_location, fragment).commit()
    }

    private fun setLayoutMargin() {
        val padding = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt()
        bottomSheetWrapper.setPadding(0, padding, 0, 0)
        bottomSheetHeader.setPadding(padding, 0, padding, 0)
    }
}