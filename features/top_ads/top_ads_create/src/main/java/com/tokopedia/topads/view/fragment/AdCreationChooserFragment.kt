package com.tokopedia.topads.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.data.response.AutoAdsResponse
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.AdChooserViewModel
import com.tokopedia.topads.view.sheet.ManualAdsConfirmationSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import kotlinx.android.synthetic.main.topads_create_ads_chooser_fragment.*
import kotlinx.android.synthetic.main.topads_create_ads_chooser_fragment.view.*
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.view.*
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.view.imageView7
import javax.inject.Inject

class AdCreationChooserFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: AdChooserViewModel
    private var adStatus = 0
    private var dailyBudget = 0
    private var current_auto_ads_status = 0
    private var manual_ads: UnifyButton? = null
    private var auto_ads: UnifyButton? = null

    companion object {

        private const val TOGGLE_OFF = "toggle_off"
        private const val AUTO_ADS_DISABLED = 111
        private const val ACTIVE = 500
        private const val NON_ACTIVE = 600
        private const val IN_PROGRESS_200 = 200
        private const val IN_PROGRESS_300 = 300
        private const val IN_PROGRESS_400 = 400
        private const val MANAUAL = 3
        private const val AUTO = 4
        private const val NO_ADS = 2
        private const val NO_PRODUCT = 1

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
            ACTIVE -> setActiveStatus(R.string.ads_active, R.drawable.active_status_green)
            NON_ACTIVE -> setActiveStatus(R.string.ads_not_delivered, R.drawable.active_status_orange)
            else -> {
//                tv_shop_status.text = ""
//                tv_shop_status.setBackgroundResource(0)
            }
        }

        dailyBudget = data.topAdsGetAutoAds.data.dailyBudget
        current_auto_ads_status = data.topAdsGetAutoAds.data.status
        when (data.topAdsGetAutoAds.data.status) {
            IN_PROGRESS_200 -> inProgress()
            IN_PROGRESS_300 -> inProgress()
            IN_PROGRESS_400 -> inProgress()
            else -> autoAdsDisableConfirm()
        }
    }

    private fun setActiveStatus(adsActive: Int, bg: Int) {
//        tv_shop_status.setText(adsActive)
//        tv_shop_status.setBackgroundResource(bg)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.autoAdsData.observe(viewLifecycleOwner, Observer {
            viewModel.getAutoAdsStatus(this::onSuccessAutoAds)

        })
        view.imageView7.setImageDrawable(view.context.getResDrawable(R.drawable.ill_header))
        view.topads_choose_ads.apply {
            slideToShow = 1.2f
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            centerMode = true
            freeMode = false

            val autoAdsView = layoutInflater.inflate(R.layout.topads_automatic_onboarding_auto_ads, null).apply {
                val image = this.findViewById<ImageUnify>(R.id.top_corner_icon1)
                image.setImageDrawable(this.context.getResDrawable(R.drawable.topads_create_ic_thumb_up))
                val topCorner = this.findViewById<ImageUnify>(R.id.top_corner_bg1)
                topCorner.setImageDrawable(this.context.getResDrawable(R.drawable.topads_create_bg_top_corner))

                val icon2 = this.findViewById<ImageUnify>(R.id.icon2)
                val icon3 = this.findViewById<ImageUnify>(R.id.icon3)
                val icon4 = this.findViewById<ImageUnify>(R.id.icon4)

                icon2.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon3.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon4.setImageResource(R.drawable.topads_create_ic_checklist_blue)

                auto_ads = this.findViewById<UnifyButton>(R.id.btn_start_auto_ads)
                auto_ads?.setOnClickListener {
                    if (adStatus == AUTO) {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS)
                        startActivityForResult(intent, AUTO_ADS_DISABLED)
                    }
                    if (adStatus == MANAUAL || adStatus == NO_ADS) {
                        RouteManager.route(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
                    }
                }
            }


            val manualAutoAds = layoutInflater.inflate(R.layout.topads_automatic_onboarding_manual_ads, null).apply {
                val image = this.findViewById<ImageUnify>(R.id.top_corner_icon2)
                image.setImageDrawable(this.context.getResDrawable(R.drawable.topads_create_ic_gear))
                val topCorner = this.findViewById<ImageUnify>(R.id.top_corner_bg2)
                topCorner.setImageDrawable(this.context.getResDrawable(R.drawable.topads_create_bg_top_corner))

                val icon6 = this.findViewById<ImageUnify>(R.id.icon6)
                val icon7 = this.findViewById<ImageUnify>(R.id.icon7)
                val icon8 = this.findViewById<ImageUnify>(R.id.icon8)

                icon6.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon7.setImageResource(R.drawable.topads_create_ic_checklist_blue)
                icon8.setImageResource(R.drawable.topads_create_ic_checklist_blue)

                manual_ads = this.findViewById<UnifyButton>(R.id.btn_start_manual_ads)
                manual_ads?.setOnClickListener {
                    if (adStatus == MANAUAL || adStatus == NO_ADS) {
                        startActivity(Intent(activity, StepperActivity::class.java))
                    }
                    if (adStatus == AUTO) {
                        val sheet = ManualAdsConfirmationSheet.newInstance()
                        sheet.show(fragmentManager!!, "")
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
        ticker_info.visibility = View.GONE
        auto_ads?.isEnabled = true
        manual_ads?.isEnabled = true
        auto_ads?.alpha = 1f
        manual_ads?.alpha = 1f
    }

    private fun inProgress() {
        ticker_info.visibility = View.VISIBLE
        auto_ads?.isEnabled = false
        manual_ads?.isEnabled = false
        auto_ads?.alpha = 0.5f
        manual_ads?.alpha = 0.5f
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