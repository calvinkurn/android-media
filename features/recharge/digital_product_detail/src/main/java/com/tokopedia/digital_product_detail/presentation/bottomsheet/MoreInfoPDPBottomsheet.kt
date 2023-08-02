package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.digital_product_detail.databinding.BottomSheetMoreInfoBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalMoreInfoAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MoreInfoPDPBottomsheet : BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
        clearContentPadding = true
    }

    private var listInfo: ArrayList<String> = arrayListOf()
    private var title: String = ""

    private var binding by autoClearedNullable<BottomSheetMoreInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            listInfo = arguments.getStringArrayList(LIST_INFO_EXTRA) ?: arrayListOf()
            title = arguments.getString(TITLE_EXTRA, "") ?: ""
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(){
        binding = BottomSheetMoreInfoBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            rvMoreInfo.run {
                val adapterMoreInfo = DigitalMoreInfoAdapter()
                val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                layoutManager = linearLayoutManager
                adapter = adapterMoreInfo
                adapterMoreInfo.setListInfo(listInfo.toMutableList())
            }
        }
        setTitle(title)
        setChild(binding?.root)
    }

    companion object {
        private const val LIST_INFO_EXTRA = "LIST_INFO_EXTRA"
        private const val TITLE_EXTRA = "TITLE_EXTRA"

        fun newInstance(listInfo: ArrayList<String>, title: String): MoreInfoPDPBottomsheet {
            val bottomSheet = MoreInfoPDPBottomsheet()
            val bundle = Bundle()
            bundle.putStringArrayList(LIST_INFO_EXTRA, listInfo)
            bundle.putString(TITLE_EXTRA, title)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
