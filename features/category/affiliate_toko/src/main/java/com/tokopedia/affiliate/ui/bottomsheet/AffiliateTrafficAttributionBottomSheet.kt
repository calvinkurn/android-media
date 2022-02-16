package com.tokopedia.affiliate.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetAdapterFactory
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetAdapter
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetDiffcallback
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetTypeFactory
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.*

class AffiliateTrafficAttributionBottomSheet: BottomSheetUnify() {
    private var contentView: View? = null
    private var type: String =""
    private var titleSheet: String = ""
    private var subText: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {
        private const val TYPE = "type"
        private const val TITLE = "title"
        private const val SUB_TEXT = "subText"
        fun newInstance(type: String,title: String,subText: String): AffiliateTrafficAttributionBottomSheet {
            return AffiliateTrafficAttributionBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(TYPE,type)
                    putString(TITLE,title)
                    putString(SUB_TEXT,subText)
                }
            }
        }
    }
    private var dataRv: RecyclerView? = null
    private fun init() {
        initBundle()
        showCloseIcon = true
        showKnob = false
        contentView = View.inflate(context,
            R.layout.affiliate_traffic_attribution_bottom_sheet_attribute, null)
        setTitle(titleSheet)
        dataRv = contentView?.findViewById(R.id.traffic_rv)
        setData()
        setChild(contentView)
    }

    private fun initBundle() {
        arguments?.let { bundle ->
            type = bundle.getString(TYPE,"")
            titleSheet = bundle.getString(titleSheet,"")
            subText = bundle.getString(SUB_TEXT,"")
        }
    }

    private val adapter by lazy {
        val asyncDifferConfig = AsyncDifferConfig.Builder(AffiliateBottomSheetDiffcallback())
            .build()
        AffiliateBottomSheetAdapter(asyncDifferConfig, AffiliateBottomSheetAdapterFactory())
    }

    private fun setData() {
       dataRv?.layoutManager = LinearLayoutManager(context)
       dataRv?.adapter = adapter
       adapter.submitList(itemList as List<Visitable<*>>?)
    }
    private val itemList: ArrayList<Visitable<AffiliateBottomSheetTypeFactory>> = ArrayList()

}