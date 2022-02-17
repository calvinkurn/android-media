package com.tokopedia.topads.dashboard.view.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.ListArticle
import com.tokopedia.topads.dashboard.data.utils.Utils.openWebView
import com.tokopedia.topads.dashboard.view.adapter.education.ListArticleRvAdapter
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

const val READ_MORE_URL =
    "https://seller.tokopedia.com/edu/topic/fitur-kembangkan-toko-promosi/topads/"

class ListArticleTopAdsEducationFragment : TkpdBaseV4Fragment() {

    private lateinit var txtDescription: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnReadMore: UnifyButton
    private val adapter by lazy { ListArticleRvAdapter.createInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_list_article_topads_education, container, false
        )
        txtDescription = view.findViewById(R.id.txtTitleArticleTopAdsEducation)
        recyclerView = view.findViewById(R.id.rvArticleArticleTopAdsEducation)
        btnReadMore = view.findViewById(R.id.btnReadMoreArticleTopAdsEducation)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val raw = arguments?.getString(ARTICLES)
        val data = Gson().fromJson(raw, ListArticle.ListArticleItem::class.java)

        if (data != null) {
            initView(data)
        }

        initClicks()

    }

    private fun initClicks() {
        adapter.itemClick = { requireContext().openWebView(it) }

        btnReadMore.setOnClickListener { requireContext().openWebView(READ_MORE_URL) }
    }

    private fun initView(data: ListArticle.ListArticleItem) {
        if (data.description.isEmpty()) txtDescription.hide()
        else txtDescription.text = data.description

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
            bundle.putString(ARTICLES, Gson().toJson(listArticleItem))
            val fragment = ListArticleTopAdsEducationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}