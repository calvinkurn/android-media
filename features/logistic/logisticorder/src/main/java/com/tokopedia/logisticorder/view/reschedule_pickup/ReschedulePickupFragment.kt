package com.tokopedia.logisticorder.view.reschedule_pickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.view.TrackingPageFragment

class ReschedulePickupFragment : BaseDaggerFragment() {
    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun initInjector() {
        TODO("Not yet implemented")
    }

    companion object {
        fun newInstance() : ReschedulePickupFragment {
            return ReschedulePickupFragment().apply {
                arguments = Bundle().apply {
                    // todo add data here
                }
            }
        }
    }
}