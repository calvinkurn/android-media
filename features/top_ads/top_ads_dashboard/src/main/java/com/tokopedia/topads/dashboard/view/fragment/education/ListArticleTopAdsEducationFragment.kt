package com.tokopedia.topads.dashboard.view.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.ListArticle
import com.tokopedia.topads.dashboard.data.utils.Utils.openWebView
import com.tokopedia.topads.dashboard.view.adapter.education.ListArticleRvAdapter
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

const val READ_MORE_URL =
    "https://seller.tokopedia.com/edu/topic/fitur-kembangkan-toko-promosi/topads/"

class ListArticleTopAdsEducationFragment : TkpdBaseV4Fragment(), CoroutineScope {

    private val job = SupervisorJob()
    private lateinit var txtDescription: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnReadMore: UnifyButton
    private val adapter by lazy { ListArticleRvAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_list_article_topads_education, container, false
        )
        txtDescription = view.findViewById(R.id.txtTitleArticleTopAdsEducation)
        recyclerView = view.findViewById(R.id.rvArticleArticleTopAdsEducation)
        btnReadMore = view.findViewById(R.id.btnReadMoreArticleTopAdsEducation)
        return view
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadArticles()

        initClicks()
    }

    private fun loadArticles() {
        val data = arguments?.getParcelable<ListArticle.ListArticleItem>(ARTICLES)
        data?.let { initView(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initClicks() = context?.run {
        adapter.itemClick = { openWebView(it) }

        btnReadMore.setOnClickListener { openWebView(READ_MORE_URL) }
    }

    private fun initView(data: ListArticle.ListArticleItem) {
        if (data.description.isNullOrEmpty()) txtDescription.hide()
        else txtDescription.text = data.description

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.addItems(data.articles)
    }

    override fun getScreenName(): String {
        return javaClass.name
    }

    companion object {
        private const val ARTICLES = "articles"
        fun createInstance(listArticleItem: ListArticle.ListArticleItem): ListArticleTopAdsEducationFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARTICLES, listArticleItem)
            val fragment = ListArticleTopAdsEducationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}