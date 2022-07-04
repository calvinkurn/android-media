package com.tokopedia.tokopedianow.educationalinfo.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.educationalinfo.presentation.bottomsheet.TokoNowEducationalInfoBottomSheet

class TokoNowEducationalInfoFragment: Fragment() {

    companion object {
        fun newInstance(): TokoNowEducationalInfoFragment {
            return TokoNowEducationalInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_education_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showEducationalInfoBottomSheet()
    }

    private fun showEducationalInfoBottomSheet() {
        val bottomSheet = TokoNowEducationalInfoBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            activity?.finish()
        }
        bottomSheet.show(childFragmentManager)

    }
}