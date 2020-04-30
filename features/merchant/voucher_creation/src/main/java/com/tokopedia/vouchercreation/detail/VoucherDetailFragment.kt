package com.tokopedia.vouchercreation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.vouchercreation.R

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailFragment : Fragment() {

    companion object {
        fun newInstance(): VoucherDetailFragment = VoucherDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {

    }
}