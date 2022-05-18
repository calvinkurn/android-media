package com.tokopedia.top_ads_on_boarding.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_KEY
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_PRODUCT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_SHOP
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.STEPPER_THREE
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TAG_STEPPER_FRAGMENT
import com.tokopedia.top_ads_on_boarding.data.AdsTypeEducationData
import com.tokopedia.top_ads_on_boarding.view.adapter.AdsTypeEducationAdapter
import com.tokopedia.top_ads_on_boarding.view.fragment.AdsTypeFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton


class AdsTypeEducationBottomSheet : BottomSheetUnify() {

    private var adsTypeEducationRv: RecyclerView? = null
    private var adsTypeEducationAdapter: AdsTypeEducationAdapter? = null
    private var adsType: String? = ""
    private var educationBottomSheetButton: UnifyButton? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        getAdsType()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun getAdsType() {
        adsType = arguments?.getString(ADS_TYPE_KEY)
    }

    private fun initChildLayout() {
        val contentView =
            View.inflate(context, R.layout.topads_ads_type_education_sheet_layout, null)
        setChild(contentView)
        showKnob = true
        showCloseIcon = false
        isFullpage = true
        isHideable = true
        showHeader = false
        clearContentPadding = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initAdapter()
        educationBottomSheetButton?.setOnClickListener {
            dismiss()
            if (adsType == ADS_TYPE_PRODUCT) {
                val bundle = Bundle()
                bundle.putString(ADS_TYPE_KEY, ADS_TYPE_PRODUCT)
                val fragment = AdsTypeFragment.newInstance(bundle)
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.onBoardingFragmentContainer,
                    fragment,
                    TAG_STEPPER_FRAGMENT
                )?.addToBackStack(STEPPER_THREE)?.commit()
            } else if (adsType == ADS_TYPE_SHOP) {
                val bundle = Bundle()
                bundle.putString(ADS_TYPE_KEY, ADS_TYPE_SHOP)
                val fragment = AdsTypeFragment.newInstance(bundle)
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.onBoardingFragmentContainer,
                    fragment,
                    TAG_STEPPER_FRAGMENT
                )?.addToBackStack(STEPPER_THREE)?.commit()
            }
        }
    }

    private fun initAdapter() {
        adsTypeEducationAdapter = AdsTypeEducationAdapter()
        adsTypeEducationRv?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adsTypeEducationRv?.adapter = adsTypeEducationAdapter
        adsType?.let {
            adsTypeEducationAdapter?.submitList(AdsTypeEducationData.getAdsTypeEducationList(it))
            setButton(it)
        }
    }

    private fun initView(view: View) {
        educationBottomSheetButton = view.findViewById(R.id.educationBottomSheetButton)
        adsTypeEducationRv = view.findViewById(R.id.pointList)
    }

    private fun setButton(adsType: String) {
        if (adsType == ADS_TYPE_PRODUCT)
            educationBottomSheetButton?.text =
                context?.getString(R.string.topads_product_ads_edu_button_text)
        else
            educationBottomSheetButton?.text =
                context?.getString(R.string.topads_shop_ads_edu_button_text)

    }

    companion object {
        fun newInstance(bundle: Bundle): AdsTypeEducationBottomSheet {
            val fragment = AdsTypeEducationBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }
}
