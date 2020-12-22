package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.topads.common.view.adapter.tips.viewholder.TipsUiSortViewHolder
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiSortModel
import com.tokopedia.topads.common.view.adapter.viewpager.KeywordEditPagerAdapter
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_edit_ad_cost.*

private const val POSITION_KEYWORD = 0
private const val POSITION_NEGATIVE_KEYWORD = 1

class EditAdCostFragment : BaseDaggerFragment(), TipsUiSortViewHolder.OnUiSortItemClick {
    private var tipsSortListSheet: TipsListSheet? = null

    override fun getScreenName(): String {
        return EditAdCostFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    companion object {
        fun newInstance(): EditAdCostFragment = EditAdCostFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_ad_cost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderViewPager()
        setUpToolTipButton()
    }

    private fun renderViewPager() {
        keyword.chipType = ChipsUnify.TYPE_SELECTED
        keyword.setOnClickListener {
            keyword.chipType = ChipsUnify.TYPE_SELECTED
            neg_keyword.chipType = ChipsUnify.TYPE_NORMAL
            view_pager.currentItem = POSITION_KEYWORD
        }
        neg_keyword.setOnClickListener {
            neg_keyword.chipType = ChipsUnify.TYPE_SELECTED
            keyword.chipType = ChipsUnify.TYPE_NORMAL
            view_pager.currentItem = POSITION_NEGATIVE_KEYWORD
        }
        view_pager.adapter = getViewPagerAdapter()
        view_pager.disableScroll(true)
    }

    private fun getViewPagerAdapter(): KeywordEditPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        list.add(HeadlineEditKeywordFragment.getInstance(KEYWORD_POSITIVE))
        list.add(HeadlineEditKeywordFragment.getInstance(KEYWORD_NEGATIVE))
        val adapter = KeywordEditPagerAdapter(childFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    private fun setUpToolTipButton() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            val tvToolTipText = findViewById<Typography>(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.topads_headline_schedule_tooltip_text)
            val imgTooltipIcon = findViewById<ImageUnify>(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_tips))
        }
        tooltipBtn.addItem(tooltipView)
        tooltipBtn.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiSortModel(R.string.topads_headline_broad_sort_type_header, R.string.topads_headline_broad_sort_type_subheader, true))
                add(TipsUiSortModel(R.string.topads_headline_specific_sort_type_header, R.string.topads_headline_specific_sort_type_subheader))
            }
            tipsSortListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList, sortItemClick = this) }
            tipsSortListSheet?.showHeader = true
            tipsSortListSheet?.showKnob = false
            tipsSortListSheet?.setTitle(getString(R.string.topads_headline_sort_type_title))
            tipsSortListSheet?.show(childFragmentManager, "")
        }
    }

    override fun onItemClick(sortModel: TipsUiSortModel) {
        tipsSortListSheet?.getTipsList()?.forEach { model ->
            if (model is TipsUiSortModel) {
                model.isChecked = model == sortModel
            }
        }
        tipsSortListSheet?.notifyDataSetChanged()
    }
}