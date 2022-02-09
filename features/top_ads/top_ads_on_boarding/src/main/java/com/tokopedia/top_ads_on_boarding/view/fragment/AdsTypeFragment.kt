package com.tokopedia.top_ads_on_boarding.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_KEY
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.EMPTY_TEXT
import com.tokopedia.top_ads_on_boarding.data.AdsTypeData
import com.tokopedia.top_ads_on_boarding.di.TopAdsOnBoardingComponent
import com.tokopedia.top_ads_on_boarding.view.adapter.AdsTypeCardAdapter
import javax.inject.Inject

class AdsTypeFragment : BaseDaggerFragment() {
    private var adsTypeListRv: RecyclerView? = null
    private var adsTypeCardAdapter: AdsTypeCardAdapter? = null
    private var adsType: String? = ""

    @Inject
    lateinit var adsTypeData: AdsTypeData

    override fun getScreenName(): String {
        return EMPTY_TEXT
    }

    companion object {
        fun newInstance(bundle: Bundle): AdsTypeFragment {
            val fragment = AdsTypeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjector() {
        getComponent(TopAdsOnBoardingComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.topads_ads_type_fragment_layout, container, false)
        adsType = arguments?.getString(ADS_TYPE_KEY)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adsTypeListRv = view.findViewById(R.id.adsTypeList)
        initAdapter()

    }

    private fun initAdapter() {
        adsTypeCardAdapter = AdsTypeCardAdapter()
        adsTypeListRv?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adsTypeListRv?.adapter = adsTypeCardAdapter
        adsType?.let {
            adsTypeCardAdapter?.submitList(adsTypeData.getAdsTypeList(it))
        }
    }
}
