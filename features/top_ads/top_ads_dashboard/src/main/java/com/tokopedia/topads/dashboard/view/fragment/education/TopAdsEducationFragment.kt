package com.tokopedia.topads.dashboard.view.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.activity.TopAdsEducationActivity
import com.tokopedia.topads.dashboard.view.adapter.education.TopadsEducationRvAdapter
import com.tokopedia.topads.dashboard.viewmodel.TopAdsEducationViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class TopAdsEducationFragment : TkpdBaseV4Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = TopadsEducationRvAdapter()

    private val viewModel: TopAdsEducationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(TopAdsEducationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_list_article_topads_education, container, false
        )
        recyclerView = view.findViewById(R.id.rvArticleArticleTopAdsEducation)
        view.findViewById<UnifyButton>(R.id.btnReadMoreArticleTopAdsEducation).hide()
        view.findViewById<Typography>(R.id.txtTitleArticleTopAdsEducation).hide()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        loadData()
        initListeners()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.articlesLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    adapter.addItems(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun loadData() {
        viewModel.fetchArticles()
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun initListeners() {
        adapter.itemClick = {
            (activity as? TopAdsEducationActivity)?.addFragment(
                ListArticleTopAdsEducationFragment.createInstance(it)
            )
        }
    }

    override fun getScreenName(): String = javaClass.name

    companion object {
        fun createInstance() = TopAdsEducationFragment()
    }
}