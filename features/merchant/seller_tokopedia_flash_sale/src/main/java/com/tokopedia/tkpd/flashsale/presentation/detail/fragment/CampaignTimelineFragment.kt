package com.tokopedia.tkpd.flashsale.presentation.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentCampaignTimelineBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.common.adapter.VerticalSpaceItemDecoration
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail.TimelineProcessAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignTimelineFragment: BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_TIMELINE_STEP_MODEL = "TimelineStepModel"
        @JvmStatic
        fun newInstance(timelineSteps: List<TimelineStepModel>): CampaignTimelineFragment {
            return CampaignTimelineFragment().apply { //TODO: use SaveInstanceCacheManager
                arguments = Bundle().apply {
                    putParcelableArrayList(BUNDLE_KEY_TIMELINE_STEP_MODEL, ArrayList(timelineSteps))
                }
            }
        }
    }

    private var binding by autoClearedNullable<StfsFragmentCampaignTimelineBinding>()
    private val timelineSteps by lazy {
        arguments?.getParcelableArrayList<TimelineStepModel>(BUNDLE_KEY_TIMELINE_STEP_MODEL)?.toList().orEmpty()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentCampaignTimelineBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupTimelineList()
    }

    override fun onResume() {
        super.onResume()
        // refresh views to re-calculate height at viewpager
        binding?.root?.requestLayout()
    }

    private fun setupTimelineList() {
        binding?.rvTimelineProcess?.apply {
            val spacingAmount = resources.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalSpaceItemDecoration(spacingAmount))
            adapter = TimelineProcessAdapter().apply {
                setDataList(timelineSteps)
            }
        }
    }
}
