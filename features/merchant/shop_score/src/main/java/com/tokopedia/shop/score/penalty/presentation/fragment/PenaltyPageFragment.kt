package com.tokopedia.shop.score.penalty.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory
import kotlinx.android.synthetic.main.item_penalty_filter_list.view.*

class PenaltyPageFragment: BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>() {

    companion object {
        fun newInstance(): PenaltyPageFragment {
            return PenaltyPageFragment()
        }
    }

    private val penaltyPageAdapterFactory by lazy { PenaltyPageAdapterFactory() }
    private val penaltyPageAdapter by lazy { PenaltyPageAdapter(penaltyPageAdapterFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_penalty_page, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java).inject(this)
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvPenaltyPage)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        //no op
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory> {
        return penaltyPageAdapter
    }

    override fun loadData(page: Int) {

    }

    override fun getAdapterTypeFactory(): PenaltyPageAdapterFactory {
        return penaltyPageAdapterFactory
    }
}