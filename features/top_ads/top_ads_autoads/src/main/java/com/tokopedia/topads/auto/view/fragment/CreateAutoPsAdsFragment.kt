package com.tokopedia.topads.auto.view.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.auto.databinding.TopadsAutoadsPsLayoutBinding
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.topads.common.R as topadscommonR

class CreateAutoPsAdsFragment : BaseDaggerFragment(), View.OnClickListener{

    private var binding: TopadsAutoadsPsLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopadsAutoadsPsLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupTooltip(view)
    }

    private fun setupTooltip(view: View) {
        val tooltipView =
            layoutInflater.inflate(topadscommonR.layout.tooltip_custom_view, null)
                .apply {
                    val tvToolTipText = this.findViewById<Typography>(topadscommonR.id.tooltip_text)
                    tvToolTipText?.text = getString(topadscommonR.string.topads_common_daily_budget_tips)

                    val imgTooltipIcon = this.findViewById<ImageUnify>(topadscommonR.id.tooltip_icon)
                    imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(topadscommonR.drawable.topads_ic_tips))

                    val container = this.findViewById<ConstraintLayout>(topadscommonR.id.container)
                    container.background = ColorDrawable(
                        ContextCompat.getColor(
                        this@CreateAutoPsAdsFragment.requireContext(),
                            unifyprinciplesR.color.Unify_YN100
                    ))
                }
        binding?.tipBtn?.addItem(tooltipView)
    }

    companion object {
        fun newInstance(): CreateAutoPsAdsFragment {
            return CreateAutoPsAdsFragment()
        }
    }

    override fun getScreenName(): String {
        return CreateAutoPsAdsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){

        }
    }
}
