package com.tokopedia.affiliate.feature.education.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.constant.AffiliateConstant
import com.tokopedia.affiliate.feature.education.view.adapter.EducationAdapter
import com.tokopedia.affiliate.feature.education.view.viewmodel.EducationItemViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.fragment_af_education.*

/**
 * @author by milhamj on 14/01/19.
 */
class AffiliateEducationFragment : BaseDaggerFragment() {

    lateinit var adapter: EducationAdapter

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
        initListener()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {

    }

    private fun initView() {
        imageBg.loadImageWithoutPlaceholder(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_EDUCATION_BG)
        )

        adapter = EducationAdapter(getCarouselList())
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun initListener() {
        tabLayout.onTabSelected {
            if (it.position == adapter.list.size - 1) {
                skipText.hide()
                endBtn.show()

            } else {
                skipText.show()
                endBtn.hide()
            }
        }

        skipText.setOnClickListener {
            activity?.finish()
        }

        endBtn.setOnClickListener {
            activity?.finish()
        }
    }

    private fun getCarouselList(): MutableList<EducationItemViewModel> {
        val list = arrayListOf<EducationItemViewModel>()

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_ILLUSTRATION),
                getString(R.string.af_share_and_commission)
        ))

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_SCREENSHOT_1),
                getString(R.string.af_choose_favorite_product),
                1
        ))

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_SCREENSHOT_2),
                getString(R.string.af_curate_product_profile),
                2
        ))

        list.add(EducationItemViewModel(
                getImagePath(AffiliateConstant.IMG_AFFILIATE_SCREENSHOT_3),
                getString(R.string.af_share_product_buy),
                3
        ))

        return list
    }

    private fun getImagePath(imageName: String, imageFormat: String = AffiliateConstant.PNG)
            : String {

        val screenDensity = DisplayMetricUtils.getScreenDensity(context)
        return String.format(
                AffiliateConstant.ANDROID_DRAWABLE_PATH_FORMAT,
                imageName,
                screenDensity,
                imageName,
                imageFormat
        )
    }
}