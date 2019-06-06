package com.tokopedia.instantloan.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.GqlLendingPartnerData
import com.tokopedia.instantloan.view.adapter.LendingPartnerAdapter
import kotlinx.android.synthetic.main.fragment_content_fragment.*

class LePartnerFragment : TkpdBaseV4Fragment() {

    private var mContext: Context? = null
    private lateinit var partnerDataItemList: ArrayList<GqlLendingPartnerData>

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var lendingPartnerAdpater: LendingPartnerAdapter

    override fun getScreenName(): String {

        return ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        partnerDataItemList = arguments?.getParcelableArrayList<GqlLendingPartnerData>(PARTNER_LIST)!!
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content_fragment, null)
    }

    override fun onAttachActivity(context: Context) {
        this.mContext = context
        super.onAttachActivity(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        layoutManager = GridLayoutManager(mContext, COLUMN_COUNT_FOR_LOAN_PARTNER)
        lendingPartnerAdpater = LendingPartnerAdapter(partnerDataItemList)
        rv_lending_partner.layoutManager = layoutManager
        rv_lending_partner.adapter = lendingPartnerAdpater
    }

    companion object {

        val COLUMN_COUNT_FOR_LOAN_PARTNER = 3
        private val PARTNER_LIST = "partner_list"

        fun createInstance(partnerItemList: ArrayList<GqlLendingPartnerData>): LePartnerFragment {

            val bundle = Bundle()
            bundle.putParcelableArrayList(PARTNER_LIST, partnerItemList)
            val lendingPartnerFragment = LePartnerFragment()
            lendingPartnerFragment.arguments = bundle
            return lendingPartnerFragment
        }
    }
}