package com.tokopedia.topads.dashboard.view.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.ListArticleRvAdapter
import com.tokopedia.topads.dashboard.view.adapter.beranda.LatestReadingTopAdsDashboardRvAdapter
import com.tokopedia.unifycomponents.UnifyButton

class ListArticleTopAdsEducationFragment : TkpdBaseV4Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnReadMore: UnifyButton
    private val adapter by lazy { ListArticleRvAdapter.createInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_list_article_topads_education, container, false
        )
        recyclerView = view.findViewById(R.id.rvArticleArticleTopAdsEducation)
        btnReadMore = view.findViewById(R.id.btnReadMoreArticleTopAdsEducation)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

    }

    override fun getScreenName(): String {
        return javaClass.name
    }

    companion object {
        fun createInstance() = ListArticleTopAdsEducationFragment()
    }
}