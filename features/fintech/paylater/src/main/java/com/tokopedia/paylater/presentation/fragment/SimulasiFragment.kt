package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.widget.PayLaterSignupBottomSheet
import kotlinx.android.synthetic.main.fragment_simulasi.*

class SimulasiFragment  : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simulasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        btnDaftarPayLater.setOnClickListener {
            PayLaterSignupBottomSheet.show(Bundle(), childFragmentManager)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                SimulasiFragment()
    }
}