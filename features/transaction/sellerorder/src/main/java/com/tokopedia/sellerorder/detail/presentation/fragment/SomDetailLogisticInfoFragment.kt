package com.tokopedia.sellerorder.detail.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailLogisticInfoAdapter
import kotlinx.android.synthetic.main.fragment_som_detail_logistic_info.*

class SomDetailLogisticInfoFragment : BaseDaggerFragment() {

    private var logisticInfoAllList: ArrayList<SomDetailOrder.Data.GetSomDetail.LogisticInfo.All>? = null

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailLogisticInfoFragment {
            return SomDetailLogisticInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(SomConsts.PARAM_LOGISTIC_INFO_ALL, bundle.getParcelableArrayList(SomConsts.PARAM_LOGISTIC_INFO_ALL))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            logisticInfoAllList = arguments?.getParcelableArrayList(SomConsts.PARAM_LOGISTIC_INFO_ALL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_detail_logistic_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        initToolbar()
        initView()
    }

    private fun initView() {
        rvLogisticInfo?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = logisticInfoAllList?.let { SomDetailLogisticInfoAdapter(it) }
        }
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(logistic_info_toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                logistic_info_toolbar?.title = getString(R.string.title_logistic_info)
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

}