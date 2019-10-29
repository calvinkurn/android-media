package com.tokopedia.feedplus.view.adapter.viewholder.onboarding

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_layout_onboarding.view.*

/**
 * @author by yoasfs on 2019-08-30
 */

class OnboardingViewHolder(
        v : View,
        val userSession: UserSessionInterface,
        val listener: OnboardingAdapter.InterestPickItemListener)
    : AbstractViewHolder<OnboardingViewModel>(v), OnboardingAdapter.InterestPickItemListener by listener {

    var countMinimumPick = 0

    companion object {
        val PAYLOAD_UPDATE_ADAPTER = 1234
        @LayoutRes
        val LAYOUT = R.layout.item_layout_onboarding
    }

    lateinit var adapter: OnboardingAdapter

    override fun bind(element: OnboardingViewModel) {
        initView(element)
        initViewListener(element)
    }

    override fun bind(element: OnboardingViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        when (payloads[0] as Int) {
            PAYLOAD_UPDATE_ADAPTER -> initView(element)
            else -> bind(element)
        }
    }

    private fun initView(element: OnboardingViewModel) {
        countMinimumPick = element.minimumPick
        ImageHandler.loadImageCircle2(itemView.context, itemView.iv_onboarding_user, userSession.profilePicture)
        itemView.tv_onboarding_instruction.text = element.instruction
        itemView.tv_onboarding_user.text = String.format(getString(R.string.feed_onboarding_name_format), userSession.name)
        itemView.tv_onboarding_title.text =  element.titleIntro
        itemView.btn_onboarding.text = element.buttonCta

        itemView.rv_interest_pick.layoutManager = GridLayoutManager(itemView.context, 3)

        adapter = OnboardingAdapter(this, OnboardingAdapter.SOURCE_FEED)
        adapter.setList(element.dataList)
        itemView.rv_interest_pick.adapter = adapter
        itemView.rv_interest_pick.addItemDecoration(OnboardingAdapter.getItemDecoration())
        updateButtonCheckRecommendation()
    }

    private fun initViewListener(element: OnboardingViewModel) {
        itemView.btn_onboarding.setOnClickListener {
            listener.onCheckRecommendedProfileButtonClicked(adapter.getSelectedItems())
        }
    }

    private fun updateButtonCheckRecommendation() {
        itemView.btn_onboarding.isEnabled = countMinimumPick <= adapter.getSelectedItemIdList().size
    }


    override fun onInterestPickItemClicked(item: OnboardingDataViewModel) {
        updateButtonCheckRecommendation()
        listener.onInterestPickItemClicked(item)
    }
}