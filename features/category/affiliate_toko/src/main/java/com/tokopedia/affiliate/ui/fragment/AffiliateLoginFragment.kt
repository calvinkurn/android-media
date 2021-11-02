package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.affiliate.adapter.AffiliateTutorialPagerAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.viewmodel.AffiliateLoginViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.affiliate_login_fragment_layout.*
import javax.inject.Inject

class AffiliateLoginFragment : BaseViewModelFragment<AffiliateLoginViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateLoginViewModel: AffiliateLoginViewModel

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliateLoginFragment().apply {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_login_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        afterViewCreated()
    }

    private fun setupViewPager () {
        val viewPager = view?.findViewById<TouchViewPager>(R.id.affiliate_login_view_pager)
        val tutorialArray = arrayListOf<AffiliateTutorialPagerAdapter.LoginTutorialData>().apply {
            context?.apply {
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.tutorial_title_1),
                        getString(R.string.tutorial_subtitle_1),
                        "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_never_bought_product.png"))
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.tutorial_title_2),
                        getString(R.string.tutorial_subtitle_2),
                        "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_product_not_found.png"))
                add(AffiliateTutorialPagerAdapter.LoginTutorialData(
                        getString(R.string.tutorial_title_3),
                        getString(R.string.tutorial_subtitle_3),
                        "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_never_bought_product.png"))
            }
        }
        val tutorialAdapter = AffiliateTutorialPagerAdapter(tutorialArray)
        viewPager?.adapter = tutorialAdapter
        viewPager?.setCurrentItem(0, true)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                affiliate_login_page_control.setCurrentIndicator(position)
            }
        })

        affiliate_login_page_control.apply {
            inactiveColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N75)
            setIndicator(tutorialArray.size)
        }
    }

    private fun afterViewCreated() {

    }

    private fun setObservers() {

    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectLoginFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getViewModelType(): Class<AffiliateLoginViewModel> {
        return AffiliateLoginViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateLoginViewModel = viewModel as AffiliateLoginViewModel
    }
}