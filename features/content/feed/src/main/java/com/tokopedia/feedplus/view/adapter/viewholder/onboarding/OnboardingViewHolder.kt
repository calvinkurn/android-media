package com.tokopedia.feedplus.view.adapter.viewholder.onboarding

import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import kotlinx.android.synthetic.main.item_layout_onboarding.view.*

/**
 * @author by yoasfs on 2019-08-30
 */

class OnboardingViewHolder(v : View) : AbstractViewHolder<OnboardingViewModel>(v){
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_layout_onboarding
    }

    override fun bind(element: OnboardingViewModel) {
        initView(element)
        initViewListener(element)
    }

    private fun initView(element: OnboardingViewModel) {
        itemView.tv_onboarding_title.text =  element.titleIntro
        itemView.btn_onboarding.text = element.buttonCta

        itemView.btn_onboarding.isEnabled = false
        itemView.rv_interest_pick.layoutManager = GridLayoutManager(itemView.context, 3)
        val adapter = OnboardingAdapter(element.dataList)
        itemView.rv_interest_pick.adapter = adapter
    }

    private fun initViewListener(element: OnboardingViewModel) {

    }
}