package com.tokopedia.deals.location_picker.ui.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.CurrentLocationCallback
import com.tokopedia.deals.databinding.LayoutDealsChangeLocationBinding
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.ui.fragment.DealsSelectLocationFragment
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared

class SelectLocationBottomSheet (private val selectedLocation: String = "", private val currentLocation: Location?, private val isLandmarkPage: Boolean, private val callback: CurrentLocationCallback)
    : BottomSheetUnify() {

    private var binding by autoCleared<LayoutDealsChangeLocationBinding>()

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
        context?.let { context ->
            setTitle(context.resources.getString(R.string.deals_location_bottomsheet_title))
        }
        binding = LayoutDealsChangeLocationBinding.inflate(LayoutInflater.from(requireContext()))

        setChild(binding.root)
    }
    private fun initView() {
        setLayoutMargin()
        val fragment = DealsSelectLocationFragment.createInstance(selectedLocation, currentLocation, isLandmarkPage, callback)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_location, fragment).commit()
    }

    private fun setLayoutMargin() {
        context?.let { context ->
            val padding = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt()
            bottomSheetWrapper.setPadding(Int.ZERO, padding, Int.ZERO, Int.ZERO)
            bottomSheetHeader.setPadding(padding, Int.ZERO, padding, Int.ZERO)
        }
    }
}
