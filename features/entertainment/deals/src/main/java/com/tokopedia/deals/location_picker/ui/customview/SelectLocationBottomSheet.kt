package com.tokopedia.deals.location_picker.ui.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.CurrentLocationCallback
import com.tokopedia.deals.databinding.LayoutDealsChangeLocationBinding
import com.tokopedia.deals.location_picker.DealsLocationConstants
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.ui.fragment.DealsSelectLocationFragment
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared

class SelectLocationBottomSheet : BottomSheetUnify() {

    private var binding by autoCleared<LayoutDealsChangeLocationBinding>()

    private var callback: CurrentLocationCallback? = null

    private var selectedLocation: String? = ""
    private var currentLocation: Location? = null
    private var isLandmarkPage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            if (fragment is DealsSelectLocationFragment) {
                callback?.let { callback->
                    fragment.setCallback(callback)
                }
            }
        }
        super.onCreate(savedInstanceState)
        selectedLocation = arguments?.getString(DealsLocationConstants.SELECTED_LOCATION)
        currentLocation = arguments?.getParcelable(DealsLocationConstants.LOCATION_OBJECT)
        isLandmarkPage = arguments?.getBoolean(DealsLocationConstants.IS_LANDMARK_PAGE_EXTRA, false) ?: false
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
        callback?.let { callback ->
            val fragment = DealsSelectLocationFragment.createInstance(selectedLocation, currentLocation, isLandmarkPage)
            fragment.setCallback(callback)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.layout_location, fragment).commit()
        }
    }

    private fun setLayoutMargin() {
        context?.let { context ->
            val padding = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt()
            bottomSheetWrapper.setPadding(Int.ZERO, padding, Int.ZERO, Int.ZERO)
            bottomSheetHeader.setPadding(padding, Int.ZERO, padding, Int.ZERO)
        }
    }

    fun setCallback(callback: CurrentLocationCallback) {
        this.callback = callback
    }

    companion object {
        fun createInstance(selectedLocation: String?,
                           location: Location?,
                           isLandmarkPage: Boolean): SelectLocationBottomSheet {
            val bottomSheet = SelectLocationBottomSheet()
            val bundle = Bundle()
            bundle.putString(DealsLocationConstants.SELECTED_LOCATION, selectedLocation)
            bundle.putParcelable(DealsLocationConstants.LOCATION_OBJECT, location)
            bundle.putBoolean(DealsLocationConstants.IS_LANDMARK_PAGE_EXTRA, isLandmarkPage)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
