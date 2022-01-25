package com.tokopedia.top_ads_on_boarding.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.TopAdsOnBoardingUtil.getSpannedText
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_KEY
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_PRODUCT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.ADS_TYPE_SHOP
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.EMPTY_TEXT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.STEPPER_THREE
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TAG_BOTTOM_SHEET_FRAGMENT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TAG_STEPPER_FRAGMENT
import com.tokopedia.top_ads_on_boarding.constant.TopAdsOnBoardingConstant.TopAdsOnBoardingLink.LEARN_TOKOPEDIA_URL
import com.tokopedia.top_ads_on_boarding.view.activity.TopAdsOnBoardingActivity
import com.tokopedia.top_ads_on_boarding.view.sheet.AdsTypeEducationBottomSheet
import com.tokopedia.unifyprinciples.Typography

class AdsObjectiveFragment : TkpdBaseV4Fragment(), View.OnClickListener {
    private var educationCenter: View? = null
    private var productAds: View? = null
    private var productAdsText: Typography? = null
    private var shopAds: View? = null
    private var shopAdsText: Typography? = null
    private var iButtonProductAds: View? = null
    private var iButtonShopAds: View? = null
    private var adsObjSubTitle: Typography? = null
    override fun getScreenName(): String {
        return EMPTY_TEXT
    }

    companion object {
        fun newInstance(): AdsObjectiveFragment {
            return AdsObjectiveFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.topads_ads_objective_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setSubTitleText()
        setProductAdsText()
        setShopAdsText()
        setClickListener()
    }

    private fun setShopAdsText() {
        shopAdsText?.text =
            getSpannedText(context?.getString(R.string.topads_ads_objective_shop_card_text) ?: "")
    }

    private fun setProductAdsText() {
        productAdsText?.text = getSpannedText(
            context?.getString(R.string.topads_ads_objective_product_card_text) ?: ""
        )
    }

    private fun setSubTitleText() {
        adsObjSubTitle?.text =
            getSpannedText(context?.getString(R.string.topads_ads_objective_subtitle) ?: "")
    }

    private fun setClickListener() {
        educationCenter?.setOnClickListener(this)
        shopAds?.setOnClickListener(this)
        productAds?.setOnClickListener(this)
        iButtonProductAds?.setOnClickListener(this)
        iButtonShopAds?.setOnClickListener(this)
    }

    private fun initViews(view: View) {
        educationCenter = view.findViewById(R.id.educationCenter)
        productAds = view.findViewById(R.id.productAds)
        shopAds = view.findViewById(R.id.shopAds)
        iButtonProductAds = view.findViewById(R.id.iButtonProductAds)
        iButtonShopAds = view.findViewById(R.id.iButtonShopAds)
        adsObjSubTitle = view.findViewById(R.id.adsObjSubTitle)
        productAdsText = view.findViewById(R.id.productAdsText)
        shopAdsText = view.findViewById(R.id.shopAdsText)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.educationCenter -> {
                (activity as? TopAdsOnBoardingActivity)?.goToWebView(LEARN_TOKOPEDIA_URL)
            }
            R.id.productAds -> {
                val bundle = Bundle()
                bundle.putString(ADS_TYPE_KEY, ADS_TYPE_PRODUCT)
                val fragment = AdsTypeFragment.newInstance(bundle)
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.onBoardingFragmentContainer,
                    fragment,
                    TAG_STEPPER_FRAGMENT
                )?.addToBackStack(STEPPER_THREE)?.commit()

            }
            R.id.shopAds -> {
                val bundle = Bundle()
                bundle.putString(ADS_TYPE_KEY, ADS_TYPE_SHOP)
                val fragment = AdsTypeFragment.newInstance(bundle)
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.onBoardingFragmentContainer,
                    fragment,
                    TAG_STEPPER_FRAGMENT
                )?.addToBackStack(STEPPER_THREE)?.commit()
            }

            R.id.iButtonProductAds -> {
                val bundle = Bundle()
                bundle.putString(ADS_TYPE_KEY, ADS_TYPE_PRODUCT)
                val sheet = AdsTypeEducationBottomSheet.newInstance(bundle)
                sheet.show(childFragmentManager, TAG_BOTTOM_SHEET_FRAGMENT)
            }

            R.id.iButtonShopAds -> {
                val bundle = Bundle()
                bundle.putString(ADS_TYPE_KEY, ADS_TYPE_SHOP)
                val sheet = AdsTypeEducationBottomSheet.newInstance(bundle)
                sheet.show(childFragmentManager, TAG_BOTTOM_SHEET_FRAGMENT)
            }
        }
    }
}
