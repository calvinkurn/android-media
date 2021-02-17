package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.view.sheet.ManualAdsConfirmationCommonSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.data.response.AutoAdsResponse
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.AdChooserViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.topads_create_ads_chooser_fragment.*
import kotlinx.android.synthetic.main.topads_create_ads_chooser_fragment.view.*
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.view.imageView7
import javax.inject.Inject

class AdCreationChooserFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: AdChooserViewModel
    private var adStatus = 0
    private var dailyBudget = 0
    private var manualAds: UnifyButton? = null
    private var autoAds: UnifyButton? = null

    companion object {

        private const val TOGGLE_OFF = "toggle_off"
        private const val AUTO_ADS_DISABLED = 111
        private const val ACTIVE = 500
        private const val NON_ACTIVE = 600
        private const val IN_PROGRESS_200 = 200
        private const val IN_PROGRESS_300 = 300
        private const val IN_PROGRESS_400 = 400
        private const val MANUAL = 3
        private const val AUTO = 4
        private const val NO_ADS = 2

        fun newInstance(): AdCreationChooserFragment {
            val args = Bundle()
            val fragment = AdCreationChooserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AdChooserViewModel::class.java)
        return inflater.inflate(resources.getLayout(R.layout.topads_create_ads_chooser_fragment), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAdsState(this::onSuccess)
        viewModel.getAutoAdsStatus(this::onSuccessAutoAds)
    }

    private fun onSuccess(data: AdCreationOption) {
        adStatus = data.topAdsGetShopInfo.data.category
    }

    private fun onSuccessAutoAds(data: AutoAdsResponse) {
        when (data.topAdsGetAutoAds.data.status) {
            ACTIVE -> setActiveStatus()
            NON_ACTIVE -> setActiveStatus()
            else -> inProgress()
        }
        dailyBudget = data.topAdsGetAutoAds.data.dailyBudget
        when (data.topAdsGetAutoAds.data.status) {
            IN_PROGRESS_200 -> inProgress()
            IN_PROGRESS_300 -> inProgress()
            IN_PROGRESS_400 -> inProgress()
            else -> autoAdsDisableConfirm()
        }
    }

    private fun setActiveStatus() {
        autoAds?.isEnabled = true
        manualAds?.isEnabled = true
        ticker_info?.gone()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.autoAdsData.observe(viewLifecycleOwner, Observer {
            viewModel.getAutoAdsStatus(this::onSuccessAutoAds)

        })
        view.imageView7.setImageDrawable(view.context.getResDrawable(R.drawable.ill_header))
        setCaraouselView(view)
    }

    private fun setCaraouselView(view: View) {
        view.topAdsCaraousel.apply {
            slideToShow = 1.2f
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            centerMode = true
            freeMode = false

            val autoAdsView = layoutInflater.inflate(R.layout.topads_automatic_onboarding_auto_ads, null).apply {
                val image = this.findViewById<ImageUnify>(R.id.top_corner_icon1)
                image.setImageDrawable(this.context.getResDrawable(R.drawable.topads_create_ic_thumb_up))
                val topCorner = this.findViewById<ImageUnify>(R.id.top_corner_bg1)
                topCorner.setImageResource(R.drawable.topads_create_bg_top_corner)

                val icon2 = this.findViewById<ImageUnify>(R.id.icon2)
                val icon3 = this.findViewById<ImageUnify>(R.id.icon3)
                val icon4 = this.findViewById<ImageUnify>(R.id.icon4)

                icon2.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon3.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon4.setImageResource(R.drawable.topads_create_ic_checklist_blue)

                autoAds = this.findViewById(R.id.btn_start_auto_ads)
                autoAds?.setOnClickListener {
                    if (adStatus == AUTO) {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS)
                        startActivityForResult(intent, AUTO_ADS_DISABLED)
                    }
                    if (adStatus == MANUAL || adStatus == NO_ADS) {
                        RouteManager.route(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
                    }
                }
            }

            val manualAutoAds = layoutInflater.inflate(R.layout.topads_automatic_onboarding_manual_ads, null).apply {
                val image = this.findViewById<ImageUnify>(R.id.top_corner_icon2)
                image.setImageDrawable(this.context.getResDrawable(R.drawable.topads_create_ic_gear))
                val topCorner = this.findViewById<ImageUnify>(R.id.top_corner_bg2)
                topCorner.setImageResource(R.drawable.topads_create_bg_top_corner)

                val icon6 = this.findViewById<ImageUnify>(R.id.icon6)
                val icon7 = this.findViewById<ImageUnify>(R.id.icon7)
                val icon8 = this.findViewById<ImageUnify>(R.id.icon8)

                icon6.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon7.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon8.setImageResource(R.drawable.topads_create_ic_checklist_blue)

                manualAds = this.findViewById(R.id.btn_start_manual_ads)
                manualAds?.setOnClickListener {
                    if (adStatus == MANUAL || adStatus == NO_ADS) {
                        startActivity(Intent(activity, StepperActivity::class.java))
                    }
                    if (adStatus == AUTO) {
                        val sheet = ManualAdsConfirmationCommonSheet.newInstance()
                        sheet.show(childFragmentManager, "")
                        sheet.manualClick = {
                            viewModel.postAutoAds(TOGGLE_OFF, dailyBudget)
                        }
                    }
                }
            }
            addItem(autoAdsView)
            addItem(manualAutoAds)
        }
    }

    private fun autoAdsDisableConfirm() {
        ticker_info.gone()
        autoAds?.isEnabled = true
        manualAds?.isEnabled = true
    }

    private fun inProgress() {
        ticker_info.visible()
        autoAds?.isEnabled = false
        manualAds?.isEnabled = false
    }

    override fun getScreenName(): String {
        return AdCreationChooserFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTO_ADS_DISABLED) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
        viewModel.getAutoAdsStatus(this::onSuccessAutoAds)
    }
}