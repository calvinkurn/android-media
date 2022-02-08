package com.tokopedia.topads.dashboard.view.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.ListArticle
import com.tokopedia.topads.dashboard.data.raw.articlesJson
import com.tokopedia.topads.dashboard.view.activity.TopAdsEducationActivity
import com.tokopedia.topads.dashboard.view.adapter.TopadsEducationRvAdapter
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TopAdsEducationFragment : TkpdBaseV4Fragment() {

    private val articles by lazy { Gson().fromJson(articlesJson, ListArticle::class.java) }
    private lateinit var recyclerView: RecyclerView
    private val adapter by lazy { TopadsEducationRvAdapter.createInstance(articles) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        initListeners()
    }

    private fun initListeners() {
        adapter.itemClick = {
            (requireActivity() as? TopAdsEducationActivity)?.addFragment(
                ListArticleTopAdsEducationFragment.createInstance(it)
            )
        }
    }

    override fun getScreenName(): String = javaClass.name

    companion object {
        fun createInstance() = TopAdsEducationFragment()
    }
}