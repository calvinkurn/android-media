package com.tokopedia.affiliate.feature.education.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.constant.AffiliateConstant
import com.tokopedia.affiliate.feature.education.view.viewmodel.EducationItemViewModel

/**
 * @author by milhamj on 14/01/19.
 */
class AffiliateEducationFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance() = AffiliateEducationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_af_education, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {

    }

    private fun initView() {

    }

    private fun getCarouselList(): MutableList<EducationItemViewModel> {
        val list = arrayListOf<EducationItemViewModel>()

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_TEASER, AffiliateConstant.JPG),
                getString(R.string.af_share_and_commission)
        ))

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_SCREENSHOT_1, AffiliateConstant.PNG),
                getString(R.string.af_choose_favorite_product),
                1
        ))

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_SCREENSHOT_2, AffiliateConstant.PNG),
                getString(R.string.af_curate_product_profile),
                2
        ))

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_SCREENSHOT_3, AffiliateConstant.PNG),
                getString(R.string.af_share_product_buy),
                3
        ))

        return list
    }

    private fun getImagePath(imageName: String, imageFormat: String): String {
        val screenDensity = DisplayMetricUtils.getScreenDensity(context)
        return String.format(
                AffiliateConstant.ANDROID_PATH_FORMAT,
                imageName,
                screenDensity,
                imageName,
                imageFormat
        )
    }
}