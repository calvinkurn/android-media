package com.tokopedia.withdraw.auto_withdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.domain.model.GetInfoAutoWD
import com.tokopedia.withdraw.auto_withdrawal.presentation.adapter.AutoWDInfoAdapter

class AutoWDInfoFragment : BottomSheetUnify() {

    private var infoAutoWD: GetInfoAutoWD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_AWD_INFO)) {
                infoAutoWD = it.getParcelable(ARG_AWD_INFO)
            } else
                dismiss()
        }
        val rootView = LayoutInflater.from(context)
                .inflate(R.layout.swd_layout_awd_info, null, false)
        rootView.apply {
            setChild(this)
            findViewById<View>(R.id.btnMoreInfo).setOnClickListener { openMoreInfo() }
            val recyclerView = findViewById<RecyclerView>(R.id.rvAutoWDInfo)
            initRecyclerAdapter(recyclerView)
        }
    }

    private fun initRecyclerAdapter(recyclerView: RecyclerView) {
        infoAutoWD?.let {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = AutoWDInfoAdapter(it.data)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun openMoreInfo() {

    }

    companion object {
        private const val ARG_AWD_INFO = "arg_awd_info"
        fun getInstance(getInfoAutoWD: GetInfoAutoWD)
                : AutoWDInfoFragment {
            return AutoWDInfoFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_AWD_INFO, getInfoAutoWD)
                arguments = bundle
            }
        }
    }


}